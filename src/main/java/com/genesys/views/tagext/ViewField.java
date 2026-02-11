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

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
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

public class ViewField extends BodyTagSupport
{
	private String m_sViewName;
	XMLNodeList m_fieldNodes;
	HashMap<String, InterfaceCfg.View.Field> _fieldMap;
	Iterator<InterfaceCfg.View.Field>  _iter_fields;

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View viewNode = interfaceCfg.getView(m_sViewName);
			if(viewNode!=null)
			{
				_fieldMap = viewNode.getFields();
				_iter_fields = _fieldMap.values().iterator();
				if( _iter_fields.hasNext() )
				{
					if( updateVariables( _iter_fields.next() ) == true )
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
			if( _iter_fields.hasNext() )
			{
				if( updateVariables( _iter_fields.next() ) == true )
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
	
	private static String capitalizeString(String string) {
		  char[] chars = string.toLowerCase().toCharArray();
		  boolean found = false;
		  for (int i = 0; i < chars.length; i++) {
		    if (!found && Character.isLetter(chars[i])) {
		      chars[i] = Character.toUpperCase(chars[i]);
		      found = true;
		    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
		      found = false;
		    }
		  }
		  return String.valueOf(chars);
		}
	
	private boolean updateVariables( InterfaceCfg.View.Field fieldNode )
	{
		try
		{
			if( fieldNode != null )
			{
				//setPageAttribute( fieldNode, "inputName", "name" );
				String fieldDisplay = getSafeString(fieldNode.getName(),"");
				pageContext.setAttribute( "inputName", fieldDisplay );
				pageContext.setAttribute( "inputDisplay", capitalizeString(fieldDisplay.replaceAll("_", " ")) );
				//setPageAttribute( fieldNode, "inputProperty", "property" );
				pageContext.setAttribute( "inputProperty", getSafeString(fieldNode.getProperty(),"") );
				//setPageAttribute( fieldNode, "inputSearchable", "searchable" );
				pageContext.setAttribute( "inputSearchable", "" );
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
