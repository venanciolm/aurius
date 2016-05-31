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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusAuthException;

/**
 * @see https://wiki.jasig.org/display/CASUM/RESTful+API
 */
public class RestCasClientTestJunit {

	private static final String USER_NAME = "vlopezm";
	private static final String PWD = "vlm##2016";
	private static final String CAS_SERVER = "https://visor-dev.seinsir.indra.es/cas";
	private static final Logger logger = LoggerFactory
			.getLogger(RestCasClientTestJunit.class);

	@Test
	public void main() throws IOException {
		String service1 = "https://<example1>.seinsir.indra.es";
		String service2 = "https://<example2>.seinsir.indra.es";
		String service3 = "https://<example3>.seinsir.indra.es";
		RestCasClient client = new RestCasClient();
		client.setServerBase(CAS_SERVER);

		String tgt = client.getTicketGrantingTicket(USER_NAME, PWD);
		Assert.assertNotNull(tgt);
		String ticket = null;
		String response = null;
		logger.info("El TGT es:       '{}'", tgt);
		//
		ticket = client.getServiceTicket(tgt, service1);
		logger.info("El ticket 1 es:  '{}'", ticket);
		response = client.validate(service1, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
		//
		AuriusAuthException authEx = null;
		response = null;
		ticket = client.getServiceTicket(tgt, service2);
		logger.info("El ticket 2 es:  '{}'", ticket + "-");
		try {
			response = client.validate(service2, ticket + "-");
		} catch (AuriusAuthException e) {
			authEx = e;
		}
		logger.info("La respuesta es: '{}'", response);
		Assert.assertNotNull(authEx);
		Assert.assertNull(response);
		authEx = null;
		response = null;
		try {
			response = client.validate(service3, ticket);
		} catch (AuriusAuthException e) {
			authEx = e;
		}
		logger.info("La respuesta es: '{}'", response);
		Assert.assertNotNull(authEx);
		Assert.assertNull(response);
		authEx = null;
		response = null;
		try {
			response = client.validate(service2, ticket);
		} catch (AuriusAuthException e) {
			authEx = e;
		}
		logger.info("La respuesta es: '{}'", response);
		Assert.assertNotNull(authEx);
		Assert.assertNull(response);
		authEx = null;
		response = null;
		//
		ticket = client.getServiceTicket(tgt, service3);
		logger.info("El ticket 3 es:  '{}'", ticket);
		response = client.validate(service3, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
		//
		logger.info("");
		logger.info("");
		logger.info("============ obteniendo pgt =============");
		logger.info("");
		ServiceValidateResponse out = null;
		ticket = client.getServiceTicket(tgt, service3);
		logger.info("El proxy ticket es:  '{}'", ticket);
		out = client
				.serviceValidate(service3, ticket, CAS_SERVER + "/casProxy");
		logger.info("La respuesta es: '{}'", out);
		logger.info("");
		logger.info("");
		logger.info("=========== trabajando con pgt ==========");
		logger.info("");
		ticket = client.getProxyServiceTicket(out.getProxyGrantingTicket(),
				service1);
		logger.info("La respuesta es: '{}'", ticket);
		response = client.proxyValidate(service1, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
		ticket = client.getProxyServiceTicket(out.getProxyGrantingTicket(),
				service2);
		logger.info("La respuesta es: '{}'", ticket);
		response = client.proxyValidate(service2, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
		ticket = client.getProxyServiceTicket(out.getProxyGrantingTicket(),
				service3);
		logger.info("La respuesta es: '{}'", ticket);
		response = client.proxyValidate(service3, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
		logger.info("");
		logger.info("");
		logger.info("=========================================");
		logger.info("");
		client.logout(tgt);
		logger.info(" tgt: {}",tgt);
		logger.info(" pgt: {}",out.getProxyGrantingTicket());
		logger.info("");
		logger.info("");
		logger.info("=========================================");
		logger.info("");
		ticket = client.getProxyServiceTicket(out.getProxyGrantingTicket(),
				service2);
		logger.info("La respuesta es: '{}'", ticket);
		response = client.proxyValidate(service2, ticket);
		logger.info("La respuesta es: '{}'", response);
		Assert.assertEquals(USER_NAME, response);
	}
}
