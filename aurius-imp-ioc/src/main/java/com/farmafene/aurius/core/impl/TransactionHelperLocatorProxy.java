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

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.farmafene.aurius.core.ITransactionHelperLocator;
import com.farmafene.commons.ioc.BeanFactory;

public class TransactionHelperLocatorProxy implements ITransactionHelperLocator {

	public TransactionHelperLocatorProxy() {

	}

	private ITransactionHelperLocator getITransactionHelperLocator() {
		return BeanFactory.getBean(ITransactionHelperLocator.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return getITransactionHelperLocator().getImplementationVentor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return getITransactionHelperLocator().getImplementationVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return getITransactionHelperLocator().getImplementationDescription();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getUserTransaction()
	 */
	@Override
	public UserTransaction getUserTransaction() {
		return getITransactionHelperLocator().getUserTransaction();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getUserTransactionName()
	 */
	@Override
	public String getUserTransactionName() {
		return getITransactionHelperLocator().getUserTransactionName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getTransactionManager()
	 */
	@Override
	public TransactionManager getTransactionManager() {
		return getITransactionHelperLocator().getTransactionManager();
	}
}
