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
/**
 * 
 */
package com.farmafene.aurius.mngt.impl;

import com.farmafene.aurius.core.IGestorMonitorizacion;
import com.farmafene.aurius.mngt.IOperacionEventObserver;
import com.farmafene.aurius.mngt.OperacionEvent;

/**
 * @author vlopez
 * 
 */
public class GestorMonitorizacion implements IGestorMonitorizacion {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#addListener(com.farmafene.aurius.mngt.IOperacionEventObserver)
	 */
	@Override
	public void addListener(IOperacionEventObserver listener) {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#fire(com.farmafene.aurius
	 *      .mngt.OperacionEvent)
	 */
	@Override
	public void fire(OperacionEvent event) {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#isStarted()
	 */
	@Override
	public boolean isStarted() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#removeListener(com.farmafene
	 *      .aurius.mngt.IOperacionEventObserver)
	 */
	@Override
	public boolean removeListener(IOperacionEventObserver listener) {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#start()
	 */
	@Override
	public void start() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IGestorMonitorizacion#stop()
	 */
	@Override
	public void stop() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ IGestorMonitorizacion.class.getName());
	}
}
