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

import java.io.Serializable;
import java.util.Set;

import com.farmafene.aurius.server.Command;

/**
 * Definición de un determinado servicio
 * 
 * @author vlopez
 * @since 1.0.0
 * 
 */
public interface Servicio extends Serializable {

	/**
	 * Obtiene el identificador del Servicio
	 * 
	 * @return identificador del servicio
	 * @since 1.0.0
	 */
	public String getId();

	/**
	 * Obtiene la versión del Servicio
	 * 
	 * @return versión del servicio
	 * @since 1.0.0
	 */
	public String getVersion();

	/**
	 * Obtiene la aplicación a la que pertenece el servicio
	 * 
	 * @return Aplicación del servicio
	 * @since 1.0.0
	 */
	public String getIdAplicacion();

	/**
	 * Obtiene la clase Java que implementa el servicio
	 * 
	 * @return clase java
	 * @since 1.0.0
	 * @see Command
	 */
	public String getCommand();

	/**
	 * Obtiene el registro de entrada al servicio
	 * 
	 * @return registro de entrada
	 * @since 1.0.0
	 */
	public String getIdRegistroEntrada();

	/**
	 * Obtiene el registro de salida del servicio
	 * 
	 * @return registro de salida
	 * @since 1.0.0
	 */
	public String getIdRegistroSalida();

	/**
	 * Si el servico está disponible
	 * 
	 * @return disponible (<code>true</code>) o indisponible(<code>false</code>)
	 * @since 1.0.0
	 */
	public boolean isDisponible();

	/**
	 * Obtiene el texto descriptivo de la indisponibilidad
	 * 
	 * @return Texto descriptivo de la indisponibilidad
	 * @since 1.0.0
	 */
	public String getMotivoIndisponibilidad();

	public boolean isPublic();

	public boolean isInheritAuth();

	public Set<String> getRoles();
}
