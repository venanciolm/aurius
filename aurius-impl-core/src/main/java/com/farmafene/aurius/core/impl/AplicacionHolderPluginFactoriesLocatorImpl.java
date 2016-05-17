/*
 * Copyright (c) 2009-2014 farmafene.com
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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAplicacionHolderPluginFactoriesLocator;
import com.farmafene.aurius.core.IAplicacionHolderPluginFactory;

/**
 * @author vlopez
 * 
 */
public class AplicacionHolderPluginFactoriesLocatorImpl implements
		IAplicacionHolderPluginFactoriesLocator {

	private static final Logger logger = LoggerFactory
			.getLogger(AplicacionHolderPluginFactoriesLocatorImpl.class);
	private List<IAplicacionHolderPluginFactory> iAplicacionHolderPluginFactories;

	/**
	 * Constructor por defecto
	 */
	public AplicacionHolderPluginFactoriesLocatorImpl() {
		if (logger.isDebugEnabled()) {
			logger.debug(this + "<init>");
		}
	}

	public void init() {
		if (logger.isDebugEnabled()) {
			logger.debug(this + ".init()");
		}
	}

	/**
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
		return "Clase base para Farmafene";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IAplicacionHolderPluginFactoriesLocator#getIAplicacionHolderPluginFactories()
	 */
	@Override
	public List<IAplicacionHolderPluginFactory> getIAplicacionHolderPluginFactories() {
		return iAplicacionHolderPluginFactories;
	}

	/**
	 * @param iAplicacionHolderPluginFactories
	 *            the iAplicacionHolderPluginFactories to set
	 */
	public void setIAplicacionHolderPluginFactories(
			List<IAplicacionHolderPluginFactory> iAplicacionHolderPluginFactories) {
		this.iAplicacionHolderPluginFactories = iAplicacionHolderPluginFactories;
	}
}
