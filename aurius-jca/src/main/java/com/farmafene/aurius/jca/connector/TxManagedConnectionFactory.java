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

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.jms.QueueConnectionFactory;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.BasicRegistroFactory;

@SuppressWarnings("serial")
public class TxManagedConnectionFactory implements ManagedConnectionFactory {

	private static final String CONNECTION_FACTORY_DEFAULT = "org.apache.activemq.ActiveMQConnectionFactory";
	private static final Logger logger = LoggerFactory
			.getLogger(TxManagedConnectionFactory.class);
	private String managedConnectionFactoryClass;
	private String broker;
	private TxConnectionRequestInfo txConnectionRequestInfo;
	private PrintWriter logWriter;
	private QueueConnectionFactory queueConnectionFactory;
	private boolean internalRegistro = null != BasicRegistroFactory
			.getIBasicRegistroFactory();

	public TxManagedConnectionFactory() {
		this.txConnectionRequestInfo = new TxConnectionRequestInfo();
		this.managedConnectionFactoryClass = CONNECTION_FACTORY_DEFAULT;
		setTimeOut(120000); /* 3 minutos */
		setTimeToLive(30000); /* 30 segundos */
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("managedConnectionFactoryClass=").append(
				getManagedConnectionFactoryClass());
		sb.append(", broker=").append(getBroker());
		sb.append(", TxConnectionRequestInfo=").append(txConnectionRequestInfo);

		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 0x07;
		hashCode = hashCode
				* 31
				+ ((txConnectionRequestInfo == null) ? 0
						: txConnectionRequestInfo.hashCode());
		hashCode = hashCode
				* 31
				+ ((managedConnectionFactoryClass == null) ? 0
						: managedConnectionFactoryClass.hashCode());
		hashCode = hashCode * 31 + ((broker == null) ? 0 : broker.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof TxManagedConnectionFactory) {
			equals = this.hashCode() == obj.hashCode();
		}
		return equals;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory()
	 */
	@Override
	public Object createConnectionFactory() throws ResourceException {
		throw new NotSupportedException(
				"createConnectionFactory(): Not Supported!!!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory(javax.resource.spi.ConnectionManager)
	 */
	@Override
	public Object createConnectionFactory(ConnectionManager cm)
			throws ResourceException {
		TxConnectionFactory acf = new TxConnectionFactory();
		acf.setConnectionManager(cm);
		acf.setTxConnectorManagedConnectionFactory(this);
		acf.setConnectionRequestInfo(txConnectionRequestInfo);
		return acf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject,
	 *      javax.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection createManagedConnection(Subject subject,
			ConnectionRequestInfo cri) throws ResourceException {
		TxManagedConnection mc = new TxManagedConnection();
		mc.setInternalRegistro(isInternalRegistro());
		mc.setQueueConnectionFactory(getQueueConnectionFactory());
		try {
			mc.setTxConnectionRequestInfo(((TxConnectionRequestInfo) cri)
					.clone());
		} catch (CloneNotSupportedException e) {
			throw new NotSupportedException(e);
		}
		return mc;
	}

	QueueConnectionFactory getQueueConnectionFactory() {
		if (this.queueConnectionFactory == null) {
			try {
				this.queueConnectionFactory = (QueueConnectionFactory) Class
						.forName(getManagedConnectionFactoryClass())
						.getConstructor(String.class).newInstance(getBroker());
			} catch (ClassNotFoundException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (SecurityException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (NoSuchMethodException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (IllegalArgumentException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (InstantiationException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (IllegalAccessException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			} catch (InvocationTargetException e) {
				logger.error("Excepción al crear la QueueConnectionFactory:", e);
			}
			if (null == this.queueConnectionFactory) {

			}
		}
		return this.queueConnectionFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set,
	 *      javax.security.auth.Subject,
	 *      javax.resource.spi.ConnectionRequestInfo)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ManagedConnection matchManagedConnections(Set pool, Subject subject,
			ConnectionRequestInfo cri) throws ResourceException {
		TxManagedConnection salida = null;
		if (pool != null && cri instanceof TxConnectionRequestInfo) {
			Set<TxManagedConnection> smc = pool;
			for (TxManagedConnection candidate : smc) {
				cri.equals(candidate.getTxConnectionRequestInfo());
				logger.info("Obtenida la conexi�n del pool: " + candidate);
				salida = candidate;
				break;
			}
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return this.logWriter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter logWriter) throws ResourceException {
		this.logWriter = logWriter;
	}

	/**
	 * @return the broker
	 */
	public String getBroker() {
		return this.broker;
	}

	/**
	 * @param broker
	 *            the connectionFactory to set
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}

	/**
	 * @return the managedConnectionFactoryClass
	 */
	public String getManagedConnectionFactoryClass() {
		return managedConnectionFactoryClass;
	}

	/**
	 * @param managedConnectionFactoryClass
	 *            the managedConnectionFactoryClass to set
	 */
	public void setManagedConnectionFactoryClass(
			String managedConnectionFactoryClass) {
		this.managedConnectionFactoryClass = managedConnectionFactoryClass;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.txConnectionRequestInfo.getUser();
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.txConnectionRequestInfo.setUser(user);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.txConnectionRequestInfo.getPassword();
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.txConnectionRequestInfo.setPassword(password);
	}

	/**
	 * @return the timeOut
	 */
	public long getTimeOut() {
		return this.txConnectionRequestInfo.getTimeOut();
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public void setTimeOut(long timeOut) {
		this.txConnectionRequestInfo.setTimeOut(timeOut);
	}

	/**
	 * @return the timeToLive
	 */
	public long getTimeToLive() {
		return this.txConnectionRequestInfo.getTimeToLive();
	}

	/**
	 * @param timeToLive
	 *            the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.txConnectionRequestInfo.setTimeToLive(timeToLive);
	}

	/**
	 * @return the jcaQueue
	 */
	public String getQueue() {
		return this.txConnectionRequestInfo.getQueue();
	}

	/**
	 * @param jcaQueue
	 *            the jcaQueue to set
	 */
	public void setQueue(String queue) {
		this.txConnectionRequestInfo.setQueue(queue);
	}

	/**
	 * @return el internalRegistro
	 */
	public boolean isInternalRegistro() {
		return internalRegistro;
	}

	/**
	 * @param internalRegistro
	 *            el internalRegistro a establecer
	 */
	public void setInternalRegistro(boolean internalRegistro) {
		this.internalRegistro = internalRegistro;
	}
}
