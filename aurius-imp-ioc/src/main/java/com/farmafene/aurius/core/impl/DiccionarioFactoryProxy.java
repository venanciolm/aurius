/*
 * Copyright (c) 2009-2014 farmafene.com
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
package com.farmafene.aurius.core.impl;

import java.util.Map;

import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.IDiccionarioFactory;
import com.farmafene.aurius.core.Servicio;
import com.farmafene.commons.ioc.BeanFactory;

/**
 * Implementacion proxy
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
public class DiccionarioFactoryProxy implements IDiccionarioFactory {

	private IDiccionarioFactory getIDiccionarioFactory() {
		IDiccionarioFactory conf = null;
		conf = (IDiccionarioFactory) BeanFactory
				.getBean(IDiccionarioFactory.class);
		if (null == conf) {
			throw new IllegalStateException(IDiccionarioFactory.class.getName()
					+ ", no existe");
		}
		return conf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public TypeRegistro getDefinicionRegistro(String arg0)
			throws IllegalArgumentException {

		return getIDiccionarioFactory().getDefinicionRegistro(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public Servicio getServicio(String arg0) throws IllegalArgumentException {
		return getIDiccionarioFactory().getServicio(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public Servicio getServicio(String arg0, String arg1)
			throws IllegalArgumentException {
		return getIDiccionarioFactory().getServicio(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getRegistroNormalizado(java.lang.String)
	 */
	@Override
	public String getRegistroNormalizado(String id) {
		return getIDiccionarioFactory().getRegistroNormalizado(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getServicioNormalizado(java.lang.String)
	 */
	@Override
	public String getServicioNormalizado(String id) {
		return getIDiccionarioFactory().getServicioNormalizado(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getVersionNomalizada(java
	 *      .lang.String)
	 */
	@Override
	public String getVersionNomalizada(String version) {
		return getIDiccionarioFactory().getVersionNomalizada(version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioFactory#getRegistro(java.lang.String,
	 *      java.util.Map)
	 */
	@Override
	public Registro getRegistro(String idRegistro,
			Map<String, TypeRegistro> definiciones) {
		return getIDiccionarioFactory().getRegistro(idRegistro, definiciones);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return getIDiccionarioFactory().getImplementationVentor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return getIDiccionarioFactory().getImplementationVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return getIDiccionarioFactory().getImplementationDescription();
	}
}
