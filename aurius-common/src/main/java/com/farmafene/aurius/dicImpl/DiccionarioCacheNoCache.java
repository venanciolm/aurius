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
/**
 * 
 */
package com.farmafene.aurius.dicImpl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.IDiccionarioCache;
import com.farmafene.aurius.core.Servicio;

/**
 * @author vlopez
 * 
 */
public class DiccionarioCacheNoCache implements IDiccionarioCache {

	private static final Logger logger = LoggerFactory
			.getLogger(DiccionarioCacheNoCache.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getDefinicionRegistro(java
	 *      .lang.String)
	 */
	@Override
	public TypeRegistro getDefinicionRegistro(String id)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDefinicionRegistro('" + id + "')");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getServicio(java.lang.String)
	 */
	@Override
	public Servicio getServicio(String id) throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getServicio('" + id + "')");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getServicio(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Servicio getServicio(String id, String version)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getServicio('" + id + "','" + version + "')");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      com.farmafene.aurius.TypeRegistro)
	 */
	@Override
	public void put(String nombre, TypeRegistro reg) {
		if (logger.isDebugEnabled()) {
			logger.debug("put('" + nombre + "'" + reg + "')");
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      com.farmafene.aurius.core.Servicio)
	 */
	@Override
	public void put(String nombre, Servicio svr) {
		if (logger.isDebugEnabled()) {
			logger.debug("put('" + nombre + "'" + svr + "')");
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      com.farmafene.aurius.core.Servicio, java.lang.String)
	 */
	@Override
	public void put(String nombre, String v, Servicio svr) {
		if (logger.isDebugEnabled()) {
			logger.debug("put('" + nombre + "''" + svr + "','" + v + "')");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getRegistro(java.lang.String)
	 */
	@Override
	public Map<String, TypeRegistro> getRegistro(String idRegistro)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getRegistro(" + idRegistro + ")");
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      com.farmafene.aurius.Registro)
	 */
	@Override
	public void put(String idRegistro, Map<String, TypeRegistro> reg) {
		if (logger.isDebugEnabled()) {
			logger.debug("put('" + idRegistro + "''" + reg + "')");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return "Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return "1.0";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return "Implementación sin caché";
	}
}
