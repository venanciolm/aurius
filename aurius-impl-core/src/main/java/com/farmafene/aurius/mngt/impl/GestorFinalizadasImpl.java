/*
 * Copyright (c) 2009-2014 farmafene.com
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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.mngt.CallStatus;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class GestorFinalizadasImpl implements IGestorFinalizadas, Work {
	private static final Logger logger = LoggerFactory
			.getLogger(GestorFinalizadasImpl.class);

	private List<ITxFinalizadaListener> listeners;
	private List<ITxFinalizadaListener> others;
	private WorkManager workManager;
	private BlockingQueue<CallStatus> queue;

	public GestorFinalizadasImpl() {
		queue = new LinkedBlockingDeque<CallStatus>();
		listeners = new LinkedList<ITxFinalizadaListener>();
		others = new LinkedList<ITxFinalizadaListener>();
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

	public void init() {
		if (null != workManager) {
			try {
				workManager.scheduleWork(this);
			} catch (WorkException e) {
				logger.error("Error al arrancar el WorkManager de Finalizadas",
						e);
			}
		}
	}

	public void setListeners(List<ITxFinalizadaListener> listeners) {
		if (logger.isDebugEnabled()) {
			logger.debug("setListeners(" + listeners + ")");
		}
		if (listeners != null) {
			synchronized (this) {
				List<ITxFinalizadaListener> nuevo = new LinkedList<ITxFinalizadaListener>(
						listeners);
				this.listeners = nuevo;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#addListener(com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public void addListener(ITxFinalizadaListener listener) {
		if (logger.isDebugEnabled()) {
			logger.debug("addListener(" + listener + ")");
		}
		if (listener != null) {
			synchronized (this) {
				List<ITxFinalizadaListener> nuevo = new LinkedList<ITxFinalizadaListener>(
						listeners);
				nuevo.add(listener);
				this.listeners = nuevo;
				others.add(listener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#removeListener(com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public boolean removeListener(ITxFinalizadaListener listener) {
		List<ITxFinalizadaListener> nuevo = null;
		boolean antiguo = false;
		if (listener != null) {
			synchronized (this) {
				nuevo = new LinkedList<ITxFinalizadaListener>(listeners);
				nuevo.remove(listener);
				this.listeners = nuevo;
				antiguo = others.remove(listener);
			}
		}
		return antiguo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#send(com.farmafene.aurius.mngt.CallStatus)
	 */
	@Override
	public void send(CallStatus status) {
		if (logger.isDebugEnabled()) {
			logger.debug("send(" + status + ")");
		}
		if (null != status) {
			queue.add(status);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info(this + "<running>");
		}
		for (;;) {
			CallStatus status = null;
			if (logger.isDebugEnabled()) {
				logger.debug(this + "<waiting>");
			}
			try {
				status = queue.take();
				List<ITxFinalizadaListener> listeners = this.listeners;
				if (listeners.size() > 0) {
					TxFinalizadasWorker worker = new TxFinalizadasWorker();
					worker.setStatus(status);
					worker.setListeners(listeners);
					try {
						workManager.scheduleWork(worker);
					} catch (WorkException e) {
						logger.error("Error al procesar el trabajo", e);
					} catch (Throwable e) {
						logger.error("Error al procesar el trabajo", e);
					}
				}
			} catch (InterruptedException e) {
				logger.info(this + ": interrumpido!!!!", e);
				break;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Procesando: " + status);
			}

		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return "Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return "1.0";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return "Volcado a fichero de Logs de Transacciones";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		if (logger.isInfoEnabled()) {
			logger.info(this + "<release>");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#start()
	 */
	@Override
	public void start() {
		if (logger.isInfoEnabled()) {
			logger.info(this + "<start>");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#stop()
	 */
	@Override
	public void stop() {
		if (logger.isInfoEnabled()) {
			logger.info(this + "<stop>");
		}
	}

	/**
	 * @param workManager
	 *            el workManager a establecer
	 */
	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

}
