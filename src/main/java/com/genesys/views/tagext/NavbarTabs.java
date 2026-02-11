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

public class NavbarTabs extends BodyTagSupport
{
	String m_sAccess, m_sPortal;
	List<InterfaceCfg.Portal.Tab> _tabs = null;
	Iterator<InterfaceCfg.Portal.Tab> _iter_tabs = null;

	public NavbarTabs()
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
			_tabs = _portal.getTabs();
			_iter_tabs = _tabs.iterator();
			if( _iter_tabs.hasNext() )
			{
				if( updateVariables( _iter_tabs.next() ) == true )
				{
					return EVAL_BODY_INCLUDE;
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
			if( _iter_tabs.hasNext() )
			{
				if( updateVariables( _iter_tabs.next() ) == true )
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

	private boolean updateVariables( InterfaceCfg.Portal.Tab tabNode )
	{
		try
		{
			if( tabNode != null )
			{
				//setPageAttribute( node, "tabText", "text" );
				pageContext.setAttribute( "tabText", getSafeString(tabNode.getText(),"") );
				
				
				
				

				// Peak at the childern nodes
				boolean bTabEmpty = true;
				
				List<InterfaceCfg.Portal.Tab.Shortcut> _shortcuts = tabNode.getShortcuts();
				Iterator<InterfaceCfg.Portal.Tab.Shortcut> _iter_shortcuts = _shortcuts.iterator();
				while( _iter_shortcuts.hasNext() )
				{
					InterfaceCfg.Portal.Tab.Shortcut _shortcut = _iter_shortcuts.next();
					if( getAccessRights( m_sAccess, _shortcut.getViewName() ) > 0 )
					{
						bTabEmpty = false;
						break;
					}
				}

				if( bTabEmpty == true )
					pageContext.setAttribute( "tabEmpty", "true" );
				else
					pageContext.setAttribute( "tabEmpty", "false" );

				return true;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	int getAccessRights( String AccessRights, String ViewName )
	{
		boolean bForceDeny = false;
		int iAccRights = 0, iDefRights = 0;
		String sAccessStr = AccessRights;
		String sAccess;
		//String sAccessArray[] = sAccessStr.split( "|" );
		int iStartIdx = 0;
		int iIdx = sAccessStr.indexOf( "|", iStartIdx );
		while( iIdx >= 0 )
		{
			sAccess = sAccessStr.substring( iStartIdx, iIdx );
			iStartIdx = iIdx + 1;
		/////////////////
		//for( int i = 0; i < sAccessArray.length; i++ )
		if( sAccess.length() > 0 )
		{
			//sAccess = sAccessArray[i];
			int iIndex = sAccess.indexOf( ":" );
			if( iIndex >= 0 )
			{
				String sView = sAccess.substring( 0, iIndex );
				String sRights = sAccess.substring( iIndex + 1 );
				if( sView.equalsIgnoreCase( ViewName ) == true )
				{
					if( sRights.equalsIgnoreCase( "read/write" ) == true )
					{
						iAccRights = 2;
					}
					else if( sRights.equalsIgnoreCase( "read only" ) == true )
					{
						iAccRights = 1;
					}
					else if( sRights.equalsIgnoreCase( "denied" ) == true )
					{
						bForceDeny = true;
					}
				}
				else if( sView.equalsIgnoreCase( "(default)" ) == true )
				{
					if( sRights.equalsIgnoreCase( "read/write" ) == true )
					{
						iDefRights = 2;
					}
					else if( sRights.equalsIgnoreCase( "read only" ) == true )
					{
						iDefRights = 1;
					}
					//else if( sRights.equalsIgnoreCase( "denied" ) == true )
					//{
					//	iDefRights = 0;
					//}
				}
			}
		}
		/////////////////////
		////////////////
			iIdx = sAccessStr.indexOf( "|", iStartIdx );
		}
		if( iAccRights == 0 && bForceDeny == false ) iAccRights = iDefRights;
		return iAccRights;
	}

	public void setAccess( String a_sAccess )
	{
		m_sAccess = a_sAccess;
	}

	public void setPortal( String a_sPortal )
	{
		m_sPortal = a_sPortal;
	}
}
