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
package com.farmafene.aurius.mngt;

import java.io.Serializable;

import com.farmafene.aurius.mngt.AuriusResource.Operacion;
import com.farmafene.aurius.mngt.OperacionEvent.ResourceNotificationType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@SuppressWarnings("serial")
@XStreamAlias("AuriusResourceStatus")
@XStreamInclude(AuriusOperacionStatus.class)
public class AuriusResourceStatus implements Serializable, IBasicStat {

	@XStreamAlias("operation")
	@XStreamAsAttribute
	private Operacion operacion;
	@XStreamAlias("AuriusResource")
	private AuriusResource resource;
	@XStreamAlias("BasicStatus")
	private AuriusOperacionStatus status;

	protected AuriusResourceStatus() {
		this(null, null);
	}

	public AuriusResourceStatus(AuriusResource resource, Operacion operacion) {
		super();
		this.resource = resource;
		this.operacion = operacion;
		this.status = new AuriusOperacionStatus();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("operacion=").append(getOperacion());
		sb.append(", resource=").append(getResource());
		sb.append(", ").append(status);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#start()
	 */
	@Override
	public void start() {
		status.start();
		OperacionEventSubject.fire(new OperacionEvent(
				ResourceNotificationType.BEGIN, this));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#stop()
	 */
	@Override
	public void stop() {
		stop(null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#stop(java.lang.Throwable)
	 */
	@Override
	public void stop(Throwable th) {
		status.stop(th);
		OperacionEventSubject.fire(new OperacionEvent(
				ResourceNotificationType.END, this));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getInitTime()
	 */
	@Override
	public long getInitTime() {
		return status.getInitTime();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getDuracion()
	 */
	@Override
	public long getDuracion() {
		return status.getDuracion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getMiliSeconds()
	 */
	@Override
	public long getMiliSeconds() {
		return status.getMiliSeconds();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getError()
	 */
	@Override
	public String getError() {
		return status.getError();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#isStop()
	 */
	@Override
	public boolean isStop() {
		return status.isStop();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#isLocal()
	 */
	@Override
	public boolean isLocal() {
		return status.isLocal();
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	void setResource(AuriusResource resource) {
		this.resource = resource;
	}

	/**
	 * @param operacion
	 *            the operacion to set
	 */
	void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	/**
	 * @return the resource
	 */
	public AuriusResource getResource() {
		return resource;
	}

	/**
	 * @return the operacion
	 */
	public Operacion getOperacion() {
		return operacion;
	}

}
