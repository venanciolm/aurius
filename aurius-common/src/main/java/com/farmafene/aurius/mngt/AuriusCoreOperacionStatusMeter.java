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

import com.farmafene.aurius.AuriusExceptionTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@SuppressWarnings("serial")
@XStreamAlias("AuriusCoreOperacionStatus")
@XStreamConverter(AuriusCoreOperacionStatusMeterConverter.class)
public class AuriusCoreOperacionStatusMeter implements Serializable, IBasicStat {
	private long initCpuTime;
	private long initContentionTime;
	private long initWaitedTime;
	private long initBlockedTime;
	private long endCpuTime;
	private long endContentionTime;
	private long endBlockedTime;
	private long nowCpuTime;
	private long nowBlockedTime;
	private long nowContentionTime;
	private long endWaitedTime;
	private long nowWaitedTime;
	private Long threadId;
	private long pauseInitTime;
	private AuriusOperacionStatus status;

	/**
	 * Constructor
	 */
	public AuriusCoreOperacionStatusMeter() {
		status = new AuriusOperacionStatus();
		this.threadId = Long.valueOf(Thread.currentThread().getId());
		this.pauseInitTime = 0; /* ms */
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
		sb.append(status);
		sb.append(", CpuTime=").append(getCpuTime()).append(" ns");
		sb.append(", ContentionTime=").append(getContentionTime())
				.append(" ms");
		sb.append(", WaitedTime=").append(getWaitedTime()).append(" ms");
		sb.append(", BlockedTime=").append(getBlockedTime()).append(" ms");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Serializable
	 * @see writeObject(java.io.ObjectOutputStream s)
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		if (!isStop()) {
			now();
		}
		s.defaultWriteObject();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#start()
	 */
	@Override
	public void start() {
		status.start();
		this.initCpuTime = ThreadManagementFactory.getCpuTime(threadId);
		this.initContentionTime = ThreadManagementFactory
				.getIThreadManagement().getContentionTime(threadId);
		this.initWaitedTime = ThreadManagementFactory.getIThreadManagement()
				.getWaitedTime(threadId);
		this.initBlockedTime = ThreadManagementFactory.getIThreadManagement()
				.getBlockedTime(threadId);
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
		this.endContentionTime = ThreadManagementFactory.getIThreadManagement()
				.getContentionTime(threadId);
		this.endWaitedTime = ThreadManagementFactory.getIThreadManagement()
				.getWaitedTime(threadId);
		this.endBlockedTime = ThreadManagementFactory.getIThreadManagement()
				.getBlockedTime(threadId);
		this.endCpuTime = ThreadManagementFactory.getCpuTime(threadId);
		status.stop(th);
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
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#setError(java.lang.Throwable)
	 */
	public void setError(Throwable error) {
		status.setError(error == null ? null : AuriusExceptionTO
				.getInstance(error));
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
	 * @see com.farmafene.aurius.mngt.IBasicStat#getInitTime()
	 */
	@Override
	public long getInitTime() {
		return status.getInitTime();
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
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#isStop()
	 */
	@Override
	public boolean isStop() {
		return status.isStop();
	}

	// sb.append("<CpuTime value=\"").append(getCpuTime()).append("\" unit=\"ns\"/>");
	// sb.append("<ContentionTime value=\"").append(getContentionTime()).append("\" unit=\"ms\"/>");
	// sb.append("<WaitedTime value=\"").append(getWaitedTime()).append("\" unit=\"ms\"/>");
	// sb.append("<BlockedTime value=\"").append(getBlockedTime()).append("\" unit=\"ms\"/>");

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#pause()
	 */
	public void pause() {
		if (0 == pauseInitTime) {
			now();
			this.pauseInitTime = System.currentTimeMillis();
			this.threadId = null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#resume(long)
	 */
	public void resume(long th) {
		if (0 != this.pauseInitTime) {
			this.initCpuTime = this.initCpuTime - this.nowCpuTime
					+ ThreadManagementFactory.getCpuTime(th);
			this.initContentionTime = this.initContentionTime
					- this.nowContentionTime
					+ ThreadManagementFactory.getIThreadManagement()
							.getContentionTime(th);
			this.initWaitedTime = this.initWaitedTime
					- this.nowWaitedTime
					+ pauseInitTime
					- System.currentTimeMillis()
					+ ThreadManagementFactory.getIThreadManagement()
							.getWaitedTime(th);
			this.initBlockedTime = this.initBlockedTime
					- this.nowBlockedTime
					+ ThreadManagementFactory.getIThreadManagement()
							.getBlockedTime(th);
			this.threadId = Long.valueOf(th);
			this.pauseInitTime = 0;
		}
	}

	private void now() {
		if (0 == pauseInitTime) {
			this.nowContentionTime = ThreadManagementFactory
					.getIThreadManagement().getContentionTime(threadId);
			this.nowBlockedTime = ThreadManagementFactory
					.getIThreadManagement().getBlockedTime(threadId);
			this.nowCpuTime = ThreadManagementFactory.getCpuTime(threadId);
			this.nowWaitedTime = ThreadManagementFactory.getIThreadManagement()
					.getWaitedTime(threadId);
		} else {
			long now = System.currentTimeMillis();
			this.nowWaitedTime = this.nowWaitedTime + now - pauseInitTime;
			this.pauseInitTime = now;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#getThreadId()
	 */
	public Long getThreadId() {
		return threadId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#getBlockedTime()
	 */
	public long getBlockedTime() {
		long end = initBlockedTime;
		if (isStop()) {
			end = endBlockedTime;
		} else if (isLocal() && 0 == pauseInitTime) {
			end = ThreadManagementFactory.getIThreadManagement()
					.getBlockedTime(threadId);
		} else {
			end = nowBlockedTime;
		}
		return end - initBlockedTime;
	}

	protected void setBlockedTime(long time) {
		if (isStop()) {
			endBlockedTime = time;
		} else {
			nowBlockedTime = time;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#getWaitedTime()
	 */
	public long getWaitedTime() {
		long end = initWaitedTime;
		if (isStop()) {
			end = endWaitedTime;
		} else if (isLocal() && 0 == pauseInitTime) {
			end = ThreadManagementFactory.getIThreadManagement().getWaitedTime(
					threadId);
		} else if (isLocal() && 0 != pauseInitTime) {
			long now = System.currentTimeMillis();
			this.nowWaitedTime = this.nowWaitedTime + now - pauseInitTime;
			this.pauseInitTime = now;
			end = nowWaitedTime;
		} else {
			end = nowWaitedTime;
		}
		return end - initWaitedTime;
	}

	protected void setWaitedTime(long time) {
		if (isStop()) {
			endWaitedTime = time;
		} else {
			nowWaitedTime = time;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#getCpuTime()
	 */
	public long getCpuTime() {
		long end = initCpuTime;
		if (isStop()) {
			end = endCpuTime;
		} else if (isLocal() && 0 == pauseInitTime) {
			end = ThreadManagementFactory.getIThreadManagement().getCpuTime(
					threadId);
		} else {
			end = nowCpuTime;
		}
		return end - initCpuTime;
	}

	protected void setCpuTime(long time) {
		if (isStop()) {
			endCpuTime = time;
		} else {
			nowCpuTime = time;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IAuriusCoreStat#getContentionTime()
	 */
	public long getContentionTime() {
		long end = initContentionTime;
		if (isStop()) {
			end = endContentionTime;
		} else if (isLocal() && 0 == pauseInitTime) {
			end = ThreadManagementFactory.getIThreadManagement()
					.getContentionTime(threadId);
		} else {
			end = nowContentionTime;
		}
		return end - initContentionTime;
	}

	protected void setContentionTime(long time) {
		if (isStop()) {
			endContentionTime = time;
		} else {
			nowContentionTime = time;
		}
	}

	/**
	 * @return the status
	 */
	protected AuriusOperacionStatus getStatus() {
		return status;
	}
}
