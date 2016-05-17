/*
 * Copyright (c) 2009-2013 farmafene.com
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
package com.farmafene.aurius.auth.impl;

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
class AuriusPrincipalDummy implements IAuriusUser {

	private String userName;
	private IWrapperClass wrapperClass;

	AuriusPrincipalDummy() {
		wrapperClass = new WrapperClassContainer(this);
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("={");
		builder.append("userName=");
		builder.append(userName);
		builder.append("}");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#isUserInRoles(java.util.Set)
	 */
	@Override
	public boolean isUserInRoles(Set<String> roles) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#isUserInRole(java.lang.String
	 *      )
	 */
	@Override
	public boolean isUserInRole(String role) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#getName()
	 */
	@Override
	public String getName() {
		return getUserName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.IAuriusPrincipal#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
