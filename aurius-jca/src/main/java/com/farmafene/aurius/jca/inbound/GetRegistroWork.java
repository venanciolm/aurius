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

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.jca.AuriusListener;
import com.farmafene.aurius.jca.io.AuriusDiccionarioMessage;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusResponseMessage;

public class GetRegistroWork implements Work, WorkListener {

	private AuriusDiccionarioMessage msg;
	private static final Logger logger;
	// private static Method[] GETREGISTRO_METHODS;

	static {
		logger = LoggerFactory.getLogger(GetRegistroWork.class);
		// try {
		// GETREGISTRO_METHODS = new Method[] {
		// AuriusListener.class.getMethod("getRegistro", new Class[] {
		// AuthInfo.class, String.class }),
		// AuriusListener.class.getMethod("getRegistro", new Class[] {
		// AuthInfo.class, String.class, String.class }) };
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

	public GetRegistroWork() {

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
		getToken().getCallStatus().resume(Thread.currentThread().getId());
		ContextoManager.getIContextoManager().setContexto(
				getToken().getCallStatus().getContexto());
		ContextoManager.getContexto().setAuthInfo(msg.getCookie());
		try {
			boolean defecto = null == msg.getVersion();
			doPrecondiciones(defecto);
			AuriusResponseMessage msgOut = new AuriusResponseMessage();
			if (defecto) {
				msgOut.setRegistro(listener.getRegistro(msg.getCookie(),
						msg.getServicioId()));
			} else {
				msgOut.setRegistro(listener.getRegistro(msg.getCookie(),
						msg.getServicioId(), msg.getVersion()));
			}
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

	private void doPrecondiciones(boolean defecto)
			throws NoSuchMethodException, ResourceException {
		getToken().getCallStatus().resume(Thread.currentThread().getId());
		ContextoManager.getIContextoManager().setContexto(
				getToken().getCallStatus().getContexto());
		ContextoManager.getContexto().setAuthInfo(msg.getCookie());
		setListener(token.getIJCAPlataformAdapter().getAuriusListener(null));
		if (listener instanceof MessageEndpoint) {
			if (logger.isInfoEnabled()) {
				logger.info("MessageEndPoint.beforeDelivery(): " + this);
			}
			// ((MessageEndpoint) listener)
			// .beforeDelivery(GETREGISTRO_METHODS[defecto ? 0 : 1]);
			if (logger.isInfoEnabled()) {
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
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndpoint.afterDelivery()<end>");
			}
			// } catch (ResourceException e) {
			// logger.error("", e);
			// }
			if (logger.isDebugEnabled()) {
				logger.debug("MessageEndpoint.release(): " + this);
			}
			endPoint.release();
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
	}

	public AuriusDiccionarioMessage getMsg() {
		return this.msg;
	}

	public void setMsg(AuriusDiccionarioMessage msg) {
		this.msg = msg;
	}

}
