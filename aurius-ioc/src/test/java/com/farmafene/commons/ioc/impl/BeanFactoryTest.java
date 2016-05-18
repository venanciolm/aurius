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

import java.util.LinkedList;
import java.util.List;

import com.farmafene.commons.ioc.IBeanFactory;
import com.farmafene.commons.ioc.IBeanFactoryLifecycleListener;

public class BeanFactoryTest implements IBeanFactory {

	private static List<IBeanFactoryLifecycleListener> listeners;

	public BeanFactoryTest() {
		listeners = new LinkedList<IBeanFactoryLifecycleListener>();
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
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class)
	 */
	@Override
	public <O> O getBean(Class<O> clazz) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public <O> O getBean(Class<O> clazz, String beanId) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#getBean(java.lang.String)
	 */
	@Override
	public <O> O getBean(String beanId) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#isInit()
	 */
	@Override
	public boolean isInit() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#destroy()
	 */
	@Override
	public void destroy() {
		List<IBeanFactoryLifecycleListener> items = new LinkedList<IBeanFactoryLifecycleListener>(
				listeners);
		for (IBeanFactoryLifecycleListener l : items) {
			l.notifyDestroy(this);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#addIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public void addIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener observer) {
		listeners.add(observer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.ioc.IBeanFactory#removeIBeanFactoryLifecycleListener(com.farmafene.commons.ioc.IBeanFactoryLifecycleListener)
	 */
	@Override
	public boolean removeIBeanFactoryLifecycleListener(
			IBeanFactoryLifecycleListener observer) {
		return listeners.remove(observer);
	}

	public void notifyInit() {
		List<IBeanFactoryLifecycleListener> items = new LinkedList<IBeanFactoryLifecycleListener>(
				listeners);
		for (IBeanFactoryLifecycleListener l : items) {
			l.notifyInit(this);
		}
	}
}
