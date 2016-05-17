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
package com.farmafene.aurius.mngt.impl;

import javax.resource.spi.work.WorkManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.BasicRegistroFactory;
import com.farmafene.aurius.mngt.CallStatus;
import com.thoughtworks.xstream.XStream;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class TxFinalizadasListenerLog implements ITxFinalizadaListener {

	private static final String CR = System.getProperty("line.separator");
	private static final Logger logger = LoggerFactory
			.getLogger(TxFinalizadasListenerLog.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.ITxFinalizadaListener#setWorkManager(javax.resource.spi.work.WorkManager)
	 */
	@Override
	public void setWorkManager(WorkManager wm) {
		if (logger.isDebugEnabled()) {
			logger.debug("setWorkManager(" + wm + ")");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.impl.ITxFinalizadaListener#fire(com.farmafene.aurius.mngt.CallStatus)
	 */
	@Override
	public void fire(CallStatus source) {
		if (logger.isInfoEnabled()) {
			long init = System.currentTimeMillis();
			XStream xs = new XStream();
			xs.setMode(XStream.NO_REFERENCES);
			xs.processAnnotations(new Class[] {
					CallStatus.class,
					BasicRegistroFactory.getIBasicRegistroFactory()
							.getSupports() });
			logger.info(CR + xs.toXML(source));
			logger.info("done! (" + (System.currentTimeMillis() - init)
					+ " ms)");
		}
	}
}
