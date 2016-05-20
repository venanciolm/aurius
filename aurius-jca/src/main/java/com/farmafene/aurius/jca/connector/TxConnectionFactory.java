/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.jca.connector;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.jca.AuriusConnection;
import com.farmafene.aurius.jca.AuriusConnectionFactory;

@SuppressWarnings("serial")
public class TxConnectionFactory implements AuriusConnectionFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(TxConnectionFactory.class);

	private ConnectionManager connectionManager;
	private TxManagedConnectionFactory txManagedConnectionFactory;
	private TxConnectionRequestInfo connectionRequestInfo;

	TxConnectionFactory() {

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
		sb.append(connectionRequestInfo);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnectionFactory#getConnection()
	 */
	@Override
	public AuriusConnection getConnection() {
		AuriusConnection con = null;
		try {
			con = (AuriusConnection) connectionManager.allocateConnection(
					txManagedConnectionFactory, connectionRequestInfo.clone());
		} catch (ResourceException e) {
			logger.error("No es posible generar la conexión", e);
		} catch (CloneNotSupportedException e) {
			logger.error("No es posible generar la conexión", e);
		}
		return con;
	}

	void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;

	}

	void setTxConnectorManagedConnectionFactory(
			TxManagedConnectionFactory txManagedConnectionFactory) {
		this.txManagedConnectionFactory = txManagedConnectionFactory;

	}

	void setConnectionRequestInfo(TxConnectionRequestInfo connectionRequestInfo) {
		this.connectionRequestInfo = connectionRequestInfo;
	}
}
