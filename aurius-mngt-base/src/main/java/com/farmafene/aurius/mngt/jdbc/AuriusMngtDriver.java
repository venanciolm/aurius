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
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResourceStatus;
import com.farmafene.aurius.mngt.IBasicStat;

public abstract class AuriusMngtDriver implements Driver {
	private static final String DRIVER_PREFIX = "jdbc:aurius:";
	private Driver innerDriver;
	private String subPrefix;

	/**
	 * Constructor por defecto
	 * 
	 * @throws SQLException
	 */
	public AuriusMngtDriver() {

	}

	/**
	 * Subprefijo
	 * 
	 * @return
	 */
	public String getSubPrefix() {
		return subPrefix;
	}

	/**
	 * Establece el subprefijo
	 * 
	 * @param subPrefix
	 */
	public void setSubPrefix(String subPrefix) {
		if (null != subPrefix) {
			this.subPrefix = subPrefix;
		} else {
			subPrefix = null;
		}
	}

	/**
	 * Obtiene la clase configurada como driver
	 * 
	 * @return clase del driver
	 */
	public String getDriver() {
		return innerDriver.getClass().getName();
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
		Class<?> driverClazz = loadClass(driverClassName);
		innerDriver = (Driver) driverClazz.newInstance();
		DriverManager.registerDriver(innerDriver);
		DriverManager.registerDriver(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append("={");
		sb.append("driver=").append(getDriver());
		sb.append("}");
		return sb.toString();
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
	 * @see java.sql.Driver#acceptsURL(java.lang.String)
	 */
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		boolean salida = false;
		if (null != url && url.startsWith(DRIVER_PREFIX + subPrefix)) {
			salida = true;
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */
	@Override
	public Connection connect(String url, Properties info) throws SQLException {

		Connection conn = null;
		String urlEfectiva = unpack(url);
		AuriusResource resource = new AuriusResource(resourceName(urlEfectiva),
				AuriusResource.Type.JDBC);
		IBasicStat r = new AuriusResourceStatus(resource,
				AuriusResource.Operacion.OPEN);
		Throwable thStatus = null;
		try {
			r.start();
			Connection inner = getInnerConnection(urlEfectiva, info);
			conn = new ConnectionDecorator(resource, inner);
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
		return conn;
	}

	/**
	 * Obtenemos la conexión a través del driver efectivo
	 * 
	 * @param urlEfectiva
	 *            url de la que se desea obtener la conexión
	 * @param info
	 *            información de la conexión
	 * @return conexión.
	 * @throws SQLException
	 *             si se produce un error en la obtención de la misma
	 */
	protected Connection getInnerConnection(String urlEfectiva, Properties info)
			throws SQLException {
		return DriverManager.getConnection(urlEfectiva, info);
	}

	private String resourceName(String urlEfectiva) {
		return urlEfectiva;
	}

	/**
	 * Convierte la url a una url de petición
	 * 
	 * @param url
	 *            url del driver
	 * @return url efectiva.
	 */
	private String unpack(String url) {
		String salida = url;
		if (null != url && url.startsWith(DRIVER_PREFIX)) {
			salida = "jdbc:" + url.substring(DRIVER_PREFIX.length());
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Driver#getMajorVersion()
	 */
	@Override
	public int getMajorVersion() {
		return innerDriver.getMajorVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Driver#getMinorVersion()
	 */
	@Override
	public int getMinorVersion() {
		return innerDriver.getMinorVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Driver#getPropertyInfo(java.lang.String,
	 *      java.util.Properties)
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return DriverManager.getDriver(unpack(url)).getPropertyInfo(
				unpack(url), info);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Driver#jdbcCompliant()
	 */
	@Override
	public boolean jdbcCompliant() {
		return innerDriver.jdbcCompliant();
	}
}
