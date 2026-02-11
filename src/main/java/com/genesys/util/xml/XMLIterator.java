package com.genesys.util.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLIterator
{
	XMLNodeList m_Nodes = null;
	XMLNode m_Node = null;
	public XMLIterator(XMLNodeList nodes)
	{
		m_Nodes = nodes;
	}
	public boolean each()
	{
		if(m_Nodes==null) return false;
		if(m_Node==null) m_Node = m_Nodes.getFirstNode();
		else m_Node = m_Nodes.getNextNode();
		return (m_Node!=null);
	}
	public XMLNode getNode()
	{
		return m_Node;
	}
}