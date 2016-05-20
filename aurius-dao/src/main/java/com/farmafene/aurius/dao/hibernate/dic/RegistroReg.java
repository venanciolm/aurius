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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

//CREATE TABLE REGISTRO_REG (
//		  OP_GRUPO char(6) NOT NULL DEFAULT '',
//		  DIC_ID int(5) unsigned zerofill NOT NULL,
//		  REG_ORDEN int(3) unsigned zerofill NOT NULL,
//		  OP_GRUPO0 char(6) NOT NULL,
//		  DIC_ID0 int(5) unsigned zerofill NOT NULL,
//		  REG_OBLIGATORIO int(1) unsigned zerofill DEFAULT NULL,
//		  REG_INT0 int(3) unsigned zerofill DEFAULT NULL,
//		  REG_INT1 int(3) unsigned zerofill DEFAULT NULL,
//		  PRIMARY KEY (OP_GRUPO,DIC_ID,REG_ORDEN)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * Dao que contiene los datos de los datos de un registro
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "REGISTRO_REG")
@org.hibernate.annotations.Proxy(lazy = true)
public class RegistroReg implements Serializable {

	@EmbeddedId
	private RegistroRegKey id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO0", referencedColumnName = "OP_GRUPO"),
			@JoinColumn(name = "DIC_ID0", referencedColumnName = "DIC_ID") })
	private DiccionarioDic elemento;

	@Column(name = "REG_OBLIGATORIO")
	private boolean obligatorio;

	@Column(name = "REG_INT0")
	private Integer cardinalidadMinima;

	@Column(name = "REG_INT1")
	private Integer cardinalidadMaxima;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public RegistroReg() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see java.lang.Object#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append("={");
		sb.append("id=").append(id);
		sb.append(", elemento=").append(elemento);
		sb.append(", obligatorio=").append(obligatorio);
		sb.append(", cardinalidadMinima=").append(cardinalidadMinima);
		sb.append(", cardinalidadMaxima=").append(cardinalidadMaxima);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public RegistroRegKey getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(RegistroRegKey id) {
		this.id = id;
	}

	/**
	 * @return the cardinalidadMinima
	 */
	public Integer getCardinalidadMinima() {
		return cardinalidadMinima;
	}

	/**
	 * @param cardinalidadMinima
	 *            the cardinalidadMinima to set
	 */
	public void setCardinalidadMinima(Integer cardinalidadMinima) {
		this.cardinalidadMinima = cardinalidadMinima;
	}

	/**
	 * @return the cardinalidadMaxima
	 */
	public Integer getCardinalidadMaxima() {
		return cardinalidadMaxima;
	}

	/**
	 * @param cardinalidadMaxima
	 *            the cardinalidadMaxima to set
	 */
	public void setCardinalidadMaxima(Integer cardinalidadMaxima) {
		this.cardinalidadMaxima = cardinalidadMaxima;
	}

	/**
	 * @return the elemento
	 */
	public DiccionarioDic getElemento() {
		return elemento;
	}

	/**
	 * @param elemento
	 *            the elemento to set
	 */
	public void setElemento(DiccionarioDic elemento) {
		this.elemento = elemento;
	}

	/**
	 * @return the obligatorio
	 */
	public boolean isObligatorio() {
		return obligatorio;
	}

	/**
	 * @param obligatorio
	 *            the obligatorio to set
	 */
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}
}
