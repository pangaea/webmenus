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

import java.util.*;
import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.ObjectProperty;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

//class FieldInfo extends Object
//{
//	public String m_Property;
	//public String m_ValueOf;
//}

public class ViewForm extends BodyTagSupport
{
	//private Document m_oDocument;
	//private XMLDocument m_xmlDocument;
	private String m_sViewName;
	//private int m_iCurIndex, m_iMaxIndex;
	//private NodeList m_inputNodes;
	//Map fieldMap;
	//XMLNodeList m_inputNodes;
	HashMap<String,InterfaceCfg.View.Field> _fieldMap = null;
	List<InterfaceCfg.View.Input> _inputs = null;
	Iterator<InterfaceCfg.View.Input> _iter_inputs = null;

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View viewNode = interfaceCfg.getView(m_sViewName);
			if(viewNode!=null)
			{
			    _fieldMap = viewNode.getFields();
				///////////////////////////////////////
				//////////////////////////////////////
				
				_inputs = viewNode.getInputs();
				_iter_inputs = _inputs.iterator();
				if( _iter_inputs.hasNext() )
				{
					if( updateVariables( _iter_inputs.next() ) == true )
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
			if( _iter_inputs.hasNext() )
			{
				if( updateVariables( _iter_inputs.next() ) == true )
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

	private boolean updateVariables( InterfaceCfg.View.Input inputNode )
	{
		try
		{
			if( inputNode != null )
			{

				pageContext.setAttribute( "inputField", getSafeString(inputNode.getField(),"") );
				InterfaceCfg.View.Field iField = (InterfaceCfg.View.Field)_fieldMap.get(inputNode.getField());
				pageContext.setAttribute( "inputProperty", getSafeString(iField.getProperty(),"") );
				pageContext.setAttribute( "inputType", getSafeString(inputNode.getType(),"") );
				pageContext.setAttribute( "inputText", getSafeString(inputNode.getText(),"") );
				pageContext.setAttribute( "inputDisplay", getSafeString(inputNode.getDisplay(),"") );
				pageContext.setAttribute( "inputConstraint", getSafeString(inputNode.getConstraint(),"") );
				pageContext.setAttribute( "inputLen", Integer.toString(inputNode.getLength()) );
				pageContext.setAttribute( "inputView", getSafeString(inputNode.getView(),"") );
				pageContext.setAttribute( "inputHeight", Integer.toString(inputNode.getHeight()) );
				pageContext.setAttribute( "inputWidth", Integer.toString(inputNode.getWidth()) );
				pageContext.setAttribute( "inputVisible", getSafeString(inputNode.getVisible(),"") );
				pageContext.setAttribute( "inputTarget", getSafeString(inputNode.getTarget(),"") );
				pageContext.setAttribute( "inputFilter", getSafeString(inputNode.getFilter(),"") );
				pageContext.setAttribute( "inputRequired", inputNode.getRequired() );
				pageContext.setAttribute( "inputDefaultVal", inputNode.getDefaultVal() );
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
