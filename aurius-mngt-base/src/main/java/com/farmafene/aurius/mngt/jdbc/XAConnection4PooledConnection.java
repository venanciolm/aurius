package com.farmafene.aurius.mngt.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

public class XAConnection4PooledConnection implements XAConnection {

	private PooledConnection connection;
	private XAResource xaResource;

	private XAConnection4PooledConnection() {

	}

	public XAConnection4PooledConnection(PooledConnection pconn)
			throws SQLException {
		this();
		this.connection = pconn;
		this.xaResource = new XAResourceImpl(getConnection());

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return connection.getConnection();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#close()
	 */
	@Override
	public void close() throws SQLException {
		connection.close();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		connection.addConnectionEventListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.
	 *      ConnectionEventListener)
	 */
	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		connection.removeConnectionEventListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		connection.addStatementEventListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.
	 *      StatementEventListener)
	 */
	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		connection.removeStatementEventListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.XAConnection#getXAResource()
	 */
	@Override
	public XAResource getXAResource() throws SQLException {
		return xaResource;
	}
}
