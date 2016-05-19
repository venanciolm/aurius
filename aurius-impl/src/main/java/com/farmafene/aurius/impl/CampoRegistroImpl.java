/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.impl;

import java.io.Serializable;
import java.util.List;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.DatoRegistro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.Registro;

/**
 * Implementación de un campo de registro
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class CampoRegistroImpl implements CampoRegistro {

	private String name;
	private DatoRegistro<Serializable> datoRegistro;
	private boolean obligatorio;
	private Integer cardinalidadMinima;
	private Integer cardinalidadMaxima;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	public CampoRegistroImpl() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CampoRegistro#valida(java.lang.Object)
	 * @since 1.0.0
	 */
	public void valida(Object valor) throws AuriusValidationException {
		if (((Object) getDatoRegistro()) instanceof TypeRegistro) {
			@SuppressWarnings("unchecked")
			List<Registro> registros = (List<Registro>) valor;
			if (null != getCardinalidadMinima()
					&& registros.size() < getCardinalidadMinima().intValue()) {
				throw new AuriusValidationException("El número de registros '"
						+ name + "' introducidos es inferior al mínimo.");
			}
			if (null != getCardinalidadMaxima()
					&& registros.size() > getCardinalidadMaxima().intValue()) {
				throw new AuriusValidationException("El número de registros '"
						+ name + "' introducidos es superior al máximo.");
			}
		} else if (null == valor && isObligatorio()) {
			throw new AuriusValidationException("El campo '" + name
					+ "' es obligatorio.");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("nombre=").append(getNombre());
		if (null != getCardinalidadMinima()) {
			sb.append(", cardinalidadMínima=").append(getCardinalidadMinima());
		}
		if (null != getCardinalidadMaxima()) {
			sb.append(", cardinalidadMáxima=").append(getCardinalidadMaxima());
		}
		sb.append(", datoRegistro=").append(getDatoRegistro());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode * 31
				+ ((getNombre() == null) ? 0 : getNombre().hashCode());
		hashCode = hashCode
				* 31
				+ ((getCardinalidadMinima() == null) ? 0
						: getCardinalidadMinima().hashCode());
		hashCode = hashCode
				* 31
				+ ((getCardinalidadMaxima() == null) ? 0
						: getCardinalidadMaxima().hashCode());
		return hashCode;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj instanceof CampoRegistroImpl) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CampoRegistro#getDatoRegistro()
	 * @since 1.0.0
	 */
	public DatoRegistro<Serializable> getDatoRegistro() {
		return this.datoRegistro;
	}

	/**
	 * @param datoRegistro
	 *            the datoRegistro to set
	 * @since 1.0.0
	 */
	public void setDatoRegistro(DatoRegistro<Serializable> datoRegistro) {
		this.datoRegistro = datoRegistro;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see CampoRegistro#getName()
	 * @since 1.0.0
	 */
	public String getNombre() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 * @since 1.0.0
	 */
	public void setNombre(String name) {
		this.name = name;
	}

	/**
	 * @return the cardinalidadMinima
	 * @since 1.0.0
	 */
	public Integer getCardinalidadMinima() {
		return this.cardinalidadMinima;
	}

	/**
	 * @param cardinalidadMinima
	 *            the cardinalidadMinima to set
	 * @since 1.0.0
	 */
	public void setCardinalidadMinima(Integer cardinalidadMinima) {
		this.cardinalidadMinima = cardinalidadMinima;
	}

	/**
	 * @return the cardinalizadMaxima
	 * @since 1.0.0
	 */
	public Integer getCardinalidadMaxima() {
		return this.cardinalidadMaxima;
	}

	/**
	 * @param cardinalizadMaxima
	 *            the cardinalizadMaxima to set
	 * @since 1.0.0
	 */
	public void setCardinalidadMaxima(Integer cardinalizadMaxima) {
		this.cardinalidadMaxima = cardinalizadMaxima;
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
