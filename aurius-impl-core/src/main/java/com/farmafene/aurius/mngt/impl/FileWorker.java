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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.resource.spi.work.Work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.farmafene.aurius.BasicRegistroFactory;
import com.farmafene.aurius.mngt.CallStatus;
import com.thoughtworks.xstream.XStream;

public class FileWorker implements Work {

	private static final Log logger = LogFactory.getLog(FileWorker.class);
	private File file;
	private CallStatus status;

	public FileWorker() {
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
		sb.append("file=").append(file);
		sb.append(", thread=").append(Thread.currentThread().getName());
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
		FileOutputStream fos = null;
		try {
			XStream xs = new XStream();
			xs.setMode(XStream.NO_REFERENCES);
			xs.processAnnotations(new Class[] {
					CallStatus.class,
					BasicRegistroFactory.getIBasicRegistroFactory()
							.getSupports() });
			fos = new FileOutputStream(file);
			try {
				fos.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xs
						.toXML(status)).getBytes("UTF-8"));
			} catch (IOException e) {
				logger.error(e);
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
		if (fos != null) {
			try {
				fos.flush();
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				fos.close();
			} catch (IOException e) {
				logger.error(e);
			}
			logger.debug(this + "<end>");
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.work.Work#release()
	 */
	@Override
	public void release() {
		logger.debug(this + "<release>");
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the status
	 */
	public CallStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(CallStatus status) {
		this.status = status;
	}
}
