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
package com.farmafene.aurius.hibernate;

import java.util.Set;

import org.hibernate.SessionFactory;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IAplicacionHolderPlugin;
import com.farmafene.commons.hibernate.IHibernateSessionFactory;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class AuriusHBSessionFactory implements IAplicacionHolderPlugin,
		IHibernateSessionFactory {

	private IHibernateSessionFactory factory;
	private IAplicacionHolder holder;

	/**
	 * Constructor por defecto
	 */
	public AuriusHBSessionFactory(IAplicacionHolder holder) {
		this.holder = holder;
	}

	/**
	 *{@inheritDoc}
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
		AuriusHBSessionFactoryFactory f = new AuriusHBSessionFactoryFactory();
		f.setClassLoader(holder.getClassLoader());
		f.buildIHibernateSessionFactory();
		factory = f.getIHibernateSessionFactory();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (null != factory) {
			for (String name : getNames()) {
				factory.getSessionFactory(name).close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateSessionFactory#getNames()
	 */
	@Override
	public Set<String> getNames() {
		return factory.getNames();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateSessionFactory#getSessionFactory(java
	 *      .lang.String)
	 */
	@Override
	public SessionFactory getSessionFactory(String id)
			throws IllegalArgumentException {
		return factory.getSessionFactory(id);
	}

}
