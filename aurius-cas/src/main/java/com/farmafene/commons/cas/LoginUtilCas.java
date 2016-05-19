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

import java.util.Properties;

import org.apache.http.cookie.Cookie;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.util.ILoginUtil;
import com.farmafene.aurius.util.IOnInitValidate;

public class LoginUtilCas implements ILoginUtil, IOnInitValidate {

	private LoginTGT loginTGT;
	private String casServerURL;
	private String casTGCName;
	private String jSessionCookieName;

	public LoginUtilCas() {
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
		sb.append("loginTGT=").append(loginTGT);
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
		this.loginTGT = new LoginTGT();
		loginTGT.setCasServerURL(casServerURL);
		loginTGT.setCasTGCName(casTGCName);
		loginTGT.setJSessionCookieName(jSessionCookieName);
		loginTGT.afterPropertiesSet();
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
			pass.setLogin(props.getProperty("user.login"));
			pass.setPassword(props.getProperty("user.password"));
		} catch (Throwable th) {
			throw new AuriusAuthException(th);
		}
		return pass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.shell.ILoginUtil#processLogin(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public AuthInfo processLogin(AuthInfo info) throws AuriusAuthException {
		Cookie tGT = loginTGT.doLogin(info.unwrap(AuthInfoLoginPassword.class)
				.getLogin(), info.unwrap(AuthInfoLoginPassword.class)
				.getPassword());
		AuthInfoCas salida = null;
		if (null != tGT) {
			salida = new AuthInfoCas();
			salida.setCookie(tGT);
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.ILoginUtil#getServerAuthInfo(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public AuthInfo getServerAuthInfo(AuthInfo info) {
		return info.unwrap(AuthInfoCas.class).getServerAuthInfo();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.shell.ILoginUtil#processLogout(com.farmafene.aurius.AuthInfo)
	 */
	@Override
	public void processLogout(AuthInfo info) {
		loginTGT.doLogout(info.unwrap(AuthInfoCas.class).getCookie());
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
	 * @return the casTGCName
	 */
	public String getCasTGCName() {
		return casTGCName;
	}

	/**
	 * @param casTGCName
	 *            the casTGCName to set
	 */
	public void setCasTGCName(String casTGCName) {
		this.casTGCName = casTGCName;
	}

	/**
	 * @return the jSessionCookieName
	 */
	public String getjSessionCookieName() {
		return jSessionCookieName;
	}

	/**
	 * @param jSessionCookieName
	 *            the jSessionCookieName to set
	 */
	public void setjSessionCookieName(String jSessionCookieName) {
		this.jSessionCookieName = jSessionCookieName;
	}
}
