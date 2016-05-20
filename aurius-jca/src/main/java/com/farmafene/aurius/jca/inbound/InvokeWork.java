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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.WorkContextLifecycleListener;
import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.jca.AuriusListener;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusInvokeMessage;
import com.farmafene.aurius.jca.io.AuriusResponseMessage;

@SuppressWarnings("serial")
public class InvokeWork implements Work, WorkListener, XAResource,
		WorkContextProvider, WorkContextLifecycleListener {

	private static final Logger logger;
	// private static Method INVOKE_METHOD;

	static {
		logger = LoggerFactory.getLogger(InvokeWork.class);
		// try {
		// INVOKE_METHOD = AuriusListener.class.getMethod("invoke",
		// new Class[] { ContextoCore.class, String.class,
		// String.class, Registro.class });
		// } catch (SecurityException e) {
		// logger.error("Listener mal configurado!!!", e);
		// throw new RuntimeException(e);
		// } catch (NoSuchMethodException e) {
		// logger.error("Listener mal configurado!!!", e);
		// throw new RuntimeException(e);
		// }
	}
	private AuriusListener listener;
	private AuriusCallToken token;
	private AuriusInvokeMessage msg;
	private int transactionTimeout;
	private UUID id;
	private List<WorkContext> workContext;

	public InvokeWork() {
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
		sb.append("UUID=").append(
				null == token ? null : token.getCallStatus().getContexto()
						.getId());
		sb.append("response=").append(
				null == token ? null : token.getReplayTo());
		sb.append(", thread=").append(Thread.currentThread().getName());
		sb.append("}");
		return sb.toString();
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
		if (getClass() != obj.getClass())
			return false;
		InvokeWork other = (InvokeWork) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
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
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			doPrecondiciones();
			AuriusResponseMessage msgOut = new AuriusResponseMessage();
			msgOut.setRegistro(listener.invoke(ContextoManager.getContexto(),
					msg.getServicioId(), msg.getVersion(), msg.getRegistro()));
			getToken().setMessageOut(msgOut);
		} catch (Throwable th) {
			AuriusExceptionMessage msg = new AuriusExceptionMessage();
			msg.setTh(th);
			getToken().setMessageOut(msg);
		} finally {
			doPoscondiciones();
		}
		getToken().response();
	}

	private void doPrecondiciones() throws NoSuchMethodException,
			ResourceException {
		getToken().getCallStatus().resume(Thread.currentThread().getId());
		ContextoManager.getIContextoManager().setContexto(
				getToken().getCallStatus().getContexto());
		ContextoManager.getContexto().setAuthInfo(msg.getCookie());
		setListener(token.getIJCAPlataformAdapter().getAuriusListener(this));
		if (listener instanceof MessageEndpoint) {
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndPoint.beforeDelivery(): " + this);
			}
			// ((MessageEndpoint) listener).beforeDelivery(INVOKE_METHOD);
			if (logger.isDebugEnabled()) {
				logger.info("MessageEndPoint.beforeDelivery()<end>");
			}
		}
	}

	private void doPoscondiciones() {
		if (listener instanceof MessageEndpoint) {
			MessageEndpoint endPoint = null;
			endPoint = (MessageEndpoint) listener;
			// try {
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndpoint.afterDelivery(): " + this);
			}
			// endPoint.afterDelivery();
			// } catch (ResourceException e) {
			// logger.error("", e);
			// }
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndpoint.afterDelivery()<end>");
				logger.debug("MessageEndpoint.release(): " + this);
			}
			endPoint.release();
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndpoint.release()<end>");
			}
		}
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
		// if (listener instanceof MessageEndpoint) {
		// MessageEndpoint endPoint = null;
		// endPoint = (MessageEndpoint) listener;
		// endPoint.release();
		// }
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

	public AuriusListener getListener() {
		return this.listener;
	}

	public void setListener(AuriusListener listener) {
		this.listener = listener;
	}

	public AuriusCallToken getToken() {
		return this.token;
	}

	public void setToken(AuriusCallToken token) {
		this.token = token;
		id = token.getCallStatus().getContexto().getId();
	}

	public AuriusInvokeMessage getMsg() {
		return this.msg;
	}

	public void setMsg(AuriusInvokeMessage msg) {
		this.msg = msg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid,
	 *      boolean)
	 */
	@Override
	public void commit(Xid xid, boolean onePhase) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("commit:");
			pf.print("\tXid:");
			pf.println(xid);
			pf.print("\tOne Phase: ");
			pf.println(onePhase);
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void end(Xid xid, int flags) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("end:");
			pf.print("\tXid:");
			pf.println(xid);
			pf.print("\tFlags: ");
			pf.println(flags);
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
	 */
	@Override
	public void forget(Xid xid) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("forget:");
			pf.print("\tXid:");
			pf.println(xid);
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#getTransactionTimeout()
	 */
	@Override
	public int getTransactionTimeout() throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("getTransactionTimeout:");
			pf.print("\ttransaction time out:");
			pf.println(transactionTimeout);
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
		return transactionTimeout;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
	 */
	@Override
	public boolean isSameRM(XAResource xares) throws XAException {
		boolean isSameRM = this.equals(xares);
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("isSameRM:");
			pf.print("this :");
			pf.println(this);
			pf.print("other:");
			pf.println(xares);
			pf.print("\treturn:");
			pf.println(isSameRM);
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
		return isSameRM;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
	 */
	@Override
	public int prepare(Xid xid) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("prepare:");
			pf.print("\tXid:");
			pf.println(xid);
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
		return XA_OK;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#recover(int)
	 */
	@Override
	public Xid[] recover(int flag) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("recover:");
			pf.print("\tFlags:");
			pf.println(flag);
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
		Xid[] salida = new Xid[] {};
		for (WorkContext wc : workContext) {
			if (wc instanceof TransactionContext) {
				salida = new Xid[] { ((TransactionContext) wc).getXid() };
				break;
			}
		}
		return salida;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
	 */
	@Override
	public void rollback(Xid xid) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("rollback:");
			pf.print("\tXid:");
			pf.println(xid);
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
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
	 */
	@Override
	public boolean setTransactionTimeout(int seconds) throws XAException {
		this.transactionTimeout = seconds;
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("setTransactionTimeout:");
			pf.print("\ttimeout:");
			pf.println(seconds);
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void start(Xid xid, int flags) throws XAException {
		if (logger.isDebugEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			pf.println();
			pf.println("start:");
			pf.print("\tXid:");
			pf.println(xid);
			pf.print("\tFlags: ");
			pf.println(flags);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkContextLifecycleListener#contextSetupComplete()
	 */
	@Override
	public void contextSetupComplete() {
		if (logger.isDebugEnabled()) {
			logger.debug("contextSetupComplete()");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.WorkContextLifecycleListener#contextSetupFailed(java.lang.String)
	 */
	@Override
	public void contextSetupFailed(String errorCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("contextSetupFailed('" + errorCode + "')");
		}
	}
}
