///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

//import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.Date;

//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;
import com.genesys.util.xml.XMLDocument;
import com.genesys.util.xml.XMLNode;
import com.genesys.util.xml.XMLNodeList;
import com.genesys.util.xsl.XSLParser;
import com.genesys.util.email.Outbound;
import com.genesys.views.InterfaceCfg;
import com.mysql.cj.xdevapi.JsonArray;
import com.genesys.util.xml.XMLStreamHelper;

class OrderRenderer
{
	//private String m_orderXML = null;
	private ObjectManager 		m_objectBean = null;
	//private Document 			m_interfaceCFG = null;
	//private String 				m_orderId = null;
	//private String				m_RoleId = null;
	private StringWriter 		m_outText;
	private StringWriter 		m_outHtml;
	private RepositoryObject	m_oOrder = null;
	private Credentials 		m_info = null;
	private String				m_link = null;
	
//	public OrderRenderer(String orderId, String roleId)
//	{
//		this(orderId, roleId, null, null);
//	}
	public OrderRenderer(RepositoryObject orderItem, Credentials info, String link)
	{
		//m_objectBean = objectBean;
		m_objectBean = SystemServlet.getObjectManager();
		//m_interfaceCFG = interfaceCFG;
//		m_orderId = new String(orderId);
//		m_RoleId = roleId;
		m_outText = new StringWriter();
		m_outHtml = new StringWriter();
		
//		if( orderItem != null )
//		{
		m_oOrder = orderItem;
		m_info = info;
		m_link = link;
//		}
//		else
//		{
//		try
//		{
//			if( m_objectBean.SystemLogin("guest", m_info ) == true )
//			{
//				// Sync up roles with location
//				m_info.m_RoleId = m_RoleId;
//				
//				// Query order object
//				ObjectQuery queryOrder = new ObjectQuery( "CCMenuOrder" );
//				queryOrder.addProperty("id", m_orderId);
//				QueryResponse qrOrder = m_objectBean.Query( m_info, queryOrder );
//				RepositoryObjects oOrders = qrOrder.getObjects( queryOrder.getClassName());
//				if( oOrders.count() == 0 )
//				{
//					SystemServlet.g_logger.error( "Id not found while processing order [" + m_orderId + "]" );
//					return;
//				}
//				m_oOrder = oOrders.get(0);
//			}
//		}
//		catch(Exception e)
//		{
//			SystemServlet.g_logger.error( "Exception caught trying to post new order - " + e.getMessage() );
//		}
//		}
	}
	
	public String getOrderText()
	{
		return m_outText.toString();
	}
	
	public String getOrderHTML()
	{
		return m_outHtml.toString();
	}
	
//	public RepositoryObject getOrder()
//	{
//		return m_oOrder;
//	}
	
	public String getCurrencyString(double price)
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(price);
	}

	public String getCurrencyString(String price)
	{
		BigDecimal bdTaxRate = new BigDecimal(price);
		return getCurrencyString(bdTaxRate.doubleValue());
	}
	
