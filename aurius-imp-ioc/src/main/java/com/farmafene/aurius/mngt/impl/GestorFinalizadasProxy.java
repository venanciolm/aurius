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

import com.farmafene.aurius.mngt.CallStatus;
import com.farmafene.commons.ioc.BeanFactory;

/**
 * Implementación de Gestor de monitorización de transacciones finalizadas.
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class GestorFinalizadasProxy implements IGestorFinalizadas {

	public GestorFinalizadasProxy() {
		IGestorFinalizadas proxy = BeanFactory
				.getBean(IGestorFinalizadas.class);
		if (proxy == null) {
			throw new IllegalStateException("No existe el bean");
		}
	}

	private IGestorFinalizadas getIGestorFinalizadas() {
		return BeanFactory.getBean(IGestorFinalizadas.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#send(com.farmafene.aurius.mngt.CallStatus)
	 */
	@Override
	public void send(CallStatus source) {
		getIGestorFinalizadas().send(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#addListener(com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public void addListener(ITxFinalizadaListener listener) {
		getIGestorFinalizadas().addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#removeListener(com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public boolean removeListener(ITxFinalizadaListener listener) {
		return getIGestorFinalizadas().removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#start()
	 */
	@Override
	public void start() {
		getIGestorFinalizadas().start();

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#stop()
	 */
	@Override
	public void stop() {
		getIGestorFinalizadas().stop();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return getIGestorFinalizadas().getImplementationVentor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return getIGestorFinalizadas().getImplementationVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return getIGestorFinalizadas().getImplementationDescription();
	}
}
