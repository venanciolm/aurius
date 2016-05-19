package com.farmafene.aurius.core;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.farmafene.aurius.core.ITransactionHelperLocator;
import com.farmafene.aurius.core.impl.TransactionHelperLocatorProxy;

public class TransactionHelperLocator {

	private static final ITransactionHelperLocator iTransactionHelperLocator = new TransactionHelperLocatorProxy();

	private TransactionHelperLocator() {
	}

	public static ITransactionHelperLocator getITransactionHelperLocator() {
		return iTransactionHelperLocator;
	}

	public static UserTransaction getUserTransaction() {
		return getITransactionHelperLocator().getUserTransaction();
	}

	public static TransactionManager getTransactionManager() {
		return getITransactionHelperLocator().getTransactionManager();
	}
}
