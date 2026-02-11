package com.genesys.views.tagext;

import org.w3c.dom.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

public class NavbarButtons extends BodyTagSupport
{
	String m_sTab, m_sPortal;
	List<InterfaceCfg.Portal.Tab.Shortcut> _shortcuts = null;
	Iterator<InterfaceCfg.Portal.Tab.Shortcut> _iter_shortcuts = null;

	public NavbarButtons()
	{
		m_sPortal = new String("");
	}
	
	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.Portal _portal = null;
			if( m_sPortal.length() == 0 )
			{
				HashMap<String,InterfaceCfg.Portal> _portalMap = interfaceCfg.getPortals();
				_portal = _portalMap.values().iterator().next();
			}
			else
			{
				_portal = interfaceCfg.getPortal(m_sPortal);
			}
			List<InterfaceCfg.Portal.Tab> _tabs = _portal.getTabs();
			Iterator<InterfaceCfg.Portal.Tab> _iter_tabs = _tabs.iterator();
			while( _iter_tabs.hasNext() )
			{
				InterfaceCfg.Portal.Tab _tab = _iter_tabs.next();
				if( m_sTab.equalsIgnoreCase( _tab.getText() ) )
				{
					_shortcuts = _tab.getShortcuts();
					_iter_shortcuts = _shortcuts.iterator();
					if( updateVariables( _iter_shortcuts.next() ) == true )
					{
						return EVAL_BODY_INCLUDE;
					}
					break;
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in doStartTag" );
		}
		return SKIP_BODY;
	}

	public int doAfterBody() throws JspTagException
	{
		try
		{
			// Iterate to the next node
			if( _iter_shortcuts.hasNext() )
			{
				if( updateVariables( _iter_shortcuts.next() ) == true )
				{
					// There is another node left in the list - go again
					return EVAL_BODY_BUFFERED;
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in doAfterBody" );
		}
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException
	{
		return EVAL_PAGE;
	}

	private String getSafeString(String str, String defVal)
	{
		if( str != null ) return str;
		else return defVal;
	}

	//private boolean updateVariables( XMLNode shortcutNode )
	private boolean updateVariables( InterfaceCfg.Portal.Tab.Shortcut node )
	{
		try
		{
			if( node != null )
			{
				pageContext.setAttribute( "navText", getSafeString(node.getText(),"") );
				pageContext.setAttribute( "navImage", getSafeString(node.getImage(),"") );
				pageContext.setAttribute( "navView", getSafeString(node.getViewName(),"") );
				pageContext.setAttribute( "navFilter", getSafeString(node.getFilter(),"") );
				pageContext.setAttribute( "navDetails", getSafeString(node.getDetails(),"") );
				return true;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}


	public void setTab( String a_sTab )
	{
		m_sTab = a_sTab;
	}
	
	public void setPortal( String a_sPortal )
	{
		m_sPortal = a_sPortal;
	}
}
