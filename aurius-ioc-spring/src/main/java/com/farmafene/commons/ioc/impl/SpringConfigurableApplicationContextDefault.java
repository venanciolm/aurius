package com.farmafene.commons.ioc.impl;

import java.io.File;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

class SpringConfigurableApplicationContextDefault implements
		ISpringConfigurableApplicationContext {
	private static final String JNDI_AURIUS_CONF = "java:comp/env/com.farmafene.commons/ioc/IBeanFactory";
	private static final String CONFIGURATION_FILE = "IBeanFactory-beans.xml";
	private static final Logger logger = LoggerFactory
			.getLogger(SpringConfigurableApplicationContextDefault.class);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.farmafene.commons.ioc.impl.ISpringConfigurableApplicationContext#getSpringConfigurableApplicationContext()
	 */
	@Override
	public ConfigurableApplicationContext getSpringConfigurableApplicationContext() {
		ConfigurableApplicationContext factory = null;
		try {
			Object obj = new InitialContext().lookup(JNDI_AURIUS_CONF);
			File f = null;
			if (obj != null && (obj instanceof String)) {
				f = new File((String) obj);
			}
			if (obj != null && (obj instanceof URL)) {
				f = new File(((URL) obj).toURI());
			}
			logger.debug("El fichero es: " + f);
			if (f != null) {
				factory = new FileSystemXmlApplicationContext(f.getPath());
			}
		} catch (NamingException e) {
			logger.debug("NamingException: " + e.getMessage());
		} catch (Exception e) {
			logger.debug("Exception: ", e);
		}
		if (factory == null) {
			logger.info("No ha sido posible, obteniendola del ClassPath ...");
			try {
				factory = new ClassPathXmlApplicationContext("classpath:"
						+ CONFIGURATION_FILE);
			} catch (Exception e) {
				logger.debug("Exception: ", e);
			}
		}
		return factory;
	}

}
