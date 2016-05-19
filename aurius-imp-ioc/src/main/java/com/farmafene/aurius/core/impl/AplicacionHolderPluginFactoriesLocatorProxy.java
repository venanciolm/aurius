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

import com.farmafene.aurius.core.IAplicacionHolderPluginFactoriesLocator;
import com.farmafene.aurius.core.IAplicacionHolderPluginFactory;
import com.farmafene.commons.ioc.BeanFactory;

public class AplicacionHolderPluginFactoriesLocatorProxy implements
		IAplicacionHolderPluginFactoriesLocator {

	private static IAplicacionHolderPluginFactoriesLocator sustitute;

	private IAplicacionHolderPluginFactoriesLocator getIAplicacionHolderPluginFactoriesLocator() {
		IAplicacionHolderPluginFactoriesLocator conf = null;
		if (sustitute == null) {
			conf = (IAplicacionHolderPluginFactoriesLocator) BeanFactory
					.getBean(IAplicacionHolderPluginFactoriesLocator.class);
			if (null == conf) {
				sustitute = new IAplicacionHolderPluginFactoriesLocator() {
					/**
					 * {@inheritDoc}
					 * 
					 * @see IAplicacionHolderPluginFactoriesLocator#getIAplicacionHolderPluginFactories()
					 */
					@Override
					public List<IAplicacionHolderPluginFactory> getIAplicacionHolderPluginFactories() {
						return null;
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
						return "Dummy Implementation";
					}
				};
				conf = sustitute;
			}
		} else {
			conf = sustitute;
		}
		return conf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IAplicacionHolderPluginFactoriesLocator#getIAplicacionHolderPluginFactories()
	 */
	@Override
	public List<IAplicacionHolderPluginFactory> getIAplicacionHolderPluginFactories() {
		return getIAplicacionHolderPluginFactoriesLocator()
				.getIAplicacionHolderPluginFactories();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVentor()
	 */
	@Override
	public String getImplementationVentor() {
		return getIAplicacionHolderPluginFactoriesLocator()
				.getImplementationVentor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationVersion()
	 */
	@Override
	public String getImplementationVersion() {
		return getIAplicacionHolderPluginFactoriesLocator()
				.getImplementationVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.IConfigurableBean#getImplementationDescription()
	 */
	@Override
	public String getImplementationDescription() {
		return getIAplicacionHolderPluginFactoriesLocator()
				.getImplementationDescription();
	}
}
