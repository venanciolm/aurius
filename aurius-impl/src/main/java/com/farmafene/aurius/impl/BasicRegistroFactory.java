package com.farmafene.aurius.impl;

import com.farmafene.aurius.IBasicRegistroFactory;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.xml.XMLReg;

public class BasicRegistroFactory implements IBasicRegistroFactory {

	public BasicRegistroFactory() {

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
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IBasicRegistroFactory#getSupports()
	 */
	@Override
	public Class<? extends Registro> getSupports() {
		return XMLReg.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IBasicRegistroFactory#getNewRegistro(java.lang.String)
	 */
	@Override
	public Registro getNewRegistro(String id) {
		XMLReg reg = new XMLReg();
		if (null != id) {
			reg.setId(id);
		}
		return reg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return "Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return "1.0";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return "XML IBasicRegistroFactory";
	}
}
