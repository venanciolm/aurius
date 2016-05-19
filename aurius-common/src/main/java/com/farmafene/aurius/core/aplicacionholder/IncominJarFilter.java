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
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IEstructuraDirectorios;

class IncominJarFilter implements FilenameFilter {
	private static final Logger logger = LoggerFactory
			.getLogger(IncominJarFilter.class);
	private IAplicacionHolder holder;
	private String prefix;

	public IncominJarFilter() {
	}

	public void init() {
		this.prefix = new SimpleDateFormat(
				IEstructuraDirectorios.TIMESTAMP_FORMAT).format(new Date());
	}

	public boolean accept(File Firectorio, String name) {
		boolean accepted = false;
		if ((name == null ? false : name.toUpperCase().endsWith(".JAR"))
				&& new File(Firectorio, name).isFile()) {
			accepted = false;
			int length = name.length();
			try {
				if (length > 23) {
					new SimpleDateFormat(EstructuraDirectorios.TIMESTAMP_FORMAT)
							.parse(name.substring(length - 22, length - 4));
				} else {
					accepted = holder.getEstructuraDirectorios()
							.validateJarFile(Firectorio, name);
				}
			} catch (ParseException e) {
				accepted = holder.getEstructuraDirectorios().validateJarFile(
						Firectorio, name);
			}
			if (logger.isDebugEnabled()) {
				if (accepted) {
					logger.debug("Fichero  IN>" + name + "< aceptado.");
				} else {
					logger.debug("Fichero  IN> " + name + "< erroneo.");
				}
			}
		}
		if (!accepted) {
			holder.getEstructuraDirectorios().moveFile(
					new File(Firectorio, name),
					holder.getEstructuraDirectorios().getError(),
					"IN." + prefix + "." + name);
		}
		return accepted;
	}

	/**
	 * @param holder
	 *            the holder to set
	 */
	public void setHolder(IAplicacionHolder holder) {
		this.holder = holder;
	}

}
