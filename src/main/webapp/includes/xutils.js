/**
 * XML ABSTACTION LAYER
 *
 * Copyright � 2004-2009 Kevin Jacovelli
 * All Rights Reserved
 */

 // XNode: Abstraction of XML node object
function XNode( node )
{
	this.m_node = node;						// XML Node object
	this.isNull = function()
	{
		if( this.m_node == null || typeof(this.m_node) == "undefined" ) return true;
		else return false;
	}
	this.nodeValue = function(){
		var xmlTag = this.m_node;
		if(xmlTag.firstChild.textContent && xmlTag.normalize) {
			xmlTag.normalize(xmlTag.firstChild);
			content=xmlTag.firstChild.textContent;
		} else if(xmlTag.firstChild.nodeValue) {
			content=xmlTag.firstChild.nodeValue;
		} else {
			content=null;
		}
		return content;
	}
	this.getAttribute = function( name )
	{
		try
		{
			var attr = this.m_node.attributes.getNamedItem( name );
			if( attr == null ) return "";
			else return attr.nodeValue;
		}
		catch(e)
		{
			return "";
		}
	}
	this.getText = function()
	{
		//if( window.ActiveXObject )	// IE
		//{
		//	return this.m_node.text;
		//}
		//else
		//{
			if( this.m_node != null && this.m_node.firstChild != null )
				return this.nodeValue();
			else
				return "";
		//}
	}
	this.getTagName = function()
	{
		return this.m_node.tagName;
	}
	this.getChildren = function()
	{
		return new XNodeList( this.m_node.childNodes );
	}
	this.selectNode = function( query )
	{
		return new XNode( this.m_node.selectNodes( query )[0] );
	}
	this.selectNodeList = function( query )
	{
		return new XNodeList( this.m_node.selectNodes( query ) );
	}
}

 // XNodeList: Abstraction of XML node object list
function XNodeList( nodelist )
{
	this.m_nodelist = nodelist;				// XML NodeList object
	this.isNull = function()
	{
		if( this.m_nodelist == null || typeof(this.m_nodelist) == "undefined" ) return true;
		else return false;
	}
	this.getLength = function(){ return this.m_nodelist.length; }
	this.getNode = function( index )
	{
		return new XNode( this.m_nodelist[index] );
	}
}

 // XDocument: Abstraction of XML DOM
function XDocument()
{
	this.m_xmldoc = null;					// XML document (DOM) object
	this.isNull = function()
	{
		if( this.m_xmldoc == null || typeof(this.m_xmldoc) == "undefined" ) return true;
		else return false;
	}
	this.getDocument = function()
	{
		return this.m_xmldoc;
	}
	this.attach = function( xmldoc )
	{
		//this.m_xmldoc = xmldoc;
		var xmlstr = xmldoc.xml ? xmldoc.xml : (new XMLSerializer()).serializeToString(xmldoc);
		this.create(xmlstr);
	}
	this.create = function( xmldoc )
	{
		if( window.ActiveXObject !== undefined )	// IE
		{
			try
			{
				this.m_xmldoc = new ActiveXObject( "msxml2.DOMDocument" );
				this.m_xmldoc.async = false;
				this.m_xmldoc.loadXML( xmldoc );
			}
			catch(e)
			{
				return false;
			}
		}
		else // Firefox
		{
			var domParser = new DOMParser();
			this.m_xmldoc = domParser.parseFromString(xmldoc,"application/xml");
		}
	}
	this.getNode = function( query )
	{
		return new XNode( this.m_xmldoc.getElementsByTagName( query )[0] );
	}
	this.getNodeList = function( query )
	{
		return new XNodeList( this.m_xmldoc.getElementsByTagName( query ) );
	}
	this.selectNode = function( query )
	{
		return new XNode( this.m_xmldoc.selectNodes( query )[0] );
	}
	this.selectNodeList = function( query )
	{
		return new XNodeList( this.m_xmldoc.selectNodes( query ) );
	}
}


//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//// XML PROTOTYPES ///////////////////////
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

/*
Prefix-correcting evaluate statement from http://www.faqts.com/knowledge_base/view.phtml/aid/34022/fid/119
*/

if( document.implementation.hasFeature("XPath", "3.0") )
{
	Document.prototype.selectNodes = function(cXPathString, xNode)
	{
		if( !xNode )
		{
			xNode = this;
		}

		var defaultNS = this.defaultNS;

		var aItems = this.evaluate(cXPathString, xNode,
		{
			normalResolver:
				this.createNSResolver(this.documentElement),
				lookupNamespaceURI : function (prefix)
				{
					switch (prefix)
					{
					case "dflt":
						return defaultNS;
					default:
						return this.normalResolver.lookupNamespaceURI(prefix);
					}
				}
		},
		XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);

		var aResult = [];
		for( var i = 0; i < aItems.snapshotLength; i++)
		{
			aResult[i] =  aItems.snapshotItem(i);
		}
		return aResult;
	}

	Element.prototype.selectNodes = function(cXPathString)
	{
		if(this.ownerDocument.selectNodes)
		{
			return this.ownerDocument.selectNodes(cXPathString, this);
		}
		else
		{
			throw "For XML Elements Only";
		}
	}

	// set the SelectionNamespaces property the same for NN or IE:
	Document.prototype.setProperty = function(p,v)
	{
		if(p=="SelectionNamespaces" && v.indexOf("xmlns:dflt")==0)
		{
			this.defaultNS = v.replace(/^.*=\'(.+)\'/,"$1");
		}
	}

	Document.prototype.defaultNS;
}
