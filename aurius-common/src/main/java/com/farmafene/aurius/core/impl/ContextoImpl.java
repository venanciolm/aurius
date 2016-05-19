/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.farmafene.aurius.AuthInfo;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.server.IAuriusUser;

/**
 * Implementación del contexto
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
class ContextoImpl implements ContextoCore, Cloneable {

	private long initTime = 0;
	private UUID id = null;
	private AuthInfo authInfo;
	private IAuriusUser iAuriusUser;
	private String idServicio;
	private String idServicioActual;
	private String versionServicioActual;
	private String versionServicio;
	private HashMap<String, IAplicacionHolder> aplicationHolders;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	ContextoImpl() {

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
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public AuthInfo getAuthInfo() {
		return this.authInfo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public UUID getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getIdServicio() {
		return this.idServicio;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getIdServicioActual() {
		return this.idServicioActual;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getVersionServicioActual() {
		return this.versionServicioActual;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getVersionServicio() {
		return this.versionServicio;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public long getInitTime() {
		return this.initTime;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param initTime
	 *            the initTime to set
	 */
	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param id
	 *            the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param authInfo
	 *            the authInfo to set
	 */
	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param idServicio
	 *            the idServicio to set
	 */
	public void setIdServicio(String idServicio) {
		this.idServicio = idServicio;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param idServicioActual
	 *            the idServicioActual to set
	 */
	public void setIdServicioActual(String idServicioActual) {
		this.idServicioActual = idServicioActual;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param versionServicioActual
	 *            the versionServicioActual to set
	 */
	public void setVersionServicioActual(String versionServicioActual) {
		this.versionServicioActual = versionServicioActual;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @param versionServicio
	 *            the versionServicio to set
	 */
	public void setVersionServicio(String versionServicio) {
		this.versionServicio = versionServicio;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public Map<String, IAplicacionHolder> getIAplicacionHolders() {
		if (this.aplicationHolders == null) {
			this.aplicationHolders = new HashMap<String, IAplicacionHolder>();
		}
		return this.aplicationHolders;
	}

	/**
	 * @return the iAuriusUser
	 */
	public IAuriusUser getIAuriusUser() {
		return iAuriusUser;
	}

	/**
	 * @param iAuriusUser the iAuriusUser to set
	 */
	public void setIAuriusUser(IAuriusUser iAuriusUser) {
		this.iAuriusUser = iAuriusUser;
	}
}
