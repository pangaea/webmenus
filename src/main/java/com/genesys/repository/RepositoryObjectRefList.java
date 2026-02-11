package com.genesys.repository;

import java.util.*;

public class RepositoryObjectRefList
{
	Vector m_Values;
	
	public RepositoryObjectRefList()
	{
		m_Values = new Vector();
	}
	public void add( RepositoryObjectRef obj )
	{
		m_Values.addElement( obj );
	}
	public int count()
	{
		return m_Values.size();
	}
	public RepositoryObjectRef get( int index )
	{
		return (RepositoryObjectRef)m_Values.get( index );
	}
}