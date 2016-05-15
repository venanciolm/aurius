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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

import com.farmafene.aurius.mngt.AuriusResource;

public class XAConnectionDecorator implements XAConnection {

	private AuriusResource resource;
	private XAConnection inner;

	public XAConnectionDecorator(AuriusResource resource, XAConnection con) {
		if (con == null || resource == null) {
			throw new IllegalArgumentException("XAConnectionDecorator("
					+ resource + "," + con
					+ "); No se adminten parámetros nulos.");
		}
		this.resource = resource;
		this.inner = con;
	}

	public XAResource getXAResource() throws SQLException {
		return inner.getXAResource();
	}

	public void addConnectionEventListener(ConnectionEventListener listener) {
		inner.addConnectionEventListener(listener);
	}

	public void addStatementEventListener(StatementEventListener listener) {
		inner.addStatementEventListener(listener);
	}

	public void close() throws SQLException {
		JdbcConnectionStatus r = new JdbcConnectionStatus(resource, "close");
		Throwable thStatus = null;
		try {
			r.start();
			inner.close();
		} catch (RuntimeException e) {
			thStatus = e;
			throw new SQLException(e);
		} catch (SQLException e) {
			thStatus = e;
			throw e;
		} catch (Throwable e) {
			thStatus = e;
			throw new SQLException(e);
		} finally {
			r.stop(thStatus);
		}
	}

	public Connection getConnection() throws SQLException {
		Connection con = inner.getConnection();
		if (con != null) {
			con = new ConnectionDecorator(this.resource, con);
		}
		return con;
	}

	public void removeConnectionEventListener(ConnectionEventListener listener) {
		inner.removeConnectionEventListener(listener);

	}

	public void removeStatementEventListener(StatementEventListener listener) {
		inner.removeStatementEventListener(listener);
	}

}
