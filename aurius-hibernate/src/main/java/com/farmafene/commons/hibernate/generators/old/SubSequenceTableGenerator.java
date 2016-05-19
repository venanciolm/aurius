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
package com.farmafene.commons.hibernate.generators.old;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.TransactionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.mapping.Table;
import org.hibernate.property.DirectPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generador de hibernate
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class SubSequenceTableGenerator implements IdentifierGenerator,
		Configurable {
	public static final String SEQUENCE = "SEQUENCE";
	public static final String GENERATED_VALUE = "GENERATED_VALUE";
	private static final String ENTITY_NAME = "entity_name";
	private static final String TARGET_TABLE = "target_table";
	protected static Logger logger = LoggerFactory
			.getLogger(SubSequenceTableGenerator.class);

	@SuppressWarnings("unused")
	private Type identifierType;
	private String tableName;
	private PropertyAccessor propertyAccesor;
	private FieldsGenerador campos;
	private DataBaseName nombreSecuencias;
	private boolean tipoEmbedded;
	private Dialect dialectoBBDD;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public void configure(Type type, Properties props, Dialect dialect)
			throws MappingException {
		if (logger.isDebugEnabled()) {
			logger.debug(props.toString());
		}
		String generatedField = PropertiesHelper.getString(GENERATED_VALUE,
				props, null);
		Class<?> entityClass = null;
		try {
			entityClass = Class.forName(PropertiesHelper.getString(ENTITY_NAME,
					props, null));
		} catch (ClassNotFoundException e) {
			throw new IdentifierGenerationException(e.getMessage(), e);
		}
		dialectoBBDD = dialect;
		propertyAccesor = new DirectPropertyAccessor();
		identifierType = type;
		tableName = PropertiesHelper.getString(TARGET_TABLE, props, null);
		nombreSecuencias = new DataBaseName(PropertiesHelper.getString(
				"catalog", props, null), PropertiesHelper.getString("schema",
				props, null), PropertiesHelper.getString(SEQUENCE, props, null));
		Field[] lista = entityClass.getDeclaredFields();
		Field campoClave = null;
		Field key = null;
		for (Field f : lista) {
			if (f.getAnnotation(GeneratedValue.class) != null) {
				key = f;
				if (f.getAnnotation(EmbeddedId.class) != null) {
					tipoEmbedded = true;
					try {
						lista = f.getType().getDeclaredFields();
						campoClave = f.getType().getDeclaredField(
								generatedField);
					} catch (SecurityException e) {
						throw new IllegalArgumentException(e);
					} catch (NoSuchFieldException e) {
						throw new IllegalArgumentException(e);
					}
					break;
				} else if (f.getAnnotation(Id.class) != null) {
					tipoEmbedded = false;
					campoClave = f;
				} else {
					throw new IllegalArgumentException(
							"Se ha de configurarar Id o EmbeddedId");
				}
				break;
			}
		}
		if (campoClave == null) {
			throw new IdentifierGenerationException(
					"Debe existir un Campo que generar");
		}
		ArrayList<Field> listaCampos = new ArrayList<Field>();
		for (Field f : lista) {
			if (f.getName().equals(campoClave.getName())) {
				continue;
			}
			if (tipoEmbedded) {
				if (f.getAnnotation(Column.class) != null) {
					listaCampos.add(f);
				}
			} else {
				if (f.getAnnotation(Id.class) != null) {
					listaCampos.add(f);
				}
			}
		}
		campos = new FieldsGenerador(key, campoClave, listaCampos);
		if (logger.isDebugEnabled()) {
			logger.debug("Campos.key: " + campos.getKey());
			logger.debug("Campos.target: " + campos.getTarget());
			logger.debug("Campos.lista: " + campos.getLista());
			logger.debug("Campos.lista: " + campos.getLista().length);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Serializable generate(SessionImplementor sess, Object entity)
			throws HibernateException {
		logger.debug("La clase es: " + entity.getClass().getName());
		Object[] propiedades = new Object[0];
		Serializable key = null;
		if (campos.getLista().length > 0) {
			key = (Serializable) propertyAccesor.getGetter(entity.getClass(),
					campos.getKey().getName()).get(entity);
			propiedades = leeParametros(key);
		}
		if (campos.getTarget().getName() != null) {
			try {
				logger.debug("La clave es: "
						+ ((key == null) ? null : key.getClass().getName()));
				logger.debug("Numero Propiedades: " + propiedades.length);

				if (tipoEmbedded) {
					if (null == propertyAccesor.getGetter(
							campos.getKey().getType(),
							campos.getTarget().getName()).get((Object) key)) {

						Object id = new WorkInOther(propiedades, logger,
								dialectoBBDD, nombreSecuencias, campos)
								.doWorkInNewTransaction(sess);
						propertyAccesor.getSetter(campos.getKey().getType(),
								campos.getTarget().getName()).set((Object) key,
								id, sess.getFactory());
					} else {
						logger
								.warn("Ya se ha introducido el valor a generar!!!, utilizando ese valor");
					}
				} else {
					if (key == null) {
						Object id = new WorkInOther(propiedades, logger,
								dialectoBBDD, nombreSecuencias, campos)
								.doWorkInNewTransaction(sess);
						key = (Serializable) id;
					} else {
						// FIXME !!!, debemos reestructurar la secuencia!!!
						logger
								.warn("Ya se ha introducido el valor a generar!!!, utilizando ese valor");
					}
				}
			} catch (IllegalArgumentException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (SecurityException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			}

		}
		return key;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private Object[] leeParametros(Serializable key) {
		Object[] propiedades = new Object[campos.getLista().length];
		for (int i = 0; i < propiedades.length; i++) {
			propiedades[i] = propertyAccesor.getGetter(key.getClass(),
					campos.getLista()[i].getName()).get(key);

		}
		return propiedades;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	class DataBaseName {
		private String schemaName;
		private String catalogName;
		private String secuencia;
		public String getNombreSecuencia() {
			return getNombreCompletoTabla(secuencia);
		}

		public DataBaseName(String catalog, String schema, String secuencia) {
			this.catalogName = catalog;
			this.schemaName = schema;
			this.secuencia = secuencia;
		}

		private String getNombreCompletoTabla(String nombre) {
			if (nombre == null || "".equals(nombre.trim())) {
				return null;
			} else if (nombre.indexOf('.') < 0) {
				return Table.qualify(catalogName, schemaName, nombre);
			}
			return nombre;

		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	class FieldsGenerador {
		private Field[] lista;
		private Field target;
		private Field key;

		public FieldsGenerador(Field key, Field t, List<Field> list) {
			this.target = t;
			this.lista = list.toArray(new Field[0]);
			this.key = key;
		}

		public Field[] getLista() {
			return lista;
		}

		public Field getTarget() {
			return target;
		}

		public Field getKey() {
			return key;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	class WorkInOther extends TransactionHelper {
		private Object[] theKey;
		private Logger logger;
		private Dialect dialectoBBDD;
		private DataBaseName db;
		private FieldsGenerador campos;

		public WorkInOther(Object[] theKey, Logger logger,
				Dialect dialectoBBDD, DataBaseName db, FieldsGenerador campos) {
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
			sb.append(tableName);
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
}
