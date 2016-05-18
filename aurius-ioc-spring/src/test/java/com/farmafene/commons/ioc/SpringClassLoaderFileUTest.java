package com.farmafene.commons.ioc;

import org.junit.Assert;
import org.junit.Test;

public class SpringClassLoaderFileUTest {

	@Test
	public void load() {
		BeanFactory.getIBeanFactoryManager().setTestMode(
				new SpringClassLoaderFile("spring-beans-test.xml"));
		Assert.assertNotNull(BeanFactory.getBean(String.class));
		Assert.assertNotNull(BeanFactory.getBean(String.class, "string"));
		String aux = BeanFactory.getBean("string");
		Assert.assertNotNull(aux);
	}
}
