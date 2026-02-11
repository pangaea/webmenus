package com.genesys.util.xml;

//import org.apache.xerces.parsers.DOMParser;
//import org.apache.xpath.XPath;
//import org.apache.xpath.XPathAPI;

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

public class XMLNodeList
{
	private NodeList m_nodeList;
	private int m_iCurIndex;

	public XMLNodeList( NodeList nodeList )
	{
		m_nodeList = nodeList;
		m_iCurIndex = 0;
	}

	public boolean isNull()
	{
		if( m_nodeList == null )
			return true;
		else
			return false;
	}

	public int getCount()
	{
		if( m_nodeList == null ) return 0;
		return m_nodeList.getLength();
	}

	public XMLNode getFirstNode()
	{
		if( m_nodeList == null ) return null;
		m_iCurIndex = 0;
		if( getCount() > 0 )
			return new XMLNode( (Node)m_nodeList.item( m_iCurIndex ) );
		else
			return null;
	}

	public XMLNode getNextNode()
	{
		if( m_nodeList == null ) return null;
		try
		{
			if( m_iCurIndex >= 0 &&
				m_iCurIndex < ( this.getCount() - 1 ) )
			{
				return new XMLNode( (Node)m_nodeList.item( ++m_iCurIndex ) );
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLNodeList::getNextNode" );
		}
		return null;
	}

	public XMLNode getNodeByIndex( int iIndex )
	{
		if( m_nodeList == null ) return null;
		if( iIndex < this.getCount() )
		{
			return new XMLNode( (Node)m_nodeList.item( iIndex ) );
		}
		return null;
	}
};
