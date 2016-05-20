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
package com.farmafene.aurius.jca.inbound;

import javax.transaction.xa.XAResource;

public class XAHelper {

	public static String getStringFromFlag(int flags) {
		String salida = null;
		switch (flags) {
		case XAResource.TMENDRSCAN:
			salida = "TMENDRSCAN";
			break;
		case XAResource.TMFAIL:
			salida = "TMFAIL";
			break;
		case XAResource.TMJOIN:
			salida = "TMJOIN";
			break;
		case XAResource.TMNOFLAGS:
			salida = "TMNOFLAGS";
			break;
		case XAResource.TMONEPHASE:
			salida = "TMONEPHASE";
			break;
		case XAResource.TMRESUME:
			salida = "TMRESUME";
			break;
		case XAResource.TMSTARTRSCAN:
			salida = "TMSTARTRSCAN";
			break;
		case XAResource.TMSUCCESS:
			salida = "TMSUCCESS";
			break;
		case XAResource.TMSUSPEND:
			salida = "TMSUSPEND";
			break;
		default:
			salida = "NO RECONOCIDO";
			break;
		}

		return salida;
	}

}
