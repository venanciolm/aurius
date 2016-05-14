/*
 * Copyright (c) 2009-2010 farmafene.com
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
package com.farmafene.aurius;

/**
 * Excepción propagable, siempre es serializable
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class AuriusExceptionTO extends AuriusRuntimeException {

	private String className;

	/**
	 * Instancia una nueva excepción propagable
	 * 
	 * @param cause
	 *            causa de la excepción
	 * @since 1.0.0
	 */
	private AuriusExceptionTO(Throwable cause) {
		super(cause.getLocalizedMessage(), AuriusExceptionTO.getInstance(cause
				.getCause()));
		setStackTrace(cause.getStackTrace());
		className = cause.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		String s = getLocalizedMessage();
		String s1 = getClassName();
		if (s == null)
			return s1;
		return (new StringBuffer(s1.length() + 2 + s.length())).append(s1)
				.append(": ").append(s).toString();
	}

	/**
	 * Obtiene una instancia de una excepción a transferir
	 * 
	 * @param cause
	 *            causa de la excepción
	 * @return excepción a transferir
	 * @throws IllegalArgumentException
	 *             si th es <code>null</code>
	 * @since 1.0.0
	 */
	public static RuntimeException getInstance(Throwable cause)
			throws IllegalArgumentException {
		RuntimeException c = null;
		if (cause == null) {
			return null;
		} else if (cause instanceof AuriusRuntimeException) {
			c = (RuntimeException) cause;
		} else if (AuriusAuthException.class.equals(cause.getClass())) {
			c = (RuntimeException) cause;
		} else if (AuriusValidationException.class.equals(cause.getClass())) {
			c = (RuntimeException) cause;
		} else {
			c = new AuriusExceptionTO(cause);
		}
		return c;
	}

	/**
	 * Obtiene la clase que se decora (la causa).
	 * 
	 * @return clase de la excepción.
	 * @since 1.0.0
	 */
	private String getClassName() {
		return this.className;
	}
}
