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

import java.io.IOException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusMessage;
import com.farmafene.aurius.jca.io.Codec;
import com.farmafene.aurius.jca.io.IOUtil;
import com.farmafene.aurius.mngt.CallStatus;
import com.farmafene.aurius.mngt.UnmarshallStatus;

public class JMSReaderWork extends AbstractJMSWork {

	private static final Logger logger = LoggerFactory
			.getLogger(JMSReaderWork.class);

	private boolean cluster;
	private MessageConsumer consumer = null;

	/**
	 * Constructor por defecto
	 */
	public JMSReaderWork() {
		this.cluster = false;
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
		sb.append("mustWork=").append(mustWork());
		sb.append(", running=").append(isRunning());
		sb.append(", queueToRead=").append(getQueueToRead());
		sb.append(", cluster=").append(cluster);
		sb.append(", thread=").append(Thread.currentThread().getName());
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
		super.run();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		super.release();
	}

	protected boolean mustWork() {
		return isRunning();
	}

	protected void doWork() throws JMSException {
		AuriusCallToken token = null;
		Message m = null;
		consumer = null;
		while (mustWork()) {
			try {
				if (null == consumer) {
					Session sess = getSession();
					Queue q = sess.createQueue(getQueueToRead());
					consumer = sess.createConsumer(q);
					logger.info("Consumer: (" + getQueueToRead() + ")"
							+ consumer);
				}
			} catch (JMSException e) {
				logger.error("Error al crear el consumer", e);
				throw e;
			}
			try {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("Esperando Mensaje: " + this);
					}
					m = consumer.receive();
				} catch (JMSException e) {
					if (mustWork()) {
						try {
							consumer.close();
							logger.error(this + ": Cierre del consumidor");
						} catch (Throwable th) {
							logger.error(this
									+ ": Error en el cierre del consumidor", e);
						}
						logger.error(this
								+ ": Error en la recepción del mensaje", e);
						throw e;
					}
				}
				if (!mustWork() && null == m) {
					MessageConsumer c = consumer;
					consumer = null;
					if (null != c) {
						c.close();
						logger.info("Se ha cerrado el consumidor!!!!");
					}
					continue;
				} else if (null == m) {
					logger.debug(this + "Mensaje inválido, retornando.");
					continue;
				}
				CallStatus status = new CallStatus();
				status.setConector("AuriusJMS-JCA");
				token = new AuriusCallToken();
				try {
					token.setIJCAPlataformAdapter(getIJCAPlataformAdapter());
					ContextoCore c = ContextoManager.getIContextoManager()
							.createContext();
					status.setContexto(c);
					status.start();
					token.setCallStatus(status);
					ContextoManager.getIContextoManager().setContexto(c);
				} catch (Throwable e) {
					JMSException jmsE = new JMSException("Estado no controlado");
					logger.error("Estado no controlado", e);
					AuriusExceptionMessage r = new AuriusExceptionMessage();
					r.setTh(e);
					token.setMessageOut(r);
					token.response();
					throw jmsE;
				}
				UnmarshallStatus us = null;
				us = new UnmarshallStatus();
				us.start();
				try {
					if (m instanceof BytesMessage && m.propertyExists("Codec")) {
						Codec codec = Codec
								.getByCode(m.getIntProperty("Codec"));
						token.setReplayTo(m.getJMSReplyTo());
						token.setCodec(codec);
						AuriusMessage msg = null;
						msg = (AuriusMessage) IOUtil.unmarshall(
								IOUtil.readBytes(((BytesMessage) m)), codec);
						token.setMessageIn(msg);
						us.stop(null);
						token.submit();
					} else {
						us.stop(null);
						AuriusExceptionMessage r = new AuriusExceptionMessage();
						r.setTh(new IllegalStateException(
								"Tipo de mensaje inv�lido enviado"));
						token.setMessageOut(r);
						token.response();
					}
				} catch (IllegalArgumentException e) {
					us.stop(e);
					logger.error("Error en el unmarshall", e);
					AuriusExceptionMessage r = new AuriusExceptionMessage();
					r.setTh(new IllegalStateException(
							"Tipo de mensaje inválido enviado"));
					token.setMessageOut(r);
					token.response();
				} catch (JMSException e) {
					us.stop(e);
					logger.error("Error en el unmarshall", e);
					AuriusExceptionMessage r = new AuriusExceptionMessage();
					r.setTh(new IllegalStateException(
							"Tipo de mensaje inválido enviado"));
					token.setMessageOut(r);
					token.response();
					throw e;
				} catch (IOException e) {
					us.stop(e);
					logger.error("Error en el unmarshall", e);
					AuriusExceptionMessage r = new AuriusExceptionMessage();
					r.setTh(new IllegalStateException(
							"Tipo de mensaje inválido enviado"));
					token.setMessageOut(r);
					token.response();
				}
			} finally {
				m = null;
				token = null;
			}
		}
	}

	/**
	 * @return the queueToRead
	 */
	public String getQueueToRead() {
		return cluster ? getIJCAPlataformAdapter().getClusterQueue()
				: getIJCAPlataformAdapter().getQueue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.AbstractJMSWork#startCluster()
	 */
	@Override
	public void startCluster() {
		if (isCluster()) {
			start();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.inbound.AbstractJMSWork#stopCluster()
	 */
	@Override
	public void stopCluster() {
		if (isCluster()) {
			stop();
		}
	}

	/**
	 * @return el cluster
	 */
	public boolean isCluster() {
		return cluster;
	}

	/**
	 * @param cluster
	 *            el cluster a establecer
	 */
	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}
}
