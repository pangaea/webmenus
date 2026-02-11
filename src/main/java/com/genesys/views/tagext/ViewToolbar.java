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
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.*;

import com.genesys.SystemServlet;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

//class FieldInfo extends Object
//{
//	public String m_Property;
//	public String m_ValueOf;
//}

public class ViewToolbar extends BodyTagSupport
{
	private String m_sViewName;
	private String m_sButtonType;
	List<InterfaceCfg.View.ToolbarButton> _buttons = null;
	Iterator<InterfaceCfg.View.ToolbarButton> _iter_buttons = null;

	public int doStartTag() throws JspTagException
	{
		try
		{
			if(m_sButtonType.equalsIgnoreCase("list") == false &&
			   m_sButtonType.equalsIgnoreCase("form") == false ) return SKIP_BODY;
			
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View view = interfaceCfg.getView(m_sViewName);
			if(view!=null)
			{
				if(m_sButtonType.equalsIgnoreCase("form")){
					_buttons = view.getFormToolbarButtons();
				}else{
					_buttons = view.getToolbarButtons();
				}
				_iter_buttons = _buttons.iterator();
				if( _iter_buttons.hasNext() )
				{
					if( updateVariables( _iter_buttons.next() ) == true )
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
			
			if( _iter_buttons.hasNext() )
			{
				if( updateVariables( _iter_buttons.next() ) == true )
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

	private boolean updateVariables(InterfaceCfg.View.ToolbarButton toolbarBtn)
	{
		try
		{
			if( toolbarBtn != null )
			{
				//setPageAttribute( toolbarBtn, "buttonText", toolbarBtn.getText() );
				pageContext.setAttribute( "buttonText", getSafeString(toolbarBtn.getText(),"") );
				//setPageAttribute( toolbarBtn, "buttonEventNum", Integer.toString(toolbarBtn.getEventnum()) );
				pageContext.setAttribute( "buttonEventNum", Integer.toString(toolbarBtn.getEventnum()) );
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

	public void setButtonType( String a_sButtonType )
	{
		m_sButtonType = a_sButtonType;
	}
}
