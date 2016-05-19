package com.farmafene.aurius.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.DatoRegistro;
import com.farmafene.aurius.ELMap;
import com.farmafene.aurius.NewClearInstanciable;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.util.IWrapperClass;
import com.farmafene.aurius.util.ProxyFactory;
import com.farmafene.aurius.util.WrapperClassContainer;
import com.farmafene.aurius.xml.XMLReg;

@SuppressWarnings("serial")
public class RegistroImpl implements Registro, NewClearInstanciable<Registro>,
		ELMap, IWrapperClass {

	private static final ProxyFactory<Registro> proxyFactory = new ProxyFactory<Registro>();
	private Map<String, TypeRegistro> definiciones;
	private transient IWrapperClass iface;
	private XMLReg inner;

	public RegistroImpl(String id, Map<String, TypeRegistro> definiciones) {
		this(id, definiciones, new XMLReg());
	}

	public RegistroImpl(String id, Map<String, TypeRegistro> definiciones,
			XMLReg inner) {
		this.inner = inner;
		inner.setId(id);
		this.definiciones = definiciones;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return inner.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#isWrapperFor(java.lang.Class)
	 */
	@Override
	public <T> boolean isWrapperFor(Class<T> item) {
		boolean isWrapperFor = false;
		if (XMLReg.class.isAssignableFrom(item)) {
			isWrapperFor = true;
		} else {
			if (null == iface) {
				iface = new WrapperClassContainer(this);
			}
			isWrapperFor = iface.isWrapperFor(item);
		}
		return isWrapperFor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.util.IWrapperClass#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> item) {
		T unwrap = null;
		if (XMLReg.class.isAssignableFrom(item)) {
			@SuppressWarnings("unchecked")
			T unwrap2 = (T) inner;
			unwrap = unwrap2;
		} else {
			if (null == iface) {
				iface = new WrapperClassContainer(this);
			}
			unwrap = iface.unwrap(item);
		}
		return unwrap;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.ELMap#getValue()
	 */
	@Override
	public Map<String, Serializable> getValue() {
		return inner.getValue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.NewClearInstanciable#newClearInstance()
	 */
	@Override
	public Registro newClearInstance() {
		return proxyFactory.newInstance(new RegistroImpl(inner.getId(),
				definiciones));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getId()
	 */
	@Override
	public String getId() {
		return inner.getId();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends Serializable> T get(String name, Class<T> clazz)
			throws IllegalArgumentException {
		return inner.get(name, clazz);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(String name) throws IllegalArgumentException {
		return inner.getBytes(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String name) throws IllegalArgumentException {
		return inner.getDate(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getDecimal(String name) throws IllegalArgumentException {
		return inner.getDecimal(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) throws IllegalArgumentException {
		return inner.getString(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getRegistro(java.lang.String, int)
	 */
	@Override
	public Registro getRegistro(String name, int index)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		XMLReg r = (XMLReg) inner.getRegistro(name, index);
		return proxyFactory.newInstance(new RegistroImpl(r.getId(),
				definiciones, r));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getRegistroSize(java.lang.String)
	 */
	@Override
	public int getRegistroSize(String name) throws IllegalArgumentException {
		return inner.getRegistroSize(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#newRegistro(java.lang.String)
	 */
	@Override
	public Registro newRegistro(String name) throws IllegalArgumentException {
		Registro salida = null;
		TypeRegistro definicion = null;
		CampoRegistro campo = null;
		DatoRegistro<?> defDato = null;
		if (null == name) {
			throw new IllegalArgumentException("newRegistro(null) is an error");
		}
		try {
			definicion = definiciones.get(inner.getId());
			campo = definicion.getCampoRegistro(name);
			defDato = campo.getDatoRegistro();
			if (Registro.class.isAssignableFrom(defDato.getValueType())) {
				salida = proxyFactory.newInstance(new RegistroImpl(defDato
						.getId(), definiciones));
			} else {
				throw new IllegalArgumentException("El campo '" + name
						+ "' no es un registro");
			}
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (RuntimeException e) {
			throw new IllegalArgumentException(
					"Error en la factoria del registro '" + getId()
							+ "', campo '" + name + "'", e);
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#put(java.lang.String,
	 *      java.io.Serializable)
	 */
	@Override
	public void put(String name, Serializable value)
			throws IllegalArgumentException {
		TypeRegistro definicion = null;
		CampoRegistro campo = null;
		DatoRegistro<?> defDato = null;
		try {
			definicion = definiciones.get(inner.getId());
			campo = definicion.getCampoRegistro(name);
			if (null == campo) {
				throw new IllegalArgumentException("El campo '" + name
						+ "', no existe en el registro '" + getId() + "'");
			}
			defDato = campo.getDatoRegistro();
			inner.put(name, (Serializable) defDato.valida(value));
		} catch (RuntimeException e) {
			throw new IllegalArgumentException(
					"Error en la escritura del registro '" + getId()
							+ "', campo '" + name + "'", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#addRegistro(java.lang.String,
	 *      com.farmafene.aurius.Registro)
	 */
	@Override
	public void addRegistro(String name, Registro other)
			throws IllegalArgumentException {
		if (null != other) {
			XMLReg registro = null;
			if (other instanceof IWrapperClass) {
				IWrapperClass wc = (IWrapperClass) other;
				if (!wc.isWrapperFor(RegistroImpl.class)) {
					throw new UnsupportedOperationException(other.getClass()
							.getCanonicalName()
							+ ", incompatible with "
							+ this.getClass().getCanonicalName());
				}
				registro = wc.unwrap(RegistroImpl.class).inner;
			}
			TypeRegistro definicion = null;
			CampoRegistro campo = null;
			DatoRegistro<?> defDato = null;
			try {
				definicion = definiciones.get(inner.getId());
				campo = definicion.getCampoRegistro(name);
				if (null == campo) {
					throw new IllegalArgumentException("El campo '" + name
							+ "', no existe en el registro '" + getId()
							+ "' o no es un registro.");
				}
				defDato = campo.getDatoRegistro();
				defDato.valida(registro);
				inner.addRegistro(name, registro);
			} catch (RuntimeException e) {
				throw new IllegalArgumentException(
						"Error en al añadir en el registro '" + getId()
								+ "', campo '" + name + "'", e);
			}
		}
	}
}
