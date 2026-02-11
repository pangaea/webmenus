package com.genesys.repository.sql;

import java.util.*;

import com.genesys.repository.sql.StatementBuilder.PropType;

public class SQLStatement
{
	/**
	 * Statement types
	 */
	public enum StmtType
	{
		INSERT,			// INSERT only
		SUBMIT,			// INSERT/UPDATE statements accordingly
		DELETE			// DELETE statement
	}

	private String m_sTable;
	public Vector m_vParams;
	public StmtType m_type;

	public SQLStatement( String table )
	{
		m_sTable = new String(table);
		m_vParams = new Vector();
		m_type = StmtType.SUBMIT;
	}
	public String getTable(){ return m_sTable; };
	public void addParam( SQLParam param )
	{
		m_vParams.add( param );
	}
	public String generateInsert()
	{
		String sQuery = new String("");
		String sCols = new String("");
		String sValues = new String("");
		for( int i = 0; i < m_vParams.size(); i++ )
		{
			SQLParam param = (SQLParam)m_vParams.get(i);
			if( sCols.length() > 0 ) sCols += ",";
			sCols += param.getName();
			if( sValues.length() > 0 ) sValues += ",";
			sValues += "?";//param.getValue();
		}
		sQuery += "INSERT INTO " + m_sTable + " (" + sCols + ") VALUES (" + sValues + ")";
		return sQuery;
	}
	public String generateUpdate( String key )
	{
		
		String sQuery = new String("UPDATE " + m_sTable + " SET ");
		String sWhere = new String("");
		boolean bExtraParams = false;
		for( int i = 0; i < m_vParams.size(); i++ )
		{
			SQLParam param = (SQLParam)m_vParams.get(i);
			String sName = param.getName();
			if( sName.equalsIgnoreCase(key) == true )
			{
				sWhere = key + "=?";// + param.getValue();
			}
			else if( sName.equalsIgnoreCase("owner") == false &&
					sName.equalsIgnoreCase("role") == false )
			{
				if( bExtraParams ) sQuery += ",";
				bExtraParams = true;
				sQuery += param.getName() + "=?";// + param.getValue();
			}
		}
		sQuery += " WHERE " + sWhere;
		return sQuery;
	}
	public String generateDelete()
	{
		String sQuery = new String("");
		if( m_vParams.size() > 0 )
		{
			sQuery += "DELETE FROM " + m_sTable + " WHERE ";
			for( int i = 0; i < m_vParams.size(); i++ )
			{
				SQLParam param = (SQLParam)m_vParams.get(i);
				if( i > 0 ) sQuery += " AND ";
				sQuery += param.getName() + "=?";// + param.getValue();
			}
		}
		return sQuery;
	}
}