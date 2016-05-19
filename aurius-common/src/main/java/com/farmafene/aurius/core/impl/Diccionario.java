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
package com.farmafene.aurius.core.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.AuthenticationManager;
import com.farmafene.aurius.core.IDiccionario;
import com.farmafene.aurius.core.IDiccionarioCache;
import com.farmafene.aurius.core.IDiccionarioFactory;
import com.farmafene.aurius.core.Servicio;
import com.farmafene.aurius.core.TransactionHelperLocator;
import com.farmafene.aurius.mngt.DiccionarioStatus;
import com.farmafene.aurius.util.ProxyFactory;

@SuppressWarnings("serial")
public class Diccionario implements Serializable, IDiccionario {
	private static final Logger logger;
	private static final DiccionarioFactory diccionarioFactory;

	/**
	 * Factoría para la obtención de datos del diccionario
	 * 
	 * @author vlopez
	 * @since 1.0.0
	 */
	static class DiccionarioFactory {
		private IDiccionarioFactory factory = new DiccionarioFactoryProxy();
		private IDiccionarioCache cache = new DiccionarioCacheProxy();
		private ProxyFactory<Servicio> proxyServicio = new ProxyFactory<Servicio>();;
		private ProxyFactory<TypeRegistro> proxyTypeRegistro = new ProxyFactory<TypeRegistro>();
		private ProxyFactory<Registro> proxyRegistro = new ProxyFactory<Registro>();

		/**
		 * Constructor privado
		 * 
		 * @since 1.0.0
		 */
		public DiccionarioFactory() {
		}

		/**
		 * Obtención de la Factoría activa
		 * 
		 * @return factoria actual
		 * @since 1.0.0
		 */
		public final IDiccionarioFactory getIDiccionarioFactory() {
			return factory;
		}

