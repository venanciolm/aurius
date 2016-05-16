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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.farmafene.aurius.core.GestorMonitorizacion;
import com.farmafene.aurius.mngt.OperacionEvent.ResourceNotificationType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@SuppressWarnings("serial")
@XStreamAlias("AuriusOperationContainerStatus")
@XStreamInclude(value = { AuriusCoreOperacionStatus.class, CallStatus.class,
		ServiceStatus.class, DiccionarioStatus.class, CommitStatus.class,
		RollbackStatus.class, ForgetStatus.class, PrepareStatus.class,
		MarshallStatus.class, UnmarshallStatus.class,
		AuriusOperacionStatus.class })
public class AuriusOperationContainerStatus implements Serializable,
		IBasicStat, IAuriusCoreStat {

	@XStreamAlias("CoreStats")
	private AuriusCoreOperacionStatusMeter stat;
	@XStreamImplicit
	private List<IBasicStat> status;

	public AuriusOperationContainerStatus() {
		this.stat = new AuriusCoreOperacionStatusMeter();
		this.status = new ArrayList<IBasicStat>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append(stat);
		sb.append(", CpuTime=").append(getCpuTime()).append(" ns");
		sb.append(", ContentionTime=").append(getContentionTime())
				.append(" ms");
		sb.append(", WaitedTime=").append(getWaitedTime()).append(" ms");
		sb.append(", BlockedTime=").append(getBlockedTime()).append(" ms");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the status
	 */
	public List<IBasicStat> getOperacionStatus() {
		return status;
	}

	/**
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#start()
	 */
	public void start() {
		stat.start();
		GestorMonitorizacion.fire(new OperacionEvent(
				ResourceNotificationType.BEGIN, this));
	}

	/**
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#stop()
	 */
	public void stop() {
		stop(null);
	}

	/**
	 * @param th
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#stop(java.lang.Throwable)
	 */
	public void stop(Throwable th) {
		stat.stop(th);
		GestorMonitorizacion.fire(new OperacionEvent(
				ResourceNotificationType.END, this));
	}

	/**
	 * @param error
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#setError(java.lang.Throwable)
	 */
	public void setError(Throwable error) {
		stat.setError(error);
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getDuracion()
	 */
	public long getDuracion() {
		return stat.getDuracion();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getError()
	 */
	public String getError() {
		return stat.getError();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getInitTime()
	 */
	public long getInitTime() {
		return stat.getInitTime();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#isLocal()
	 */
	public boolean isLocal() {
		return stat.isLocal();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#isStop()
	 */
	public boolean isStop() {
		return stat.isStop();
	}

	/**
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#pause()
	 */
	public void pause() {
		stat.pause();
	}

	/**
	 * @param th
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#resume(long)
	 */
	public void resume(long th) {
		stat.resume(th);
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getThreadId()
	 */
	public Long getThreadId() {
		return stat.getThreadId();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getBlockedTime()
	 */
	public long getBlockedTime() {
		return stat.getBlockedTime();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getWaitedTime()
	 */
	public long getWaitedTime() {
		return stat.getWaitedTime();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getCpuTime()
	 */
	public long getCpuTime() {
		return stat.getCpuTime();
	}

	/**
	 * @return
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#getContentionTime()
	 */
	public long getContentionTime() {
		return stat.getContentionTime();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getMiliSeconds()
	 */
	@Override
	public long getMiliSeconds() {
		return stat.getMiliSeconds();
	}
}
