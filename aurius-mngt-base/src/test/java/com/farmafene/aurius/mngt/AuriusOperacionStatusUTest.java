package com.farmafene.aurius.mngt;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class AuriusOperacionStatusUTest {

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusOperacionStatusUTest.class);

	@BeforeClass
	public static void beforeClass() {

	}

	@AfterClass
	public static void afterClass() {

	}

	@Test
	public void printTest() {
		XStream xStream = new XStream();
		logger.info("===========================================");
		IBasicStat stat = new AuriusOperacionStatus();
		stat.start();
		stat.stop(new RuntimeException());
		xStream.processAnnotations(stat.getClass());
		logger.info("XML: \n" + xStream.toXML(stat));
		logger.info("===========================================");
		stat = new AuriusOperacionStatus();
		stat.start();
		stat.stop();
		logger.info("XML: \n" + xStream.toXML(stat));
		logger.info("===========================================");
	}

	@Test
	public void printTest2() {
		AuriusOperacionStatus stat = new AuriusOperacionStatus();
		XStream xStream = new XStream();
		xStream.processAnnotations(stat.getClass());
		stat.start();
		stat.stop(new RuntimeException());
		String xml = xStream.toXML(stat);
		logger.info(xml);
		IBasicStat rs = (IBasicStat) xStream.fromXML(xml);
		logger.info(xStream.toXML(rs));
		Assert.assertEquals(stat.getInitTime(), rs.getInitTime());
		Assert.assertEquals(stat.getMiliSeconds(), rs.getMiliSeconds());
		Assert.assertEquals(stat.getDuracion(), rs.getDuracion());
		Assert.assertEquals(stat.getError(), rs.getError());
		Assert.assertEquals(stat.isLocal(), rs.isLocal());
		Assert.assertEquals(stat.isStop(), rs.isStop());
	}
}
