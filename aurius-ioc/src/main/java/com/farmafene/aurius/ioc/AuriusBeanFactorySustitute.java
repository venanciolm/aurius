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
package com.farmafene.aurius.ioc;

class AuriusBeanFactorySustitute implements IAuriusBeanFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#getBean(java.lang.Class)
	 */
	@Override
	public <M extends Object> M getBean(Class<M> clazz) {
		throw new IllegalStateException("No se ha establecido la factoría: "
				+ IAuriusBeanFactory.class.getName()
				+ ", configure el sistema adecuadamente.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#getBean(java.lang.String)
	 */
	@Override
	public Object getBean(String id) {
		throw new IllegalStateException("No se ha establecido la factoría: "
				+ IAuriusBeanFactory.class.getName()
				+ ", configure el sistema adecuadamente.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#destroy()
	 */
	@Override
	public void destroy() {
		throw new IllegalStateException("No se ha establecido la factoría: "
				+ IAuriusBeanFactory.class.getName()
				+ ", configure el sistema adecuadamente.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#forceInit()
	 */
	@Override
	public boolean forceInit() {
		throw new IllegalStateException("No se ha establecido la factoría: "
				+ IAuriusBeanFactory.class.getName()
				+ ", configure el sistema adecuadamente.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		throw new IllegalStateException("No se ha establecido la factoría: "
				+ IAuriusBeanFactory.class.getName()
				+ ", configure el sistema adecuadamente.");
	}

}