//	private void addTextNode(XMLStreamWriter xmlStreamWriter, String tagName, String textVal)
//	throws XMLStreamException
//	{
//		xmlStreamWriter.writeStartElement(tagName);		// <textVal>
//		xmlStreamWriter.writeCharacters(textVal);
//		xmlStreamWriter.writeEndElement();				// </textVal>
//	}
	
	//private String generateCurrencyStr(double price)
	//{
	//	NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
	//	return n.format(price);
	//}

	private void insertPatron(XMLStreamWriter xmlStreamWriter, String emailAddr)
	{
		try
		{
			ObjectQuery queryPatron = new ObjectQuery( "CEPatron" );
			queryPatron.addProperty("email", emailAddr);
			QueryResponse qrPatron = m_objectBean.Query( m_info, queryPatron );
			RepositoryObjects oPatrons = qrPatron.getObjects( queryPatron.getClassName() );
			if( oPatrons.count() > 0 )
			{
				RepositoryObject oPatron = oPatrons.get(0);
				try
				{
					InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
					InterfaceCfg.View viewNode = interfaceCfg.getView("patrons");
					if(viewNode!=null)
					{
						List<InterfaceCfg.View.Input> _inputs = viewNode.getInputs();
						Iterator<InterfaceCfg.View.Input> _iter_inputs = _inputs.iterator();
						while( _iter_inputs.hasNext() )
						{
							InterfaceCfg.View.Input input = _iter_inputs.next();
							String field = input.getField();
							XMLStreamHelper.addTextNode(xmlStreamWriter, field, oPatron.getPropertyValue(field));
						}
					}
				}
				catch(XMLStreamException e){}
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
	}

	@SuppressWarnings("unchecked")
	public void renderOrder()
	{
		// Send email
		try
		{
				OutputStream outXML = new ByteArrayOutputStream();
				XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
				xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
				xmlStreamWriter.writeStartElement("transaction");	// <transaction>
				xmlStreamWriter.writeStartElement("order");	// <order>

				String orderId = m_oOrder.getPropertyValue("id");
				
				// Add order information to order node
				xmlStreamWriter.writeAttribute("id", orderId);
				xmlStreamWriter.writeAttribute("time", m_oOrder.getPropertyValue("order_time"));
				xmlStreamWriter.writeAttribute("link", m_link);
				
				xmlStreamWriter.writeStartElement("customer");	// <customer>
				
				
				insertPatron(xmlStreamWriter, m_oOrder.getPropertyValue("email"));

				xmlStreamWriter.writeEndElement();				// </customer>
				
				XMLStreamHelper.addTextNode(xmlStreamWriter,"subtotal", getCurrencyString(m_oOrder.getPropertyValue("subtotal")));
				XMLStreamHelper.addTextNode(xmlStreamWriter,"tax_rate", m_oOrder.getPropertyValue("tax_rate"));
				XMLStreamHelper.addTextNode(xmlStreamWriter,"tax", getCurrencyString(m_oOrder.getPropertyValue("tax")));
				XMLStreamHelper.addTextNode(xmlStreamWriter,"total", getCurrencyString(m_oOrder.getPropertyValue("total")));
				XMLStreamHelper.addTextNode(xmlStreamWriter,"delivery", m_oOrder.getPropertyValue("delivery"));
				XMLStreamHelper.addTextNode(xmlStreamWriter,"delivery_info", m_oOrder.getPropertyValue("delivery_info"));
				
				XMLStreamHelper.addTextNode(xmlStreamWriter,"location", m_oOrder.getPropertyValue("location.name"));
				xmlStreamWriter.writeStartElement("items");	// <items>
				
				// Query order items
				ObjectQuery queryOrderItem = new ObjectQuery( "CCMenuOrderItem" );
				queryOrderItem.addProperty("menuorder", orderId);
				QueryResponse qrOrderItem = m_objectBean.Query( m_info, queryOrderItem );
				RepositoryObjects oOrderItems = qrOrderItem.getObjects( queryOrderItem.getClassName() );
				for( int i = 0; i < oOrderItems.count(); i++ )
				{
					RepositoryObject oOrderItem = oOrderItems.get(i);
					
					xmlStreamWriter.writeStartElement("item");	// <menu>
					XMLStreamHelper.addTextNode(xmlStreamWriter,"id", oOrderItem.getPropertyValue("id"));
					XMLStreamHelper.addTextNode(xmlStreamWriter,"name", oOrderItem.getPropertyValue("name"));
					XMLStreamHelper.addTextNode(xmlStreamWriter,"description", oOrderItem.getPropertyValue("description"));
					XMLStreamHelper.addTextNode(xmlStreamWriter,"special_instructions", oOrderItem.getPropertyValue("special_instructions"));
					//XMLStreamHelper.addTextNode(xmlStreamWriter,"options", oOrderItem.getPropertyValue("options"));

					xmlStreamWriter.writeStartElement("options");	// <options>
					String optionsJson = oOrderItem.getPropertyValue("options");
					if (optionsJson != null && !optionsJson.isBlank()) {
						ObjectMapper mapper = new ObjectMapper();
						try {
							JsonNode node = mapper.readTree(optionsJson);
							JsonNode options = node.get("options");
							if (options.isArray()) {
								for (JsonNode option : options) {
									xmlStreamWriter.writeStartElement("option");
									String optName = option.get("name").asText();
									XMLStreamHelper.addTextNode(xmlStreamWriter,"name", optName);
									xmlStreamWriter.writeStartElement("choices");	// <choices>
									JsonNode choices = option.get("selected_choices");
									if (choices.isArray()) {
										for (JsonNode choice : choices) {
											xmlStreamWriter.writeStartElement("choice");
											XMLStreamHelper.addTextNode(xmlStreamWriter,"name", choice.get("name").asText());
											if (choice.get("price").asDouble() > 0) {
												XMLStreamHelper.addTextNode(xmlStreamWriter,"price", getCurrencyString(choice.get("price").asText()));
											}
											xmlStreamWriter.writeEndElement();	// </choice>
										}
									}
									xmlStreamWriter.writeEndElement();	// </choices>
									xmlStreamWriter.writeEndElement();	// </option>
								}
							}
						} catch (Exception e){
							SystemServlet.g_logger.error( "MessagingException caught trying to read option json - " + e.getMessage() );
						}
					}
					xmlStreamWriter.writeEndElement();	// </options>

					XMLStreamHelper.addTextNode(xmlStreamWriter,"size", oOrderItem.getPropertyValue("size"));
					//String sFormattedPrice = NumberFormat.getCurrencyInstance(Locale.US).format(oOrderItem.getPropertyValue_Real("price"));
					String sFormattedPrice = getCurrencyString(oOrderItem.getPropertyValue_Real("price"));
					XMLStreamHelper.addTextNode(xmlStreamWriter,"price", sFormattedPrice);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"quantity", oOrderItem.getPropertyValue("quantity"));
					double item_total = oOrderItem.getPropertyValue_Real("price") * oOrderItem.getPropertyValue_Int("quantity");
					XMLStreamHelper.addTextNode(xmlStreamWriter,"itemtotal", getCurrencyString(item_total));
					xmlStreamWriter.writeEndElement();	// </item>
				}
				
				// Close out items collection
				xmlStreamWriter.writeEndElement();	// </items>
				xmlStreamWriter.writeEndElement();	// </order>
				xmlStreamWriter.writeEndElement();	// </transaction>
				
				// Close XML Document
				xmlStreamWriter.writeEndDocument();
				xmlStreamWriter.flush();
				xmlStreamWriter.close();

				// Run order XML through XSL transform
				XSLParser xslParser = new XSLParser();
				String rootPath = SystemServlet.getGenesysHome();

				// Generate text email body through XSL
				String xslTextUri = rootPath + "templates/webmenus/reciept_text.xsl";
				xslParser.transform( outXML.toString(), xslTextUri, m_outText );
				
				// Generate html email body through XSL
				String xslHtmlUri = rootPath + "templates/webmenus/reciept_html.xsl";
				xslParser.transform( outXML.toString(), xslHtmlUri, m_outHtml );
				
				// Exit successfully
				return;
//			}
		}
//		catch(MessagingException e)
//		{
//			SystemServlet.g_logger.error( "MessagingException caught trying to post new order - " + e.getMessage() );
//		}
		catch(Exception e)
		{
			SystemServlet.g_logger.error( "Exception caught trying to post new order - " + e.getMessage() );
		}
	}
};