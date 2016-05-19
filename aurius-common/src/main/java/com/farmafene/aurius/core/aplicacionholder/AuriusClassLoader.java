/*
 * Copyright (c) 2009-2012 farmafene.com
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
package com.farmafene.aurius.core.aplicacionholder;

import java.net.URL;

import org.apache.xbean.classloader.JarFileClassLoader;

import com.farmafene.aurius.core.IAplicacionHolder;
import com.farmafene.aurius.core.IClassLoaderWithHolder;

class AuriusClassLoader extends JarFileClassLoader implements
		IClassLoaderWithHolder {

	private IAplicacionHolder holder;
	private ClassLoader containerClassLoader;

	AuriusClassLoader(IAplicacionHolder holder, ClassLoader classloader) {
		super(holder.getAplicacion(), new URL[] {}, classloader);
		this.holder = holder;
		this.containerClassLoader = classloader;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("={");
		sb.append("holder=").append(holder);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IClassLoaderWithHolder#getHolder()
	 */
	@Override
	public IAplicacionHolder getHolder() {
		return holder;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.farmafene.aurius.core.IClassLoaderWithHolder#getURLs()
	 */
	@Override
	public URL[] getURLs() {
		return super.getURLs();
	}

	/**
	 * @return el containerClassLoader
	 */
	public ClassLoader getContainerClassLoader() {
		return containerClassLoader;
	}

}
