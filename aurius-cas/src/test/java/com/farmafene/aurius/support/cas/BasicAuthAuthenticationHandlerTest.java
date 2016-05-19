package com.farmafene.aurius.support.cas;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthAuthenticationHandlerTest {

	private static final Logger logger = LoggerFactory
			.getLogger(BasicAuthAuthenticationHandlerTest.class);

	public static void main(String... args) {
		BasicAuthAuthenticationHandler h = new BasicAuthAuthenticationHandler();

		h.setLoginURL("http://localhost/svn/");
		// h.setProxyHost("127.0.0.1");
		// h.setProxyPort(3128);
		System.out.println(h.toString());
		UsernamePasswordCredentials c = new UsernamePasswordCredentials();
		c.setUsername("vlopez");
		c.setPassword("71hidalgo");
		try {
			logger.info("Salida: " + h.authenticate(c));
		} catch (AuthenticationException e) {
			logger.error("",e);
		}
	}
}
