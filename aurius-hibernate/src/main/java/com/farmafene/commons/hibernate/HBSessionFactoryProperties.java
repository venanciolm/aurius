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
package com.farmafene.commons.hibernate;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factoría de Hibernate
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 */
public class HBSessionFactoryProperties {

	private static final Logger logger = LoggerFactory
			.getLogger(HBSessionFactoryProperties.class);
	private String refName;
	private String dialect;
	private String dataSource;
	private Properties propiedades;
	private IHibernateAdapter iHibernateAdapter;

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public HBSessionFactoryProperties() {
		propiedades = new Properties();
		propiedades.put("hibernate.transaction.factory_class",
				"org.hibernate.transaction.JTATransactionFactory");
		propiedades.put("hibernate.current_session_context_class", "jta");
		propiedades.put("hibernate.cache.provider_class",
				"org.hibernate.cache.NoCacheProvider");

		propiedades.put("hibernate.cache.use_second_level_cache", "false");
		propiedades.put("hibernate.cache.use_query_cache", "false");
		propiedades.put("hibernate.show_sql", "true");
		propiedades.put("hibernate.order_updates", "true");
		propiedades.put("hibernate.statement_cache.size", "0");
		propiedades.put("hibernate.jdbc.batch_size", "0");
		propiedades.put("hibernate.jdbc.use_scrollable_resultset", "true");
		propiedades.put("hibernate.bytecode.use_reflection_optimizer", "false");
		propiedades.put("hibernate.transaction.flush_before_completion",
				"false");
		propiedades
				.put("hibernate.transaction.manager_lookup_class",
						"com.farmafene.commons.hibernate.SimpleTransactionManagerLookup");
	}

	/**
	 * @return the refName
	 * @since 1.0.0
	 */
	public String getRefName() {
		return refName;
	}

	/**
	 * @param refName
	 *            the refName to set
	 * @since 1.0.0
	 */
	public void setRefName(String refName) {
		this.refName = refName;
	}

	/**
	 * @return the dialect
	 * @since 1.0.0
	 */
	public String getDialect() {
		return dialect;
	}

	/**
	 * @param dialect
	 *            the dialect to set
	 * @since 1.0.0
	 */
	public void setDialect(String dialect) {
		if (dialect != null) {
			propiedades.put("hibernate.dialect", dialect);
		}
		this.dialect = dialect;
	}

	/**
	 * @return the propiedades
	 * @since 1.0.0
	 */
	public Properties getPropiedades() {
		return propiedades;
	}

	/**
	 * @param propiedades
	 *            the propiedades to set
	 */
	public void setPropiedades(Map<String, String> propiedades) {
		if (propiedades != null) {
			for (String key : propiedades.keySet()) {
				this.propiedades.put(key, propiedades.get(key));
			}
		}
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		if (dataSource != null) {
			propiedades.put("hibernate.connection.datasource", dataSource);
		}
		this.dataSource = dataSource;
	}

	/**
	 * @return the iHibernateAdapter
	 */
	public IHibernateAdapter getIHibernateAdapter() {
		return iHibernateAdapter;
	}

	/**
	 * @param iHibernateAdapter the iHibernateAdapter to set
	 */
	public void setIHibernateAdapter(IHibernateAdapter iHibernateAdapter) {
		this.iHibernateAdapter = iHibernateAdapter;
		if (null != iHibernateAdapter) {

			String jndi = iHibernateAdapter.getJNDITemplateClass();
			if (null != jndi && !"".equals(jndi.trim())) {
				propiedades.put("hibernate.jndi.class", jndi);
			} else {
				logger.info("No se ha encontrado 'hibernate.jndi.class', utilizando jndi por defect");
			}
			String lookup = iHibernateAdapter
					.getTransactionManagerLookupClass();
			if (null != lookup && !"".equals(lookup.trim())) {
				propiedades.put("hibernate.transaction.manager_lookup_class",
						lookup);
			} else {
				logger.info("No se ha encontrado 'hibernate.transaction.manager_lookup_class', utilizando jndi por defect");
			}
		}
	}
}
