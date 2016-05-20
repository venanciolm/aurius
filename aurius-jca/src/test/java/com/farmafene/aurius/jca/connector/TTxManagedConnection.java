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

import java.rmi.RemoteException;

import javax.resource.spi.ManagedConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.jca.inbound.XAHelper;

public class TTxManagedConnection extends TxManagedConnection implements
		ManagedConnection {
	private static final Logger logger = LoggerFactory
			.getLogger(TTxManagedConnection.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#process(com.farmafene
	 *      .aurius.AuthInfo, java.lang.String, com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId,
			Registro registro) throws IllegalArgumentException, RemoteException {
		logger.debug("process(" + auth + ", " + servicioId + ", " + registro
				+ ")");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#process(com.farmafene
	 *      .aurius.AuthInfo, java.lang.String, java.lang.String,
	 *      com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId,
			String version, Registro registro) throws IllegalArgumentException,
			RemoteException {
		logger.debug("process(" + auth + ", " + servicioId + ", " + version
				+ ", " + registro + ")");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#process(com.farmafene
	 *      .aurius.AuthInfo, java.lang.String, java.lang.String)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId, String version)
			throws IllegalArgumentException, RemoteException {
		logger.debug("process(" + auth + ", " + servicioId + ", " + version
				+ ")");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#process(com.farmafene
	 *      .aurius.AuthInfo, java.lang.String)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId)
			throws IllegalArgumentException, RemoteException {
		logger.debug("process(" + auth + ", " + servicioId + ")");
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#commit(javax.
	 *      transaction.xa.Xid, boolean)
	 */
	@Override
	void commit(Xid id, boolean onePhase) throws XAException {
		logger.debug("commit(" + id + ", " + onePhase + ")");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#forget(javax.
	 *      transaction.xa.Xid)
	 */
	@Override
	void forget(Xid id) throws XAException {
		logger.debug("forget(" + id + ")");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#prepare(javax.
	 *      transaction.xa.Xid)
	 */
	@Override
	int prepare(Xid id) throws XAException {
		logger.debug("prepare(" + id + ")");
		return XAResource.XA_OK;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#recover(int)
	 */
	@Override
	Xid[] recover(int flag) throws XAException {
		logger.debug("recover(" + XAHelper.getStringFromFlag(flag) + ")");
		return new Xid[0];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.connector.TxManagedConnection#rollback(javax
	 *      .transaction.xa.Xid)
	 */
	@Override
	void rollback(Xid id) throws XAException {
		logger.debug("rollback(" + id + ")");
	}
}
