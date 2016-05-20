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
package com.farmafene.aurius.dao;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.IHibernateSessionFactory;
import com.farmafene.commons.ioc.BeanFactory;

/**
 * @author vlopez@farmafene.com
 * @since 1.0.0
 */
public class DiccionarioSessionFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(DiccionarioSessionFactory.class);

	/**
	 * Constructor por defecto
	 */
	private DiccionarioSessionFactory() {
		logger.debug(this + "<init>");
	}

	public static IHibernateSessionFactory getIHibernateSessionFactory() {
		IHibernateSessionFactory factory =  BeanFactory
				.getBean(IHibernateSessionFactory.class);
		if (factory == null) {
			throw new IllegalStateException("La factoria "
					+ IHibernateSessionFactory.class
					+ ", debe estar establecida");
		}
		return factory;
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

	public static SessionFactory getSessionFactory()
			throws IllegalArgumentException {
		return getIHibernateSessionFactory().getSessionFactory(
				AuriusSessionFactory.AURIUS_DB);
	}
}
