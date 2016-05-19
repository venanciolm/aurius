package com.farmafene.aurius.core.impl;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import com.farmafene.aurius.core.ITransactionHelperLocator;

public class TransactionHelperLocatorProxy implements ITransactionHelperLocator {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getUserTransaction()
	 */
	@Override
	public UserTransaction getUserTransaction() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getTransactionManager()
	 */
	@Override
	public TransactionManager getTransactionManager() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getUserTransactionName()
	 */
	@Override
	public String getUserTransactionName() {
		throw new IllegalStateException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada: "
						+ ITransactionHelperLocator.class.getName());
	}
}
