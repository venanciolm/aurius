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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

//CREATE TABLE OPERATIVAS_OP (
//		  OP_GRUPO char(6) NOT NULL,
//		  DICD_ID DECIMAL(9,0) NOT NULL,
//		  OP_DESC varchar(255) DEFAULT NULL,
//		  PRIMARY KEY (OP_GRUPO)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * Lista de operativas disponibles
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "OPERATIVAS_OP")
@org.hibernate.annotations.Proxy(lazy = true)
public class OperativasOp implements Serializable {

	@Id
	@Column(name = "OP_GRUPO")
	private String grupo;

	@Column(name = "OP_DESC")
	private String descripcion;

	@OneToOne(optional = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "DICD_ID", referencedColumnName = "DICD_ID")
	private DiccionarioDesc descripcionLarga;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public OperativasOp() {

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
		sb.append("grupo=").append(grupo);
		sb.append(", descripcion=").append(descripcion);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the grupo
	 * @since 1.0.0
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo
	 *            the grupo to set
	 * @since 1.0.0
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the descripcion
	 * @since 1.0.0
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 * @since 1.0.0
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the descripcionLarga
	 * @since 1.0.0
	 */
	public DiccionarioDesc getDescripcionLarga() {
		return descripcionLarga;
	}

	/**
	 * @param descripcionLarga the descripcionLarga to set
	 */
	public void setDescripcionLarga(DiccionarioDesc descripcionLarga) {
		this.descripcionLarga = descripcionLarga;
	}
}
