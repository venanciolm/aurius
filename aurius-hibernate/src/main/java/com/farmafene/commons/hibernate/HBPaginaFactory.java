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
package com.farmafene.commons.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.ScrollableResults;

/**
 * Factoría de listas
 * 
 * @author vlopez
 * @since 1.0.0
 * @version 1.0.0
 * 
 * @param <T>
 */
public class HBPaginaFactory<T extends Serializable> {

	private HBSrollableResultReaderHandler<T> handler;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	class HBPaginaImp implements HBPagina<T> {

		private int first;
		private int pageSize;
		private List<T> lista;
		private int totalSize;

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public HBPaginaImp() {
			lista = new ArrayList<T>();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(getClass().getSimpleName());
			sb.append("={");
			sb.append("desde=").append(getFirst());
			sb.append(", hasta=").append(getLast());
			sb.append(", de=").append(getTotalSize());
			sb.append("}");
			return sb.toString();

		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public int getFirst() {
			return first;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public int getLast() {
			return (lista.size() == 0) ? 0 : first - 1 + lista.size();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public List<T> getLista() {
			return lista;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public int getNumRegistros() {
			return lista.size();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public int getPageSize() {
			return pageSize;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public int getTotalSize() {
			return totalSize;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public void setFirst(int first) {
			this.first = first;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @since 1.0.0
		 */
		public void setTotalSize(int totalSize) {
			this.totalSize = totalSize;
		}

	}

	/**
	 * Constructor por defecto
	 * 
	 * @since 1.0.0
	 */
	public HBPaginaFactory() {
		this(new HBSrollableResultReaderHandler<T>() {

			@SuppressWarnings("unchecked")
			public T makeObject(Object[] objects) {
				T salida = null;
				if (objects != null) {

					salida = (T) objects[0];
				}
				return salida;
			}
		});

	}

	/**
	 * Constructor con handler
	 * 
	 * @param handler
	 *            handler que manejará la salida
	 * @since 1.0.0
	 */
	public HBPaginaFactory(HBSrollableResultReaderHandler<T> handler) {
		this.handler = handler;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		// TODO !!
		sb.append("}");
		return sb.toString();

	}

	/**
	 * Metodo factor�a
	 * 
	 * @param begin
	 *            primer registro
	 * @param size
	 *            tamaño página
	 * @param result
	 *            dónde buscar
	 * @return página generada
	 * @since 1.0.0
	 */
	public HBPagina<T> getPagina(int begin, int size, ScrollableResults result) {
		HBPaginaImp lista = new HBPaginaImp();
		if (lista.getFirst() < 1) {
			lista.setFirst(1);
		}
		if (lista.getPageSize() < 0) {
			lista.setPageSize(0);
		}
		result.beforeFirst();
		result.scroll(lista.getFirst() - 1);
		for (int i = 0; result.next()
				&& (lista.getPageSize() == 0 || i < lista.getPageSize()); i++) {
			T obj = handler.makeObject(result.get());
			if (obj != null) {
				lista.getLista().add(obj);
			}
		}
		if (result.last()) {
			lista.setTotalSize(result.getRowNumber() + 1);
		} else {
			lista.setTotalSize(0);
			lista.setFirst(0);
		}
		return lista;
	}
}
