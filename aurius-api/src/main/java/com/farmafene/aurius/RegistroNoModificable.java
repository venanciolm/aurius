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
package com.farmafene.aurius;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author vlopez
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class RegistroNoModificable implements Registro {

	private Registro registro;

	private RegistroNoModificable() {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (registro != null) {
			return registro.toString();
		}
		return getClass().getSimpleName() + "={}";
	}

	/**
	 * Constructor
	 * 
	 * @param reg
	 * @since 1.0.0
	 */
	public RegistroNoModificable(Registro reg) {
		this();
		this.registro = reg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void addRegistro(String arg0, Registro arg1) {
		throw new UnsupportedOperationException(
				"La modificación no está permitida!!!!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public <T extends Serializable> T get(String arg0, Class<T> clazz)
			throws IllegalArgumentException {
		return getRegistro().get(arg0, clazz);
	}

	private Registro getRegistro() {
		if (this.registro == null) {
			throw new IllegalArgumentException(
					"El registro original no puede ser Null");
		}
		return this.registro;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public byte[] getBytes(String nombre) throws IllegalArgumentException {
		byte[] salida = null;
		byte[] original = getRegistro().getBytes(nombre);
		if (null != original) {
			salida = new byte[original.length];
			if (salida.length > 0) {
				System.arraycopy(original, 0, salida, 0, original.length);
			}
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public Date getDate(String arg0) throws IllegalArgumentException {
		return getRegistro().getDate(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public BigDecimal getDecimal(String arg0) throws IllegalArgumentException {
		return getRegistro().getDecimal(arg0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getId() {
		return getRegistro().getId();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public Registro getRegistro(String nombre, int index)
			throws IllegalArgumentException {
		return new RegistroNoModificable(getRegistro().getRegistro(nombre,
				index));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public int getRegistroSize(String nombre) throws IllegalArgumentException {
		return getRegistro().getRegistroSize(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String getString(String nombre) throws IllegalArgumentException {
		return getRegistro().getString(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public Registro newRegistro(String nombre) throws IllegalArgumentException {
		return new RegistroNoModificable(getRegistro().newRegistro(nombre));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void put(String nombre, Serializable value)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException(
				"La modificación no está permitida!!!!");
	}
}
