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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://wiki.jasig.org/display/CASUM/RESTful+API
 */
public class URLConnectionUtilMainTest {

	private static final Logger logger = LoggerFactory
			.getLogger(URLConnectionUtilMainTest.class);

	public static void main(String... args) throws IOException {
		String serverUrl = "https://<aaa>.yourdomain";
		String username = "<user>";
		String password = "<passwor>";
		String service1 = "https://<example1>.yourdomain";
		String service2 = "https://<example2>.yourdomain";
		String service3 = "https://<example3>.yourdomain";
		RestCasClient client = new RestCasClient();
		client.setServerBase(serverUrl);

		String tgt = client.getTicketGrantingTicket(username, password);
		String ticket = null;
		String response = null;
		logger.info("El TGT es:       '{}'", tgt);
		ticket = client.getServiceTicket(tgt, service1);
		logger.info("El ticket 1 es:  '{}'", ticket);
		response = client.validate(service1, ticket);
		logger.info("La respuesta es: '{}'", response);
		ticket = client.getServiceTicket(tgt, service2);
		logger.info("El ticket 2 es:  '{}'", ticket);
		response = client.validate(service2, ticket);
		logger.info("La respuesta es: '{}'", response);
		ticket = client.getServiceTicket(tgt, service3);
		logger.info("El ticket 3 es:  '{}'", ticket);
		response = client.validate(service3, ticket);
		logger.info("La respuesta es: '{}'", response);
		client.logout(tgt);
	}
}
