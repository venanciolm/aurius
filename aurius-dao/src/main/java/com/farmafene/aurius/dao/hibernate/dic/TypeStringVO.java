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
@DiscriminatorValue("1")
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
public class TypeStringVO extends DiccionarioDic {

	@Column(name = "DIC_LONG0")
	private Long longitudMinima;
	@Column(name = "DIC_LONG1")
	private Long longitudMaxima;
	@Column(name = "DIC_STR0")
	private String mascara;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public TypeStringVO() {

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
		sb.append(", longitudMinima=").append(longitudMinima);
		sb.append(", longitudMaxima=").append(longitudMaxima);
		sb.append(", mascara=").append(mascara);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the longitudMinima
	 */
	public Long getLongitudMinima() {
		return longitudMinima;
	}

	/**
	 * @param longitudMinima
	 *            the longitudMinima to set
	 */
	public void setLongitudMinima(Long longitudMinima) {
		this.longitudMinima = longitudMinima;
	}

	/**
	 * @return the longitudMaxima
	 */
	public Long getLongitudMaxima() {
		return longitudMaxima;
	}

	/**
	 * @param longitudMaxima
	 *            the longitudMaxima to set
	 */
	public void setLongitudMaxima(Long longitudMaxima) {
		this.longitudMaxima = longitudMaxima;
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
}
