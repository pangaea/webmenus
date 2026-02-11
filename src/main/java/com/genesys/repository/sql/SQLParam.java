package com.genesys.repository.sql;

import java.util.*;

public class SQLParam
{
	public enum ParamType
	{
		BOOLEAN, DATE, TIME, DATETIME,
		INTEGER, REAL, TEXT, PASSWORD;
	};
	private String m_sName;
	private String m_sValue;
	private int m_iValue;
	private double m_dValue;
	private ParamType m_type;
	private String m_passQry;

	public SQLParam( String name, String value, ParamType type )
	{
		m_sName = new String(name);
		m_sValue = new String(value);
		m_type = type;
		m_passQry = "";
	}
	public SQLParam( String name, int value, ParamType type )
	{
		m_sName = new String(name);
		m_iValue = value;
		m_type = type;
	}
	public SQLParam( String name, double value, ParamType type )
	{
		m_sName = new String(name);
		m_dValue = value;
		m_type = type;
	}
	public String getName(){ return m_sName; };
	public String getText(){ return m_sValue; };
	public int getInt(){ return m_iValue; };
	public double getReal(){ return m_dValue; };
	public ParamType getType(){ return m_type; };
	
	public void setPasswordQuery(String qry){m_passQry = qry;}
	public String getPasswordQuery(){return m_passQry;}
}