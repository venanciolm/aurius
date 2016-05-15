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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.farmafene.aurius.mngt.AuriusOperacionStatus;
import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResource.Operacion;
import com.farmafene.aurius.mngt.IBasicStat;
import com.farmafene.aurius.mngt.OperacionEvent;
import com.farmafene.aurius.mngt.OperacionEvent.ResourceNotificationType;
import com.farmafene.aurius.mngt.OperacionEventSubject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@SuppressWarnings("serial")
@XStreamAlias("JdbcExecuteStatus")
public class JdbcExecuteStatus implements IBasicStat, Serializable {

	@XStreamAlias("operation")
	@XStreamAsAttribute
	private Operacion operacion;
	@XStreamAlias("AuriusResource")
	private AuriusResource resource;
	@XStreamAlias("SQL")
	private String sql;
	@XStreamAlias("params")
	@XStreamConverter(JdbcBatchStatusInnerData.class)
	private JdbcBatchStatusInnerData inner = new JdbcBatchStatusInnerData(null);
	@XStreamAlias("BasicStatus")
	private AuriusOperacionStatus status;

	public JdbcExecuteStatus(AuriusResource resource, Operacion operacion,
			String sql, List<String> params, Map<String, String> nparams) {
		this.resource = resource;
		this.operacion = operacion;
		this.sql = sql;
		this.inner.params = params;
		this.inner.nparams = nparams;
		this.status = new AuriusOperacionStatus();
	}

	public JdbcExecuteStatus(AuriusResource resource, Operacion operation,
			String s) {
		this(resource, operation, s, new LinkedList<String>(),
				new LinkedHashMap<String, String>());
	}

	public JdbcExecuteStatus() {
		this(null, null, null);
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
		sb.append(", sql=").append(getSql());
		if (getParams() != null) {
			sb.append(", params=").append(getParams());
		}
		if (getNParams() != null) {
			sb.append(", nparams=").append(getNParams());
		}
		sb.append("}");
		return sb.toString();
	}

	public String getSql() {
		return this.sql;
	}

	public List<String> getParams() {
		return this.inner.params;
	}

	/**
	 * @return the nparams
	 */
	protected Map<String, String> getNParams() {
		return this.inner.nparams;
	}

	/**
	 * @param nparams
	 *            the nparams to set
	 */
	protected void setNParams(Map<String, String> nparams) {
		this.inner.nparams = nparams;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	protected void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the operacion
	 */
	public Operacion getOperacion() {
		return operacion;
	}

	/**
	 * @param operacion
	 *            the operacion to set
	 */
	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}

	/**
	 * @return the resource
	 */
	public AuriusResource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(AuriusResource resource) {
		this.resource = resource;
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
}
