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

import java.io.Serializable;

import com.farmafene.aurius.core.GestorMonitorizacion;
import com.farmafene.aurius.mngt.OperacionEvent.ResourceNotificationType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@SuppressWarnings("serial")
@XStreamAlias("AuriusCoreOperacionStatus")
@XStreamConverter(AuriusCoreOperacionStatusMeterConverter.class)
public class AuriusCoreOperacionStatus extends AuriusCoreOperacionStatusMeter
		implements Serializable, IBasicStat, IAuriusCoreStat {

	/**
	 * Constructor
	 */
	public AuriusCoreOperacionStatus() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#start()
	 */
	@Override
	public void start() {
		super.start();
		GestorMonitorizacion.fire(new OperacionEvent(
				ResourceNotificationType.BEGIN, this));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#stop()
	 */
	@Override
	public void stop() {
		stop(null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.mngt.AuriusCoreOperacionStatusMeter#stop(java.lang.Throwable)
	 */
	@Override
	public void stop(Throwable th) {
		super.stop(th);
		GestorMonitorizacion.fire(new OperacionEvent(
				ResourceNotificationType.END, this));
	}
}
