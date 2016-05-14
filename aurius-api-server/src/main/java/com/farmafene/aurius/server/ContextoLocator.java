/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.server;

import com.farmafene.aurius.server.impl.ContextoLocatorImpl;
import com.farmafene.aurius.util.ProxyFactory;

/**
 * Localizador del contexto
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
public class ContextoLocator {
	private static final IContextoLocator locator = new IContextoLocator() {
		private IContextoLocator instance = new ContextoLocatorImpl();
		private ProxyFactory<Contexto> proxyFactory;

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.IContextoLocator#getContexto()
		 */
		@Override
		public Contexto getContexto() {
			return getProxyFactory().newInstance(instance.getContexto());
		}

		private ProxyFactory<Contexto> getProxyFactory() {
			if (null == proxyFactory) {
				proxyFactory = new ProxyFactory<Contexto>();
				proxyFactory.setInterfaces(Contexto.class);
			}
			return proxyFactory;
		}
	};

	private ContextoLocator() {

	}

	/**
	 * Obtiene la factoría activa de contextos
	 * 
	 * @return
	 * @since 1.0.0
	 */
	public static IContextoLocator getIContextoLocator() {
		return locator;
	}

	/**
	 * @see IContextoLocator#getContexto()
	 * @since 1.0.0
	 */
	public static Contexto getContexto() {
		return getIContextoLocator().getContexto();
	}
}
