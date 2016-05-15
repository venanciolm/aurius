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
package com.farmafene.aurius.mngt.jdbc;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.farmafene.aurius.mngt.AuriusOperacionStatus;
import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResource.Operacion;
import com.farmafene.aurius.mngt.IBasicStat;
import com.farmafene.aurius.mngt.OperacionEvent;
import com.farmafene.aurius.mngt.OperacionEvent.ResourceNotificationType;
import com.farmafene.aurius.mngt.OperacionEventSubject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@SuppressWarnings("serial")
@XStreamAlias("JdbcBatchStatus")
public class JdbcBatchStatus implements IBasicStat, Serializable {

	@XStreamAlias("operation")
	@XStreamAsAttribute
	private Operacion operacion;
	@XStreamAlias("AuriusResource")
	private AuriusResource resource;
	@XStreamImplicit(itemFieldName = "batch")
	private List<String> batch;
	@XStreamAlias("BasicStatus")
	private AuriusOperacionStatus status;

	protected JdbcBatchStatus() {
		this(null, null, new LinkedList<String>());
	}

	public JdbcBatchStatus(AuriusResource resource, Operacion operacion,
			List<String> batch) {
		this.resource = resource;
		this.operacion = operacion;
		this.status = new AuriusOperacionStatus();
		this.batch = batch;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("operacion=").append(operacion);
		sb.append(", resource=").append(resource);
		sb.append(", duracion=").append(getDuracion());
		sb.append(", stop=").append(isStop());
		sb.append(", local=").append(isLocal());
		sb.append(", batch=").append(getBatch());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#start()
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
		this.stop(null);

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

	public List<String> getBatch() {
		return this.batch;
	}

	/**
	 * @return the operacion
	 */
	public Operacion getOperacion() {
		return operacion;
	}

	/**
	 * @return the resource
	 */
	public AuriusResource getResource() {
		return resource;
	}
}
