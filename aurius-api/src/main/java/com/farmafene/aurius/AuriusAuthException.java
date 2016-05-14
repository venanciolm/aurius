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
 * Excepción que indica que existe un error de autenticación
 * 
 * @author vlopez
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class AuriusAuthException extends IllegalStateException {

	/**
	 * 
	 */
	public AuriusAuthException() {
		super();
	}

	/**
	 * @param msg Any message
	 * @param cause Any cause
	 */
	public AuriusAuthException(String msg, Throwable cause) {
		super(msg, AuriusExceptionTO.getInstance(cause));
	}

	/**
	 * @param msg Any message
	 */
	public AuriusAuthException(String msg) {
		super(msg);
	}

	/**
	 * @param cause Any case
	 */
	public AuriusAuthException(Throwable cause) {
		super(AuriusExceptionTO.getInstance(cause));
	}

}
