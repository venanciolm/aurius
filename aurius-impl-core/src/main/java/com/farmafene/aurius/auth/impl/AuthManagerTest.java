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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.util.IOnInitValidate;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.WrapperClassContainer;
import com.farmafene.commons.cas.AuthInfoString;

@SuppressWarnings("serial")
public class AuthManagerTest extends AuthenticationManagerFarmafene implements
		IWrapperClass, IOnInitValidate {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthManagerTest.class);
	private WrapperClassContainer wrapperClass;

	public AuthManagerTest() {
		wrapperClass = new WrapperClassContainer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.auth.impl.AuthenticationManagerFarmafene#isWrapperFor
	 *      (java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> iface) {
		return wrapperClass.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.auth.impl.AuthenticationManagerFarmafene#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) {
		return wrapperClass.unwrap(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.auth.impl.AuthenticationManagerFarmafene#
	 *      afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		wrapperClass = new WrapperClassContainer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.auth.impl.AuthenticationManagerFarmafene#getLoginFromCookie(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	protected String getLoginFromCookie(AuthInfo cookie) {
		String getLoginFromCookie = null;
		if (cookie.isWrapperFor(AuthInfoString.class)) {
			getLoginFromCookie = cookie.unwrap(AuthInfoString.class).getValue();
		} else {
			throw new IllegalArgumentException("cookie inválida");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Retornando el usuario: [" + getLoginFromCookie + "]");
		}
		return getLoginFromCookie;
	}
}
