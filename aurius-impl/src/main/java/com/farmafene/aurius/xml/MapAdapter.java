package com.farmafene.aurius.xml;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.farmafene.aurius.ELMap;

class MapAdapter implements Map<String, Object> {
	public Map<String, XMLField<?>> inner;

	public MapAdapter(Map<String, XMLField<?>> inner) {
		this.inner = inner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return inner.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(Object key) {
		XMLField<?> a = inner.get(key);
		Object salida = null;
		if (null != a && a instanceof XMLRegA) {
			XMLRegA regs = (XMLRegA) a;
			List<?> ll = regs.getValue();
			@SuppressWarnings("unchecked")
			List<ELMap> l = (List<ELMap>) ll;
			salida = new ListAdapter(l);
		} else if (null != a) {
			salida = a.getValue();
		}
		return salida;
	}

	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		return inner.keySet();
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}
}
