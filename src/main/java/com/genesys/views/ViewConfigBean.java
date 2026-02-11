///////////////////////////////////////
// Copyright (c) 2004-2012 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.views;
/*
import java.io.File;
import java.util.StringTokenizer;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
*/
import com.genesys.SystemServlet;


public class ViewConfigBean
{
	InterfaceCfg m_interfaceCfg = null;
	InterfaceCfg.View m_viewNode = null;
	private String m_sViewName;

	public ViewConfigBean()
	{
		m_interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
	}

	public String getAnonUserID()
	{
		return SystemServlet.getAnonymousUserid();
	}

	public String getAnonUserPass()
	{
		return SystemServlet.getAnonymousPassword();
	}

	public String getView()
	{
		return this.m_sViewName;
	}

	public void setView( String viewName )
	{
		this.m_sViewName = (viewName!=null) ? viewName : getDefaultView();
		m_viewNode = m_interfaceCfg.getView(m_sViewName);
	}

	public String getTitle()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getTitle();
	}

	public String getDescription()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getDescription();
	}

	public String getHelpIndex()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getHelpIndex();
	}

	public String getButtonText( String button )
	{
		return new String(button);
	}

	public String getTemplate()
	{

		return new String("");
	}

	public String getListSize()
	{
		if(m_viewNode==null) return new String("");
		try
		{
			return Integer.toString(m_viewNode.getListSize());
		}
		catch(Exception e){}
		return new String("");
	}

	public String getEventsScript()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getEventScript();
	}

	public String getEventNum_OnFormLoad()
	{
		if(m_viewNode==null) return new String("");
		return new String("");
	}
	
	public String getListExpandSize()
	{
		if(m_viewNode==null) return new String("");
		try
		{
			return Integer.toString(m_viewNode.getListExpandSize());
		}
		catch(Exception e){}
		return new String("");
	}
	
	public String getListRowHeight()
	{
		if(m_viewNode==null) return new String("");
		try
		{
			return Integer.toString(m_viewNode.getListRowHeight());
		}
		catch(Exception e){}
		return new String("");
	}

	public boolean getListVisible()
	{
		if(m_viewNode==null) return true;
		return m_viewNode.getListVisible();
	}

	public String getByRef()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getByRef();
	}

	public String getAccess()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getAccess();
	}

	public String getFormVisible()
	{
		if(m_viewNode==null) return new String("");
		return Boolean.toString(m_viewNode.getFormVisible());
	}
	
	public String getFormExternal()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getFormExternal();
	}
	
	public String getFormExternalLinkColumn()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getFormExternalLinkColumn();
	}

	public String getSelObjReadonlyField()
	{
		return new String("");
	}

	public String getSelObjReadonlyValue()
	{
		return new String("");
	}

	public String getCreateObjInitField()
	{
		return new String("");
	}

	public String getCreateObjInitValue()
	{
		return new String("");
	}

	public String getSortBy()
	{
		if(m_viewNode==null) return new String("");
		return m_viewNode.getSortBy();
	}

	public String getSortOrder()
	{
		if(m_viewNode==null) return new String("D");
		if( m_viewNode.getSortBy().equalsIgnoreCase( "desc" ) == true )
			return "D";
		else
			return "A";
	}

	public String getNavbarWidth()
	{
		//if(m_viewNode==null) return new String("");
		try
		{
			return Integer.toString(m_interfaceCfg.getPortals().values().iterator().next().getNavbarWidth());
		}
		catch(Exception e)
		{
			return "80";
		}
	}
	
	public String getDefaultView()
	{
		//if(m_viewNode==null) return new String("");
		try
		{
			return m_interfaceCfg.getPortals().values().iterator().next().getDefaultView();
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public String getWelcomePage()
	{
		//if(m_viewNode==null) return new String("");
		try
		{
			return m_interfaceCfg.getPortals().values().iterator().next().getWelcomePage();
		}
		catch(Exception e)
		{
			return "";
		}
	}

	public String getBanner()
	{
		return new String("");
	}
}
