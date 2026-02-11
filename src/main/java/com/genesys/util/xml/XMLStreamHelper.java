package com.genesys.util.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLStreamHelper
{
	public static void addTextNode(XMLStreamWriter xmlStreamWriter, String tagName, String textVal)
	throws XMLStreamException
	{
		xmlStreamWriter.writeStartElement(tagName);		// <textVal>
		xmlStreamWriter.writeCharacters(textVal);
		xmlStreamWriter.writeEndElement();				// </textVal>
	}
}