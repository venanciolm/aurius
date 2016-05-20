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

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

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
@DiscriminatorValue("0")
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
public class TypeRegistroVO extends DiccionarioDic {

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public TypeRegistroVO() {

	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO", referencedColumnName = "OP_GRUPO"),
			@JoinColumn(name = "DIC_ID", referencedColumnName = "DIC_ID") })
	@OrderBy(value = "id.orden")
	private List<RegistroReg> elementos;

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
		if (null != getNombre()) {
			sb.append(", nombre=").append(getNombre());
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the elementos
	 */
	public List<RegistroReg> getElementos() {
		return elementos;
	}
}
