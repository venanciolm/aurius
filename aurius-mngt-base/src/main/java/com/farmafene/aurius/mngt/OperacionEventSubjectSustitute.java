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
package com.farmafene.aurius.mngt;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementación de Base de observador
 * 
 * @author vlopez
 * @since 1.0.0
 */
class OperacionEventSubjectSustitute implements IOperacionEventSubject {

	private List<IOperacionEventObserver> listeners;
	private Boolean semaphoreListeners = Boolean.TRUE;

	public OperacionEventSubjectSustitute() {
		this.listeners = new LinkedList<IOperacionEventObserver>();
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
		sb.append("número de listeners=").append(this.listeners.size());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public void addListener(IOperacionEventObserver listener) {
		synchronized (semaphoreListeners) {
			List<IOperacionEventObserver> nuevo = new LinkedList<IOperacionEventObserver>(
					listeners);
			nuevo.add(listener);
			this.listeners = nuevo;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public boolean removeListener(IOperacionEventObserver listener) {
		List<IOperacionEventObserver> nuevo = null;
		boolean antiguo;
		synchronized (semaphoreListeners) {
			nuevo = new LinkedList<IOperacionEventObserver>(listeners);
			antiguo = nuevo.remove(listener);
			this.listeners = nuevo;
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
			return;
		}
		List<IOperacionEventObserver> actual = this.listeners;
		for (IOperacionEventObserver listener : actual) {
			try {
				listener.actionPerformed(event);
			} catch (Throwable e) {
			}
		}
	}
}
