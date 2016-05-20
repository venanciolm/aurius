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
package com.farmafene.aurius.jca;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.AuriusContainerSubject;
import com.farmafene.aurius.core.IAuriusContainerObserver;

public class AuriusResourceAdapter implements ResourceAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusResourceAdapter.class);
	private BootstrapContext bootStrapContext;
	private Map<ActivationSpec, MessageEndpointFactory> activationSpecs;

	public AuriusResourceAdapter() {
		logger.info("AuriusResourceAdapter<init>()");
		activationSpecs = new ConcurrentHashMap<ActivationSpec, MessageEndpointFactory>();
		AuriusContainerSubject.getIAuriusContainerSubject().addInitial(
				new IAuriusContainerObserver() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see java.lang.Object#toString()
					 */
					@Override
					public String toString() {
						StringBuilder sb = new StringBuilder();
						sb.append(AuriusResourceAdapter.class.getSimpleName()
								+ ".IAuriusContainerObserver");
						sb.append("={");
						sb.append("with ").append(activationSpecs.size())
								.append(" activationSpecs.");
						sb.append("}");
						return sb.toString();
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
					 * {@inheritDoc}
					 * 
					 * @see com.farmafene.aurius.core.IAuriusContainerObserver#stop()
					 */
					@Override
					public void stop() {
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
					 * @see com.farmafene.aurius.core.IAuriusContainerObserver#start()
					 */
					@Override
					public void start() {
						for (ActivationSpec as : activationSpecs.keySet()) {
							((AuriusJMSActivationSpecBasic) as).start();
						}
					}
				});
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activationSpecs == null) ? 0 : activationSpecs.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AuriusResourceAdapter)) {
			return false;
		}
		AuriusResourceAdapter other = (AuriusResourceAdapter) obj;
		if (activationSpecs == null) {
			if (other.activationSpecs != null) {
				return false;
			}
		} else if (!activationSpecs.equals(other.activationSpecs)) {
			return false;
		}
		return true;
	}

	public void endpointActivation(MessageEndpointFactory mepf,
			ActivationSpec as) throws ResourceException {
		logger.info("endpointActivation('" + mepf + "','" + as + "')");
		activationSpecs.put(as, mepf);
		// AuriusJMSActivationSpecBasic myAs = (AuriusJMSActivationSpecBasic) as;
		// delayled !!! myAs.start();
		logger.info("endpointActivation() -Done by '" + as + "'");
	}

	public void endpointDeactivation(MessageEndpointFactory mepf,
			ActivationSpec as) {
		logger.info("endpointDeactivation('" + mepf + "','" + as + "')");
		activationSpecs.remove(as);
		((AuriusJMSActivationSpecBasic) as).stop();
		logger.info("endpointDeactivation() -Done by '" + as + "'");
	}

	public XAResource[] getXAResources(ActivationSpec[] as)
			throws ResourceException {
		logger.info("getXAResources('" + as + "')");
		// FIXME --
		return new XAResource[] {};
	}

	public void start(BootstrapContext bsc)
			throws ResourceAdapterInternalException {
		this.bootStrapContext = bsc;
		logger.info("start('" + bootStrapContext + "')");
	}

	public void stop() {
		logger.info("stop()");
		stopListener();
		logger.info("stop() - Done");
	}

	private void stopListener() {
		for (ActivationSpec as : activationSpecs.keySet()) {
			((AuriusJMSActivationSpecBasic) as).stop();
		}

	}

	public MessageEndpointFactory getMessageEndpointFactory(ActivationSpec as) {
		MessageEndpointFactory mepf = activationSpecs.get(as);
		if (logger.isDebugEnabled()) {
			logger.debug("getMessageEndpointFactory('" + as + "'): " + mepf);
		}
		return mepf;
	}

	public WorkManager getWorkManager() {
		logger.debug("getWorkManager()");
		return bootStrapContext.getWorkManager();
	}

	public XATerminator getXATerminator() {
		logger.debug("getXATerminator()");
		return bootStrapContext.getXATerminator();
	}

	public Timer createTimer() throws UnavailableException {
		logger.debug("createTimer()");
		return bootStrapContext.createTimer();
	}
}
