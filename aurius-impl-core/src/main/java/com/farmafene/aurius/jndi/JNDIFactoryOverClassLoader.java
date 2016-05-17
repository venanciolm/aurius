/*
 * Copyright (c) 2009-2012 farmafene.com
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
package com.farmafene.aurius.jndi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IClassLoaderWithHolder;

/**
 * @author vlopez@farmafene.com
 * 
 */
public class JNDIFactoryOverClassLoader implements InitialContextFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(JNDIFactoryOverClassLoader.class);

	class ContextHandler implements InvocationHandler {
		private Context context;
		private ClassLoader classLoader;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
		 *      java.lang.reflect.Method, java.lang.Object[])
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Object invoke = null;
			ClassLoader actual = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(classLoader);
			try {
				logger.debug("ClassLoader: " + classLoader);
				invoke = method.invoke(context, args);
				if (invoke instanceof Context) {
					ContextHandler h = new ContextHandler();
					h.setContext((Context) invoke);
					h.setClassLoader(classLoader);
					invoke = (Context) Proxy.newProxyInstance(classLoader,
							new Class<?>[] { Context.class }, h);

				}
			} catch (Throwable th) {
				logger.info("Error en el Invoke", th);
			} finally {
				Thread.currentThread().setContextClassLoader(actual);
			}
			logger.debug("Retornando: "+ invoke);
			return invoke;
		}

		/**
		 * @return el classLoader
		 */
		public ClassLoader getClassLoader() {
			return classLoader;
		}

		/**
		 * @param classLoader
		 *            el classLoader a establecer
		 */
		public void setClassLoader(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		/**
		 * @return el context
		 */
		public Context getContext() {
			return context;
		}

		/**
		 * @param context
		 *            el context a establecer
		 */
		public void setContext(Context context) {
			this.context = context;
		}

	}

	public JNDIFactoryOverClassLoader() {
		logger.debug(this + "<init>");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("={");
		builder.append("}");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.naming.spi.InitialContextFactory#getInitialContext(java.util.Hashtable)
	 */
	@Override
	public Context getInitialContext(Hashtable<?, ?> environment)
			throws NamingException {

		if (logger.isDebugEnabled()) {
			logger.debug("Environment: " + environment);
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (IClassLoaderWithHolder.class.isAssignableFrom(cl.getClass())) {
			Thread.currentThread().setContextClassLoader(
					((IClassLoaderWithHolder) cl).getContainerClassLoader());
		}
		try {
			Context ctx = new InitialContext();
			ContextHandler h = new ContextHandler();
			if (logger.isDebugEnabled()) {
				logger.debug("Ctx: " + ctx);
			}
			h.setContext(ctx);
			h.setClassLoader(Thread.currentThread().getContextClassLoader());
			return (Context) Proxy.newProxyInstance(Thread.currentThread()
					.getContextClassLoader(), new Class<?>[] { Context.class },
					h);
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}
}