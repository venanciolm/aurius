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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.resource.spi.work.Work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.AplicacionHolderFactory;
import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IAplicacionHolderFactory;
import com.farmafene.aurius.core.ILifeCicleJarListener;

class WatchTask implements Work, ILifeCicleJarListener {

	private static final Logger logger = LoggerFactory
			.getLogger(WatchTask.class);
	private boolean running;
	private Queue<ToMove> toMove;
	private Map<URL, Integer> actuales;
	private Map<URL, URL> reos;

	class ToMove {

		private URL from;
		private URL to;

		private ToMove() {

		}

		public ToMove(URL from, URL to) {
			this();
			this.from = from;
			this.to = to;
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
			sb.append("from=").append(from);
			sb.append(", to=").append(to);
			return sb.toString();
		}

		/**
		 * @return the from
		 */
		public URL getFrom() {
			return from;
		}

		/**
		 * @param from
		 *            the from to set
		 */
		public void setFrom(URL from) {
			this.from = from;
		}

		/**
		 * @return the to
		 */
		public URL getTo() {
			return to;
		}

		/**
		 * @param to
		 *            the to to set
		 */
		public void setTo(URL to) {
			this.to = to;
		}

	}

	public WatchTask() {
		if (logger.isDebugEnabled()) {
			logger.debug("Creada la task para actualizacion de ClassLoaders");
		}
		this.running = true;
		this.toMove = new LinkedBlockingQueue<ToMove>();
		this.actuales = new HashMap<URL, Integer>();
		this.reos = new HashMap<URL, URL>();
	}

	public void setRunning(boolean flag) {
		synchronized (this) {
			this.running = flag;
		}
	}

	public boolean isRunning() {
		synchronized (this) {
			return running;
		}
	}

	public void run() {
		do {
			try {
				Thread.sleep(10000);
				if (logger.isDebugEnabled()) {
					logger
							.debug("Ejecutando task para actualización de classLoaders");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lifeCicleJarListener();
			watchTask();
		} while (running);
	}

	private void watchTask() {

		IAplicacionHolderFactory factory = AplicacionHolderFactory
				.getIAplicacionHolderFactory();
		Collection<IAplicacionHolder> sets = factory.getAplicacionHolders();
		for (IAplicacionHolder holder : sets) {
			if (logger.isDebugEnabled()) {
				logger.debug("Procesando grupo: >" + holder.getAplicacion()
						+ "<");
			}
			if (hayFicherosIncoming(holder)) {
				factory.close(holder);
				if (logger.isDebugEnabled()) {
					logger.debug("Cerrado el holder: " + holder);
				}
			}
		}
	}

	private boolean hayFicherosIncoming(IAplicacionHolder holder) {

		IncominJarFilter filter = new IncominJarFilter();
		filter.setHolder(holder);
		filter.init();
		File[] lista = holder.getEstructuraDirectorios().getIncoming()
				.listFiles(filter);
		if (logger.isDebugEnabled()) {
			logger.debug("lista: " + Arrays.toString(lista) + ", holder="
					+ holder);
		}
		return lista.length > 0;
	}

	public void release() {
		logger.debug("release()");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#disposed(com.farmafene.aurius.core.IAplicacionHolder)
	 */
	@Override
	public void disposed(IAplicacionHolder source) {
		if (null != source) {
			source.removeIJarListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#move(java.net.URL,
	 *      java.net.URL)
	 */
	@Override
	public void move(URL fromJarFile, URL toJarFile) {
		synchronized (this) {
			if (actuales.containsKey(fromJarFile)) {
				Integer value = actuales.get(fromJarFile);
				if (value.intValue() == 0) {
					actuales.remove(fromJarFile);
					toMove.add(new ToMove(fromJarFile, toJarFile));
				} else {
					reos.put(fromJarFile, toJarFile);
				}
			} else {
				toMove.add(new ToMove(fromJarFile, toJarFile));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#release(java.net.URL)
	 */
	@Override
	public void release(URL jarFile) {
		synchronized (this) {
			if (actuales.containsKey(jarFile)) {
				Integer value = actuales.get(jarFile);
				int newValue = value.intValue() - 1;
				if (newValue <= 0) {
					if (reos.containsKey(jarFile)) {
						toMove.add(new ToMove(jarFile, reos.get(jarFile)));
						actuales.remove(jarFile);
						reos.remove(jarFile);
						if (logger.isDebugEnabled()) {
							logger.debug("release('" + jarFile
									+ "'): --> toMove");
						}
					} else {
						actuales.put(jarFile, Integer.valueOf(0));
						if (logger.isDebugEnabled()) {
							logger.debug("release('" + jarFile + "'): 0");
						}
					}
				} else {
					actuales.put(jarFile, Integer.valueOf(newValue));
					if (logger.isDebugEnabled()) {
						logger.debug("release('" + jarFile + "'): " + newValue);
					}
				}
			} else {
				logger.warn("release('" + jarFile + "'), no dado de alta");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#use(java.net.URL)
	 */
	@Override
	public void use(URL jarFile) {
		if (logger.isDebugEnabled()) {
			logger.debug("use('" + jarFile + "')");
		}
		synchronized (this) {
			int newValue = 1;
			if (actuales.containsKey(jarFile)) {
				Integer value = actuales.get(jarFile);
				newValue = value.intValue() + 1;
			}
			actuales.put(jarFile, Integer.valueOf(newValue));
		}
	}

	private void lifeCicleJarListener() {
		ToMove object = null;
		LinkedList<ToMove> errores = new LinkedList<ToMove>();
		while ((object = toMove.poll()) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Moviendo: " + object);
			}
			try {
				File fo = new File(object.getFrom().toURI());
				File fd = new File(object.getTo().toURI());
				boolean renamed = fo.renameTo(fd);
				if (!renamed) {
					logger.warn("No se ha movido: " + object);
					errores.add(object);
				}
			} catch (URISyntaxException e) {
				logger.error("No se puede Mover el archivo!!!", e);
			}
		}
		if (errores.size() > 0) {
			toMove.addAll(errores);
		}
	}
}
