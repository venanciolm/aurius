package com.farmafene.aurius.mngt.jdbc;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResource.Operacion;
import com.thoughtworks.xstream.XStream;

public class JdbcExecuteStatusUTest {

	private static final Logger logger = LoggerFactory
			.getLogger(JdbcExecuteStatusUTest.class);

	@BeforeClass
	public static void beforeClass() {
		logger.info("BeforeClass()");
	}

	@AfterClass
	public static void afterClass() {
		logger.info("AfterClass()");
	}

	@Test
	public void printTest() {
		JdbcExecuteStatus stat = new JdbcExecuteStatus(new AuriusResource(
				"resource", AuriusResource.Type.JDBC), Operacion.OPERATION,
				"select 1 from dual", new LinkedList<String>(),
				new LinkedHashMap<String, String>());
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.getParams().add("index value 1");
		logger.info("Stattus: "+ stat.getDuracion());
		stat.getParams().add("index value 2");
		logger.info("Stattus: "+ stat.getDuracion());
		stat.getNParams().put("param1", "value 1");
		logger.info("Stattus: "+ stat.getDuracion());
		stat.getNParams().put("param2", "value 2");
		logger.info("Stattus: "+ stat.getDuracion());
		stat.stop(new RuntimeException());
		logger.info("XML: \n" + xs.toXML(stat));
	}

	@Test
	public void printTest2() {
		JdbcExecuteStatus stat = new JdbcExecuteStatus(new AuriusResource(
				"resource", AuriusResource.Type.JDBC), Operacion.OPERATION,
				"select 1 from dual", new LinkedList<String>(),
				new LinkedHashMap<String, String>());
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.getParams().add("index value 1");
		stat.getParams().add("index value 2");
		stat.getNParams().put("param1", "value 1");
		stat.getNParams().put("param2", "value 2");
		stat.stop(new RuntimeException());
		String xml = xs.toXML(stat);
		logger.info(xml);
		JdbcExecuteStatus rs = (JdbcExecuteStatus) xs.fromXML(xml);
		logger.info(xs.toXML(rs));
		Assert.assertEquals(stat.getInitTime(), rs.getInitTime());
		Assert.assertEquals(stat.getMiliSeconds(), rs.getMiliSeconds());
		Assert.assertEquals(stat.getDuracion(), rs.getDuracion());
		Assert.assertEquals(stat.getError(), rs.getError());
		Assert.assertEquals(stat.isLocal(), rs.isLocal());
		Assert.assertEquals(stat.isStop(), rs.isStop());
		Assert.assertEquals(stat.getResource(), rs.getResource());
		Assert.assertEquals(stat.getParams(), rs.getParams());
		Assert.assertEquals(stat.getNParams(), rs.getNParams());
		logger.info(rs.getResource().toString());
	}
}
