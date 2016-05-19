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
package com.farmafene.aurius.support.cas;

import org.apache.http.cookie.Cookie;

/**
 * @author vlopez@farmafene.com
 * 
 */
class LoginTicketContainer {

	private String loginTicket;
	private String execution;
	private Cookie sessionCookie;

	public LoginTicketContainer() {

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
		sb.append("loginTicket=").append(loginTicket);
		sb.append(", execution=").append(execution);
		sb.append(", sessionCookie=").append(sessionCookie);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return el loginTicket
	 */
	public String getLoginTicket() {
		return loginTicket;
	}

	/**
	 * @param loginTicket
	 *            el loginTicket a establecer
	 */
	public void setLoginTicket(String loginTicket) {
		this.loginTicket = loginTicket;
	}

	/**
	 * @return el execution
	 */
	public String getExecution() {
		return execution;
	}

	/**
	 * @param execution
	 *            el execution a establecer
	 */
	public void setExecution(String execution) {
		this.execution = execution;
	}

	/**
	 * @return el sessionCookie
	 */
	public Cookie getSessionCookie() {
		return sessionCookie;
	}

	/**
	 * @param sessionCookie el sessionCookie a establecer
	 */
	public void setSessionCookie(Cookie sessionCookie) {
		this.sessionCookie = sessionCookie;
	}
}
