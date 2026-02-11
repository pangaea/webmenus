package com.genesys.repository;

import java.util.*;

public class RepositoryObjects
{
	private Vector m_objList;

	public RepositoryObjects( Vector objList )
	{
		m_objList = objList;
	}
	
	public boolean avail()
	{
		if( m_objList == null )
			return false;
		else
			return true;
	}

	public int count()
	{
		if( avail() )
			return m_objList.size();
		else
			return 0;
	}
	
	public RepositoryObject get( int index )
	{
		return (RepositoryObject)m_objList.get(index);
	}
}