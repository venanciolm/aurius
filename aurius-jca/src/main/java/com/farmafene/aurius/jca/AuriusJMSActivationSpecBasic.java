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
package com.farmafene.aurius.jca;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.jms.ConnectionFactory;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.AuriusContainerSubject;
import com.farmafene.aurius.jca.inbound.AbstractJMSWork;
import com.farmafene.aurius.jca.inbound.AuriusCallToken;
import com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter;
import com.farmafene.aurius.jca.inbound.JMSReaderWork;
import com.farmafene.aurius.jca.inbound.JMSWriterWork;

public class AuriusJMSActivationSpecBasic implements ActivationSpec,
		IJCAPlataformAdapter {
	protected static final int JCA_MSG_TIMEOUT_DEFAULT = 12000;
	protected static final int JCA_MSG_TIMETOLIVE_DEFAULT = 30000;
	protected static final String CONNECTION_FACTORY_DEFAULT = "org.apache.activemq.ActiveMQConnectionFactory";

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusJMSActivationSpecBasic.class);
	private String managedConnectionFactoryClass = CONNECTION_FACTORY_DEFAULT;
	private String broker;
	private String queue;
	private String clusterQueue;
	private String topic;
	private long timeToLive = JCA_MSG_TIMETOLIVE_DEFAULT;
	private long timeOut = JCA_MSG_TIMEOUT_DEFAULT;
	private String user;
	private String password;

	private BlockingDeque<AuriusCallToken> outQueue;

	private AuriusResourceAdapter resourceAdapter;
	private AbstractJMSWork[] readers;
	private AbstractJMSWork[] writters;
	private ConnectionFactory connectionFactory;

	public AuriusJMSActivationSpecBasic() {
		logger.info("AuriusJMSActivationSpec<init>()");
		outQueue = new LinkedBlockingDeque<AuriusCallToken>();
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
		sb.append("ResourceAdapter=").append(resourceAdapter);
		sb.append(", Queue=").append(getQueue());
		sb.append(", Broker=").append(getBroker());
		sb.append(", ClusterQueue=").append(getClusterQueue());
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((broker == null) ? 0 : broker.hashCode());
		result = prime * result
				+ ((clusterQueue == null) ? 0 : clusterQueue.hashCode());
		result = prime
				* result
				+ ((managedConnectionFactoryClass == null) ? 0
						: managedConnectionFactoryClass.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AuriusJMSActivationSpecBasic))
			return false;
		AuriusJMSActivationSpecBasic other = (AuriusJMSActivationSpecBasic) obj;
		if (broker == null) {
			if (other.broker != null)
				return false;
		} else if (!broker.equals(other.broker))
			return false;
		if (clusterQueue == null) {
			if (other.clusterQueue != null)
				return false;
		} else if (!clusterQueue.equals(other.clusterQueue))
			return false;
		if (managedConnectionFactoryClass == null) {
			if (other.managedConnectionFactoryClass != null)
				return false;
		} else if (!managedConnectionFactoryClass
				.equals(other.managedConnectionFactoryClass))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ResourceAdapterAssociation#getResourceAdapter()
	 */
	@Override
	public AuriusResourceAdapter getResourceAdapter() {
		logger.debug("getResourceAdapter()");
		return resourceAdapter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ResourceAdapterAssociation#setResourceAdapter(javax.resource.spi.ResourceAdapter)
	 */
	@Override
	public void setResourceAdapter(ResourceAdapter resAdapter)
			throws ResourceException {
		logger.debug("setResourceAdapter('" + resAdapter + "')");
		this.resourceAdapter = (AuriusResourceAdapter) resAdapter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ActivationSpec#validate()
	 */
	@Override
	public void validate() throws InvalidPropertyException {
		logger.debug("validate()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getQueue()
	 */
	@Override
	public String getQueue() {
		return queue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getTimeToLive()
	 */
	@Override
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getUser()
	 */
	@Override
	public String getUser() {
		return user;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getWorkManager()
	 */
	@Override
	public WorkManager getWorkManager() {
		return resourceAdapter.getWorkManager();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getXATerminator()
	 */
	@Override
	public XATerminator getXATerminator() {
		return resourceAdapter.getXATerminator();
	}

	public MessageEndpointFactory getMessageEndpointFactory() {
		return resourceAdapter.getMessageEndpointFactory(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#start()
	 */
	@Override
	public void start() {
		try {
			init();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (null != getClusterQueue() && !"".equals(getClusterQueue().trim())) {
			readers = new AbstractJMSWork[2];
		} else {
			readers = new AbstractJMSWork[1];
		}
		JMSReaderWork reader = new JMSReaderWork();
		reader.setIJCAPlataformAdapter(this);
		readers[0] = reader;
		if (2 == readers.length) {
			reader = new JMSReaderWork();
			reader.setIJCAPlataformAdapter(this);
			reader.setCluster(true);
			readers[1] = reader;
		}
		writters = new AbstractJMSWork[1];
		JMSWriterWork writter = new JMSWriterWork();
		writter.setIJCAPlataformAdapter(this);
		writters[0] = writter;
		if (writters != null) {
			for (AbstractJMSWork w : writters) {
				AuriusContainerSubject.add(w);
			}
		}
		if (readers != null) {
			for (AbstractJMSWork w : readers) {
				AuriusContainerSubject.add(w);
			}
		}
	}

	protected void init() throws InvalidPropertyException {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#stop()
	 */
	@Override
	public void stop() {
		logger.info("stop(): " + this);
		if (readers != null) {
			for (AbstractJMSWork w : readers) {
				AuriusContainerSubject.remove(w);
				if (w.isRunning()) {
					w.stop();
				}
			}
			readers = null;
		}
		if (writters != null) {
			for (AbstractJMSWork w : writters) {
				AuriusContainerSubject.remove(w);
				if (w.isRunning()) {
					w.stop();
				}
			}
			writters = null;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getOutQueue()
	 */
	@Override
	public BlockingDeque<AuriusCallToken> getOutQueue() {
		return outQueue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getConnectionFactory()
	 */
	@Override
	public ConnectionFactory getConnectionFactory() {
		if (this.connectionFactory == null) {
			try {
				this.connectionFactory = (ConnectionFactory) Class
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
		}
		return this.connectionFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter#getAuriusListener(javax.transaction.xa.XAResource)
	 */
	@Override
	public AuriusListener getAuriusListener(XAResource xAResource)
			throws UnavailableException {
		MessageEndpointFactory endPointFactory = getMessageEndpointFactory();
		if (logger.isDebugEnabled()) {
			logger.debug("getAuriusListener('" + xAResource + "')");
		}
		AuriusListener listener = (AuriusListener) endPointFactory
				.createEndpoint(xAResource);
		return listener;
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
	 * @return the broker
	 */
	public String getBroker() {
		return broker;
	}

	/**
	 * @param broker
	 *            the broker to set
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}

	/**
	 * @return the clusterQueue
	 */
	public String getClusterQueue() {
		return clusterQueue;
	}

	/**
	 * @param clusterQueue
	 *            the clusterQueue to set
	 */
	public void setClusterQueue(String clusterQueue) {
		this.clusterQueue = clusterQueue;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the timeOut
	 */
	public long getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * @param timeToLive
	 *            the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param resourceAdapter
	 *            the resourceAdapter to set
	 */
	public void setResourceAdapter(AuriusResourceAdapter resourceAdapter) {
		this.resourceAdapter = resourceAdapter;
	}
}
