/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius.mngt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class AuriusOperacionStatusConverter implements Converter {

	protected Mapper mapper;
	private SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public AuriusOperacionStatusConverter(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
	 */
	@Override
	public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
		boolean canConvert = AuriusOperacionStatus.class.equals(type);
		return canConvert;
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
		AuriusOperacionStatus reg = (AuriusOperacionStatus) source;
		marshalAttributes(reg, writer, context);
		marshalChildren(reg, writer, context);
	}

	protected void marshalAttributes(AuriusOperacionStatus source,
			HierarchicalStreamWriter writer, MarshallingContext context) {
		StringBuilder sb = new StringBuilder(df.format(source.getInitTime()));
		sb.insert(26, ":");
		writer.addAttribute("start", sb.toString());
		writer.addAttribute("msTime", "" + source.getMiliSeconds());
		writer.addAttribute("duracion", "" + source.getDuracion());
		writer.addAttribute("stop", source.isStop() ? "true" : "false");
		writer.addAttribute("local", source.isLocal() ? "true" : "false");
	}

	protected void marshalChildren(IBasicStat source,
			HierarchicalStreamWriter writer, MarshallingContext context) {
		if (null != source.getError()) {
			writer.startNode("Error");
			writer.setValue(source.getError());
			writer.endNode();
		}
	}

	protected void writeItem(Object item, MarshallingContext context,
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
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		AuriusOperacionStatus source = new AuriusOperacionStatus();
		unmarshalAttributes(source, reader, context);
		unmarshalChildren(source, reader, context);
		return source;
	}

	protected void unmarshalChildren(AuriusOperacionStatus object,
			HierarchicalStreamReader reader, UnmarshallingContext context) {
		if (reader.hasMoreChildren()) {
			reader.moveDown();
			object.setError(reader.getValue());
			reader.moveUp();
		}
	}

	protected void unmarshalAttributes(AuriusOperacionStatus object,
			HierarchicalStreamReader reader, UnmarshallingContext context) {
		String startStr = reader.getAttribute("start");
		if (null != startStr && !"".equals(startStr.trim())) {
			try {
				StringBuilder sb = new StringBuilder(startStr);
				Date start = df.parse(sb.replace(26, 27, "").toString());
				object.setInitTime(start.getTime());
			} catch (ParseException e) {
			}
		}
		String durationStr = reader.getAttribute("duracion");
		long offsetTicks = 0;
		if (null != durationStr) {
			try {
				offsetTicks = Long.valueOf(durationStr);
			} catch (NumberFormatException e) {
				// do nothing
			}
		}
		object.setStop(Boolean.valueOf(reader.getAttribute("stop")));
		object.setLocal(Boolean.valueOf(reader.getAttribute("local")));
		Long nowt = System.nanoTime();
		if (object.isStop()) {
			object.setEndTicks(nowt);
			String msStr = reader.getAttribute("msTime");
			long offsetTime = 0;
			if (null != msStr) {
				try {
					offsetTime = Long.valueOf(msStr);
				} catch (NumberFormatException e) {
					// do nothing
				}
			}
			object.setEndTime(object.getInitTime() + offsetTime);
		}
		object.setInitTicks(nowt - offsetTicks);
	}
}
