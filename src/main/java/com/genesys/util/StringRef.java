package com.genesys.util;

import java.lang.Integer;

import com.genesys.SystemServlet;

public class StringRef
{
	public String m_str;
	
	public StringRef()
	{
		m_str = "";
	}
	public StringRef( String str )
	{
		m_str = str;
	}
	public String getStr()
	{
		return m_str;
	}
	public int getInt()
	{
		int iResult = 0;
		try
		{
			iResult = Integer.parseInt( m_str );
		}
		catch( Exception ex )
		{
			SystemServlet.g_logger.error( "Exception thrown in StrignRef trying to convert " + m_str + " to an int" );
		}
		return iResult;
	}
};
