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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.jms.Destination;
import javax.resource.spi.work.HintsContext;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkListener;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.jca.io.AuriusDiccionarioMessage;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusInvokeMessage;
import com.farmafene.aurius.jca.io.AuriusMessage;
import com.farmafene.aurius.jca.io.AuriusXAMessage;
import com.farmafene.aurius.jca.io.AuriusXIDMessage;
import com.farmafene.aurius.jca.io.Codec;
import com.farmafene.aurius.mngt.CallStatus;

@SuppressWarnings("serial")
public class AuriusCallToken implements Serializable {
	private static final Logger logger = LoggerFactory
			.getLogger(AuriusCallToken.class);
	private CallStatus callStatus;
	private AuriusMessage messageIn;
	private AuriusMessage messageOut;
	private IJCAPlataformAdapter iJCAPlataformAdapter;
	private Codec codec;
	private Destination replayTo;

	/**
	 * Constructor por defecto
	 */
	public AuriusCallToken() {

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
		sb.append("callStatus=").append(getCallStatus());
		sb.append("}");
		return sb.toString();
	}

	public void submit() {
		if (null == getIJCAPlataformAdapter()) {
			throw new IllegalStateException(
					"submit(): No se puede realizar la operacion");
		}
		if (null == getMessageIn()) {
			throw new IllegalStateException(
					"submit(): No existe mensaje que tratar");
		}
		try {
			Xid xId = null;
			Work w = null;
			if (getMessageIn() instanceof AuriusXIDMessage) {
				xId = ((AuriusXIDMessage) getMessageIn()).getCorrelationXid();
				if (null != xId) {
					callStatus.setXid(xId);
				}
				if (getMessageIn() instanceof AuriusXAMessage) {
					XATerminatorWork work = new XATerminatorWork();
					work.setXATerminator(getIJCAPlataformAdapter()
							.getXATerminator());
					work.setMsg((AuriusXAMessage) getMessageIn());
					work.setToken(this);
					w = work;
				} else if (getMessageIn() instanceof AuriusInvokeMessage) {
					AuriusInvokeMessage request = (AuriusInvokeMessage) getMessageIn();
					InvokeWork work = new InvokeWork();
					work.setToken(this);
					work.setMsg(request);
					w = work;
				} else {
					throw new IllegalArgumentException(getMessageIn()
							.getClass().getName() + ", es inválido");
				}
				if (null != xId) {
					TransactionContext tctx = new TransactionContext();
					tctx.setXid(((AuriusXIDMessage) getMessageIn())
							.getCorrelationXid());
					tctx.setTransactionTimeout(((AuriusXIDMessage) getMessageIn())
							.getTransactionTimeOut());
					((WorkContextProvider) w).getWorkContexts().add(tctx);
					((WorkContextProvider) w).getWorkContexts().add(
							new HintsContext());

				}
			} else if (getMessageIn() instanceof AuriusDiccionarioMessage) {
				GetRegistroWork work = new GetRegistroWork();
				w = work;
				work.setMsg((AuriusDiccionarioMessage) getMessageIn());
				work.setToken(this);
			} else {
				throw new IllegalArgumentException(getMessageIn().getClass()
						.getName() + ", es inválido");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("token antes de pausa: " + this);
			}
			this.getCallStatus().pause();
			if (logger.isDebugEnabled()) {
				logger.debug("token despues de pausa: " + this);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pf = new PrintWriter(baos);
				pf.println();
				pf.print("StartWork:");
				pf.print("\twork: ");
				pf.println(w);
				pf.println("\tMessage: ");
				pf.println(getMessageIn());
				pf.flush();
				try {
					baos.flush();
				} catch (IOException e) {
				}
				logger.debug(baos.toString());
				pf.close();
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
			getIJCAPlataformAdapter().getWorkManager().startWork(w,
					WorkManager.INDEFINITE, null,
					(w instanceof WorkListener) ? ((WorkListener) w) : null);
		} catch (Throwable e) {
			this.getCallStatus().resume(Thread.currentThread().getId());
			AuriusExceptionMessage msg = new AuriusExceptionMessage();
			msg.setTh(e);
			setMessageOut(msg);
			response();
		}
	}

	public void response() {
		try {
			if (null == getIJCAPlataformAdapter()) {
				throw new IllegalStateException(
						"response(): No se puede realizar la operacion");
			}
			getCallStatus().pause();
			getIJCAPlataformAdapter().getOutQueue().put(this);
		} catch (InterruptedException e) {
			getCallStatus().resume(Thread.currentThread().getId());
			logger.error(
					"Se ha producido un error al procesar el envío de la respuesta",
					e);
		}
	}

	/**
	 * @return the callStatus
	 */
	public CallStatus getCallStatus() {
		return callStatus;
	}

	/**
	 * @param callStatus
	 *            the callStatus to set
	 */
	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}

	/**
	 * @return the messageOut
	 */
	public AuriusMessage getMessageOut() {
		return messageOut;
	}

	/**
	 * @param messageOut
	 *            the messageOut to set
	 */
	public void setMessageOut(AuriusMessage messageOut) {
		this.messageOut = messageOut;
	}

	/**
	 * @return the codec
	 */
	public Codec getCodec() {
		return codec;
	}

	/**
	 * @param codec
	 *            the codec to set
	 */
	public void setCodec(Codec codec) {
		this.codec = codec;
	}

	/**
	 * @return the replayTo
	 */
	public Destination getReplayTo() {
		return replayTo;
	}

	/**
	 * @param replayTo
	 *            the replayTo to set
	 */
	public void setReplayTo(Destination replayTo) {
		this.replayTo = replayTo;
	}

	/**
	 * @return the messageIn
	 */
	public AuriusMessage getMessageIn() {
		return messageIn;
	}

	/**
	 * @param messageIn
	 *            the messageIn to set
	 */
	public void setMessageIn(AuriusMessage messageIn) {
		this.messageIn = messageIn;
	}

	/**
	 * @return el iJCAPlataformAdapter
	 */
	public IJCAPlataformAdapter getIJCAPlataformAdapter() {
		return iJCAPlataformAdapter;
	}

	/**
	 * @param iJCAPlataformAdapter
	 *            el iJCAPlataformAdapter a establecer
	 */
	public void setIJCAPlataformAdapter(
			IJCAPlataformAdapter iJCAPlataformAdapter) {
		this.iJCAPlataformAdapter = iJCAPlataformAdapter;
	}
}
