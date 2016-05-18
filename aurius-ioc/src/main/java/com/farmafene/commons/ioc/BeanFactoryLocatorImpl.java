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
package com.farmafene.commons.ioc;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;

class BeanFactoryLocatorImpl implements IBeanFactoryLifecycleListener,
		IBeanFactoryManager {
	private static final String RESOURCES = "META-INF/services/"
			+ IBeanFactoryFactory.class.getCanonicalName();

	private Stack<IBeanFactory> factories;
	private boolean permitTestMode = true;

	BeanFactoryLocatorImpl() {
		factories = new Stack<IBeanFactory>();
		try {
			factories
					.push(new com.farmafene.commons.ioc.impl.BeanFactoryImpl());
		} catch (Throwable e)/* ClassNotFoundException or NoClassDefFoundError? */{
			factories.push(new BeanFactorySustitute());
		}
		factories.peek().addIBeanFactoryLifecycleListener(this);
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
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactoryLifecycleListener#notifyInit(com
	 *      .farmafene.commons.ioc.IBeanFactory)
	 */
	@Override
	public void notifyInit(IBeanFactory source) {
		if (source == factories.get(0)) {
			permitTestMode = false;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactoryLifecycleListener#notifyDestroy
	 *      (com.farmafene.commons.ioc.IBeanFactory)
	 */
	@Override
	public void notifyDestroy(IBeanFactory source) {
		verifySource(source);
		IBeanFactory item = factories.pop();
		item.removeIBeanFactoryLifecycleListener(this);
	}

	private void verifySource(IBeanFactory source) {
		if (!factories.peek().equals(source)) {
			throw new IllegalStateException("Illegal Factory: " + source
					+ " vs. " + factories.peek() + ".");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactoryManager#setTestMode(com.farmafene
	 *      .commons.ioc.IBeanFactoryParams)
	 */
	@Override
	public void setTestMode(IBeanFactoryParams testParams) {
		if (permitTestMode) {
			IBeanFactory obtain = search(testParams);
			if (null == obtain) {
				throw new IllegalStateException("Factory not configured!");
			}
		} else {
			throw new IllegalStateException("Not permited test Mode!");
		}
	}

	/**
	 * 
	 * @see IBeanFactoryFactory
	 */
	public IBeanFactory getIBeanFactory() {
		return factories.peek();
	}

	private IBeanFactory search(IBeanFactoryParams testParams) {

		IBeanFactory search = null;
		Enumeration<URL> resources;
		try {
			resources = IBeanFactoryFactory.class.getClassLoader()
					.getResources(RESOURCES);
			URL u = null;
			mainLoop: while (resources.hasMoreElements()) {
				u = resources.nextElement();
				System.out.println(" - "+u);
				Properties p = new Properties();
				p.load(u.openStream());
				for (Object k : p.keySet()) {
					String key = (String) k;
					System.out.println("   - "+key);
					IBeanFactoryFactory factory = ((IBeanFactoryFactory) Class
							.forName(key).newInstance());
					if (factory.supports(testParams)) {
						search = factory.getIBeanFactory(testParams, this);
						factories.push(search);
						break mainLoop;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error in search", e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error in search", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error in search", e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error in search", e);
		}
		return search;
	}
}
