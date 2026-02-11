package com.genesys.repository;

import java.util.*;

public class QueryResponse
{
	private String m_sClassName;
	private Vector m_objList;
	private long m_objCount;
	
	public QueryResponse( String className )
	{
		m_sClassName = className;
		m_objList = new Vector();
		m_objCount = 0;
	}
	
	public void setCount(long count){ m_objCount = count; }
	public long getCount(){ return m_objCount; }
	
	public String getClassName(){ return m_sClassName; }
	
	public void addObject( RepositoryObject obj )
	{
		m_objList.add(obj);
		m_objCount++;
	}
	
	public RepositoryObjects getObjects()
	{
		return getObjects(m_sClassName);
	}

	public RepositoryObjects getObjects( String collectionName )
	{
		// EVentually this class could hold many collections so I pass in the class name now.
		return new RepositoryObjects( m_objList );
	}
}