package com.farmafene.aurius.xml;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.ELMap;

public class XMLRegUTest {
	private static final Logger logger = LoggerFactory
			.getLogger(XMLRegUTest.class);

	@BeforeClass
	public static void beforeClass() throws SQLException {
		logger.info("======================== beforeClass ========================");
	}

	@AfterClass
	public static void afterClass() {
		logger.info("======================== afterClass =========================");
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void valueMap() throws IllegalArgumentException,
			CloneNotSupportedException {
		XMLReg reg = getReg();
		logger.info("Registro: \n" + reg);
		Assert.assertEquals(reg.getDecimal("NOMBRE#1"),
				reg.getValue().get("NOMBRE#1"));
		Assert.assertEquals(reg.getString("NOMBRE#2"),
				reg.getValue().get("NOMBRE#2"));
		Assert.assertEquals(
				reg.getRegistro("REG#1", 0).getDecimal("DECIMAL#1"),
				((ELMap) ((List) reg.getValue().get("REG#1")).get(0))
						.getValue().get("DECIMAL#1"));
	}

	private XMLReg getReg() {
		XMLReg reg2 = new XMLReg();
		XMLReg reg = new XMLReg();
		reg.setId("id");
		reg.put("NOMBRE#1", new BigDecimal("22"));
		reg.put("NOMBRE#2", "Un nombre");
		reg.addRegistro("REG#1", reg2);
		reg.addRegistro("REG#1", reg2);
		reg.addRegistro("REG#1", reg2);
		reg2.setId("id3");
		reg2.put("DECIMAL#1", new BigDecimal("33"));
		reg2.put("DATE", new Date());
		return reg;
	}

	@Test
	public void cloneTest() throws CloneNotSupportedException {
		XMLReg reg = getReg();
		XMLReg clone = reg.clone();
		logger.info("Clone:\n" + clone);
		Assert.assertEquals(reg.getId(), clone.getId());
	}

}
