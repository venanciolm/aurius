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
package com.farmafene.commons.hibernate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHBSessionFactoryFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractHBSessionFactoryFactory.class);
	private HBAnnotationSessionFactory iHibernateSessionFactory;
	private String basePackage;
	private String lookupClass;
	private Map<String, String> properties;
	private Map<String, HBSessionFactoryProperties> sessionProperties;
	private ClassLoader classLoader;

	public AbstractHBSessionFactoryFactory() {
		logger.debug(this + "<init>");
		sessionProperties = new HashMap<String, HBSessionFactoryProperties>();
		properties = new HashMap<String, String>();
		basePackage = "";
		iHibernateSessionFactory = new HBAnnotationSessionFactory();
		classLoader = Thread.currentThread().getContextClassLoader();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("}");
		return sb.toString();
	}

	protected HBAnnotationFinder getHBAnnotationFinder() {
		return new HBAnnotationFinder();
	}

	/**
	 * InitMethod de Spring
	 */
	public void buildIHibernateSessionFactory() {
		logger.debug(this + "buildIHibernateSessionFactory()");

		URL[] urls = getURLResources(classLoader);
		HBAnnotationFinder finder = getHBAnnotationFinder();
		HBAnnotationHolder holder = new HBAnnotationHolder();
		finder.setUrls(urls);
		finder.setHolder(holder);
		holder.setClassLoader(classLoader);
		holder.setBasePackage(basePackage);
		finder.find();
		for (HibernateSessionFactory hf : holder.getHibernateFactories()) {
			HBSessionFactoryProperties sfp = getHBSessionFactoryProperties(
					hf.getName(), hf.getJNDIName());
			IHibernateAdapter iHibernateAdapter = getIHibernateAdapter();
			if (logger.isDebugEnabled()) {
				logger.debug(" - El getIHibernateAdapter es {}",
						iHibernateAdapter);
			}
			if (null != sfp) {
				sfp.setIHibernateAdapter(iHibernateAdapter);
				Properties props = new Properties();
				props.putAll(properties);
				props.putAll(sfp.getPropiedades());
				hf.setProperties(props);
				Set<Class<?>> classes = holder.getHibernateEntities().get(
						hf.getName());
				hf.addClasses(classes);
				logFactory(hf, props, classes);
				try {
					hf.buildSessionFactory();
					iHibernateSessionFactory.addSessionFactory(hf);
				} catch (Throwable th) {
					logger.error(
							"Error al generar la factoría: " + hf.getName(), th);
					logger.error("El classLoader es: "
							+ Thread.currentThread().getContextClassLoader());
					logger.error("El classLoader factory: "
							+ hf.getClass().getClassLoader());
				}

			} else {
				logger.error("No se ha definido la factoría: [" + hf.getName()
						+ "]");
			}
		}
	}

	/**
	 * Do nothing!
	 * 
	 * @return
	 */
	protected IHibernateAdapter getIHibernateAdapter() {
		return null;
	}

	public HBSessionFactoryProperties getHBSessionFactoryProperties(
			String name, String jndiName) {
		return sessionProperties.get(name);
	}

	public abstract URL[] getURLResources(ClassLoader cl);

	private void logFactory(HibernateSessionFactory hf, Properties props,
			Set<Class<?>> classes) {
		if (logger.isInfoEnabled()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream sb = new PrintStream(baos);
			sb.println();
			sb.print("HibernateFactory name=");
			sb.println(hf.getName());
			sb.println(" - properties:");
			for (Object pkey : props.keySet()) {
				sb.print("    - ");
				sb.print(pkey);
				sb.print("=");
				sb.println(props.get(pkey));
			}
			sb.println(" - classes:");
			for (Class<?> clazz : classes) {
				sb.print("    - ");
				sb.println(clazz.getName());
			}
			sb.flush();
			try {
				baos.flush();
			} catch (IOException e) {
			}
			logger.info(baos.toString());
			sb.close();
			try {
				baos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * sets the classloader
	 * 
	 * @param classLoader
	 *            classLoader to set
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * FactoryMethod para Spring
	 * 
	 * @return the iHibernateSessionFactory
	 */
	public IHibernateSessionFactory getIHibernateSessionFactory() {
		logger.debug("getIHibernateSessionFactory");
		return iHibernateSessionFactory;
	}

	/**
	 * @param basePackage
	 *            the basePackage to set
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	/**
	 * @param lookupClass
	 *            the lookupClass to set
	 */
	public void setLookupClass(String lookupClass) {
		if (null != lookupClass) {
			logger.debug("Setting 'hibernate.transaction.manager_lookup_class'="
					+ this.lookupClass);
			properties.put("hibernate.transaction.manager_lookup_class",
					lookupClass);
		}
		this.lookupClass = lookupClass;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<String, String> properties) {
		if (null != properties) {
			this.properties.putAll(properties);
		}
	}

	/**
	 * @param sessionProperties
	 *            the sessionProperties to set
	 */
	public void setSessionProperties(
			Set<HBSessionFactoryProperties> sessionProperties) {
		if (null != sessionProperties) {
			for (HBSessionFactoryProperties p : sessionProperties) {
				this.sessionProperties.put(p.getRefName(), p);
			}
		}
	}
}
