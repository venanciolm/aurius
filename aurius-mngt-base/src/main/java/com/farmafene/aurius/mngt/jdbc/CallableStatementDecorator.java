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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Implementación del decorador
 * 
 * @author vlopez@farmafene.com
 * 
 */
public class CallableStatementDecorator extends PreparedStatementDecorator
		implements CallableStatement {

	private CallableStatement inner;

	public CallableStatementDecorator(ConnectionDecorator con,
			CallableStatement prepareCall, String sql) {
		super(con, prepareCall, sql);
		this.inner = prepareCall;
	}

	public Array getArray(int i) throws SQLException {
		return inner.getArray(i);
	}

	public Array getArray(String s) throws SQLException {
		return inner.getArray(s);
	}

	public BigDecimal getBigDecimal(int i) throws SQLException {
		return inner.getBigDecimal(i);
	}

	public BigDecimal getBigDecimal(String s) throws SQLException {
		return inner.getBigDecimal(s);
	}

	@Deprecated
	public BigDecimal getBigDecimal(int i, int j) throws SQLException {

		return inner.getBigDecimal(i, j);
	}

	public Blob getBlob(int i) throws SQLException {
		return inner.getBlob(i);
	}

	public Blob getBlob(String s) throws SQLException {
		return inner.getBlob(s);
	}

	public boolean getBoolean(int i) throws SQLException {
		return inner.getBoolean(i);
	}

	public boolean getBoolean(String s) throws SQLException {
		return inner.getBoolean(s);
	}

	public byte getByte(int i) throws SQLException {
		return inner.getByte(i);
	}

	public byte getByte(String s) throws SQLException {
		return inner.getByte(s);
	}

	public byte[] getBytes(int i) throws SQLException {
		return inner.getBytes(i);
	}

	public byte[] getBytes(String s) throws SQLException {
		return inner.getBytes(s);
	}

	public Reader getCharacterStream(int i) throws SQLException {
		return inner.getCharacterStream(i);
	}

	public Reader getCharacterStream(String s) throws SQLException {
		return inner.getCharacterStream(s);
	}

	public Clob getClob(int i) throws SQLException {
		return inner.getClob(i);
	}

	public Clob getClob(String s) throws SQLException {
		return inner.getClob(s);
	}

	public Date getDate(int i) throws SQLException {
		return inner.getDate(i);
	}

	public Date getDate(String s) throws SQLException {
		return inner.getDate(s);
	}

	public Date getDate(int i, Calendar calendar) throws SQLException {
		return inner.getDate(i, calendar);
	}

	public Date getDate(String s, Calendar calendar) throws SQLException {
		return inner.getDate(s, calendar);
	}

	public double getDouble(int i) throws SQLException {
		return inner.getDouble(i);
	}

	public double getDouble(String s) throws SQLException {
		return inner.getDouble(s);
	}

	public float getFloat(int i) throws SQLException {
		return inner.getFloat(i);
	}

	public float getFloat(String s) throws SQLException {
		return inner.getFloat(s);
	}

	public int getInt(int i) throws SQLException {
		return inner.getInt(i);
	}

	public int getInt(String s) throws SQLException {
		return inner.getInt(s);
	}

	public long getLong(int i) throws SQLException {
		return inner.getLong(i);
	}

	public long getLong(String s) throws SQLException {
		return inner.getLong(s);
	}

	public Reader getNCharacterStream(int i) throws SQLException {
		return inner.getNCharacterStream(i);
	}

	public Reader getNCharacterStream(String s) throws SQLException {
		return inner.getNCharacterStream(s);
	}

	public NClob getNClob(int i) throws SQLException {
		return inner.getNClob(i);
	}

	public NClob getNClob(String s) throws SQLException {
		return inner.getNClob(s);
	}

	public String getNString(int i) throws SQLException {
		return inner.getNString(i);
	}

	public String getNString(String s) throws SQLException {
		return inner.getNString(s);
	}

	public Object getObject(int i) throws SQLException {
		return inner.getObject(i);
	}

	public Object getObject(String s) throws SQLException {
		return inner.getObject(s);
	}

	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException {
		return inner.getObject(parameterIndex, map);
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		return inner.getObject(parameterName, map);
	}

	public Ref getRef(int i) throws SQLException {
		return inner.getRef(i);
	}

	public Ref getRef(String s) throws SQLException {
		return inner.getRef(s);
	}

	public RowId getRowId(int i) throws SQLException {
		return inner.getRowId(i);
	}

	public RowId getRowId(String s) throws SQLException {
		return inner.getRowId(s);
	}

	public SQLXML getSQLXML(int i) throws SQLException {
		return inner.getSQLXML(i);
	}

	public SQLXML getSQLXML(String s) throws SQLException {
		return inner.getSQLXML(s);
	}

	public short getShort(int i) throws SQLException {
		return inner.getShort(i);
	}

	public short getShort(String s) throws SQLException {
		return inner.getShort(s);
	}

	public String getString(int i) throws SQLException {
		return inner.getString(i);
	}

	public String getString(String s) throws SQLException {
		return inner.getString(s);
	}

	public Time getTime(int i) throws SQLException {
		return inner.getTime(i);
	}

	public Time getTime(String s) throws SQLException {
		return inner.getTime(s);
	}

	public Time getTime(int i, Calendar calendar) throws SQLException {
		return inner.getTime(i, calendar);
	}

	public Time getTime(String s, Calendar calendar) throws SQLException {
		return inner.getTime(s, calendar);
	}

	public Timestamp getTimestamp(int i) throws SQLException {
		return inner.getTimestamp(i);
	}

	public Timestamp getTimestamp(String s) throws SQLException {
		return inner.getTimestamp(s);
	}

	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return inner.getTimestamp(i, calendar);
	}

	public Timestamp getTimestamp(String s, Calendar calendar)
			throws SQLException {
		return inner.getTimestamp(s, calendar);
	}

	public URL getURL(int i) throws SQLException {
		return inner.getURL(i);
	}

	public URL getURL(String s) throws SQLException {
		return inner.getURL(s);
	}

	public void registerOutParameter(int i, int j) throws SQLException {
		inner.registerOutParameter(i, j);

	}

	public void registerOutParameter(String s, int i) throws SQLException {
		inner.registerOutParameter(s, i);

	}

	public void registerOutParameter(int i, int j, int k) throws SQLException {
		inner.registerOutParameter(i, j, k);
	}

	public void registerOutParameter(int i, int j, String s)
			throws SQLException {
		inner.registerOutParameter(i, j, s);
	}

	public void registerOutParameter(String s, int i, int j)
			throws SQLException {
		inner.registerOutParameter(s, i, j);
	}

	public void registerOutParameter(String s, int i, String s1)
			throws SQLException {
		inner.registerOutParameter(s, i, s1);
	}

	public void setAsciiStream(String s, InputStream inputstream)
			throws SQLException {
		inner.setAsciiStream(s, inputstream);
		addParameter(s, "InputStream");
	}

	public void setAsciiStream(String s, InputStream inputstream, int i)
			throws SQLException {
		inner.setAsciiStream(s, inputstream, i);
		addParameter(s, "InputStream_int");
	}

	public void setAsciiStream(String s, InputStream inputstream, long l)
			throws SQLException {
		inner.setAsciiStream(s, inputstream, l);
		addParameter(s, "InputStream_long");
	}

	public void setBigDecimal(String s, BigDecimal bigdecimal)
			throws SQLException {
		inner.setBigDecimal(s, bigdecimal);
		addParameter(s, bigdecimal);
	}

	public void setBinaryStream(String s, InputStream inputstream)
			throws SQLException {
		inner.setBinaryStream(s, inputstream);
		addParameter(s, "InputStream");
	}

	public void setBinaryStream(String s, InputStream inputstream, int i)
			throws SQLException {
		inner.setBinaryStream(s, inputstream, i);
		addParameter(s, "InputStream_int");
	}

	public void setBinaryStream(String s, InputStream inputstream, long l)
			throws SQLException {
		inner.setBinaryStream(s, inputstream, l);
		addParameter(s, "InputStream_long");
	}

	public void setBlob(String s, Blob blob) throws SQLException {
		inner.setBlob(s, blob);
		addParameter(s, blob);
	}

	public void setBlob(String s, InputStream inputstream) throws SQLException {
		inner.setBlob(s, inputstream);
		addParameter(s, "InputStream");
	}

	public void setBlob(String s, InputStream inputstream, long l)
			throws SQLException {
		inner.setBlob(s, inputstream, l);
		addParameter(s, "InputStream_long");
	}

	public void setBoolean(String s, boolean flag) throws SQLException {
		inner.setBoolean(s, flag);
		addParameter(s, flag);
	}

	public void setByte(String s, byte byte0) throws SQLException {
		inner.setByte(s, byte0);
		addParameter(s, byte0);
	}

	public void setBytes(String s, byte[] abyte0) throws SQLException {
		inner.setBytes(s, abyte0);
		addParameter(s, abyte0);
	}

	public void setCharacterStream(String s, Reader reader) throws SQLException {
		inner.setCharacterStream(s, reader);
		addParameter(s, "Reader");
	}

	public void setCharacterStream(String s, Reader reader, int i)
			throws SQLException {
		inner.setCharacterStream(s, reader, i);
		addParameter(s, "Reader_int");
	}

	public void setCharacterStream(String s, Reader reader, long l)
			throws SQLException {
		inner.setCharacterStream(s, reader, l);
		addParameter(s, "Reader_long");
	}

	public void setClob(String s, Clob clob) throws SQLException {
		inner.setClob(s, clob);
		addParameter(s, clob);

	}

	public void setClob(String s, Reader reader) throws SQLException {
		inner.setClob(s, reader);
		addParameter(s, "Reader");
	}

	public void setClob(String s, Reader reader, long l) throws SQLException {
		inner.setClob(s, reader, l);
		addParameter(s, "Reader_long");
	}

	public void setDate(String s, Date date) throws SQLException {
		inner.setDate(s, date);
		addParameter(s, date);
	}

	public void setDate(String s, Date date, Calendar calendar)
			throws SQLException {
		inner.setDate(s, date, calendar);
		addParameter(s, date);
	}

	public void setDouble(String s, double d) throws SQLException {
		inner.setDouble(s, d);
		addParameter(s, d);
	}

	public void setFloat(String s, float f) throws SQLException {
		inner.setFloat(s, f);
		addParameter(s, f);
	}

	public void setInt(String s, int i) throws SQLException {
		inner.setInt(s, i);
		addParameter(s, i);
	}

	public void setLong(String s, long l) throws SQLException {
		inner.setLong(s, l);
		addParameter(s, l);
	}

	public void setNCharacterStream(String s, Reader reader)
			throws SQLException {
		inner.setNCharacterStream(s, reader);
		addParameter(s, "Reader");
	}

	public void setNCharacterStream(String s, Reader reader, long l)
			throws SQLException {
		inner.setNCharacterStream(s, reader, l);
		addParameter(s, "Reader_long");
	}

	public void setNClob(String s, NClob nclob) throws SQLException {
		inner.setNClob(s, nclob);
		addParameter(s, nclob);
	}

	public void setNClob(String s, Reader reader) throws SQLException {
		inner.setNClob(s, reader);
		addParameter(s, "Reader");
	}

	public void setNClob(String s, Reader reader, long l) throws SQLException {
		inner.setNClob(s, reader, l);
		addParameter(s, "Reader_long");
	}

	public void setNString(String s, String s1) throws SQLException {
		inner.setNString(s, s1);
		addParameter(s, s1);
	}

	public void setNull(String s, int i) throws SQLException {
		inner.setNull(s, i);
		addParameter(s, null);
	}

	public void setNull(String s, int i, String s1) throws SQLException {
		inner.setNull(s, i, s1);
		addParameter(s, null);
	}

	public void setObject(String s, Object obj) throws SQLException {
		inner.setObject(s, obj);
		addParameter(s, obj);
	}

	public void setObject(String s, Object obj, int i) throws SQLException {
		inner.setObject(s, obj, i);
		addParameter(s, obj);
	}

	public void setObject(String s, Object obj, int i, int j)
			throws SQLException {
		inner.setObject(s, obj, i, j);
		addParameter(s, obj);
	}

	public void setRowId(String s, RowId rowid) throws SQLException {
		inner.setRowId(s, rowid);
		addParameter(s, rowid);
	}

	public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {
		inner.setSQLXML(s, sqlxml);
		addParameter(s, "SQLXML");
	}

	public void setShort(String s, short word0) throws SQLException {
		inner.setShort(s, word0);
		addParameter(s, word0);
	}

	public void setString(String s, String s1) throws SQLException {
		inner.setString(s, s1);
		addParameter(s, s1);
	}

	public void setTime(String s, Time time) throws SQLException {
		inner.setTime(s, time);
		addParameter(s, time);
	}

	public void setTime(String s, Time time, Calendar calendar)
			throws SQLException {
		inner.setTime(s, time, calendar);
		addParameter(s, time);
	}

	public void setTimestamp(String s, Timestamp timestamp) throws SQLException {
		inner.setTimestamp(s, timestamp);
		addParameter(s, timestamp);
	}

	public void setTimestamp(String s, Timestamp timestamp, Calendar calendar)
			throws SQLException {
		inner.setTimestamp(s, timestamp, calendar);
		addParameter(s, timestamp);
	}

	public void setURL(String s, URL url) throws SQLException {
		inner.setURL(s, url);
		addParameter(s, url);
	}

	public boolean wasNull() throws SQLException {
		return inner.wasNull();
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.CallableStatement#getObject(int, java.lang.Class)
	 */
	public <T> T getObject(int parameterIndex, Class<T> type)
			throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.sql.CallableStatement#getObject(java.lang.String,
	 *      java.lang.Class)
	 */
	public <T> T getObject(String parameterName, Class<T> type)
			throws SQLException {
		throw new SQLFeatureNotSupportedException("Not Supported");
	}
}
