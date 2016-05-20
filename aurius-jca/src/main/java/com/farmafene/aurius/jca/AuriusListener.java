/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.jca;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.core.ContextoCore;

/**
 * Interfaz del adaptador.
 * <p>
 * Publica los servicios disponibles en el sistema.
 * </p>
 * 
 * @author vlopez@farmafene.com
 * @since 1.0.0
 */
public interface AuriusListener {

	/**
	 * Realiza una solicitud al diccionario de datos
	 * 
	 * @param cookie
	 *            datos de autenticación
	 * @param idServicio
	 *            identificador del servicio
	 * @return Registro de entrada al servicio
	 */
	Registro getRegistro(AuthInfo cookie, String idServicio);

	/**
	 * Realiza una solicitud al diccionario de datos
	 * 
	 * @param cookie
	 *            datos de autenticación
	 * @param idServicio
	 *            identificador del servicio
	 * @param version
	 *            versión del servicio solicitado
	 * @return Registro de entrada al servicio
	 */
	Registro getRegistro(AuthInfo cookie, String idServicio, String version);

	/**
	 * Realiza la invocación de un servicio
	 * 
	 * @param ctx
	 *            Contexto de ejecución
	 * @param idServicio
	 *            identificador del servicio
	 * @param version
	 *            versión del servicio
	 * @param registro
	 *            resgistro de entrada
	 * @return registro con el resultado
	 */
	Registro invoke(ContextoCore ctx, String idServicio, String version,
			Registro registro);
}
