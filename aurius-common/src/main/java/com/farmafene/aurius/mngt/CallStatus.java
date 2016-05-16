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
package com.farmafene.aurius.mngt;

import java.util.Stack;

import javax.transaction.xa.Xid;

import com.farmafene.aurius.core.ContextoCore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@SuppressWarnings("serial")
@XStreamAlias("CallStatus")
public class CallStatus extends AuriusOperationContainerStatus {
	@XStreamAlias("Xid")
	private Xid xId;
	@XStreamAlias("Connector")
	@XStreamAsAttribute
	private String conector;
	@XStreamAlias("Method")
	@XStreamAsAttribute
	private String method;
	@XStreamAlias("ServiceId")
	@XStreamAsAttribute
	private String serviceId;
	@XStreamAlias("Version")
	@XStreamAsAttribute
	private String version;
	@XStreamImplicit
	private Stack<IBasicStat> pila;
	@XStreamOmitField
	private ContextoCore contexto;

	public CallStatus() {
		this.pila = new Stack<IBasicStat>();
		pila.push(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatus#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("conector=").append(conector);
		sb.append(", contexto=").append(contexto).append(", ");
		sb.append(", xid=").append(xId).append(", ");
		sb.append(super.toString());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Obtiene el status actual
	 * 
	 * @return status actual
	 */
	public IBasicStat getCurrent() {
		IBasicStat current = null;
		if (!pila.isEmpty()) {
			current = pila.peek();
		}
		return current;
	}

	/**
	 * Obtiene la pila de llamadas
	 * 
	 * @return pila de llamadas
	 */
	public Stack<IBasicStat> getPila() {
		return pila;
	}

	/**
	 * @param contexto
	 *            the contexto to set
	 */
	public void setContexto(ContextoCore contexto) {
		this.contexto = contexto;
	}

	/**
	 * @return the contexto
	 */
	public ContextoCore getContexto() {
		return contexto;
	}

	public void setMsg(String stat, String serviceId, String version) {
		this.method = stat;
		this.serviceId = serviceId;
		this.version = version;
	}

	/**
	 * @param conector
	 *            el conector a establecer
	 */
	public void setConector(String conector) {
		this.conector = conector;
	}

	/**
	 * @return the xId
	 */
	public Xid getXid() {
		return xId;
	}

	/**
	 * @param xId
	 *            the xId to set
	 */
	public void setXid(Xid xId) {
		this.xId = xId;
	}
}
