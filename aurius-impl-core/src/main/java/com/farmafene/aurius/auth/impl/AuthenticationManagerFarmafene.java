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
package com.farmafene.aurius.auth.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.core.Diccionario;
import com.farmafene.aurius.core.IAuriusUserFactory;
import com.farmafene.aurius.core.IAuthenticationManager;
import com.farmafene.aurius.core.Servicio;
import com.farmafene.aurius.server.IAuriusUser;
import com.farmafene.aurius.util.IOnInitValidate;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.WrapperClassContainer;
import com.farmafene.commons.cas.AuthInfoString;
import com.farmafene.commons.cas.ValidateTGT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Gestor de autenticación
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class AuthenticationManagerFarmafene implements IAuthenticationManager,
		IWrapperClass, IOnInitValidate {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationManagerFarmafene.class);
	private WrapperClassContainer wrapperClass;
	private Cache<AuthInfo, IAuriusUser> cache;
	private ValidateTGT validateTGT;
	private long minutes = 10;
	private long size = 100;
	private String casServerURL;
	private String casServiceName;
	private IAuriusUserFactory iAuriusUserFactory;

	/**
	 * Constructor por defecto
	 */
	public AuthenticationManagerFarmafene() {
		cache = CacheBuilder.newBuilder()
				.expireAfterAccess(minutes, TimeUnit.MINUTES).maximumSize(size)
				.build();
		validateTGT = new ValidateTGT();
		validateTGT.setCasServerURL("https://cas.farmafenene.com");
		validateTGT.setCasServiceName("Aurius");
		validateTGT.afterPropertiesSet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.IAuthenticationManager#valida(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	@Override
	public void valida(AuthInfo cookie, String idServicio)
			throws AuriusAuthException {

		if (idServicio == null) {
			throw new AuriusAuthException(
					"valida(<cookie>,null): Debe establecerse el servicio");
		}
		Servicio svr = Diccionario.getServicio(idServicio);
		if (svr == null) {
			throw new AuriusAuthException("valida(<cookie>," + idServicio
					+ "): No existe el servicio");
		}
		ContextoCore ctx = ContextoManager.getContexto();
		IAuriusUser user = ctx.getIAuriusUser();
		if (null == user) {
			user = getIAuriusUserFromCookie(cookie);
			ContextoManager.getContexto().setIAuriusUser(user);
		}
		if (null == ctx.getIdServicioActual() && !svr.isPublic()) {
			throw new AuriusAuthException(
					"El servicio no puede ser invocado directamente!['"
							+ idServicio + "']");
		} else if ((null == ctx.getIdServicioActual() || (null != ctx
				.getIdServicioActual() && !svr.isInheritAuth()))
				&& !user.isUserInRoles(svr.getRoles())) {
			throw new AuriusAuthException("No se disponene de permisos!['"
					+ idServicio + "']");
		}
	}

	/**
	 * @param cookie
	 */
	private IAuriusUser getIAuriusUserFromCookie(AuthInfo cookie) {
		if (cookie == null) {
			throw new AuriusAuthException("No autenticado");
		}
		IAuriusUser a = cache.getIfPresent(cookie);
		if (a == null) {
			String login = getLoginFromCookie(cookie);
			if (null == login) {
				throw new AuriusAuthException("Usuario inexistente");
			}
			a = getIAuriusUserFromPersistence(login);
			if (a == null) {
				throw new AuriusAuthException("Usuario inválido");
			}
			cache.put(cookie, a);
		}
		return a;
	}

	protected String getLoginFromCookie(AuthInfo cookie) {
		return validateTGT.validateTGT(cookie);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IOnInitValidate#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		validateTGT = new ValidateTGT();
		validateTGT.setCasServerURL(casServerURL);
		validateTGT.setCasServiceName(casServiceName);
		validateTGT.afterPropertiesSet();
		wrapperClass = new WrapperClassContainer(this);
		cache = CacheBuilder.newBuilder().maximumSize(getSize())
				.expireAfterAccess(getMinutes(), TimeUnit.MINUTES).build();
		if (logger.isDebugEnabled()) {
			logger.debug(this + ".afterPropertiesSet()");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuthenticationManager#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> iface) {
		return wrapperClass.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuthenticationManager#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws IllegalArgumentException {
		return wrapperClass.unwrap(iface);
	}

	private IAuriusUser getIAuriusUserFromPersistence(String login) {
		AuthInfoString cookie = new AuthInfoString();
		cookie.setValue(login);
		return getIAuriusUserFactory().getIAuriusUser(cookie);
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
		return "Autenticador basado en CAS y para Farmafene";
	}

	/**
	 * @return the minutes
	 */
	public long getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes
	 *            the minutes to set
	 */
	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the casServerURL
	 */
	public String getCasServerURL() {
		return casServerURL;
	}

	/**
	 * @param casServerURL
	 *            the casServerURL to set
	 */
	public void setCasServerURL(String casServerURL) {
		this.casServerURL = casServerURL;
	}

	/**
	 * @return the casServiceName
	 */
	public String getCasServiceName() {
		return casServiceName;
	}

	/**
	 * @param casServiceName
	 *            the casServiceName to set
	 */
	public void setCasServiceName(String casServiceName) {
		this.casServiceName = casServiceName;
	}

	/**
	 * @return the iAuriusUserFactory
	 */
	public IAuriusUserFactory getIAuriusUserFactory() {
		return iAuriusUserFactory;
	}

	/**
	 * @param iAuriusUserFactory
	 *            the iAuriusUserFactory to set
	 */
	public void setIAuriusUserFactory(IAuriusUserFactory iAuriusUserFactory) {
		this.iAuriusUserFactory = iAuriusUserFactory;
	}
}