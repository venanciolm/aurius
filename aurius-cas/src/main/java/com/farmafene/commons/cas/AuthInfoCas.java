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
package com.farmafene.commons.cas;

import org.apache.http.cookie.Cookie;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.WrapperClassContainer;

@SuppressWarnings("serial")
public class AuthInfoCas implements AuthInfo {

	private Cookie cookie;
	private AuthInfo serverAuthInfo;
	private IWrapperClass iface;

	public AuthInfoCas() {
		this.iface = new WrapperClassContainer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("cGC=").append(cookie == null ? "<null>" : cookie);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int hashCode = 1;
		hashCode = prime * hashCode
				+ ((cookie == null) ? 0 : cookie.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AuthInfoCas other = (AuthInfoCas) obj;
		if (cookie == null) {
			if (other.cookie != null) {
				return false;
			}
		} else if (!cookie.getName().equals(other.cookie.getName())) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.AuthInfo#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> iface) {
		return this.iface.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.AuthInfo#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws IllegalArgumentException {
		return this.iface.unwrap(iface);
	}

	/**
	 * @return the cookie
	 */
	public Cookie getCookie() {
		return cookie;
	}

	/**
	 * @param cookie
	 *            the cookie to set
	 */
	void setCookie(Cookie cookie) {
		AuthInfoString info = null;
		if (cookie != null) {
			info = new AuthInfoString();
			info.setValue(cookie.getValue());
		}
		this.serverAuthInfo = info;
		this.cookie = cookie;
	}

	/**
	 * @return the serverAuthInfo
	 */
	public AuthInfo getServerAuthInfo() {
		return serverAuthInfo;
	}
}
