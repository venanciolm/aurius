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
package com.farmafene.commons.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * Utilidad para devolver una paginación de un listado
 * 
 * @version 1.0.0
 * @since 1.0.0
 * 
 * @author vlopez
 * 
 * @param <T>
 */
public interface HBPagina<T extends Serializable> {
	/**
	 * Obtiene la lista
	 * 
	 * @return lista de registros
	 * @since 1.0.0
	 */
	public List<T> getLista();

	/**
	 * Obtiene el número de registros en esta página
	 * 
	 * @return número de registros en esta página
	 * @since 1.0.0
	 */
	public int getNumRegistros();

	/**
	 * Obtiene el tamaño de página
	 * 
	 * @return tamaño de página
	 * @since 1.0.0
	 */
	public int getPageSize();

	/**
	 * Obtiene el primer registro
	 * 
	 * @return primer registro
	 * @since 1.0.0
	 */
	public int getFirst();

	/**
	 * Obtiene el último registro
	 * 
	 * @return último registro
	 * @since 1.0.0
	 */
	public int getLast();

	/**
	 * Obtiene el número total de registros
	 * 
	 * @return número total de registros
	 * @since 1.0.0
	 */
	public int getTotalSize();
}
