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
package com.farmafene.aurius.mngt.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 * Implementación de XAResource para no-XA JDBC connection emulando XA
 * <p>
 * El flujo del protocolo XA implementa esta máquina de estados:
 * </p>
 * 
 * <pre>
 * NO_TX
 *   |
 *   | start(TMNOFLAGS)
 *   |
 *   |       end(TMFAIL)
 * STARTED -------------- NO_TX
 *   |
 *   | end(TMSUCCESS)
 *   |
 *   |    start(TMJOIN)
 * ENDED ---------------- STARTED
 *   |\
 *   | \  commit (one phase)
 *   |  ----------------- NO_TX
 *   |
 *   | prepare()
 *   |
 *   |       commit() or
 *   |       rollback()
 * PREPARED ------------- NO_TX
 * </pre>
 */
public class XAResourceImpl implements XAResource {

	public static final int NO_TX = 0;
	public static final int STARTED = 1;
	public static final int ENDED = 2;
	public static final int PREPARED = 3;
	private Connection connection;
	private Xid xid;
	private boolean autocommitActiveBeforeStart;
	private int state = NO_TX;
	private int txTimeout = 0;

	/**
	 * Constructor
	 * 
	 * @param connection
	 *            conexión a controlar
	 */
	public XAResourceImpl(Connection connection) {
		this.connection = connection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#forget(Xid)
	 */
	public void forget(Xid xid) throws XAException {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#recover(int)
	 */
	public Xid[] recover(int flags) throws XAException {
		return new Xid[0];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#isSameRM(XAResource)
	 */
	public boolean isSameRM(XAResource xaResource) throws XAException {
		return xaResource == this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#start(Xid, int)
	 */
	public void start(Xid xid, int flag) throws XAException {
		if (xid == null) {
			XAErrorsException e = new XAErrorsException(
					"XID no puede ser nulo", XAException.XAER_INVAL);
			throw e;
		}

		if (state == NO_TX) {
			if (this.xid != null) {
				XAErrorsException e = new XAErrorsException(
						"Recurso ya iniciado con XID " + this.xid,
						XAException.XAER_PROTO);
				throw e;
			}
			if (flag == XAResource.TMJOIN) {
				XAErrorsException e = new XAErrorsException(
						"Recurso a�n no arrancado", XAException.XAER_PROTO);
				throw e;
			}
			this.xid = xid;
		} else if (state == STARTED) {
			XAErrorsException e = new XAErrorsException(
					"Recurso ya arrancado con XID " + this.xid,
					XAException.XAER_PROTO);
			throw e;
		} else if (state == ENDED) {
			if (flag == XAResource.TMNOFLAGS) {
				XAErrorsException e = new XAErrorsException(
						"Recurso ya registrado con XID " + this.xid,
						XAException.XAER_DUPID);
				throw e;
			}
			if (!xid.equals(this.xid)) {
				XAErrorsException e = new XAErrorsException(
						"Recurso ya arrancado con XID "
								+ this.xid
								+ " - no se pueden arrancar más de un XID al mismo tiempo",
						XAException.XAER_RMERR);
				throw e;
			}
		}
		try {
			autocommitActiveBeforeStart = connection.getAutoCommit();
			if (autocommitActiveBeforeStart) {
				connection.setAutoCommit(false);
			}
			this.state = STARTED;
		} catch (SQLException ex) {
			XAErrorsException e = new XAErrorsException(
					"No se puede deshabilitar autocommit en connection no-XA ",
					XAException.XAER_RMERR);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#end(Xid, int)
	 */
	public void end(Xid xid, int flag) throws XAException {
		if (xid == null) {
			XAErrorsException e = new XAErrorsException(
					"XID no puede ser nulo", XAException.XAER_INVAL);
			throw e;

		}
		if (flag == XAResource.TMFAIL) {
			try {
				connection.rollback();
				state = NO_TX;
				this.xid = null;
				return;
			} catch (SQLException ex) {
				XAErrorsException e = new XAErrorsException(
						"Error en rollback del recurso al terminar",
						XAException.XAER_RMERR, ex);
				throw e;
			}
		}
		this.state = ENDED;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#prepare(Xid)
	 */
	public int prepare(Xid xid) throws XAException {
		if (xid == null) {
			XAErrorsException e = new XAErrorsException(
					"XID no puede ser nulo", XAException.XAER_INVAL);
			throw e;
		}

		this.state = PREPARED;
		return XAResource.XA_OK;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#commit(Xid, boolean)
	 */
	public void commit(Xid xid, boolean onePhase) throws XAException {
		if (xid == null) {
			XAErrorsException e = new XAErrorsException(
					"XID no puede ser nulo", XAException.XAER_INVAL);
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException ex) {
			XAErrorsException e = new XAErrorsException("Error en el commit",
					XAException.XAER_RMERR, ex);
			throw e;
		}

		this.state = NO_TX;
		this.xid = null;

		try {
			if (autocommitActiveBeforeStart) {
				connection.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			XAErrorsException e = new XAErrorsException(
					"no se puede establecer autocommit en la connection no-XA",
					XAException.XAER_RMERR);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#rollback(Xid)
	 */
	public void rollback(Xid xid) throws XAException {
		if (xid == null) {
			XAErrorsException e = new XAErrorsException(
					"XID no puede ser nulo", XAException.XAER_INVAL);
			throw e;
		}

		try {
			connection.rollback();
			this.state = NO_TX;
			this.xid = null;
		} catch (SQLException ex) {
			XAErrorsException e = new XAErrorsException(
					"Error en el prepare del recurso  no-XA",
					XAException.XAER_RMERR, ex);
			throw e;
		}

		try {
			if (autocommitActiveBeforeStart) {
				connection.setAutoCommit(true);
			}
		} catch (SQLException ex) {
			XAErrorsException e = new XAErrorsException(
					"no se puede establecer autocommit en la connection no-XA",
					XAException.XAER_RMERR);
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("state=").append(pintaEstado());
		sb.append(", txTimeout=").append(txTimeout);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Obtiene el descriptivo de un flag de XAResource
	 * 
	 * @param flag
	 *            el flag
	 * @return descriptivo del flag
	 */
	protected String decodeXAResourceFlag(int flag) {
		switch (flag) {
		case XAResource.TMENDRSCAN:
			return "ENDRSCAN";
		case XAResource.TMFAIL:
			return "FAIL";
		case XAResource.TMJOIN:
			return "JOIN";
		case XAResource.TMNOFLAGS:
			return "NOFLAGS";
		case XAResource.TMONEPHASE:
			return "ONEPHASE";
		case XAResource.TMRESUME:
			return "RESUME";
		case XAResource.TMSTARTRSCAN:
			return "STARTRSCAN";
		case XAResource.TMSUCCESS:
			return "SUCCESS";
		case XAResource.TMSUSPEND:
			return "SUSPEND";
		default:
			return "�flag invalido(" + flag + ")!";
		}
	}

	/**
	 * Obtiene el string con el estado del Recurso
	 * 
	 * @return descriptivo del estado del recurso
	 */
	private String pintaEstado() {
		switch (state) {
		case NO_TX:
			return "NO_TX";
		case STARTED:
			return "STARTED";
		case ENDED:
			return "ENDED";
		case PREPARED:
			return "PREPARED";
		default:
			return "!estado invalido (" + state + ")!";
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#getTransactionTimeout()
	 */
	public int getTransactionTimeout() throws XAException {
		return txTimeout;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see XAResource#setTransactionTimeout(int)
	 */
	public boolean setTransactionTimeout(int seconds) throws XAException {
		this.txTimeout = seconds;
		return true;
	}
}
