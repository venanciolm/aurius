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
package com.farmafene.commons.ioc;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanFactoryTest implements IBeanFactory, InitializingBean,
		DisposableBean {

	private static final Logger logger = LoggerFactory
			.getLogger(SpringBeanFactoryTest.class);
	private ConfigurableApplicationContext factory = null;
	private boolean init = false;
	private String file;
	private List<IBeanFactoryLifecycleListener> listeners;

	public SpringBeanFactoryTest() {
		listeners = new LinkedList<IBeanFactoryLifecycleListener>();
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
		sb.append("file=").append(file);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class)
	 */
	@Override
	public <O> O getBean(Class<O> clazz) {
		O getBean = null;
		try {
			getBean = factory.getBean(clazz);
			init = true;
		} catch (NoSuchBeanDefinitionException e) {
			// do nothing
		} catch (Throwable e) {
			logger.error("getBean(Class<?>:'{}')", clazz);
			e.printStackTrace();
		}
		return getBean;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public <O> O getBean(Class<O> clazz, String idBean) {
		O salida = null;
		try {
			salida = factory.getBeanFactory().getBean(idBean, clazz);
		} catch (NoSuchBeanDefinitionException e) {
			// do nothing
		} catch (Throwable e) {
			logger.error("getBean(Class<?>:'{}', '{}')", clazz, idBean);
			logger.error("Error: ", e);
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.String)
	 */
	@Override
	public <O> O getBean(String beanId) {
		O salida = null;
		try {
			@SuppressWarnings("unchecked")
			O bean = (O) factory.getBeanFactory().getBean(beanId);
			salida = bean;
		} catch (NoSuchBeanDefinitionException e) {
			// do nothing
		} catch (Throwable e) {
			logger.error("getBean(Class<?>:'{}')", beanId);
			logger.error("Error: ", e);
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#destroy()
	 */
	@Override
	public void destroy() {
		factory.close();
		@SuppressWarnings("unchecked")
		List<IBeanFactoryLifecycleListener> newL = (LinkedList<IBeanFactoryLifecycleListener>) ((LinkedList<IBeanFactoryLifecycleListener>) listeners)
				.clone();
		for (IBeanFactoryLifecycleListener l : newL) {
			l.notifyDestroy(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		factory = new ClassPathXmlApplicationContext("classpath:" + file);
		@SuppressWarnings("unchecked")
		List<IBeanFactoryLifecycleListener> newL = (LinkedList<IBeanFactoryLifecycleListener>) ((LinkedList<IBeanFactoryLifecycleListener>) listeners)
				.clone();
		for (IBeanFactoryLifecycleListener l : newL) {
			l.notifyInit(this);
		}

	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		return init;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#addIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public void addIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#removeIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public boolean removeIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener listener) {
		boolean remove = listeners.remove(listener);
		if (!remove) {
			throw new IllegalArgumentException("Listener not exist");
		}
		return remove;
	}
}
