///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

/*<Imports>*/
// Imported java classes
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.xml.stream.*;
import java.net.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.RandomGUID;

/*</Imports>*/
public class QueryMenuItems extends HttpServlet
{
	ObjectManager m_objectBean = null;
	//MenuOrderBean m_menuOrderBean = null;
/*
	String XmlEncodeString( String CharData )
	{
		String retStr = new String("");
		if( CharData != null )
		{
			retStr = CharData;
			retStr = retStr.replaceAll( "&", "&amp;" );
			retStr = retStr.replaceAll( "<", "&lt;" );
			retStr = retStr.replaceAll( ">", "&gt;" );
			//retStr = retStr.replaceAll( "]", "" );
		}
		return retStr;
	}
*/
    /**
     * Initializes a ServerMsg instance.
     *  Also sets its logging value from application init param.
     *  Also creates its logger if not done before.
     */
	public void init() throws ServletException
	{
		// Instantiate m_objectBean by loading an ObjectManager bean at application scope
		/////////////////////////////
		// Loading and accessing a bean from a servlet
		//////////////////////////////////////////////////////
		try
		{
			m_objectBean = SystemServlet.getObjectManager();
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( e.getMessage() );
			//e.printStackTrace();
		}
		///////////////////
		//////////////
	}

	/**
	 * Generate XML for specified category
	 *
	 * @param info				Object Manager Credentials
	 * @param category 			Category Id
	 * @param xmlStreamWriter 	XML Stream
	 */
	public void GenerateCategoryXML( Credentials info, boolean bQueryMenu, String sId,
									 MenuOrderBean menuOrderBean, XMLStreamWriter xmlStreamWriter )
	{
		try
		{
			ObjectQuery queryCat = new ObjectQuery( "CCMenuCategory" );
			queryCat.setSortBy("cat_index");		// TODO: Fix this - it should reference the property, not the column
			queryCat.setSortOrder("ASC");
			queryCat.addProperty("hidden", "N");
			if( bQueryMenu == true )
				queryCat.addProperty("menu", sId);
			else
				queryCat.addProperty("id", sId);
			QueryResponse qrCat = m_objectBean.Query( info, queryCat );
			RepositoryObjects oCats = qrCat.getObjects( queryCat.getClassName() );
			if( oCats.count() == 0 ) return;	// category doesn't exist

			// Write out menu id and name for root node
			RepositoryObject tmpCat = oCats.get(0);
			//xmlString.append(" id='" + tmpCat.getPropertyValue("menu") + "'");
			//xmlString.append(" name='" + tmpCat.getPropertyValue("menu.name") + "'");
			xmlStreamWriter.writeAttribute("id",tmpCat.getPropertyValue("menu"));
			xmlStreamWriter.writeAttribute("name",tmpCat.getPropertyValue("menu.name"));
			boolean showOptions = tmpCat.getPropertyValue_Boolean("menu.show_options");
			//xmlStreamWriter.writeAttribute("show_options",tmpCat.getPropertyValue("menu.show_options"));
			
			if( menuOrderBean.isWithinMenuOpertingHours(tmpCat.getPropertyValue("menu")) )
				xmlStreamWriter.writeAttribute("status", "open");
				//xmlString.append(" status='open'>");
			else
				xmlStreamWriter.writeAttribute("status", "closed");
				//xmlString.append(" status='closed'>");

			for( int iCat = 0; iCat < oCats.count(); iCat++ )
			{
				RepositoryObject oCat = oCats.get(iCat);
				xmlStreamWriter.writeStartElement("category");	// <category>
				xmlStreamWriter.writeAttribute("id",oCat.getPropertyValue("id"));
				xmlStreamWriter.writeAttribute("name",oCat.getPropertyValue("name"));
				
				//xmlStreamWriter.writeStartElement("hidden");		// <hidden>
				//xmlStreamWriter.writeCharacters(oCat.getPropertyValue("hidden"));
				//xmlStreamWriter.writeEndElement();					// </hidden>
				
				//xmlStreamWriter.writeStartElement("items");	// <items>
				//xmlString.append("<category id='" + oCat.getPropertyValue("id") + "' name='" + oCat.getPropertyValue("name") + "'><items>");

				ObjectQuery queryMenuItems = new ObjectQuery( "CCMenuItem" );
				queryMenuItems.setSortBy("item_index");	// TODO: Fix this - it should reference the property, not the column
				queryMenuItems.setSortOrder("ASC");
				queryMenuItems.addProperty("hidden", "N");
				queryMenuItems.addProperty("menucategory", oCat.getPropertyValue("id"));
				QueryResponse qrMenuItems = m_objectBean.Query( info, queryMenuItems );		
				RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryMenuItems.getClassName() );
				for( int i = 0; i < oMenuItems.count(); i++ )
				{
					RepositoryObject obj = oMenuItems.get( i );
					String sItemId = obj.getId();
					String sItemName = obj.getPropertyValue("name");
					String sItemDesc = obj.getPropertyValue("description");
					String sItemImage = obj.getPropertyValue("image");
					//String sItemHidden = obj.getPropertyValue("hidden");
					//String sItemIndex = obj.getPropertyValue("item_index");
					String sItemSize = obj.getPropertyValue("size_desc");
					String sItemPriceF = NumberFormat.getCurrencyInstance(Locale.US).format(obj.getPropertyValue_Real("price"));
					//String sItemPrice = sFormattedPrice;//obj.getPropertyValue("price");

					xmlStreamWriter.writeStartElement("item");
					xmlStreamWriter.writeAttribute("id",sItemId);
					xmlStreamWriter.writeStartElement("name");			// <name>
					xmlStreamWriter.writeCharacters(sItemName);
					xmlStreamWriter.writeEndElement();					// </name>
					xmlStreamWriter.writeStartElement("description");	// <description>
					xmlStreamWriter.writeCharacters(sItemDesc);
					xmlStreamWriter.writeEndElement();					// </description>
					xmlStreamWriter.writeStartElement("image");			// <image>
					xmlStreamWriter.writeCharacters(sItemImage);
					xmlStreamWriter.writeEndElement();					// </image>
					//xmlStreamWriter.writeStartElement("index");			// <index>
					//xmlStreamWriter.writeCharacters(sItemIndex);
					//xmlStreamWriter.writeEndElement();					// </index>
					xmlStreamWriter.writeStartElement("portions");		// <portions>
					xmlStreamWriter.writeStartElement("size");			// <size>
					xmlStreamWriter.writeAttribute("id",sItemId);
					xmlStreamWriter.writeAttribute("price",sItemPriceF);
					xmlStreamWriter.writeCharacters(sItemSize);
					xmlStreamWriter.writeEndElement();					// </size>

					//xmlString.append("<item id='" + sItemId + "'>");
					//xmlString.append("<name><![CDATA[" + sItemName + "]]></name>");
					//xmlString.append("<description><![CDATA[" + sItemDesc + "]]></description>");
					//xmlString.append("<image><![CDATA[" + sItemImage + "]]></image>");
					//xmlString.append("<portions>");
					//xmlString.append("<size id='" + sItemId + "' price='" + sItemPriceF + "'><![CDATA[" + sItemSize + "]]></size>");

					ObjectQuery queryItemSizes = new ObjectQuery( "CCMenuItemSize" );
					queryItemSizes.setSortBy("size_index");	// TODO: Fix this - it should reference the property, not the column
					queryItemSizes.setSortOrder("ASC");
					queryItemSizes.addProperty("menuitem",sItemId);
					QueryResponse qrItemSizes = m_objectBean.Query( info, queryItemSizes );
					RepositoryObjects oItemSizes = qrItemSizes.getObjects( queryItemSizes.getClassName() );
					if( oItemSizes.count() > 0 )
					{
						for( int ii = 0; ii < oItemSizes.count(); ii++ )
						{
							RepositoryObject item_size = oItemSizes.get( ii );
							String sSizeId = item_size.getId();
							String sSizeDesc = item_size.getPropertyValue("size_desc");
							String sSizePriceF = NumberFormat.getCurrencyInstance(Locale.US).format(item_size.getPropertyValue_Real("price"));
							//String sSizePrice = sFormattedPrice;//item_size.getPropertyValue("price");

							xmlStreamWriter.writeStartElement("size");			// <size>
							xmlStreamWriter.writeAttribute("id",sSizeId);
							xmlStreamWriter.writeAttribute("price",sSizePriceF);
							xmlStreamWriter.writeCharacters(sSizeDesc);
							xmlStreamWriter.writeEndElement();					// </size>
							//xmlString.append("<size id='" + sSizeId + "' price='" + sSizePriceF + "'><![CDATA[" + sSizeDesc + "]]></size>");
						}
					}
					xmlStreamWriter.writeEndElement();	// </portions>
					
					
					
					
					
					
					
					// O P T I O N S
					
					if( showOptions )
					{
						xmlStreamWriter.writeStartElement("options");		// <options>
						ObjectQuery queryItemOptions = new ObjectQuery( "CCMenuItemOption" );
						queryItemOptions.setSortBy("option_index");		// TODO: Fix this - it should reference the property, not the column
						queryItemOptions.setSortOrder("ASC");
						queryItemOptions.addProperty("menuitem",sItemId);
						QueryResponse qrItemOptions = m_objectBean.Query( info, queryItemOptions );
						RepositoryObjects oItemOptions = qrItemOptions.getObjects( queryItemOptions.getClassName() );
						if( oItemOptions.count() > 0 )
						{
							for( int ii = 0; ii < oItemOptions.count(); ii++ )
							{
								RepositoryObject item_option = oItemOptions.get( ii );
								String sOptionId = item_option.getId();
								String sOptionName = item_option.getPropertyValue("name");
								String sOptionType = item_option.getPropertyValue("type");
								String sOptionPriceF = NumberFormat.getNumberInstance().format(item_option.getPropertyValue_Real("price"));
								//String sOptionPriceF = NumberFormat.getCurrencyInstance(Locale.US).format(item_option.getPropertyValue_Real("price"));
								String sOptionData = item_option.getPropertyValue("data");
								xmlStreamWriter.writeStartElement("option");			// <option>
								xmlStreamWriter.writeAttribute("id",sOptionId);
								xmlStreamWriter.writeAttribute("name",sOptionName);
								xmlStreamWriter.writeAttribute("type",sOptionType);
								xmlStreamWriter.writeAttribute("price",sOptionPriceF);
								xmlStreamWriter.writeCharacters(sOptionData);
								xmlStreamWriter.writeEndElement();					// </option>
							}
						}
						xmlStreamWriter.writeEndElement();	// </options>
					}
					
					
					
					
					
					
					
					
					
					xmlStreamWriter.writeEndElement();	// </item>

					//xmlString.append("</portions></item>");

				}
				//xmlStreamWriter.writeEndElement();	// </items>
				xmlStreamWriter.writeEndElement();	// </category>
				//xmlString.append("</items></category>");
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException
	{
		// Load menu order bean for this session
		MenuOrderBean menuOrderBean = null;
		HttpSession beanSessionContext = request.getSession(); // Session Level Scope
		synchronized (beanSessionContext)
		{
			menuOrderBean = (MenuOrderBean) beanSessionContext.getAttribute("menuOrderBean");
			if( menuOrderBean == null )
			{
				//PrintWriter out = response.getWriter();
				//out.write( "<query-results><error><code>1</code><msg>Unable to load menu order bean</msg></error></query-results>" );
				//return;
				try
				{
					menuOrderBean = (MenuOrderBean) java.beans.Beans.instantiate(this.getClass().getClassLoader(), "com.genesys.webmenus.MenuOrderBean");
					//menuOrderBean.setObjectManager(m_objectBean);
				}
				catch (Exception exc)
				{
					throw new ServletException("Cannot create bean of class com.genesys.session.MenuOrderBean", exc);
				}
				beanSessionContext.setAttribute("menuOrderBean", menuOrderBean);
			}
		}

		// Build XML response
		//StringWriter stringWriter = new StringWriter();
		ByteArrayOutputStream outXML = new ByteArrayOutputStream();
		try
		{
			//String sMenu = request.getParameter("menu");
			
			XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
			//XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
			
			// Open XML Document
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
			xmlStreamWriter.writeStartElement("query-results");	// <query-results>
			//xmlStreamWriter.writeStartElement("menu");	// <menu>
			//StringBuffer xmlString = new StringBuffer();
			//xmlString.append("<?xml version='1.0' encoding='UTF-8'?><query-results><menu ");
			
			
			//if( menuOrderBean.isWithinMenuOpertingHours(sMenu) )
			//	xmlStreamWriter.writeAttribute("status", "open");
			//else
			//	xmlStreamWriter.writeAttribute("status", "closed");
			
			// Login to object manager
			//Credentials info = new Credentials();
			//if( m_objectBean.Login( "guest", "guest", info ) == true )
			if( menuOrderBean.verifyObjManCreds() )
			{
				xmlStreamWriter.writeStartElement("menu");	// <menu>
				String sMenu = request.getParameter("menu");
				if( sMenu != null && sMenu.length() > 0 )
				{
					GenerateCategoryXML(menuOrderBean.getCredentials(), true, sMenu, menuOrderBean, xmlStreamWriter);
				}
				else
				{
					// Only query for the selected category
					String sCategory = request.getParameter("cat");
					if( sCategory != null && sCategory.length() > 0 )
						GenerateCategoryXML(menuOrderBean.getCredentials(), false, sCategory, menuOrderBean, xmlStreamWriter);
				}
				xmlStreamWriter.writeEndElement();	// </menu>
				
				// Logout
				//m_objectBean.Logout(info);
			}
			
			//xmlStreamWriter.writeEndElement();	// </menu>
			xmlStreamWriter.writeEndElement();	// </query-results>
			//xmlString.append("</menu></query-results>");
			
			// Close XML Document
			xmlStreamWriter.writeEndDocument();
			xmlStreamWriter.flush();
			xmlStreamWriter.close();

			//response.addHeader("Content-Type", "text/xml; charset=utf-8");
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			//out.write(stringWriter.toString());
			out.write(outXML.toString("utf-8"));
			//out.write(xmlString.toString());
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace();
		}
		catch(FactoryConfigurationError e)
		{
			e.printStackTrace();
		}
	}

	public void doPost( HttpServletRequest request, HttpServletResponse response )
    					throws IOException, ServletException
	{
		PrintWriter out = response.getWriter();
		out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
	}

	//public void destroy()
	//{
	//}
}
