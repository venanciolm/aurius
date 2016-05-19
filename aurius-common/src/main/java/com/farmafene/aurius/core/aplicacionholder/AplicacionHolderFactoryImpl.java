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
package com.farmafene.aurius.core.aplicacionholder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.AplicacionHolderPluginFactoriesLocator;
import com.farmafene.aurius.core.AuriusContainerSubject;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IAplicacionHolderFactory;
import com.farmafene.aurius.core.IAplicacionHolderPlugin;
import com.farmafene.aurius.core.IAplicacionHolderPluginFactory;
import com.farmafene.aurius.core.IAuriusContainerObserver;
import com.farmafene.aurius.core.Servicio;

public class AplicacionHolderFactoryImpl implements IAplicacionHolderFactory,
		IAuriusContainerObserver {

	private static final String WATCHTASK_NAME = "CLASSLOADER TASK";
	private static final Logger logger = LoggerFactory
			.getLogger(IAplicacionHolderFactory.class);

	private Map<String, IAplicacionHolder> aplicaciones;
	private Map<String, String> semaforos;
	private ClassLoader parent;
	private WatchTask watchTask;

	public AplicacionHolderFactoryImpl() {
		this.aplicaciones = new ConcurrentHashMap<String, IAplicacionHolder>();
		this.semaforos = new HashMap<String, String>();
		this.parent = Thread.currentThread().getContextClassLoader();
		this.watchTask = new WatchTask();
		new WatchTaskWork(this.watchTask, WATCHTASK_NAME).start();
		AuriusContainerSubject.add(this);
	}

	public void stop() {
		if (watchTask != null) {
			watchTask.setRunning(false);
			watchTask = null;
		}
		for (IAplicacionHolder h : aplicaciones.values()) {
			try {
				h.close();
			} catch (IOException e) {
				logger.error("Error al cerrar el holder: " + h);
			}
		}
		AuriusContainerSubject.remove(this);
	}

	private IAplicacionHolder getHolder(String app) {
		if (null == app) {
			throw new IllegalArgumentException(
					"El identificador no puede ser null");
		}
		ContextoCore ctx = ContextoManager.getContexto();
		IAplicacionHolder holder = ctx.getIAplicacionHolders().get(app);
		if (holder == null) {
			synchronized (getSemaforo(app)) {
				holder = aplicaciones.get(app);
				if (null == holder) {
					AplicacionHolder nHolder = new AplicacionHolder();
					nHolder.setAplicacion(app);
					nHolder.setParent(parent);
					nHolder.addIJarListener(watchTask);
					if (logger.isDebugEnabled()) {
						logger.debug("Creado el contenedor de aplicaciones");
					}
					List<IAplicacionHolderPluginFactory> pluginFactories = AplicacionHolderPluginFactoriesLocator
							.getIAplicacionHolderPluginFactories();
					if (pluginFactories != null) {
						for (IAplicacionHolderPluginFactory f : pluginFactories) {
							IAplicacionHolderPlugin p = f.newInstance(nHolder);
							nHolder.addPlugin(p);
						}
					}
					nHolder.init();
					aplicaciones.put(app, nHolder);
					holder = nHolder;
				}
				holder.use();
				ctx.getIAplicacionHolders().put(app, holder);
			}
		}
		return holder;
	}

	private synchronized String getSemaforo(String app) {
		String salida = semaforos.get(app);
		if (salida == null) {
			semaforos.put(app, app);
			salida = app;
		}
		return salida;
	}

	@Override
	public IAplicacionHolder getHolder(Servicio svr) {

		if (svr == null) {
			throw new IllegalArgumentException(
					"El servicio no debe ser null...");
		}
		return getHolder(svr.getIdAplicacion());
	}

	public Collection<IAplicacionHolder> getAplicacionHolders() {

		return aplicaciones.values();
	}

	@Override
	public void close(IAplicacionHolder holder) {
		synchronized (getSemaforo(holder.getAplicacion())) {
			aplicaciones.remove(holder.getAplicacion());
			try {
				holder.close();
			} catch (IOException e) {
				logger.error("Error al cerrar " + holder);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderFactory#dispose()
	 */
	@Override
	public void dispose() {
		ContextoCore ctx = ContextoManager.getContexto();
		if (logger.isDebugEnabled()) {
			logger.debug("dispose()");
			logger.debug("  - Contexto: " + ctx);
			logger.debug("  - ClassLoaders: "
					+ ctx.getIAplicacionHolders().size());
		}
		for (IAplicacionHolder holder : ctx.getIAplicacionHolders().values()) {
			holder.release();
		}
		ctx.getIAplicacionHolders().clear();
	}

	/**
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#start()
	 */
	@Override
	public void start() {
	}

	/**
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#startCluster()
	 */
	@Override
	public void startCluster() {
	}

	/**
	 * @see com.farmafene.aurius.core.IAuriusContainerObserver#stopCluster()
	 */
	@Override
	public void stopCluster() {
	}
}
