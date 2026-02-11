package com.genesys.repository.sql;

import java.util.*;

public class SQLStatements
{
	HashMap m_StmtMap;
	Vector	m_StmtList;

	public SQLStatements()
	{
		m_StmtMap = new HashMap();
		m_StmtList = new Vector();
	}
	public void add( SQLStatement stmt )
	{
		m_StmtMap.put( stmt.getTable(), stmt );
		m_StmtList.add( stmt );
	}
	public int count()
	{
		return m_StmtList.size();
	}
	public SQLStatement get( String name )
	{
		return (SQLStatement)m_StmtMap.get( name );
	}
	public SQLStatement getAt( int index )
	{
		return (SQLStatement)m_StmtList.get( index );
	}
}