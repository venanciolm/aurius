/*
 * Copyright (c) 2009-2012 farmafene.com
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.util.Assertion;
import com.farmafene.aurius.util.IOnInitValidate;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class LoginTGT implements IOnInitValidate {

	private static final Logger logger = LoggerFactory
			.getLogger(LoginTGT.class);
	private String casServerURL;
	private String casTGCName;
	private String jSessionCookieName;

	public LoginTGT() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("casServerURL=").append(casServerURL);
		sb.append(", casTGCName=").append(casTGCName);
		sb.append(", jSessionCookieName=").append(jSessionCookieName);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.shell.utils.IOnInitValidate#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		Assertion.notNull(casServerURL, "Url del servidor no puede ser null");
		Assertion.notNull(casTGCName,
				"El nombre de la cookie no puede ser null");
		if (null == jSessionCookieName) {
			this.jSessionCookieName = "JSESSIONID";
		}
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<afterPropertiesSet>");
		}
	}

	public Cookie doLogin(String user, String password) {
		Cookie doLogin = null;
		LoginTicketContainer lt = processInitRequest();
		doLogin = doLogin(lt, user, password);
		return doLogin;
	}

	/**
	 * @param lt
	 * @param user
	 * @param password
	 * @return
	 */
	private Cookie doLogin(LoginTicketContainer lt, String user, String password) {
		return doLoginCookie(lt, user, password);
	}

	public void doLogout(Cookie cookie) {
		HttpResponse res = null;
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(getCasServerURL());
		DefaultHttpClient client = null;
		client = f.getClient();
		HttpContext localContext = new BasicHttpContext();
		BasicCookieStore cs = new BasicCookieStore();
		cs.addCookie(cookie);

		HttpPost post = new HttpPost(getURL("logout"));
		client.setCookieStore(cs);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cs);
		try {
			res = client.execute(post, localContext);
			if (res.getStatusLine().getStatusCode() != 200) {
				AuriusAuthException ex = new AuriusAuthException("Error");
				logger.error("Excepcion en el login", ex);
				throw ex;
			}
		} catch (ClientProtocolException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"ClientProtocolException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
	}

	/**
	 * @param lt
	 * @param user
	 * @param password
	 * @return
	 */
	private Cookie doLoginCookie(LoginTicketContainer lt, String user,
			String password) {
		Cookie doLoginCookie = null;
		HttpResponse res = null;
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(getCasServerURL());
		DefaultHttpClient client = null;
		client = f.getClient();
		HttpContext localContext = new BasicHttpContext();
		BasicCookieStore cs = new BasicCookieStore();
		cs.addCookie(lt.getSessionCookie());
		HttpPost post = new HttpPost(getURL("login"));
		client.setCookieStore(cs);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cs);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("lt", lt.getLoginTicket()));
		nameValuePairs.add(new BasicNameValuePair("execution", lt
				.getExecution()));
		nameValuePairs.add(new BasicNameValuePair("_eventId", "submit"));
		nameValuePairs.add(new BasicNameValuePair("username", user));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"UnsupportedEncodingException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		try {
			res = client.execute(post, localContext);
			if (res.getStatusLine().getStatusCode() != 200) {
				AuriusAuthException ex = new AuriusAuthException("Error");
				logger.error("Excepcion en el login", ex);
				throw ex;
			}
		} catch (ClientProtocolException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"ClientProtocolException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		if (client.getCookieStore().getCookies() != null) {
			for (Cookie c : client.getCookieStore().getCookies()) {
				if (getCasTGCName().equals(c.getName())) {
					doLoginCookie = c;
					break;
				}
			}
		}
		if (doLoginCookie == null) {
			throw new AuriusAuthException("No se ha logrado el login");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Obtenido: " + doLoginCookie);
		}
		return doLoginCookie;
	}

	/**
	 * @return
	 */
	private LoginTicketContainer processInitRequest() {
		LoginTicketContainer processInitRequest = null;
		HttpResponse res = null;
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(getCasServerURL());
		DefaultHttpClient client = null;
		client = f.getClient();
		HttpContext localContext = new BasicHttpContext();
		BasicCookieStore cs = new BasicCookieStore();
		HttpPost post = new HttpPost(getURL("login"));
		client.setCookieStore(cs);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cs);
		try {
			res = client.execute(post, localContext);
		} catch (ClientProtocolException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"ClientProtocolException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		InputStream is = null;
		try {
			is = res.getEntity().getContent();
		} catch (IllegalStateException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"IllegalStateException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int leido = 0;
		try {
			while ((leido = is.read(buffer)) > 0) {
				baos.write(buffer, 0, leido);
			}
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		String html = baos.toString().replace("\n", "").replace("\r", "");
		/*
		 * Buscamos los tipos de "input"
		 */
		String[] inputs = html.split("<\\s*input\\s+");
		processInitRequest = new LoginTicketContainer();
		for (String input : inputs) {
			String value = null;
			if (null != (value = search(input, "lt"))) {
				processInitRequest.setLoginTicket(value);
			} else if (null != (value = search(input, "execution"))) {
				processInitRequest.setExecution(value);
			}
		}
		/*
		 * Obtenemos la session Cookie
		 */
		if (client.getCookieStore().getCookies() != null) {
			for (Cookie c : client.getCookieStore().getCookies()) {
				if (getJSessionCookieName().equals(c.getName())) {
					processInitRequest.setSessionCookie(c);
				}
			}
		}
		if (null != baos) {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el OutputStream", e);
			}
		}
		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el InputStream", e);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Obtenido: " + processInitRequest);
		}
		return processInitRequest;
	}

	/**
	 * @param target
	 *            cadena a buscar el atributo
	 * @param name
	 *            atributo a buscar
	 * @return el valor del atributo, si existe (<code>null</code> si no
	 *         existe);
	 */
	private String search(String target, String name) {
		String search = null;
		if (target != null) {
			if (Pattern.matches(".*\\sname\\s*=\"" + name + "\"\\s.*", target)) {
				Pattern p = Pattern.compile(".*\\svalue\\s*=\"(.*)\"\\s.*");
				Matcher m = p.matcher(target);
				if (m.matches()) {
					search = m.group(1);
				}
			}
		}
		return search;
	}

	/**
	 * @param command
	 *            url a buscar
	 * @return url del comando
	 */
	private String getURL(String command) {
		return casServerURL.trim().endsWith("/") ? casServerURL.trim()
				+ command : casServerURL.trim() + "/" + command;
	}

	/**
	 * @return el casServerURL
	 */
	public String getCasServerURL() {
		return casServerURL;
	}

	/**
	 * @param casServerURL
	 *            el casServerURL a establecer
	 */
	public void setCasServerURL(String casServerURL) {
		this.casServerURL = casServerURL;
	}

	/**
	 * @return el casTGCName
	 */
	public String getCasTGCName() {
		return casTGCName;
	}

	/**
	 * @param casTGCName
	 *            el casTGCName a establecer
	 */
	public void setCasTGCName(String casTGCName) {
		this.casTGCName = casTGCName;
	}

	/**
	 * @return el jSessionCookieName
	 */
	public String getJSessionCookieName() {
		return jSessionCookieName;
	}

	/**
	 * @param jSessionCookieName
	 *            el jSessionCookieName a establecer
	 */
	public void setJSessionCookieName(String jSessionCookieName) {
		this.jSessionCookieName = jSessionCookieName;
	}
}
