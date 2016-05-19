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
package com.farmafene.aurius.core.impl;

import java.io.Serializable;

import javax.resource.NotSupportedException;
import javax.resource.spi.IllegalStateException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.UserTransaction;

import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.core.AplicacionHolderFactory;
import com.farmafene.aurius.core.AuthenticationManager;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IGestorTx;
import com.farmafene.aurius.core.TransactionHelperLocator;
import com.farmafene.aurius.mngt.CommitStatus;
import com.farmafene.aurius.mngt.RollbackStatus;
import com.farmafene.aurius.server.CommandInvoker;
import com.farmafene.aurius.server.impl.CommandInvokerImpl;

/**
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class GestorTx implements Serializable, IGestorTx {

	private static enum Estado {
		ERROR_PRECONDICIONES, TX_COMMAND, TX_BIND, ENDED
	};

	private static Logger logger = LoggerFactory.getLogger(GestorTx.class);

	public GestorTx() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see IGestorTx#invoke(ContextoCore, String, String, Registro)
	 */
	@Override
	public Registro invoke(ContextoCore ctx, String idServicio, String version,
			Registro in) {
		Estado state = Estado.ERROR_PRECONDICIONES;
		ContextoManager.getIContextoManager().setContexto(ctx);
		ContextoManager.getIContextoManager().setIdServicio(ctx, idServicio);
		ContextoManager.getIContextoManager().setVersionServicio(ctx, version);
		CommandInvoker invoker = null;
		Throwable error = null;
		try {
			state = doPrecondiciones();
			AuthenticationManager.valida(ctx.getAuthInfo(), idServicio);
			invoker = new CommandInvokerImpl(idServicio, version, in);
			return invoker.invoke();
		} catch (Throwable th) {
			error = th;
			RuntimeException rte = AuriusExceptionTO.getInstance(error);
			logger.error("Error en invocación", rte);
			throw rte;
		} finally {
			try {
				doPostCondiciones(state, error);
			} catch (Throwable th) {
				error = th;
				RuntimeException rte = AuriusExceptionTO.getInstance(error);
				logger.error("Error en poscondiciones", rte);
				throw rte;
			}
		}
	}

	private Estado doPrecondiciones() throws SystemException,
			NotSupportedException, javax.transaction.SystemException,
			javax.transaction.NotSupportedException, IllegalStateException,
			RollbackException {
		Estado state = Estado.ERROR_PRECONDICIONES;
		if (ContextoManager.getContexto() == null) {
			throw new IllegalStateException("El contexto no puede ser Null.");
		}
		UserTransaction txm = TransactionHelperLocator.getUserTransaction();
		if (txm.getStatus() == Status.STATUS_NO_TRANSACTION) {
			txm.begin();
			state = Estado.TX_COMMAND;
		} else {
			state = Estado.TX_BIND;
			if (Status.STATUS_MARKED_ROLLBACK == TransactionHelperLocator
					.getUserTransaction().getStatus()) {
				throw new IllegalStateException("RollbackOnly Transaction!!!!");
			}
			final ContextoCore ctx = ContextoManager.getContexto();
			TransactionHelperLocator.getTransactionManager().getTransaction()
					.registerSynchronization(new Synchronization() {
						/**
						 * {@inheritDoc}
						 * 
						 * @see javax.transaction.Synchronization#beforeCompletion
						 *      ()
						 */
						@Override
						public void beforeCompletion() {
							logger.debug("Transacción con {} ClassLoaders.",
									ctx.getIAplicacionHolders().size());
						}

						/**
						 * {@inheritDoc}
						 * 
						 * @see javax.transaction.Synchronization#afterCompletion
						 *      (int)
						 */
						@Override
						public void afterCompletion(int status) {
							logger.debug(
									"Procediendo a la liberacion de {} ClassLoaders.",
									ctx.getIAplicacionHolders().size());
							for (IAplicacionHolder h : ctx
									.getIAplicacionHolders().values()) {
								logger.debug("Liberando: {}", h);
								h.release();
							}
						}
					});
		}
		logger.debug("Precondiciones [{}], state={}", txm.getStatus(), state);
		return state;
	}

	private void doPostCondiciones(Estado state, Throwable error)
			throws SecurityException, HeuristicMixedException,
			HeuristicRollbackException, RollbackException, SystemException,
			IllegalStateException, javax.transaction.SystemException {
		logger.debug("Postcondiciones={}", this);
		try {
			Throwable error2Phase = null;
			switch (state) {
			case TX_COMMAND:
				if (null != error) {
					RollbackStatus status = new RollbackStatus();
					try {
						status.start();
						TransactionHelperLocator.getUserTransaction()
								.rollback();
					} catch (Throwable e) {
						error2Phase = e;
					} finally {
						status.stop(error2Phase);
					}
				} else {
					CommitStatus status = new CommitStatus();
					status.start();
					try {
						TransactionHelperLocator.getUserTransaction().commit();
					} catch (Throwable th) {
						error2Phase = th;
					} finally {
						status.stop(error2Phase);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Cerrando los ClassLaders abiertos!!!");
				}
				AplicacionHolderFactory.dispose();
				break;
			case TX_BIND:
				if (null != error) {
					TransactionHelperLocator.getUserTransaction()
							.setRollbackOnly();
				}
				break;
			default:
				logger.info("El estado {}, no es válido en postcondiciones",
						state);
			}
		} finally {
			state = Estado.ENDED;
			if (logger.isDebugEnabled()) {
				logger.debug("Postcondiciones={}", state);
			}
		}
	}
}
