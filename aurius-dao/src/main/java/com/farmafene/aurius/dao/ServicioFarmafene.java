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
package com.farmafene.aurius.dao;

import java.util.HashSet;
import java.util.Set;

import com.farmafene.aurius.core.Servicio;

/**
 * @author vlopez
 * 
 */
@SuppressWarnings("serial")
public class ServicioFarmafene implements Servicio {

	private String id;
	private String version;
	private String idRegistroSalida;
	private String idRegistroEntrada;
	private String idAplicacion;
	private String command;
	private Set<String> roles;
	private boolean inheritAuth;
	private boolean publica;
	private boolean disponible;
	private String motivoIndisponibilidad;

	/**
	 * Constructor por defecto
	 */
	public ServicioFarmafene() {
		this.roles = new HashSet<String>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append("={");
		sb.append("id=").append(id);
		if (version != null) {
			sb.append(", version=").append(version);
		}
		sb.append(", disponible=").append(disponible);
		if (!disponible) {
			sb.append(", motivo=").append(motivoIndisponibilidad);
		}
		sb.append(", idRegistroEntrada=").append(idRegistroEntrada);
		sb.append(", idRegistroSalida=").append(idRegistroSalida);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getCommand()
	 */
	@Override
	public String getCommand() {
		return command;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getIdAplicacion()
	 */
	@Override
	public String getIdAplicacion() {
		return idAplicacion;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getIdRegistroEntrada()
	 */
	@Override
	public String getIdRegistroEntrada() {
		return idRegistroEntrada;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getIdRegistroSalida()
	 */
	@Override
	public String getIdRegistroSalida() {
		return idRegistroSalida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getMotivoIndisponibilidad()
	 */
	@Override
	public String getMotivoIndisponibilidad() {
		return motivoIndisponibilidad;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getVersion()
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#isDisponible()
	 */
	@Override
	public boolean isDisponible() {
		return disponible;
	}

	/**
	 * @param disponible
	 *            the disponible to set
	 */
	protected void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	protected void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param motivoIndisponibilidad
	 *            the motivoIndisponibilidad to set
	 */
	protected void setMotivoIndisponibilidad(String motivoIndisponibilidad) {
		this.motivoIndisponibilidad = motivoIndisponibilidad;
	}

	/**
	 * @param idRegistroSalida
	 *            the idRegistroSalida to set
	 */
	protected void setIdRegistroSalida(String idRegistroSalida) {
		this.idRegistroSalida = idRegistroSalida;
	}

	/**
	 * @param idRegistroEntrada
	 *            the idRegistroEntrada to set
	 */
	protected void setIdRegistroEntrada(String idRegistroEntrada) {
		this.idRegistroEntrada = idRegistroEntrada;
	}

	/**
	 * @param idAplicacion
	 *            the idAplicacion to set
	 */
	protected void setIdAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	protected void setCommand(String command) {
		this.command = command;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#isPublic()
	 */
	@Override
	public boolean isPublic() {
		return publica;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#isInheritAuth()
	 */
	@Override
	public boolean isInheritAuth() {
		return inheritAuth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.Servicio#getRoles()
	 */
	@Override
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public void setInheritAuth(boolean inheritAuth) {
		this.inheritAuth = inheritAuth;
	}

	public void setPublic(boolean publica) {
		this.publica = publica;
	}
}
