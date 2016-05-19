/*
 * Copyright (c) 2009-2013 farmafene.com
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
package com.farmafene.aurius.server.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.AuriusExceptionTO;
import com.farmafene.aurius.AuriusValidationException;
import com.farmafene.aurius.CampoRegistro;
import com.farmafene.aurius.DatoRegistro;
import com.farmafene.aurius.Registro;
import com.farmafene.aurius.RegistroNoModificable;
import com.farmafene.aurius.TypeRegistro;
import com.farmafene.aurius.core.AplicacionHolderFactory;
import com.farmafene.aurius.core.ContextoCore;
import com.farmafene.aurius.core.ContextoManager;
import com.farmafene.aurius.core.Diccionario;
import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.Servicio;
import com.farmafene.aurius.mngt.ServiceStatus;
import com.farmafene.aurius.server.Command;
import com.farmafene.aurius.server.CommandInvoker;

@SuppressWarnings("serial")
public class CommandInvokerImpl implements CommandInvoker {

	private static final Logger logger = LoggerFactory
			.getLogger(CommandInvoker.class);

	private Registro registro;
	private String idResistroOut;
	private Servicio srv;

	/**
	 * Constructor
	 * 
	 * @param idServicio
	 * @param version
	 */
	public CommandInvokerImpl(String idServicio, String version) {
		this(idServicio, version, null);
	}

	/**
	 * Constructor
	 * 
	 * @param idServicio
	 * @param version
	 * @param rin
	 */
	public CommandInvokerImpl(String idServicio, String version, Registro rin) {
		srv = null;
		if (version == null) {
			srv = Diccionario.getServicio(idServicio);
		} else {
			srv = Diccionario.getServicio(idServicio, version);
		}
		if (rin != null) {
			this.registro = rin;
		} else {
			if (null != srv.getIdRegistroEntrada()) {
				this.registro = Diccionario.getRegistro(srv
						.getIdRegistroEntrada());
			}
		}
		this.idResistroOut = srv.getIdRegistroSalida();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pf = new PrintWriter(baos);
		pf.print("<invoker id=\"");
		pf.print(srv.getId());
		pf.print("\" version=\"");
		pf.print(srv.getVersion());
		if (this.registro != null) {
			pf.println("\">");
			pf.print(this.registro.toString());
			pf.println("</invoker>");
		} else {
			pf.println("\"/>");
		}
		try {
			baos.flush();
		} catch (IOException e) {
		}
		String salida = baos.toString();
		try {
			baos.close();
		} catch (IOException e) {
		}
		return salida;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.CommandInvoker#invoke()
	 */
	@Override
	public Registro invoke() {
		if (logger.isDebugEnabled()) {
			logger.debug("Invocando " + this + ", registro: " + registro);
		}
		valida(registro,
				Diccionario.getIDiccionario().getDefinicionRegistro(
						srv.getIdRegistroEntrada()));
		IAplicacionHolder holder = null;
		Registro regOut = null;
		ServiceStatus status = null;
		if (logger.isDebugEnabled()) {
			logger.debug("Buscando el AplicacionHolder");
		}
		try {
			holder = AplicacionHolderFactory.getHolder(srv);
		} catch (Throwable th) {
			logger.error("Excepción en el Holder", th);
			throw AuriusExceptionTO.getInstance(th);
		}
		if (null == holder) {
			throw new IllegalArgumentException(
					"No existe el el contenedor de servicio '" + srv + "' ");
		}
		if (null != idResistroOut) {
			regOut = Diccionario.getRegistro(idResistroOut);
		}
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		RuntimeException excepcion = null;
		ContextoCore ctx = null;
		String idServicioActual = null;
		String idVersionServicioActual = null;
		try {
			ctx = ContextoManager.getContexto();
			ClassLoader classLoader = holder.getClassLoader();
			status = doPrecondiciones();
			idServicioActual = ctx.getIdServicioActual();
			idVersionServicioActual = ctx.getVersionServicioActual();
			ContextoManager.getIContextoManager().setIdServicioActual(ctx,
					getIdServicio());
			ContextoManager.getIContextoManager().setVersionServicioActual(ctx,
					getVersion());
			Thread.currentThread().setContextClassLoader(classLoader);
			Class<?> svrClass = null;
			Object commandObject = null;
			Command command = null;
			try {
				svrClass = Class.forName(srv.getCommand(), true,
						holder.getClassLoader());
			} catch (ClassNotFoundException e) {
				RuntimeException re = new IllegalArgumentException(
						"No encontrada la clase [" + srv.getCommand()
								+ "] en el contenedor '" + holder + "'", e);
				logger.info(
						"Error en la optención del la clase '"
								+ srv.getCommand() + "'", re);
				throw re;
			}
			try {
				commandObject = svrClass.newInstance();
			} catch (InstantiationException e) {
				throw new IllegalStateException(
						"Error al instanciar la clase '" + svrClass + "'", e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(
						"Error al instanciar la clase '" + svrClass + "'", e);
			}
			try {
				command = (Command) commandObject;
			} catch (ClassCastException e) {
				throw new IllegalStateException("Error en la clase '"
						+ commandObject + "', no es del tipo correcto.", e);
			}
			command.invoke(new RegistroNoModificable(registro), regOut);
		} catch (Throwable th) {
			excepcion = AuriusExceptionTO.getInstance(th);
			throw excepcion;
		} finally {
			Thread.currentThread().setContextClassLoader(loader);
			ContextoManager.getIContextoManager().setVersionServicioActual(ctx,
					idVersionServicioActual);
			ContextoManager.getIContextoManager().setIdServicioActual(ctx,
					idServicioActual);
			doPoscondiciones(status, regOut, excepcion);
		}
		return regOut;
	}

	/**
	 * Validación del registro de entrada
	 * 
	 * @param reg
	 * @param def
	 */
	private void valida(Registro reg, TypeRegistro def) {
		if (null != def && null != reg) {
			for (String name : def.getNombres()) {
				CampoRegistro campo = def.getCampoRegistro(name);
				DatoRegistro<?> defDato = campo.getDatoRegistro();
				if (defDato instanceof TypeRegistro) {
					List<Registro> items = new LinkedList<Registro>();
					for (int i = 0; i < reg.getRegistroSize(campo.getNombre()); i++) {
						Registro item = reg.getRegistro(campo.getNombre(), i);
						items.add((Registro) defDato.valida(item));
					}
					campo.valida(items);

				} else {
					campo.valida(reg.get(campo.getNombre(), campo
							.getDatoRegistro().getValueType()));
				}
			}
		} else if (def != null) {
			throw new AuriusValidationException("Debe mandarse un registro '"
					+ def.getId() + "' de forma obligatorio.");
		}
		return;
	}

	/**
	 * Realiza las precondidciones
	 * 
	 * @return
	 */
	private ServiceStatus doPrecondiciones() {

		ServiceStatus status = new ServiceStatus(srv.getId(), srv.getVersion());
		status.setEntrada(registro);
		status.start();
		return status;
	}

	/**
	 * Realiza las postcondiciones
	 * 
	 * @param status
	 * @param out
	 * @param excepcion
	 */
	private void doPoscondiciones(ServiceStatus status, Registro out,
			Throwable excepcion) {
		if (logger.isDebugEnabled()) {

			logger.debug("doPoscondiciones(" + status + ", " + out + ", "
					+ excepcion + ")");
		}
		status.setSalida(out);
		status.stop(excepcion);
	}

	/**
	 * Obtiene el registro
	 * 
	 * @return registro de entrada activo
	 */
	private Registro getRegistro() {
		if (null == this.registro) {
			throw new IllegalArgumentException(
					"Registro <null> no permite modificaciones");
		}
		return this.registro;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.CommandInvoker#getIdServicio()
	 */
	@Override
	public String getIdServicio() {
		return this.srv.getId();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.server.CommandInvoker#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.srv.getVersion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#addRegistro(java.lang.String,
	 *      com.farmafene.aurius.Registro)
	 */
	@Override
	public void addRegistro(String nombre, Registro value) {
		getRegistro().addRegistro(nombre, value);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T extends Serializable> T get(String nombre, Class<T> clazz)
			throws IllegalArgumentException {
		return getRegistro().get(nombre, clazz);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(String nombre) throws IllegalArgumentException {
		return getRegistro().getBytes(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String nombre) throws IllegalArgumentException {
		return getRegistro().getDate(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getDecimal(String nombre) throws IllegalArgumentException {
		return getRegistro().getDecimal(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getId()
	 */
	@Override
	public String getId() {
		String id = null;
		if (null == this.registro) {
			id = this.registro.getId();
		}
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getRegistro(java.lang.String, int)
	 */
	@Override
	public Registro getRegistro(String nombre, int pos)
			throws IllegalArgumentException {
		return getRegistro().getRegistro(nombre, pos);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getRegistroSize(java.lang.String)
	 */
	@Override
	public int getRegistroSize(String nombre) throws IllegalArgumentException {
		return getRegistro().getRegistroSize(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#getString(java.lang.String)
	 */
	@Override
	public String getString(String nombre) throws IllegalArgumentException {
		return getRegistro().getString(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#newRegistro(java.lang.String)
	 */
	@Override
	public Registro newRegistro(String nombre) throws IllegalArgumentException {
		return getRegistro().newRegistro(nombre);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.Registro#put(java.lang.String,
	 *      java.io.Serializable)
	 */
	@Override
	public void put(String nombre, Serializable value)
			throws IllegalArgumentException {
		getRegistro().put(nombre, value);
	}
}
