/*
 * Copyright (c) 2009-2014 farmafene.com
 * All rights reserved.
 * 
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * 
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * 
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.farmafene.commons.hibernate.generators;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;

import org.hibernate.LockMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.TransactionHelper;
import org.hibernate.id.IdentifierGenerationException;
import org.slf4j.Logger;

import com.farmafene.commons.hibernate.generators.SubSequenceTableGenerator.DataBaseName;

/**
 * {@inheritDoc}
 * 
 * @since 1.0.0
 */
class WorkInOther extends TransactionHelper {
	/**
	 * 
	 */
	private final SubSequenceTableGenerator WorkInOther;

	private Object[] theKey;

	private Logger logger;

	private Dialect dialectoBBDD;

	private DataBaseName db;

	private FieldsGenerador campos;

	public WorkInOther(SubSequenceTableGenerator subSequenceTableGeneratorV2, Object[] theKey, Logger logger,
			Dialect dialectoBBDD, DataBaseName db, FieldsGenerador campos) {
		WorkInOther = subSequenceTableGeneratorV2;
		this.theKey = theKey;
		this.logger = logger;
		this.dialectoBBDD = dialectoBBDD;
		this.db = db;
		this.campos = campos;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	protected Serializable doWorkInCurrentTransaction(Connection conn,
			String sqlNOUSADO) throws SQLException {
		try {
			Serializable salida = null;
			try {
				salida = getNextValue(conn, theKey);
			} catch (SQLException e) {
				logger.error("getNextValue SQLException: ", e);
				throw new UnsupportedOperationException(e);
			}
			updateNextValue(conn, salida, theKey);
			logger.debug(String.format("NextValue (%1$s): %2$s", salida
					.getClass().getName(), salida.toString()));
			return salida;

		} catch (IdentifierGenerationException e) {
			throw e;
		} finally {
			// DO NOTHING
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private void updateNextValue(Connection conn, Serializable id,
			Object[] theKey) throws SQLException {
		String sql = null;
		PreparedStatement qps = null;
		try {
			sql = getQueryUpdate();
			logger.debug(sql);
			qps = conn.prepareStatement(sql);
			qps.setString(1, id.toString());
			cargaValores(qps, 2, theKey);
			qps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error al recuperar el identificador", e);
			throw e;
		} finally {
			if (qps != null) {
				try {
					qps.close();
				} catch (Throwable e) {

				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private Serializable getNextValue(Connection conn, Object[] theKey)
			throws SQLException {
		PreparedStatement qps = null;
		PreparedStatement qps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String result = null;
		String sql = null;
		Serializable id = null;
		try {
			sql = getSelectQuery(dialectoBBDD);
			logger.debug(sql);
			qps = conn.prepareStatement(sql);
			cargaValores(qps, 1, theKey);
			rs = qps.executeQuery();
			if (!rs.next()) {
				logger
						.info("No se puede leer el siguiente valor - es necesario introducir valores en la tabla: "
								+ db.getNombreSecuencia());
				qps2 = conn.prepareStatement(getInsertQuery(dialectoBBDD));
				int index = cargaValores(qps2, 1, theKey);
				cargaValores(qps2, index, theKey);
				qps2.executeUpdate();
				rs2 = qps.executeQuery();
				if (rs2.next()) {
					result = rs2.getString(1);
				} else {
					throw new IdentifierGenerationException(
							"No se ha podido generar el identificador");
				}
			} else {
				result = rs.getString(1);
			}
			if (result == null) {
				throw new IdentifierGenerationException(
						"Identificador no encontrado");
			}
			try {
				id = (Serializable) campos.getTarget().getType()
						.getConstructor(String.class).newInstance(result);
				if ("0".equals(id.toString())) {
					throw new SQLException(
							"Error en la generación del identificador");
				}
			} catch (IllegalArgumentException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (SecurityException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (NoSuchMethodException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			}
			return id;
		} catch (SQLException e) {
			throw new SQLException("Identificador no encontrado", e);
		} finally {
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (Exception e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (qps2 != null) {
				try {
					qps2.close();
				} catch (Exception e) {
				}
			}
			if (qps != null) {
				try {
					qps.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private String getSelectQuery(Dialect dialect) {
		StringBuilder sb = new StringBuilder("SELECT ").append(campos
				.getTarget().getAnnotation(Column.class).name());
		sb.append(" FROM ");
		sb.append(dialect.appendLockHint(LockMode.UPGRADE, db
				.getNombreSecuencia()));
		writeWhereParameters(null, sb, campos.getLista());
		sb.append(dialect.getForUpdateString());
		return sb.toString();
	}

	private String getInsertQuery(Dialect dialect) {
		String initValue = "1";
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(db.getNombreSecuencia());
		sb.append(" (");
		for (Field f : campos.getLista()) {
			sb.append(f.getAnnotation(Column.class).name());
			sb.append(",");
		}
		sb.append(campos.getTarget().getAnnotation(Column.class).name());
		sb.append(") VALUES(");
		for (@SuppressWarnings("unused")
		Field f : campos.getLista()) {
			sb.append("?,");
		}
		sb.append("(SELECT CASE WHEN MAX(");
		sb.append(campos.getTarget().getAnnotation(Column.class).name());
		sb.append(") IS NULL THEN ");
		sb.append(initValue);
		sb.append(" ELSE MAX(");
		sb.append(campos.getTarget().getAnnotation(Column.class).name());
		sb.append(")+1 END FROM ");
		sb.append(WorkInOther.tableName);
		writeWhereParameters(null, sb, campos.getLista());
		sb.append(")");
		sb.append(")");
		String salida = sb.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getInsertQuery(): >" + salida + "<");
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	void writeWhereParameters(String prefix, StringBuilder sb,
			Field[] listaCampos) {
		boolean where;
		where = true;
		for (Field f : listaCampos) {
			sb.append(where ? " WHERE " : " AND ");
			if (prefix != null) {
				sb.append(prefix).append(".");
			}
			sb.append(f.getAnnotation(Column.class).name());
			sb.append("=?");
			where = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private int cargaValores(PreparedStatement qps, int indexInsert,
			Object[] theKey) throws SQLException {
		for (Object f : theKey) {
			logger.debug(String.format("parámetro[%1$d]: >%2$s<",
					indexInsert, f));
			qps.setObject(indexInsert++, f);
		}
		return indexInsert;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private String getQueryUpdate() {
		StringBuilder sb = new StringBuilder("UPDATE ").append(db
				.getNombreSecuencia());
		sb.append(" SET ").append(
				campos.getTarget().getAnnotation(Column.class).name())
				.append("=(?+1) ");
		writeWhereParameters(null, sb, campos.getLista());
		return sb.toString();
	}
}