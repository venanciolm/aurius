/**
 * 
 */
package com.farmafene.commons.cas;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * @author Indra
 * 
 */
public class TestRedmine {

	public static void main(String... args) throws AuthenticationException {
		URLAuthenticationHandler h = new URLAuthenticationHandler();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
		credentials.setUsername("vlopez");
		credentials.setPassword("xxx");
		h.setLoginURL("https://cas.farmafene.com/users/current.json");
		System.out.println(h.authenticate(credentials));
	}

}
