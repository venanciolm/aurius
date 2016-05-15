package com.farmafene.aurius.mngt.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PooledConnectionSubject {

	public void connectionClosed(SQLException e);

	public void connectionErrorOccurred(SQLException e);

	public void statementClosed(PreparedStatement stmt, SQLException e);

	public void statementErrorOccurred(PreparedStatement stmt, SQLException e);

	public void addStatement(PreparedStatement stmt);
}