		/**
		 * Obtención de la Factoría activa
		 * 
		 * @return factoria actual
		 * @since 1.0.0
		 */
		public final IDiccionarioCache getIDiccionarioCache() {
			return cache;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see IDiccionarioFactory#getDefinicionRegistro(String)
		 * @since 1.0.0
		 */
		public TypeRegistro getDefinicionRegistro(String id)
				throws IllegalArgumentException {
			TypeRegistro reg = null;
			String nombre = getIDiccionarioFactory().getRegistroNormalizado(id);
			if (logger.isDebugEnabled()) {
				logger.debug(" - Id entrada: " + id);
				logger.debug(" - nombre entrada: " + nombre);
			}
			if (null != id) {
				reg = getIDiccionarioCache().getDefinicionRegistro(nombre);
				if (reg == null) {
					reg = proxyTypeRegistro
							.newInstance(getIDiccionarioFactory()
									.getDefinicionRegistro(nombre));
					if (reg != null) {
						getIDiccionarioCache().put(nombre, reg);
					}
				}
			}
			return reg;

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see IDiccionarioFactory#getServicio(String)
		 * @since 1.0.0
		 */
		public Servicio getServicio(String id) throws IllegalArgumentException {
			Servicio svr = null;
			String nombre = getIDiccionarioFactory().getServicioNormalizado(id);
			svr = getIDiccionarioCache().getServicio(nombre);
			if (null == svr) {
				svr = proxyServicio.newInstance(getIDiccionarioFactory()
						.getServicio(nombre));
				if (svr != null) {
					getIDiccionarioCache().put(nombre, svr);
				}
			}
			return svr;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see IDiccionarioFactory#getServicio(String, String)
		 * @since 1.0.0
		 */
		public Servicio getServicio(String id, String version)
				throws IllegalArgumentException {
			Servicio svr = null;
			String nombre = getIDiccionarioFactory().getServicioNormalizado(id);
			if (version == null) {
				svr = getServicio(nombre);
			} else {
				String v = getIDiccionarioFactory().getVersionNomalizada(
						version);
				svr = getIDiccionarioCache().getServicio(nombre, v);
				if (null == svr) {
					svr = proxyServicio.newInstance(getIDiccionarioFactory()
							.getServicio(nombre, v));
					if (svr != null) {
						getIDiccionarioCache().put(nombre, v, svr);
					}
				}
			}
			return svr;
		}

		public Registro getRegistro(String idRegistro) {
			Map<String, TypeRegistro> definiciones = null;
			if ((definiciones = getIDiccionarioCache().getRegistro(idRegistro)) == null) {
				definiciones = new HashMap<String, TypeRegistro>();
				TypeRegistro tr = getDefinicionRegistro(idRegistro);
				if (null != tr) {
					definiciones.put(idRegistro, tr);
					loadDefiniciones(definiciones, tr);
				}
			}
			return proxyRegistro.newInstance(getIDiccionarioFactory()
					.getRegistro(idRegistro, definiciones));
		}

		private void loadDefiniciones(Map<String, TypeRegistro> definiciones,
				TypeRegistro tr) {
			for (String name : tr.getNombres()) {
				CampoRegistro campo = tr.getCampoRegistro(name);
				if (((Object) campo.getDatoRegistro()) instanceof TypeRegistro) {
					if (!definiciones.containsKey(campo.getDatoRegistro()
							.getId())) {
						TypeRegistro def = getDefinicionRegistro(campo
								.getDatoRegistro().getId());
						definiciones.put(campo.getDatoRegistro().getId(), def);
						loadDefiniciones(definiciones, def);
					}
				}
			}
		}
	}

	static {
		logger = LoggerFactory.getLogger(Diccionario.class);
		diccionarioFactory = new DiccionarioFactory();
	}

	public Diccionario() {
		logger.debug(this + "<init>");
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
	 */
	public Registro getRegistroServicio(AuthInfo cookie, String servicioId) {
		DiccionarioStatus stat = new DiccionarioStatus();
		stat.setIdentificador(servicioId);
		stat.setOperacion("getRegistroServicio");
		stat.start();
		Throwable thE = null;
		boolean inTransaction = true;
		UserTransaction txm = TransactionHelperLocator.getUserTransaction();
		try {
			if (txm.getStatus() == Status.STATUS_NO_TRANSACTION) {
				txm.begin();
				inTransaction = false;
			}
		} catch (SystemException e) {
			logger.error("Error en UserTransaction", e);
		} catch (NotSupportedException e) {
			logger.error("Error en UserTransaction", e);
		}
		try {
			AuthenticationManager.valida(cookie, servicioId);
			Servicio svr = diccionarioFactory.getServicio(servicioId);
			if (svr == null) {
				throw new IllegalArgumentException("El servicio '" + servicioId
						+ "' no exite.");
			}
			if (null != svr.getIdRegistroEntrada()) {
				return diccionarioFactory.getRegistro(svr
						.getIdRegistroEntrada());
			} else {
				logger.info("No hay registro de entrada para: " + svr);
				return null;
			}
		} catch (Throwable th) {
			thE = th;
			throw AuriusExceptionTO.getInstance(th);
		} finally {
			stat.stop(thE);
			if (!inTransaction) {
				if (null == thE) {
					try {
						txm.commit();
					} catch (IllegalStateException e) {
						logger.error("Error en Commit!", e);
					} catch (SecurityException e) {
						logger.error("Error en Commit!", e);
					} catch (HeuristicMixedException e) {
						logger.error("Error en Commit!", e);
					} catch (HeuristicRollbackException e) {
						logger.error("Error en Commit!", e);
					} catch (RollbackException e) {
						logger.error("Error en Commit!", e);
					} catch (SystemException e) {
						logger.error("Error en Commit!", e);
					}
				} else {
					try {
						txm.rollback();
					} catch (IllegalStateException e) {
						logger.error("Error en Rollback!", e);
					} catch (SecurityException e) {
						logger.error("Error en Rollback!", e);
					} catch (SystemException e) {
						logger.error("Error en Rollback!", e);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Registro getRegistroServicio(AuthInfo cookie, String servicioId,
			String version) {
		DiccionarioStatus stat = new DiccionarioStatus();
		Throwable thE = null;
		stat.setIdentificador(servicioId + "," + version);
		stat.setOperacion("getRegistroServicio");
		stat.start();
		boolean inTransaction = true;
		UserTransaction txm = TransactionHelperLocator.getUserTransaction();
		try {
			if (txm.getStatus() == Status.STATUS_NO_TRANSACTION) {
				txm.begin();
				inTransaction = false;
			}
		} catch (SystemException e) {
			logger.error("Error en UserTransaction", e);
		} catch (NotSupportedException e) {
			logger.error("Error en UserTransaction", e);
		}
		try {
			AuthenticationManager.valida(cookie, servicioId);
			Servicio svr = diccionarioFactory.getServicio(servicioId, version);
			if (svr == null) {
				throw new IllegalArgumentException("El servicio '" + servicioId
						+ "v" + version + "' no exite.");
			}
			if (null != svr.getIdRegistroEntrada()) {
				return diccionarioFactory.getRegistro(svr
						.getIdRegistroEntrada());
			} else {
				logger.info("No hay registro de entrada para: " + svr);
				return null;
			}
		} catch (Throwable th) {
			thE = th;
			throw new RuntimeException(th);
		} finally {
			stat.stop(thE);
			if (!inTransaction) {
				if (null == thE) {
					try {
						txm.commit();
					} catch (IllegalStateException e) {
						logger.error("Error en Commit!", e);
					} catch (SecurityException e) {
						logger.error("Error en Commit!", e);
					} catch (HeuristicMixedException e) {
						logger.error("Error en Commit!", e);
					} catch (HeuristicRollbackException e) {
						logger.error("Error en Commit!", e);
					} catch (RollbackException e) {
						logger.error("Error en Commit!", e);
					} catch (SystemException e) {
						logger.error("Error en Commit!", e);
					}
				} else {
					try {
						txm.rollback();
					} catch (IllegalStateException e) {
						logger.error("Error en Rollback!", e);
					} catch (SecurityException e) {
						logger.error("Error en Rollback!", e);
					} catch (SystemException e) {
						logger.error("Error en Rollback!", e);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.Diccionario#getRegistro(String)
	 */
	public Registro getRegistro(String idDiccionario) {
		DiccionarioStatus stat = new DiccionarioStatus();
		Throwable thE = null;
		stat.setIdentificador(idDiccionario);
		stat.setOperacion("getRegistro");
		stat.start();
		try {
			return diccionarioFactory.getRegistro(idDiccionario);
		} catch (Throwable th) {
			thE = th;
			throw AuriusExceptionTO.getInstance(th);
		} finally {
			stat.stop(thE);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.Diccionario#getDefinicionRegistro(java.lang
	 *      .String)
	 */
	public TypeRegistro getDefinicionRegistro(String idRegistro) {
		DiccionarioStatus stat = new DiccionarioStatus();
		Throwable thE = null;
		stat.setIdentificador(idRegistro);
		stat.setOperacion("getDefinicionRegistro");
		stat.start();
		try {
			return diccionarioFactory.getDefinicionRegistro(idRegistro);
		} catch (Throwable th) {
			thE = th;
			throw AuriusExceptionTO.getInstance(th);
		} finally {
			stat.stop(thE);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.Diccionario#getServicio(java.lang.String)
	 */
	public Servicio getServicio(String idServicio) {
		DiccionarioStatus stat = new DiccionarioStatus();
		Throwable thE = null;
		stat.setIdentificador(idServicio);
		stat.setOperacion("getServicio");
		stat.start();
		try {
			return diccionarioFactory.getServicio(idServicio);
		} catch (Throwable th) {
			thE = th;
			throw AuriusExceptionTO.getInstance(th);
		} finally {
			stat.stop(thE);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.core.Diccionario#getServicio(java.lang.String,
	 *      java.lang.String)
	 */
	public Servicio getServicio(String idServicio, String version) {
		DiccionarioStatus stat = new DiccionarioStatus();
		Throwable thE = null;
		stat.setIdentificador(idServicio + "," + version);
		stat.setOperacion("getServicio");
		stat.start();
		try {
			return diccionarioFactory.getServicio(idServicio, version);
		} catch (Throwable th) {
			thE = th;
			throw AuriusExceptionTO.getInstance(th);
		} finally {
			stat.stop(thE);
		}
	}
}
