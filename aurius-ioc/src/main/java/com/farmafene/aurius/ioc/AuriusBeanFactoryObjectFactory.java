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
public class AuriusBeanFactoryObjectFactory implements ObjectFactory {

	private static final Logger logger = Logger
			.getLogger(AuriusBeanFactoryObjectFactory.class.getName());

	public AuriusBeanFactoryObjectFactory() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(this + "<init>");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObjectInstance(Object reference, Name binding,
			Context ctx, Hashtable<?, ?> enviroment) throws Exception {
		Object getObjectInstance = null;
		if (AuriusBeanFactory.getIAuriusBeanFactory().isInit()) {
			Reference ref = (Reference) reference;
			String value = (String) ref.get("uniqueName").getContent();
			try {
				getObjectInstance = AuriusBeanFactory.getBean(value);
			} catch (Throwable th) {
				logger.log(Level.SEVERE, "Excepción en la ObjectFactory", th);
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Retornando: " + getObjectInstance);
		}
		return getObjectInstance;
	}
}
