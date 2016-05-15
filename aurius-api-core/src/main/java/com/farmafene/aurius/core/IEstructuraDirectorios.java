package com.farmafene.aurius.core;

import java.io.File;

public interface IEstructuraDirectorios {
	String TIMESTAMP_FORMAT = "yyyyMMdd'T'HHmmssSSS";

	File getIncoming();
	File getError();
	File getCurrent();
	File getOld();
	File getConf();
	boolean validateJarFile(File directory, String name);
	void moveFile(File origen, File directoryDest, String nameDest);
}
