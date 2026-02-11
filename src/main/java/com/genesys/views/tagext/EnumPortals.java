package com.genesys.views.tagext;

import org.w3c.dom.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

public class EnumPortals extends BodyTagSupport
{
	HashMap<String,InterfaceCfg.Portal> _portalMap = null;
	Iterator<InterfaceCfg.Portal> _iter_portals = null;
	
	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			_portalMap = interfaceCfg.getPortals();
			_iter_portals = _portalMap.values().iterator();
			if( _iter_portals.hasNext() )
			{
				if( updateVariables( _iter_portals.next() ) == true )
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
			if( _iter_portals.hasNext() )
			{
				if( updateVariables( _iter_portals.next() ) == true )
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
	
	private boolean updateVariables( InterfaceCfg.Portal portalNode )
	{
		try
		{
			if( portalNode != null )
			{
				//setPageAttribute( portalNode, "portalName", "name" );
				pageContext.setAttribute( "portalName", getSafeString(portalNode.getName(),"") );
				return true;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}
}
