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

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.jca.inbound.XAHelper;

@SuppressWarnings("serial")
public class TxXAResource implements XAResource, Serializable {

	private static final Logger logger = LoggerFactory
			.getLogger(TxXAResource.class);
	private TxManagedConnection txManagedConnection;

	public TxXAResource(TxManagedConnection txManagedConnection) {
		this.txManagedConnection = txManagedConnection;
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
		sb.append("TxManagedConnection=").append(txManagedConnection);
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
		return (this.txManagedConnection == null) ? 0
				: this.txManagedConnection.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj instanceof TxXAResource) {
			equals = this.hashCode() == obj.hashCode();
		}
		return equals;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid,
	 *      boolean)
	 */
	@Override
	public void commit(Xid xId, boolean onePhase) throws XAException {
		logger.info("commit('" + xId + "','" + onePhase + "')");
		if (onePhase) {
			logger.debug("Performing One Phase Commit");
		} else {
			logger.debug("Performing Two Phase Commit");
		}
		txManagedConnection.commit(xId, onePhase);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void end(Xid xId, int flag) throws XAException {
		logger.info("end('" + xId + "','" + XAHelper.getStringFromFlag(flag)
				+ "')");
		switch (flag) {
		case TMSUCCESS:
			logger.debug("Transaction Ending Successfully");
			break;
		case TMFAIL:
			logger.debug("Transaction Ending Failed");
			break;
		case TMSUSPEND:
			logger.debug("Transaction is suspended");
			break;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
	 */
	@Override
	public void forget(Xid xId) throws XAException {
		logger.info("forget('" + xId + "')");
		txManagedConnection.forget(xId);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#getTransactionTimeout()
	 */
	@Override
	public int getTransactionTimeout() throws XAException {
		logger.info("getTransactionTimeout()");
		return this.txManagedConnection.getTxConnectionRequestInfo()
				.getTransactionTimeout();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
	 */
	@Override
	public boolean isSameRM(XAResource xAResource) throws XAException {
		logger.info("isSameRM('" + xAResource + "')");

		return equals(xAResource);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
	 */
	@Override
	public int prepare(Xid xId) throws XAException {
		logger.info("prepare('" + xId + "')");
		return txManagedConnection.prepare(xId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#recover(int)
	 */
	@Override
	public Xid[] recover(int flag) throws XAException {
		logger.info("recover('" + XAHelper.getStringFromFlag(flag) + "')");
		return txManagedConnection.recover(flag);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
	 */
	@Override
	public void rollback(Xid xId) throws XAException {
		logger.info("rollback('" + xId + "')");
		txManagedConnection.rollback(xId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
	 */
	@Override
	public boolean setTransactionTimeout(int transactionTimeout)
			throws XAException {
		logger.info("setTransactionTimeout('" + transactionTimeout + "')");
		this.txManagedConnection.getTxConnectionRequestInfo()
				.setTransactionTimeout(transactionTimeout);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void start(Xid xId, int flag) throws XAException {
		logger.info("start('" + xId + "','" + flag + "')");
		if (TMRESUME != flag) {
			txManagedConnection.getTxConnectionRequestInfo().setXid(
					new AuriusXid(xId));
			txManagedConnection.started();
		}

	}

	/**
	 * @param txManagedConnection
	 *            the txManagedConnection to set
	 */
	public void setTxManagedConnection(TxManagedConnection txManagedConnection) {
		this.txManagedConnection = txManagedConnection;
	}

}
