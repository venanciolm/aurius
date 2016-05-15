package com.farmafene.aurius.mngt.jdbc;

import java.io.PrintWriter;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public class AuriusConnectionPoolDataSourceDriver implements
		ConnectionPoolDataSource {

	private PrintWriter printWriter;
	private int loginTimeout;
	private String driver;
	private String url;

	public AuriusConnectionPoolDataSourceDriver() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("{");
		if (printWriter != null) {
			sb.append("printWriter=");
			sb.append(printWriter);
			sb.append(", ");
		}
		sb.append("loginTimeout=");
		sb.append(loginTimeout);
		sb.append(", ");
		if (driver != null) {
			sb.append("driver=");
			sb.append(driver);
			sb.append(", ");
		}
		if (url != null) {
			sb.append("url=");
			sb.append(url);
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, SQLException {
		this.driver = driver;
		Class<?> driverClazz = loadClass(driver);
		DriverManager.registerDriver((Driver) driverClazz.newInstance());
	}

	private Class<?> loadClass(String className) throws ClassNotFoundException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl != null) {
			try {
				return cl.loadClass(className);
			} catch (ClassNotFoundException ex) {
			}
		}

		return Class.forName(className);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.printWriter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.printWriter = out;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeout = seconds;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return this.loginTimeout;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.ConnectionPoolDataSource#getPooledConnection()
	 */
	@Override
	public PooledConnection getPooledConnection() throws SQLException {
		return new Connection2PooledConnection(DriverManager.getConnection(url));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.ConnectionPoolDataSource#getPooledConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public PooledConnection getPooledConnection(String user, String password)
			throws SQLException {
		return new Connection2PooledConnection(DriverManager.getConnection(url,
				user, password));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Not Supported!");
	}
}
