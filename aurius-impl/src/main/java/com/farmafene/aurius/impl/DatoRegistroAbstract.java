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

import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.DatoRegistro;

/**
 * Contenedor básico de datos para los datos de diccionario.
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class DatoRegistroAbstract<T> implements DatoRegistro<T> {

	private String id;
	private String name;

	/**
	 * Constructor
	 * 
	 * @since 1.0.0
	 */
	private DatoRegistroAbstract() {

	}

	/**
	 * Constructor del tipo de dato.
	 * 
	 * @param id
	 *            Identificador de diccionario del tipo de dato
	 * @throws IllegalArgumentException
	 *             si el Id no es válido
	 * @since 1.0.0
	 */
	public DatoRegistroAbstract(String id) throws IllegalArgumentException {
		this();
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo");
		}
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		int hashCode = 7;
		hashCode = hashCode * 31 + ((getId() == null) ? 0 : getId().hashCode());
		hashCode = hashCode * 31
				+ ((getNombre() == null) ? 0 : getNombre().hashCode());
		return hashCode;
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
		sb.append("id=").append(getId());
		if (null != getNombre()) {
			sb.append(", nombre=").append(getNombre());
		}
		sb.append("}");
		return sb.toString();
	}

	protected abstract void validaValor(T objeto)
			throws AuriusValidationException;

	protected abstract T fromString(String objeto);

	protected abstract String toStringValue(T valor);

	@Override
	public String toString(T valor) throws IllegalArgumentException {
		try {
			return toStringValue(valor);
		} catch (ClassCastException cce) {
			// do nothing
		}
		throw new IllegalArgumentException(valor + ", incompatible con " + this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.DatoRegistro#valida(java.lang.Object)
	 */
	@Override
	public T valida(Object valor) throws AuriusValidationException {
		T salida = null;
		if (valor != null) {
			if (valor instanceof String) {
				salida = fromString((String) valor);
			} else {
				@SuppressWarnings("unchecked")
				T valorUnck = (T) valor;
				salida = valorUnck;
			}
			validaValor(salida);
		}
		return salida;
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
		if (obj instanceof DatoRegistroAbstract<?>) {
			iguales = this.hashCode() == obj.hashCode();
		}
		return iguales;
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
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#getId()
	 * @since 1.0.0
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see DatoRegistro#getNombre()()
	 * @since 1.0.0
	 */
	public String getNombre() {
		return this.name;
	}
}
