
package com.genesys.util.xsl;

// Imported java classes
import java.io.*;
import java.util.Hashtable;
import java.util.Date;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.parsers.*;

//import javax.xml.transform.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

public class XSLParser
{
	public XSLParser()
	{
	}

	public void transform(String inXML, String inXSL, Writer outDoc)//, PrintWriter out)
			throws org.xml.sax.SAXException, Exception
	{
		try
		{
		// Use the static TransformerFactory.newInstance() method to instantiate 
		// a TransformerFactory. The javax.xml.transform.TransformerFactory 
		// system property setting determines the actual class to instantiate --
		// org.apache.xalan.transformer.TransformerImpl.
		  TransformerFactory tFactory = TransformerFactory.newInstance();
			
		  // Use the TransformerFactory to instantiate a Transformer that will work with  
		  // the stylesheet you specify. This method call also processes the stylesheet
		// into a compiled Templates object.
		  Transformer transformer = tFactory.newTransformer(new StreamSource(inXSL));

		  // Use the Transformer to apply the associated Templates object to an XML document
		  // (foo.xml) and write the output to a file (foo.out).
		  StringReader reader = new StringReader(inXML);

		  transformer.transform(new StreamSource(reader), new StreamResult(outDoc));
		}
		catch( Exception ex )
		{
			outDoc.write( "<br><br><font color='red'>" );
			outDoc.write( ex.getLocalizedMessage() );
			outDoc.write( "</font>" );
		}
	}

	/* Code below here is based on Apache Software Foundation samples
	(see notice at file beginning)
	*/

    /** Normalizes the given string, replacing chars with entities.
     * (less than, greater than, ampersand, double quote, return and linefeed).
     * Note: this replaces null string with empty string.
     *
     * @param s String
     * @return normalized string (not null)
     */
    protected static String normalize(String s) {
        StringBuffer str = new StringBuffer();
        	str.append("");
			int len = (s != null) ? s.length() : 0;
			for (int i = 0; i < len; i++) {
				char ch = s.charAt(i);
				switch (ch) {
					case '<': {
						str.append("&lt;");
						break;
					}
					case '>': {
						str.append("&gt;");
						break;
					}
					case '&': {
						str.append("&amp;");
						break;
					}
					case '"': {
						str.append("&quot;");
						break;
					}
					case '\r':
					case '\n': {
						str.append("&#");
						str.append(Integer.toString(ch));
						str.append(';');
						break;
					}
					default: {
						str.append(ch);
					}
				}
			}
        return str.toString();
    }
}
