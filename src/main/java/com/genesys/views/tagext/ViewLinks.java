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
import java.util.Iterator;
import java.util.List;

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

public class ViewLinks extends BodyTagSupport
{
	private String m_sViewName;
	List<InterfaceCfg.View.Link> _links;
	Iterator<InterfaceCfg.View.Link> _iter_links;

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View viewNode = interfaceCfg.getView(m_sViewName);
			if(viewNode!=null)
			{
				_links = viewNode.getLinks();
				_iter_links = _links.iterator();
				if( _iter_links.hasNext() )
				{
					if( updateVariables( _iter_links.next() ) == true )
					{
						return EVAL_BODY_INCLUDE;
					}
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
			if( _iter_links.hasNext() )
			{
				if( updateVariables( _iter_links.next() ) == true )
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
	
	private String getSafeString(String str, String defVal)
	{
		if( str != null ) return str;
		else return defVal;
	}

	private boolean updateVariables( InterfaceCfg.View.Link linkNode )
	{
		try
		{
			if( linkNode != null )
			{
				//setPageAttribute( fieldNode, "linkText", "text" );
				pageContext.setAttribute( "linkText", getSafeString(linkNode.getText(),"") );
				//setPageAttribute( fieldNode, "linkView", "view" );
				pageContext.setAttribute( "linkView", getSafeString(linkNode.getViewName(),"") );
				//setPageAttribute( fieldNode, "linkReference", "reference" );
				pageContext.setAttribute( "linkReference", getSafeString(linkNode.getReference(),"") );
				return true;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	public void setViewName( String a_sViewName )
	{
		m_sViewName = a_sViewName;
	}
}
