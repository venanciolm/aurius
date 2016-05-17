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
package com.farmafene.aurius.dicImpl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.IDiccionarioCache;
import com.farmafene.aurius.core.Servicio;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Manejador de caché
 * 
 * @author vlopez@farmafene.com
 * @see http 
 *      ://docs.guava-libraries.googlecode.com/git/javadoc/index.html?com/google
 *      /common/cache/Cache.html
 * 
 */
public class DiccionarioCacheGuava implements IDiccionarioCache,
		InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(DiccionarioCacheGuava.class);

	private Cache<String, TypeRegistro> cacheTypeRegistro;
	private Cache<String, Map<String, TypeRegistro>> cacheRegistro;
	private Cache<String, String> cacheVersiones;
	private Cache<String, Servicio> cacheServicio;
	private int cacheSize = 100;
	private TimeUnit timeUnit = TimeUnit.MINUTES;
	private int timeValue = 10;

	/**
	 * Constructor
	 */
	public DiccionarioCacheGuava() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		cacheTypeRegistro = CacheBuilder.newBuilder()
				.maximumSize(getCacheSize())
				.expireAfterWrite(getTimeValue(), getTimeUnit()).build();
		cacheRegistro = CacheBuilder.newBuilder().maximumSize(getCacheSize())
				.expireAfterWrite(getTimeValue(), getTimeUnit()).build();
		cacheVersiones = CacheBuilder.newBuilder().maximumSize(getCacheSize())
				.expireAfterWrite(getTimeValue(), getTimeUnit()).build();
		cacheServicio = CacheBuilder.newBuilder().maximumSize(getCacheSize())
				.expireAfterWrite(getTimeValue(), getTimeUnit()).build();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getDefinicionRegistro(java.lang.String)
	 */
	@Override
	public TypeRegistro getDefinicionRegistro(String id)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getDefinicionRegistro(" + id + ")");
		}
		return cacheTypeRegistro.getIfPresent(id);
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
		return cacheRegistro.getIfPresent(idRegistro);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#getServicio(java.lang.String)
	 */
	@Override
	public Servicio getServicio(String id) throws IllegalArgumentException {
		Servicio servicio = null;
		if (logger.isDebugEnabled()) {
			logger.debug("getServicio(" + id + ")");
		}
		String versionSolicitada = cacheVersiones.getIfPresent(id);
		if (null != versionSolicitada) {
			servicio = cacheServicio.getIfPresent(getServicioVersion(id,
					versionSolicitada));
		}
		return servicio;
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
			logger.debug("getDefinicionRegistro(" + id + "," + version + ")");
		}
		return cacheServicio.getIfPresent(getServicioVersion(id, version));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      com.farmafene.aurius.TypeRegistro)
	 */
	@Override
	public void put(String idRegistro, TypeRegistro reg) {
		if (logger.isDebugEnabled()) {
			logger.debug("put(" + idRegistro + "," + reg + ")");
		}
		cacheTypeRegistro.put(idRegistro, reg);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionarioCache#put(java.lang.String,
	 *      java.util.Map)
	 */
	@Override
	public void put(String idRegistro, Map<String, TypeRegistro> reg) {
		if (logger.isDebugEnabled()) {
			logger.debug("put(" + idRegistro + "," + reg + ")");
		}
		cacheRegistro.put(idRegistro, reg);

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
			logger.debug("put(" + nombre + "," + svr + ")");
		}
		if (null == getServicio(nombre)) {
			cacheVersiones.put(nombre, svr.getVersion());
			put(nombre, svr.getVersion(), svr);
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
			logger.debug("put(" + nombre + "," + svr + "," + v + ")");
		}
		cacheServicio.put(getServicioVersion(nombre, v), svr);
	}

	private String getServicioVersion(String nombre, String v) {
		return nombre + "v" + v;
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
		return "Caché basada en Guava";
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public void setTimeUnit(String timeUnit) {
		try {
			TimeUnit t = TimeUnit.valueOf(timeUnit);
			if (null != t) {
				this.timeUnit = t;
			}
		} catch (Exception e) {
			// do nothing
		}
	}

	public void setTimeValue(int timeValue) {
		this.timeValue = timeValue;
	}

	public int getCacheSize() {
		return this.cacheSize;
	}

	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	public int getTimeValue() {
		return this.timeValue;
	}
}
