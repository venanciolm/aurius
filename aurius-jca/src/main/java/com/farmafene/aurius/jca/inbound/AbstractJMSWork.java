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
package com.farmafene.aurius.jca.inbound;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAuriusContainerObserver;

public abstract class AbstractJMSWork implements Work, IAuriusContainerObserver {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractJMSWork.class);
	private IJCAPlataformAdapter iJCAPlataformAdapter;
	private boolean running;
	private Connection connection;
	private Session session;
	private Thread thread;

	/**
	 * Constructor por defecto
	 */
	public AbstractJMSWork() {
		this.running = false;
	}

	/**
	 * @return the connection
	 */
	protected Connection getConnection() {
		return connection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#stop()
	 */
	public void stop() {
		logger.info("stop(): " + this);
		this.running = false;
		thread.interrupt();
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		logger.info("release(): " + this);
		doRelease();
	}

	/**
	 * Post Ejecución del Release del {@link Work#release()}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	protected void doRelease() {
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
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		connection = null;
		thread = Thread.currentThread();
		try {
			while (mustWork()) {
				try {
					if (getIJCAPlataformAdapter().getUser() != null
							&& !"".equals(getIJCAPlataformAdapter().getUser()
									.trim()))
						connection = getIJCAPlataformAdapter()
								.getConnectionFactory()
								.createConnection(
										getIJCAPlataformAdapter().getUser(),
										getIJCAPlataformAdapter().getPassword());
					else {
						connection = getIJCAPlataformAdapter()
								.getConnectionFactory().createConnection();
					}
					connection.start();
					sessionWork();
				} catch (JMSException e) {
					logger.error("No ha sido posible crear la conexión", e);
				}
				if (session != null) {
					try {
						session.close();
					} catch (JMSException es) {
						logger.warn("Error al cerrar la session", es);
					}
					session = null;
				}
				if (connection != null) {
					try {
						connection.stop();
					} catch (JMSException es) {
						logger.warn("Error al parar la conexión", es);
					}
					try {
						connection.close();
					} catch (JMSException es) {
						logger.warn("Error al cerrar la conexión", es);
					}
					connection = null;
				}
				if (mustWork()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException ie) {
						logger.error("Error en la espera de la conexión JMS",
								ie);
					}
				}
			}
		} finally {
			Thread.interrupted();
			logger.info("Terminado: " + this);
		}
	}

	/**
	 * Decide si el work debe continuar o no.
	 */
	protected abstract boolean mustWork();

	/**
	 * Trabajo efectivo a realizar
	 * 
	 * @throws JMSException
	 */
	protected abstract void doWork() throws JMSException;

	private void sessionWork() throws JMSException {
		while (mustWork()) {
			try {
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				logger.error("Error al generar la sessión", e);
				throw e;
			}
			doWork();
		}
	}

	/**
	 * @return the iJCAPlataformAdapter
	 */
	public IJCAPlataformAdapter getIJCAPlataformAdapter() {
		return iJCAPlataformAdapter;
	}

	/**
	 * @param iJCAPlataformAdapter
	 *            the activationSpec to set
	 */
	public void setIJCAPlataformAdapter(
			IJCAPlataformAdapter iJCAPlataformAdapter) {
		this.iJCAPlataformAdapter = iJCAPlataformAdapter;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#start()
	 */
	@Override
	public void start() {
		if (logger.isDebugEnabled()) {
			logger.debug("start():" + this);
		}
		if (!isRunning()) {
			try {
				this.running = true;
				logger.debug("starting():" + this);
				getIJCAPlataformAdapter().getWorkManager().startWork(this);
			} catch (WorkException e) {
				logger.error("Error al arrancar: " + this, e);
			} catch (Throwable e) {
				logger.error("Error indeterminado al arrancar: " + this, e);
			}
		} else {
			logger.warn("Se ha realizdo un start y el estado es: "
					+ isRunning());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#startCluster()
	 */
	@Override
	public void startCluster() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#stopCluster()
	 */
	@Override
	public void stopCluster() {
	}

	/**
	 * @return the thread
	 */
	protected Thread getThread() {
		return thread;
	}
}
