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
package com.farmafene.aurius.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAuriusContainerObserver;
import com.farmafene.aurius.core.IAuriusContainerSubject;

public class AuriusContainerSubject implements IAuriusContainerSubject {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusContainerSubject.class);

	private List<IAuriusContainerObserver> listeners;
	private List<IAuriusContainerObserver> initials;
	private boolean started = false;

	public AuriusContainerSubject() {
		this.listeners = new CopyOnWriteArrayList<IAuriusContainerObserver>();
		this.initials = new CopyOnWriteArrayList<IAuriusContainerObserver>();
		logger.debug(this + "<init>");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#addInitial(com.farmafene.aurius.core.IAuriusContainerObserver)
	 */
	@Override
	public void addInitial(IAuriusContainerObserver onStart) {
		initials.add(onStart);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#add(com.farmafene.aurius.core.IAuriusContainerObserver)
	 */
	@Override
	public void add(IAuriusContainerObserver obj) {
		listeners.add(obj);
		if (logger.isDebugEnabled()) {
			logger.debug(this.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#remove(com.farmafene
	 *      .aurius.core.IAuriusContainerObserver)
	 */
	@Override
	public boolean remove(IAuriusContainerObserver obj) {
		boolean salida = listeners.remove(obj);
		if (logger.isDebugEnabled()) {
			logger.debug(this.toString());
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#start()
	 */
	@Override
	public void start() {
		if (!started) {
			started = true;
			for (IAuriusContainerObserver o : initials) {
				logger.debug("start('" + o + "')");
				try {
					o.start();
				} catch (Throwable th) {
					logger.error("Error al arrancar: " + o
							+ ", se ha producido una excepción:", th);
				}
			}
		}
		for (IAuriusContainerObserver o : listeners) {
			logger.debug("start('" + o + "')");
			try {
				o.start();
			} catch (Throwable th) {
				logger.error("Error al arrancar: " + o
						+ ", se ha producido una excepción:", th);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#startCluster()
	 */
	@Override
	public void startCluster() {
		for (IAuriusContainerObserver o : listeners) {
			try {
				o.startCluster();
			} catch (Throwable th) {
				logger.error("Error al arrancar el cluster: " + o
						+ ", se ha producido una excepción:", th);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#stop()
	 */
	@Override
	public void stop() {
		List<IAuriusContainerObserver> listeners = new ArrayList<IAuriusContainerObserver>(
				this.listeners);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			IAuriusContainerObserver o = listeners.get(i);
			try {
				if (logger.isInfoEnabled()) {
					logger.info("closing " + o);
				}
				o.stop();
				if (logger.isInfoEnabled()) {
					logger.info("closed " + o);
				}
			} catch (Throwable th) {
				logger.error("Error al parar: " + o
						+ ", se ha producido una excepción:", th);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * @see com.farmafene.aurius.core.IAuriusContainerSubject#stopCluster()
	 */
	@Override
	public void stopCluster() {
		List<IAuriusContainerObserver> listeners = new ArrayList<IAuriusContainerObserver>(
				this.listeners);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			IAuriusContainerObserver o = listeners.get(i);
			try {
				o.stopCluster();
			} catch (Throwable th) {
				logger.error("Error al parar el cluster: " + o
						+ ", se ha producido una excepción:", th);
			}
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
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("with ").append(listeners.size()).append(" observers");
		sb.append("}");
		return sb.toString();
	}
}