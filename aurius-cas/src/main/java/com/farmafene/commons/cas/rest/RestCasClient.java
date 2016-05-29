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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestCasClient {

	private static enum Method {
		POST, PUT, DELETE, GET
	};

	private static final Logger logger = LoggerFactory
			.getLogger(RestCasClient.class);
	private static final String REGEXP_TGT = ".*action=\".*/(.*?)\".*";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String SERVICE = "service";
	private static final String TICKET = "ticket";
	private static final String URL_ENCODED = "application/x-www-form-urlencoded";
	private static final String ENCODING = "UTF-8";
	private static final int READ_TIMEOUT_DEFAULT = 60000;
	private static final int CONNECT_TIMEOUT_DEFAULT = 60000;
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private String serverBase;
	private String restServlet = "/v1/tickets";
	private String encoding = ENCODING;
	private int connectTimeoutMs = CONNECT_TIMEOUT_DEFAULT;
	private int readTimeoutMs = READ_TIMEOUT_DEFAULT;

	public RestCasClient() {

	}

	HttpURLConnection getHttpConnection(String url, Method type,
			int connectTimeoutMs, int readTimeoutMs, String charEncoding,
			String contentType) throws ProtocolException, IOException,
			MalformedURLException {
		URL uri = null;
		HttpURLConnection con = null;
		uri = new URL(url);
		con = (HttpURLConnection) uri.openConnection();
		con.setRequestMethod(type.toString());
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setConnectTimeout(connectTimeoutMs);
		con.setReadTimeout(readTimeoutMs);
		con.setRequestProperty(ACCEPT_ENCODING, charEncoding);
		con.setRequestProperty(CONTENT_TYPE, contentType);
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

	public String getTicketGrantingTicket(String username, String password) {
		HttpURLConnection con = null;
		InputStream is = null;
		String response = null;
		try {
			con = getHttpConnection(serverBase + restServlet, Method.POST,
					connectTimeoutMs, readTimeoutMs, encoding, URL_ENCODED);
			Map<String, String> items = new HashMap<String, String>();
			items.put(USERNAME, username);
			items.put(PASSWORD, password);
			String urlParameters = urlEncode(items, encoding);
			byte[] postData = urlParameters.getBytes(con
					.getRequestProperty(ACCEPT_ENCODING));
			int postDataLength = postData.length;
			con.setRequestProperty(CONTENT_LENGTH,
					Integer.toString(postDataLength));
			con.setUseCaches(false);
			con.connect();
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			con.connect();
			switch (con.getResponseCode()) {
			case HttpURLConnection.HTTP_CREATED:
				// reading the TGT
				is = con.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				response = br.readLine();
				Matcher matcher = Pattern.compile(REGEXP_TGT).matcher(response);
				if (matcher.matches()) {
					response = matcher.group(1);
				}
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
				// for example: invalid
				break;
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
				// the documentation say it is posible
				break;
			default:
				// other thing!
			}
		} catch (IOException e) {
			// TODO - error in connection!
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return response;
	}

	public String getServiceTicket(String ticketGrantingTicket, String service)
			throws IOException {
		String ticket = null;
		if (ticketGrantingTicket == null) {
			return ticket;
		}
		HttpURLConnection con = null;
		try {
			con = getHttpConnection(serverBase + restServlet + "/"
					+ ticketGrantingTicket, Method.POST, connectTimeoutMs,
					readTimeoutMs, encoding, URL_ENCODED);

			Map<String, String> items = new HashMap<String, String>();
			items.put(SERVICE, service);
			String urlParameters = urlEncode(items, encoding);
			byte[] postData = urlParameters.getBytes(con
					.getRequestProperty(ACCEPT_ENCODING));
			int postDataLength = postData.length;
			con.setRequestProperty(CONTENT_LENGTH,
					Integer.toString(postDataLength));
			con.setUseCaches(false);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			con.connect();
			switch (con.getResponseCode()) {
			case HttpURLConnection.HTTP_OK:
				// reading the ticket
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				ticket = br.readLine();
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
				// for example: invalid
				break;
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
				// the documentation say it is posible
				break;
			default:
				// other thing!
			}
		} catch (IOException e) {
			// TODO - error in connection!
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return ticket;
	}

	public String validate(String service, String serviceTicket)
			throws IOException {
		String user = null;
		Map<String, String> items = new HashMap<String, String>();
		items.put(TICKET, serviceTicket);
		items.put(SERVICE, service);
		String urlParameters = urlEncode(items, encoding);
		HttpURLConnection con = getHttpConnection(serverBase + "/validate?"
				+ urlParameters, Method.GET, connectTimeoutMs, readTimeoutMs,
				encoding, CONTENT_TYPE);
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
		HttpURLConnection con = null;
		try {
			con = getHttpConnection(serverBase + restServlet + "/"
					+ ticketGrantingTicket, Method.DELETE, connectTimeoutMs,
					readTimeoutMs, encoding, CONTENT_TYPE);
			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				// do nothing
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				logger.debug("El código de respuesta es: {}", responseCode);
				// TODO 
			}

		} catch (IOException e) {
			// todo
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
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
