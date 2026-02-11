package com.genesys.util;

import java.lang.Integer;

public class LongRef
{
	public long m_lng;
	
	public LongRef()
	{
		m_lng = 0;
	}
	public LongRef( long lng )
	{
		m_lng = lng;
	}
	public long getLong()
	{
		return m_lng;
	}
	public String getString()
	{
		return Long.toString(m_lng);
	}
};
