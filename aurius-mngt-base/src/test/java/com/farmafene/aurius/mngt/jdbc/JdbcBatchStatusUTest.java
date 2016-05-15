package com.farmafene.aurius.mngt.jdbc;

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

public class JdbcBatchStatusUTest {

	private static final Logger logger = LoggerFactory
			.getLogger(JdbcBatchStatusUTest.class);

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
		JdbcBatchStatus stat = new JdbcBatchStatus(new AuriusResource(
				"resource", AuriusResource.Type.JDBC), Operacion.OPERATION,
				new LinkedList<String>());
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.getBatch().add("batch 1");
		stat.getBatch().add("batch 2");
		stat.stop(new RuntimeException());
		logger.info("XML: \n" + xs.toXML(stat));
	}

	@Test
	public void printTest2() {
		JdbcBatchStatus stat = new JdbcBatchStatus(new AuriusResource(
				"resource", AuriusResource.Type.JDBC), Operacion.OPERATION,
				new LinkedList<String>());
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.getBatch().add("batch 1");
		stat.getBatch().add("batch 2");
		stat.stop(new RuntimeException());
		String xml = xs.toXML(stat);
		logger.info(xml);
		JdbcBatchStatus rs = (JdbcBatchStatus) xs.fromXML(xml);
		logger.info(xs.toXML(rs));
		Assert.assertEquals(stat.getInitTime(), rs.getInitTime());
		Assert.assertEquals(stat.getMiliSeconds(), rs.getMiliSeconds());
		Assert.assertEquals(stat.getDuracion(), rs.getDuracion());
		Assert.assertEquals(stat.getError(), rs.getError());
		Assert.assertEquals(stat.isLocal(), rs.isLocal());
		Assert.assertEquals(stat.isStop(), rs.isStop());
		Assert.assertEquals(stat.getResource(), rs.getResource());
		Assert.assertEquals(stat.getBatch(), rs.getBatch());
		logger.info(rs.getResource().toString());
	}
}
