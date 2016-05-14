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
package com.farmafene.aurius.server;

import com.farmafene.aurius.server.impl.CommandInvokerFactoryImpl;
import com.farmafene.aurius.util.ProxyFactory;

/**
 * Factoria para la obtención de {@link CommandInvoker}
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class CommandInvokerFactory {

	/**
	 * Factoría protegida. Envía un proxy de los commandInvokers
	 */
	private static final ICommandInvokerFactory factory = new ICommandInvokerFactory() {

		private ICommandInvokerFactory instance = new CommandInvokerFactoryImpl();
		private ProxyFactory<CommandInvoker> proxyFactory = new ProxyFactory<CommandInvoker>();

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.ICommandInvokerFactory#getCommandInvoker(java.lang.String,
		 *      java.lang.String)
		 */
		@Override
		public CommandInvoker getCommandInvoker(String idServicio,
				String version) throws IllegalArgumentException {
			return proxyFactory.newInstance(instance.getCommandInvoker(
					idServicio, version));
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see com.farmafene.aurius.server.ICommandInvokerFactory#getCommandInvoker(java.lang.String)
		 */
		@Override
		public CommandInvoker getCommandInvoker(String idServicio)
				throws IllegalArgumentException {
			return proxyFactory.newInstance(instance
					.getCommandInvoker(idServicio));
		}
	};

	private CommandInvokerFactory() {

	}

	/**
	 * Obtiene la factoría actual activa
	 * 
	 * @return factoría activa
	 * @since 1.0.0
	 */
	public static ICommandInvokerFactory getICommandInvokerFactory() {
		return factory;
	}

	/**
	 * Método factoría de {@link CommandInvoker}
	 * 
	 * @param idServicio
	 *            Idenfiticador del servicio
	 * @return El invocador de ese servicio
	 * @throws IllegalArgumentException
	 *             los parámetros solicitados son inválidos
	 * @return factoría activa
	 * @since 1.0.0
	 * @see ICommandInvokerFactory#getCommandInvoker(Contexto, String)
	 */
	public static CommandInvoker getCommandInvoker(String idServicio)
			throws IllegalArgumentException {
		return getICommandInvokerFactory().getCommandInvoker(idServicio);
	}

	/**
	 * Método factoría de {@link CommandInvoker}
	 * 
	 * @param idServicio
	 *            Idenfiticador del servicio
	 * @param version
	 *            Idenfiticador de la versión del servicio
	 * @return El invocador de ese servicio
	 * @throws IllegalArgumentException
	 *             los parámetros solicitados son inválidos
	 * @return factoría activa
	 * @see ICommandInvokerFactory#getCommandInvoker(Contexto, String, String)
	 * 
	 */
	public static CommandInvoker getCommandInvoker(String idServicio,
			String version) throws IllegalArgumentException {
		return getICommandInvokerFactory().getCommandInvoker(idServicio,
				version);
	}
}
