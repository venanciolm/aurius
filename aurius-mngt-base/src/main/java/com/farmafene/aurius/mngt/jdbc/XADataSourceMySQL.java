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
package com.farmafene.aurius.mngt.jdbc;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import com.farmafene.aurius.mngt.AuriusNamedResource;
import com.farmafene.aurius.mngt.AuriusResource;
import com.farmafene.aurius.mngt.AuriusResourceStatus;
import com.farmafene.aurius.mngt.IBasicStat;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@SuppressWarnings("serial")
public class XADataSourceMySQL extends MysqlXADataSource implements
		XADataSource, AuriusNamedResource {

	private AuriusResource resource;

	public XADataSourceMySQL() {
		super();
		this.resource = new AuriusResource("MySQL", AuriusResource.Type.JDBC);
		try {
			super.setPinGlobalTxToPhysicalConnection(true);
		} catch (Throwable th) {
			// do nothing
		}
	}

	public XAConnection getXAConnection() throws SQLException {
		XAConnection con = null;
		IBasicStat r = new AuriusResourceStatus(resource,
				AuriusResource.Operacion.OPEN);
		Throwable thStatus = null;
		try {
			r.start();
			con = super.getXAConnection();
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		if (con != null) {
			con = new XAConnectionDecorator(resource, con);
		}
		return con;
	}

	public XAConnection getXAConnection(String u, String p) throws SQLException {
		XAConnection con = null;
		IBasicStat r = new AuriusResourceStatus(resource,
				AuriusResource.Operacion.OPEN);
		Throwable thStatus = null;
		try {
			r.start();
			con = super.getXAConnection(u, p);
		} catch (RuntimeException th) {
			thStatus = th;
			throw th;
		} catch (SQLException th) {
			thStatus = th;
			throw th;
		} catch (Throwable th) {
			thStatus = th;
			throw new SQLException(th);
		} finally {
			r.stop(thStatus);
		}
		if (con != null) {
			con = new XAConnectionDecorator(resource, con);
		}
		return con;
	}

	/**
	 * @return the jdbcResource
	 */
	public String getAuriusResourceName() {
		return this.resource.getName();
	}

	/**
	 * @param jdbcResource
	 *            the jdbcResource to set
	 */
	public void setAuriusResourceName(String jdbcResource) {
		if (null != jdbcResource && !"".equals(jdbcResource.trim())) {
			resource = new AuriusResource(jdbcResource,
					AuriusResource.Type.JDBC);
		}
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.sql.CommonDataSource#getParentLogger()
	 */
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Not supproted");
	}
}
