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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.work.WorkManager;
import javax.security.auth.Subject;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusRuntimeException;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.BasicRegistroFactory;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.jca.io.AuriusDiccionarioMessage;
import com.farmafene.aurius.jca.io.AuriusExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusInvokeMessage;
import com.farmafene.aurius.jca.io.AuriusMessage;
import com.farmafene.aurius.jca.io.AuriusResponseMessage;
import com.farmafene.aurius.jca.io.AuriusXACommit;
import com.farmafene.aurius.jca.io.AuriusXAExceptionMessage;
import com.farmafene.aurius.jca.io.AuriusXAForget;
import com.farmafene.aurius.jca.io.AuriusXAMessage;
import com.farmafene.aurius.jca.io.AuriusXAPrepare;
import com.farmafene.aurius.jca.io.AuriusXAPrepareResponse;
import com.farmafene.aurius.jca.io.AuriusXARollBack;
import com.farmafene.aurius.jca.io.Codec;
import com.farmafene.aurius.jca.io.IOUtil;
import com.farmafene.aurius.jca.io.JMSHelper;

public class TxManagedConnection implements ManagedConnection {

	private static final Logger logger = LoggerFactory
			.getLogger(TxManagedConnection.class);
	private UUID uUID;
	private PrintWriter logWriter;
	private List<ConnectionEventListener> connectionEventListeners;
	private Connection jmsConnection;
	private transient QueueConnectionFactory queueConnectionFactory;
	private TxConnectionRequestInfo txConnectionRequestInfo;
	private TxManagedConnectionMetaData txManagedConnectionMetaData;
	private Set<TxConnection> connectionHandlers;
	private XAResource xAResource;
	private boolean internalRegistro = true;

	/**
	 * Constructor
	 */
	public TxManagedConnection() {
		this.uUID = UUID.randomUUID();
		this.connectionEventListeners = new LinkedList<ConnectionEventListener>();
		this.txManagedConnectionMetaData = new TxManagedConnectionMetaData();
		this.connectionHandlers = new HashSet<TxConnection>();
	}

	/**
	 * Establece la fábrica de conexiones
	 * 
	 * @param queueConnectionFactory
	 */
	void setQueueConnectionFactory(QueueConnectionFactory queueConnectionFactory) {
		this.queueConnectionFactory = queueConnectionFactory;
		getJmsConnection();
	}

