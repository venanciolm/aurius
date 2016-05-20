/* Copyright (c) 2009-2013 farmafene.com
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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Clave del DAO de Registros
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Embeddable
public class RegistroRegKey implements Serializable {
	@Column(name = "OP_GRUPO")
	private String grupo;

	@Column(name = "DIC_ID")
	private BigDecimal id;

	@Column(name = "REG_ORDEN")
	private BigDecimal orden;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public RegistroRegKey() {

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
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("grupo=").append(grupo);
		sb.append(", id=").append(id);
		sb.append(", orden=").append(orden);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == grupo ? 0 : grupo.hashCode());
		hash = 31 * hash + (null == id ? 0 : id.hashCode());
		hash = 31 * hash + (null == orden ? 0 : orden.hashCode());
		return hash;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(Object that) {
		if ((that == null) || !(that instanceof RegistroRegKey)) {
			return false;
		}
		RegistroRegKey other = (RegistroRegKey) that;
		if (equals(this.grupo, other.grupo)) {
			if (equals(this.id, other.id)) {
				if (equals(this.orden, other.orden)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private boolean equals(Object este, Object otro) {
		if (este != null) {
			if (!este.equals(otro)) {
				return false;
			}
		} else {
			if (otro != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo
	 *            the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * @return the orden
	 */
	public BigDecimal getOrden() {
		return orden;
	}

	/**
	 * @param orden
	 *            the orden to set
	 */
	public void setOrden(BigDecimal orden) {
		this.orden = orden;
	}
}
