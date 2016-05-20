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

//CREATE TABLE USUARIO_USR (
//		  USR_ID CHAR(32) NOT NULL,
//		  USR_LOGIN char(8) DEFAULT NULL,
//		  USR_ACTIVO char(1) DEFAULT NULL,
//		  USR_PASSWORD char(50) DEFAULT NULL,
//		  USR_NOMBRE char(50) DEFAULT NULL,
//		  USR_APELLIDO1 char(50) DEFAULT NULL,
//		  USR_APELLIDO2 char(50) DEFAULT NULL,
//		  USR_COUNTRY char(2) DEFAULT NULL,
//		  USR_LANG char(2) DEFAULT NULL,
//		  USR_VARIANT char(25) DEFAULT NULL,
//		  PRIMARY KEY (USR_ID),
//		  UNIQUE KEY INX_LOGIN (USR_LOGIN),
//		  KEY INX_ACTIVO (USR_ACTIVO,USR_LOGIN)
//		) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
@SuppressWarnings("serial")
@Entity
@Datasource(refName = AuriusSessionFactory.AURIUS_DB)
@Table(name = "USUARIO_USR")
@org.hibernate.annotations.Proxy(lazy = true)
@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
public class UserUsr implements Serializable {
	@Id
	@Column(name = "USR_ID")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	@Column(name = "USR_LOGIN")
	private String login;
	@Column(name = "USR_ACTIVO")
	private char activo;
	@Column(name = "USR_PASSWORD")
	private String password;
	@Column(name = "USR_NOMBRE")
	private String nombre;
	@Column(name = "USR_APELLIDO1")
	private String apellido1;
	@Column(name = "USR_APELLIDO2")
	private String apellido2;
	@Column(name = "USR_COUNTRY")
	private String country;
	@Column(name = "USR_LANG")
	private String lang;
	@Column(name = "USR_VARIANT")
	private String variant;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USR_GRP", joinColumns = { @JoinColumn(name = "USR_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "GRP_ID", nullable = false, updatable = false) })
	private Set<GrupoGrp> grupos;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USR_ROL", joinColumns = { @JoinColumn(name = "USR_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROL_ID", nullable = false, updatable = false) })
	private Set<RoleRol> roles;

	public UserUsr() {
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
		sb.append(", login=").append(login);
		sb.append(", activo=").append(activo);
		sb.append(", password=").append(password);
		sb.append(", nombre=").append(nombre);
		sb.append(", apellido1=").append(apellido1);
		sb.append(", apellido2=").append(apellido2);
		sb.append(", country=").append(country);
		sb.append(", lang=").append(lang);
		sb.append(", variant=").append(variant);
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
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserUsr))
			return false;
		UserUsr other = (UserUsr) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (activo != other.activo)
			return false;
		if (apellido1 == null) {
			if (other.apellido1 != null)
				return false;
		} else if (!apellido1.equals(other.apellido1))
			return false;
		if (apellido2 == null) {
			if (other.apellido2 != null)
				return false;
		} else if (!apellido2.equals(other.apellido2))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (variant == null) {
			if (other.variant != null)
				return false;
		} else if (!variant.equals(other.variant))
			return false;
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the activo
	 */
	public char getActivo() {
		return activo;
	}

	/**
	 * @param activo
	 *            the activo to set
	 */
	public void setActivo(char activo) {
		this.activo = activo;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the apellido1
	 */
	public String getApellido1() {
		return apellido1;
	}

	/**
	 * @param apellido1
	 *            the apellido1 to set
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	/**
	 * @return the apellido2
	 */
	public String getApellido2() {
		return apellido2;
	}

	/**
	 * @param apellido2
	 *            the apellido2 to set
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the variant
	 */
	public String getVariant() {
		return variant;
	}

	/**
	 * @param variant
	 *            the variant to set
	 */
	public void setVariant(String variant) {
		this.variant = variant;
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
