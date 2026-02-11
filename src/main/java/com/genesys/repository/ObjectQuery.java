package com.genesys.repository;

public class ObjectQuery
{
	String 	m_sClass;
	long 	m_lStartIndex;
	long 	m_lCount;
	String 	m_sSortBy;
	String 	m_sSortByPrefix;
	String 	m_sSortOrder;
	String 	m_sRequestLevel;
	ObjectProperties m_Properties;
	boolean m_bCountOnly;
	
	public ObjectQuery( String sClass )
	{
		m_sClass = new String(sClass);
		m_lStartIndex = 0;
		m_lCount = 0;
		m_sSortBy = new String("");
		m_sSortByPrefix = new String("");
		m_sSortOrder = new String("");
		m_sRequestLevel = new String("0");
		m_Properties = new ObjectProperties();
		m_bCountOnly = false;
	}
	
	// Public
	public String getClassName(){ return m_sClass; };
	public void setStart( long lStartIndex ){ m_lStartIndex = lStartIndex; };
	public long getStart(){ return m_lStartIndex; };
	public void setCount( long lCount ){ m_lCount = lCount; };
	public long getCount(){ return m_lCount; };
	public void setSortBy( String sSortBy ){ m_sSortBy = sSortBy; };
	public String getSortBy(){ return m_sSortBy; };
	public void setSortByPrefix( String sSortByPrefix ){ m_sSortByPrefix = sSortByPrefix; };
	public String getSortByPrefix(){ return m_sSortByPrefix; };
	public void setSortOrder( String sSortOrder ){ m_sSortOrder = sSortOrder; };
	public String getSortOrder(){ return m_sSortOrder; };
	public void setSortParams(String sSortBy, String sSortOrder){ m_sSortBy = sSortBy; m_sSortOrder = sSortOrder; };
	public void setRequestLevel( String sRequestLevel ){ m_sRequestLevel = sRequestLevel; };
	public String getRequestLevel(){ return m_sRequestLevel; };
	public void setCountOnly( boolean bCountOnly ){ m_bCountOnly = bCountOnly; };
	public boolean getCountOnly(){ return m_bCountOnly; };
	public ObjectProperties getProperties(){ return m_Properties; };
	public void addProperty(String name, String value)
	{
		m_Properties.add(new ObjectProperty(name,value));
	};
}