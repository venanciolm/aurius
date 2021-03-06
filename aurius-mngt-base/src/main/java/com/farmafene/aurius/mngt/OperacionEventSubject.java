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
package com.farmafene.aurius.mngt;

/**
 * Implementación de Base de observador
 * 
 * @author vlopez
 * @since 1.0.0
 */
public class OperacionEventSubject {

	private static final IOperacionEventSubject iOperacionEventSubject = create();

	/**
	 * Instancia la factoria
	 * 
	 * @return la factoria activa
	 * @since 1.0.0
	 */
	private static IOperacionEventSubject create() {

		IOperacionEventSubject conf = null;
		try {
			conf = new OperacionEventSubjectImpl();
		} catch (Exception e) {
			conf = new OperacionEventSubjectSustitute();
		}
		return conf;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("EvenSubject=").append(iOperacionEventSubject);
		sb.append("}");
		return sb.toString();
	}

	public static IOperacionEventSubject getIOperacionEventSubject() {
		return iOperacionEventSubject;
	}

	public static void fire(OperacionEvent event) {
		getIOperacionEventSubject().fire(event);
	}
}
