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

import com.farmafene.aurius.mngt.IOperacionEventObserver;
import com.farmafene.aurius.mngt.OperacionEvent;

/**
 * Definición del gestor de monitorización
 * 
 * @author vlopez
 * @since 1.0.0
 */
public interface IGestorMonitorizacion {

	/**
	 * Añade un listener de transacciones.
	 * 
	 * @param listener
	 *            Listener a introducir.
	 * @since 1.0.0
	 */
	public abstract void addListener(IOperacionEventObserver listener);

	/**
	 * Elimina un listener de transacciones activas (el primero en ejecutarse).
	 * 
	 * @param listener
	 *            Listener a añadir
	 * @return <code>true</code> si fue posible <code></code> false en caso
	 *         contrario
	 * @since 1.0.0
	 */
	public abstract boolean removeListener(IOperacionEventObserver listener);

	/**
	 * Env�a un evento de una transacción activa.
	 * 
	 * @param event
	 *            evento a introducir.
	 * @since 1.0.0
	 */
	public abstract void fire(OperacionEvent event);

	/**
	 * Ordena la parada de la monitorización
	 * 
	 * @since 1.0.0
	 */
	public abstract void stop();

	/**
	 * Ordena el arranque de la monitorización
	 * 
	 * @since 1.0.0
	 */
	public abstract void start();

	/**
	 * Indica si el m�dulo de monitorización está o no funcionando
	 * 
	 * @return <code>true</code> si el módulo está monitorizando;
	 *         <code>false</code> en caso contrario.
	 * @since 1.0.0
	 */
	public abstract boolean isStarted();
}
