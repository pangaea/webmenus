package com.genesys.repository.sql;

import java.util.*;

public class QueryPropertyList
{
	//HashMap m_ValuesMap;
	Vector	m_PropList;
	
	public QueryPropertyList()
	{
		m_PropList = new Vector();
	}
	public void add( QueryProperty obj )
	{
		m_PropList.add( obj );
	}
	public int count()
	{
		return m_PropList.size();
	}
	public QueryProperty get( int index )
	{
		return (QueryProperty)m_PropList.get( index );
	}
}