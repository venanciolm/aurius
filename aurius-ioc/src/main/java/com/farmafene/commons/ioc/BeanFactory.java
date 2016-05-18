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

/**
 * 
 * Implementación del localizador de una Bean Factory del sistema
 * 
 * @see IBeanFactory
 * @since 1.0
 */
public abstract class BeanFactory {

	private static final BeanFactoryLocatorImpl iBeanFactoryLocator = new BeanFactoryLocatorImpl();

	private BeanFactory() {
		// do nothing
	}

	/**
	 * Método localizador de la {@link BeanFactory}
	 * 
	 * @return la {@link IBeanFactory} activa en el sistema
	 */
	public static final IBeanFactory getIBeanFactory() {
		return iBeanFactoryLocator.getIBeanFactory();
	}

	public static final IBeanFactoryManager getIBeanFactoryManager() {
		return iBeanFactoryLocator;
	}

	/**
	 * Acceso al método de localización de clases
	 * 
	 * @param clazz
	 *            Interfaz de la implementación que se intenta localizar
	 * @return Implementación activa
	 * @see IBeanFactory#getBean(Class)
	 */
	public static <O> O getBean(Class<O> clazz) {
		return getIBeanFactory().getBean(clazz);
	}

	public static <O> O getBean(Class<O> clazz, String beanId) {
		return getIBeanFactory().getBean(clazz, beanId);
	}

	public static <O> O getBean(String beanId) {
		return getIBeanFactory().getBean(beanId);
	}
}
