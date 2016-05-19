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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IAplicacionHolderPlugin;
import com.farmafene.aurius.core.IEstructuraDirectorios;
import com.farmafene.aurius.core.ILifeCicleJarListener;

/**
 * Se encarga de contener los datos de una determinada aplicación
 * 
 * @author vlopez@farmafene.com
 * 
 */
class AplicacionHolder implements IAplicacionHolder {

	private static final Logger logger = LoggerFactory
			.getLogger(AplicacionHolder.class);
	private AuriusClassLoader classLoader;
	private ClassLoader parent;
	private String aplicacion;
	private IEstructuraDirectorios estructuraDirectorios;
	private int threads;
	private boolean isClose;
	private boolean isInit;
	private List<IAplicacionHolderPlugin> plugins;
	private Map<String, IAplicacionHolderPlugin> pluginsMap;
	private List<ILifeCicleJarListener> listeners;

	/**
	 * Constructor por defecto
	 */
	public AplicacionHolder() {
		isClose = true;
		isInit = false;
		plugins = new LinkedList<IAplicacionHolderPlugin>();
		pluginsMap = new HashMap<String, IAplicacionHolderPlugin>();
		listeners = new LinkedList<ILifeCicleJarListener>();
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
		sb.append("aplicacion=").append(aplicacion);
		sb.append(", isClose=").append(isClose);
		sb.append(", threads=").append(threads);
		sb.append(", with ").append(plugins.size()).append(" plugins");
		sb.append(", with ").append(listeners.size())
				.append(" jarListeners activos");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#getEstructuraDirectorios()
	 */
	@Override
	public IEstructuraDirectorios getEstructuraDirectorios() {
		return this.estructuraDirectorios;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		synchronized (this) {
			isClose = true;
			if (threads == 0) {
				destroy();
			}
		}
	}

	private void destroy() throws IOException {
		onDestroy();
		URL[] jars = this.classLoader.getURLs();
		this.classLoader.destroy();
		for (URL uj : jars) {
			release(uj);
		}
		disposed();

	}

	public void init() {
		EstructuraDirectorios estructura = new EstructuraDirectorios();
		estructuraDirectorios = estructura;
		estructura.setAplicacion(aplicacion);
		estructura.init();
		classLoader = new AuriusClassLoader(this, parent);
		URL[] ficheros = compareDirectories();
		isClose = false;
		isInit = true;
		for (URL url : ficheros) {
			classLoader.addURL(url);
			use(url);
		}
		threads = 0;
		ClassLoader initClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(getClassLoader());
			onCreate();
		} finally {
			Thread.currentThread().setContextClassLoader(initClassLoader);
		}
	}

	/**
	 * @return the classLoader
	 */
	public URLClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * @return the aplicacion
	 */
	public String getAplicacion() {
		return aplicacion;
	}

	/**
	 * @param aplicacion
	 *            the aplicacion to set
	 */
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(ClassLoader parent) {
		this.parent = parent;
	}

	public ClassLoader getParent() {
		return this.parent;
	}

	private URL[] compareDirectories() {
		IncominJarFilter filter = new IncominJarFilter();
		filter.setHolder(this);
		filter.init();
		File[] incoming = estructuraDirectorios.getIncoming().listFiles(filter);
		String datePrefix = new SimpleDateFormat(
				IEstructuraDirectorios.TIMESTAMP_FORMAT).format(new Date());

		for (File f : incoming) {
			estructuraDirectorios.moveFile(
					f,
					estructuraDirectorios.getCurrent(),
					f.getName().substring(0, f.getName().length() - 4) + "."
							+ datePrefix
							+ f.getName().substring(f.getName().length() - 4));
		}
		Map<String, File> elegidos = new HashMap<String, File>();
		Set<File> eliminados = new HashSet<File>();
		CurrentJarFilter cFilter = new CurrentJarFilter();
		cFilter.setPrefix(datePrefix);
		cFilter.setHolder(this);
		cFilter.init();
		for (File a : estructuraDirectorios.getCurrent().listFiles(cFilter)) {
			eligeFichero(elegidos, eliminados, a);
		}
		List<URL> filesAcepted = new LinkedList<URL>();
		for (File f : elegidos.values()) {
			try {
				filesAcepted.add(f.toURI().toURL());
			} catch (MalformedURLException e) {
				logger.warn("filesAcepted!!!", e);
			}
		}
		for (File f : eliminados) {
			try {
				move(f.toURI().toURL(), new File(
						estructuraDirectorios.getOld(), f.getName()).toURI()
						.toURL());
			} catch (MalformedURLException e) {
				logger.warn("move!!!", e);
			}
		}
		return filesAcepted.toArray(new URL[0]);
	}

	private String getFileNameFileFromCurrent(File c) {
		int length = c.getName().length();
		return c.getName().substring(0, length - 22).toUpperCase();
	}

	private String getStrTimestampFromFile(File a) {
		String aTimeStamp = a.getName().substring(a.getName().length() - 22,
				a.getName().length() - 4);
		return aTimeStamp;
	}

	private void eligeFichero(Map<String, File> elegidos, Set<File> eliminados,
			File a) {
		String name = getFileNameFileFromCurrent(a);
		if (!elegidos.containsKey(name)) {
			elegidos.put(name, a);
		} else {
			String eTimeStamp = getStrTimestampFromFile(elegidos.get(name));
			String aTimeStamp = getStrTimestampFromFile(a);
			if (aTimeStamp.compareTo(eTimeStamp) > 0) {
				eliminados.add(elegidos.get(name));
				elegidos.put(name, a);
			} else {
				eliminados.add(a);
			}
		}
	}

