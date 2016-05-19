/*
 * Copyright (c) 2009-2012 farmafene.com
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
package com.farmafene.aurius.support.cas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.farmafene.aurius.AuriusAuthException;
import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.util.Assertion;
import com.farmafene.aurius.util.IOnInitValidate;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class ValidateTGT implements IOnInitValidate {

	private static final Logger logger = LoggerFactory
			.getLogger(ValidateTGT.class);
	private String casServerURL;
	private String casServiceName;

	public ValidateTGT() {
	}

	public String validateTGT(AuthInfo serverAuthInfo)
			throws AuriusAuthException {
		return validateTicket(
				proxyTicket(serverAuthInfo.unwrap(AuthInfoString.class)
						.getValue(), getCasServiceName()), getCasServiceName());
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
		sb.append("casServerURL=").append(casServerURL);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.shell.utils.IOnInitValidate#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		Assertion.notNull(casServerURL, "Url del servidor no puede ser null");
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<afterPropertiesSet>");
		}
	}

	/**
	 * @return
	 * @throws LocalCasAuthException
	 */
	private String proxyTicket(final String proxyGrantingTicketId,
			final String targetService) throws AuriusAuthException {
		if (proxyGrantingTicketId == null) {
			throw new IllegalArgumentException("El ticket no puede ser null");
		}
		String proxyTicket = null;
		HttpResponse res = null;
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(getCasServerURL());
		DefaultHttpClient client = null;
		client = f.getClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet post = new HttpGet(constructUrl(proxyGrantingTicketId,
				targetService));
		try {
			res = client.execute(post, localContext);
		} catch (ClientProtocolException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"ClientProtocolException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		InputStream is = null;
		try {
			is = res.getEntity().getContent();
		} catch (IllegalStateException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"IllegalStateException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int leido = 0;
		try {
			while ((leido = is.read(buffer)) > 0) {
				baos.write(buffer, 0, leido);
			}
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		proxyTicket = baos.toString();
		if (null != baos) {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el OutputStream", e);
			}
		}
		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el InputStream", e);
			}
		}
		final String error = getTextForElement(proxyTicket,
				"proxyFailure");

		if (null != error && !"".equals(error.trim())) {

			throw new AuriusAuthException(error);
		}
		return getTextForElement(proxyTicket, "proxyTicket");
	}

	private String constructUrl(final String proxyGrantingTicketId,
			final String targetService) {
		try {
			return this.casServerURL
					+ (this.casServerURL.endsWith("/") ? "" : "/") + "proxy"
					+ "?pgt=" + proxyGrantingTicketId + "&targetService="
					+ URLEncoder.encode(targetService, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private String constructUrlValidate(final String ticket,
			final String targetService) {
		try {
			return this.casServerURL
					+ (this.casServerURL.endsWith("/") ? "" : "/")
					+ "proxyValidate" + "?ticket=" + ticket + "&service="
					+ URLEncoder.encode(targetService, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 * @throws LocalCasAuthException
	 */
	private String validateTicket(final String ticket, final String targetService)
			throws AuriusAuthException {
		if (ticket == null || "".equals(ticket.trim())) {
			throw new IllegalArgumentException("El ticket no puede ser null");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Validando: '" + ticket + "'");
		}
		String proxyTicket = null;
		HttpResponse res = null;
		HttpClientFactory f = new HttpClientFactory();
		f.setLoginURL(getCasServerURL());
		DefaultHttpClient client = null;
		client = f.getClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet post = new HttpGet(constructUrlValidate(ticket, targetService));
		try {
			res = client.execute(post, localContext);
		} catch (ClientProtocolException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"ClientProtocolException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		InputStream is = null;
		try {
			is = res.getEntity().getContent();
		} catch (IllegalStateException e) {
			AuriusAuthException ex = new AuriusAuthException(
					"IllegalStateException", AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int leido = 0;
		try {
			while ((leido = is.read(buffer)) > 0) {
				baos.write(buffer, 0, leido);
			}
		} catch (IOException e) {
			AuriusAuthException ex = new AuriusAuthException("IOException",
					AuriusExceptionTO.getInstance(e));
			logger.error("Excepcion en el login", ex);
			throw ex;
		}
		proxyTicket = baos.toString();
		if (null != baos) {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el OutputStream", e);
			}
		}
		if (null != is) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el InputStream", e);
			}
		}
		final String error = getTextForElement(proxyTicket,
				"authenticationFailure");

		if (null != error && !"".equals(error.trim())) {

			throw new AuriusAuthException(error);
		}
		return getTextForElement(proxyTicket, "user");
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

	/**
	 * @return el casServerURL
	 */
	public String getCasServerURL() {
		return casServerURL;
	}

	/**
	 * @param casServerURL
	 *            el casServerURL a establecer
	 */
	public void setCasServerURL(String casServerURL) {
		this.casServerURL = casServerURL;
	}

	/**
	 * @return the casServiceName
	 */
	public String getCasServiceName() {
		return casServiceName;
	}

	/**
	 * @param casServiceName
	 *            the casServiceName to set
	 */
	public void setCasServiceName(String casServiceName) {
		this.casServiceName = casServiceName;
	}
}
