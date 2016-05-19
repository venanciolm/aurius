package com.farmafene.commons.hibernate;

import org.hibernate.transaction.JNDITransactionManagerLookup;
import org.hibernate.transaction.TransactionManagerLookup;

public class SimpleTransactionManagerLookup extends JNDITransactionManagerLookup
		implements TransactionManagerLookup {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.hibernate.transaction.TransactionManagerLookup#getUserTransactionName()
	 */
	@Override
	public String getUserTransactionName() {
		return "java:comp/UserTransaction";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.hibernate.transaction.JNDITransactionManagerLookup#getName()
	 */
	@Override
	protected String getName() {
		return getUserTransactionName();
	}

}
