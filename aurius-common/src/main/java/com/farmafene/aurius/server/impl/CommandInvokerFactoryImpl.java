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
package com.farmafene.aurius.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.server.Command;
import com.farmafene.aurius.server.CommandInvoker;
import com.farmafene.aurius.server.ICommandInvokerFactory;

/**
 * Implementación por defecto de la Factoría de Invocadora de comandos
 * 
 * @see Command
 * @author vlopez
 * @since 1.0.0
 * 
 */
public class CommandInvokerFactoryImpl implements ICommandInvokerFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(CommandInvokerFactoryImpl.class);

	/**
	 * 
	 * Constructor
	 * 
	 * @see ICommandInvokerFactory
	 * @since 1.0.0
	 */
	public CommandInvokerFactoryImpl() {
		logger.debug(this + "<init>()");
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
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see ICommandInvokerFactory#getCommandInvoker(String)
	 */
	public CommandInvoker getCommandInvoker(String idServicio)
			throws IllegalArgumentException {
		return getCommandInvoker(idServicio, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see ICommandInvokerFactory#getCommandInvoker(String, String)
	 */
	public CommandInvoker getCommandInvoker(String idServicio, String version)
			throws IllegalArgumentException {

		return new CommandInvokerImpl(idServicio, version);
	}
}
