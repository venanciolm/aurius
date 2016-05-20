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
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.IOUtil;
import com.farmafene.aurius.mngt.MarshallStatus;

public class JMSWriterWork extends AbstractJMSWork {

	private static final Logger logger = LoggerFactory
			.getLogger(JMSWriterWork.class);
	private Destination replayTo;
	MessageProducer prod = null;

	/**
	 * Constructor por defecto
	 */
	public JMSWriterWork() {

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
		sb.append(", replayToDestination=").append(getReplyToDestination());
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
		return isRunning()
				|| !getIJCAPlataformAdapter().getOutQueue().isEmpty();
	}

	protected void doWork() throws JMSException {
		AuriusCallToken token = null;
		prod = null;
		while (mustWork()) {
			token = null;
			try {
				if (null == prod) {
					prod = getSession().createProducer(null);
					prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				}
			} catch (JMSException e) {
				logger.error("Error al crear el consumer", e);
				throw e;
			}

			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Esperando mensaje: " + this);
				}
				token = getIJCAPlataformAdapter().getOutQueue().take();
				if (logger.isDebugEnabled()) {
					logger.debug("Recibido: " + token);
				}
			} catch (InterruptedException e) {
				if (mustWork()) {
					logger.error(this + ": Error en la espera de la cola", e);
				}
				continue;
			}
			if (null == token) {
				logger.debug("Mensaje inválido, retornando.");
				continue;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("token antes de resume: " + token);
			}
			token.getCallStatus().resume(Thread.currentThread().getId());
			if (logger.isDebugEnabled()) {
				logger.debug("token despues de resume: " + token);
			}
			byte[] serializado = null;
			BytesMessage msg = null;
			MarshallStatus status = null;
			try {
				ContextoManager.getIContextoManager().setContexto(
						token.getCallStatus().getContexto());
				logger.debug("Establecido el contexto");
				status = new MarshallStatus();
				status.start();
				serializado = IOUtil.marshall(token.getMessageOut(),
						token.getCodec());
				status.stop(null);
			} catch (IllegalArgumentException e) {
				status.stop(e);
				logger.error("Error al serializar", e);
				token.getCallStatus().stop(e);
				continue;
			} catch (IOException e) {
				status.stop(e);
				logger.error("Error al serializar", e);
				token.getCallStatus().stop(e);
				continue;
			} catch (Throwable e) {
				status.stop(e);
				logger.error("Error al serializar", e);
				token.getCallStatus().stop(e);
				continue;
			}
			try {
				msg = getSession().createBytesMessage();
				msg.setIntProperty("Codec", token.getCodec().ordinal());
				msg.writeBytes(serializado);
				msg.setJMSReplyTo(getReplayTo());
				prod.send(token.getReplayTo(), msg);
				if (token.getMessageOut() instanceof AuriusExceptionMessage) {
					token.getCallStatus().stop(
							((AuriusExceptionMessage) token.getMessageOut())
									.getTh());
				} else {
					token.getCallStatus().stop(null);
				}
			} catch (JMSException e1) {
				logger.error("No ha sido posible generar el producer", e1);
				try {
					token.getCallStatus().pause();
					getIJCAPlataformAdapter().getOutQueue().putFirst(token);
				} catch (InterruptedException e) {
					logger.error("Error al volver introducir en la cola", e);
				}
				continue;
			} catch (Throwable th) {
				token.getCallStatus().stop(th);
				logger.error("Estado no controlable - Throwable: ", th);
			} finally {
				if (prod != null) {
					try {
						prod.close();
					} catch (JMSException e) {
						logger.warn("Error al cerrar el producer", e);
					}
				}
				prod = null;
			}
		}
		if (null != prod) {
			prod.close();
		}
	}

	private Destination getReplayTo() throws JMSException {
		if (null == replayTo) {
			replayTo = getSession().createQueue(getReplyToDestination());
		}
		return replayTo;
	}

	/**
	 * @return the replyToDestination
	 */
	public String getReplyToDestination() {
		return getIJCAPlataformAdapter().getQueue();
	}
}
