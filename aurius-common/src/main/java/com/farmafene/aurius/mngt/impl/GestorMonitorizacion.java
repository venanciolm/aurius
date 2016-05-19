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
package com.farmafene.aurius.mngt.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IGestorMonitorizacion;
import com.farmafene.aurius.mngt.AuriusOperationContainerStatus;
import com.farmafene.aurius.mngt.CallStatus;
import com.farmafene.aurius.mngt.IBasicStat;
import com.farmafene.aurius.mngt.IOperacionEventObserver;
import com.farmafene.aurius.mngt.OperacionEvent;
import com.farmafene.aurius.mngt.OperacionEventSubject;
import com.farmafene.aurius.server.Contexto;
import com.farmafene.aurius.server.ContextoLocator;

/**
 * Implementación de Gestor de monitorización
 * 
 * @author vlopez
 * @since 1.0.0
 */
public final class GestorMonitorizacion implements IGestorMonitorizacion,
		IOperacionEventObserver {
	private static final Logger logger = LoggerFactory
			.getLogger(GestorMonitorizacion.class);

	private boolean state = false;

	private List<IOperacionEventObserver> listeners;
	private List<IOperacionEventObserver> others;
	private Map<UUID, CallStatus> activas;
	private IGestorFinalizadas iGestorFinalizadas;

	/**
	 * Listener que se ocupa de dar de alta las llamadas activas
	 */
	private IOperacionEventObserver beginListener = new IOperacionEventObserver() {
		@Override
		public void actionPerformed(OperacionEvent event) {
			switch (event.getTipo()) {
			case BEGIN:
				if (event.getSource() instanceof CallStatus) {
					CallStatus src = (CallStatus) event.getSource();
					logger.debug("Introducido el contexto para monitorizar: "
							+ src.getContexto());
					activas.put(src.getContexto().getId(), src);
				}
				break;
			case END:
				// DO NOTHING
				break;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(GestorMonitorizacion.class.getSimpleName()).append(
					".beginListener={}");
			return sb.toString();
		}
	};

	/**
	 * Listener que se ocupa de la gestión de estados activos
	 */
	private IOperacionEventObserver processListener = new IOperacionEventObserver() {
		@Override
		public void actionPerformed(OperacionEvent event) {
			final CallStatus r = getCurrentCall();
			logger.debug("\n - currentCall:{}\n - event: {}", r, event);
			if (null != r) {
				switch (event.getTipo()) {
				case BEGIN:
					if (!(event.getSource() instanceof CallStatus)) {
						IBasicStat current = r.getCurrent();
						r.getPila().push(event.getSource());
						if (current instanceof AuriusOperationContainerStatus) {
							((AuriusOperationContainerStatus) current)
									.getOperacionStatus()
									.add(event.getSource());
						} else {
							logger.error(
									"Estado inválido: {} , debería ser un contenedor y no lo es",
									current);
						}
					}
					break;
				case END:
					if (r.getCurrent() != event.getSource()) {
						logger.error(
								"Se ha producido un error en monitorizacion: {} vs. {}",
								r.getCurrent(), event.getSource());
					}
					if (!r.getPila().isEmpty()) {
						r.getPila().pop();
					} else {
						logger.error(
								"Se ha producido un error en monitorizacion: {} no tiene llamadas asociadas ",
								r);
					}
					if ((event.getSource() instanceof CallStatus)) {
						activas.remove(((CallStatus) event.getSource())
								.getContexto().getId());
						toFinalizadas((CallStatus) event.getSource());
					}
					break;
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(GestorMonitorizacion.class.getSimpleName()).append(
					".processListener={}");
			return sb.toString();
		}

	};
	/**
	 * Cierra la monitorizacion, dejando todo en su estado inicial
	 */
	private IOperacionEventObserver gcListener = new IOperacionEventObserver() {

		@Override
		public void actionPerformed(OperacionEvent event) {
			if (0 == activas.size()) {
				listeners = new LinkedList<IOperacionEventObserver>();
				OperacionEventSubject.getIOperacionEventSubject()
						.removeListener(this);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(GestorMonitorizacion.class.getSimpleName()).append(
					".gcListener={}");
			return sb.toString();
		}
	};

	/**
	 * Constructor
	 */
	public GestorMonitorizacion() {
		this.activas = new ConcurrentHashMap<UUID, CallStatus>();
		this.listeners = new LinkedList<IOperacionEventObserver>();
		this.others = new LinkedList<IOperacionEventObserver>();
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<init>()");
		}
		try {
			iGestorFinalizadas = new GestorFinalizadasProxy();
		} catch (Exception e) {
			logger.info("No se ha encontrado un gestor de Finalizadas");
		}
		if (null == iGestorFinalizadas) {
			iGestorFinalizadas = new IGestorFinalizadas() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#send(com
				 *      .farmafene.aurius.mngt.CallStatus)
				 */
				@Override
				public void send(CallStatus source) {
					if (logger.isDebugEnabled()) {
						logger.debug("IGestorMonitorizacion.iGestorFinalizadas.send(...)");
					}
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#removeListener
				 *      (com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
				 */
				@Override
				public boolean removeListener(ITxFinalizadaListener listener) {
					return false;
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#addListener
				 *      (com.farmafene.aurius.mngt.impl.ITxFinalizadaListener)
				 */
				@Override
				public void addListener(ITxFinalizadaListener listener) {

				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#start()
				 */
				@Override
				public void start() {
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.mngt.impl.IGestorFinalizadas#stop()
				 */
				@Override
				public void stop() {
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
				 */
				@Override
				public String getImplementationVentor() {
					return "Farmafene";
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
				 */
				@Override
				public String getImplementationVersion() {
					return "1.0";
				}

				/**
				 * {@inheritDoc}
				 * 
				 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
				 */
				@Override
				public String getImplementationDescription() {
					return "Dummy Implementation";
				}
			};
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("state=").append(state);
		sb.append(", número de listeners=").append(this.listeners.size());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Ha terminado la llamada, la guardamos.
	 * 
	 * @param source
	 */
	private void toFinalizadas(CallStatus source) {
		iGestorFinalizadas.send(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IGestorMonitorizacion#addListener(IOperacionEventObserver)
	 * @since 1.0.0
	 */
	public void addListener(IOperacionEventObserver listener) {
		if (listener != null) {
			synchronized (this) {
				if (isStarted()) {
					List<IOperacionEventObserver> nuevo = new LinkedList<IOperacionEventObserver>(
							listeners);
					nuevo.add(listener);
					this.listeners = nuevo;
				}
				others.add(listener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IGestorMonitorizacion#removeListener(IOperacionEventObserver)
	 * @since 1.0.0
	 */
	public boolean removeListener(IOperacionEventObserver listener) {
		List<IOperacionEventObserver> nuevo = null;
		boolean antiguo = false;
		if (listener != null) {
			synchronized (this) {
				if (isStarted()) {
					nuevo = new LinkedList<IOperacionEventObserver>(listeners);
					nuevo.remove(listener);
					this.listeners = nuevo;
				}
				antiguo = others.remove(listener);
			}
		}
		return antiguo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IGestorMonitorizacion#fire(MonitorizacionEvent)
	 * @since 1.0.0
	 */
	public void fire(OperacionEvent event) {

		if (event == null) {
			logger.warn("fire('null')");
			return;
		}
		if (event.getSource() == null) {
			logger.warn("fire('null Source')");
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("fire('" + event + "')");
		}
		List<IOperacionEventObserver> actual = this.listeners;
		for (IOperacionEventObserver listener : actual) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invocando: " + listener);
			}
			try {
				listener.actionPerformed(event);
			} catch (Throwable e) {
				logger.warn("Error en la ejecución del listener: " + listener
						+ ".notify(" + event + ")", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IGestorMonitorizacion#isStarted()
	 * @since 1.0.0
	 */
	public boolean isStarted() {
		synchronized (this) {
			return state;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.GestorMonitorizacion#start()
	 */
	public void start() {
		logger.debug("start()");
		synchronized (this) {

			if (!state) {
				try {
					List<IOperacionEventObserver> nuevo = new LinkedList<IOperacionEventObserver>();
					nuevo.add(beginListener);
					nuevo.add(processListener);
					nuevo.addAll(others);
					OperacionEventSubject.getIOperacionEventSubject()
							.addListener(this);
					listeners = nuevo;
					state = true;
					iGestorFinalizadas.start();
				} catch (Throwable th) {
					logger.error(
							"Ha sido imposible enlazar con los drivers!!!!", th);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.GestorMonitorizacion#stop()
	 */
	public void stop() {
		logger.debug("stop()");
		synchronized (this) {
			iGestorFinalizadas.stop();
			List<IOperacionEventObserver> nuevo = new LinkedList<IOperacionEventObserver>();
			nuevo.add(processListener);
			nuevo.addAll(others);
			nuevo.add(gcListener);
			state = false;
		}
	}

	/**
	 * Manda los eventos del Driver al gestor de eventos.
	 * 
	 * @see com.farmafene.aurius.mngt.IOperacionEventObserver#actionPerformed(com.farmafene.aurius.mngt.OperacionEvent)
	 */
	@Override
	public void actionPerformed(OperacionEvent event) {
		if (isTransaccionActiva()) {
			fire(event);
		} else {
			logger.debug("Recibido evento expureo: {}", event);
		}
	}

	/**
	 * Obtiene, si está activa la llamada que ha producido el evento
	 * 
	 * @return llamada actual, <code>null</code> si no existe.
	 */
	private CallStatus getCurrentCall() {
		CallStatus salida = null;
		final Contexto ctx = ContextoLocator.getContexto();
		if (null != ctx) {
			salida = activas.get(ctx.getId());
		}
		return salida;
	}

	/**
	 * Obtiene si existe una llama activa en el thead actual
	 * 
	 * @return si existe o no existe
	 */
	private boolean isTransaccionActiva() {
		boolean salida = false;
		CallStatus root = getCurrentCall();
		if (null != root) {
			salida = true;
		}
		return salida;
	}
}
