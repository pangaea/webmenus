package com.genesys.repository;

public class ObjectSubmit
{
	public String m_sClass;
	ObjectProperties m_Properties;
	//private String _overrideRole = null;
	
	public ObjectSubmit( String sClass )
	{
		m_sClass = new String(sClass);
		m_Properties = new ObjectProperties();
	}

	public String getClassName(){ return m_sClass; };
	//public String getOverrideRole(){ return this._overrideRole; }
	//public void setOverrideRole(String overrideRole){ this._overrideRole = overrideRole; }
	public ObjectProperties getProperties(){ return m_Properties; };
	public void addProperty(String name, String value)
	{
		m_Properties.add(new ObjectProperty(name,value));
	};
	public void addProperty(String name, int value)
	{
		m_Properties.add(new ObjectProperty(name,value));
	};
	public void addProperty(String name, double value)
	{
		m_Properties.add(new ObjectProperty(name,value));
	};
	public void addProperty(String name, boolean value)
	{
		if( value )
			m_Properties.add(new ObjectProperty(name,"Y"));
		else
			m_Properties.add(new ObjectProperty(name,"N"));
	};
}