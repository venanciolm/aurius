/**
 * 
 */
package com.farmafene.commons.cas;

import java.util.ArrayList;
import java.util.List;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * @author Indra
 * 
 */
public class TestComposite {

	public static void main(String... args) throws AuthenticationException {
		List<AuthenticationHandler> lista = new ArrayList<AuthenticationHandler>();
		CompositeAuthenticationHandler ch = new CompositeAuthenticationHandler();
		ch.setHandlers(lista);
		URLAuthenticationHandler h1 = new URLAuthenticationHandler();
		h1.setLoginURL("http://vlopezm-w7/sites/redmine/users/current.json");
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
