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
package com.farmafene.aurius.mngt.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.farmafene.aurius.mngt.CallStatus;
import com.farmafene.aurius.server.Configuracion;

public class DirectoryWorker implements Work {

	private static final Log logger = LogFactory.getLog(DirectoryWorker.class);
	private BlockingQueue<CallStatus> queue;
	private WorkManager workManager;

	private DateFormat df;
	private DateFormat df2;
	private DateFormat df3;

	public DirectoryWorker() {
		this.df = new SimpleDateFormat("yyyyMMdd");
		this.df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		this.df3 = new SimpleDateFormat("HH'.'mm");
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<init>");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName())
				.append("={");
		sb.append("thread=").append(Thread.currentThread().getName());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (logger.isInfoEnabled()) {
			logger.info(this + "<running>");
		}
		for (;;) {
			CallStatus status = null;
			if (logger.isDebugEnabled()) {
				logger.debug(this + "<waiting>");
			}
			try {
				status = queue.take();
			} catch (InterruptedException e) {
				logger.info(this + ": Interrumpido", e);
				break;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Procesando: " + status);
			}
			Date exDate = new Date(status.getInitTime());
			String fileName = Configuracion.getConfigPath();
			File f = new File(fileName);
			f = f.getParentFile();
			f = new File(f, "logs");
			File dirBase = new File(f, df.format(exDate));
			if (logger.isDebugEnabled()) {
				logger.debug("Log Base: " + f.getPath());
			}
			File logFile = null;
			if (!dirBase.exists()) {
				dirBase.mkdirs();
			}
			try {
				String dirNop = String.format(String.format("%1$08x",
						status.hashCode()));
				logFile = new File(dirBase, "tx");
				logFile = new File(logFile, dirNop.substring(0, 2));
				logFile = new File(logFile, dirNop.substring(2, 4));
				logFile = new File(logFile, dirNop.substring(4, 6));
				// logFile = new File(logFile, dirNop.substring(6));
				if (!logFile.exists()) {
					logFile.mkdirs();
				}
				String fff = status.getContexto().getId().toString();
				fff = fff + df2.format(exDate) + ".xml";

				logFile = new File(logFile, fff);
				FileOutputStream appendedFile = new FileOutputStream(
						new File(dirBase, "listado."
								+ df3.format(exDate).substring(0, 4) + "0.html"),
						true);
				FileWorker work = new FileWorker();
				work.setFile(logFile);
				work.setStatus(status);
				if (logger.isDebugEnabled()) {
					logger.debug("Enviando Status:" + work);
				}
				workManager.scheduleWork(work);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintWriter pf = new PrintWriter(baos);
				pf.print("<a href=\"");
				pf.print(makeRelative(logFile, dirBase.getPath()));
				pf.print("\">");
				pf.print(logFile.getName());
				pf.print("</a><br/>");
				pf.flush();
				baos.flush();
				appendedFile.write(baos.toString().getBytes());
				appendedFile.flush();
				appendedFile.close();
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (WorkException e) {
				logger.error(e);
			}

		}

	}

	private String makeRelative(File logFile, String pathBase) {
		String makeRelative = null;
		try {
			String strUrlI = new File(pathBase).toURI().toURL().toString();
			String strUrl = logFile.toURI().toURL().toString();
			makeRelative = strUrl.substring(strUrlI.length());

		} catch (MalformedURLException e) {
			makeRelative = logFile.getPath();
		}
		return makeRelative;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		logger.info(this + "<release>");
		// Do nothing
	}

	/**
	 * @return the queue
	 */
	public BlockingQueue<CallStatus> getQueue() {
		return queue;
	}

	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(BlockingQueue<CallStatus> queue) {
		this.queue = queue;
	}

	/**
	 * @return the workManager
	 */
	public WorkManager getWorkManager() {
		return workManager;
	}

	/**
	 * @param workManager
	 *            the workManager to set
	 */
	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

}
