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
package com.farmafene.aurius.server;

import com.farmafene.aurius.server.impl.ConfiguracionImpl;
import com.farmafene.aurius.util.ProxyFactory;

/**
 * Factoría por defecto de configuración
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
public class Configuracion {

	private static final IConfiguracion factory = new IConfiguracion() {

		private IConfiguracion instance = new ConfiguracionImpl();
		private ProxyFactory<IConfiguracionEntries> proxyFactory = new ProxyFactory<IConfiguracionEntries>();

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.IConfiguracion#getProperty(java.lang.
		 *      String, java.lang.String)
		 */
		@Override
		public String getProperty(String idConfiguracion, String key) {
			return instance.getProperty(idConfiguracion, key);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.IConfiguracion#getProperty(java.lang.
		 *      String)
		 */
		@Override
		public String getProperty(String key) {
			return instance.getProperty(key);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.IConfiguracion#getEntries(java.lang.String
		 *      )
		 */
		@Override
		public IConfiguracionEntries getEntries(String idConfiguracion) {
			return proxyFactory.newInstance(instance
					.getEntries(idConfiguracion));
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.IConfiguracion#getConfigPath()
		 */
		@Override
		public String getConfigPath() {
			return instance.getConfigPath();
		}
	};

	/**
	 * Constructor privado
	 */
	private Configuracion() {

	}

	/**
	 * Acceso a la configuración del sistema
	 * 
	 * @return configuración del sistema
	 * @since 1.0.0
	 */
	public static IConfiguracion getIConfiguracion() {
		return factory;
	}

	/**
	 * @see IConfiguracion#getConfigPath()
	 * @since 1.0.0
	 */
	public static String getConfigPath() {
		return getIConfiguracion().getConfigPath();
	}

	/**
	 * @see IConfiguracion#getEntries(String)
	 * @since 1.0.0
	 */
	public static IConfiguracionEntries getEntries(String idConfiguracion) {
		return getIConfiguracion().getEntries(idConfiguracion);
	}

	/**
	 * @see IConfiguracion#getProperty(String, String)
	 * @since 1.0.0
	 */
	public static String getProperty(String idConfiguracion, String key) {
		return getIConfiguracion().getProperty(idConfiguracion, key);
	}

	/**
	 * @see IConfiguracion#getProperty(String)
	 * @since 1.0.0
	 */
	public static String getProperty(String key) {
		return getIConfiguracion().getProperty(key);
	}
}