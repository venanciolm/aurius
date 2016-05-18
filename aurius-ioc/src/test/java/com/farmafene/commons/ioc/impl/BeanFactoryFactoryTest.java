package com.farmafene.commons.ioc.impl;

import com.farmafene.commons.ioc.IBeanFactory;
import com.farmafene.commons.ioc.IBeanFactoryFactory;
import com.farmafene.commons.ioc.IBeanFactoryLifecycleListener;
import com.farmafene.commons.ioc.IBeanFactoryParams;

public class BeanFactoryFactoryTest implements IBeanFactoryFactory {

	public BeanFactoryFactoryTest() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactoryFactory#getIBeanFactory(IBeanFactoryParams,
	 *      com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public IBeanFactory getIBeanFactory(IBeanFactoryParams params,
			IBeanFactoryLifecycleListener observer) {
		BeanFactoryTest a = new BeanFactoryTest();
		a.addIBeanFactoryLifecycleListener(observer);
		a.notifyInit();
		return a;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactoryFactory#supports(com.farmafene.commons.ioc.IBeanFactoryParams)
	 */
	@Override
	public boolean supports(IBeanFactoryParams configured) {
		return BeanFactoryParamsTest.class.equals(configured.getClass());
	}

}
