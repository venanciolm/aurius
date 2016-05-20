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
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.farmafene.aurius.dao.hibernate.AuriusSessionFactory;
import com.farmafene.aurius.dao.hibernate.auth.RoleRol;
import com.farmafene.commons.hibernate.Datasource;

//CREATE TABLE SERVICIO_SVRD (
//		  OP_GRUPO char(6) NOT NULL,
//		  SVR_ID int(3) unsigned zerofill NOT NULL,
//		  SVR_VERSION int(2) unsigned zerofill DEFAULT NULL,
//		  DICD_ID INT(10) UNSIGNED ZEROFILL NOT NULL,
//		  SVRD_DESC varchar(255) DEFAULT NULL,
//		  PRIMARY KEY (OP_GRUPO,SVR_ID)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
@Table(name = "SERVICIO_SVRD")
@org.hibernate.annotations.Proxy(lazy = true)
public class ServicioDesc implements Serializable {

	@EmbeddedId
	private ServicioDescKey id;
	@Column(name = "SVRD_DESC")
	private String descripcion;
	@Column(name = "SVR_VERSION")
	private BigDecimal version;
	@Column(name = "SVR_INHERIT")
	private boolean inheritAuth;
	@Column(name = "SVR_PUBLIC")
	private boolean publica;

	@OneToOne(optional = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "DICD_ID", referencedColumnName = "DICD_ID")
	private DiccionarioDesc descripcionLarga;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "OP_GRUPO", referencedColumnName = "OP_GRUPO"),
			@JoinColumn(name = "SVR_ID", referencedColumnName = "SVR_ID") })
	@OrderBy(value = "id.version DESC")
	private List<ServicioSvr> servicios;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SVR_ROL", joinColumns = {
			@JoinColumn(name = "OP_GRUPO", nullable = false, updatable = false),
			@JoinColumn(name = "SVR_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROL_ID", nullable = false, updatable = false) })
	private Set<RoleRol> roles;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public ServicioDesc() {

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
		sb.append(", descripcion=").append(descripcion);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public ServicioDescKey getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ServicioDescKey id) {
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
	 * @return the version
	 */
	public BigDecimal getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	/**
	 * @return the descripcionLarga
	 */
	public DiccionarioDesc getDescripcionLarga() {
		return descripcionLarga;
	}

	/**
	 * @param descripcionLarga
	 *            the descripcionLarga to set
	 */
	public void setDescripcionLarga(DiccionarioDesc descripcionLarga) {
		this.descripcionLarga = descripcionLarga;
	}

	/**
	 * @return the servicios
	 */
	public List<ServicioSvr> getServicios() {
		return servicios;
	}

	/**
	 * @param servicios
	 *            the servicios to set
	 */
	public void setServicios(List<ServicioSvr> servicios) {
		this.servicios = servicios;
	}

	/**
	 * @return the inheritAuth
	 */
	public boolean isInheritAuth() {
		return inheritAuth;
	}

	/**
	 * @param inheritAuth
	 *            the inheritAuth to set
	 */
	public void setInheritAuth(boolean inheritAuth) {
		this.inheritAuth = inheritAuth;
	}

	/**
	 * @return the publica
	 */
	public boolean isPublic() {
		return publica;
	}

	/**
	 * @param publica
	 *            the publica to set
	 */
	public void setPublic(boolean publica) {
		this.publica = publica;
	}

	/**
	 * @return the roles
	 */
	public Set<RoleRol> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<RoleRol> roles) {
		this.roles = roles;
	}
}
