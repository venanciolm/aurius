/*
 * Copyright (c) 2009-2014 farmafene.com
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
package com.farmafene.aurius.mngt;

import com.farmafene.aurius.BasicRegistroFactory;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.util.IWrapperClass;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@SuppressWarnings("serial")
@XStreamAlias("ServicioStatus")
public class ServiceStatus extends AuriusOperationContainerStatus {

	@XStreamAlias("Servicio")
	@XStreamAsAttribute
	private String idServicio;
	@XStreamAlias("Version")
	@XStreamAsAttribute
	private String version;
	@XStreamAlias("RegistroIn")
	private Registro entrada;
	@XStreamAlias("RegistroOut")
	private Registro salida;

	public ServiceStatus(String idServicio, String version) {
		this.idServicio = idServicio;
		this.version = version;
		this.entrada = null;
		this.salida = null;
	}

	/**
	 * @return the entrada
	 */
	public Registro getEntrada() {
		return entrada;
	}

	/**
	 * @param entrada
	 *            the entrada to set
	 */
	public void setEntrada(Registro entrada) {
		Registro r = entrada;
		if (null != r
				&& (r instanceof IWrapperClass)
				&& ((IWrapperClass) r).isWrapperFor(BasicRegistroFactory
						.getIBasicRegistroFactory().getSupports())) {
			r = ((IWrapperClass) r).unwrap(BasicRegistroFactory
					.getIBasicRegistroFactory().getSupports());
		}
		this.entrada = r;
	}

	/**
	 * @return the salida
	 */
	public Registro getSalida() {
		return salida;
	}

	/**
	 * @param salida
	 *            the salida to set
	 */
	public void setSalida(Registro salida) {
		Registro r = salida;
		if (null != r
				&& (r instanceof IWrapperClass)
				&& ((IWrapperClass) r).isWrapperFor(BasicRegistroFactory
						.getIBasicRegistroFactory().getSupports())) {
			r = ((IWrapperClass) r).unwrap(BasicRegistroFactory
					.getIBasicRegistroFactory().getSupports());
		}
		this.salida = r;
	}

	/**
	 * @return the idServicio
	 */
	public String getIdServicio() {
		return idServicio;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
}
