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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.DatoRegistro;

/**
 * Implementación de una fecha
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class DatoRegistroTypeTime extends DatoRegistroAbstract<Date> implements
		DatoRegistro<Date> {

	private Long longitud;
	private String dateFormat;
	private String mascara;
	private Date maxExclusive;
	private Date maxInclusive;
	private Date minExclusive;
	private Date minInclusive;
	private DateFormat df;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public DatoRegistroTypeTime(String id) {
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
		if (null != getLongitud()) {
			sb.append(", longitud").append(getLongitud());
		}
		if (null != getDateFormat()) {
			sb.append(", dateFormat").append(getDateFormat());
		}
		if (null != getMascara()) {
			sb.append(", mascara").append(getMascara());
		}
		if (null != getMinInclusive()) {
			sb.append(", minInclusive").append(getMinInclusive());
		}
		if (null != getMinExclusive()) {
			sb.append(", minExclusive").append(getMinExclusive());
		}
		if (null != getMaxInclusive()) {
			sb.append(", maxInclusive").append(getMaxInclusive());
		}
		if (null != getMaxExclusive()) {
			sb.append(", maxExclusive").append(getMaxExclusive());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#toString(Object)
	 * @since 1.0.0
	 */
	@Override
	public String toString(Date value) {
		String salida = null;
		if (null != value) {
			if (!(value instanceof Date)) {
				throw new IllegalArgumentException(
						"Debe introducirse un valor compatible");
			}
			if (null != df) {
				salida = df.format((Date) value);
			} else {
				salida = value.toString();
			}
		}
		return salida;
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
				+ ((getLongitud() == null) ? 0 : getLongitud().hashCode());
		hashCode = hashCode * 31
				+ ((getDateFormat() == null) ? 0 : getDateFormat().hashCode());
		hashCode = hashCode * 31
				+ ((getMascara() == null) ? 0 : getMascara().hashCode());
		hashCode = hashCode
				* 31
				+ ((getMinInclusive() == null) ? 0 : getMinInclusive()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getMinExclusive() == null) ? 0 : getMinExclusive()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getMaxInclusive() == null) ? 0 : getMaxInclusive()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getMaxExclusive() == null) ? 0 : getMaxExclusive()
						.hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj instanceof DatoRegistroTypeTime) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#fromString(java.lang.String)
	 */
	@Override
	protected Date fromString(String objeto) {
		try {
			return df.parse(objeto);
		} catch (Throwable th) {
			throw new AuriusValidationException(
					"Fecha inválida; el valor introducido '" + objeto
							+ "', no cumple con la definición '"
							+ this.toString() + "'", th);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#toStringValue(java.lang.Object)
	 */
	@Override
	protected String toStringValue(Date valor) {
		return df.format(valor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#validaValor(java.lang.Object)
	 */
	@Override
	protected void validaValor(Date date) throws AuriusValidationException {
		if (null != getMinInclusive() && date.compareTo(getMinInclusive()) < 0) {
			throw new AuriusValidationException(
					"Valor minimo superado; el valor introducido '" + date
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		}
		if (null != getMinExclusive() && date.compareTo(getMinExclusive()) <= 0) {
			throw new AuriusValidationException(
					"Valor minimo superado; el valor introducido '" + date
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		}
		if (null != getMaxInclusive() && date.compareTo(getMaxInclusive()) > 0) {
			throw new AuriusValidationException(
					"Valor maximo superado; el valor introducido '" + date
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		}
		if (null != getMaxExclusive() && date.compareTo(getMaxExclusive()) >= 0) {
			throw new AuriusValidationException(
					"Valor maximo superado; el valor introducido '" + date
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		}
		if ((null != df) && null != getMascara()) {
			try {
				if (!df.format(date).matches(getMascara())) {
					throw new AuriusValidationException(
							"No se cumple el formato; el valor introducido '"
									+ date + "', no cumple con la definición '"
									+ this.toString() + "'");

				}
			} catch (PatternSyntaxException e) {
				throw new AuriusValidationException(
						"Máscara invalida; el valor introducido '" + date
								+ "', no cumple con la definición '"
								+ this.toString() + "'");
			} catch (IllegalArgumentException e) {
				throw new AuriusValidationException(
						"Máscara invalida; el valor introducido '" + date
								+ "', no cumple con la definición '"
								+ this.toString() + "'");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 *            the dateFormat to set
	 * @since 1.0.0
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
		if (null != dateFormat) {
			try {
				df = new SimpleDateFormat(dateFormat);
				this.dateFormat = dateFormat;
			} catch (Exception e) {
				df = new SimpleDateFormat(this.dateFormat);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public String getMascara() {
		return this.mascara;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Date getMaxExclusive() {
		return this.maxExclusive;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Date getMaxInclusive() {
		return this.maxInclusive;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Date getMinExclusive() {
		return this.minExclusive;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Date getMinInclusive() {
		return this.minInclusive;
	}

	/**
	 * @param mascara
	 *            the mascara to set
	 */
	public void setMascara(String mascara) {
		this.mascara = null;
		try {
			Pattern.compile(mascara);
			this.mascara = mascara;
		} catch (Exception e) {
		}
	}

	/**
	 * @param maxExclusive
	 *            the maxExclusive to set
	 */
	public void setMaxExclusive(Date maxExclusive) {
		this.maxExclusive = maxExclusive;
	}

	/**
	 * @param maxInclusive
	 *            the maxInclusive to set
	 */
	public void setMaxInclusive(Date maxInclusive) {
		this.maxInclusive = maxInclusive;
	}

	/**
	 * @param minExclusive
	 *            the minExclusive to set
	 */
	public void setMinExclusive(Date minExclusive) {
		this.minExclusive = minExclusive;
	}

	/**
	 * @param minInclusive
	 *            the minInclusive to set
	 */
	public void setMinInclusive(Date minInclusive) {
		this.minInclusive = minInclusive;
	}

	/**
	 * @return the longitud
	 */
	public Long getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud
	 *            the longitud to set
	 */
	public void setLongitud(Long longitud) {
		this.longitud = longitud;
	}

	@Override
	public Class<Date> getValueType() {
		return Date.class;
	}

}
