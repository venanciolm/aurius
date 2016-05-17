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
package com.farmafene.aurius.spring;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IAplicacionHolderPlugin;
import com.farmafene.aurius.ioc.IAuriusBeanFactory;
import com.farmafene.aurius.server.Configuracion;

/**
 * Plugin de adaptación para una factoría de Spring
 * 
 * @author vlopez@farmafene.com
 */
public class AplicacionHolderBeanFactory implements IAplicacionHolderPlugin,
		IAuriusBeanFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(AplicacionHolderBeanFactory.class);

	private IAplicacionHolder holder = null;
	private ConfigurableApplicationContext factory = null;

	private AplicacionHolderBeanFactory() {

	}

	public AplicacionHolderBeanFactory(IAplicacionHolder holder) {
		this();
		this.holder = holder;
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
		sb.append("holder=").append(holder);
		sb.append("}");
		return sb.toString();
	}

	private BeanFactory getBeanFactory() {
		if (factory == null) {
			throw new IllegalStateException("No inicializado");
		}
		return factory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#onCreate()
	 */
	@Override
	public void onCreate() {
		logger.debug("onCreate()");
		try {
			File root = new File(Configuracion.getConfigPath());
			File apps = new File(root.getParent(), "apps");
			File aplicacion = new File(apps, holder.getAplicacion());
			File conf = new File(aplicacion, "conf");
			File beans = new File(conf, "AuriusSpringConfiguration-beans.xml");
			if (beans.exists()) {
				factory = new FileSystemXmlApplicationContext(beans.getPath());
			} else {
				logger.warn("Fichero de configuración existente: "
						+ beans.getPath());
			}
		} catch (Throwable th) {
			logger.warn("Fichero erroneo: ", th.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(
						"Fichero de configuración erroneo o no existente: ", th);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#onDestroy()
	 */
	@Override
	public void onDestroy() {
		logger.info("onDestroy()");
		destroy();
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
			salida = getBeanFactory().getBean(clazz);
		} catch (NoSuchBeanDefinitionException e) {
			// do nothing
		} catch (IllegalStateException th) {
			throw th;
		} catch (Throwable e) {
			if (null == factory) {

			}
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
	public <O> O getBean(Class<O> clazz, String beanId) {
		O salida = null;
		try {
			salida = getBeanFactory().getBean(beanId, clazz);
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
	 * @see com.farmafene.commons.ioc.IBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		return factory == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#destroy()
	 */
	@Override
	public void destroy() {
		if (factory != null) {
			factory.close();
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
