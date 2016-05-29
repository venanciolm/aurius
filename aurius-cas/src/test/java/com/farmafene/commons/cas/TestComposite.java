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
package com.farmafene.commons.cas;

import java.util.ArrayList;
import java.util.List;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * @author vlopez
 * 
 */
public class TestComposite {

	public static void main(String... args) throws AuthenticationException {
		List<AuthenticationHandler> lista = new ArrayList<AuthenticationHandler>();
		CompositeAuthenticationHandler ch = new CompositeAuthenticationHandler();
		ch.setHandlers(lista);
		URLAuthenticationHandler h1 = new URLAuthenticationHandler();
		h1.setLoginURL("http://vlopezm/sites/redmine/users/current.json");
		NTLMAuthenticationHandler h2 = new NTLMAuthenticationHandler();
		h2.setDomainController("VLOPEZM-W7");
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
		credentials.setUsername("vlopezm");
		credentials.setPassword("xxx");
		lista.add(h1);
		lista.add(h2);
		System.out.println(ch.authenticate(credentials));
	}

}
