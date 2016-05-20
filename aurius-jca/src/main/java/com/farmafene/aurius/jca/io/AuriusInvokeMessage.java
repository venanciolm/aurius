/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.jca.io;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;

@SuppressWarnings("serial")
public class AuriusInvokeMessage extends AuriusXIDMessage {

	private AuthInfo cookie;
	private String servicioId;
	private String version;
	private Registro registro;

	public AuriusInvokeMessage() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.io.AuriusXIDMessage#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("ServicioId=").append(getServicioId());
		sb.append(", version=").append(getVersion());
		sb.append(", cookie=").append(getCookie());
		sb.append(", registro=").append(getRegistro());
		if (null != getCorrelationXid()) {
			sb.append(", correlationXid=").append(getCorrelationXid());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the cookie
	 */
	public AuthInfo getCookie() {
		return cookie;
	}

	/**
	 * @param cookie
	 *            the cookie to set
	 */
	public void setCookie(AuthInfo cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the servicioId
	 */
	public String getServicioId() {
		return servicioId;
	}

	/**
	 * @param servicioId
	 *            the servicioId to set
	 */
	public void setServicioId(String servicioId) {
		this.servicioId = servicioId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the registro
	 */
	public Registro getRegistro() {
		return registro;
	}

	/**
	 * @param registro
	 *            the registro to set
	 */
	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

}
