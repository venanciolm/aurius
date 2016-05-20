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
package com.farmafene.aurius.jca.connector;

import java.io.Serializable;

import javax.jms.Destination;
import javax.resource.spi.ConnectionRequestInfo;

@SuppressWarnings("serial")
public class TxConnectionRequestInfo implements ConnectionRequestInfo,
		Serializable, Cloneable {

	private long timeOut;
	private long timeToLive;
	private String user;
	private String password;

	private String queue;
	private Destination destination;
	private Destination localDestination;
	private AuriusXid xid;
	private int transactionTimeout;

	public TxConnectionRequestInfo() {
		this.transactionTimeout = 120;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected TxConnectionRequestInfo clone() throws CloneNotSupportedException {
		return (TxConnectionRequestInfo) super.clone();
	}

	public void cleanup() {
		this.xid = null;
		this.localDestination = null;
		this.transactionTimeout = 120; /* Seconds */
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
		sb.append("Queue=").append(getQueue());
		if (null != getXid()) {
			sb.append(", XID=").append(getXid());
			sb.append(", transactionTimeOut=").append(getTransactionTimeout());
		}
		sb.append(", timeToLive=").append(getTimeToLive());
		sb.append(", timeOut=").append(getTimeOut());
		if (user != null) {
			sb.append(", user=").append(user);
			sb.append(", password=").append("<????????>");
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = 7;
		hashCode = hashCode * 31 + ((queue == null) ? 0 : queue.hashCode());
		hashCode = hashCode * 31 + ((user == null) ? 0 : user.hashCode());
		hashCode = hashCode * 31
				+ ((password == null) ? 0 : password.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof TxConnection) {
			equals = this.hashCode() == obj.hashCode();
		}
		return equals;
	}

	/**
	 * @return the queue
	 */
	public Destination getDestination() {
		if (getXid() != null && localDestination != null) {
			return localDestination;
		} else {
			return destination;
		}
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the timeOut
	 */
	public long getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 *            the timeOut to set
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return the timeToLive
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * @param timeToLive
	 *            the timeToLive to set
	 */
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	/**
	 * @return the xid
	 */
	public AuriusXid getXid() {
		return xid;
	}

	/**
	 * @param xid
	 *            the xid to set
	 */
	public void setXid(AuriusXid xid) {
		this.xid = xid;
	}

	public void setTransactionTimeout(int transactionTimeout) {
		this.transactionTimeout = transactionTimeout;

	}

	public int getTransactionTimeout() {
		return this.transactionTimeout;
	}

	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * @return the localDestination
	 */
	public Destination getLocalDestination() {
		return localDestination;
	}

	/**
	 * @param localDestination
	 *            the localDestination to set
	 */
	public void setLocalDestination(Destination localDestination) {
		this.localDestination = localDestination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

}
