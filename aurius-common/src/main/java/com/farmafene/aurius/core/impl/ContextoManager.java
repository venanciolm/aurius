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
import com.farmafene.aurius.core.UUIDFactory;
import com.farmafene.aurius.server.Contexto;
import com.farmafene.aurius.server.IContextoLocator;

/**
 * Implementa la factor�a de localización y gestión de contextos
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContextoManager implements IContextoManager {

	private ThreadLocal<ContextoCore> holder;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public ContextoManager() {
		holder = new ThreadLocal<ContextoCore>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IContextoLocator#getContexto()
	 * @since 1.0.0
	 */
	@Override
	public ContextoCore getContexto() {
		return holder.get();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IContextoManager#setContexto(Contexto)
	 * @since 1.0.0
	 */
	public void setContexto(ContextoCore contexto) {
		holder.set(contexto);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IContextoManager#createContext()
	 * @since 1.0.0
	 */
	@Override
	public ContextoCore createContext() {
		ContextoImpl ctx = new ContextoImpl();
		ctx.setId(UUIDFactory.getNewUUID());
		ctx.setInitTime(System.currentTimeMillis());
		return ctx;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void setIdServicio(ContextoCore ctx, String id) {
		if (ctx == null) {
			throw new IllegalArgumentException("El contexto no puede ser null.");
		}
		((ContextoImpl) ctx).setIdServicio(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void setIdServicioActual(ContextoCore ctx, String id) {
		if (ctx == null) {
			throw new IllegalArgumentException("El contexto no puede ser null.");
		}
		((ContextoImpl) ctx).setIdServicioActual(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void setVersionServicio(ContextoCore ctx, String version) {
		if (ctx == null) {
			throw new IllegalArgumentException("El contexto no puede ser null.");
		}
		((ContextoImpl) ctx).setVersionServicio(version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void setVersionServicioActual(ContextoCore ctx, String version) {
		if (ctx == null) {
			throw new IllegalArgumentException("El contexto no puede ser null.");
		}
		((ContextoImpl) ctx).setVersionServicioActual(version);
	}
}
