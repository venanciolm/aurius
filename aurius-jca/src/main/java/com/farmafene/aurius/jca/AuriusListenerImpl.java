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
package com.farmafene.aurius.jca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.Diccionario;
import com.farmafene.aurius.core.GestorTx;

public class AuriusListenerImpl implements AuriusListener {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusListenerImpl.class);
	public AuriusListenerImpl() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#invoke(com.farmafene.aurius.core.ContextoCore,
	 *      java.lang.String, java.lang.String, com.farmafene.aurius.Registro)
	 */
	@Override
	public Registro invoke(ContextoCore ctx, String idServicio, String version,
			Registro registro) {
		if (logger.isDebugEnabled()) {
			logger.debug("invoke('" + ctx + "', '" + idServicio + "', '"
					+ version + "', '" + registro + "')");
		}
		return GestorTx.invoke(ctx, idServicio, version, registro);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#invoke(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String)
	 */
	@Override
	public Registro getRegistro(AuthInfo cookie, String idServicio) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRegistro('" + cookie + "', '" + idServicio + "')");
		}
		return Diccionario.getRegistroServicio(cookie, idServicio);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.jca.AuriusListener#invoke(com.farmafene.aurius.AuthInfo,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public Registro getRegistro(AuthInfo cookie, String idServicio,
			String version) {
		if (logger.isDebugEnabled()) {
			logger.debug("invoke('" + cookie + "', '" + idServicio + ", '"
					+ version + "')");
		}
		return Diccionario.getRegistroServicio(cookie, idServicio, version);
	}

}
