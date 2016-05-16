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

public class Diccionario {

	private static final IDiccionario iDiccionario = new com.farmafene.aurius.core.impl.Diccionario();

	private Diccionario() {

	}

	public static IDiccionario getIDiccionario() {
		return iDiccionario;
	}

	public static Registro getRegistroServicio(AuthInfo cookie,
			String servicioId) {
		return getIDiccionario().getRegistroServicio(cookie, servicioId);
	}

	public static Registro getRegistroServicio(AuthInfo cookie,
			String servicioId, String version) {
		return getIDiccionario().getRegistroServicio(cookie, servicioId,
				version);
	}

	public static Registro getRegistro(String idDiccionario) {
		return getIDiccionario().getRegistro(idDiccionario);
	}

	public static Servicio getServicio(String idServicio) {
		return getIDiccionario().getServicio(idServicio);
	}

	public static Servicio getServicio(String idServicio, String version) {
		return getIDiccionario().getServicio(idServicio, version);
	}
}
