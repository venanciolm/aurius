/* Copyright (c) 2009-2014 farmafene.com
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

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Implementación de la factoría de Hibernate
 * 
 * @author vlopez
 * @version 1.0.0
 */
public abstract class HibernateSessionFactory {

	private SessionFactory sessionFactory;
	private Properties properties;
	private List<Class<?>> classes;
	private AnnotationConfiguration annotationConfiguration;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public HibernateSessionFactory() {
		annotationConfiguration = null;
		properties = new Properties();
		classes = new LinkedList<Class<?>>();

	}

	public void buildSessionFactory() {

		if (null != annotationConfiguration) {
			throw new IllegalStateException(
					"La factoría ya ha sido configurada");
		}
		annotationConfiguration = new AnnotationConfiguration();
		annotationConfiguration.setProperties(properties);
		for (Class<?> clazz : classes) {
			annotationConfiguration.addAnnotatedClass(clazz);
		}
		sessionFactory = annotationConfiguration.buildSessionFactory();
	}

	public String getJNDIName() {
		return this.getClass().getAnnotation(HibernateFactory.class).jndiName();
	}

	public String getName() {
		return this.getClass().getAnnotation(HibernateFactory.class).name();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public SessionFactory getSessionFactory() throws IllegalArgumentException {

		return sessionFactory;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties.putAll(properties);
	}

	/**
	 * @param classes
	 *            the classes to set
	 */
	public void addClasses(Set<Class<?>> classes) {
		this.classes.addAll(classes);
	}
}
