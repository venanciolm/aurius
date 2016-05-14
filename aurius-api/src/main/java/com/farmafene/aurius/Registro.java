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
package com.farmafene.aurius;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Contenedor básico de datos, como parámetros de entrada/salida de las
 * operaciones en el sistema.
 * 
 * @author vlopez
 * @since 1.0.0
 */
public interface Registro extends /*
								 * ELMap, NewClearInstanciable<Registro>,
								 * AuriusXMLOutput,
								 */Serializable {

	/**
	 * @return identificador en el diccionario
	 * @since 1.0.0
	 */
	String getId();

	/**
	 * Obtiene el contenido de un dato genérico
	 * 
	 * @param name
	 *            nombre del campo
	 * @param clazz
	 *            Clase a recuperar
	 * @return
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	<T extends Serializable> T get(String name, Class<T> clazz)
			throws IllegalArgumentException;

	/**
	 * Obtiene el dato contenido con ese nombre
	 * 
	 * @param name
	 *            nombre del dato
	 * @return dato obtenido, puede ser <code>null</code>
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	byte[] getBytes(String name) throws IllegalArgumentException;

	/**
	 * Obtiene el dato contenido con ese nombre
	 * 
	 * @param name
	 *            nombre del dato
	 * @return dato obtenido, puede ser <code>null</code>
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	Date getDate(String name) throws IllegalArgumentException;

	/**
	 * Obtiene el dato contenido con ese nombre
	 * 
	 * @param name
	 *            nombre del dato
	 * @return dato obtenido, puede ser <code>null</code>
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	BigDecimal getDecimal(String name) throws IllegalArgumentException;

	/**
	 * Obtiene el dato contenido con ese nombre
	 * 
	 * @param name
	 *            nombre del dato
	 * @return dato obtenido, puede ser <code>null</code>
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	String getString(String name) throws IllegalArgumentException;

	/**
	 * Obtiene el dato contenido con ese nombre
	 * 
	 * @param name
	 *            nombre del dato
	 * @param index
	 *            indice el registro a leer
	 * @return dato obtenido, puede ser <code>null</code>
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	Registro getRegistro(String name, int index)
			throws IllegalArgumentException, IndexOutOfBoundsException;

	/**
	 * Obtiene la longitud del elemento
	 * 
	 * @param name
	 *            nombre del dato
	 * @return numero de registros contenidos en ese elemento
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	int getRegistroSize(String name) throws IllegalArgumentException;

	/**
	 * Factor�a de registros
	 * 
	 * @param name
	 *            nombre del registro a crear
	 * @return Una nueva instancia de un registro
	 * @throws IllegalArgumentException
	 *             si el nombre es inválido o incompatible
	 * @since 1.0.0
	 */
	Registro newRegistro(String name) throws IllegalArgumentException;

	/**
	 * Introduce un dato en un registro
	 * 
	 * @param name
	 *            nombre del elemento
	 * @param value
	 *            valor del elemento
	 * @throws IllegalArgumentException
	 *             error en los parámetros de introduccion
	 * @since 1.0.0
	 */
	void put(String name, Serializable value) throws IllegalArgumentException;

	/**
	 * Introduce un dato en un registro
	 * 
	 * @param name
	 *            nombre del elemento
	 * @param registro
	 *            valor del elemento
	 * @throws IllegalArgumentException
	 *             error en los parámetros de introduccion
	 * @since 1.0.0
	 */
	void addRegistro(String name, Registro registro)
			throws IllegalArgumentException;
}