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
package com.farmafene.commons.ioc.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;

import com.farmafene.commons.ioc.IBeanFactory;
import com.farmafene.commons.ioc.IBeanFactoryLifecycleListener;

public class BeanFactoryImpl implements IBeanFactory {
	private static Boolean INIT = false;
	private static final List<IBeanFactoryLifecycleListener> listeners = new LinkedList<IBeanFactoryLifecycleListener>();
	private static final Logger logger = LoggerFactory
			.getLogger(BeanFactoryImpl.class);

	public BeanFactoryImpl() {
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
	 * @see com.farmafene.commons.ioc.IBeanFactory#destroy()
	 */
	@Override
	public void destroy() {
		for (IBeanFactoryLifecycleListener l : new LinkedList<IBeanFactoryLifecycleListener>(
				listeners)) {
			l.notifyDestroy(this);
		}
		Holder.getInstance().destroy();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class)
	 */
	@Override
	public <O> O getBean(Class<O> clazz) {
		return Holder.getInstance().getBean(clazz);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public <O> O getBean(Class<O> clazz, String beanId) {
		return Holder.getInstance().getBean(clazz, beanId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.String)
	 */
	@Override
	public <O> O getBean(String beanId) {
		return Holder.getInstance().getBean(beanId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		synchronized (INIT) {
			return INIT;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#addIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public void addIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener observer) {
		listeners.add(observer);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#removeIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public boolean removeIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener observer) {
		return listeners.remove(observer);
	}

	static class Holder implements IBeanFactory {
		private static final Holder INSTANCE = new Holder();
		private ConfigurableApplicationContext factory;

		private static final Holder getInstance() {
			return INSTANCE;
		}

		private Holder() {
			synchronized (INIT) {
				if (INIT) {
					return;
				}
				try {
					SpringApplicationContextFactory f = new SpringApplicationContextFactory();
					factory = f.getISpringConfigurableApplicationContext()
							.getSpringConfigurableApplicationContext();
				} finally {
					if (factory != null) {
						INIT = true;
					} else {
						logger.error("No ha sido posible instanciar la factoría!!!!");
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class)
		 */
		@Override
		public <O> O getBean(Class<O> clazz) {
			O salida = null;
			try {
				salida = factory.getBeanFactory().getBean(clazz);
			} catch (NoSuchBeanDefinitionException e) {
				// do nothing
			} catch (Throwable e) {
				logger.error("getBean(Class<?>:'" + clazz + "')", e);
			}
			return salida;
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
				logger.error("getBean(Class<?>:'" + clazz + "', '" + idBean
						+ "')", e);
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
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.commons.ioc.IBeanFactory#isInit()
		 */
		@Override
		public boolean isInit() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.commons.ioc.IBeanFactory#addIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
		 */
		@Override
		public void addIBeanFactoryLifecycleListener(
				IBeanFactoryLifecycleListener observer) {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.commons.ioc.IBeanFactory#removeIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
		 */
		@Override
		public boolean removeIBeanFactoryLifecycleListener(
				IBeanFactoryLifecycleListener observer) {
			throw new UnsupportedOperationException();
		}
	}
}
