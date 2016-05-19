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

/**
 * Implementación de Gestor de monitorización para finalizadas
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class GestorFinalizadasProxy implements IGestorFinalizadas {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#send(com.farmafene.
	 *      aurius.mngt.CallStatus)
	 */
	@Override
	public void send(CallStatus source) {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#addListener(com.farmafene
	 *      .aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public void addListener(ITxFinalizadaListener listener) {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#removeListener(com.
	 *      farmafene.aurius.mngt.impl.ITxFinalizadaListener)
	 */
	@Override
	public boolean removeListener(ITxFinalizadaListener listener) {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#start()
	 */
	@Override
	public void start() {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#stop()
	 */
	@Override
	public void stop() {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		throw new IllegalStateException(
				"Error de compilación, esta clase no debe de ser invocada");
	}
}
