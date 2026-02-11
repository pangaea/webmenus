package com.genesys.repository;

import java.text.SimpleDateFormat;
import java.util.regex.*;
//import java.text.SimpleDateFormat;

import com.genesys.repository.sql.StatementBuilder.PropType;

public class ObjectProperty
{
	private PropType m_type;
	String m_sName;
	String m_sValue;
	int m_iValue;
	double m_dValue;
	//boolean m_objList = false;

	public ObjectProperty()
	{
		m_sName = new String();
		m_type = PropType.INVALID;
	//	init( "", "" );
	}
	public ObjectProperty( String name, String value )
	{
		m_sName = new String( name );
		setValue(value);
	}
	public ObjectProperty( String name, int value )
	{
		m_sName = new String( name );
		setValue(value);
	}
	public ObjectProperty( String name, double value )
	{
		m_sName = new String( name );
		setValue(value);
	}
	//public void setIsObjList( boolean objList ){ m_objList = objList; };
	//public boolean getIsObjList(){ return m_objList; };
	public void setName( String name ){ m_sName =  name; }
	public String getName(){ return m_sName; };
	public void setValue( String value ){ m_type = PropType.TEXT; if( value != null ) m_sValue = new String(value); else m_sValue = new String(""); }
	public void setValue( int value ){ m_type = PropType.INT; m_iValue = value; }
	public void setValue( double value ){ m_type = PropType.REAL; m_dValue = value; }
	public String getText()
	{
		if( m_type == PropType.INT )
			return Integer.toString(m_iValue);
		else if( m_type == PropType.REAL )
			return Double.toString(m_dValue);
		else
			return m_sValue;
	};
	public String getDate()
	{
		//if( Pattern.matches("(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/\\d\\d", m_sValue) )
		//	return m_sValue;

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try
		{
			dateFormat.parse(m_sValue);
			return m_sValue;
		}
		catch(Exception e)
		{
			//return dateFormat.format(new java.util.Date());
			return "01/01/2000";
		}
	};
	public String getTime()
	{
		//if( Pattern.matches("(0[0-9]|1[0-9]|2[0-4]):([0-5][0-9]) [AM|PM]", m_sValue) )
		//	return m_sValue;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		try
		{
			dateFormat.parse(m_sValue);
			return m_sValue;
		}
		catch(Exception e)
		{
			//return dateFormat.format(new java.util.Date());
			return "12:00 AM";
		}
	};
	public String getDateTime()
	{
		//if( Pattern.matches("(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/\\d\\d (0[0-9]|1[0-9]|2[0-4]):([0-5][0-9]) [AM|PM]", m_sValue) )
		//	return m_sValue;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		try
		{
			dateFormat.parse(m_sValue);
			return m_sValue;
		}
		catch(Exception e)
		{
			//return dateFormat.format(new java.util.Date());
			return "01/01/2000 12:00 AM";
		}
	};
	public int getInt()
	{
		try
		{
			if( m_type == PropType.TEXT )
				return Integer.parseInt(m_sValue);
			else
				return m_iValue;
		}
		catch(Exception e)
		{
			return 0;
		}
	};
	public double getReal()
	{
		try
		{
			if( m_type == PropType.TEXT )
				return Double.parseDouble(m_sValue);
			else
				return m_dValue;
		}
		catch(Exception e)
		{
			return 0.0;
		}
	};
}