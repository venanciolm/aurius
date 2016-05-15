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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.farmafene.aurius.mngt.AuriusResource;

public class PreparedStatementDecorator extends StatementDecorator implements
		PreparedStatement {

	private PreparedStatement inner;
	private String sql;
	private String[] parameters = null;
	private Map<String, String> namedParams = null;

	public PreparedStatementDecorator(ConnectionDecorator con,
			PreparedStatement prepareStatement, String sql) {
		super(con, prepareStatement);
		this.inner = prepareStatement;
		this.sql = sql;
	}

	protected void addParameter(int posicion, Object x) throws SQLException {
		if (this.parameters == null) {
			this.parameters = new String[this.inner.getParameterMetaData()
					.getParameterCount()];
		}
		this.parameters[posicion - 1] = (x == null ? null : String.valueOf(x));
	}

	protected void addParameter(String name, Object x) throws SQLException {
		if (this.namedParams == null) {
			this.namedParams = new LinkedHashMap<String, String>(1);
		}
		this.namedParams.put(name, (x == null ? null : String.valueOf(x)));
	}

	protected List<String> getParametersList() {
		List<String> aDevolver = new LinkedList<String>();
		if (null != parameters && 0 < parameters.length) {
			Collections.addAll(aDevolver, parameters);
		}
		return aDevolver;

	}

	protected Map<String, String> getNamedParameters() {
		Map<String, String> aDevolver = null;
		if (null != namedParams && 0 < namedParams.size()) {
			aDevolver = new HashMap<String, String>(namedParams);
		}
		return aDevolver;
	}

	public void addBatch() throws SQLException {
		inner.addBatch();
	}

	public void clearParameters() throws SQLException {
		inner.clearParameters();
		namedParams = null;
		parameters = null;
	}

	public boolean execute() throws SQLException {
		boolean salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, sql, getParametersList(),
				getNamedParameters());
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.execute();
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
		return salida;
	}

	public ResultSet executeQuery() throws SQLException {
		ResultSet salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, sql, getParametersList(),
				getNamedParameters());
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeQuery();
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
		return salida;
	}

	public int executeUpdate() throws SQLException {
		int salida;
		JdbcExecuteStatus r = new JdbcExecuteStatus(getAuriusResource(),
				AuriusResource.Operacion.OPERATION, sql, getParametersList(),
				getNamedParameters());
		Throwable thStatus = null;
		try {
			r.start();
			salida = inner.executeUpdate();
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
		return salida;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return inner.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return inner.getParameterMetaData();
	}

	public void setArray(int i, Array array) throws SQLException {
		inner.setArray(i, array);
		addParameter(i, "Array");
	}

	public void setAsciiStream(int i, InputStream inputstream)
			throws SQLException {
		inner.setAsciiStream(i, inputstream);
		addParameter(i, "AsciiStream");
	}

	public void setAsciiStream(int i, InputStream inputstream, int j)
			throws SQLException {
		inner.setAsciiStream(i, inputstream, j);
		addParameter(i, "AsciiStream_int");
	}

	public void setAsciiStream(int i, InputStream inputstream, long l)
			throws SQLException {
		inner.setAsciiStream(i, inputstream, l);
		addParameter(i, "AsciiStream_long");

	}

	public void setBigDecimal(int i, BigDecimal bigdecimal) throws SQLException {
		inner.setBigDecimal(i, bigdecimal);
		addParameter(i, bigdecimal);
	}

	public void setBinaryStream(int i, InputStream inputstream)
			throws SQLException {
		inner.setBinaryStream(i, inputstream);
		addParameter(i, "BinaryStream");

	}

	public void setBinaryStream(int i, InputStream inputstream, int j)
			throws SQLException {
		inner.setBinaryStream(i, inputstream, j);
		addParameter(i, "BinaryStream_int");

	}

	public void setBinaryStream(int i, InputStream inputstream, long l)
			throws SQLException {
		inner.setBinaryStream(i, inputstream, l);
		addParameter(i, "BinaryStream_long");
	}

	public void setBlob(int i, Blob blob) throws SQLException {
		inner.setBlob(i, blob);
		addParameter(i, blob);
	}

	public void setBlob(int i, InputStream inputstream) throws SQLException {
		inner.setBlob(i, inputstream);
		addParameter(i, "InputStream");
	}

	public void setBlob(int i, InputStream inputstream, long l)
			throws SQLException {
		inner.setBlob(i, inputstream);
		addParameter(i, "InputStream_long");
	}

	public void setBoolean(int i, boolean flag) throws SQLException {
		inner.setBoolean(i, flag);
		addParameter(i, flag);
	}

	public void setByte(int i, byte byte0) throws SQLException {
		inner.setByte(i, byte0);
		addParameter(i, byte0);
	}

	public void setBytes(int i, byte[] abyte0) throws SQLException {
		inner.setBytes(i, abyte0);
		addParameter(i, abyte0);
	}

	public void setCharacterStream(int i, Reader reader) throws SQLException {
		inner.setCharacterStream(i, reader);
		addParameter(i, "Reader");
	}

	public void setCharacterStream(int i, Reader reader, int j)
			throws SQLException {
		inner.setCharacterStream(i, reader, j);
		addParameter(i, "Reader_int");
	}

	public void setCharacterStream(int i, Reader reader, long l)
			throws SQLException {
		inner.setCharacterStream(i, reader, l);
		addParameter(i, "Reader_long");
	}

	public void setClob(int i, Clob clob) throws SQLException {
		inner.setClob(i, clob);
		addParameter(i, clob);
	}

	public void setClob(int i, Reader reader) throws SQLException {
		inner.setClob(i, reader);
		addParameter(i, "Reader");
	}

	public void setClob(int i, Reader reader, long l) throws SQLException {
		inner.setClob(i, reader);
		addParameter(i, "Reader_long");
	}

	public void setDate(int i, Date date) throws SQLException {
		inner.setDate(i, date);
		addParameter(i, date);
	}

	public void setDate(int i, Date date, Calendar calendar)
			throws SQLException {
		inner.setDate(i, date, calendar);
		addParameter(i, date);
	}

	public void setDouble(int i, double d) throws SQLException {
		inner.setDouble(i, d);
		addParameter(i, d);
	}

	public void setFloat(int i, float f) throws SQLException {
		inner.setFloat(i, f);
		addParameter(i, f);
	}

	public void setInt(int i, int j) throws SQLException {
		inner.setInt(i, j);
		addParameter(i, j);
	}

	public void setLong(int i, long l) throws SQLException {
		inner.setLong(i, l);
		addParameter(i, l);
	}

	public void setNCharacterStream(int i, Reader reader) throws SQLException {
		inner.setNCharacterStream(i, reader);
		addParameter(i, "Reader");

	}

	public void setNCharacterStream(int i, Reader reader, long l)
			throws SQLException {
		inner.setNCharacterStream(i, reader, l);
		addParameter(i, "Reader_long");
	}

	public void setNClob(int i, NClob nclob) throws SQLException {
		inner.setNClob(i, nclob);
		addParameter(i, nclob);
	}

	public void setNClob(int i, Reader reader) throws SQLException {
		inner.setNClob(i, reader);
		addParameter(i, "Reader");
	}

	public void setNClob(int i, Reader reader, long l) throws SQLException {
		inner.setNClob(i, reader, l);
		addParameter(i, "Reader_long");
	}

	public void setNString(int i, String s) throws SQLException {
		inner.setNString(i, s);
		addParameter(i, s);
	}

	public void setNull(int i, int j) throws SQLException {
		inner.setNull(i, j);
		addParameter(i, null);
	}

	public void setNull(int i, int j, String s) throws SQLException {
		inner.setNull(i, j, s);
		addParameter(i, null);
	}

	public void setObject(int i, Object obj) throws SQLException {
		inner.setObject(i, obj);
		addParameter(i, obj);
	}

	public void setObject(int i, Object obj, int j) throws SQLException {
		inner.setObject(i, obj, j);
		addParameter(i, obj);
	}

	public void setObject(int i, Object obj, int j, int k) throws SQLException {
		inner.setObject(i, obj, j, k);
		addParameter(i, obj);
	}

	public void setRef(int i, Ref ref) throws SQLException {
		inner.setRef(i, ref);
		addParameter(i, ref);
	}

	public void setRowId(int i, RowId rowid) throws SQLException {
		inner.setRowId(i, rowid);
		addParameter(i, rowid);
	}

	public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
		inner.setSQLXML(i, sqlxml);
		addParameter(i, "SQLXML");
	}

	public void setShort(int i, short word0) throws SQLException {
		inner.setShort(i, word0);
		addParameter(i, word0);

	}

	public void setString(int i, String s) throws SQLException {
		inner.setString(i, s);
		addParameter(i, s);
	}

	public void setTime(int i, Time time) throws SQLException {
		inner.setTime(i, time);
		addParameter(i, time);
	}

	public void setTime(int i, Time time, Calendar calendar)
			throws SQLException {
		inner.setTime(i, time, calendar);
		addParameter(i, time);
	}

	public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		inner.setTimestamp(i, timestamp);
		addParameter(i, timestamp);
	}

	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar)
			throws SQLException {
		inner.setTimestamp(i, timestamp, calendar);
		addParameter(i, timestamp);
	}

	public void setURL(int i, URL url) throws SQLException {
		inner.setURL(i, url);
		addParameter(i, url);

	}

	@SuppressWarnings("deprecation")
	public void setUnicodeStream(int i, InputStream inputstream, int j)
			throws SQLException {
		inner.setUnicodeStream(i, inputstream, j);
		addParameter(i, "InputStream");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		if (iface == null) {
			throw new SQLException("la clase no puede ser null");
		}
		return iface.isAssignableFrom(inner.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (!isWrapperFor(iface)) {
			throw new SQLException("Not asignable");
		}
		return (T) inner;
	}
}
