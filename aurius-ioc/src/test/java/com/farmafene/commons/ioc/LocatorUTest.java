package com.farmafene.commons.ioc;

import org.junit.Assert;
import org.junit.Test;

import com.farmafene.commons.ioc.impl.BeanFactoryImpl;
import com.farmafene.commons.ioc.impl.BeanFactoryParamsTest;
import com.farmafene.commons.ioc.impl.BeanFactoryParamsTest2;
import com.farmafene.commons.ioc.impl.BeanFactoryTest;

public class LocatorUTest {

	@Test
	public void testBeanFactoryLocator() {
		BeanFactoryLocatorImpl a = new BeanFactoryLocatorImpl();
		Exception e = null;
		try {
			a.setTestMode(new BeanFactoryParamsTest2());
		} catch (Exception e1) {
			e = e1;
		}
		Assert.assertNotNull(e);
		Assert.assertNotNull(a.getIBeanFactory());
		Assert.assertEquals(BeanFactoryImpl.class, a.getIBeanFactory()
				.getClass());
		a.setTestMode(new BeanFactoryParamsTest());
		Assert.assertNotNull(a.getIBeanFactory());
		Assert.assertEquals(BeanFactoryTest.class, a.getIBeanFactory()
				.getClass());
		a.getIBeanFactory().destroy();
		Assert.assertEquals(BeanFactoryImpl.class, a.getIBeanFactory()
				.getClass());
	}
}
