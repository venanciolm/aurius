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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author vlopez@farmafene.com
 */
public class URLAuthenticationHandler extends
		AbstractUsernamePasswordAuthenticationHandler implements
		AuthenticationHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(URLAuthenticationHandler.class);
	private String loginURL;
	private String proxyHost;
	private int proxyPort;

	public URLAuthenticationHandler() {
		proxyPort = 80;
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
		sb.append("loginURL=").append(loginURL);
		sb.append(", proxyHost=").append(proxyHost);
		sb.append(", proxyPort=").append(proxyPort);
		sb.append("}");
		return sb.toString();
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
		boolean authenticateUsernamePasswordInternal = false;
		HttpContext context = new BasicHttpContext();
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(loginURL);
		f.setProxyHost(proxyHost);
		f.setProxyPort(proxyPort);
		DefaultHttpClient httpClient = f.getClient();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		org.apache.http.auth.UsernamePasswordCredentials cred = new org.apache.http.auth.UsernamePasswordCredentials(
				credentials.getUsername(), credentials.getPassword());
		credsProvider.setCredentials(AuthScope.ANY, cred);
		List<String> n = new ArrayList<String>();
		n.add(AuthPolicy.BASIC);
		n.add(AuthPolicy.DIGEST);
		httpClient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, n);
		context.setAttribute(ClientContext.CREDS_PROVIDER, credsProvider);
		HttpGet httpGet = new HttpGet(loginURL);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet, context);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				authenticateUsernamePasswordInternal = true;
			}
		} catch (ClientProtocolException e) {
			logger.error("Error: ", e);
		} catch (IOException e) {
			logger.error("Error: ", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Total time: "
					+ (System.currentTimeMillis() - initTime) + " ms, " + this);
		}
		return authenticateUsernamePasswordInternal;
	}

	/**
	 * @return the loginURL
	 */
	public String getLoginURL() {
		return loginURL;
	}

	/**
	 * @param loginURL
	 *            the loginURL to set
	 */
	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost
	 *            the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}