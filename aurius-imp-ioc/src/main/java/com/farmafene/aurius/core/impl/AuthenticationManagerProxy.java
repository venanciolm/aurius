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

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.core.IAuthenticationManager;
import com.farmafene.commons.ioc.BeanFactory;

/**
 * Proxy del gestor de autenticación
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class AuthenticationManagerProxy implements IAuthenticationManager {

	public AuthenticationManagerProxy() {
	}

	/**
	 * Obtenemos el Proxy activo
	 */
	private IAuthenticationManager getIAuthenticationManager() {
		IAuthenticationManager conf = null;
		conf = (IAuthenticationManager) BeanFactory
				.getBean(IAuthenticationManager.class);
		if (null == conf) {
			throw new IllegalStateException(
					IAuthenticationManager.class.getName() + ", no existe");
		}
		return conf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.IAuthenticationManager#valida(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	public void valida(AuthInfo cookie, String idServicio)
			throws AuriusAuthException {
		getIAuthenticationManager().valida(cookie, idServicio);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuthenticationManager#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> iface) {
		if (iface == null) {
			throw new IllegalArgumentException("la clase no puede ser null");
		}
		return iface.isAssignableFrom(getIAuthenticationManager().getClass());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuthenticationManager#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws IllegalArgumentException {
		if (!isWrapperFor(iface)) {
			throw new IllegalArgumentException("Not asignable");
		}
		@SuppressWarnings("unchecked")
		T unwrap = (T) getIAuthenticationManager();
		return unwrap;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return getIAuthenticationManager().getImplementationVentor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return getIAuthenticationManager().getImplementationVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return getIAuthenticationManager().getImplementationDescription();
	}
}
