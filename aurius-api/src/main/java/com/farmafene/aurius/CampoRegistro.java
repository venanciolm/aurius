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
 * Identificación de diccionario del campo de un determinado registro
 * 
 * @author vlopez
 * @since 1.0.0
 */
public interface CampoRegistro extends Serializable {

	/**
	 * Obtiene el tipo de dato
	 * 
	 * @return el tipo de dato
	 * @since 1.0.0
	 */
	DatoRegistro<Serializable> getDatoRegistro();

	/**
	 * Obtiene en el registro el nombre único en la lista de campos del mismo
	 * 
	 * @return el nombre
	 * @since 1.0.0
	 */
	String getNombre();

	/**
	 * Realiza la validación de un objeto, basándose en la cardinalidad del
	 * mismo.
	 * 
	 * @param obj
	 *            objeto a validar
	 * @return el objeto validado
	 * @since 1.0.0
	 */
	void valida(Object obj) throws AuriusValidationException;
}
