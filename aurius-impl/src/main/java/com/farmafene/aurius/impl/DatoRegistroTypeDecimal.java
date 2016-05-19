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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.DatoRegistro;

/**
 * Implementación de un Decimal(P,S)
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class DatoRegistroTypeDecimal extends DatoRegistroAbstract<BigDecimal>
		implements DatoRegistro<BigDecimal> {

	private Long maxDigitos;
	private Long maxDecimales;
	private String mascara;
	private BigDecimal minExclusive;
	private BigDecimal minInclusive;
	private BigDecimal maxExclusive;
	private BigDecimal maxInclusive;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public DatoRegistroTypeDecimal(String id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#toString()
	 * @since 1.0.0
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("id=").append(getId());
		sb.append(", nombre=").append(getNombre());
		if (null != getMaxDigitos()) {
			sb.append(", maxDigitos").append(getMaxDigitos());
		}
		if (null != getMaxDecimales()) {
			sb.append(", maxDecimales=").append(getMaxDecimales());
		}
		if (null != getMascara()) {
			sb.append(", mascara=").append(getMascara());
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
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = hashCode * 31
				+ ((getMaxDigitos() == null) ? 0 : getMaxDigitos().hashCode());
		hashCode = hashCode
				* 31
				+ ((getMaxDecimales() == null) ? 0 : getMaxDecimales()
						.hashCode());
		hashCode = hashCode * 31
				+ ((getMascara() == null) ? 0 : getMascara().hashCode());
		hashCode = hashCode
				* 31
				+ ((getMinInclusive() == null) ? 0 : getMinInclusive()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getMaxInclusive() == null) ? 0 : getMaxInclusive()
						.hashCode());
		hashCode = hashCode
				* 31
				+ ((getMinExclusive() == null) ? 0 : getMinExclusive()
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
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj instanceof DatoRegistroTypeDecimal) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#fromString(java.lang.String
	 *      )
	 */
	@Override
	protected BigDecimal fromString(String value) {
		try {
			return (BigDecimal) getDecimalFormat().parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#toStringValue(java.lang.Object)
	 */
	@Override
	protected String toStringValue(BigDecimal value) {
		return getDecimalFormat().format(value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#validaValor(java.lang.Object)
	 */
	@Override
	protected void validaValor(BigDecimal bd) throws AuriusValidationException {
		if (null != getMaxDigitos()
				&& getLongitud(bd) > getMaxDigitos().intValue()) {
			throw new AuriusValidationException(
					"Longitud máxima superada; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (getMaxDecimales() != null && bd.scale() > getMaxDecimales()) {
			throw new AuriusValidationException(
					"Numero de decimales inválido; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != getMinInclusive()
				&& bd.compareTo(getMinInclusive()) < 0) {
			throw new AuriusValidationException(
					"Valor minimo superado; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != getMinExclusive()
				&& bd.compareTo(getMinExclusive()) <= 0) {
			throw new AuriusValidationException(
					"Valor minimo superado; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != getMaxInclusive()
				&& bd.compareTo(getMaxInclusive()) > 0) {
			throw new AuriusValidationException(
					"Valor máximo superado; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != getMaxExclusive()
				&& bd.compareTo(getMaxExclusive()) >= 0) {
			throw new AuriusValidationException(
					"Valor máximo superado; el valor introducido '" + bd
							+ "', no cumple con la definición '"
							+ this.toString() + "'");
		} else if (null != getMascara()) {
			try {
				if (!getStringNormalizado(bd).matches(getMascara())) {
					throw new AuriusValidationException(
							"Formato inválido; el valor introducido '" + bd
									+ "', no cumple con la definición '"
									+ this.toString() + "'");

				}
			} catch (PatternSyntaxException e) {
				throw new AuriusValidationException(
						"Error en la máscara; el valor introducido '" + bd
								+ "', no cumple con la definición '"
								+ this.toString() + "'");
			}
		}
	}

	/**
	 * Obtenemos la longitud de un determinado BigDecimal
	 * 
	 * @param bd
	 *            a evaluar
	 * @return longitud normalizada de BigDecimal.
	 */
	private int getLongitud(BigDecimal bd) {
		int longitud = 0;
		if (bd.scale() < 0) {
			longitud = bd.precision() + bd.scale();
		} else if (bd.precision() > bd.scale()) {
			longitud = bd.precision();
		} else {
			longitud = bd.scale() + 1;
		}
		return longitud;
	}

	private String getStringNormalizado(BigDecimal bd) {

		DecimalFormat df = getDecimalFormat();
		return df.format(bd);
	}

	private DecimalFormat getDecimalFormat() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat();
		df.setDecimalFormatSymbols(dfs);
		df.setMinimumIntegerDigits(1);
		df.setPositivePrefix("+");
		df.setNegativeSuffix("-");
		df.setGroupingUsed(false);
		df.setDecimalSeparatorAlwaysShown(false);
		df.setParseBigDecimal(true);
		return df;
	}

	/**
	 * @return the maxDigitos
	 */
	public Long getMaxDigitos() {
		return maxDigitos;
	}

	/**
	 * @param maxDigitos
	 *            the maxDigitos to set
	 */
	public void setPrecision(Long precision) {
		this.maxDigitos = precision;
	}

	/**
	 * @return the maxDecimales
	 */
	public Long getMaxDecimales() {
		return maxDecimales;
	}

	/**
	 * @param maxDecimales
	 *            the maxDecimales to set
	 */
	public void setEscala(Long escala) {
		this.maxDecimales = escala;
	}

	/**
	 * @return the mascara
	 */
	public String getMascara() {
		return mascara;
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
	 * @return the minExclusive
	 */
	public BigDecimal getMinExclusive() {
		return minExclusive;
	}

	/**
	 * @param minExclusive
	 *            the minExclusive to set
	 */
	public void setMinExclusive(BigDecimal minExclusive) {
		this.minExclusive = minExclusive;
	}

	/**
	 * @return the minInclusive
	 */
	public BigDecimal getMinInclusive() {
		return minInclusive;
	}

	/**
	 * @param minInclusive
	 *            the minInclusive to set
	 */
	public void setMinInclusive(BigDecimal minInclusive) {
		this.minInclusive = minInclusive;
	}

	/**
	 * @return the maxExclusive
	 */
	public BigDecimal getMaxExclusive() {
		return maxExclusive;
	}

	/**
	 * @param maxExclusive
	 *            the maxExclusive to set
	 */
	public void setMaxExclusive(BigDecimal maxExclusive) {
		this.maxExclusive = maxExclusive;
	}

	/**
	 * @return the maxInclusive
	 */
	public BigDecimal getMaxInclusive() {
		return maxInclusive;
	}

	/**
	 * @param maxInclusive
	 *            the maxInclusive to set
	 */
	public void setMaxInclusive(BigDecimal maxInclusive) {
		this.maxInclusive = maxInclusive;
	}

	@Override
	public Class<BigDecimal> getValueType() {
		return BigDecimal.class;
	}
}
