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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación de búsqueda de anotaciones
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class HBAnnotationHolder implements IAnnotationHolder {

	private static final Logger logger = LoggerFactory
			.getLogger(HBAnnotationHolder.class);
	private String basePackage;
	private ClassLoader classLoader;
	private Set<HibernateSessionFactory> hibernateFactories;
	private Map<String, Set<Class<?>>> hibernateEntities;

	public HBAnnotationHolder() {
		hibernateFactories = new HashSet<HibernateSessionFactory>();
		hibernateEntities = new HashMap<String, Set<Class<?>>>();
	}

	/**
	 *{@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IAnnotationHolder#addIfIsValid(java.lang.String)
	 */
	@Override
	public void addIfIsValid(String resource) {

		if (null == resource || !resource.endsWith(".class")
				|| !resource.startsWith(basePackage)
				|| resource.startsWith("org/hibernate/")) {
			return;
		}
		Class<?> clazz = null;
		try {
			clazz = Class
					.forName(getNameResource(resource), false, classLoader);
		} catch (ClassNotFoundException e) {
			logger.info("La clase >" + resource + "< no ha sido encontrada");
			return;
		} catch (NoClassDefFoundError e) {
			logger.info("La clase >" + resource + "< en el cl '" + classLoader
					+ "'contiene un error de definici�n: "+ e.getMessage());
			return;
		}

		Datasource ds = clazz.getAnnotation(Datasource.class);
		if (ds != null) {
			if (clazz.getAnnotation(Entity.class) == null) {
				logger.warn("La clase >" + clazz
						+ "< no está anotada correctamente");
				return;
			}
			Set<Class<?>> l = hibernateEntities.get(ds.refName());
			if (l == null) {
				l = new HashSet<Class<?>>();
				hibernateEntities.put(ds.refName(), l);
			}
			l.add(clazz);
		}
		HibernateFactory hbf = clazz.getAnnotation(HibernateFactory.class);
		if (null != hbf) {
			if (HibernateSessionFactory.class.isAssignableFrom(clazz)) {
				HibernateSessionFactory fac = null;
				try {
					fac = (HibernateSessionFactory) clazz.newInstance();
					hibernateFactories.add(fac);
				} catch (InstantiationException e) {
					logger.error("Error al instanciar la factoria: "
							+ hbf.name(), e);
				} catch (IllegalAccessException e) {
					logger.error("Error al instanciar la factoria: "
							+ hbf.name(), e);
				}
			} else {
				logger.warn("La clase >" + clazz
						+ "< no está anotada correctamente");
			}
		}
	}

	private String getNameResource(String resource) {
		return resource.replaceAll("/", "\\.").substring(0,
				resource.lastIndexOf('.'));
	}

	/**
	 * @return the hibernateFactories
	 */
	public Set<HibernateSessionFactory> getHibernateFactories() {
		return hibernateFactories;
	}

	/**
	 * @return the hibernateEntities
	 */
	public Map<String, Set<Class<?>>> getHibernateEntities() {
		return hibernateEntities;
	}

	/**
	 * @param classLoader
	 *            the classLoader to set
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * @param basePackage
	 *            the basePackage to set
	 */
	public void setBasePackage(String basePackage) {
		if (basePackage != null) {
			this.basePackage = basePackage.replaceAll("\\.", "/");

		}
	}
}
