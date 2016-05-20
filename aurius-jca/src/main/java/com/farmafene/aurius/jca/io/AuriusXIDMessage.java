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
package com.farmafene.aurius.jca.io;

import javax.transaction.xa.Xid;

import com.farmafene.aurius.jca.connector.AuriusXid;

@SuppressWarnings("serial")
public class AuriusXIDMessage extends AuriusMessage {

	private AuriusXid correlationXid;
	private long transactionTimeOut; /* seconds */

	public AuriusXIDMessage() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.io.AuriusMessage#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("correlationXid=").append(getCorrelationXid());
		sb.append(" transactionTimeOut=").append(getTransactionTimeOut());
		sb.append("}");
		return sb.toString();
	}

	public Xid getCorrelationXid() {
		return correlationXid;
	}

	public void setCorrelationXid(AuriusXid correlationXid) {
		this.correlationXid = correlationXid;
	}

	/**
	 * @return the transactionTimeOut in seconds
	 */
	public long getTransactionTimeOut() {
		return transactionTimeOut;
	}

	/**
	 * @param transactionTimeOut
	 *            the transactionTimeOut in seconds to set
	 */
	public void setTransactionTimeOut(long transactionTimeOut) {
		this.transactionTimeOut = transactionTimeOut;
	}
}
