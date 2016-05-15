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
package com.farmafene.aurius.core.impl;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.IDiccionario;
import com.farmafene.aurius.core.Servicio;

public class Diccionario implements IDiccionario {

	public Diccionario() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getRegistroServicio(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	@Override
	public Registro getRegistroServicio(AuthInfo cookie, String servicioId) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getRegistroServicio(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Registro getRegistroServicio(AuthInfo cookie, String servicioId,
			String version) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getRegistro(java.lang.String)
	 */
	@Override
	public Registro getRegistro(String idDiccionario) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getServicio(java.lang.String)
	 */
	@Override
	public Servicio getServicio(String idServicio) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getServicio(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Servicio getServicio(String idServicio, String version) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IDiccionario#getDefinicionRegistro(java.lang.String)
	 */
	@Override
	public TypeRegistro getDefinicionRegistro(String idRegistro) {
		throw new UnsupportedOperationException(
				"Error en el empaquetado. Esta clase nunca debe ser invocada");
	}
}
