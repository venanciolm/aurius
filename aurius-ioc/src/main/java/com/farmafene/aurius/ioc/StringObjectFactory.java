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
package com.farmafene.aurius.ioc;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * ObjectFactory para la publicación en JNDI de objetos de configuración
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
public class StringObjectFactory implements ObjectFactory {

	private static final Logger logger = Logger
			.getLogger(StringObjectFactory.class.getName());

	public StringObjectFactory() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(this + "<init>");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Object getObjectInstance(Object reference, Name binding,
			Context ctx, Hashtable<?, ?> enviroment) throws Exception {
		Throwable th = null;
		try {
			Reference ref = (Reference) reference;
			String value = (String) ref.get("value").getContent();

			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("'" + binding + "'->'" + ref.getClassName()
						+ "' (" + value + ")");
			}
			Class<?> clazz = createClass(ref.getClassName());
			Constructor<?> constructor = clazz.getConstructor(String.class);
			return constructor.newInstance(value);

		} catch (Throwable e) {
			th = e;
		}
		logger.log(Level.SEVERE,
				"Se ha producido una excepcion incontrolada: ", th);
		throw new RuntimeException(th);
	}

	/**
	 * @param className
	 * @return
	 * @since 1.0.0
	 */
	private Class<?> createClass(String className) {
		Class<?> clazz = null;
		ClassLoader tcl = Thread.currentThread().getContextClassLoader();
		if (tcl != null) {
			try {
				clazz = tcl.loadClass(className);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "ClassNotFoundException:", e);
			}
		}
		if (clazz == null) {
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "ClassNotFoundException:", e);
			}
		}
		return clazz;
	}

}
