/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.jca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.commons.cas.AuthInfoCas;
import com.farmafene.commons.cas.AuthInfoLoginPassword;
import com.farmafene.commons.cas.LoginUtilCas;

public class LoginUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(LoginUtil.class);

	private LoginUtilCas l;
	private AuthInfo info;

	public LoginUtil() {
		l = new LoginUtilCas();
		l.setCasTGCName("AuriusTGC");
		l.setCasServerURL("http://localhost:8080/aurius/cas");
		l.afterPropertiesSet();
	}

	public AuthInfo getAuthInfo() {
		AuthInfo info = null;
		if (this.info == null) {
			AuthInfoLoginPassword cred = new AuthInfoLoginPassword();
			cred.setLogin("vlopezm");
			cred.setPassword("vlopezm");
			this.info = l.processLogin(cred);
			logger.info("Recuperado: " + info);
			info = this.info.unwrap(AuthInfoCas.class).getServerAuthInfo();
			logger.info("Recuperado: " + info);
		}
		return info;
	}

	public void logout() {
		if (this.info != null) {
			AuthInfo i = this.info;
			this.info = null;
			l.processLogout(i);
		}
	}

	public static void main(String... args) {
		LoginUtil l = new LoginUtil();
		logger.info("Login info: {}" , l.getAuthInfo());
		l.logout();
	}
}
