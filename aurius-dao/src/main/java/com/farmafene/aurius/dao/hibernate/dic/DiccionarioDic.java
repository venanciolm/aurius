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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

//CREATE TABLE DICCIONARIO_DIC (
//		  OP_GRUPO char(6) NOT NULL,
//		  DIC_ID int(5) unsigned zerofill NOT NULL,
//		  DIC_TIPO int(1) unsigned zerofill DEFAULT NULL,
//		  DICD_ID INT(10) UNSIGNED ZEROFILL NOT NULL,
//		  DIC_NOMBRE varchar(50) NOT NULL,
//		  DIC_LONG0 int(10) unsigned zerofill DEFAULT NULL,
//		  DIC_LONG1 int(10) unsigned zerofill DEFAULT NULL,
//		  DIC_STR0 varchar(50) DEFAULT NULL,
//		  DIC_STR1 varchar(50) DEFAULT NULL,
//		  DIC_STR2 varchar(50) DEFAULT NULL,
//		  DIC_STR3 varchar(50) DEFAULT NULL,
//		  DIC_STR4 varchar(50) DEFAULT NULL,
//		  DIC_STR5 varchar(50) DEFAULT NULL,
//		  PRIMARY KEY (OP_GRUPO,DIC_ID)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/**
 * <p>
 * Dao que contiene los datos del diccionario.
 * </p>
 * <p>
 * Se trata de una clase abstracta, con determinadas implementaciones
 * </p>
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DIC_TIPO", discriminatorType = DiscriminatorType.INTEGER)
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "DICCIONARIO_DIC")
@org.hibernate.annotations.Proxy(lazy = true)
public abstract class DiccionarioDic implements Serializable {

	@EmbeddedId
	private DiccionarioDicKey id = new DiccionarioDicKey();

	@Column(name = "DIC_NOMBRE")
	private String nombre;

	@OneToOne(optional = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "DICD_ID", referencedColumnName = "DICD_ID")
	private DiccionarioDesc descripcion;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public DiccionarioDic() {

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
		sb.append(", nombre").append(nombre);
		sb.append("}");
		return sb.toString();
	}

	public DiccionarioDesc getDescripcion() {
		return this.descripcion;
	}

	/**
	 * @return the id
	 */
	public DiccionarioDicKey getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(DiccionarioDicKey id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(DiccionarioDesc descripcion) {
		this.descripcion = descripcion;
	}
}
