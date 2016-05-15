/**
 * 
 */
package com.farmafene.aurius.mngt.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;

/**
 * @author vlopezm
 * 
 */
public class Connection2PooledConnection implements PooledConnection,
		PooledConnectionSubject, StatementEventListener {

	private Connection connection;
	private List<StatementEventListener> statementEventListeners;
	private List<ConnectionEventListener> connectionEventListeners;
	private Set<PreparedStatement> preparedStatements;

	/**
	 * 
	 */
	private Connection2PooledConnection() {
		this.statementEventListeners = new LinkedList<StatementEventListener>();
		this.connectionEventListeners = new LinkedList<ConnectionEventListener>();
		this.preparedStatements = new HashSet<PreparedStatement>();
		addStatementEventListener(this);
	}

	/**
	 * Constuctor parametrizado
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public Connection2PooledConnection(Connection connection)
			throws SQLException {
		this();
		if (connection == null) {
			throw new SQLException("Connection must be not null");
		}
		this.connection = new Connection4ConnectionPooled(connection, this);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		if (connection != null) {
			sb.append("connection=");
			sb.append(connection);
			sb.append(", ");
		}
		if (statementEventListeners != null) {
			sb.append("statementEventListeners=");
			sb.append(statementEventListeners.size());
			sb.append(", ");
		}
		if (connectionEventListeners != null) {
			sb.append("connectionEventListeners=");
			sb.append(connectionEventListeners.size());
			sb.append(", ");
		}
		if (preparedStatements != null) {
			sb.append("preparedStatements=");
			sb.append(preparedStatements.size());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return this.connection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			List<ConnectionEventListener> list = new LinkedList<ConnectionEventListener>(
					this.connectionEventListeners);
			list.add(listener);
			this.connectionEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		synchronized (connectionEventListeners) {
			List<ConnectionEventListener> list = new LinkedList<ConnectionEventListener>(
					this.connectionEventListeners);
			list.remove(listener);
			this.connectionEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		synchronized (statementEventListeners) {
			List<StatementEventListener> list = new LinkedList<StatementEventListener>(
					statementEventListeners);
			list.add(listener);
			statementEventListeners = list;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		synchronized (statementEventListeners) {
			List<StatementEventListener> list = new LinkedList<StatementEventListener>(
					statementEventListeners);
			list.remove(listener);
			statementEventListeners = list;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.jdbc.PooledConnectionSubject#connectionClosed
	 *      (javax.sql.ConnectionEvent)
	 */
	@Override
	public void connectionClosed(SQLException e) {
		List<ConnectionEventListener> list = null;
		synchronized (connectionEventListeners) {
			list = connectionEventListeners;
		}
		ConnectionEvent event = new ConnectionEvent(this, e);
		for (ConnectionEventListener l : list) {
			l.connectionClosed(event);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.jdbc.PooledConnectionSubject#
	 *      connectionErrorOccurred(javax.sql.ConnectionEvent)
	 */
	@Override
	public void connectionErrorOccurred(SQLException e) {
		List<ConnectionEventListener> list = null;
		synchronized (connectionEventListeners) {
			list = connectionEventListeners;
		}
		ConnectionEvent event = new ConnectionEvent(this, e);
		for (ConnectionEventListener l : list) {
			l.connectionErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.jdbc.PooledConnectionSubject#statementClosed
	 *      (javax.sql.StatementEvent)
	 */
	@Override
	public void statementClosed(PreparedStatement stmt, SQLException e) {
		List<StatementEventListener> list = null;
		synchronized (statementEventListeners) {
			list = statementEventListeners;
		}
		StatementEvent event = new StatementEvent(this, stmt, e);
		for (StatementEventListener l : list) {
			l.statementClosed(event);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.jdbc.PooledConnectionSubject#addStatement(java.sql.PreparedStatement)
	 */
	@Override
	public void addStatement(PreparedStatement pstmt) {
		if (null == this.preparedStatements) {
			return;
		}
		// FIXME!!! Aquí empieza el caché
		System.out.println("addStatement(" + pstmt + ")");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.StatementEventListener#statementClosed(javax.sql.StatementEvent
	 *      )
	 */
	@Override
	public void statementClosed(StatementEvent event) {
		System.out.println("statementClosed(" + event + ")");
		try {
			event.getStatement().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.StatementEventListener#statementErrorOccurred(javax.sql.StatementEvent)
	 */
	@Override
	public void statementErrorOccurred(StatementEvent event) {
		System.out.println("statementErrorOccurred(" + event + ")");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.jdbc.PooledConnectionSubject#statementErrorOccurred
	 *      (javax.sql.StatementEvent)
	 */
	@Override
	public void statementErrorOccurred(PreparedStatement stmt, SQLException e) {
		List<StatementEventListener> list = null;
		synchronized (statementEventListeners) {
			list = statementEventListeners;
		}
		StatementEvent event = new StatementEvent(this, stmt, e);
		for (StatementEventListener l : list) {
			l.statementErrorOccurred(event);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#close()
	 */
	@Override
	public void close() throws SQLException {
		SQLException th = null;
		try {
			connection.close();
		} catch (SQLException e) {
			th = e;
			throw th;
		} catch (Throwable t) {
			th = new SQLException(t);
			throw th;
		} finally {
			connection = null;
		}
	}
}
