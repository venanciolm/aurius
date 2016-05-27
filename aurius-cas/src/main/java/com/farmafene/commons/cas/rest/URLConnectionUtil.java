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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLConnectionUtil {

	// cas-server-integration-restlet-3.4.11.jar
	// com.noelios.restlet.ext.spring-1.1.1.jar
	// com.noelios.restlet.ext.servlet-1.1.1.jar
	// org.restlet.ext.spring-1.1.1.jar
	// com.noelios.restlet-1.1.1.jar
	// org.restlet-1.1.1.jar
	// cglib-nodep-2.1_3.jar
	//
	//
	// <servlet>
	// <servlet-name>restlet</servlet-name>
	// <servlet-class>com.noelios.restlet.ext.spring.RestletFrameworkServlet</servlet-class>
	// <load-on-startup>1</load-on-startup>
	// </servlet>
	// <servlet-mapping>
	// <servlet-name>restlet</servlet-name>
	// <url-pattern>/v1/*</url-pattern>
	// </servlet-mapping>
	public static enum Method {
		POST, PUT, DELETE, GET
	};

	static final String URL_ENCODED = "application/x-www-form-urlencoded";
	static final String ENCODING = "UTF-8";
	static final int READ_TIMEOUT_DEFAULT = 60000;
	static final int CONNECT_TIMEOUT_DEFAULT = 60000;
	static final String CONTENT_LENGTH = "Content-Length";
	static final String CONTENT_TYPE = "Content-Type";
	static final String ACCEPT_ENCODING = "Accept-Encoding";

	private static final Logger logger = LoggerFactory
			.getLogger(URLConnectionUtil.class);

	private URLConnectionUtil() {
		// by design
	}

	public static HttpURLConnection getHttpConnection(String url, Method type,
			int connectTimeoutMs, int readTimeoutMs, String charEncoding,
			String contentType) {
		URL uri = null;
		HttpURLConnection con = null;
		try {
			uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod(type.toString());
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setConnectTimeout(connectTimeoutMs);
			con.setReadTimeout(readTimeoutMs);
			con.setRequestProperty(ACCEPT_ENCODING, charEncoding);
			con.setRequestProperty(CONTENT_TYPE, contentType);
		} catch (Exception e) {
			logger.info("connection i/o failed");
		}
		return con;
	}

	public static String urlEncode(Map<String, String> items, String enc)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (String var : items.keySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(URLEncoder.encode(var, enc));
			sb.append("=");
			sb.append(URLEncoder.encode(items.get(var), enc));
		}
		return sb.toString();
	}

	public static void main(String... args) throws IOException {
		String serverUrl = "https://cas.farmafene.com";
		String username = "<user>";
		String password = "<password>";
		String service1 = "https://sample01.farmafene.com";
		String service2 = "https://sample02.farmafene.com";
		String service3 = "https://sample03.farmafene.com";
		RestCasClient client = new RestCasClient();
		client.setServerBase(serverUrl);

		String tgt = client.getTicketGrantingTicket(username, password);
		String ticket = null;
		String response = null;
		System.out.println(tgt);
		ticket = client.getServiceTicket(tgt, service1);
		System.out.println(ticket);
		response = client.validate(service1, ticket);
		System.out.println(response);
		ticket = client.getServiceTicket(tgt, service2);
		System.out.println(ticket);
		response = client.validate(service2, ticket);
		System.out.println(response);
		ticket = client.getServiceTicket(tgt, service3);
		System.out.println(ticket);
		response = client.validate(service3, ticket);
		System.out.println(response);
		client.logout(tgt);
	}
}
