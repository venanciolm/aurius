/* Copyright (c) 2009-2014 farmafene.com
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
package com.farmafene.aurius.dao.hibernate.dic;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

/**
 * Dao que contiene la definición de un determinado Registro
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("2")
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
public class TypeDecimalVO extends DiccionarioDic {

	@Column(name = "DIC_LONG0")
	private Long maxDigitos;
	@Column(name = "DIC_LONG1")
	private Long maxDecimales;
	@Column(name = "DIC_STR0")
	private String mascara;
	@Column(name = "DIC_STR1")
	private String minInclusive;
	@Column(name = "DIC_STR2")
	private String minExclusive;
	@Column(name = "DIC_STR3")
	private String maxInclusive;
	@Column(name = "DIC_STR4")
	private String maxExclusive;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public TypeDecimalVO() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("id=").append(getId());
		sb.append(", nombre=").append(getNombre());
		sb.append(", maxDigitos=").append(maxDigitos);
		sb.append(", maxDecimales=").append(maxDecimales);
		sb.append(", mascara=").append(mascara);
		sb.append(", minInclusive=").append(minInclusive);
		sb.append(", maxInclusive=").append(maxInclusive);
		sb.append(", minExclusive=").append(minExclusive);
		sb.append(", maxExclusive=").append(maxExclusive);
		sb.append("}");
		return sb.toString();
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
	public void setMaxDigitos(Long maxDigitos) {
		this.maxDigitos = maxDigitos;
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
	public void setMaxDecimales(Long maxDecimales) {
		this.maxDecimales = maxDecimales;
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
		this.mascara = mascara;
	}

	/**
	 * @return the minInclusive
	 */
	public String getMinInclusive() {
		return minInclusive;
	}

	/**
	 * @param minInclusive
	 *            the minInclusive to set
	 */
	public void setMinInclusive(String minInclusive) {
		this.minInclusive = minInclusive;
	}

	/**
	 * @return the minExclusive
	 */
	public String getMinExclusive() {
		return minExclusive;
	}

	/**
	 * @param minExclusive
	 *            the minExclusive to set
	 */
	public void setMinExclusive(String minExclusive) {
		this.minExclusive = minExclusive;
	}

	/**
	 * @return the maxInclusive
	 */
	public String getMaxInclusive() {
		return maxInclusive;
	}

	/**
	 * @param maxInclusive
	 *            the maxInclusive to set
	 */
	public void setMaxInclusive(String maxInclusive) {
		this.maxInclusive = maxInclusive;
	}

	/**
	 * @return the maxExclusive
	 */
	public String getMaxExclusive() {
		return maxExclusive;
	}

	/**
	 * @param maxExclusive
	 *            the maxExclusive to set
	 */
	public void setMaxExclusive(String maxExclusive) {
		this.maxExclusive = maxExclusive;
	}

}
