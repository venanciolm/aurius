package com.farmafene.commons.hibernate.generators;

import java.lang.reflect.Field;
import java.util.List;

/**
 * {@inheritDoc}
 * 
 * @since 1.0.0
 */
class FieldsGenerador {

	private Field[] lista;

	private Field target;

	private Field key;

	public FieldsGenerador(Field key,
			Field t, List<Field> list) {
		this.target = t;
		this.lista = list.toArray(new Field[0]);
		this.key = key;
	}

	public Field[] getLista() {
		return lista;
	}

	public Field getTarget() {
		return target;
	}

	public Field getKey() {
		return key;
	}
}