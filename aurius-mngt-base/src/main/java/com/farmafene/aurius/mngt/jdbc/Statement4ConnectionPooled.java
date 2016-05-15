/**
 * 
 */
package com.farmafene.aurius.mngt.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Statement;

import javax.sql.PooledConnection;

/**
 * @author vlopezm
 * 
 */
public class Statement4ConnectionPooled implements Statement {

	private Statement statement;
	private PooledConnectionSubject subject;

	private Statement4ConnectionPooled() {

	}

	/**
	 * Constructor
	 * 
	 * @param statement
	 * @param subject
	 */
	public Statement4ConnectionPooled(Statement statement,
			PooledConnectionSubject subject) {
		this();
		this.statement = statement;
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
		if (getSubject() != null) {
			sb.append("subject=");
			sb.append(getSubject());
			sb.append(", ");
		}
		if (statement != null) {
			sb.append("statement=");
			sb.append(statement);
		}
		sb.append("}");
		return sb.toString();
	}

	protected PooledConnectionSubject getSubject() {
		return subject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return statement.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return statement.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		statementClosed(statement, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	@Override
	public int getMaxFieldSize() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMaxFieldSize();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setMaxFieldSize(max);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMaxRows();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(int max) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setMaxRows(max);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setEscapeProcessing(enable);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	@Override
	public int getQueryTimeout() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getQueryTimeout();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setQueryTimeout(seconds);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#cancel()
	 */
	@Override
	public void cancel() throws SQLException {
		SQLException sqle = null;
		try {
			statement.cancel();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getWarnings();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		SQLException sqle = null;
		try {
			statement.clearWarnings();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	@Override
	public void setCursorName(String name) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setCursorName(name);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	@Override
	public boolean execute(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSet();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getUpdateCount()
	 */
	@Override
	public int getUpdateCount() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getUpdateCount();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMoreResults();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(int direction) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setFetchDirection(direction);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getFetchDirection();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(int rows) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setFetchSize(rows);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getFetchSize();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	@Override
	public int getResultSetConcurrency() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetConcurrency();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetType()
	 */
	@Override
	public int getResultSetType() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetType();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(String sql) throws SQLException {
		SQLException sqle = null;
		try {
			statement.addBatch(sql);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		SQLException sqle = null;
		try {
			statement.clearBatch();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeBatch();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return ((PooledConnection) subject).getConnection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	@Override
	public boolean getMoreResults(int current) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getMoreResults(current);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getGeneratedKeys();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, autoGeneratedKeys);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	@Override
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, columnIndexes);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#executeUpdate(java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		SQLException sqle = null;
		try {
			return statement.executeUpdate(sql, columnNames);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	@Override
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, autoGeneratedKeys);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, columnIndexes);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		SQLException sqle = null;
		try {
			return statement.execute(sql, columnNames);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	@Override
	public int getResultSetHoldability() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.getResultSetHoldability();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.isClosed();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		SQLException sqle = null;
		try {
			statement.setPoolable(poolable);
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isPoolable()
	 */
	@Override
	public boolean isPoolable() throws SQLException {
		SQLException sqle = null;
		try {
			return statement.isPoolable();
		} catch (SQLException e) {
			sqle = e;
			throw sqle;
		} catch (Throwable th) {
			sqle = new SQLException(th);
			throw sqle;
		} finally {
			if (null != sqle) {
				statementErrorOccurred(this, sqle);
			}
		}
	}

	private void statementClosed(Statement stmt, SQLException e)
			throws SQLException {
		if (stmt instanceof PreparedStatement) {
			subject.statementClosed((PreparedStatement) stmt, e);
		} else {
			SQLException sqle = null;
			try {
				stmt.close();
			} catch (SQLException ex) {
				sqle = ex;
				throw sqle;
			} catch (Throwable th) {
				sqle = new SQLException(th);
				throw sqle;
			}
		}
	}

	protected void statementErrorOccurred(Statement stmt, SQLException e) {
		if (stmt instanceof PreparedStatement) {
			subject.statementErrorOccurred((PreparedStatement) stmt, e);
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
	 * @see java.sql.CallableStatement#getObject(int, java.lang.Class)
	 */
	public <T> T getObject(int parameterIndex, Class<T> type)
			throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.CallableStatement#getObject(java.lang.String,
	 *      java.lang.Class)
	 */
	public <T> T getObject(String parameterName, Class<T> type)
			throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}
}
