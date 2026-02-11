package com.genesys.repository;

import java.util.*;
//import java.math.BigDecimal;

public class RepositoryObject
{
	String m_id;
	ObjectProperties m_props;
	HashMap m_embeddObjLists;
	
	public RepositoryObject( String id )
	{
		m_id = id;
		m_props = new ObjectProperties();
		m_embeddObjLists = new HashMap();

	}
	
	public void addProperty( ObjectProperty prop )
	{
		m_props.add(prop);
	}
	
	public void addPropertyObjRefs( String name, RepositoryObjectRefList objList )
	{
		m_embeddObjLists.put(name, objList);
	}
	
	public String getId()
	{
		return m_id;
	}
	
	public RepositoryObjectRefList getPropertyObjectRefs( String name )
	{
		return (RepositoryObjectRefList)m_embeddObjLists.get(name);
	}
	
	public int getNumOfProperties()
	{
		return m_props.count();
	}
	
	public ObjectProperty getPropertyByIndex( int index )
	{
		return m_props.getAt(index);
	}

	public String getPropertyValue( String name )
	{
		ObjectProperty prop = m_props.get(name);
		if( prop == null ) return new String("");
		return prop.getText();
	}
	
	public int getPropertyValue_Int( String name )
	{
		ObjectProperty prop = m_props.get(name);
		if( prop == null ) return 0;
		return prop.getInt();
	}

	public double getPropertyValue_Real( String name )
	{
		ObjectProperty prop = m_props.get(name);
		if( prop == null ) return 0;
		return prop.getReal();
	}
	
	public boolean getPropertyValue_Boolean( String name )
	{
		ObjectProperty prop = m_props.get(name);
		if( prop != null && prop.getText().equalsIgnoreCase("Y")) return true;
		return false;
	}
}