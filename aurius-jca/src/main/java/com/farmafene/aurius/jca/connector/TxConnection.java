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
import java.util.UUID;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.jca.AuriusConnection;

@SuppressWarnings("serial")
public class TxConnection implements AuriusConnection {

	private UUID uUID;
	private boolean closed;
	private TxManagedConnection txManagedConnection;

	TxConnection() {
		this.uUID = UUID.randomUUID();
		this.closed = false;
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
		sb.append("UUID=").append(uUID);
		sb.append(", closed=").append(closed);
		sb.append(", txManagedConnection=").append(txManagedConnection);
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
		hashCode = hashCode * 31 + ((uUID == null) ? 0 : uUID.hashCode());
		hashCode = hashCode
				* 31
				+ ((txManagedConnection == null) ? 0 : txManagedConnection
						.hashCode());
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
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#close()
	 */
	@Override
	public void close() {
		if (!closed) {
			closed = true;
			txManagedConnection.closed(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId)
			throws IllegalArgumentException, RemoteException {
		if (closed) {
			throw new IllegalStateException("La conexión " + this
					+ ", está cerrada.");
		}
		return txManagedConnection.process(auth, servicioId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId, String version)
			throws IllegalArgumentException, RemoteException {
		if (closed) {
			throw new IllegalStateException("La conexión " + this
					+ ", está cerrada.");
		}
		return txManagedConnection.process(auth, servicioId, version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String, com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId, String version,
			Registro registro) throws IllegalArgumentException, RemoteException {
		if (closed) {
			throw new IllegalStateException("La conexión " + this
					+ ", está cerrada.");
		}
		return txManagedConnection.process(auth, servicioId, version, registro);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro process(AuthInfo auth, String servicioId, Registro registro)
			throws IllegalArgumentException, RemoteException {
		if (closed) {
			throw new IllegalStateException("La conexión " + this
					+ ", está cerrada.");
		}
		return txManagedConnection.process(auth, servicioId, registro);
	}

	void setTxManagedConnection(TxManagedConnection txManagedConnection) {
		this.txManagedConnection = txManagedConnection;
	}

	/**
	 * @return the txManagedConnection
	 */
	TxManagedConnection getTxManagedConnection() {
		return txManagedConnection;
	}
}
