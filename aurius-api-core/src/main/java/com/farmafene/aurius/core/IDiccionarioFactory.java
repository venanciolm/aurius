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
package com.farmafene.aurius.core;

import java.util.Map;

import com.farmafene.aurius.IConfigurableBean;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;

/**
 * Definición de la factoría de Diccionario de Datos
 * 
 * @author vlopez
 * @since 1.0.0
 */
public interface IDiccionarioFactory extends IConfigurableBean{

	/**
	 * Obtenemos el servicio por defecto
	 * 
	 * @param id
	 *            Identificador del servicio
	 * @return Servicio
	 * @throws IllegalArgumentException
	 *             si el Identificador es inválido
	 * @since 1.0.0
	 */
	public Servicio getServicio(String id) throws IllegalArgumentException;

	/**
	 * Obtenemos una versión de un determinado servicio
	 * 
	 * @param id
	 *            Identificador del servicio
	 * @param version
	 *            version solicitada
	 * @return Servicio
	 * @throws IllegalArgumentException
	 *             si el Identificador es inválido
	 * @since 1.0.0
	 */
	public Servicio getServicio(String id, String version)
			throws IllegalArgumentException;

	/**
	 * Obtenemos la definición de un determinado registro
	 * 
	 * @param id
	 *            Identificador del servicio
	 * @return Definición del registro
	 * @throws IllegalArgumentException
	 *             si el Identificador es inválido
	 * @since 1.0.0
	 */
	public TypeRegistro getDefinicionRegistro(String id)
			throws IllegalArgumentException;

	/**
	 * Método factoría de los registros
	 * 
	 * @param idRegistro
	 *            registro a construir
	 * @param definiciones
	 *            mapa con los identificadore se registros, y su definición.
	 * @return registro que cumple las condiciones
	 */
	public Registro getRegistro(String idRegistro,
			Map<String, TypeRegistro> definiciones);

	/**
	 * Construye un Id de registro normalizado
	 * 
	 * @param id
	 *            identificador no normalizado
	 * @return identificador de registro normalizado
	 */
	public String getRegistroNormalizado(String id);

	/**
	 * Identificador de un registro normalizado (sin versión)
	 * 
	 * @param id
	 *            identificador del registro
	 * @return identificador normalizado del registro
	 */
	public String getServicioNormalizado(String id);

	/**
	 * Proporciona un identificador (sufijo) de una versión de un registro.
	 * 
	 * @param version
	 *            identificador de versión
	 * @return identificaci�n de versión normalizada.
	 */
	public String getVersionNomalizada(String version);
}
