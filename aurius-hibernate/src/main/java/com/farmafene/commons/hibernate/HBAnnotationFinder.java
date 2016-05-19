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
package com.farmafene.commons.hibernate;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean de obtención de clases de Hibernate
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
public class HBAnnotationFinder {
	private static final Logger logger = LoggerFactory
			.getLogger(HBAnnotationFinder.class);
	private URL[] urls;
	private IAnnotationHolder holder;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public HBAnnotationFinder() {
		holder = new HBAnnotationHolder();
	}

	/**
	 * Método inicial a invocar para la obtención de los datos para generar las
	 * factorías de Hibernate
	 * 
	 * @since 1.0.0
	 */
	public void find() {
		if (null == urls) {
			throw new IllegalArgumentException("Urls debe ser inicializado");
		}
		if (null == holder) {
			throw new IllegalArgumentException(
					"classLoder debe ser inicializado");
		}
		for (URL url : urls) {
			find(url);
		}
	}

	/**
	 * Obtiene las clases anotadas de una determinada URL
	 * 
	 * @param url
	 * @since 1.0.0
	 */
	private void find(URL url) {
		File file = new File(url.getFile());

		if (file.isDirectory()) {
			findDirectory(url, file);
		} else if (file.getName().endsWith(".jar")
				|| file.getName().endsWith(".zip")) {
			logger.debug("Tratando: " + file.getName());
			findJarFile(file);
		} else {
			logger.info("el fichero: " + file.getName()
					+ ", no es un fichero tratable.");
		}
	}

	/**
	 * Obtiene las clases anotadas de un fichero Jar
	 * 
	 * @param url
	 * @since 1.0.0
	 */
	private void findJarFile(File file) {
		JarInputStream jarFile = null;
		try {
			try {
				jarFile = new JarInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("El fichero >"
						+ file.getAbsolutePath() + "<  no existe.", e);
			} catch (IOException e) {
				throw new IllegalArgumentException("El fichero >"
						+ file.getAbsolutePath() + "<  ha producido un error.",
						e);
			}
			JarEntry jarEntry = null;
			while (true) {
				try {
					jarEntry = jarFile.getNextJarEntry();
				} catch (IOException e) {
					logger.error(e.getMessage());
					throw new RuntimeException("Error al procesar el jar."
							+ e.getMessage());
				}
				if (jarEntry == null) {
					break;
				}
				holder.addIfIsValid(jarEntry.getName());
				try {
					jarFile.closeEntry();
				} catch (IOException e) {
					logger.warn("Error al cerrar la entrada del Jar", e);
				}
			}
		} finally {
			if (null != jarFile) {
				try {
					jarFile.close();
				} catch (IOException e) {
					logger.warn("Error al cerrar el Jar", e);
				}
			}
		}
	}

	/**
	 * Obtiene las clases anotadas contenidas en un directorio de clases.
	 * 
	 * @param url
	 * @since 1.0.0
	 */
	private void findDirectory(URL urlBase, File file) {
		if (!file.isDirectory()) {
			throw new IllegalArgumentException(
					"Se intenta buscar anotaciones en un fichero que no es un directorio");
		}
		File[] contents = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				boolean salida = false;
				if (file.isDirectory() || file.getName().endsWith(".class")) {
					salida = true;
				}
				return salida;
			}
		});
		for (File elem : contents) {
			try {
				if (elem.isDirectory()) {
					findDirectory(urlBase, elem);
				}
				URL urlFile = new URL("file://" + elem.getAbsolutePath());
				String resource = urlFile.toString().substring(
						urlBase.toString().length() + 1);
				holder.addIfIsValid(resource);
			} catch (MalformedURLException e) {
				logger.warn("El archivo " + elem.getPath()
						+ ", está mal formado.", e);
			}

		}
	}

	/**
	 * URLs dónde se buscarán las clases anotadas de hibernate
	 * 
	 * @param urls
	 *            the urls to set
	 * @since 1.0.0
	 */
	public void setUrls(URL[] urls) {
		this.urls = urls;
	}

	/**
	 * @return the holder
	 */
	public IAnnotationHolder getHolder() {
		return holder;
	}

	/**
	 * @param holder
	 *            the holder to set
	 */
	public void setHolder(IAnnotationHolder holder) {
		this.holder = holder;
	}
}
