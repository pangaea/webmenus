package com.genesys.repository;

import java.util.HashMap;

public class RepositoryObjectRef
{
	private String id;
	HashMap m_PropertyMap;
	
	public RepositoryObjectRef( String id )
	{
		this.id = id;
		m_PropertyMap = new HashMap();
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public void addProperty( ObjectProperty obj )
	{
		m_PropertyMap.put( obj.getName(), obj );
	}
	
	public ObjectProperty getProperty( String name )
	{
		return (ObjectProperty)m_PropertyMap.get( name );
	}
}