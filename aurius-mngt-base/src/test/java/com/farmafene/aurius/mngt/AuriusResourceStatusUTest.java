package com.farmafene.aurius.mngt;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class AuriusResourceStatusUTest {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusResourceStatusUTest.class);

	@BeforeClass
	public static void beforeClass() {

	}

	@AfterClass
	public static void afterClass() {

	}

	@Test
	public void printTest() {

		AuriusResourceStatus stat = new AuriusResourceStatus(
				new AuriusResource("resource", AuriusResource.Type.JDBC),
				AuriusResource.Operacion.OPERATION);
		XStream xStream = new XStream();
		xStream.processAnnotations(stat.getClass());
		stat.start();
		stat.stop(new RuntimeException());
		logger.info("XML: \n" + xStream.toXML(stat));
		stat = new AuriusResourceStatus(new AuriusResource("resource2",
				AuriusResource.Type.JDBC), AuriusResource.Operacion.OPERATION);
		stat.start();
		stat.stop();
		logger.info("XML: \n" + xStream.toXML(stat));
	}

	@Test
	public void printTest2() {
		AuriusResourceStatus stat = new AuriusResourceStatus(
				new AuriusResource("resource", AuriusResource.Type.JDBC),
				AuriusResource.Operacion.OPERATION);
		XStream xs = new XStream();
		xs.processAnnotations(stat.getClass());
		stat.start();
		stat.stop(new RuntimeException());
		String xml = xs.toXML(stat);
		logger.info(xml);
		AuriusResourceStatus rs = (AuriusResourceStatus) xs.fromXML(xml);
		logger.info(xs.toXML(rs));
		Assert.assertEquals(stat.getInitTime(), rs.getInitTime());
		Assert.assertEquals(stat.getMiliSeconds(), rs.getMiliSeconds());
		Assert.assertEquals(stat.getDuracion(), rs.getDuracion());
		Assert.assertEquals(stat.getError(), rs.getError());
		Assert.assertEquals(stat.isLocal(), rs.isLocal());
		Assert.assertEquals(stat.isStop(), rs.isStop());
		Assert.assertEquals(stat.getResource(), rs.getResource());
		logger.info(rs.getResource().toString());
	}
}
