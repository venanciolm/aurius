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
package com.farmafene.aurius.xml;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.farmafene.aurius.ELMap;
import com.farmafene.aurius.Registro;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamInclude;

@SuppressWarnings("serial")
@XStreamAlias("Registro")
@XStreamInclude(value = { XMLRegA.class, XMLBigDecimal.class, XMLString.class,
		XMLDate.class, XMLBytes.class })
@XStreamConverter(XMLRegConverter.class)
public class XMLReg implements Registro, ELMap, Cloneable {

	private String id;
	private String name;
	private Map<String, XMLField<?>> values = new TreeMap<String, XMLField<?>>();
	private transient XStream xStream;

	public XMLReg() {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getXStream().toXML(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public XMLReg clone() throws CloneNotSupportedException {
		return (XMLReg) super.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends Serializable> T get(String name, Class<T> clazz)
			throws IllegalArgumentException {
		return getRawValue(name, clazz);
	}

	@Override
	public byte[] getBytes(String name) throws IllegalArgumentException {
		return getRawValue(name, byte[].class);
	}

	@Override
	public Date getDate(String name) throws IllegalArgumentException {
		return getRawValue(name, Date.class);
	}

	@Override
	public BigDecimal getDecimal(String name) throws IllegalArgumentException {
		return getRawValue(name, BigDecimal.class);
	}

	@Override
	public String getString(String name) throws IllegalArgumentException {
		return getRawValue(name, String.class);
	}

	@Override
	public Registro getRegistro(String name, int index)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		Registro reg = null;
		XMLRegA aReg = getRawValue(name, XMLRegA.class);
		if (aReg != null) {
			reg = aReg.getValue().get(index);
		}
		return reg;
	}

	@Override
	public int getRegistroSize(String name) throws IllegalArgumentException {
		int size = 0;
		XMLRegA aReg = getRawValue(name, XMLRegA.class);
		if (aReg != null) {
			size = aReg.getValue().size();
		}
		return size;
	}

	@Override
	public Registro newRegistro(String name) throws IllegalArgumentException {
		XMLReg reg = new XMLReg();
		reg.setName(name);
		return reg;
	}

	@Override
	public void put(String name, Serializable value)
			throws IllegalArgumentException {
		if (value != null && Registro.class.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException(Registro.class
					+ ", not asignable by this method");
		}
		setRawValue(name, value);
	}

	@Override
	public void addRegistro(String name, Registro registro)
			throws IllegalArgumentException {
		if (null == registro) {
			throw new IllegalArgumentException("Null no soportado");
		} else if (!XMLReg.class.isAssignableFrom(registro.getClass())) {
			throw new IllegalArgumentException(registro.getClass() + ", vs. "
					+ XMLReg.class);
		}
		XMLRegA aReg = getRawValue(name, XMLRegA.class);
		if (aReg == null) {
			aReg = new XMLRegA();
			aReg.setName(name);
			aReg.setValue(new ArrayList<XMLReg>());
			values.put(name, aReg);
		}
		XMLReg xmlReg = (XMLReg) registro;
		xmlReg.setName(aReg.getName());
		aReg.getValue().add(xmlReg);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	private XStream getXStream() {
		if (null == xStream) {
			xStream = new XStream();
			xStream.processAnnotations(XMLReg.class);
			xStream.setMode(XStream.NO_REFERENCES);
		}
		return xStream;
	}

	private <T extends Serializable> T getRawValue(String name, Class<T> clazz) {
		T output = null;
		if (null == name) {
			throw new IllegalArgumentException("getRawValue(null," + clazz
					+ "): Debe establecerse el campo");
		}
		if (null == clazz) {
			throw new IllegalArgumentException("getRawValue(" + name
					+ ",null): Debe establecerse la clase");
		}
		@SuppressWarnings("unchecked")
		XMLField<Serializable> value = (XMLField<Serializable>) values
				.get(name);
		Class<?> inClass = clazz;
		if (null != value) {
			inClass = getReturnClass(value);
			if (XMLRegA.class.equals(inClass)) {
				@SuppressWarnings("unchecked")
				T readValue = (T) value;
				output = readValue;
			} else {
				if (!inClass.isAssignableFrom(value.getValue().getClass())) {
					throw new IllegalArgumentException("No compatible: '"
							+ inClass + "' vs. '" + value.getValue().getClass()
							+ "'");
				}
				@SuppressWarnings("unchecked")
				T readValue = (T) value.getValue();
				output = readValue;
			}
		}
		return output;
	}

	private Class<? extends Serializable> getReturnClass(
			XMLField<Serializable> value) {
		Class<? extends Serializable> clazz = null;
		if (value != null) {
			if (XMLRegA.class.isAssignableFrom(value.getClass())) {
				clazz = XMLRegA.class;
			} else if (XMLBigDecimal.class.isAssignableFrom(value.getClass())) {
				clazz = BigDecimal.class;
			} else if (XMLString.class.isAssignableFrom(value.getClass())) {
				clazz = String.class;
			} else if (XMLDate.class.isAssignableFrom(value.getClass())) {
				clazz = Date.class;
			} else if (XMLBytes.class.isAssignableFrom(value.getClass())) {
				clazz = byte[].class;
			} else {
				throw new UnsupportedOperationException(value.getClass()
						+ ", not supported");
			}
		}
		return clazz;
	}

	/**
	 * Parte privada de introducción de datos
	 * 
	 * @param name
	 * @param value
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setRawValue(String name, Serializable value)
			throws IllegalArgumentException {
		if (null == name) {
			throw new IllegalArgumentException("setRawValue(null," + value
					+ "): Debe establecerse el campo");
		}
		XMLField<Serializable> oldValue = (XMLField<Serializable>) values
				.get(name);
		if (null != oldValue && null == value) {
			values.remove(name);
		} else if (value == null) {
			// do nothing, all is null
		} else if (null == oldValue) {
			Class<?> clazz = value.getClass();
			XMLField<Serializable> newObject = null;
			if (String.class.isAssignableFrom(clazz)) {
				newObject = (XMLField) new XMLString();
			} else if (BigDecimal.class.isAssignableFrom(clazz)) {
				newObject = (XMLField) new XMLBigDecimal();
			} else if (Date.class.isAssignableFrom(clazz)) {
				newObject = (XMLField) new XMLDate();
			} else if (byte[].class.isAssignableFrom(clazz)) {
				newObject = (XMLField) new XMLBytes();
			} else {
				throw new UnsupportedOperationException(value.getClass()
						+ ", not supported");
			}
			newObject.setName(name);
			values.put(name, newObject);
			setRawValue(name, value);
		} else {
			oldValue.setValue(value);
		}
	}

	Map<String, XMLField<?>> getXMLFieldsValues() {
		return values;
	}

	void setXMLFieldsValues(Map<String, XMLField<?>> values) {
		this.values = values;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ELMap#getValue()
	 */
	public Map<String, Serializable> getValue() {
		@SuppressWarnings("rawtypes")
		Map a = Collections.unmodifiableMap(new MapAdapter(values));
		@SuppressWarnings("unchecked")
		Map<String, Serializable> v = (Map<String, Serializable>) a;
		return v;
	}
}