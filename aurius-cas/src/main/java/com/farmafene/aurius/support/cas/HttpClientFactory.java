/*
 * Copyright (c) 2009-2013 farmafene.com
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
package com.farmafene.aurius.support.cas;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuriusExceptionTO;

public class HttpClientFactory implements X509TrustManager,
		X509HostnameVerifier {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpClientFactory.class);
	private String loginURL;
	private String proxyHost;
	private int proxyPort;

	public HttpClientFactory() {
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

	public DefaultHttpClient getClient() throws AuriusAuthException {
		if (logger.isDebugEnabled()) {
			logger.debug("getClient():" + this);
		}
		DefaultHttpClient httpClient = null;
		URL u = null;
		try {
			u = new URL(loginURL.toLowerCase());
		} catch (MalformedURLException e) {
			AuriusAuthException ex = new AuriusAuthException("Error",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		if ("https".equals(u.getProtocol())) {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, new TrustManager[] { this },
						new SecureRandom());
			} catch (NoSuchAlgorithmException e) {
				AuriusAuthException ex = new AuriusAuthException("Error",
						AuriusExceptionTO.getInstance(e));
				logger.error("Excepcion en el login", ex);
				throw ex;
			} catch (KeyManagementException e) {
				AuriusAuthException ex = new AuriusAuthException("Error",
						AuriusExceptionTO.getInstance(e));
				logger.error("Excepcion en el login", ex);
				throw ex;
			}
			SSLSocketFactory sf = new SSLSocketFactory(sslContext, this);
			Scheme httpsScheme = new Scheme("https", u.getPort() == -1 ? 443
					: u.getPort(), sf);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(httpsScheme);
			Scheme httpScheme = new Scheme("http", 80, new PlainSocketFactory());
			schemeRegistry.register(httpScheme);

			ClientConnectionManager cm = new SingleClientConnManager(
					schemeRegistry);
			httpClient = new DefaultHttpClient(cm);
		} else {
			httpClient = new DefaultHttpClient();
		}
		if (null != proxyHost) {
			if (logger.isDebugEnabled()) {
				logger.debug("Existe proxy: " + this);
			}
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		return httpClient;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.
	 *      X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] arg0,
			String arg1) throws CertificateException {
		logger.debug("checkClientTrusted(..)");

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.
	 *      X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		logger.debug("checkServerTrusted(..)");

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		logger.debug("getAcceptedIssuers(..)");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String,
	 *      javax.net.ssl.SSLSocket)
	 */
	@Override
	public void verify(String host, SSLSocket ssl) throws IOException {
		logger.debug("verify(..)");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String,
	 *      java.security.cert.X509Certificate)
	 */
	@Override
	public void verify(String host, X509Certificate cert) throws SSLException {
		logger.debug("verify(..)");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.http.conn.ssl.X509HostnameVerifier#verify(java.lang.String,
	 *      java.lang.String[], java.lang.String[])
	 */
	@Override
	public void verify(String host, String[] cns, String[] subjectAlts)
			throws SSLException {
		logger.debug("verify(..)");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
	 *      javax.net.ssl.SSLSession)
	 */
	@Override
	public boolean verify(String hostname, SSLSession session) {
		logger.debug("verify(..)");
		return true;
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
