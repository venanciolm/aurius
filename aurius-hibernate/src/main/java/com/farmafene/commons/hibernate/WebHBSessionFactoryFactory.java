/*
 * Copyright (c) 2009-2010 farmafene.com
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebHBSessionFactoryFactory extends AbstractHBSessionFactoryFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(WebHBSessionFactoryFactory.class);

	public WebHBSessionFactoryFactory() {
		super();
		logger.debug("<init>");
	}
	public URL[] getURLResources(ClassLoader cl) {
		if (cl == null) {
			throw new IllegalArgumentException(
					"El ClassLoader no puede ser Null.");
		}
		URL urlResources = cl.getResource("/");
		if (null == urlResources
				|| !urlResources.toString().endsWith("/WEB-INF/classes/")) {
			throw new IllegalArgumentException("No se trata de una WebApp["
					+ urlResources + "]");
		}
		List<URL> urls = new LinkedList<URL>();
		urls.add(urlResources);
		File urlF = new File(urlResources.getFile());
		File libs = new File(urlF.getParentFile(), "lib");
		File[] files = libs.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				boolean salida = false;
				if (file.isFile()
						&& (file.getName().endsWith(".jar") || file.getName()
								.endsWith(".zip"))) {
					salida = true;
				}
				return salida;
			}
		});
		for (File f : files) {
			try {
				URL url;
				url = new URL("file://" + f.getAbsolutePath());
				urls.add(url);
			} catch (MalformedURLException e) {
				logger.error("Error en el fichero: " + f.getPath(), e);
			}
		}
		return urls.toArray(new URL[] {});
	}
}
