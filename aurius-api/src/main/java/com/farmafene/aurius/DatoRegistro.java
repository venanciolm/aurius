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

/**
 * Dato del diccionario
 * 
 * @author vlopez
 * @since 1.0.0
 */
public interface DatoRegistro<T> extends Serializable {

	/**
	 * Valida si el objeto propuesto es del tipo que indica este tipo de dato
	 * 
	 * @param valor
	 *            objeto a validar
	 * @return el objeto si es válido,
	 * @throws AuriusValidationException
	 *             si es inválido
	 * @since 1.0.0
	 */
	T valida(Object valor) throws AuriusValidationException;

	/**
	 * Obtiene el identificador de diccionario del determinado tipo de dato
	 * 
	 * @return identificador único del tipo de dato
	 * @since 1.0.0
	 */
	String getId();

	/**
	 * Nombre propuesto para la identificacion por nombre del mismo
	 * 
	 * @return nombre propuesto
	 * @since 1.0.0
	 */
	String getNombre();

	/**
	 * Realiza la conversión de un objeto a un String, bas�ndose en la
	 * definición del campo de un determiado registro.
	 * 
	 * @param obj
	 *            Objeto a convertir en String
	 * @return el objeto validado
	 * @since 1.0.0
	 */
	String toString(T obj) throws IllegalArgumentException;

	Class<T> getValueType();
}
