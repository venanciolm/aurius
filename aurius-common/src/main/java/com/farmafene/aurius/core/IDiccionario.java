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

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;

public interface IDiccionario {

	/**
	 * Factoría de servicios
	 * 
	 * @param cookie
	 *            información de autenticación
	 * @param servicioId
	 *            identificador del servicio
	 * @return el registro de entrada del servicio
	 */
	Registro getRegistroServicio(AuthInfo cookie, String servicioId);

	/**
	 * Factoría de servicios
	 * 
	 * @param cookie
	 *            información de autenticacion
	 * @param servicioId
	 *            identificador del servicio
	 * @param version
	 *            versión del servicio
	 * @return registro de entrada del servicio
	 */
	Registro getRegistroServicio(AuthInfo cookie, String servicioId,
			String version);

	/**
	 * Factoría de Registros
	 * 
	 * @param idDiccionario
	 *            Identificador del diccionario
	 * @return registro de entrada del servicio
	 */
	Registro getRegistro(String idDiccionario);

	/**
	 * Obtiene la definición de un determinado servicio
	 * 
	 * @param idServicio
	 *            identificador del servicio
	 * @return servicio
	 */
	Servicio getServicio(String idServicio);

	/**
	 * Obtiene la definición de un determinado servicio
	 * 
	 * @param idServicio
	 *            identificador del servicio
	 * @param version
	 *            versión del servicio
	 * @return Servicio solicitado
	 */
	Servicio getServicio(String idServicio, String version);

	/**
	 * Obtiene la definición de un determinado registro para su validación
	 * 
	 * @param idRegistro
	 *            identificador del registro
	 * @return Definición del registro
	 */
	TypeRegistro getDefinicionRegistro(String idRegistro);
}
