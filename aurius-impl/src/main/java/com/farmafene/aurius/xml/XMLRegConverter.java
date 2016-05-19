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
package com.farmafene.aurius.xml;

import java.io.Serializable;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XMLRegConverter implements Converter {

	private Mapper mapper;

	public XMLRegConverter(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.
	 *      lang.Class)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return XMLReg.class.equals(type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		XMLReg reg = (XMLReg) source;
		if (null != reg.getId()) {
			writer.addAttribute("id", reg.getId());
		}
		if (null != reg.getName()) {
			writer.addAttribute("name", reg.getName());
		}
		for (String name : reg.getXMLFieldsValues().keySet()) {
			XMLField<?> field = reg.getXMLFieldsValues().get(name);
			if (XMLRegA.class.equals(field.getClass())) {
				for (XMLReg r : ((XMLRegA) field).getValue()) {
					writeItem(r, context, writer);
				}
			} else {
				writeItem(field, context, writer);
			}
		}
	}

	private void writeItem(Object item, MarshallingContext context,
			HierarchicalStreamWriter writer) {
		if (item == null) {
			String name = mapper.serializedClass(null);
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, name,
					Mapper.Null.class);
			writer.endNode();
		} else {
			String name = mapper.serializedClass(item.getClass());
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, name,
					item.getClass());
			context.convertAnother(item);
			writer.endNode();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks
	 *      .xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		XMLReg reg = new XMLReg();
		reg.setId(reader.getAttribute("id"));
		reg.setName(reader.getAttribute("name"));
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			Class<?> type = HierarchicalStreams.readClassType(reader, mapper);
			Object field = null;
			try {
				field = context.convertAnother(type.newInstance(), type);
			} catch (InstantiationException e) {
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			reader.moveUp();
			if (field instanceof XMLField) {
				@SuppressWarnings("unchecked")
				XMLField<Serializable> item = (XMLField<Serializable>) field;
				reg.put(item.getName(), item.getValue());
			} else if (field instanceof XMLReg) {
				XMLReg r = (XMLReg) field;
				XMLRegA ra = (XMLRegA) reg.getXMLFieldsValues().get(r.getName());
				if (null == ra) {
					ra = new XMLRegA();
					reg.getXMLFieldsValues().put(r.getName(), ra);
				}
				ra.getValue().add(r);
			} else {
				throw new ConversionException(field.getClass()
						+ " not soportado....");
			}
		}
		return reg;
	}
}
