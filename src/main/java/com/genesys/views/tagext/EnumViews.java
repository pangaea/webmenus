package com.genesys.views.tagext;

//import org.apache.xerces.parsers.DOMParser;
//import org.apache.xpath.XPath;
//import org.apache.xpath.XPathAPI;

//import org.w3c.dom.Attr;
import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

//import java.util.Map;
//import java.util.HashMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

//class FieldInfo extends Object
//{
//	public String m_Property;
//	public String m_ValueOf;
//}

public class EnumViews extends BodyTagSupport
{
	private String m_sEnumName;
	HashMap<String,InterfaceCfg.View> _viewMap = null;
	Iterator<InterfaceCfg.View> _iter_views = null;

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			_viewMap = interfaceCfg.getViews();
			_iter_views = _viewMap.values().iterator();
			if( _iter_views.hasNext() )
			{
				if( updateVariables( _iter_views.next() ) == true )
				{
					return EVAL_BODY_INCLUDE;
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in doStartTag" );
		}
		//catch( IOException e )
		//{
		//	throw new JspTagException( "something bad happened!" );
		//}

		return SKIP_BODY;
	}

	public int doAfterBody() throws JspTagException
	{
		try
		{
			// Iterate to the next node
			if( _iter_views.hasNext() )
			{
				if( updateVariables( _iter_views.next() ) == true )
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
		//catch( IOException e )
		//{
		///	throw new JspTagException( "something bad happened!" );
		//}
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException
	{
		/*try
		{
			pageContext.getOut().write( "<table><tr><td>this is...</td></tr><tr><td>a test!</td></tr></table>" );
		}
		catch( IOException e )
		{
			throw new JspTagException( "something bad happened!" );
		}*/
		return EVAL_PAGE;
	}

	public String getTitle( String viewName )
	{
		String retVal = "";
		InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
		InterfaceCfg.View viewNode = interfaceCfg.getView(viewName);
		if(viewNode!=null) retVal = viewNode.getTitle();
		return retVal;
	}

	private String getSafeString(String str, String defVal)
	{
		if( str != null ) return str;
		else return defVal;
	}

	//private boolean updateVariables( XMLNode enumNode )
	private boolean updateVariables( InterfaceCfg.View viewNode )
	{
		try
		{
			if( viewNode != null )
			{
				pageContext.setAttribute( "viewName", getSafeString(viewNode.getName(),"") );
				pageContext.setAttribute( "viewTitle", getSafeString(viewNode.getTitle(),"") );
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
