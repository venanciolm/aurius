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
package com.farmafene.aurius.tm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IClassLoaderWithHolder;
import com.farmafene.aurius.core.ITransactionHelperLocator;

public class UserTransactionLocatorJNDI implements ITransactionHelperLocator {

	private static final Logger logger = LoggerFactory
			.getLogger(UserTransactionLocatorJNDI.class);
	private String userTransactionName;
	private String transactionManagerName;

	public UserTransactionLocatorJNDI() {
		userTransactionName = "java:comp/UserTransaction";
		transactionManagerName = "java:comp/TransactionManager";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName())
				.append("={");
		sb.append("userTransactionName=").append(userTransactionName);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IUserTransactionLocator#getUserTransaction()
	 */
	@Override
	public UserTransaction getUserTransaction() {
		boolean proxy = false;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			if (cl instanceof IClassLoaderWithHolder) {
				Thread.currentThread()
						.setContextClassLoader(
								((IClassLoaderWithHolder) cl)
										.getContainerClassLoader());
				proxy = true;
			}
			Context ctx = new InitialContext();
			logger.debug("Estamos en el proxy {}, classLoader {}", proxy, cl);
			if (proxy) {
				ContextHandler h = new ContextHandler();
				h.setContext(ctx);
				h.setClassLoader(Thread.currentThread().getContextClassLoader());
				ctx = (Context) Proxy.newProxyInstance(Thread.currentThread()
						.getContextClassLoader(),
						new Class<?>[] { Context.class }, h);
			}
			return (UserTransaction) ctx.lookup(userTransactionName);
		} catch (NamingException e) {
			throw new IllegalStateException(
					"No se ha localizado el 'UserTransaction'", e);
		} finally {
			if (null != cl) {
				Thread.currentThread().setContextClassLoader(cl);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.ITransactionHelperLocator#getTransactionManager()
	 */
	@Override
	public TransactionManager getTransactionManager() {
		boolean proxy = false;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			if (cl instanceof IClassLoaderWithHolder) {
				cl = Thread.currentThread().getContextClassLoader();
				Thread.currentThread()
						.setContextClassLoader(
								((IClassLoaderWithHolder) cl)
										.getContainerClassLoader());
				proxy = true;
			}
			logger.debug("Estamos en el proxy {}, classLoader {}", proxy, cl);
			Context ctx = new InitialContext();
			if (proxy) {
				ContextHandler h = new ContextHandler();
				h.setContext(ctx);
				h.setClassLoader(Thread.currentThread().getContextClassLoader());
				ctx = (Context) Proxy.newProxyInstance(Thread.currentThread()
						.getContextClassLoader(),
						new Class<?>[] { Context.class }, h);
			}
			return (TransactionManager) ctx.lookup(transactionManagerName);
		} catch (NamingException e) {
			throw new IllegalStateException(
					"No se ha localizado el 'TransactionManager'", e);
		} finally {
			if (null != cl) {
				Thread.currentThread().setContextClassLoader(cl);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return "Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return "1.0";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return "Localizador por JNDI";
	}

	/**
	 * @return the userTransactionName
	 */
	public String getUserTransactionName() {
		return userTransactionName;
	}

	/**
	 * @param userTransactionName
	 *            the userTransactionName to set
	 */
	public void setUserTransactionName(String userTransactionName) {
		this.userTransactionName = userTransactionName;
	}

	/**
	 * @return the transactionManagerName
	 */
	public String getTransactionManagerName() {
		return transactionManagerName;
	}

	/**
	 * @param transactionManagerName
	 *            the transactionManagerName to set
	 */
	public void setTransactionManagerName(String transactionManagerName) {
		this.transactionManagerName = transactionManagerName;
	}

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
			logger.debug("Retornando: " + invoke);
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
}
