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
package com.farmafene.commons.ioc.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringApplicationContextFactory {

	private static final String RESOURCES = "META-INF/services/"
			+ ISpringConfigurableApplicationContext.class.getCanonicalName();

	private static final Logger logger = LoggerFactory
			.getLogger(SpringApplicationContextFactory.class);

	public SpringApplicationContextFactory() {

	}

	public ISpringConfigurableApplicationContext getISpringConfigurableApplicationContext() {
		ISpringConfigurableApplicationContext factory = search();
		if (factory == null) {
			factory = new SpringConfigurableApplicationContextDefault();
		}
		return factory;
	}

	private ISpringConfigurableApplicationContext search() {

		ISpringConfigurableApplicationContext search = null;
		Enumeration<URL> resources;
		try {
			resources = SpringApplicationContextFactory.class.getClassLoader()
					.getResources(RESOURCES);
			URL u = null;
			mainLoop: while (resources.hasMoreElements()) {
				u = resources.nextElement();
				if (logger.isDebugEnabled()) {
					logger.debug("   - {}", u);
				}
				Properties p = new Properties();
				p.load(u.openStream());
				for (Object k : p.keySet()) {
					String key = (String) k;
					if (logger.isDebugEnabled()) {
						logger.debug("   - {}", key);
					}
					search = ((ISpringConfigurableApplicationContext) Class
							.forName(key).newInstance());
					break mainLoop;
				}
			}
		} catch (IOException e) {
			logger.error("", e);
			throw new IllegalStateException("Error in search", e);
		} catch (InstantiationException e) {
			logger.error("", e);
			throw new IllegalStateException("Error in search", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
			throw new IllegalStateException("Error in search", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
			throw new IllegalStateException("Error in search", e);
		}
		return search;
	}
}
