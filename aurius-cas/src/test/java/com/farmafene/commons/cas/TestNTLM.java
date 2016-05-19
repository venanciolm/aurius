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
public class TestNTLM {

	public static void main(String... args) throws AuthenticationException {
		NTLMAuthenticationHandler h = new NTLMAuthenticationHandler();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
		credentials.setUsername("Indra");
		credentials.setPassword("1234567<8");
		h.setDomainController("VLOPEZM-W7");
		System.out.println(h.authenticate(credentials));
	}

}
