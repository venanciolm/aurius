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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.farmafene.aurius.mngt.jdbc.JdbcBatchStatus;
import com.farmafene.aurius.mngt.jdbc.JdbcConnectionStatus;
import com.farmafene.aurius.mngt.jdbc.JdbcExecuteStatus;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@SuppressWarnings("serial")
@XStreamAlias("AuriusOperationStatus")
@XStreamConverter(AuriusOperacionStatusConverter.class)
@XStreamInclude(value = { AuriusResourceStatus.class,
		JdbcConnectionStatus.class, JdbcBatchStatus.class,
		JdbcExecuteStatus.class })
public class AuriusOperacionStatus implements Serializable, IBasicStat {

	private long initTicks;
	private long nowTicks;
	private long endTicks;
	private long initTime;
	private long endTime;
	private long nowTime;
	private boolean stop;
	private transient boolean local;
	private String error;

	public AuriusOperacionStatus() {
		initTime = 0;
		stop = false;
		local = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#start()
	 */
	@Override
	public void start() {
		this.initTicks = System.nanoTime();
		this.initTime = System.currentTimeMillis();
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
		stop = true;
		this.endTicks = System.nanoTime();
		this.endTime = System.currentTimeMillis();
		setError(th);
	}

	/**
	 * Serializamos y ponemos a un valor el final
	 * 
	 * @param s
	 * @throws java.io.IOException
	 */
	private void writeObject(java.io.ObjectOutputStream s)
			throws java.io.IOException {
		if (!isStop()) {
			nowTicks = System.nanoTime();
			nowTime = System.currentTimeMillis();
		}
		s.defaultWriteObject();
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
		sb.append("start=").append(
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
						.format(new Date(initTime)));
		sb.append(", duracion=").append(getDuracion()).append(" ms");
		sb.append(", duracion=").append(getDuracion()).append(" ns");
		sb.append(", stop=").append(isStop());
		sb.append(", error=").append(error != null);
		sb.append(", local=").append(isLocal());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getDuracion()
	 */
	@Override
	public long getDuracion() {
		long end = initTicks;
		if (isStop()) {
			end = endTicks;
		} else if (isLocal()) {
			end = System.nanoTime();
		} else {
			end = nowTicks;
		}
		return end - initTicks;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getInitTime()
	 */
	@Override
	public long getInitTime() {
		return initTime;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getMiliSeconds()
	 */
	@Override
	public long getMiliSeconds() {
		long end = initTime;
		if (isStop()) {
			end = endTime;
		} else if (isLocal()) {
			end = System.currentTimeMillis();
		} else {
			end = nowTime;
		}
		return end - initTime;
	}

	public void setError(Throwable error) {
		String setError = null;
		if (null != error) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter pf = new PrintWriter(baos);
			error.printStackTrace(pf);
			pf.flush();
			try {
				baos.flush();
			} catch (IOException e) {
			}
			pf.close();
			try {
				baos.close();
			} catch (IOException e) {
			}
			setError = baos.toString();
		}
		this.error = setError;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#getError()
	 */
	@Override
	public String getError() {
		return error;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#isStop()
	 */
	@Override
	public boolean isStop() {
		return stop;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.IBasicStat#isLocal()
	 */
	@Override
	public boolean isLocal() {
		return local;
	}

	/**
	 * @param initTicks
	 *            the initTicks to set
	 */
	void setInitTicks(long initTicks) {
		this.initTicks = initTicks;
	}

	/**
	 * @param endTicks
	 *            the endTicks to set
	 */
	void setEndTicks(long endTicks) {
		this.endTicks = endTicks;
	}

	/**
	 * @param initTime
	 *            the initTime to set
	 */
	void setInitTime(long initTime) {
		this.initTime = initTime;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	void setError(String error) {
		this.error = error;
	}

	/**
	 * @param stop
	 *            the stop to set
	 */
	void setStop(boolean stop) {
		this.stop = stop;
	}

	/**
	 * @param local
	 *            the local to set
	 */
	void setLocal(boolean local) {
		this.local = local;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
