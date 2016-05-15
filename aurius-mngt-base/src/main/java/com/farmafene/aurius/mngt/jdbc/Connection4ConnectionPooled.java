/**
 * 
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

/**
 * @author vlopezm
 * 
 */
public class Connection4ConnectionPooled implements Connection {

	private PooledConnectionSubject subject;
	private Connection connection;

	private Connection4ConnectionPooled() {

	}

	/**
	 * Constructor
	 * 
	 * @param connection
	 * @param subject
	 */
	public Connection4ConnectionPooled(Connection connection,
			PooledConnectionSubject subject) {
		this();
		this.connection = connection;
		this.subject = subject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		if (subject != null) {
			sb.append("subject=");
			sb.append(subject);
			sb.append(", ");
		}
		if (connection != null) {
			sb.append("connection=");
			sb.append(connection);
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return connection.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return connection.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	@Override
	public Statement createStatement() throws SQLException {
		SQLException sqle = null;
		try {
			Statement stmt = connection.createStatement();
			Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			CallableStatement cstmt = connection.prepareCall(sql);
			CallableStatement wrapper = new CallableStatement4ConnectionPooled(
					cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	@Override
	public String nativeSQL(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.nativeSQL(sql);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	@Override
	public boolean getAutoCommit() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getAutoCommit();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#commit()
	 */
	@Override
	public void commit() throws SQLException {
		SQLException sqle = null;
		try {
			connection.commit();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	@Override
	public void rollback() throws SQLException {
		SQLException sqle = null;
		try {
			connection.rollback();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		subject.connectionClosed(null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isClosed();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getMetaData()
	 */
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getMetaData();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setReadOnly(readOnly);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isReadOnly();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	@Override
	public void setCatalog(String catalog) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setCatalog(catalog);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getCatalog()
	 */
	@Override
	public String getCatalog() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getCatalog();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setTransactionIsolation(level);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	@Override
	public int getTransactionIsolation() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getTransactionIsolation();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getWarnings();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			connection.clearWarnings();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		SQLException sqle = null;
		try {
			Statement stmt = connection.createStatement(resultSetType,
					resultSetConcurrency);
			Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,
					resultSetType, resultSetConcurrency);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		SQLException sqle = null;
		try {
			CallableStatement cstmt = connection.prepareCall(sql,
					resultSetType, resultSetConcurrency);
			CallableStatement wrapper = new CallableStatement4ConnectionPooled(
					cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getTypeMap()
	 */
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getTypeMap();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setTypeMap(map);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setHoldability(int)
	 */
	@Override
	public void setHoldability(int holdability) throws SQLException {
		SQLException sqle = null;
		try {
			connection.setHoldability(holdability);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getHoldability();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setSavepoint()
	 */
	@Override
	public Savepoint setSavepoint() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.setSavepoint();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.setSavepoint(name);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		SQLException sqle = null;
		try {
			connection.rollback(savepoint);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		SQLException sqle = null;
		try {
			connection.releaseSavepoint(savepoint);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		SQLException sqle = null;
		try {
			Statement stmt = connection.createStatement(resultSetType,
					resultSetConcurrency, resultSetHoldability);
			Statement wrapper = new Statement4ConnectionPooled(stmt, subject);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int,
	 *      int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,
					resultSetType, resultSetConcurrency, resultSetHoldability);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		SQLException sqle = null;
		try {
			CallableStatement cstmt = connection.prepareCall(sql,
					resultSetType, resultSetConcurrency, resultSetHoldability);
			CallableStatement wrapper = new CallableStatement4ConnectionPooled(
					cstmt, subject);
			subject.addStatement(cstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,
					autoGeneratedKeys);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,
					columnIndexes);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		SQLException sqle = null;
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql,
					columnNames);
			PreparedStatement wrapper = new PreparedStatement4ConnectionPooled(
					pstmt, subject);
			subject.addStatement(pstmt);
			return wrapper;
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createClob()
	 */
	@Override
	public Clob createClob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createClob();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createBlob()
	 */
	@Override
	public Blob createBlob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createBlob();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createNClob()
	 */
	@Override
	public NClob createNClob() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createNClob();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createSQLXML()
	 */
	@Override
	public SQLXML createSQLXML() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createSQLXML();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#isValid(int)
	 */
	@Override
	public boolean isValid(int timeout) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.isValid(timeout);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		SQLClientInfoException sqle = null;
		try {
			connection.setClientInfo(name, value);
		} catch (SQLClientInfoException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLClientInfoException();
			sqle.initCause(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		SQLClientInfoException sqle = null;
		try {
			connection.setClientInfo(properties);
		} catch (SQLClientInfoException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLClientInfoException();
			sqle.initCause(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	@Override
	public String getClientInfo(String name) throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getClientInfo(name);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#getClientInfo()
	 */
	@Override
	public Properties getClientInfo() throws SQLException {
		SQLException sqle = null;
		try {
			return connection.getClientInfo();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createArrayOf(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createArrayOf(typeName, elements);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Connection#createStruct(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		SQLException sqle = null;
		try {
			return connection.createStruct(typeName, attributes);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				subject.connectionErrorOccurred(sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
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
