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
import java.io.StringReader;
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
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.farmafene.aurius.AuriusAuthException;

public class RestCasClient {

	private static final String YES = "yes";

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

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("serverBase=").append(serverBase);
		sb.append(", restServlet=").append(restServlet);
		sb.append("}");
		return sb.toString();
	}

	private HttpURLConnection getHttpConnection(String url, Method type,
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

	private String urlEncode(Map<String, String> items, String enc)
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

	/**
	 * Get an instance of an XML reader from the XMLReaderFactory.
	 * 
	 * @return the XMLReader.
	 */
	private XMLReader getXmlReader() {
		try {
			return XMLReaderFactory.createXMLReader();
		} catch (final SAXException e) {
			throw new RuntimeException("Unable to create XMLReader", e);
		}
	}

	/**
	 * Retrieve the text for a specific element (when we know there is only
	 * one).
	 * 
	 * @param xmlAsString
	 *            the xml response
	 * @param element
	 *            the element to look for
	 * @return the text value of the element.
	 */
	private String getTextForElement(final String xmlAsString,
			final String element) {
		final XMLReader reader = getXmlReader();
		final StringBuffer buffer = new StringBuffer();

		final DefaultHandler handler = new DefaultHandler() {

			private boolean foundElement = false;

			public void startElement(final String uri, final String localName,
					final String qName, final Attributes attributes)
					throws SAXException {
				if (localName.equals(element)) {
					this.foundElement = true;
				}
			}

			public void endElement(final String uri, final String localName,
					final String qName) throws SAXException {
				if (localName.equals(element)) {
					this.foundElement = false;
				}
			}

			public void characters(char[] ch, int start, int length)
					throws SAXException {
				if (this.foundElement) {
					buffer.append(ch, start, length);
				}
			}
		};

		reader.setContentHandler(handler);
		reader.setErrorHandler(handler);

		try {
			reader.parse(new InputSource(new StringReader(xmlAsString)));
		} catch (final Exception e) {
			logger.error("", e);
			return null;
		}

		return buffer.toString();
	}

	public String getTicketGrantingTicket(String username, String password)
			throws AuriusAuthException {
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
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.debug("El response de getTicketGrantingTicket es: '{}'",
					responseCode);
			switch (responseCode) {
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
				throw new AuriusAuthException("Invalid credentials");
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return response;
	}

	public String getServiceTicket(String ticketGrantingTicket, String service)
			throws AuriusAuthException {
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
			int responseCode = con.getResponseCode();
			logger.debug("El response de getServiceTicket es: '{}'",
					responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				// reading the ticket
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				ticket = br.readLine();
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				logger.warn("Invalid request: {} - {}", responseCode,
						con.getResponseMessage());
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return ticket;
	}

	public String getProxyServiceTicket(String ticketGrantingTicket,
			String service) throws AuriusAuthException {
		String ticket = null;
		if (ticketGrantingTicket == null) {
			return ticket;
		}
		HttpURLConnection con = null;
		try {
			Map<String, String> items = new HashMap<String, String>();
			items.put("pgt", ticketGrantingTicket);
			items.put("targetService", service);
			String urlParameters = urlEncode(items, encoding);
			con = getHttpConnection(serverBase + "/proxy?" + urlParameters,
					Method.GET, connectTimeoutMs, readTimeoutMs, encoding,
					URL_ENCODED);

			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.debug("El response de getProxyServiceTicket es: '{}'",
					responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				// reading the ticket
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				ticket = br.readLine();
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				logger.warn("Invalid request: {} - {}", responseCode,
						con.getResponseMessage());
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return ticket;
	}

	public String validate(String service, String serviceTicket)
			throws AuriusAuthException {
		String user = null;
		Map<String, String> items = new HashMap<String, String>();
		items.put(TICKET, serviceTicket);
		items.put(SERVICE, service);
		HttpURLConnection con = null;
		try {
			String urlParameters = urlEncode(items, encoding);
			con = getHttpConnection(
					this.serverBase
							+ (this.serverBase.endsWith("/") ? "" : "/")
							+ "validate?" + urlParameters, Method.GET,
					connectTimeoutMs, readTimeoutMs, encoding, CONTENT_TYPE);
			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.debug("El response de validate es: '{}'", responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				if (YES.equals(br.readLine())) {
					user = br.readLine();
				} else {
					throw new AuriusAuthException("Invalid ticket: " + service
							+ "-" + serviceTicket);
				}
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return user;
	}

	public String proxyValidate(String service, String serviceTicket)
			throws AuriusAuthException {
		String user = null;
		Map<String, String> items = new HashMap<String, String>();
		items.put(TICKET, serviceTicket);
		items.put(SERVICE, service);
		HttpURLConnection con = null;
		try {
			String urlParameters = urlEncode(items, encoding);
			con = getHttpConnection(
					this.serverBase
							+ (this.serverBase.endsWith("/") ? "" : "/")
							+ "proxyValidate?" + urlParameters, Method.GET,
					connectTimeoutMs, readTimeoutMs, encoding, CONTENT_TYPE);
			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.debug("El response de validate es: '{}'", responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				InputStreamReader isr = new InputStreamReader(
						con.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String line = br.readLine();
				if (YES.equals(line)) {
					user = line;
				} else {
					logger.info(line);
					while (br.ready()) {
						logger.info(br.readLine());
					}
					throw new AuriusAuthException("Invalid ticket: " + service
							+ "-" + serviceTicket);
				}
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
		return user;
	}

	public void logout(String ticketGrantingTicket) throws AuriusAuthException {
		HttpURLConnection con = null;
		try {
			con = getHttpConnection(serverBase + restServlet + "/"
					+ ticketGrantingTicket, Method.DELETE, connectTimeoutMs,
					readTimeoutMs, encoding, CONTENT_TYPE);
			con.setUseCaches(false);
			con.connect();
			int responseCode = con.getResponseCode();
			logger.debug("El response de logout es: '{}'", responseCode);
			switch (responseCode) {
			case HttpURLConnection.HTTP_OK:
				// do nothing
				break;
			case HttpURLConnection.HTTP_BAD_REQUEST:
			case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
			default:
				throw new AuriusAuthException("Invalid request " + responseCode
						+ " - " + con.getResponseMessage());
			}
		} catch (IOException e) {
			throw new AuriusAuthException(e);
		} finally {
			if (null != con) {
				con.disconnect();
			}
		}
	}

	/**
	 * @return the serverBase
	 */
	public String getServerBase() {
		return serverBase;
	}

	/**
	 * @param serverBase
	 *            the serverBase to set
	 */
	public void setServerBase(String serverBase) {
		this.serverBase = serverBase;
	}

	/**
	 * @return the restServlet
	 */
	public String getRestServlet() {
		return restServlet;
	}

	/**
	 * @param restServlet
	 *            the restServlet to set
	 */
	public void setRestServlet(String restServlet) {
		this.restServlet = restServlet;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the connectTimeoutMs
	 */
	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	/**
	 * @param connectTimeoutMs
	 *            the connectTimeoutMs to set
	 */
	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	/**
	 * @return the readTimeoutMs
	 */
	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	/**
	 * @param readTimeoutMs
	 *            the readTimeoutMs to set
	 */
	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}
}
