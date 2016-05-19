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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassLoaderHBSessionFactoryFactory extends
		AbstractHBSessionFactoryFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(ClassLoaderHBSessionFactoryFactory.class);

	public ClassLoaderHBSessionFactoryFactory() {
		super();
		logger.debug(this+"<init>");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
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
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getURLResources(java.lang.ClassLoader)
	 */
	public URL[] getURLResources(ClassLoader cl) {
		if (cl == null) {
			throw new IllegalArgumentException(
					"El ClassLoader no puede ser Null.");
		}
		Set<URL> urls = new HashSet<URL>();
		Enumeration<URL> esr;
		try {
			esr = cl.getResources("META-INF");
			while (esr.hasMoreElements()) {
				URL url = esr.nextElement();
				String file = url.toString();
				if (file.startsWith("jar:file:")) {
					String newPath = url.getPath();
					int l = -1;
					URL a = null;
					if ((l = newPath.indexOf("!")) > 0) {
						a = new URL(url.getPath().substring(0, l));
					} else {
						a = new URL(url.getPath());
					}
					logger.info("URL: " + a);
					urls.add(a);
				}
			}
		} catch (IOException e) {
			logger.error("IOException: ", e);
		}
		return urls.toArray(new URL[] {});
	}
}