	public void release() {
		if (logger.isDebugEnabled()) {
			logger.debug(this + ".release()");
		}
		boolean destroy = false;
		synchronized (this) {
			threads--;
			if (0 == threads && isClose) {
				destroy = true;
			}
		}
		if (destroy) {
			try {
				destroy();
			} catch (IOException e) {
				logger.error(
						"release(). Error al destruir el IAplicationHolder", e);
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#use()
	 */
	@Override
	public void use() {
		if (logger.isDebugEnabled()) {
			logger.debug(this + ".use()");
		}
		synchronized (this) {
			if (isClose) {
				throw new IllegalStateException(
						"Error de concurrrencia, el IAplicationHolder est� cerrado para el uso.");
			}
			threads++;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#addPlugin(com.farmafene.aurius.core.IAplicacionHolderPlugin)
	 */
	@Override
	public void addPlugin(IAplicacionHolderPlugin plugin)
			throws IllegalStateException {

		if (isInit) {
			throw new IllegalStateException("El holder '" + this
					+ "', est� iniciado");
		}
		if (null != plugin) {
			plugins.add(plugin);
			if (null != plugin.getPluginName()) {
				if (pluginsMap.containsKey(plugin.getPluginName())) {
					String msg = "El holder '" + this
							+ "', ya cuenta con el plugin '" + plugin + "'";
					logger.error(msg);
					throw new IllegalStateException(msg);
				}
				if (logger.isInfoEnabled()) {
					logger.info("addPlugin(" + plugin.getPluginName() + ","
							+ plugin + ")");
				}
				pluginsMap.put(plugin.getPluginName(), plugin);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#addIJarListener(com.farmafene.aurius.core.ILifeCicleJarListener)
	 */
	@Override
	public void addIJarListener(ILifeCicleJarListener listener) {
		if (null != listener) {
			listeners.add(listener);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("Se est� intentando introducir un listener nulo.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#getPlugin(java.lang.String)
	 */
	@Override
	public IAplicacionHolderPlugin getPlugin(String pluginName) {
		return pluginsMap.get(pluginName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolder#removeIJarListener(com.farmafene.aurius.core.ILifeCicleJarListener)
	 */
	@Override
	public boolean removeIJarListener(ILifeCicleJarListener listener) {
		boolean salida = false;
		if (null != listener) {
			salida = listeners.remove(listener);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("Se est� intentando eliminar un listener nulo.");
			}
		}
		return salida;
	}

	/**
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#release(java.net.URL)
	 */
	private void release(URL jarFile) {
		for (ILifeCicleJarListener l : listeners) {
			try {
				l.release(jarFile);
			} catch (Throwable th) {
				logger.error("Error al notificar release('" + jarFile + "')"
						+ ", " + th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en release('" + jarFile + "')", th);
				}
			}
		}
	}

	/**
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#use(java.net.URL)
	 */
	private void use(URL jarFile) {
		for (ILifeCicleJarListener l : listeners) {
			try {
				l.use(jarFile);
			} catch (Throwable th) {
				logger.error("Error al notificar use('" + jarFile + "')" + ", "
						+ th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en use('" + jarFile + "')", th);
				}
			}
		}
	}

	/**
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#onCreate()
	 */
	private void onCreate() {
		for (IAplicacionHolderPlugin p : plugins) {
			try {
				if (null != p) {
					p.onCreate();
				}
			} catch (Throwable th) {
				logger.error("Error al cerrar el plugin " + p.getPluginName()
						+ ", " + th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en 'onCreate()'", th);
				}
			}
		}
	}

	/**
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPlugin#onDestroy()
	 */
	private void onDestroy() {
		for (IAplicacionHolderPlugin p : plugins) {
			try {
				if (null != p) {
					p.onDestroy();
				}
			} catch (Throwable th) {
				logger.error("Error al cerrar el plugin " + p.getPluginName()
						+ ", " + th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en 'onDestroy()'", th);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#move(java.net.URL,
	 *      java.net.URL)
	 */
	private void move(URL fromJarFile, URL toJarFile) {
		for (ILifeCicleJarListener l : listeners) {
			try {
				l.move(fromJarFile, toJarFile);
			} catch (Throwable th) {
				logger.error("Error al notificar move('" + fromJarFile + "','"
						+ toJarFile + "')" + ", " + th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en move('" + fromJarFile + "','"
							+ toJarFile + "')", th);
				}
			}
		}
	}

	/**
	 * 
	 * @see com.farmafene.aurius.core.ILifeCicleJarListener#disposed(IAplicacionHolder)
	 */
	private void disposed() {
		LinkedList<ILifeCicleJarListener> old = new LinkedList<ILifeCicleJarListener>(
				listeners);
		for (ILifeCicleJarListener l : old) {
			try {
				l.disposed(this);
			} catch (Throwable th) {
				logger.error("Error al notificar disposed('" + this + "')"
						+ ", " + th.getMessage());
				if (logger.isInfoEnabled()) {
					logger.info("Excepcion en disposed('" + this + "')", th);
				}
			}
		}
	}
}
