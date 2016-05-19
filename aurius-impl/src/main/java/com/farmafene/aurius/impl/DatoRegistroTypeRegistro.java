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

import java.util.Map;

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.DatoRegistro;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;

/**
 * Implementación de un campo registro
 * 
 * @see TypeRegistro
 * @see DatoRegistro
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class DatoRegistroTypeRegistro extends DatoRegistroAbstract<Registro>
		implements TypeRegistro, DatoRegistro<Registro> {

	private Map<String, CampoRegistro> fields;
	private String[] elementsNames;

	/**
	 * Constructor
	 */
	public DatoRegistroTypeRegistro(String id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hashCode = super.hashCode();
		hashCode = hashCode * 31
				+ ((getFields() == null) ? 0 : getFields().hashCode());
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
		if (obj instanceof TypeRegistro) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
	}

	public Map<String, CampoRegistro> getFields() {
		return fields;
	}

	public void setFields(Map<String, CampoRegistro> fields) {
		this.fields = fields;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#fromString(java.lang.String)
	 */
	@Override
	protected Registro fromString(String objeto) {
		throw new UnsupportedOperationException(
				"Operación no soportada en este tipo de dato.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#toStringValue(java.lang.Object)
	 */
	@Override
	protected String toStringValue(Registro valor) {
		throw new UnsupportedOperationException(
				"Operación no soportada en este tipo de dato.");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.impl.DatoRegistroAbstract#validaValor(java.lang.Object)
	 */
	@Override
	protected void validaValor(Registro valor) throws AuriusValidationException {
		Registro reg = valor;
		if (!getId().equals(reg.getId())) {
			throw new AuriusValidationException(
					"El registro introducido es inválido: '" + reg.getId()
							+ "', vs. '" + this.getId() + "'");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see TypeRegistro#getNombres()
	 * @since 1.0.0
	 */
	public String[] getNombres() {
		return elementsNames;
	}

	/**
	 * @param elementsNames
	 *            the elementsNames to set
	 * @since 1.0.0
	 */
	public void setNombres(String[] elementsNames) {
		this.elementsNames = elementsNames;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.DatoRegistro#getValueType()
	 */
	@Override
	public Class<Registro> getValueType() {
		return Registro.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.TypeRegistro#getCampoRegistro(java.lang.String)
	 */
	@Override
	public CampoRegistro getCampoRegistro(String nombre) {
		CampoRegistro item = this.fields.get(nombre);
		if (null == item) {
			throw new IllegalArgumentException("El registro  con id '" + this.getId()
					+ "' no dispone del campo '" + nombre + "'");
		}
		return item;
	}
}
