package com.farmafene.aurius.mngt.jdbc;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.mngt.AuriusResource;
import com.thoughtworks.xstream.XStream;

public class JdbcConnectionStatusUTest {

	private static final Logger logger = LoggerFactory
			.getLogger(JdbcConnectionStatusUTest.class);

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
		JdbcConnectionStatus stat = new JdbcConnectionStatus(
				new AuriusResource("resource", AuriusResource.Type.JDBC),
				"rollback");
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.stop(new RuntimeException());
		logger.info("XML: \n" + xs.toXML(stat));
		stat = new JdbcConnectionStatus(new AuriusResource("resource2",
				AuriusResource.Type.JDBC), "rollback");
		stat.start();
		stat.stop();
		logger.info("XML: \n" + xs.toXML(stat));
	}

	@Test
	public void printTest2() {
		JdbcConnectionStatus stat = new JdbcConnectionStatus(
				new AuriusResource("resource", AuriusResource.Type.JDBC),
				"commit");
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.stop(new RuntimeException());
		String xml = xs.toXML(stat);
		logger.info(xml);
		JdbcConnectionStatus rs = (JdbcConnectionStatus) xs.fromXML(xml);
		logger.info(xs.toXML(rs));
		Assert.assertEquals(stat.getInitTime(), rs.getInitTime());
		Assert.assertEquals(stat.getMiliSeconds(), rs.getMiliSeconds());
		Assert.assertEquals(stat.getDuracion(), rs.getDuracion());
		Assert.assertEquals(stat.getError(),rs.getError());
		Assert.assertEquals(stat.isLocal(),rs.isLocal());
		Assert.assertEquals(stat.isStop(),rs.isStop());
		Assert.assertEquals(stat.getResource(),rs.getResource());
		Assert.assertEquals(stat.getMethod(),rs.getMethod());
		logger.info(rs.getResource().toString());
	}
}
