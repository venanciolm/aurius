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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.DatoRegistro;

/**
 * Tipo de dato que contiene una cadena (String)
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class DatoRegistroTypeString extends DatoRegistroAbstract<String>
		implements DatoRegistro<String> {

	private String mascara;
	private Long longitudMinima;
	private Long longitudMaxima;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public DatoRegistroTypeString(String id) {
		super(id);
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
		if (null != getMascara()) {
			sb.append(", mascara=").append(getMascara());
		}
		if (null != getLongitudMinima()) {
			sb.append(", longitudMínima=").append(getLongitudMinima());
		}
		if (null != getLongitudMaxima()) {
			sb.append(", longitudMáxima=").append(getLongitudMaxima());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = hashCode * 31
				+ ((getMascara() == null) ? 0 : getMascara().hashCode());
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
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#fromString(java.lang.String)
	 */
	@Override
	protected String fromString(String objeto) {
		return objeto;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#toStringValue(java.lang.Object)
	 */
	@Override
	protected String toStringValue(String valor) {
		return valor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#validaValor(java.lang.Object)
	 */
	@Override
	protected void validaValor(String cadena) throws AuriusValidationException {
		Long longMin = this.getLongitudMinima();
		Long longMax = this.getLongitudMaxima();

		if (null != longMax && cadena.length() > longMax.intValue()) {
			throw new AuriusValidationException(
					"Longitud máxima superada; el valor introducido '" + cadena
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != longMin && cadena.length() < longMin.intValue()) {
			throw new AuriusValidationException(
					"Longitud mínima no alcanzada; el valor introducido '"
							+ cadena + "', no cumple con la definición '"
							+ this.toString() + "'");
		} else {
			if (null != getMascara()) {
				try {
					if (!cadena.matches(getMascara())) {
						throw new AuriusValidationException(
								"No se cumple la máscara; el valor introducido '"
										+ cadena
										+ "', no cumple con la definición '"
										+ this.toString() + "'");
					}
				} catch (PatternSyntaxException e) {
					throw new AuriusValidationException(
							"No se cumple la máscara; el valor introducido '"
									+ cadena
									+ "', no cumple con la definición '"
									+ this.toString() + "'", e);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#toString(Object)
	 * @since 1.0.0
	 */
	@Override
	public String toString(String value) {
		String strSalida = null;
		if (null != value) {
			if (!(value instanceof String)) {
				throw new IllegalArgumentException(
						"Debe introducirse un valor compatible: 'java.lang.String', vs. '"
								+ value.getClass().getName() + "'.");
			}
			strSalida = (String) value;
		}
		return strSalida;
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
		if (obj instanceof DatoRegistroTypeString) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	/**
	 * Obtiene la longitud m�nima
	 * 
	 * @return the longitudMinima
	 * @since 1.0.0
	 */
	public Long getLongitudMinima() {
		return this.longitudMinima;
	}

	/**
	 * Establece la longitud minima
	 * 
	 * @param longitudMinima
	 *            the longitudMinima to set
	 * @since 1.0.0
	 */
	public void setLongitudMinima(Long longitudMinima) {
		this.longitudMinima = longitudMinima;
	}

	/**
	 * Obtiene la longitud máxima
	 * 
	 * @since 1.0.0
	 * @return the longitudMaxima
	 */
	public Long getLongitudMaxima() {
		return this.longitudMaxima;
	}

	/**
	 * Establece la longitud máxima
	 * 
	 * @param longitudMaxima
	 *            the longitudMaxima to set
	 * @since 1.0.0
	 */
	public void setLongitudMaxima(Long longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
	}

	/**
	 * Obtiene la máscara de la cadena (expresión regular)
	 * 
	 * @return the mascara
	 * @since 1.0.0
	 */
	public String getMascara() {
		return this.mascara;
	}

	/**
	 * Establece la máscara de la cadena
	 * 
	 * @param mascara
	 *            the mascara to set
	 * @since 1.0.0
	 */
	public void setMascara(String mascara) {
		this.mascara = null;
		try {
			Pattern.compile(mascara);
			this.mascara = mascara;
		} catch (Exception e) {
			this.mascara = null;
		}
	}

	@Override
	public Class<String> getValueType() {
		return String.class;
	}
}
