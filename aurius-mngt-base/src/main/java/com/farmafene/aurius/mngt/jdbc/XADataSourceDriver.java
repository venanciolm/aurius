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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import com.farmafene.aurius.mngt.AuriusNamedResource;
import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResourceStatus;
import com.farmafene.aurius.mngt.IBasicStat;

/**
 * Implementación de un XADataSource para no-XA JDBC Driver.
 */
public class XADataSourceDriver implements XADataSource, AuriusNamedResource {

	private int loginTimeout;
	private String driver;
	private String auriusResourceName;
	private String url;
	private String user;
	private String password;
	private AuriusResource resource;

	/**
	 * Constructor de la clase
	 */
	public XADataSourceDriver() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		this.loginTimeout = seconds;
	}

	/**
	 * Obtiene la clase configurada como driver
	 * 
	 * @return clase del driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * Establece el driver y lo registra
	 * 
	 * @param driverClassName
	 *            clase del driver
	 * @throws ClassNotFoundException
	 *             no se encuenta la clase
	 * @throws IllegalAccessException
	 *             no se puede acceder al método constructor
	 * @throws InstantiationException
	 *             no se puede instanciar la clase
	 * @throws SQLException
	 *             error al registrar el driver
	 */
	public void setDriver(String driverClassName)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, SQLException {
		this.driver = driverClassName;
		Class<?> driverClazz = loadClass(driverClassName);
		DriverManager.registerDriver((Driver) driverClazz.newInstance());
	}

	/**
	 * Obtiene la url de conexión para el driver
	 * 
	 * @return la url de conexión
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Establece la URL de conexión para el driver.
	 * 
	 * @param url
	 *            la url de conexión
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Obtiene el usuario del driver
	 * 
	 * @return el usuario
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Establece el usuario para el driver
	 * 
	 * @param user
	 *            el usuario
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Obtiene la password para el driver
	 * 
	 * @return la password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la password al driver
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#setLogWriter(PrintWriter)
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#getXAConnection()
	 */
	public XAConnection getXAConnection() throws SQLException {
		return getXAConnection(user, password);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XADataSource#getXAConnection(String, String)
	 */
	public XAConnection getXAConnection(String user, String password)
			throws SQLException {
		try {
			Properties props = new Properties();
			props.setProperty("user", user);
			props.setProperty("password", password);
			Connection connection = null;
			IBasicStat r = new AuriusResourceStatus(resource,
					AuriusResource.Operacion.OPEN);
			Throwable thStatus = null;
			try {
				r.start();
				connection = DriverManager.getConnection(url, props);
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
			XAConnection xaCon = new Connection2XAConnection(connection);
			XAConnection decorada = new XAConnectionDecorator(resource, xaCon);
			return decorada;
		} catch (Exception ex) {
			throw (SQLException) new SQLException(
					"Imposible conectar al recurso no-XA " + driver)
					.initCause(ex);
		}
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
		sb.append("url=").append(url);
		sb.append(", resource=").append(resource);
		sb.append(", driver=").append(driver);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the jdbcResource
	 */
	public String getAuriusResourceName() {
		return this.auriusResourceName;
	}

	/**
	 * @param jdbcResource
	 *            the jdbcResource to set
	 */
	public void setAuriusResourceName(String jdbcResource) {
		this.auriusResourceName = jdbcResource;
		resource = new AuriusResource(jdbcResource, AuriusResource.Type.JDBC);
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
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Not supproted");
	}
}
