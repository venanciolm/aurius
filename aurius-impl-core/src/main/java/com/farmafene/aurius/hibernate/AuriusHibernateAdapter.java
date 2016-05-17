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

import com.farmafene.commons.hibernate.IHibernateAdapter;

public class AuriusHibernateAdapter implements IHibernateAdapter {
	private static final String LOOKUP_CLASS = "com.farmafene.aurius.hibernate.AuriusTransactionManagerLookup";
	private String lookupClass;
	private String jndiTemplate;

	public AuriusHibernateAdapter() {
		this.lookupClass = LOOKUP_CLASS;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateAdapter#
	 *      getTransactionManagerLookupClass()
	 */
	@Override
	public String getTransactionManagerLookupClass() {
		return lookupClass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.IHibernateAdapter#getJNDITemplateClass()
	 */
	@Override
	public String getJNDITemplateClass() {
		return jndiTemplate;
	}

	/**
	 * @param lookupClass
	 *            the lookupClass to set
	 */
	public void setLookupClass(String lookupClass) {
		this.lookupClass = lookupClass;
	}

	/**
	 * @param jndiTemplate
	 *            the jndiTemplate to set
	 */
	public void setJndiTemplate(String jndiTemplate) {
		this.jndiTemplate = jndiTemplate;
	}
}
