package com.genesys.repository;

import java.util.*;

public class ObjectProperties
{
	HashMap m_ValuesMap;
	Vector	m_ValuesList;
	
	public ObjectProperties()
	{
		m_ValuesMap = new HashMap();
		m_ValuesList = new Vector();
	}
	public void add( ObjectProperty obj )
	{
		m_ValuesMap.put( obj.getName(), obj );
		m_ValuesList.add( obj );
	}
	public int count()
	{
		return m_ValuesList.size();
	}
	public ObjectProperty get( String name )
	{
		return (ObjectProperty)m_ValuesMap.get( name );
	}
	public ObjectProperty getAt( int index )
	{
		return (ObjectProperty)m_ValuesList.get( index );
	}
}