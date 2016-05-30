/*
 * Copyright (c) 2009-2016 farmafene.com
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
package com.farmafene.commons.cas.rest;

import java.util.Properties;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.util.ILoginUtil;
import com.farmafene.aurius.util.IOnInitValidate;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.WrapperClassContainer;
import com.farmafene.commons.cas.AuthInfoLoginPassword;

@SuppressWarnings("serial")
public class LoginUtilCasRest implements ILoginUtil, IOnInitValidate,
		IWrapperClass {

	private static final String USER_PASSWORD_PROP = "user.password";
	private static final String USER_LOGIN_PROP = "user.login";
	private static final String RESP_PATH_DEFAULT = "/v1/tickets";
	
	private String casServerURL;
	private String restPath = RESP_PATH_DEFAULT;
	private RestCasClient client;
	private IWrapperClass wrapperClass;

	public LoginUtilCasRest() {
		wrapperClass = new WrapperClassContainer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IOnInitValidate#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		this.client = new RestCasClient();
		client.setServerBase(casServerURL);
		client.setRestServlet(restPath);
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
		sb.append(client);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.ILoginUtil#createInfo(java.util.Properties)
	 */
	@Override
	public AuthInfo createInfo(Properties props) throws AuriusAuthException {
		AuthInfoLoginPassword pass = null;
		try {
			pass = new AuthInfoLoginPassword();
			pass.setLogin(props.getProperty(USER_LOGIN_PROP));
			pass.setPassword(props.getProperty(USER_PASSWORD_PROP));
		} catch (Throwable th) {
			throw new AuriusAuthException(th);
		}
		return pass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.ILoginUtil#processLogin(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public AuthInfo processLogin(AuthInfo info) throws AuriusAuthException {
		String ticketGrantingTicket = client.getTicketGrantingTicket(info
				.unwrap(AuthInfoLoginPassword.class).getLogin(),
				info.unwrap(AuthInfoLoginPassword.class).getPassword());
		AuthInfoCas salida = null;
		if (null != ticketGrantingTicket) {
			salida = new AuthInfoCas();
			salida.setTicketGrantingTicket(ticketGrantingTicket);
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.ILoginUtil#processLogout(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public void processLogout(AuthInfo info) {
		if (null == info) {
			throw new AuriusAuthException(new NullPointerException(
					"Info must be not null!"));
		}
		if (info.isWrapperFor(AuthInfoCas.class)) {
			client.logout(info.unwrap(AuthInfoCas.class)
					.getTicketGrantingTicket());
		} else {
			throw new AuriusAuthException(String.format(
					"Invalid auth: %1$s vs. %2$s", info.getClass()
							.getCanonicalName(), AuthInfoCas.class
							.getCanonicalName()));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.ILoginUtil#getServerAuthInfo(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public AuthInfo getServerAuthInfo(AuthInfo info) {
		return info.unwrap(AuthInfoCas.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> iface) {
		return wrapperClass.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) {
		return wrapperClass.unwrap(iface);
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
	 * @return the restPath
	 */
	public String getRestPath() {
		return restPath;
	}

	/**
	 * @param restPath
	 *            the restPath to set
	 */
	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	/**
	 * @return the client
	 */
	public RestCasClient getClient() {
		return client;
	}
}
