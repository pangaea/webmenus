package com.genesys.db;

import java.lang.Integer;

// JDBC Imports
import java.sql.*;

import com.genesys.SystemServlet;

public class DBResultSet
{
	// Internal list of results
	Statement m_resultStmt;
	ResultSet m_resultRows;
	
	public DBResultSet( Connection db, String query )
	{
		try
		{
			m_resultStmt = db.createStatement();
			m_resultRows = m_resultStmt.executeQuery( query );
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {DBResultSet::DBResultSet}" );
		}
	}
	public void close()
	{
		try
		{
			m_resultStmt.close();
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {DBResultSet::close}" );
		}
	}
	public String getStr( String name )
	{
		try
		{
			return m_resultRows.getString( name );
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {DBResultSet::getStr}" );
		}
		return null;
	}
	public int getInt( String name )
	{
		try
		{
			return m_resultRows.getInt( name );
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {DBResultSet::getInt}" );
		}
		return 0;
	}

	public boolean getNext()
	{
		try
		{
			return m_resultRows.next();
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {DBResultSet::getNext}" );
		}
		return false;
	}
};
