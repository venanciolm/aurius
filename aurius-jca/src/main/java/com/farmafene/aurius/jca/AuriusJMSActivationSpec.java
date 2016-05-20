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
package com.farmafene.aurius.jca;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.aurius.jca.inbound.IJCAPlataformAdapter;
import com.farmafene.aurius.server.Configuracion;
import com.farmafene.aurius.server.IConfiguracionEntries;

public class AuriusJMSActivationSpec extends AuriusJMSActivationSpecBasic
		implements ActivationSpec, IJCAPlataformAdapter {
	private static final String JCA_BROKER = "JCA.BROKER";
	private static final String JCA_QUEUE_CLUSTER = "JCA.QUEUE.CLUSTER";
	private static final String JCA_QUEUE_LOCAL = "JCA.QUEUE.LOCAL";
	private static final String JCA_TOPIC = "JCA.TOPIC";
	private static final String JCA_CLASS = "JCA.CLASS";
	private static final String JCA_USER = "JCA.USER";
	private static final String JCA_PWD = "JCA.PWD";
	private static final String JCA_MSG_TIMEOUT = "JCA.MSG.TIMEOUT";
	private static final String JCA_MSG_TIMETOLIVE = "JCA.MSG.TIMETOLIVE";

	private static final Logger logger = LoggerFactory
			.getLogger(AuriusJMSActivationSpec.class);
	private String confFile;

	public AuriusJMSActivationSpec() {
		logger.info("AuriusJMSActivationSpec<init>()");
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
		sb.append("ResourceAdapter=").append(getResourceAdapter());
		sb.append(", Queue=").append(getQueue());
		sb.append(", Broker=").append(getBroker());
		sb.append(", ClusterQueue=").append(getClusterQueue());
		sb.append(", confFile=").append(confFile);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AuriusJMSActivationSpec)) {
			return false;
		}
		AuriusJMSActivationSpec other = (AuriusJMSActivationSpec) obj;
		if (confFile == null) {
			if (other.confFile != null) {
				return false;
			}
		} else if (!confFile.equals(other.confFile)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((confFile == null) ? 0 : confFile.hashCode());
		return result;
	}

	private String getStringValue(IConfiguracionEntries entries, String key,
			String def, boolean nullable) throws InvalidPropertyException {
		String getStringValue = def;
		if (null != entries.getProperty(key)
				&& !"".equals(entries.getProperty(key).trim())) {
			getStringValue = entries.getProperty(key).trim();
		}
		if (!nullable && null == getStringValue) {
			throw new InvalidPropertyException("Invalid " + key);
		}
		return getStringValue;
	}

	private long getLongValue(IConfiguracionEntries entries, String key,
			long def) throws InvalidPropertyException {
		long getLongValue = def;
		if (null != entries.getProperty(key)
				&& !"".equals(entries.getProperty(key).trim())) {
			try {
				getLongValue = Long.parseLong(entries.getProperty(key).trim());
			} catch (Exception e) {
				throw new InvalidPropertyException("Invalid " + key, e);
			}

		}
		return getLongValue;
	}

	protected void init() throws InvalidPropertyException {
		IConfiguracionEntries entries = Configuracion.getEntries(this.confFile);
		if (null == entries) {
			throw new InvalidPropertyException("Fichero Erróneo: " + confFile);
		}
		setManagedConnectionFactoryClass(getStringValue(entries, JCA_CLASS,
				CONNECTION_FACTORY_DEFAULT, false));
		setTimeOut(getLongValue(entries, JCA_MSG_TIMEOUT,
				JCA_MSG_TIMEOUT_DEFAULT));
		setTimeToLive(getLongValue(entries, JCA_MSG_TIMETOLIVE,
				JCA_MSG_TIMETOLIVE_DEFAULT));

		setUser(getStringValue(entries, JCA_USER, null, true));
		setPassword(getStringValue(entries, JCA_PWD, null, true));
		setBroker(getStringValue(entries, JCA_BROKER, null, false));
		setQueue(getStringValue(entries, JCA_QUEUE_LOCAL, null, false));
		setClusterQueue(getStringValue(entries, JCA_QUEUE_CLUSTER, null, true));
		setTopic(getStringValue(entries, JCA_TOPIC, null, true));
	}
}
