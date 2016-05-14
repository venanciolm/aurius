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

/**
 * Acceso a la configuración del sistema.
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IConfiguracion {

	/**
	 * Obtiene el una propiedad de la configuración
	 * 
	 * @param key
	 *            clave de búsqueda
	 * @return valor de la clave; <code>null</code> si no existe
	 * @since 1.0.0
	 */
	public String getProperty(String key);

	/**
	 * Obtiene el una propiedad de la configuración
	 * 
	 * @param idConfiguracion
	 *            Identicador del fichero
	 * @param key
	 *            clave de búsqueda
	 * @return valor de la clave; <code>null</code> si no existe
	 * @since 1.0.0
	 */
	public String getProperty(String idConfiguracion, String key);

	/**
	 * Obtiene un conjunto de entradas de configurarición de un terminado
	 * fichero
	 * 
	 * @param idConfiguracion
	 *            Identicador del fichero
	 * @return Entrada de la clave
	 * @since 1.0.0
	 */
	public IConfiguracionEntries getEntries(String idConfiguracion);

	/**
	 * Obtiene el Directorio de configuración activo para la aplicación
	 * 
	 * @return el Path del directorio de configuración
	 * @since 1.0.0
	 */
	public String getConfigPath();

}
