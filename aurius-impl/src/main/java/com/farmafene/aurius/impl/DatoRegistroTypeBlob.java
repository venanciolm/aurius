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
package com.farmafene.aurius.impl;

//import org.apache.commons.codec.binary.Base64;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.DatoRegistro;

/**
 * Implementación de un tipo de registro que contiene byte[]
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class DatoRegistroTypeBlob extends DatoRegistroAbstract<byte[]>
		implements DatoRegistro<byte[]> {

	private Long longitudMaxima;
	private Long longitudMinima;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public DatoRegistroTypeBlob(String id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = hashCode
				* 31
				+ ((getLongitudMinima() == null) ? 0 : getLongitudMinima()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getLongitudMaxima() == null) ? 0 : getLongitudMaxima()
						.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj instanceof DatoRegistroTypeBlob) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("id=").append(getId());
		sb.append(", nombre=").append(getNombre());
		if (null != longitudMinima) {
			sb.append(", longitudMímima=").append(getLongitudMinima());
		}
		if (null != getLongitudMaxima()) {
			sb.append(", longitudMáxima=").append(getLongitudMaxima());
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	protected byte[] fromString(String value) {
		//return Base64.decodeBase64(value);
		throw new UnsupportedOperationException(
				"Operación no soportada en este tipo de dato.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#toStringValue(java.lang
	 *      .Object)
	 */
	@Override
	protected String toStringValue(byte[] valor) {
		//String salida = null;
		if (valor != null) {
			//salida = Base64.encodeBase64String(valor);
		}
		//return salida;
		throw new UnsupportedOperationException(
				"Operación no soportada en este tipo de dato.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#validaValor(java.lang.
	 *      Object)
	 */
	@Override
	protected void validaValor(byte[] bytes) throws AuriusValidationException {
		if (null != bytes) {
			Long longMax = this.getLongitudMaxima();
			Long longMin = this.getLongitudMinima();

			if (null != longMax && bytes.length > longMax.intValue()) {
				throw new AuriusValidationException();
			} else if (null != longMin && bytes.length < longMin.intValue()) {
				throw new AuriusValidationException();
			}
		}
	}

	/**
	 * @return the longitudMaxima
	 * @since 1.0.0
	 */
	public Long getLongitudMaxima() {
		return this.longitudMaxima;
	}

	/**
	 * @param longitudMaxima
	 *            the longitudMaxima to set
	 * @since 1.0.0
	 */
	public void setLongitudMaxima(Long longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
	}

	/**
	 * @return the longitudMinima
	 * @since 1.0.0
	 */
	public Long getLongitudMinima() {
		return this.longitudMinima;
	}

	/**
	 * @param longitudMinima
	 *            the longitudMinima to set
	 * @since 1.0.0
	 */
	public void setLongitudMinima(Long longitudMinima) {
		this.longitudMinima = longitudMinima;
	}

	@Override
	public Class<byte[]> getValueType() {
		return byte[].class;
	}

}
