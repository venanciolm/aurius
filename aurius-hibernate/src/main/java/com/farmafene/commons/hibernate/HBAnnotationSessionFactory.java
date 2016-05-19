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
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;

/**
 * Implementación de la clase generada por inspección de anotaciones
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 * 
 */
public class HBAnnotationSessionFactory implements IHibernateSessionFactory {

	private Map<String, SessionFactory> factories;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public HBAnnotationSessionFactory() {
		factories = new HashMap<String, SessionFactory>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateSessionFactory#getNames()
	 */
	public Set<String> getNames() {
		return factories.keySet();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateSessionFactory#getSessionFactory(java.lang.String)
	 */
	@Override
	public SessionFactory getSessionFactory(String id)
			throws IllegalArgumentException {
		if (null == id) {
			throw new IllegalArgumentException(
					"El identificador no puede ser null");
		}
		SessionFactory sess = factories.get(id);
		if (null == sess) {
			throw new IllegalArgumentException("La SessionFactory para el id='"
					+ id + "', no existe");

		}
		return sess;
	}

	/**
	 * A�ade una nueva sessionFactory al mapa
	 * 
	 * @param hf
	 * @since 1.0.0
	 */
	public void addSessionFactory(HibernateSessionFactory hf) {
		if (null == hf) {
			throw new IllegalArgumentException(
					"La 'HibernateSessionFactory', no puede ser null");
		} else if (null == hf.getName()) {
			throw new IllegalArgumentException(
					"El nombre de la  'HibernateSessionFactory', no puede ser null");
		}
		factories.put(hf.getName(), hf.getSessionFactory());
	}

}
