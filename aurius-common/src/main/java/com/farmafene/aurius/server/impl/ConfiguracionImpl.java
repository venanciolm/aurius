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
package com.farmafene.aurius.server.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IClassLoaderWithHolder;
import com.farmafene.aurius.server.IConfiguracion;
import com.farmafene.aurius.server.IConfiguracionEntries;

/**
 * Implementación de la configuración
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class ConfiguracionImpl implements IConfiguracion {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfiguracionImpl.class);
	private static final String JNDI_CONFIG_FILE = "java:comp/env/farmafene.com/aurius/conf";
	private String configPath;
	private String defaultFile;

	/**
	 * 
	 * Constructor
	 * 
	 * @see IConfiguracion
	 * @since 1.0.0
	 */
	public ConfiguracionImpl() {
		logger.debug(this + "<init>()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("defaultFile=").append(getDefaultFile());
		sb.append(", configPath=").append(getConfigPath());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public IConfiguracionEntries getEntries(String idConfiguracion) {
		return new ConfiguracionEntriesImpl(getDefaultFile(), getAplicacion(),
				idConfiguracion);
	}

	private String getAplicacion() {
		String aplicacion = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl instanceof IClassLoaderWithHolder) {
			IClassLoaderWithHolder acl = (IClassLoaderWithHolder) cl;
			IAplicacionHolder holder = acl.getHolder();
			aplicacion = holder.getAplicacion();
		}
		return aplicacion;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getProperty(String key) {
		return getEntries(null).getProperty(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getProperty(String idConfiguracion, String key) {
		return getEntries(idConfiguracion).getProperty(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getConfigPath() {
		if (configPath == null) {
			configPath = createConfigPath();
		}
		return configPath;
	}

	private String getDefaultFile() {
		if (defaultFile == null) {
			configPath = createConfigPath();
		}
		return defaultFile;
	}

	private String createConfigPath() {
		String salida = null;
		File f = null;
		URL url = null;
		try {
			Object obj = new InitialContext().lookup(JNDI_CONFIG_FILE);
			if (obj != null && (obj instanceof String)) {
				url = new URL((String) obj);
			}
			if (obj != null && (obj instanceof URL)) {
				url = (URL) obj;
			}
			logger.debug("El fichero es: " + f);
			f = new File(url.getFile());
			logger.info("El fichero leido es: " + f.getPath());
			defaultFile = f.getPath();
			if (!f.isAbsolute()) {
				logger.info("El fichero no es absoluto: " + f.getPath());
				f = new File(System.getProperty("user.dir") + url.getFile());
			}
			logger.info("El fichero absoluto es: " + f.getPath());
			if (f.isDirectory()) {
				throw new IllegalArgumentException(
						"El fichero no debe ser un directorio");
			}
			logger.info("Obteniendo el padre");
			salida = f.getParentFile().getCanonicalPath();
		} catch (NamingException e) {
			logger.error("Error en la obtención el valor de JNDI", e);
		} catch (IOException e) {
			logger.error("Error en la obtención del valor del fichero", e);
		} finally {
		}
		if (salida == null) {
			throw new IllegalArgumentException("Error en configuraci�n!!!!!");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("El directorio de configuraci�n es: " + salida);
		}
		return salida;
	}
}
