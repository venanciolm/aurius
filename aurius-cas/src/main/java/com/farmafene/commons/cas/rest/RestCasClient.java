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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.farmafene.commons.cas.rest.URLConnectionUtil.Method;

public class RestCasClient {

	private static final String REGEXP_TGT = ".*action=\".*/(.*?)\".*";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String SERVICE = "service";
	private static final String TICKET = "ticket";
	private String serverBase;
	private String restServlet = "/v1/tickets";
	private String encoding = URLConnectionUtil.ENCODING;
	private int connectTimeoutMs = URLConnectionUtil.CONNECT_TIMEOUT_DEFAULT;
	private int readTimeoutMs = URLConnectionUtil.READ_TIMEOUT_DEFAULT;

	public RestCasClient() {

	}

	public String getTicketGrantingTicket(String username, String password)
			throws IOException {
		HttpURLConnection con = URLConnectionUtil.getHttpConnection(serverBase
				+ restServlet, Method.POST, connectTimeoutMs, readTimeoutMs,
				encoding, URLConnectionUtil.CONTENT_TYPE);

		Map<String, String> items = new HashMap<String, String>();
		items.put(USERNAME, username);
		items.put(PASSWORD, password);
		String urlParameters = URLConnectionUtil.urlEncode(items, encoding);
		byte[] postData = urlParameters.getBytes(con
				.getRequestProperty(URLConnectionUtil.ACCEPT_ENCODING));
		int postDataLength = postData.length;
		con.setRequestProperty(URLConnectionUtil.CONTENT_LENGTH,
				Integer.toString(postDataLength));
		con.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());

		BufferedReader br = new BufferedReader(isr);
		String response = br.readLine();
		con.disconnect();
		Matcher matcher = Pattern.compile(REGEXP_TGT).matcher(response);
		if (matcher.matches()) {
			response = matcher.group(1);
		}
		return response;
	}

	public String getServiceTicket(String ticketGrantingTicket, String service)
			throws IOException {
		String ticket = null;
		if (ticketGrantingTicket == null) {
			return ticket;
		}
		HttpURLConnection con = URLConnectionUtil.getHttpConnection(serverBase
				+ restServlet + "/" + ticketGrantingTicket, Method.POST,
				connectTimeoutMs, readTimeoutMs, encoding,
				URLConnectionUtil.CONTENT_TYPE);

		Map<String, String> items = new HashMap<String, String>();
		items.put(SERVICE, service);
		String urlParameters = URLConnectionUtil.urlEncode(items, encoding);
		byte[] postData = urlParameters.getBytes(con
				.getRequestProperty(URLConnectionUtil.ACCEPT_ENCODING));
		int postDataLength = postData.length;
		con.setRequestProperty(URLConnectionUtil.CONTENT_LENGTH,
				Integer.toString(postDataLength));
		con.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		ticket = br.readLine();
		con.disconnect();
		return ticket;
	}

	public String validate(String service, String serviceTicket)
			throws IOException {
		String user = null;
		Map<String, String> items = new HashMap<String, String>();
		items.put(TICKET, serviceTicket);
		items.put(SERVICE, service);
		String urlParameters = URLConnectionUtil.urlEncode(items, encoding);
		HttpURLConnection con = URLConnectionUtil.getHttpConnection(serverBase
				+ "/validate?" + urlParameters, Method.GET, connectTimeoutMs,
				readTimeoutMs, encoding, URLConnectionUtil.CONTENT_TYPE);
		con.setUseCaches(false);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		if ("yes".equals(br.readLine())) {
			user = br.readLine();
		}
		con.disconnect();
		return user;
	}

	public void logout(String ticketGrantingTicket) throws IOException {
		HttpURLConnection con = URLConnectionUtil.getHttpConnection(serverBase
				+ restServlet + "/" + ticketGrantingTicket, Method.DELETE,
				connectTimeoutMs, readTimeoutMs, encoding,
				URLConnectionUtil.CONTENT_TYPE);
		con.setUseCaches(false);
		con.disconnect();
	}

	public String getServerBase() {
		return serverBase;
	}

	public void setServerBase(String serverBase) {
		this.serverBase = serverBase;
	}

	public String getRestServlet() {
		return restServlet;
	}

	public void setRestServlet(String restServlet) {
		this.restServlet = restServlet;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}
}
