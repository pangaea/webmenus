///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.genesys.SystemServlet;

class OpHours
{
	public static int SUNDAY = 1;
	public static int MONDAY = 2;
	public static int TUESDAY = 4;
	public static int WEDNESDAY = 8;
	public static int THURSDAY = 16;
	public static int FRIDAY = 32;
	public static int SATURDAY = 64;

	//private Weekday m_weekday;
	private long			m_weekdays;
	private int 			m_weekday;
	private Date			m_startTime;
	private int				m_minLen;
	//private Vector<String>	m_menus;
	private HashMap<String, String>	m_menus;
	
	public OpHours()
	{
		m_weekdays = 0;
		//m_menus = new Vector();
		m_menus = new HashMap();
	}
	public void addMenu( String menuId, String displayName )
	{
		//m_menus.add(Id);
		m_menus.put( menuId, displayName );
	}
	public boolean isAssignedToMenu( String menuId )
	{
		String menuDisplay = (String)m_menus.get( menuId );
		if( menuDisplay != null && menuDisplay.length() > 0 ) return true;
		else return false;
	}
	public void setWeekdays(long weekdays)
	{
		m_weekdays = weekdays;
	}
	public boolean isWeekdaySet(long weekday)
	{
		if((m_weekdays & weekday) != 0) return true;
		else return false;
	}
/*
	public void setWeekday(String weekday)
	{
		if(weekday.equalsIgnoreCase("SUNDAY")) m_weekday = 1;
		else if(weekday.equalsIgnoreCase("MONDAY")) m_weekday = 2;
		else if(weekday.equalsIgnoreCase("TUESDAY")) m_weekday = 3;
		else if(weekday.equalsIgnoreCase("WEDNESDAY")) m_weekday = 4;
		else if(weekday.equalsIgnoreCase("THURSDAY")) m_weekday = 5;
		else if(weekday.equalsIgnoreCase("FRIDAY")) m_weekday = 6;
		else if(weekday.equalsIgnoreCase("SATURDAY")) m_weekday = 7;
	}
	public int getWeekday()
	{
		return m_weekday;
	}
*/
	public void setStartTime(String time)
	{
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
			m_startTime = dateFormat.parse(time);
		}
		catch(java.text.ParseException e)
		{
			SystemServlet.g_logger.error( "Parse Exception thrown in {OpHours::setStartTime} - " + e.getMessage() );
		}
	}
	public Date getStartTime()
	{
		return m_startTime;
	}
	public void setMinLen(String hours, String minutes)
	{
		Integer iHours = new Integer(hours);
		Integer iMinutes = new Integer(minutes);
		m_minLen = (iHours.intValue() * 60) + iMinutes.intValue();
	}
	public int getMinLen()
	{
		return m_minLen;
	}
};