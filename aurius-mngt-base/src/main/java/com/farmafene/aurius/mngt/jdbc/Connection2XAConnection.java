/*
 * Copyright (c) 2009-2011 farmafene.com
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
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * Clase adaptadora de una conexión jdbc a una XAConnection.
 */
public class Connection2XAConnection implements XAConnection {

	private Connection connection;

	private XAResourceImpl xaResource;

	private List<ConnectionEventListener> connectionEventListeners = new LinkedList<ConnectionEventListener>();

	private List<StatementEventListener> statementEventListeners = new LinkedList<StatementEventListener>();

	/**
	 * Constructor
	 * 
	 * @param connection
	 *            conexión a realizar el Mock
	 */
	public Connection2XAConnection(Connection connection) {
		this.connection = connection;
		this.xaResource = new XAResourceImpl(connection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.connection.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("connection=").append(connection);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof Connection2XAConnection) {
			equals = this.connection
					.equals(((Connection2XAConnection) obj).connection);
		}
		return equals;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.XAConnection#getXAResource()
	 */
	public XAResource getXAResource() throws SQLException {
		return xaResource;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
	 */
	public void addConnectionEventListener(ConnectionEventListener listener) {
		connectionEventListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#close()
	 */
	public void close() throws SQLException {
		connection.close();
		for (ConnectionEventListener connectionEventListener : connectionEventListeners) {
			connectionEventListener.connectionClosed(new ConnectionEvent(this));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return connection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
	 */
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		connectionEventListeners.remove(listener);
	}

	public void addStatementEventListener(
			StatementEventListener statementeventlistener) {
		statementEventListeners.add(statementeventlistener);
	}

	public void removeStatementEventListener(
			StatementEventListener statementeventlistener) {
		statementEventListeners.remove(statementeventlistener);
	}
}
