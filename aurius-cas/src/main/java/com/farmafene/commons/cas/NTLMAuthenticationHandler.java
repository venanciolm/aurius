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

import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class NTLMAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler implements
		AuthenticationHandler, InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(NTLMAuthenticationHandler.class);
	private String domain;
	private String domainController;

	public NTLMAuthenticationHandler() {

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
		sb.append("domaincontroler=").append(domainController);
		sb.append(", domain=").append(domain);
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
		Assert.notNull(domainController);
		if (!StringUtils.hasText(domain)) {
			domain = domainController;
		}
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
		long initTime = System.currentTimeMillis();
		boolean out = false;
		UniAddress mydomaincontroller = null;
		try {
			mydomaincontroller = UniAddress.getByName(domainController);
			NtlmPasswordAuthentication mycreds = new NtlmPasswordAuthentication(
					domain, credentials.getUsername(),
					credentials.getPassword());
			SmbSession.logon(mydomaincontroller, mycreds);
			out = true;
		} catch (UnknownHostException e) {
			logger.error("Server unknown", e);
		} catch (SmbAuthException sae) {
			// AUTHENTICATION FAILURE
			logger.error("Auth Failure", sae);
		} catch (SmbException se) {
			// NETWORK PROBLEMS?
			logger.error("¿Network Problem?", se);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Total time: "
					+ (System.currentTimeMillis() - initTime) + " ms, " + this);
		}
		return out;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainController() {
		return domainController;
	}

	public void setDomainController(String domainController) {
		this.domainController = domainController;
	}
}
