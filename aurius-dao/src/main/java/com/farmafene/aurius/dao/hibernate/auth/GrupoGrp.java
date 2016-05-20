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
package com.farmafene.aurius.dao.hibernate.auth;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.commons.hibernate.Datasource;

//CREATE TABLE GRUPO_GRP (
//		  GRP_ID CHAR(32) NOT NULL,
//		  GRP_DESC char(255) DEFAULT NULL,
//		  PRIMARY KEY (GRP_ID)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "GRUPO_GRP")
@org.hibernate.annotations.Proxy(lazy = true)
@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
public class GrupoGrp implements Serializable {
	@Id
	@Column(name = "GRP_ID")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	@Column(name = "GRP_DESC")
	private String descripcion;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GRP_GRP", joinColumns = { @JoinColumn(name = "GRP_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "GRP_ID1", nullable = false, updatable = false) })
	private Set<GrupoGrp> grupos;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USR_ROL", joinColumns = { @JoinColumn(name = "GRP_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROL_ID", nullable = false, updatable = false) })
	private Set<RoleRol> roles;

	public GrupoGrp() {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("id=").append(id);
		sb.append(", descripcion=").append(descripcion);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GrupoGrp)) {
			return false;
		}
		GrupoGrp other = (GrupoGrp) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		return true;
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

	/**
	 * @return the grupos
	 */
	public Set<GrupoGrp> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos
	 *            the grupos to set
	 */
	public void setGrupos(Set<GrupoGrp> grupos) {
		this.grupos = grupos;
	}

	/**
	 * @return the roles
	 */
	public Set<RoleRol> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<RoleRol> roles) {
		this.roles = roles;
	}
}
