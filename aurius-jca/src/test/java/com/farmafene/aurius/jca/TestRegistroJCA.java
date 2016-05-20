package com.farmafene.aurius.jca;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.BasicRegistroFactory;
import com.farmafene.aurius.Registro;

public class TestRegistroJCA {

	private static final Logger logger = LoggerFactory
			.getLogger(TestRegistroJCA.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Registro regppal = BasicRegistroFactory.getRegistro(null);
		Registro reg2 = regppal.newRegistro("REGISTRO");
		Registro reg = regppal;
		reg.put("STRING", "Uno");
		reg.put("STRING#2", "Dos");
		reg.put("DATE", new Date());
		reg.put("BYTE", new byte[100]);
		reg.put("DECIMAL", BigDecimal.ZERO);
		reg.addRegistro("REGISTRO", reg2);
		logger.info("Registro: " + regppal);
		reg = reg2;
		reg.put("STRING", "Uno");
		reg.put("STRING#2", "Dos");
		reg.put("DATE", new Date());
		reg.put("BYTE", new byte[100]);
		reg.put("DECIMAL", BigDecimal.ZERO);
		logger.info("Registro: " + regppal);
		reg = regppal;
		logger.info("STRING>>> " + reg.getString("STRING"));
		logger.info("STRING#2>>> " + reg.getString("STRING#2"));
		logger.info("DATE>>> " + reg.getDate("DATE"));
		logger.info("DECIMAL>>> " + reg.getDecimal("DECIMAL"));
		logger.info("BYTE>>> " + reg.getBytes("BYTE"));
		logger.info("REGISTRO.STRING>>> "
				+ reg.getRegistro("REGISTRO", 0).getString("STRING"));
		logger.info("REGISTRO.STRING#2>>> "
				+ reg.getRegistro("REGISTRO", 0).getString("STRING#2"));
		logger.info("REGISTRO.DATE>>> "
				+ reg.getRegistro("REGISTRO", 0).getDate("DATE"));
		logger.info("REGISTRO.DECIMAL>>> "
				+ reg.getRegistro("REGISTRO", 0).getDecimal("DECIMAL"));
		logger.info("REGISTRO.BYTE>>> "
				+ reg.getRegistro("REGISTRO", 0).getBytes("BYTE"));
	}

}
