/*
 * Copyright (c) 2009-2012 farmafene.com
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
package com.farmafene.aurius.mngt.impl;

import java.util.List;

import javax.resource.spi.work.Work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.mngt.CallStatus;

public class TxFinalizadasWorker implements Work {

	private static final Logger logger = LoggerFactory
			.getLogger(TxFinalizadasWorker.class);

	private List<ITxFinalizadaListener> listeners;
	private CallStatus status;

	public TxFinalizadasWorker() {
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<init>");
		}
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
		sb.append("thread=").append(Thread.currentThread().getName());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		logger.info(this + ": release(");
		// Do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (status != null) {
			List<ITxFinalizadaListener> listeners = this.listeners;
			for (ITxFinalizadaListener l : listeners) {
				try {
					l.fire(status);
				} catch (Throwable th) {
					logger.error("error en el bucle de notificacion", th);
				}
			}
		}
	}

	/**
	 * @param listeners
	 *            the listeners to set
	 */
	public void setListeners(List<ITxFinalizadaListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(CallStatus status) {
		this.status = status;
	}
}
