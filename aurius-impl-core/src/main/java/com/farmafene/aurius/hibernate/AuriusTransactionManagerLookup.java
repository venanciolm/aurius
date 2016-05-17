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
package com.farmafene.aurius.hibernate;

import java.util.Properties;

import javax.transaction.TransactionManager;

import org.hibernate.HibernateException;
import org.hibernate.transaction.TransactionManagerLookup;

import com.farmafene.aurius.core.TransactionHelperLocator;

public class AuriusTransactionManagerLookup implements TransactionManagerLookup {

	public AuriusTransactionManagerLookup() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("={");
		builder.append("}");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.hibernate.transaction.TransactionManagerLookup#getTransactionManager(java.util.Properties)
	 */
	@Override
	public TransactionManager getTransactionManager(Properties props)
			throws HibernateException {
		return TransactionHelperLocator.getITransactionHelperLocator()
				.getTransactionManager();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.hibernate.transaction.TransactionManagerLookup#getUserTransactionName()
	 */
	@Override
	public String getUserTransactionName() {
		return TransactionHelperLocator.getITransactionHelperLocator()
				.getUserTransactionName();
	}
}
