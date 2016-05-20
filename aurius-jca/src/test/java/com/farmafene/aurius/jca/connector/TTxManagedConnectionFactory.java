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

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;

@SuppressWarnings("serial")
public class TTxManagedConnectionFactory extends TxManagedConnectionFactory {

	public TTxManagedConnectionFactory() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnectionFactory#
	 *      createManagedConnection(javax.security.auth.Subject,
	 *      javax.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection createManagedConnection(Subject subject,
			ConnectionRequestInfo cri) throws ResourceException {
		TTxManagedConnection mc = new TTxManagedConnection();
		mc.setQueueConnectionFactory(getQueueConnectionFactory());
		try {
			mc.setTxConnectionRequestInfo(((TxConnectionRequestInfo) cri)
					.clone());
		} catch (CloneNotSupportedException e) {
			throw new NotSupportedException(e);
		}
		return mc;
	}

}
