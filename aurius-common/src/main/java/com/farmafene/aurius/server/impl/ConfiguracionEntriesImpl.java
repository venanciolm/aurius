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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.server.Configuracion;
import com.farmafene.aurius.server.IConfiguracion;
import com.farmafene.aurius.server.IConfiguracionEntries;

/**
 * Implementacion de la búsqueda varia de propiedades de cofiguración
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfiguracionEntriesImpl implements IConfiguracionEntries {

	private static String DEFAULT_FILE = "Default";
	private static String DEFAULT_FILE_EXT = ".xml";
	private static final int LEVEL_ROOT = 0;
	private static final int LEVEL_CONF = 1;
	private static final int LEVEL_APP = 2;
	private static final int LEVEL_APP_CONF = 3;

	private static final Logger logger = LoggerFactory
			.getLogger(ConfiguracionEntriesImpl.class);

	private String rootFile;
	private String idConfiguracion;
	private String aplicacion;
	private int level;
	private Properties props;

	/**
	 * 
	 * Constructor
	 * 
	 * @see IConfiguracion
	 * @since 1.0.0
	 */
	private ConfiguracionEntriesImpl(String rootFile, String aplicacion,
			String idConfiguracion, int level) {
		this.rootFile = rootFile;
		this.aplicacion = aplicacion;
		this.idConfiguracion = idConfiguracion;
		this.level = level;
		loadProps();
	}

	private void loadProps() {
		File f = getPathByLevel();
		FileInputStream fis = null;
		props = new Properties();
		try {
			fis = new FileInputStream(f);
			props.loadFromXML(fis);
		} catch (FileNotFoundException e) {
			logger.warn("Fichero no encontrado", e);
		} catch (InvalidPropertiesFormatException e) {
			logger.warn("Fichero inválido", e);
		} catch (IOException e) {
			logger.warn("Error en la lectura del fichero", e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				logger.warn("Error al cerrar el fichero", e);
			}
		}
	}

	/**
	 * Constructor parametrizado
	 * 
	 * @param aplicacion
	 *            identificador de la aplicacion
	 * @param idConfiguracion
	 *            identicador del fichero.
	 * @since 1.0.0
	 */
	public ConfiguracionEntriesImpl(String rootFile, String aplicacion,
			String idConfiguracion) {
		this(
				rootFile,
				aplicacion,
				idConfiguracion,
				(aplicacion == null || "".equals(aplicacion.trim())) ? (idConfiguracion == null
						|| "".equals(idConfiguracion.trim()) ? LEVEL_ROOT
						: LEVEL_CONF) : (idConfiguracion == null
						|| "".equals(idConfiguracion.trim()) ? LEVEL_APP
						: LEVEL_APP_CONF));
	}

	private File getPathByLevel() {
		File out = null;
		switch (level) {
		case LEVEL_ROOT:
			out = new File(rootFile);
			if (!out.exists()) {
				throw new IllegalArgumentException("El fichero >"
						+ out.getPath() + "< no existe");
			}
			break;
		case LEVEL_CONF:
			out = new File(Configuracion.getConfigPath()
					+ File.separator
					+ (idConfiguracion == null
							|| "".equals(idConfiguracion.trim()) ? DEFAULT_FILE
							: idConfiguracion) + DEFAULT_FILE_EXT);
			if (!out.exists()) {
				level = LEVEL_ROOT;
				out = getPathByLevel();
			}
			break;
		case LEVEL_APP:
			out = new File(getApplicationConfigPath(aplicacion)
					+ File.separator + DEFAULT_FILE + DEFAULT_FILE_EXT);
			if (!out.exists()) {
				level = LEVEL_CONF;
				out = getPathByLevel();
			}
			break;
		case LEVEL_APP_CONF:
			out = new File(getApplicationConfigPath(aplicacion)
					+ File.separator
					+ (idConfiguracion == null
							|| "".equals(idConfiguracion.trim()) ? DEFAULT_FILE
							: idConfiguracion) + DEFAULT_FILE_EXT);
			if (!out.exists()) {
				level = LEVEL_APP;
				out = getPathByLevel();
			}
			break;
		}
		return out;
	}

	private String getApplicationConfigPath(String aplicacion) {
		File f = null;
		f = new File(Configuracion.getConfigPath());
		f = new File(f.getParent(), "apps");
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(f, aplicacion);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(f, "conf");
		if (!f.exists()) {
			f.mkdir();
		}
		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			throw new UnsupportedOperationException("Error indeterminado!!!", e);
		}
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
		sb.append("idConfiguracion=").append(getIdConfiguracion());
		sb.append(", aplicacion=").append(aplicacion);
		sb.append(", level=").append(level);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getIdConfiguracion() {
		return idConfiguracion;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getProperty(String key) {
		String value = props.getProperty(key);
		if (null == value && LEVEL_ROOT != getLevel()) {
			ConfiguracionEntriesImpl en = new ConfiguracionEntriesImpl(
					rootFile, getAplicacion(), getIdConfiguracion(),
					getLevel() - 1);
			value = en.getProperty(key);
		}
		return value;

	}

	/**
	 * @return the aplicacion
	 * @since 1.0.0
	 */
	public String getAplicacion() {
		return aplicacion;
	}

	/**
	 * @return the level
	 * @since 1.0.0
	 */
	public int getLevel() {
		return level;
	}

}
