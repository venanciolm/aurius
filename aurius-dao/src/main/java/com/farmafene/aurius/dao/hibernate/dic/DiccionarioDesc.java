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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

/**
 * <p>
 * Descripciones del diccionario
 * </p>
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "DICDESC_DICD")
@org.hibernate.annotations.Proxy(lazy = true)
@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
public class DiccionarioDesc implements Serializable {

	@Id
	@Column(name = "DICD_ID")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	@Column(name = "DICD_DESC")
	private String descripcion;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public DiccionarioDesc() {

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
		sb.append("id=").append(id);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
