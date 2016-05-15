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
package com.farmafene.aurius.core;

import com.farmafene.aurius.mngt.OperacionEvent;
import com.farmafene.aurius.util.ProxyFactory;

/**
 * Utilidad para la gestión de eventos de Monitorización del sistema.
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class GestorMonitorizacion {
	private static final IGestorMonitorizacion manager;
	static {
		ProxyFactory<IGestorMonitorizacion> proxyFactory = new ProxyFactory<IGestorMonitorizacion>();
		proxyFactory.setInterfaces(IGestorMonitorizacion.class);
		manager = proxyFactory
				.newInstance(new com.farmafene.aurius.mngt.impl.GestorMonitorizacion());
	}

	/**
	 * Constructor privado
	 * 
	 * @since 1.0.0
	 */
	private GestorMonitorizacion() {

	}

	/**
	 * Obtiene una instancia del Mediador de transacciones activas.
	 * 
	 * @return Instancia del Mediador de Transacciones activas.
	 * @since 1.0.0
	 */
	public static IGestorMonitorizacion getIGestorMonitorizacion() {
		return manager;
	}

	/**
	 * Envía un evento de una transacción activa.
	 * 
	 * @param event
	 *            evento a introducir.
	 * @see IGestorMonitorizacion#notify()
	 * @since 1.0.0
	 */
	public static void fire(OperacionEvent event) {
		getIGestorMonitorizacion().fire(event);
	}
}
