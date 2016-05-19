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
package com.farmafene.commons.cas;

import java.util.List;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class CompositeAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler implements
		AuthenticationHandler, InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(CompositeAuthenticationHandler.class);
	private List<AuthenticationHandler> handlers;

	public CompositeAuthenticationHandler() {

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
		sb.append("with ").append(null == handlers ? 0 : handlers.size())
				.append(" handlers");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(handlers);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler#authenticateUsernamePasswordInternal(org.jasig.cas.authentication.principal.UsernamePasswordCredentials)
	 */
	@Override
	protected boolean authenticateUsernamePasswordInternal(
			UsernamePasswordCredentials credentials)
			throws AuthenticationException {
		boolean out = false;
		for (AuthenticationHandler h : handlers) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Probando with: " + h);
				}
				if ((out = h.authenticate(credentials))) {
					if (logger.isDebugEnabled()) {
						logger.debug("Login with: " + h);
					}
					break;
				}
			} catch (AuthenticationException ae) {
				logger.error("Error in Authentication", ae);
			}
		}
		return out;
	}

	public List<AuthenticationHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<AuthenticationHandler> handlers) {
		this.handlers = handlers;
	}
}