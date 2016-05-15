package com.farmafene.aurius.mngt.jdbc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class JdbcBatchStatusInnerData implements Converter {

	public JdbcBatchStatusInnerData(Mapper mapper) {

	}

	protected List<String> params;
	protected Map<String, String> nparams;

	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		return JdbcBatchStatusInnerData.class.equals(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		params = ((JdbcBatchStatusInnerData) source).params;
		nparams = ((JdbcBatchStatusInnerData) source).nparams;
		if (null != params) {
			for (String param : params) {
				writer.startNode("param");
				writer.setValue(param);
				writer.endNode();
			}
		}
		if (null != nparams) {
			for (String param : nparams.keySet()) {
				writer.startNode("param");
				writer.addAttribute("name", param);
				writer.setValue(nparams.get(param));
				writer.endNode();
			}
		}

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		JdbcBatchStatusInnerData n = new JdbcBatchStatusInnerData(null);
		n.nparams = new LinkedHashMap<String, String>();
		n.params = new LinkedList<String>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			String name = reader.getAttribute("name");
			if (null != name) {
				n.nparams.put(name, reader.getValue());
			} else {
				n.params.add(reader.getValue());
			}
			reader.moveUp();
		}
		return n;
	}
}
