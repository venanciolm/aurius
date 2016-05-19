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
package com.farmafene.aurius.support.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class LoginUtilCasTest {
	private static final Logger logger = LoggerFactory
			.getLogger(LoginUtilCasTest.class);

	public static void main(String... args) throws IllegalArgumentException {
		LoginUtilCas l = new LoginUtilCas();
		ValidateTGT v = new ValidateTGT();
		l.setCasTGCName("AuriusTGC");
		l.setCasServerURL("http://localhost:8080/aurius/cas");
		v.setCasServiceName("Aurius");
		v.setCasServerURL(l.getCasServerURL());
		l.afterPropertiesSet();
		v.afterPropertiesSet();
		AuthInfoLoginPassword cred = new AuthInfoLoginPassword();
		cred.setLogin("vlopezm");
		cred.setPassword("vlopezm");
		AuthInfo info = l.processLogin(cred).unwrap(AuthInfoCas.class).getServerAuthInfo();
		String ticket1 = v.validateTGT(info);
		logger.info("Procesando ... " + ticket1);
		l.processLogout(info);
		logger.info("...Procesado");
		v.validateTGT(info.unwrap(AuthInfoCas.class).getServerAuthInfo());
	}
}
