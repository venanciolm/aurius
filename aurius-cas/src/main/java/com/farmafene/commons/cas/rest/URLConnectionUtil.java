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
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLConnectionUtil {

	public static enum Method {
		POST, PUT, DELETE, GET
	};

	private static final String URL_ENCODED = "application/x-www-form-urlencoded";
	private static final String ENCODING = "UTF-8";
	private static final int READ_TIMEOUT_DEFAULT = 60000;
	private static final int CONNECT_TIMEOUT_DEFAULT = 60000;

	private static final Logger logger = LoggerFactory
			.getLogger(URLConnectionUtil.class);

	private URLConnectionUtil() {
		// by design
	}

	public static HttpURLConnection getHttpConnection(String url, Method type) {
		return getHttpConnection(url, type, CONNECT_TIMEOUT_DEFAULT,
				READ_TIMEOUT_DEFAULT, ENCODING, URL_ENCODED);
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
			con.setRequestProperty("Accept-Encoding", charEncoding);
			con.setRequestProperty("Content-Type", contentType);
		} catch (Exception e) {
			logger.info("connection i/o failed");
		}
		return con;
	}

	private static String getTicketGrantingTicket(String server,
			String username, String password) throws IOException {
		HttpURLConnection con = getHttpConnection(server, Method.POST);

		String urlParameters = String.format("username=%1s&password=%2s",
				username, password);
		System.out.println("url: " + urlParameters);
		byte[] postData = urlParameters.getBytes(con
				.getRequestProperty("Accept-Encoding"));
		int postDataLength = postData.length;
		con.setRequestProperty("Content-Length",
				Integer.toString(postDataLength));
		con.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());

		BufferedReader br = new BufferedReader(isr);
		String response = br.readLine();
		Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(
				response);
		if (matcher.matches()) {
			response = matcher.group(1);
		}

		return response;
	}

	private static String getServiceTicket(String server,
			String ticketGrantingTicket, String service) throws IOException {
		String ticket = null;
		if (ticketGrantingTicket == null) {
			return ticket;
		}
		HttpURLConnection con = getHttpConnection(server + "/"
				+ ticketGrantingTicket, Method.POST);

		String urlParameters = String.format("service=%1s", service);
		System.out.println("url: " + urlParameters);
		byte[] postData = urlParameters.getBytes(con
				.getRequestProperty("Accept-Encoding"));
		int postDataLength = postData.length;
		con.setRequestProperty("Content-Length",
				Integer.toString(postDataLength));
		con.setUseCaches(false);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());

		BufferedReader br = new BufferedReader(isr);
		ticket = br.readLine();
		return ticket;
	}

	private static String getValidateRaw(String serverUrl, String service,
			String serviceTicket) throws IOException {
		String user = null;
		String urlParameters = String.format("ticket=%1s&service=%2s",
				serviceTicket, URLEncoder.encode(service, ENCODING));
		System.out.println("url: " + urlParameters);
		HttpURLConnection con = getHttpConnection(serverUrl + "/" + "validate?"
				+ urlParameters, Method.GET);
		con.setUseCaches(false);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		if ("yes".equals(br.readLine())) {
			user = br.readLine();
		}
		return user;
	}

	private static void logout(String server, String ticketGrantingTicket)
			throws IOException {
		HttpURLConnection con = getHttpConnection(server + "/"
				+ ticketGrantingTicket, Method.DELETE);

		con.setUseCaches(false);
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		while (br.ready()) {
			System.out.println(br.readLine());
		}
	}

	public static void main(String... args) throws IOException {
		String serverUrl = "https://cas.farmafene.com";
		String server = serverUrl + "/v1/tickets";
		String username = "vlopez";
		String password = "XXXXX";
		String service1 = "https://cas01.farmafene.com";
		String service2 = "https://cas02.farmafene.com";
		String service3 = "https://cas03.farmafene.com";
		String tgt = getTicketGrantingTicket(server, username, password);
		String ticket = null;
		String response = null;
		System.out.println(tgt);
		ticket = getServiceTicket(server, tgt, service1);
		System.out.println(ticket);
		response = getValidateRaw(serverUrl, service1, ticket);
		System.out.println(response);
		ticket = getServiceTicket(server, tgt, service2);
		System.out.println(ticket);
		response = getValidateRaw(serverUrl, service2, ticket);
		System.out.println(response);
		ticket = getServiceTicket(server, tgt, service3);
		System.out.println(ticket);
		response = getValidateRaw(serverUrl, service3, ticket);
		System.out.println(response);
		logout(server, tgt);
	}
}
