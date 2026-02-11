package com.genesys.repository.sql;

import java.util.Vector;

public class SQLEmbeddedObjStmt extends QueryProperty
{
	static final String m_szgetEmbedObjs1 =
		"select " +
		"T2.id as id ";
//		"T2.%s as display " +
//		"from " +
//		"%s T1 " +
//		"left join %s T2 on T1.objid = T2.id " +
//		"where T1.fkey='%s';";
	
//	static final String m_szgetEmbedObjs2 =
//		"select " +
//		"T2.id as id, " +
//		"T2.%s as display " +
		
	static final String m_szgetEmbedObjs2 =
		"from " +
		"%s T1 " +
		"left join %s T2 on T1.%s = T2.id " +
		"where T1.%s='%s' order by objindex asc;";

	//private String display;
	public Vector m_Cols;		// In know, this is cheating, I'm just making it public for now :-)
	private String refTable;
	private String baseTable;
	private String fKey;
	private String objectRef;

	public SQLEmbeddedObjStmt(String name, String display, String refTable, String baseTable, String fKey, String objectRef)
	{
		super(name,StatementBuilder.PropType.LIST);
		
		m_Cols = new Vector();
		String cols[] = display.split(",");
		for( int i = 0; i < cols.length; i++ )
			m_Cols.add(cols[i]);
		//this.display = display;
		
		
		this.refTable = refTable;
		this.baseTable = baseTable;
		this.fKey = fKey;
		this.objectRef = objectRef;
	}

	public String generateQuery(String sObjId)
	{
		//return String.format(m_szgetEmbedObjs, this.display, this.refTable, this.baseTable, sObjId);
		String result = m_szgetEmbedObjs1;
		
		for( int i = 0; i < m_Cols.size(); i++ )
			result += String.format(", T2.%s as %s ", m_Cols.get(i), m_Cols.get(i));
		
		result += String.format(m_szgetEmbedObjs2, this.refTable, this.baseTable, this.objectRef, this.fKey, sObjId);
		return result;
	}
}