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

public interface IAuriusCoreStat {

	/**
	 * Establece el error en el status
	 */
	public abstract void setError(Throwable error);

	/**
	 * Obtiene el identificador del Thread
	 * 
	 * @return el identificador, null si no está activo
	 */
	public abstract Long getThreadId();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#start()
	 */
	public abstract void start();

	/**
	 * Realiza la pausa del thread
	 */
	public abstract void pause();

	/**
	 * Realiza el establecimiento del read
	 * 
	 * @param th
	 */
	public abstract void resume(long th);

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public abstract long getCpuTime();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public abstract long getContentionTime();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public abstract long getWaitedTime();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 */
	public abstract long getBlockedTime();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#getDuracion()
	 */
	public abstract long getDuracion();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#getInitTime()
	 */
	public abstract long getInitTime();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#isLocal()
	 */
	public abstract boolean isLocal();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#isStop()
	 */
	public abstract boolean isStop();

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.0.0
	 * @see com.farmafene.aurius.mngt.AuriusOperacionStatus#stop(java.lang.Throwable)
	 */
	public abstract void stop(Throwable th);

}