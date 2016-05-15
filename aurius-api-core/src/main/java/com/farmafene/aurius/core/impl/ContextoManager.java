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
package com.farmafene.aurius.core.impl;

import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.IContextoManager;

public class ContextoManager implements IContextoManager {

	/**
	 * Constructor por defecto
	 */
	public ContextoManager() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#createContext()
	 */
	@Override
	public ContextoCore createContext() {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#getContexto()
	 */
	@Override
	public ContextoCore getContexto() {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#setContexto(com.farmafene.aurius.core.ContextoCore)
	 */
	@Override
	public void setContexto(ContextoCore ctx) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#setIdServicio(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String)
	 */
	@Override
	public void setIdServicio(ContextoCore ctx, String id) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#setIdServicioActual(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String)
	 */
	@Override
	public void setIdServicioActual(ContextoCore ctx, String version) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#setVersionServicio(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String)
	 */
	@Override
	public void setVersionServicio(ContextoCore ctx, String version) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IContextoManager#setVersionServicioActual(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String)
	 */
	@Override
	public void setVersionServicioActual(ContextoCore ctx, String version) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}
}
