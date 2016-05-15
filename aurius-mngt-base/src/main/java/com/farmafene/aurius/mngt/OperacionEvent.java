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

import java.util.EventObject;

@SuppressWarnings("serial")
public class OperacionEvent extends EventObject {

	public static enum ResourceNotificationType {
		BEGIN, END
	}

	private ResourceNotificationType resourceNotificationType;

	public OperacionEvent(ResourceNotificationType resourceNotificationType,
			IBasicStat source) {
		super(source);
		this.resourceNotificationType = resourceNotificationType;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("={");
		sb.append("Type=").append(resourceNotificationType);
		sb.append(", source=").append(getSource());
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @return the resourceNotificationType
	 */
	public ResourceNotificationType getTipo() {
		return resourceNotificationType;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.EventObject#getSource()
	 */
	@Override
	public IBasicStat getSource() {
		return (IBasicStat) super.getSource();
	}
}
