<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.StringWriter"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.util.Vector"%>
<%@ page import="javax.xml.stream.XMLOutputFactory"%>
<%@ page import="javax.xml.stream.XMLStreamWriter"%>
<%@ page import="com.genesys.SystemServlet"%>
<%@ page import="com.genesys.util.xml.XMLStreamHelper"%>
<%@ page import="com.genesys.util.xsl.XSLParser"%>

<%
// G E N E R A T E   'WELCOME ABOARD'   E M A I L ///////////
OutputStream outXML = new ByteArrayOutputStream();
XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
xmlStreamWriter.writeStartElement("account");									// <account>
XMLStreamHelper.addTextNode(xmlStreamWriter,"username", "kjacovel");
XMLStreamHelper.addTextNode(xmlStreamWriter,"firstname", "Kevin");
XMLStreamHelper.addTextNode(xmlStreamWriter,"lastname", "Jacovelli");
XMLStreamHelper.addTextNode(xmlStreamWriter,"city", "My Sinai");
XMLStreamHelper.addTextNode(xmlStreamWriter,"state", "NY");
XMLStreamHelper.addTextNode(xmlStreamWriter,"emailaddr", "kevin@myemail.com");
XMLStreamHelper.addTextNode(xmlStreamWriter,"phonenum", "(123) 123-4567");
XMLStreamHelper.addTextNode(xmlStreamWriter,"type", "basic");
xmlStreamWriter.writeStartElement("restaurant");								// <restaurant>
XMLStreamHelper.addTextNode(xmlStreamWriter,"name", "Kevin's Coffee House");
XMLStreamHelper.addTextNode(xmlStreamWriter,"city", "Miller Place");
XMLStreamHelper.addTextNode(xmlStreamWriter,"state", "NY");
XMLStreamHelper.addTextNode(xmlStreamWriter,"email_addr", "kevin@coffeehouse.com");
XMLStreamHelper.addTextNode(xmlStreamWriter,"phone_num", "(123) 123-4567");
xmlStreamWriter.writeEndElement();												// </restaurant>
xmlStreamWriter.writeEndElement();												// </account>
xmlStreamWriter.writeEndDocument();
xmlStreamWriter.flush();
xmlStreamWriter.close();
String outXml = outXML.toString();

XSLParser xslParser = new XSLParser();
String rootPath = SystemServlet.getGenesysHome();
StringWriter outHtml = new StringWriter();
String xslHtmlUri = rootPath + "templates/webmenus/welcome_html.xsl";
xslParser.transform( outXml, xslHtmlUri, outHtml );
%>

<%=outHtml.toString()%>