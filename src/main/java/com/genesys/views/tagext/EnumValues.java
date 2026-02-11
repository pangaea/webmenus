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
import com.genesys.views.InterfaceCfg.Enum.Value;

//class FieldInfo extends Object
//{
//	public String m_Property;
//	public String m_ValueOf;
//}

public class EnumValues extends BodyTagSupport
{
	private String m_sEnumName;
	List<InterfaceCfg.Enum.Value> _values = null;
	Iterator<InterfaceCfg.Enum.Value> _iter_values = null;
	

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.Enum _enum = interfaceCfg.getEnum(m_sEnumName);
			if(_enum!=null)
			{
				_values = _enum.getValues();
				_iter_values = _values.iterator();
				if( _iter_values.hasNext() )
				{
					if( updateVariables( _iter_values.next() ) == true )
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
			if( _iter_values.hasNext() )
			{
				if( updateVariables( _iter_values.next() ) == true )
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
	
	private boolean updateVariables( InterfaceCfg.Enum.Value valNode )
	{
		try
		{
			if( valNode != null )
			{
				pageContext.setAttribute( "enumText", getSafeString(valNode.getText(),"") );
				pageContext.setAttribute( "enumCode", getSafeString(valNode.getCode(),"") );
				pageContext.setAttribute( "enumType", "" );
				return true;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	public void setEnumName( String a_sEnumName )
	{
		m_sEnumName = a_sEnumName;
	}
}
