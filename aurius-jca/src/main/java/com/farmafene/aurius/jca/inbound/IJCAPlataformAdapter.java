/*
 * Copyright (c) 2009-2012 farmafene.com
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
package com.farmafene.aurius.jca.inbound;

import java.util.concurrent.BlockingDeque;

import javax.jms.ConnectionFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import com.farmafene.aurius.jca.AuriusListener;

/**
 * 
 * @author vlopez@farmafene.com
 * 
 */
public interface IJCAPlataformAdapter {

	public void start();

	public void stop();

	public XATerminator getXATerminator();

	public WorkManager getWorkManager();

	public BlockingDeque<AuriusCallToken> getOutQueue();
	/**
	 * @param xAResource
	 * @return
	 */
	public AuriusListener getAuriusListener(XAResource xAResource)
			throws UnavailableException;

	public ConnectionFactory getConnectionFactory();

	public String getUser();

	public String getPassword();

	public String getManagedConnectionFactoryClass();

	public String getBroker();

	public String getTopic();

	public String getQueue();

	public String getClusterQueue();

	public long getTimeToLive();

	public long getTimeOut();
}
