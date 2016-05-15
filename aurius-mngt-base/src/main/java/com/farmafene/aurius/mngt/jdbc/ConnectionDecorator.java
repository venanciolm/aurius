/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.mngt.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.farmafene.aurius.mngt.AuriusResource;

public class ConnectionDecorator implements Connection {

	private AuriusResource resource;
	private Connection inner;

	public ConnectionDecorator(AuriusResource resource, Connection con) {
		if (con == null || resource == null) {
			throw new IllegalArgumentException("XAConnectionDecorator("
					+ resource + "," + con
					+ "); No se admiten parámetros nulos.");
		}
		this.resource = resource;
		this.inner = con;
	}

	protected AuriusResource getAuriusResource() {
		return resource;
	}

	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public void close() throws SQLException {
		JdbcConnectionStatus r = new JdbcConnectionStatus(resource, "close");
		Throwable thStatus = null;
		try {
			r.start();
			inner.close();
		} catch (RuntimeException e) {
			thStatus = e;
			throw new SQLException(e);
		} catch (SQLException e) {
			thStatus = e;
			throw e;
		} catch (Throwable e) {
			thStatus = e;
			throw new SQLException(e);
		} finally {
			r.stop(thStatus);
		}
	}

	public void commit() throws SQLException {
		JdbcConnectionStatus r = new JdbcConnectionStatus(resource, "commit");
		Throwable thStatus = null;
		try {
			r.start();
			inner.commit();
		} catch (RuntimeException e) {
			thStatus = e;
			throw new SQLException(e);
		} catch (SQLException e) {
			thStatus = e;
			throw e;
		} catch (Throwable e) {
			thStatus = e;
			throw new SQLException(e);
		} finally {
			r.stop(thStatus);
		}
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return inner.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return inner.createBlob();
	}

	public Clob createClob() throws SQLException {
		return inner.createClob();
	}

	public NClob createNClob() throws SQLException {
		return inner.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return inner.createSQLXML();
	}

	public Statement createStatement() throws SQLException {
		return new StatementDecorator(this, inner.createStatement());
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return new StatementDecorator(this, inner.createStatement(
				resultSetType, resultSetConcurrency));
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new StatementDecorator(this, inner.createStatement(

		resultSetType, resultSetConcurrency, resultSetHoldability));
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return inner.createStruct(typeName, attributes);
	}

	public boolean getAutoCommit() throws SQLException {
		return inner.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return inner.getCatalog();
	}

	public Properties getClientInfo() throws SQLException {
		return inner.getClientInfo();
	}

	public String getClientInfo(String name) throws SQLException {
		return inner.getClientInfo(name);
	}

	public int getHoldability() throws SQLException {
		return inner.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return inner.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return inner.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return inner.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return inner.isReadOnly();
	}

	public boolean isValid(int timeout) throws SQLException {
		return inner.isValid(timeout);
	}

	public String nativeSQL(String sql) throws SQLException {
		return inner.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return new CallableStatementDecorator(this, inner.prepareCall(sql), sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new CallableStatementDecorator(this, inner.prepareCall(sql,
				resultSetType, resultSetConcurrency), sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new CallableStatementDecorator(this, inner.prepareCall(sql,
				resultSetType, resultSetConcurrency, resultSetHoldability), sql);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new PreparedStatementDecorator(this,
				inner.prepareStatement(sql), sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return new PreparedStatementDecorator(this, inner.prepareStatement(sql,
				autoGeneratedKeys), sql);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return new PreparedStatementDecorator(this, inner.prepareStatement(sql,
				columnIndexes), sql);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return new PreparedStatementDecorator(this, inner.prepareStatement(sql,
				columnNames), sql);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new PreparedStatementDecorator(this, inner.prepareStatement(sql,
				resultSetType, resultSetConcurrency), sql);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new PreparedStatementDecorator(this, inner.prepareStatement(sql,
				resultSetType, resultSetConcurrency, resultSetHoldability), sql);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		inner.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException {
		JdbcConnectionStatus r = new JdbcConnectionStatus(resource, "rollback");
		Throwable thStatus = null;
		try {
			r.start();
			inner.rollback();
		} catch (RuntimeException e) {
			thStatus = e;
			throw new SQLException(e);
		} catch (SQLException e) {
			thStatus = e;
			throw e;
		} catch (Throwable e) {
			thStatus = e;
			throw new SQLException(e);
		} finally {
			r.stop(thStatus);
		}
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		inner.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		inner.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws SQLException {
		inner.setCatalog(catalog);

	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		inner.setClientInfo(properties);
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		inner.setClientInfo(name, value);
	}

	public void setHoldability(int holdability) throws SQLException {
		inner.setHoldability(holdability);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		inner.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return inner.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return inner.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		inner.setTransactionIsolation(level);
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		inner.setTypeMap(map);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		if (iface == null) {
			throw new SQLException("la clase no puede ser null");
		}
		return iface.isAssignableFrom(inner.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (!isWrapperFor(iface)) {
			throw new SQLException("Not asignable");
		}
		return (T) inner;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setSchema(java.lang.String)
	 */
	public void setSchema(String schema) throws SQLException {
		throw new SQLFeatureNotSupportedException("Not supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getSchema()
	 */
	public String getSchema() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
	 */
	public void abort(Executor executor) throws SQLException {
		throw new SQLFeatureNotSupportedException("Not supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor,
	 *      int)
	 */
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		throw new SQLFeatureNotSupportedException("Not supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getNetworkTimeout()
	 */
	public int getNetworkTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not supported");
	}
}
