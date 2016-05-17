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
package com.farmafene.aurius.hibernate;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IClassLoaderWithHolder;
import com.farmafene.aurius.server.Configuracion;
import com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory;
import com.farmafene.commons.hibernate.HBSessionFactoryProperties;
import com.farmafene.commons.hibernate.IHibernateAdapter;
import com.farmafene.commons.ioc.BeanFactory;

public class AuriusHBSessionFactoryFactory extends
		AbstractHBSessionFactoryFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(AuriusHBSessionFactoryFactory.class);
	private static Set<String> hbProperties;
	private IHibernateAdapter iHibernateAdapter;

	static {
		hbProperties = new HashSet<String>();

		//
		//
		// Hibernate JDBC Properties
		//
		hbProperties.add("hibernate.connection.driver_class");
		hbProperties.add("hibernate.connection.url");
		hbProperties.add("hibernate.connection.username");
		hbProperties.add("hibernate.connection.password");
		hbProperties.add("hibernate.connection.pool_size");
		//
		hbProperties.add("hibernate.c3p0.min_size");
		hbProperties.add("hibernate.c3p0.max_size");
		hbProperties.add("hibernate.c3p0.timeout");
		hbProperties.add("hibernate.c3p0.max_statements");

		//
		//
		// Hibernate Datasource Properties
		//
		hbProperties.add("hibernate.connection.datasource");
		hbProperties.add("hibernate.jndi.url");
		hbProperties.add("hibernate.jndi.class");
		hbProperties.add("hibernate.connection.username");
		hbProperties.add("hibernate.connection.password");
		hbProperties.add("hibernate.connection.charSet");
		hbProperties.add("hibernate.connection.provider_class");

		//
		//
		// Hibernate Configuration Properties
		//
		hbProperties.add("hibernate.dialect");
		hbProperties.add("hibernate.show_sql");
		hbProperties.add("hibernate.format_sql");
		hbProperties.add("hibernate.default_schema");
		hbProperties.add("hibernate.default_catalog");
		hbProperties.add("hibernate.session_factory_name");
		hbProperties.add("hibernate.max_fetch_depth");
		hbProperties.add("hibernate.default_batch_fetch_size");
		hbProperties.add("hibernate.default_entity_mode");
		hbProperties.add("hibernate.order_updates");
		hbProperties.add("hibernate.generate_statistics");
		hbProperties.add("hibernate.use_identifier_rollback");
		hbProperties.add("hibernate.use_sql_comments");

		//
		//
		// Hibernate JDBC and Connection Properties
		//
		hbProperties.add("hibernate.jdbc.fetch_size");
		hbProperties.add("hibernate.jdbc.batch_size");
		hbProperties.add("hibernate.jdbc.batch_versioned_data");
		hbProperties.add("hibernate.jdbc.factory_class");
		hbProperties.add("hibernate.jdbc.use_scrollable_resultset");
		hbProperties.add("hibernate.jdbc.use_streams_for_binary");
		hbProperties.add("hibernate.jdbc.use_get_generated_keys");
		hbProperties.add("hibernate.connection.provider_class");
		hbProperties.add("hibernate.connection.isolation");
		hbProperties.add("hibernate.connection.autocommit");
		hbProperties.add("hibernate.connection.release_mode");

		//
		//
		// Hibernate Cache Properties
		//
		hbProperties.add("hibernate.cache.provider_class");
		hbProperties.add("hibernate.cache.use_minimal_puts");
		hbProperties.add("hibernate.cache.use_query_cache");
		hbProperties.add("hibernate.cache.use_second_level_cache");
		hbProperties.add("hibernate.cache.query_cache_factory");
		hbProperties.add("hibernate.cache.region_prefix");
		hbProperties.add("hibernate.cache.use_structured_entries");

		//
		//
		// Hibernate Transaction Properties
		//
		hbProperties.add("hibernate.transaction.factory_class");
		hbProperties.add("jta.UserTransaction");
		hbProperties.add("hibernate.transaction.manager_lookup_class");
		hbProperties.add("hibernate.transaction.flush_before_completion");
		hbProperties.add("hibernate.transaction.auto_close_session");

		//
		//
		// Miscellaneous Properties
		//
		hbProperties.add("hibernate.current_session_context_class");
		hbProperties.add("hibernate.query.factory_class");
		hbProperties.add("hibernate.query.substitutions");
		hbProperties.add("hibernate.hbm2ddl.auto");
		hbProperties.add("hibernate.cglib.use_reflection_optimizer");
	}

	public AuriusHBSessionFactoryFactory() {
		super();
		this.setBasePackage("");
		iHibernateAdapter = BeanFactory.getBean(IHibernateAdapter.class);
		logger.debug(this + "<init>");
		logger.debug("el IHibernateAdapter es {}", iHibernateAdapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getHBSessionFactoryProperties(java.lang.String)
	 */
	@Override
	public HBSessionFactoryProperties getHBSessionFactoryProperties(
			String name, String jndiName) {
		HBSessionFactoryProperties spf = new HBSessionFactoryProperties();
		Map<String, String> props = new HashMap<String, String>();
		for (String prop : hbProperties) {
			String value = Configuracion.getProperty("DB." + name + "_" + prop);
			if (null != value && !"".equals(value.trim())) {
				props.put(prop, value.trim());
			}
		}
		if (!props.containsKey("hibernate.connection.datasource")) {
			spf.setDataSource(jndiName);
		}
		spf.setPropiedades(props);
		return spf;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getURLResources(java.lang.ClassLoader)
	 */
	@Override
	public URL[] getURLResources(ClassLoader cl) {
		IClassLoaderWithHolder acl = null;
		URL[] salida = null;
		if (cl != null && cl instanceof IClassLoaderWithHolder) {
			acl = (IClassLoaderWithHolder) cl;
			salida = acl.getURLs();
		} else {
			throw new IllegalArgumentException("Parámetros inválidos");
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.commons.hibernate.AbstractHBSessionFactoryFactory#getIHibernateAdapter()
	 */
	@Override
	protected IHibernateAdapter getIHibernateAdapter() {
		return iHibernateAdapter;
	}
}
