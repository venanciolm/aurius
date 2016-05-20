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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

//create table SERVICIO_SVR (
//		OP_GRUPO	CHAR(6) NOT NULL,
//		SVR_ID		INT(3) UNSIGNED ZEROFILL NOT NULL,
//		SVR_VERSION	INT(2) UNSIGNED ZEROFILL NULL DEFAULT 0,
//		SVR_COMMAND VARCHAR(255) NOT NULL,
//		OP_GRUPO0	CHAR(6),
//		DIC_ID0		INT(5) UNSIGNED ZEROFILL,
//		OP_GRUPO1	CHAR(6),
//		DIC_ID1		INT(5) UNSIGNED ZEROFILL,
//		PRIMARY KEY (OP_GRUPO,SVR_ID,SVR_VERSION)
//	);
/**
 * Dao que contiene los datos de servicios
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "SERVICIO_SVR")
@org.hibernate.annotations.Proxy(lazy = true)
public class ServicioSvr implements Serializable {

	@EmbeddedId
	private ServicioSvrKey id = new ServicioSvrKey();

	@Column(name = "SVR_COMMAND")
	private String command;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO0", referencedColumnName = "OP_GRUPO"),
			@JoinColumn(name = "DIC_ID0", referencedColumnName = "DIC_ID") })
	private TypeRegistroVO registroEntrada;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO1", referencedColumnName = "OP_GRUPO"),
			@JoinColumn(name = "DIC_ID1", referencedColumnName = "DIC_ID") })
	private TypeRegistroVO registroSalida;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO", insertable = false, updatable = false),
			@JoinColumn(name = "SVR_ID", insertable = false, updatable = false) })
	private ServicioDesc descripcion;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public ServicioSvr() {

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
		sb.append(", command=").append(command);
		sb.append(", registroEntrada=").append(registroEntrada);
		sb.append(", registroSalida=").append(registroSalida);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public ServicioSvrKey getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ServicioSvrKey id) {
		this.id = id;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the registroEntrada
	 */
	public TypeRegistroVO getRegistroEntrada() {
		return registroEntrada;
	}

	/**
	 * @param registroEntrada
	 *            the registroEntrada to set
	 */
	public void setRegistroEntrada(TypeRegistroVO registroEntrada) {
		this.registroEntrada = registroEntrada;
	}

	/**
	 * @return the registroSalida
	 */
	public TypeRegistroVO getRegistroSalida() {
		return registroSalida;
	}

	/**
	 * @param registroSalida
	 *            the registroSalida to set
	 */
	public void setRegistroSalida(TypeRegistroVO registroSalida) {
		this.registroSalida = registroSalida;
	}

	/**
	 * @return the descripcion
	 */
	public ServicioDesc getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(ServicioDesc descripcion) {
		this.descripcion = descripcion;
	}
}
