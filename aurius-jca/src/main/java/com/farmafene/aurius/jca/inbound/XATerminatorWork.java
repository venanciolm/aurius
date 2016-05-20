/*
 * Copyright (c) 2009-2013 farmafene.com
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.resource.spi.XATerminator;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.WorkContextLifecycleListener;
import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import javax.transaction.xa.XAException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.jca.io.AuriusMessage;
import com.farmafene.aurius.jca.io.AuriusVoidMessage;
import com.farmafene.aurius.jca.io.AuriusXACommit;
import com.farmafene.aurius.jca.io.AuriusXAExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusXAForget;
import com.farmafene.aurius.jca.io.AuriusXAMessage;
import com.farmafene.aurius.jca.io.AuriusXAPrepare;
import com.farmafene.aurius.jca.io.AuriusXAPrepareResponse;
import com.farmafene.aurius.jca.io.AuriusXARecover;
import com.farmafene.aurius.jca.io.AuriusXARecoverResponse;
import com.farmafene.aurius.jca.io.AuriusXARollBack;
import com.farmafene.aurius.mngt.CommitStatus;
import com.farmafene.aurius.mngt.ForgetStatus;
import com.farmafene.aurius.mngt.IAuriusCoreStat;
import com.farmafene.aurius.mngt.PrepareStatus;
import com.farmafene.aurius.mngt.RollbackStatus;

@SuppressWarnings("serial")
public class XATerminatorWork implements Work, WorkListener,
		WorkContextProvider, WorkContextLifecycleListener {

	private static final Logger logger = LoggerFactory
			.getLogger(XATerminatorWork.class);

	private AuriusXAMessage msg;
	private XATerminator xATerminator;
	private AuriusCallToken token;
	private List<WorkContext> workContext;

	public XATerminatorWork() {
		workContext = new CopyOnWriteArrayList<WorkContext>();
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
		sb.append("response=").append(
				null == token ? null : token.getReplayTo());
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
		if (logger.isDebugEnabled()) {
			logger.debug("- Operaciones de control!!!" + this.getMsg());
		}
		IAuriusCoreStat status = null;
		AuriusMessage outAuriusMessage = null;
		Throwable th = null;
		getToken().getCallStatus().resume(Thread.currentThread().getId());
		ContextoManager.getIContextoManager().setContexto(
				getToken().getCallStatus().getContexto());
		xATerminator = token.getIJCAPlataformAdapter().getXATerminator();
		try {
			if (msg instanceof AuriusXACommit) {
				status = new CommitStatus();
				status.start();
				if (logger.isDebugEnabled()) {
					logger.debug("xATerminator.<before commit>");
				}
				xATerminator.commit(msg.getCorrelationXid(),
						((AuriusXACommit) msg).isOnePhase());
				if (logger.isDebugEnabled()) {
					logger.debug("xATerminator.<after commit>");
				}
			} else if (msg instanceof AuriusXARollBack) {
				status = new RollbackStatus();
				status.start();
				xATerminator.rollback(msg.getCorrelationXid());
			} else if (msg instanceof AuriusXAPrepare) {
				status = new PrepareStatus();
				status.start();
				AuriusXAPrepareResponse salida = new AuriusXAPrepareResponse();
				outAuriusMessage = salida;
				salida.setPrepare(xATerminator.prepare(msg.getCorrelationXid()));
			} else if (msg instanceof AuriusXARecover) {
				AuriusXARecoverResponse salida = new AuriusXARecoverResponse();
				outAuriusMessage = salida;
				salida.setXIDs(xATerminator.recover(((AuriusXARecover) msg)
						.getFlag()));
			} else if (msg instanceof AuriusXAForget) {
				status = new ForgetStatus();
				xATerminator.forget(msg.getCorrelationXid());
			} else {
				throw new IllegalStateException(msg == null ? "null"
						: msg.getClass() + ", no está admitido.");
			}
		} catch (XAException e) {
			th = AuriusExceptionTO.getInstance(e);
			AuriusXAExceptionMessage ex = new AuriusXAExceptionMessage();
			outAuriusMessage = ex;
			ex.setMessage(e.getMessage());
			ex.setErrorCode(e.errorCode);
			ex.setTh(th);
		} catch (Throwable e) {
			logger.error("process:AuriusXAMessage - Throwable", e);
			th = AuriusExceptionTO.getInstance(e);
			AuriusXAExceptionMessage ex = new AuriusXAExceptionMessage();
			outAuriusMessage = ex;
			ex.setMessage(e.getMessage());
			ex.setErrorCode(XAException.XAER_INVAL);
			ex.setTh(th);
		} finally {
			if (outAuriusMessage == null) {
				outAuriusMessage = new AuriusVoidMessage();
			}
			if (status != null) {
				status.stop(th);
			}
			getToken().setMessageOut(outAuriusMessage);
		}
		getToken().response();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		if (logger.isDebugEnabled()) {
			logger.debug("Work.release(): " + this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkListener#workAccepted(javax.resource.spi.
	 *      work.WorkEvent)
	 */
	@Override
	public void workAccepted(WorkEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("workAccepted(" + event + ")", event.getException());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkListener#workCompleted(javax.resource.spi
	 *      .work.WorkEvent)
	 */
	@Override
	public void workCompleted(WorkEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("workCompleted(" + event + ")", event.getException());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkListener#workRejected(javax.resource.spi.
	 *      work.WorkEvent)
	 */
	@Override
	public void workRejected(WorkEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("workRejected(" + event + ")", event.getException());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkListener#workStarted(javax.resource.spi.work
	 *      .WorkEvent)
	 */
	@Override
	public void workStarted(WorkEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("workStarted(" + event + ")", event.getException());
		}
	}

	public AuriusCallToken getToken() {
		return this.token;
	}

	public void setToken(AuriusCallToken token) {
		this.token = token;
	}

	public XATerminator getXATerminator() {
		return this.xATerminator;
	}

	public void setXATerminator(XATerminator xATerminator) {
		this.xATerminator = xATerminator;
	}

	public AuriusXAMessage getMsg() {
		return this.msg;
	}

	public void setMsg(AuriusXAMessage msg) {
		this.msg = msg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkContextLifecycleListener#contextSetupComplete()
	 */
	@Override
	public void contextSetupComplete() {
		logger.info("contextSetupComplete()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkContextLifecycleListener#contextSetupFailed(java.lang.String)
	 */
	@Override
	public void contextSetupFailed(String errorCode) {
		logger.info("contextSetupComplete()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkContextProvider#getWorkContexts()
	 */
	@Override
	public List<WorkContext> getWorkContexts() {
		return workContext;
	}
}
