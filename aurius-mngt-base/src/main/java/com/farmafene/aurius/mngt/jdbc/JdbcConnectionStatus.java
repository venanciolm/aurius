/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.mngt.jdbc;

import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResource.Operacion;
import com.farmafene.aurius.mngt.AuriusResourceStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@SuppressWarnings("serial")
@XStreamAlias("JdbcConnectionStatus")
public class JdbcConnectionStatus extends AuriusResourceStatus {

	@XStreamAsAttribute
	private String method;

	protected JdbcConnectionStatus() {
		this(null, null);
	}

	public JdbcConnectionStatus(AuriusResource resource, String method) {
		super(resource, Operacion.OPERATION);
		this.method = method;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("operacion=").append(getOperacion());
		sb.append(", resource=").append(getResource());
		sb.append(", duracion=").append(getDuracion());
		sb.append(", stop=").append(isStop());
		sb.append(", local=").append(isLocal());
		sb.append(", method=").append(method);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @param method
	 *            the method to set
	 */
	protected void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the method
	 */
	protected String getMethod() {
		return method;
	}
}
