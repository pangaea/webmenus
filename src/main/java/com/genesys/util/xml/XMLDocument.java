package com.genesys.util.xml;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.StringReader;

import org.xml.sax.InputSource;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.*;
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

public class XMLDocument
{
	CachedXPathAPI m_cachedXPath = null;
	Document m_Document = null;

    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
    //protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Validation feature id (http://xml.org/sax/features/validation). */
    //protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
    //protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
    //protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    // default settings

    /** Default parser name (dom.wrappers.Xerces). */
    //protected static final String DEFAULT_PARSER_NAME = "dom.wrappers.Xerces";

    /** Default element name (*). */
    //protected static final String DEFAULT_ELEMENT_NAME = "*";

    /** Default namespaces support (true). */
    //protected static final boolean DEFAULT_NAMESPACES = true;

    /** Default validation support (false). */
    //protected static final boolean DEFAULT_VALIDATION = false;

    /** Default Schema validation support (false). */
    //protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

    /** Default Schema full checking support (false). */
    //protected static final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

	public XMLDocument()
	{
		m_cachedXPath = new CachedXPathAPI();
	}

	public XMLDocument( Document document )
	{
		m_Document = document;
		m_cachedXPath = new CachedXPathAPI();
	}
	
	public Document getDocument()
	{
		return m_Document;
	}

	public boolean isNull()
	{
		if( m_Document == null )
			return true;
		else
			return false;
	}

	public XMLNodeList getNodeList( String sXPath )
	{
		try
		{
			return new XMLNodeList( m_cachedXPath.selectNodeList( m_Document, sXPath ) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLDocument::getNodeList => " + e.getMessage() );
		}
		return new XMLNodeList(null);	// Always return an valid object
	}

    public XMLNode getSingleNode( String sXPath )
	{
		try
		{
			return new XMLNode( m_cachedXPath.selectSingleNode( m_Document, sXPath ) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLDocument::getSingleNode => " + e.getMessage() );
		}
		return new XMLNode(null);	// Always return an valid object
	}

    public String getElementValue( String elementName )
	{
		String retValue = "";

        // get elements that match
        NodeList elements = m_Document.getElementsByTagName( elementName );

        // is there anything to do?
        if( elements != null )
		{

			int elementCount = elements.getLength();
			//for( int i = 0; i < elementCount; i++ )
			if( elementCount > 0 )
			{
				Node element = (Node)elements.item( 0 );
				NodeList children = element.getChildNodes();
				for( int i = 0; children != null && i < children.getLength(); ++i )
				{
					Node node = (Node)children.item( i );
                    if( node != null && node.getNodeType() == Node.TEXT_NODE )
					{
                    	//NamedNodeMap attributes = element.getAttributes();
						retValue = node.getNodeValue();
						break;
					}
				}
				//if( attributes.getNamedItem( attributeName ) != null )
				//{
				//	print(out, element, attributes);
				//}
			}
		}
        //}
		return retValue;

    }

    public String selectElementValue( String sXPath )
	{
        // get elements that match
        XMLNode node = getSingleNode(sXPath);
        if( node != null ) return node.getValue();
		return "";
    }

	public boolean loadXML( String xmlDoc )
	{
		try
		{
			DOMParser parser = new DOMParser();
        	parser.parse( xmlDoc );
			m_Document = parser.getDocument();
			return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLDocument::loadXML => " + e.getMessage() );
			//e.printStackTrace();
		}
		return false;
    }
	
	public boolean loadXMLStream( String xmlStream )
	{
		try
		{
			DOMParser parser = new DOMParser();
        	parser.parse( new InputSource( new StringReader( xmlStream ) ) );
			m_Document = parser.getDocument();
			return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in XMLDocument::loadXMLStream => " + e.getMessage() );
			//e.printStackTrace();
		}
		return false;
    }
}
