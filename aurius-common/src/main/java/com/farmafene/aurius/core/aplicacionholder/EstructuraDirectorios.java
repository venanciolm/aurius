/*
 * Copyright (c) 2009-2011 farmafene.com
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
package com.farmafene.aurius.core.aplicacionholder;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IEstructuraDirectorios;
import com.farmafene.aurius.server.Configuracion;

class EstructuraDirectorios implements IEstructuraDirectorios {

	private static final Logger logger = LoggerFactory
			.getLogger(IEstructuraDirectorios.class);
	private String aplicacion;
	private File aplicacionDir;

	void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	void init() {
		File f = new File(Configuracion.getConfigPath());
		if (!f.exists()) {
			f.mkdir();
		}
		if (!f.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ f.getAbsolutePath() + "' debe ser un directorio,");
		}
		f = new File(f.getParent(), "apps");
		if (!f.exists()) {
			f.mkdir();
		}
		if (!f.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ f.getAbsolutePath() + "' debe ser un directorio,");
		}
		this.aplicacionDir = new File(f, aplicacion);
		if (!this.aplicacionDir.exists()) {
			this.aplicacionDir.mkdirs();
		}
		if (!this.aplicacionDir.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ this.aplicacionDir.getAbsolutePath()
					+ "' debe ser un directorio,");
		}
		getIncoming();
		getCurrent();
		getError();
		getConf();
		getOld();
	}

	@Override
	public File getConf() {
		File salida = new File(this.aplicacionDir, "conf");
		if (!salida.exists()) {
			salida.mkdir();
		}
		if (!salida.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ salida.getAbsolutePath() + "' debe ser un directorio,");
		}
		return salida;
	}

	@Override
	public File getCurrent() {
		File salida = new File(this.aplicacionDir, "current");
		if (!salida.exists()) {
			salida.mkdir();
		}
		if (!salida.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ salida.getAbsolutePath() + "' debe ser un directorio,");
		}
		return salida;
	}

	@Override
	public File getError() {
		File salida = new File(this.aplicacionDir, "error");
		if (!salida.exists()) {
			salida.mkdir();
		}
		if (!salida.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ salida.getAbsolutePath() + "' debe ser un directorio,");
		}
		return salida;
	}

	@Override
	public File getIncoming() {
		File salida = new File(this.aplicacionDir, "incoming");
		if (!salida.exists()) {
			salida.mkdir();
		}
		if (!salida.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ salida.getAbsolutePath() + "' debe ser un directorio,");
		}
		return salida;
	}

	@Override
	public File getOld() {
		File salida = new File(this.aplicacionDir, "old");
		if (!salida.exists()) {
			salida.mkdir();
		}
		if (!salida.isDirectory()) {
			throw new IllegalStateException("El fichero '"
					+ salida.getAbsolutePath() + "' debe ser un directorio,");
		}
		return salida;
	}

	@Override
	public void moveFile(File origen, File directoryDest, String nameDest) {
		boolean success = origen.renameTo(new File(directoryDest, nameDest));
		if (!success) {
			logger.warn("No ha sido posible mover el fichero: " + origen);
		}
		// TODO - Tratar más tarde de moverlo
	}

	@Override
	public boolean validateJarFile(File directory, String name) {
		if (logger.isInfoEnabled()) {
			logger.info("validateJarFile(" + directory + ", " + name + ")");
		}
		// TODO - validateJarFile
		return true;
	}
}
