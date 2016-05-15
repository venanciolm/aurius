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
package com.farmafene.aurius.mngt.jdbc;

import javax.transaction.xa.XAException;

/**
 * Implementación de las excepciones que produce el XAResource
 * 
 */
@SuppressWarnings("serial")
public class XAErrorsException extends XAException {

	/**
	 * Constructor
	 * 
	 * @param message
	 *            mensaje
	 * @param errorCode
	 *            código de error
	 */
	public XAErrorsException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            mensaje
	 * @param errorCode
	 *            código de error
	 * @param t
	 *            excepción que la causa
	 */
	public XAErrorsException(String message, int errorCode, Throwable t) {
		super(message);
		this.errorCode = errorCode;
		initCause(t);
	}
}
