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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.farmafene.aurius.mngt.AuriusResource;

public class StatementDecorator implements Statement {

	private Statement inner;
	private List<String> batch;
	private ConnectionDecorator con;

	public StatementDecorator(ConnectionDecorator con, Statement statement) {
		if (statement == null || con == null) {
			throw new IllegalArgumentException("StatementDecorator(" + con
					+ ", " + statement + "); No se admiten parámetros nulos.");
		}
		this.con = con;
		this.inner = statement;
		this.batch = new LinkedList<String>();
	}

	protected AuriusResource getAuriusResource() {
		return con.getAuriusResource();
	}

	public void addBatch(String s) throws SQLException {
		inner.addBatch(s);
		batch.add(s);
	}

	public void cancel() throws SQLException {
		inner.cancel();
	}

	public void clearBatch() throws SQLException {
		inner.clearBatch();
		batch.clear();
	}

	public void clearWarnings() throws SQLException {
		inner.clearWarnings();
	}

	public void close() throws SQLException {
		inner.close();
	}

	public boolean execute(String s) throws SQLException {
		boolean salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.execute(s);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public boolean execute(String s, int i) throws SQLException {
		boolean salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.execute(s, i);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public boolean execute(String s, int[] ai) throws SQLException {
		boolean salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.execute(s, ai);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public boolean execute(String s, String[] as) throws SQLException {
		boolean salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.execute(s, as);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public int[] executeBatch() throws SQLException {
		int[] salida = null;
		JdbcBatchStatus r = new JdbcBatchStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, new LinkedList<String>(
						batch));
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeBatch();
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public ResultSet executeQuery(String s) throws SQLException {
		ResultSet salida = null;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeQuery(s);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public int executeUpdate(String s) throws SQLException {
		int salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeUpdate(s);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public int executeUpdate(String s, int i) throws SQLException {
		int salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeUpdate(s, i);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public int executeUpdate(String s, int[] ai) throws SQLException {
		int salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeUpdate(s, ai);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public int executeUpdate(String s, String[] as) throws SQLException {
		int salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, s);
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeUpdate(s, as);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		return salida;
	}

	public Connection getConnection() throws SQLException {
		return con;
	}

	public int getFetchDirection() throws SQLException {
		return inner.getFetchDirection();
	}

	public int getFetchSize() throws SQLException {
		return inner.getFetchSize();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return inner.getGeneratedKeys();
	}

	public int getMaxFieldSize() throws SQLException {
		return inner.getMaxFieldSize();
	}

	public int getMaxRows() throws SQLException {
		return inner.getMaxRows();
	}

	public boolean getMoreResults() throws SQLException {
		return inner.getMoreResults();
	}

	public boolean getMoreResults(int i) throws SQLException {
		return inner.getMoreResults(i);
	}

	public int getQueryTimeout() throws SQLException {
		return inner.getQueryTimeout();
	}

	public ResultSet getResultSet() throws SQLException {
		return inner.getResultSet();
	}

	public int getResultSetConcurrency() throws SQLException {
		return inner.getResultSetConcurrency();
	}

	public int getResultSetHoldability() throws SQLException {
		return inner.getResultSetHoldability();
	}

	public int getResultSetType() throws SQLException {
		return inner.getResultSetType();
	}

	public int getUpdateCount() throws SQLException {
		return inner.getUpdateCount();
	}

	public SQLWarning getWarnings() throws SQLException {
		return inner.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return inner.isClosed();
	}

	public boolean isPoolable() throws SQLException {
		return inner.isPoolable();
	}

	public void setCursorName(String s) throws SQLException {
		inner.setCursorName(s);
	}

	public void setEscapeProcessing(boolean flag) throws SQLException {
		inner.setEscapeProcessing(flag);
	}

	public void setFetchDirection(int i) throws SQLException {
		inner.setFetchDirection(i);
	}

	public void setFetchSize(int i) throws SQLException {
		inner.setFetchSize(i);
	}

	public void setMaxFieldSize(int i) throws SQLException {
		inner.setMaxFieldSize(i);
	}

	public void setMaxRows(int i) throws SQLException {
		inner.setMaxRows(i);
	}

	public void setPoolable(boolean flag) throws SQLException {
		inner.setPoolable(flag);
	}

	public void setQueryTimeout(int i) throws SQLException {
		inner.setQueryTimeout(i);
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
