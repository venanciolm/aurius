package com.farmafene.aurius.mngt.jdbc;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;

@SuppressWarnings("serial")
public class XADatasourced4DriverAdapterCPDS extends DriverAdapterCPDS
		implements XADataSource {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.XADataSource#getXAConnection()
	 */
	@Override
	public XAConnection getXAConnection() throws SQLException {
		PooledConnection pconn = super.getPooledConnection();
		XAConnection xaConn = new XAConnection4PooledConnection(pconn);
		return xaConn;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.XADataSource#getXAConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public XAConnection getXAConnection(String user, String password)
			throws SQLException {
		PooledConnection pconn = super.getPooledConnection(user, password);
		XAConnection xaConn = new XAConnection4PooledConnection(pconn);
		return xaConn;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Not supproted");
	}
}
