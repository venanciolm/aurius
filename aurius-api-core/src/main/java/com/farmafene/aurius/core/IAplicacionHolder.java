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
package com.farmafene.aurius.core;

import java.io.Closeable;

/**
 * @author vlopez@farmafene.com
 * @since 1.0.0
 * @version 1.0.0
 */
public interface IAplicacionHolder extends Closeable {

	/**
	 * Obtiene el nombre de la aplicación
	 * 
	 * @return
	 */
	String getAplicacion();

	/**
	 * Obtiene la estructura de directorios
	 * 
	 * @return
	 */
	IEstructuraDirectorios getEstructuraDirectorios();

	/**
	 * Obtiene el Classloader que contiene la aplicación
	 * 
	 * @return
	 */
	ClassLoader getClassLoader();

	/**
	 * Libera el el Holder después de un uso
	 */
	void release();

	/**
	 * Notifica al holder que está siendo utilizado
	 */
	void use();

	/**
	 * Añade un plugin al {@link IAplicacionHolder}
	 * 
	 * @param plugin
	 *            plugin a añadir
	 * @throws IllegalStateException
	 *             si el holder ya está activo.
	 * @see IAplicacionHolderPluginFactory
	 * @see IAplicacionHolderPluginFactoriesLocator
	 */
	void addPlugin(IAplicacionHolderPlugin plugin) throws IllegalStateException;

	/**
	 * devuelve un Plugin por su nombre
	 * 
	 * @param pluginName
	 * @return el plugin, <code>null</code>, si el plugin no existe.
	 */
	IAplicacionHolderPlugin getPlugin(String pluginName);

	/**
	 * A�ade un listener para la gestión del ciclo de vida de un determinado Jar
	 * 
	 * @param listener
	 */
	void addIJarListener(ILifeCicleJarListener listener);
	
	/**
	 * Elimina un JarListener
	 * @param listener
	 * @return si se ha producido o no la eliminación 
	 */
	boolean removeIJarListener(ILifeCicleJarListener listener);
}
