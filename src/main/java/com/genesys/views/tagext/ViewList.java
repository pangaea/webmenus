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
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/*
class FieldName extends Object
{
	FieldName( String fieldName )
	{
		m_Name = fieldName;
	}
 	public boolean equals( Object obj )
	{
		if( !( obj instanceof FieldName ) )
		{
      		return false;
		}
   		else
		{
			FieldName fieldName = (FieldName) obj;
			if( this.m_Name.equalsIgnoreCase( fieldName.m_Name ) )
			{
				return true;
			}
			else
			{
    			return false;
			}
		}
	}
	public String m_Name;
}
*/
//class FieldInfo extends Object
//{
//	public String m_Property;
//	public String m_ValueOf;
//}
import com.genesys.SystemServlet;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

public class ViewList extends BodyTagSupport
{
	private String m_sViewName;
	
	HashMap<String,InterfaceCfg.View.Field> _fieldMap = null;
	List<InterfaceCfg.View.Column> _columns = null;
	Iterator<InterfaceCfg.View.Column> _iter_columns = null;

	public int doStartTag() throws JspTagException
	{
		try
		{
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View viewNode = interfaceCfg.getView(m_sViewName);
			if(viewNode!=null)
			{
			    _fieldMap = viewNode.getFields();
			    _columns = viewNode.getColumns();
			    _iter_columns = _columns.iterator();
				if( _iter_columns.hasNext() )
				{
					if( updateVariables( _iter_columns.next() ) == true )
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
			if( _iter_columns.hasNext() )
			{
				if( updateVariables( _iter_columns.next() ) == true )
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
		return EVAL_PAGE;
	}

	private String getSafeString(String str, String defVal)
	{
		if( str != null ) return str;
		else return defVal;
	}

	private boolean updateVariables( InterfaceCfg.View.Column node )
	{
		try
		{
			if( node != null )
			{
				pageContext.setAttribute( "itemField", getSafeString(node.getFieldName(),"") );
				pageContext.setAttribute( "itemEnum", getSafeString(node.getEnum(),"") );
				pageContext.setAttribute( "itemTitle", getSafeString(node.getText(),"") );
				pageContext.setAttribute( "itemWidth", Integer.toString(node.getWidth()) );
				pageContext.setAttribute( "itemView", getSafeString(node.getViewName(),"") );
				pageContext.setAttribute( "itemFKey", getSafeString(node.getFKey(),"") );
				InterfaceCfg.View.Field iField = (InterfaceCfg.View.Field)_fieldMap.get(node.getFieldName());
				pageContext.setAttribute( "itemProperty", getSafeString(iField.getProperty(),"") );
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
