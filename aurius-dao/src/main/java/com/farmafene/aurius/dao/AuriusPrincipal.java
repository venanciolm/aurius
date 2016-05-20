/*
 * Copyright (c) 2009-2012 farmafene.com
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import com.farmafene.aurius.server.IAuriusUser;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.WrapperClassContainer;

/**
 * @author vlopez@farmafene.com
 * 
 */
@SuppressWarnings("serial")
class AuriusPrincipal implements IAuriusUser {

	private String name;
	private Set<String> roles;
	private String userName;
	private Locale locale;
	private IWrapperClass wrapperClass;

	public AuriusPrincipal() {
		this.roles = new HashSet<String>();
		wrapperClass = new WrapperClassContainer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("={");
		builder.append("userName=");
		builder.append(userName);
		builder.append(", locale=");
		builder.append(locale);
		builder.append(", name=");
		builder.append(name);
		builder.append(", witdh");
		builder.append(roles.size());
		builder.append(" roles");
		builder.append("}");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> item) {
		return wrapperClass.isWrapperFor(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> item) {
		return wrapperClass.unwrap(item);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(String role) {
		return this.roles.contains(role);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#getNombre()
	 */
	@Override
	public String getUserName() {
		return this.userName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @return el roles
	 */
	public Set<String> getRoles() {
		return roles;
	}

	/**
	 * @param name
	 *            el name a establecer
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param userName
	 *            el userName a establecer
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param locale
	 *            el locale a establecer
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#isUserInRoles(java.util.Set)
	 */
	@Override
	public boolean isUserInRoles(Set<String> roles) {
		boolean isUserInRoles = false;
		int l1 = this.roles.size();
		int l2 = roles == null ? 0 : roles.size();
		if (0 == l2) {
			isUserInRoles = true;
		} else {
			Set<String> setl = this.roles;
			Set<String> setc = roles;
			if (l1 < l2) {
				setl = roles;
				setc = this.roles;
			}
			Iterator<String> i = setc.iterator();
			while (i.hasNext()) {
				if (setl.contains(i.next())) {
					isUserInRoles = true;
					break;
				}
			}
		}
		return isUserInRoles;
	}
}
