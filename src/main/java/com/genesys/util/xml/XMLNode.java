package com.genesys.util.xml;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.genesys.SystemServlet;

//import org.xml.sax.SAXException;
//import org.xml.sax.SAXNotRecognizedException;
//import org.xml.sax.SAXNotSupportedException;
//import org.xml.sax.SAXParseException;

public class XMLNode
{
	Node m_node = null;
	CachedXPathAPI m_cachedXPath = null;

	public XMLNode( Node node )
	{
		m_node = node;
		m_cachedXPath = new CachedXPathAPI();
	}

	public boolean isNull()
	{
		if( m_node == null )
			return true;
		else
			return false;
	}

	public String getName()
	{
		return m_node.getNodeName();
	}

	public String getValue()
	{
		if( m_node != null )
		{
			if( m_node.getNodeType() == Node.TEXT_NODE )
			{
				return m_node.getNodeValue();
			}
			else if( m_node.getNodeType() == Node.CDATA_SECTION_NODE)
			{
				return m_node.getNodeValue();
			}
			else
			{
				NodeList children = m_node.getChildNodes();
				for( int i = 0; children != null && i < children.getLength(); ++i )
				{
					Node node = (Node)children.item( i );
					if( node != null && (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) )
					{
						return node.getNodeValue();
					}
				}
			}
		}
		return new String( "" );
	}

	public String getAttribute( String sName )
	{
		if( m_node != null )
		{
			//return m_node.getAttributes().getNamedItem( sName ).getNodeValue();
			NamedNodeMap attrMap = m_node.getAttributes();
			if( attrMap != null )
			{
				Node namedNode = attrMap.getNamedItem( sName );
				if( namedNode != null )
				{
					return namedNode.getNodeValue();
				}
			}
		}
		return new String( "" );
	}

	public boolean setAttribute( String sName, String sValue )
	{
		if( m_node != null )
		{
			//return m_node.getAttributes().getNamedItem( sName ).getNodeValue();
			NamedNodeMap attrMap = m_node.getAttributes();
			if( attrMap != null )
			{
				Node namedNode = attrMap.getNamedItem( sName );
				if( namedNode != null )
				{
					namedNode.setNodeValue(sValue);
					return true;
				}
				else
				{
					Document doc = m_node.getOwnerDocument();
					Attr prop = doc.createAttribute(sName);
					prop.setValue(sValue);    
			        attrMap.setNamedItem(prop);
				}
			}
		}
		return false;
	}

	public String getChildNodeValue(String tagName)
	{
		if( m_node != null )
		{
			XMLNode childNode = this.getSingleNode(tagName);
			if( childNode == null )
				return new String("");
			else
				return childNode.getValue();
		}
		return new String( "" );
	}
	
	public XMLNodeList getChildNodeList()
	{
		return new XMLNodeList( m_node.getChildNodes() );
	}

	//public XMLNodeList getChildNodeListByName( String sName )
	//{
	//	return new XMLNodeList( m_node.getElementsByTagName( sName ) );
	//}

	public XMLNodeList getNodeList( String sXPath )
	{
		try
		{
			return new XMLNodeList( m_cachedXPath.selectNodeList( m_node, sXPath ) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLNode::getNodeList" );
		}
		return new XMLNodeList(null);	// Always return an valid object
	}

    public XMLNode getSingleNode( String sXPath )
	{
		try
		{
			return new XMLNode( m_cachedXPath.selectSingleNode( m_node, sXPath ) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLNode::getSingleNode" );
		}
		return new XMLNode(null);	// Always return an valid object
	}
    
    public XMLNode getParentNode()
	{
		try
		{
			return new XMLNode( m_cachedXPath.selectSingleNode( m_node, ".." ) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLNode::getParentNode" );
		}
		return new XMLNode(null);	// Always return an valid object
	}
};
