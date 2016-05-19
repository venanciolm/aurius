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
/**
 * 
 */
package com.farmafene.aurius.mngt.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import com.farmafene.aurius.mngt.IThreadManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadManagement implements IThreadManagement {

	private static final Logger logger = LoggerFactory
			.getLogger(ThreadManagement.class);
	private ThreadMXBean threadMXBean;
	public boolean cpuEnabled = false;
	public boolean contentionEnabled = false;

	/**
	 * 
	 */
	public ThreadManagement() {
		init();
		logger.info(this.toString());
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
		sb.append("cpuEnabled=").append(cpuEnabled);
		sb.append(", contentionEnabled=").append(contentionEnabled);
		sb.append("}");
		return sb.toString();
	}

	public void init() {
		this.threadMXBean = ManagementFactory.getThreadMXBean();
		if (!threadMXBean.isThreadCpuTimeSupported()) {
			cpuEnabled = false;
			logger
					.error("ThreadMXBean.ThreadCpuTimeSupported is not supported.");
		} else if (!threadMXBean.isThreadCpuTimeEnabled()) {
			threadMXBean.setThreadCpuTimeEnabled(true);
			cpuEnabled = true;
		} else {
			cpuEnabled = true;
		}
		if (!threadMXBean.isThreadContentionMonitoringSupported()) {
			contentionEnabled = false;
			logger
					.error("ThreadMXBean.ThreadContentionMonitoringSupported is not supported.");
		} else if (!threadMXBean.isThreadContentionMonitoringEnabled()) {
			threadMXBean.setThreadContentionMonitoringEnabled(true);
			contentionEnabled = true;
		} else {
			contentionEnabled = true;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.resources.IThreadManagement#getCpuTime(long)
	 */
	public long getCpuTime(long threadId) {
		long time = 0;
		if (cpuEnabled && threadId > 0) {
			time = threadMXBean.getThreadCpuTime(threadId);
		}
		return time;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.resources.IThreadManagement
	 *      getContentionTime(long)
	 */
	public long getContentionTime(long threadId) {
		return getBlockedTime(threadId) + getWaitedTime(threadId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.resources.IThreadManagement#
	 *      getBlockedTime(long)
	 */
	public long getBlockedTime(long threadId) {
		long time = 0;
		if (contentionEnabled && threadId > 0) {
			time = threadMXBean.getThreadInfo(threadId).getBlockedTime();
		}
		return time;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.resources.IThreadManagement#
	 *      getWaitedTime(long)
	 */
	public long getWaitedTime(long threadId) {
		long time = 0;
		if (contentionEnabled && threadId > 0) {
			time = threadMXBean.getThreadInfo(threadId).getWaitedTime();
		}
		return time;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.resources.IThreadManagement#
	 *      getStackTrace(long)
	 */
	public StackTraceElement[] getStackTrace(long threadId) {

		StackTraceElement[] st = threadMXBean.getThreadInfo(threadId)
				.getStackTrace();
		if (null == st) {
			st = new StackTraceElement[0];
		}
		return st;
	}
}