/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.ioc.impl;

import java.io.File;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.farmafene.aurius.ioc.IAuriusBeanFactory;

public class AuriusBeanFactoryImpl implements IAuriusBeanFactory {

	private static final String JNDI_AURIUS_CONF = "java:comp/env/farmafene.com/aurius/conf";
	private static final Logger logger = LoggerFactory
			.getLogger(AuriusBeanFactoryImpl.class);

	private static Boolean init = Boolean.FALSE;

	private static class BeanFactoryHolder {

		private static final BeanFactoryHolder holder = new BeanFactoryHolder();
		private ConfigurableApplicationContext factory = null;

		private BeanFactoryHolder() {
			synchronized (AuriusBeanFactoryImpl.init) {
				if (AuriusBeanFactoryImpl.init) {
					return;
				}
				try {
					logger
							.info("Tratando de conseguir la configuración de JNDI");
					try {
						Object obj = new InitialContext()
								.lookup(JNDI_AURIUS_CONF);
						File f = null;
						if (obj != null && (obj instanceof String)) {
							f = new File((String) obj);
						}
						if (obj != null && (obj instanceof URL)) {
							f = new File(((URL) obj).toURI());
						}
						logger.debug("El fichero es: " + f);
						if (f != null) {
							factory = new FileSystemXmlApplicationContext(
									new File(f.getParent(),
											"AuriusSpringConfiguration-beans.xml")
											.getPath());
						}
					} catch (NamingException e) {
						logger.debug("NamingException: " + e.getMessage());
					} catch (Exception e) {
						logger.debug("Exception: ", e);
					}
					if (factory == null) {
						logger
								.info("No ha sido posible, obteniendola del ClassPath ...");
						try {
							factory = new ClassPathXmlApplicationContext(
									"classpath:AuriusSpringConfiguration-beans.xml");
						} catch (Exception e) {
							logger.debug("Exception: ", e);
						}
					}
				} finally {
					if (factory != null) {
						AuriusBeanFactoryImpl.init = Boolean.TRUE;
					} else {
						logger
								.error("No ha sido posible instanciar la factoría!!!!");
					}
				}
			}
		}

		public static BeanFactoryHolder getHolder() {
			return holder;
		}

		public ConfigurableApplicationContext getBeanFactory() {
			return factory;
		}
	}

	public void destroy() {
		logger.info("destroy()");
		synchronized (init) {
			if (init.booleanValue()) {
				BeanFactoryHolder.getHolder().getBeanFactory().close();
			}
		}
	}

	public boolean forceInit() {
		boolean salida = !init;
		BeanFactoryHolder.getHolder();
		return salida;
	}

	public <B extends Object> B getBean(Class<B> clazz) {
		try {
			return BeanFactoryHolder.getHolder().getBeanFactory()
					.getBean(clazz);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		} catch (Throwable e) {
			logger.error("getBean(Class<?>:'" + clazz + "')", e);
			return null;
		}
	}

	public Object getBean(String beanId) {
		try {
			return BeanFactoryHolder.getHolder().getBeanFactory().getBean(
					beanId);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		} catch (Throwable e) {
			logger.error("getBean(String:'" + beanId + "')", e);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.farmafene.aurius.ioc.IAuriusBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		return init;
	}
}