	/**
	 * Obtiene la conexión activa
	 * 
	 * @return conexión
	 */
	private Connection getJmsConnection() {
		if (jmsConnection == null) {
			try {
				if (this.txConnectionRequestInfo == null
						|| this.txConnectionRequestInfo.getUser() == null) {
					jmsConnection = queueConnectionFactory.createConnection();
				} else {
					jmsConnection = queueConnectionFactory.createConnection(
							txConnectionRequestInfo.getUser(),
							txConnectionRequestInfo.getPassword());
				}
				jmsConnection.start();
			} catch (JMSException e) {
				error();
				throw new AuriusRuntimeException(e);
			}
		}
		return jmsConnection;
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
		sb.append("UUID=").append(this.uUID);
		sb.append(", with=").append(connectionHandlers.size())
				.append(" active handlers");
		sb.append(", ConnectionRequestInfo=").append(
				this.txConnectionRequestInfo);
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
				+ ((txConnectionRequestInfo == null) ? 0
						: txConnectionRequestInfo.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#addConnectionEventListener(javax
	 *      .resource.spi.ConnectionEventListener)
	 */
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		connectionEventListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#removeConnectionEventListener(javax
	 *      .resource.spi.ConnectionEventListener)
	 */
	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		connectionEventListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#associateConnection(java.lang.Object
	 *      )
	 */
	@Override
	public void associateConnection(Object connection) throws ResourceException {
		if (!(connection instanceof TxConnection)) {
			throw new javax.resource.NotSupportedException(
					"No soportado para este tipo");
		}
		TxConnection con = (TxConnection) connection;
		xAResource = con.getTxManagedConnection().getXAResource();
		((TxXAResource) xAResource).setTxManagedConnection(this);
		con.getTxManagedConnection().connectionHandlers.remove(con);
		con.setTxManagedConnection(this);
		this.connectionHandlers.add(con);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#cleanup()
	 */
	@Override
	public void cleanup() throws ResourceException {
		logger.debug("cleanup()");
		for (TxConnection con : connectionHandlers) {
			con.close();
		}
		this.connectionHandlers.clear();
		this.txConnectionRequestInfo.cleanup();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#destroy()
	 */
	@Override
	public void destroy() throws ResourceException {
		logger.debug("destroy()");
		cleanup();
		Connection con = getJmsConnection();
		if (con != null) {
			JMSHelper.close(con, null, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return this.logWriter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter logWriter) throws ResourceException {
		this.logWriter = logWriter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#getLocalTransaction()
	 */
	@Override
	public LocalTransaction getLocalTransaction() throws ResourceException {
		throw new NotSupportedException(
				"getLocalTransaction(): Not Supported!!!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#getConnection(javax.security.auth
	 *      .Subject, javax.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public Object getConnection(Subject subject, ConnectionRequestInfo cri)
			throws ResourceException {
		TxConnection con = new TxConnection();
		con.setTxManagedConnection(this);
		connectionHandlers.add(con);
		return con;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#getXAResource()
	 */
	@Override
	public XAResource getXAResource() throws ResourceException {
		if (this.xAResource == null) {
			this.xAResource = new TxXAResource(this);
		}
		return this.xAResource;
	}

	/**
	 * @param txConnectionRequestInfo
	 *            the txConnectionRequestInfo to set
	 */
	void setTxConnectionRequestInfo(
			TxConnectionRequestInfo txConnectionRequestInfo) {
		this.txConnectionRequestInfo = txConnectionRequestInfo;
		this.txManagedConnectionMetaData.setUserName(txConnectionRequestInfo
				.getUser());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.resource.spi.ManagedConnection#getMetaData()
	 */
	@Override
	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		return this.txManagedConnectionMetaData;
	}

	/**
	 * @return the txConnectionRequestInfo
	 */
	TxConnectionRequestInfo getTxConnectionRequestInfo() {
		return txConnectionRequestInfo;
	}

	private void sendEvent(int type, TxConnection connection) {
		ConnectionEvent event = new ConnectionEvent(this, type);
		for (ConnectionEventListener listener : connectionEventListeners) {
			switch (type) {
			case ConnectionEvent.CONNECTION_CLOSED:
				logger.debug("CONNECTION_CLOSED: " + connection);
				event.setConnectionHandle(connection);
				listener.connectionClosed(event);
				connectionHandlers.remove(connection);
				break;
			case ConnectionEvent.CONNECTION_ERROR_OCCURRED:
				logger.debug("CONNECTION_ERROR_OCCURRED: " + connection);
				listener.connectionErrorOccurred(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
				logger.debug("LOCAL_TRANSACTION_COMMITTED: " + connection);
				listener.localTransactionCommitted(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
				logger.debug("LOCAL_TRANSACTION_ROLLEDBACK: " + connection);
				listener.localTransactionRolledback(event);
				break;
			case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
				logger.debug("LOCAL_TRANSACTION_STARTED: " + connection);
				listener.localTransactionStarted(event);
				break;
			}
		}
	}

	void closed(TxConnection con) {
		sendEvent(ConnectionEvent.CONNECTION_CLOSED, con);
	}

	private void committed() {
		sendEvent(ConnectionEvent.LOCAL_TRANSACTION_COMMITTED, null);
	}

	void error() {
		sendEvent(ConnectionEvent.CONNECTION_ERROR_OCCURRED, null);
	}

	private void rolledback() {
		sendEvent(ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK, null);
	}

	void started() {
		sendEvent(ConnectionEvent.LOCAL_TRANSACTION_STARTED, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	public Registro process(AuthInfo auth, String servicioId)
			throws IllegalArgumentException, RemoteException {
		Registro salida = null;
		if (isInternalRegistro()) {
			salida = BasicRegistroFactory.getRegistro(null);
		} else {

			salida = getFromCache(servicioId);
			if (salida == null) {
				if (logger.isDebugEnabled()) {
					logger.info("process('" + auth + "', '" + servicioId + "')");
				}
				AuriusDiccionarioMessage in = new AuriusDiccionarioMessage();
				in.setCookie(auth);
				in.setServicioId(servicioId);
				AuriusResponseMessage out = (AuriusResponseMessage) sendAndWait(in);
				salida = out.getRegistro();
				if (salida != null) {
					putInCache(servicioId, salida);
				}
			}
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String)
	 */
	public Registro process(AuthInfo auth, String servicioId, String version)
			throws IllegalArgumentException, RemoteException {
		Registro salida = getFromCache(servicioId, version);
		if (salida == null) {
			logger.info("process('" + auth + "', '" + servicioId + "', '"
					+ version + "')");
			AuriusDiccionarioMessage in = new AuriusDiccionarioMessage();
			in.setCookie(auth);
			in.setServicioId(servicioId);
			in.setVersion(version);
			AuriusResponseMessage out = (AuriusResponseMessage) sendAndWait(in);
			salida = out.getRegistro();
			if (salida != null) {
				putInCache(servicioId, version, salida);
			}
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String, com.farmafene.aurius.Registro)
	 */
	public Registro process(AuthInfo auth, String servicioId, String version,
			Registro registro) throws IllegalArgumentException, RemoteException {
		logger.info("process('" + auth + "', '" + servicioId + "', '" + version
				+ "', Registro)");
		AuriusInvokeMessage in = new AuriusInvokeMessage();
		in.setCookie(auth);
		in.setServicioId(servicioId);
		in.setVersion(version);
		in.setRegistro(registro);
		in.setTransactionTimeOut(WorkManager.UNKNOWN);
		if (null != this.getTxConnectionRequestInfo().getXid()) {
			in.setCorrelationXid(this.getTxConnectionRequestInfo().getXid());
			in.setTransactionTimeOut(Long.valueOf(""
					+ getTxConnectionRequestInfo().getTransactionTimeout()));
		}
		AuriusResponseMessage out = (AuriusResponseMessage) sendAndWait(in);
		return out.getRegistro();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusConnection#process(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, com.farmafene.aurius.Registro)
	 */
	public Registro process(AuthInfo auth, String servicioId, Registro registro)
			throws IllegalArgumentException, RemoteException {
		logger.info("process('" + auth + "', '" + servicioId + "', Registro)");
		AuriusInvokeMessage in = new AuriusInvokeMessage();
		in.setCookie(auth);
		in.setServicioId(servicioId);
		in.setRegistro(registro);
		in.setTransactionTimeOut(WorkManager.UNKNOWN);
		if (null != this.getTxConnectionRequestInfo().getXid()) {
			in.setCorrelationXid(this.getTxConnectionRequestInfo().getXid());
			in.setTransactionTimeOut(Long.valueOf(getTxConnectionRequestInfo()
					.getTransactionTimeout()));
		}
		AuriusResponseMessage out = (AuriusResponseMessage) sendAndWait(in);
		return out.getRegistro();
	}

	void commit(Xid id, boolean onePhase) throws XAException {
		logger.info("commit(" + id + ", " + (onePhase ? "1PC" : "2PC") + ")");
		AuriusXACommit msg = new AuriusXACommit();
		msg.setCorrelationXid(new AuriusXid(id));
		msg.setTransactionTimeOut(Long.valueOf(""
				+ getTxConnectionRequestInfo().getTransactionTimeout()));
		msg.setOnePhase(onePhase);
		sendXAMessage(msg);
		removeFromRecovery(id);
		committed();

	}

	void forget(Xid id) throws XAException {
		logger.info("forget(" + id + ")");
		AuriusXAForget msg = new AuriusXAForget();
		msg.setCorrelationXid(new AuriusXid(id));
		msg.setTransactionTimeOut(Long.valueOf(""
				+ getTxConnectionRequestInfo().getTransactionTimeout()));
		sendXAMessage(msg);
		// FIXME - ¿Qué ocurre con la conexión?
		rolledback();
	}

	int prepare(Xid id) throws XAException {
		logger.info("prepare(" + id + ")");
		addToRecovery(id);
		AuriusXAPrepare msg = new AuriusXAPrepare();
		msg.setCorrelationXid(new AuriusXid(id));
		msg.setTransactionTimeOut(Long.valueOf(""
				+ getTxConnectionRequestInfo().getTransactionTimeout()));
		AuriusMessage out = sendXAMessage(msg);
		if (out instanceof AuriusXAPrepareResponse) {
			return ((AuriusXAPrepareResponse) out).getPrepare();
		}
		// TODO -
		XAException e = new XAException("Error en el prepare");
		e.errorCode = XAException.XAER_INVAL;
		throw e;
	}

	Xid[] recover(int flag) throws XAException {
		logger.info("recover(" + flag + ")");
		List<Xid> aux = new ArrayList<Xid>();
		readFromRecovery(aux);
		if (aux.size() > 0) {
			// TODO recover(int arg0) throws XAException
		}
		return aux.toArray(new Xid[0]);
	}

	void rollback(Xid id) throws XAException {
		logger.info("rollback(" + id + ")");
		AuriusXARollBack msg = new AuriusXARollBack();
		msg.setCorrelationXid(new AuriusXid(id));
		msg.setTransactionTimeOut(Long.valueOf(""
				+ getTxConnectionRequestInfo().getTransactionTimeout()));
		sendXAMessage(msg);
		removeFromRecovery(id);
		rolledback();
	}

	private AuriusMessage sendXAMessage(AuriusXAMessage in) throws XAException {
		AuriusMessage outMsg = null;
		outMsg = sendAndWait(in);
		if (outMsg instanceof AuriusXAExceptionMessage) {
			AuriusXAExceptionMessage ex = (AuriusXAExceptionMessage) outMsg;
			XAException xaEx = new XAException(ex.getMessage());
			xaEx.initCause(ex.getTh());
			xaEx.errorCode = ex.getErrorCode();
			throw xaEx;
		}
		return outMsg;
	}

	private AuriusMessage sendAndWait(AuriusMessage in) {

		AuriusMessage outMsg = null;
		Session sess = null;
		MessageProducer prod = null;
		MessageConsumer consumer = null;
		try {
			sess = getJmsConnection().createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Queue replayTo = sess.createTemporaryQueue();

			if (getTxConnectionRequestInfo().getDestination() == null) {
				Queue cola = sess.createQueue(getTxConnectionRequestInfo()
						.getQueue());
				getTxConnectionRequestInfo().setDestination(cola);
			}
			prod = sess.createProducer(getTxConnectionRequestInfo()
					.getDestination());
			prod.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			consumer = sess.createConsumer(replayTo);
			if (txConnectionRequestInfo.getTimeToLive() > 0) {
				prod.setTimeToLive(txConnectionRequestInfo.getTimeToLive());
			}
			BytesMessage msg = sess.createBytesMessage();
			msg.setIntProperty("Codec", Codec.Java.ordinal());
			msg.setJMSReplyTo(replayTo);
			msg.writeBytes(IOUtil.marshall(in, Codec.Java));
			if (logger.isDebugEnabled()) {
				logger.debug("Enviando... {}", in);
				logger.debug("- Queue: {}", getTxConnectionRequestInfo()
						.getDestination());
				logger.debug("- ReplayTo: {}", replayTo);
			}
			prod.send(msg);
			if (logger.isDebugEnabled()) {
				logger.debug("Enviado.");
			}
			Message msgIn = null;
			BytesMessage msgRecibido = null;
			if (txConnectionRequestInfo.getTimeOut() > 0) {
				msgIn = consumer.receive(txConnectionRequestInfo.getTimeOut());
			} else {
				msgIn = consumer.receive();
			}
			if (msgIn instanceof BytesMessage) {
				msgRecibido = (BytesMessage) msgIn;
			} else if (null == msgIn) {
				throw new IOException("TimeOut!!! ");
			} else {
				throw new IllegalArgumentException(
						"El mensaje es de tipo inv�lido: " + msgIn);
			}
			Codec inCodec = Codec
					.getByCode(msgRecibido.getIntProperty("Codec"));
			if (msgRecibido.getJMSReplyTo() != null) {
				txConnectionRequestInfo.setLocalDestination(msgRecibido
						.getJMSReplyTo());
			}
			Serializable out = IOUtil.unmarshall(IOUtil.readBytes(msgRecibido),
					inCodec);
			if (out instanceof AuriusExceptionMessage) {
				throw ((AuriusExceptionMessage) out).getTh();
			}
			if (out instanceof AuriusMessage) {
				outMsg = (AuriusMessage) out;
			} else {
				throw new IllegalArgumentException(
						"El mensaje no es del tipo '"
								+ AuriusMessage.class.getCanonicalName() + "'.");
			}
		} catch (JMSException e) {
			error();
			AuriusRuntimeException are = new AuriusRuntimeException(e);
			logger.error("Error en el env�o", are);
			throw are;
		} catch (IllegalArgumentException e) {
			error();
			AuriusRuntimeException are = new AuriusRuntimeException(e);
			logger.error("Error en el env�o", are);
			throw are;
		} catch (IOException e) {
			error();
			AuriusRuntimeException are = new AuriusRuntimeException(e);
			logger.error("Error en el env�o", are);
			throw are;
		} finally {
			JMSHelper.close(null, sess, prod, consumer);
		}
		return outMsg;
	}

	private void putInCache(String servicioId, String version, Registro salida) {
		// TODO Auto-generated method stub

	}

	private void putInCache(String servicioId, Registro salida) {
		// TODO Auto-generated method stub

	}

	private Registro getFromCache(String servicioId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Registro getFromCache(String servicioId, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	private void addToRecovery(Xid id) {
		// TODO Auto-generated method stub

	}

	private void readFromRecovery(List<Xid> aux) {
		// TODO Auto-generated method stub

	}

	private void removeFromRecovery(Xid aux) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return el internalRegistro
	 */
	public boolean isInternalRegistro() {
		return internalRegistro;
	}

	/**
	 * @param internalRegistro
	 *            el internalRegistro a establecer
	 */
	public void setInternalRegistro(boolean internalRegistro) {
		this.internalRegistro = internalRegistro;
	}
}
