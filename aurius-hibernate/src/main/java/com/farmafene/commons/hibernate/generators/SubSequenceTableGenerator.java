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
package com.farmafene.commons.hibernate.generators;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.mapping.Table;
import org.hibernate.property.DirectPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generador de hibernate
 * 
 * @version 1.0.0
 * @since 1.0.0
 */
public class SubSequenceTableGenerator implements IdentifierGenerator,
		Configurable {
	public static final String SEQUENCE = "SEQUENCE";
	public static final String GENERATED_VALUE = "GENERATED_VALUE";
	private static final String ENTITY_NAME = "entity_name";
	private static final String TARGET_TABLE = "target_table";
	protected static Logger logger = LoggerFactory
			.getLogger(SubSequenceTableGenerator.class);

	@SuppressWarnings("unused")
	private Type identifierType;
	String tableName;
	private PropertyAccessor propertyAccesor;
	private FieldsGenerador campos;
	private DataBaseName nombreSecuencias;
	private boolean tipoEmbedded;
	private Dialect dialectoBBDD;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public void configure(Type type, Properties props, Dialect dialect)
			throws MappingException {
		if (logger.isDebugEnabled()) {
			logger.debug(props.toString());
		}
		String generatedField = PropertiesHelper.getString(GENERATED_VALUE,
				props, null);
		Class<?> entityClass = null;
		try {
			entityClass = Class.forName(PropertiesHelper.getString(ENTITY_NAME,
					props, null));
		} catch (ClassNotFoundException e) {
			throw new IdentifierGenerationException(e.getMessage(), e);
		}
		dialectoBBDD = dialect;
		propertyAccesor = new DirectPropertyAccessor();
		identifierType = type;
		tableName = PropertiesHelper.getString(TARGET_TABLE, props, null);
		nombreSecuencias = new DataBaseName(PropertiesHelper.getString(
				"catalog", props, null), PropertiesHelper.getString("schema",
				props, null), PropertiesHelper.getString(SEQUENCE, props, null));
		Field[] lista = entityClass.getDeclaredFields();
		Field campoClave = null;
		Field key = null;
		for (Field f : lista) {
			if (f.getAnnotation(GeneratedValue.class) != null) {
				key = f;
				if (f.getAnnotation(EmbeddedId.class) != null) {
					tipoEmbedded = true;
					try {
						lista = f.getType().getDeclaredFields();
						campoClave = f.getType().getDeclaredField(
								generatedField);
					} catch (SecurityException e) {
						throw new IllegalArgumentException(e);
					} catch (NoSuchFieldException e) {
						throw new IllegalArgumentException(e);
					}
					break;
				} else if (f.getAnnotation(Id.class) != null) {
					tipoEmbedded = false;
					campoClave = f;
				} else {
					throw new IllegalArgumentException(
							"Se ha de configurarar Id o EmbeddedId");
				}
				break;
			}
		}
		if (campoClave == null) {
			throw new IdentifierGenerationException(
					"Debe existir un Campo que generar");
		}
		ArrayList<Field> listaCampos = new ArrayList<Field>();
		for (Field f : lista) {
			if (f.getName().equals(campoClave.getName())) {
				continue;
			}
			if (tipoEmbedded) {
				if (f.getAnnotation(Column.class) != null) {
					listaCampos.add(f);
				}
			} else {
				if (f.getAnnotation(Id.class) != null) {
					listaCampos.add(f);
				}
			}
		}
		campos = new FieldsGenerador(key, campoClave, listaCampos);
		if (logger.isDebugEnabled()) {
			logger.debug("Campos.key: " + campos.getKey());
			logger.debug("Campos.target: " + campos.getTarget());
			logger.debug("Campos.lista: " + campos.getLista());
			logger.debug("Campos.lista: " + campos.getLista().length);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public Serializable generate(SessionImplementor sess, Object entity)
			throws HibernateException {
		logger.debug("La clase es: " + entity.getClass().getName());
		Object[] propiedades = new Object[0];
		Serializable key = null;
		if (campos.getLista().length > 0) {
			key = (Serializable) propertyAccesor.getGetter(entity.getClass(),
					campos.getKey().getName()).get(entity);
			propiedades = leeParametros(key);
		}
		if (campos.getTarget().getName() != null) {
			try {
				logger.debug("La clave es: "
						+ ((key == null) ? null : key.getClass().getName()));
				logger.debug("Numero Propiedades: " + propiedades.length);

				if (tipoEmbedded) {
					if (null == propertyAccesor.getGetter(
							campos.getKey().getType(),
							campos.getTarget().getName()).get((Object) key)) {

						Object id = new WorkInOther(this, propiedades, logger,
								dialectoBBDD, nombreSecuencias, campos)
								.doWorkInNewTransaction(sess);
						propertyAccesor.getSetter(campos.getKey().getType(),
								campos.getTarget().getName()).set((Object) key,
								id, sess.getFactory());
					} else {
						logger.warn("Ya se ha introducido el valor a generar!!!, utilizando ese valor["
								+ key + "]");
					}
				} else {
					if (key == null) {
						Object id = new WorkInOther(this, propiedades, logger,
								dialectoBBDD, nombreSecuencias, campos)
								.doWorkInNewTransaction(sess);
						key = (Serializable) id;
					} else {
						// FIXME !!!, debemos reestructurar la secuencia!!!
						logger.warn("Ya se ha introducido el valor a generar!!!, utilizando ese valor["
								+ key + "]");
					}
				}
			} catch (IllegalArgumentException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			} catch (SecurityException e) {
				throw new IdentifierGenerationException(e.getMessage(), e);
			}

		}
		return key;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	private Object[] leeParametros(Serializable key) {
		Object[] propiedades = new Object[campos.getLista().length];
		for (int i = 0; i < propiedades.length; i++) {
			propiedades[i] = propertyAccesor.getGetter(key.getClass(),
					campos.getLista()[i].getName()).get(key);

		}
		return propiedades;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	class DataBaseName {
		private String schemaName;

		private String catalogName;

		private String secuencia;

		public String getNombreSecuencia() {
			return getNombreCompletoTabla(secuencia);
		}

		public DataBaseName(String catalog, String schema, String secuencia) {
			this.catalogName = catalog;
			this.schemaName = schema;
			this.secuencia = secuencia;
		}

		private String getNombreCompletoTabla(String nombre) {
			if (nombre == null || "".equals(nombre.trim())) {
				return null;
			} else if (nombre.indexOf('.') < 0) {
				return Table.qualify(catalogName, schemaName, nombre);
			}
			return nombre;

		}
	}
}
