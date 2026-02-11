///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus.designer;

/*<Imports>*/
// Imported java classes
import java.io.*;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.xml.stream.*;
//import java.nio.charset.Charset;

import java.util.regex.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.*;
import java.io.InputStream;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.session.ClientSessionBean;
import com.genesys.util.RandomGUID;
import com.genesys.util.xml.*;

//import com.genesys.views.HttpServletRequest;
//import com.genesys.views.HttpServletResponse;
//import com.genesys.views.ServletException;
//import com.genesys.views.HttpSession;
import com.genesys.webmenus.MenuBuilder;
import com.genesys.views.ViewResponseWriter;

/*</Imports>*/
public class MenuDesigner extends HttpServlet
{
	ObjectManager m_objectBean = null;
	//private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
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
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */

	public void service( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException
	{
		// Extremely simple "REST" interface
		String resPath = request.getPathInfo();
		if( resPath == null )
		{
			Handle_Query( request, response );
		}
		else
		{
			if( resPath.equalsIgnoreCase("/ping") )
			{
				request.getSession();	// Keep the session alive
				
				PrintWriter out = response.getWriter();
				response.addHeader("Content-Type", "text/xml; charset=utf-8");
				out.write(new ViewResponseWriter("ping", 0, "PONG").serialize());
			}
			else if( resPath.equalsIgnoreCase("/query") )
			{
				Handle_Query( request, response );
			}
			else if( resPath.equalsIgnoreCase("/save") )
			{
				Handle_Submit( request, response );
			}
			else if( resPath.equalsIgnoreCase("/savetofile") )
			{
				Handle_Save( request, response );
			}
			else if( resPath.equalsIgnoreCase("/loadfromfile") )
			{
				Handle_Load( request, response );
			}
			else if( resPath.equalsIgnoreCase("/loadfromtemplate") )
			{
				Handle_LoadFromTemplate( request, response );
			}
			else
			{
				PrintWriter out = response.getWriter();
				out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
			}
		}
	}

	/**
	 * Generate XML for specified menu
	 *
	 * @param info				Object Manager Credentials
	 * @param menuId 			Menu Id
	 * @param xmlStreamWriter 	XML Stream
	 */
	public void GenerateCategoryXML( Credentials info, String menuId, XMLStreamWriter xmlStreamWriter )
	{
		try
		{
			ObjectQuery queryCat = new ObjectQuery( "CCMenuCategory" );
			queryCat.setSortBy("cat_index");		// TODO: Fix this - it should reference the property, not the column
			queryCat.setSortOrder("ASC");
			queryCat.addProperty("menu", menuId);
			QueryResponse qrCat = m_objectBean.Query( info, queryCat );
			RepositoryObjects oCats = qrCat.getObjects( queryCat.getClassName() );

			for( int iCat = 0; iCat < oCats.count(); iCat++ )
			{
				RepositoryObject oCat = oCats.get(iCat);
				xmlStreamWriter.writeStartElement("category");	// <category>
				xmlStreamWriter.writeAttribute("id",oCat.getPropertyValue("id"));
				xmlStreamWriter.writeAttribute("name",oCat.getPropertyValue("name"));
				xmlStreamWriter.writeAttribute("hidden",oCat.getPropertyValue("hidden"));
				
				//xmlStreamWriter.writeStartElement("hidden");		// <hidden>
				//xmlStreamWriter.writeCharacters(oCat.getPropertyValue("hidden"));
				//xmlStreamWriter.writeEndElement();					// </hidden>
				
				//xmlStreamWriter.writeStartElement("items");	// <items>

				ObjectQuery queryMenuItems = new ObjectQuery( "CCMenuItem" );
				queryMenuItems.setSortBy("item_index");	// TODO: Fix this - it should reference the property, not the column
				queryMenuItems.setSortOrder("ASC");
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
					String sItemHidden = obj.getPropertyValue("hidden");
					String sItemIndex = obj.getPropertyValue("item_index");
					String sItemSize = obj.getPropertyValue("size_desc");
					String sItemPriceF = NumberFormat.getNumberInstance().format(obj.getPropertyValue_Real("price"));
					//String sItemPriceF = NumberFormat.getCurrencyInstance(Locale.US).format(obj.getPropertyValue_Real("price"));

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
					xmlStreamWriter.writeStartElement("hidden");		// <hidden>
					xmlStreamWriter.writeCharacters(sItemHidden);
					xmlStreamWriter.writeEndElement();					// </hidden>
					xmlStreamWriter.writeStartElement("index");			// <index>
					xmlStreamWriter.writeCharacters(sItemIndex);
					xmlStreamWriter.writeEndElement();					// </index>
					
					
					// P O R T I O N S
					
					xmlStreamWriter.writeStartElement("portions");		// <portions>
					xmlStreamWriter.writeStartElement("size");			// <size>
					xmlStreamWriter.writeAttribute("id",sItemId);
					xmlStreamWriter.writeAttribute("price",sItemPriceF);
					xmlStreamWriter.writeCharacters(sItemSize);
					xmlStreamWriter.writeEndElement();					// </size>
					
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
							String sSizeIndex = obj.getPropertyValue("size_index");
							String sSizePriceF = NumberFormat.getNumberInstance().format(item_size.getPropertyValue_Real("price"));
							//String sSizePriceF = NumberFormat.getCurrencyInstance(Locale.US).format(item_size.getPropertyValue_Real("price"));
							xmlStreamWriter.writeStartElement("size");			// <size>
							xmlStreamWriter.writeAttribute("id",sSizeId);
							xmlStreamWriter.writeAttribute("price",sSizePriceF);
							xmlStreamWriter.writeAttribute("index",sSizeIndex);
							xmlStreamWriter.writeCharacters(sSizeDesc);
							xmlStreamWriter.writeEndElement();					// </size>
						}
					}
					xmlStreamWriter.writeEndElement();	// </portions>
					
					
					// O P T I O N S
					
					
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
					
					
					
					xmlStreamWriter.writeEndElement();	// </item>
				}
				//xmlStreamWriter.writeEndElement();	// </items>
				xmlStreamWriter.writeEndElement();	// </category>
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

	public void Handle_Query( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		//PrintWriter out = response.getWriter();
		
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
			xmlStreamWriter.writeStartElement("location");	// <location>
			//xmlStreamWriter.writeStartElement("menu");	// <menu>
			//StringBuffer xmlString = new StringBuffer();
			//xmlString.append("<?xml version='1.0' encoding='UTF-8'?><query-results><menu ");
			
			//if( menuOrderBean.isWithinMenuOpertingHours(sMenu) )
			//	xmlStreamWriter.writeAttribute("status", "open");
			//else
			//	xmlStreamWriter.writeAttribute("status", "closed");
			
			// Login to object manager
			HttpSession thisSession = request.getSession();
			if( thisSession.getAttribute( "info" ) == null )
			{
				xmlStreamWriter.writeStartElement("error");
				xmlStreamWriter.writeStartElement("code");
				xmlStreamWriter.writeCharacters("1");
				xmlStreamWriter.writeEndElement();
				xmlStreamWriter.writeEndElement();
			}
			else
			{
				Credentials info = (Credentials)thisSession.getAttribute( "info" );//new Credentials();
				//if( m_objectBean.Login( "admin", "admin", info ) == true )
				//if( menuOrderBean.verifyObjManCreds() )
				//{
				String sLocation = request.getParameter("loc");
				if( sLocation != null && sLocation.length() > 0 )
				{
					// ***** U S E D   F O R   T H E   M E N U   D E S I G N E R ***** //
					// ==================================================================
					//	- Remove filter on 'hidden' field
					
					ObjectQuery queryLocation = new ObjectQuery( "CELocation" );
					queryLocation.addProperty("id", sLocation);
					QueryResponse qrLocation = m_objectBean.Query( info, queryLocation );
					RepositoryObjects oLocations = qrLocation.getObjects( queryLocation.getClassName() );
					if( oLocations.count() == 0 )
					{
						xmlStreamWriter.writeStartElement("error");
						xmlStreamWriter.writeStartElement("code");
						xmlStreamWriter.writeCharacters("2");
						xmlStreamWriter.writeEndElement();
						xmlStreamWriter.writeEndElement();
						return;
					}
					RepositoryObject oLocation = oLocations.get(0);
					xmlStreamWriter.writeAttribute("id", sLocation);		// id=?
					xmlStreamWriter.writeAttribute("name", oLocation.getPropertyValue("name"));		// name=?
					xmlStreamWriter.writeAttribute("theme_id", oLocation.getPropertyValue("theme"));		// theme_id=?
					
					// Return all menus for specified location
					ObjectQuery queryMenu = new ObjectQuery( "CCMenu" );
					queryMenu.setSortBy("menu_index");		// TODO: Fix this - it should reference the property, not the column
					queryMenu.setSortOrder("ASC");
					//queryMenu.addProperty("hidden", "N");
					queryMenu.addProperty("location", sLocation);
					String single_menu = request.getParameter("single_menu");
					if( single_menu != null && single_menu.length() > 0 ){
						queryMenu.addProperty("id", single_menu);
						xmlStreamWriter.writeAttribute("single_menu", single_menu);
					}
						
					QueryResponse qrMenu = m_objectBean.Query( info, queryMenu );
					RepositoryObjects oMenus = qrMenu.getObjects( queryMenu.getClassName() );
					for( int i = 0; i < oMenus.count(); i++ )
					{
						RepositoryObject obj = oMenus.get( i );
						String sMenuId = obj.getId();
						
						// Write <menu> tag
						xmlStreamWriter.writeStartElement("menu");									// <menu
						xmlStreamWriter.writeAttribute("id", sMenuId);								// id=?
						xmlStreamWriter.writeAttribute("name", obj.getPropertyValue("name"));		// name=?
						xmlStreamWriter.writeAttribute("hidden", obj.getPropertyValue("hidden"));	// hidden=?>
						xmlStreamWriter.writeAttribute("show_options", obj.getPropertyValue("show_options"));	// hidden=?>
						xmlStreamWriter.writeAttribute("take_orders", obj.getPropertyValue("take_orders"));
						xmlStreamWriter.writeAttribute("schedule_id", obj.getPropertyValue("schedule"));
						
						// Write menu properties
						//xmlStreamWriter.writeStartElement("hidden");							// <hidden>
						//xmlStreamWriter.writeCharacters(obj.getPropertyValue("hidden"));
						//xmlStreamWriter.writeEndElement();										// </hidden>
						///////////////////////////////
						
						GenerateCategoryXML(info, sMenuId, xmlStreamWriter);
						
						xmlStreamWriter.writeEndElement();										// </menu>
					}
				}
					
					// Logout
				//	m_objectBean.Logout(info);
				//}
			}
			
			//xmlStreamWriter.writeEndElement();	// </menu>
			xmlStreamWriter.writeEndElement();	// </location>
			//xmlString.append("</menu></query-results>");
			
			// Close XML Document
			xmlStreamWriter.writeEndDocument();
			xmlStreamWriter.flush();
			xmlStreamWriter.close();

			//response.addHeader("Content-Type", "text/xml; charset=utf-8");
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			//response.setContentType("text/xml; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.write(outXML.toString("utf-8"));
			//out.write(xmlString.toString());
			//out.write(stringWriter.toString());
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace();
		}
		catch(FactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	
	/*
	 * 				=======================
	 * 				S A V E   P R O C E S S
	 * 				=======================
	 */
	
	
//	private String getChildNodeValue(XMLNode itemNode, String tagName)
//	{
//		XMLNode childNode = itemNode.getSingleNode(tagName);
//		if( childNode == null )
//			return new String("");
//		else
//			return childNode.getValue();
//	}
	private String getParentNodeId(XMLNode itemNode)
	{
		XMLNode parentNode = itemNode.getParentNode();
		if( parentNode == null )
			return new String("");
		else
			return parentNode.getAttribute("id");
	}
	
	/**
	 * Sync item changes
	 *
	 * @param info				Object Manager Credentials
	 * @param menuId 			Menu Id
	 * @param xmlStreamWriter 	XML Stream
	 */
	public void SyncOptions( Credentials info, String itemId, XMLDocument menuDoc )
	{
		try
		{
			ObjectQuery queryMenuOptions = new ObjectQuery( "CCMenuItemOption" );
			//queryMenuItems.setSortBy("item_index");	// TODO: Fix this - it should reference the property, not the column
			//queryMenuItems.setSortOrder("ASC");
			queryMenuOptions.addProperty("menuitem", itemId);
			QueryResponse qrMenuOptions = m_objectBean.Query( info, queryMenuOptions );		
			RepositoryObjects oMenuOptions = qrMenuOptions.getObjects( queryMenuOptions.getClassName() );
			for( int i = 0; i < oMenuOptions.count(); i++ )
			{
				RepositoryObject oMenuOption = oMenuOptions.get( i );
				String sOptionId = oMenuOption.getId();
				
				XMLNode xOption = menuDoc.getSingleNode("//menu/category/item/options/option[@id='" + sOptionId + "']");
				if( xOption.isNull() )
				{
					m_objectBean.Delete(info, "CCMenuItemOption", sOptionId);
				}
				else
				{
					xOption.setAttribute("processed", "true");
					
					// Check for changes
					String name = xOption.getAttribute("name");
					String prc = xOption.getAttribute("price");
					if(prc.length()==0) prc = "0";
					double price = Double.parseDouble(prc);
					String type = xOption.getAttribute("type");
					String data = xOption.getValue();
					boolean hidden = (xOption.getAttribute("hidden").equalsIgnoreCase("Y"))?true:false;
					
					// Compile regular expression
			        //Pattern pattern = Pattern.compile("/\n/g");
			        
			     // Replace all occurrences of pattern in input
			        //Matcher matcher = pattern.matcher(data);
			        //String data = matcher.replaceAll("\r\n");
			        
			        
					//if( Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", _email) == false )
					//	errors.add("email", new ActionMessage("personal.error.email.invalid"));

					int index = Integer.parseInt(xOption.getAttribute("index"));
					String menuitem = getParentNodeId(xOption);
					
					if( oMenuOption.getPropertyValue("name").compareTo(name) != 0 ||
						oMenuOption.getPropertyValue_Real("price") != price ||
						oMenuOption.getPropertyValue("type").compareTo(type) != 0 ||
						oMenuOption.getPropertyValue("data").compareTo(data) != 0 ||
						oMenuOption.getPropertyValue("menuitem").compareTo(menuitem) != 0 ||
						oMenuOption.getPropertyValue_Boolean("hidden") != hidden ||
						oMenuOption.getPropertyValue_Int("option_index") != index )
					{
						ObjectSubmit subObj = new ObjectSubmit("CCMenuItemOption");
						subObj.addProperty("name", name);
						subObj.addProperty("price", price);
						subObj.addProperty("type", type);
						subObj.addProperty("hidden", hidden);
						
						// Import option data body by first cleaning it up
						String adjData = new String("");
						//String optionData = optionNode.getValue();
						String results[] = data.trim().split("\n");
						for(int ii =0; ii < results.length; ii++)
						{
							String sOptionTxt = results[ii].trim();
							if( sOptionTxt.length() == 0 ) continue;
							if( adjData.length() > 0 ) adjData += "\n";
							adjData += sOptionTxt;
						}
						
						subObj.addProperty("data", adjData);
						subObj.addProperty("menucategory", menuitem);
						subObj.addProperty("option_index", index);
						m_objectBean.Update(info, sOptionId, subObj);
					}
					
					
					
//					String sItemName = obj.getPropertyValue("name");
//					String sItemDesc = obj.getPropertyValue("description");
//					String sItemImage = obj.getPropertyValue("image");
//					String sItemHidden = obj.getPropertyValue("hidden");
//					String sItemIndex = obj.getPropertyValue("item_index");
//					String sItemSize = obj.getPropertyValue("size_desc");
//					String sItemPriceF = NumberFormat.getNumberInstance().format(obj.getPropertyValue_Real("price"));
					//String sItemPriceF = NumberFormat.getCurrencyInstance(Locale.US).format(obj.getPropertyValue_Real("price"));
				}
				
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sync item changes
	 *
	 * @param info				Object Manager Credentials
	 * @param menuId 			Menu Id
	 * @param xmlStreamWriter 	XML Stream
	 */
	public void SyncItems( Credentials info, String catId, XMLDocument menuDoc )
	{
		try
		{
			ObjectQuery queryMenuItems = new ObjectQuery( "CCMenuItem" );
			//queryMenuItems.setSortBy("item_index");	// TODO: Fix this - it should reference the property, not the column
			//queryMenuItems.setSortOrder("ASC");
			queryMenuItems.addProperty("menucategory", catId);
			QueryResponse qrMenuItems = m_objectBean.Query( info, queryMenuItems );		
			RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryMenuItems.getClassName() );
			for( int i = 0; i < oMenuItems.count(); i++ )
			{
				RepositoryObject oMenuItem = oMenuItems.get( i );
				String sItemId = oMenuItem.getId();
				
				SyncOptions(info, sItemId, menuDoc);
				
				XMLNode xItem = menuDoc.getSingleNode("//menu/category/item[@id='" + sItemId + "']");
				if( xItem.isNull() )
				{
					m_objectBean.Delete(info, "CCMenuItem", sItemId);
				}
				else
				{
					xItem.setAttribute("processed", "true");
					
					// Check for changes
					String name = xItem.getChildNodeValue("name");
					String description = xItem.getChildNodeValue("description");
					String image = xItem.getChildNodeValue("image");
					//String hidden = xItem.getChildNodeValue("hidden");
					boolean hidden = (xItem.getAttribute("hidden").equalsIgnoreCase("Y"))?true:false;
					int index = Integer.parseInt(xItem.getAttribute("index"));
					String menucategory = getParentNodeId(xItem);
					double price = 0.00;
					String size = "";
					XMLNodeList xPortions = menuDoc.getNodeList("//menu/category/item[@id='" + sItemId + "']/portions/size");
					if( xPortions.getCount() > 0 )
					{
						String itemPrice = xPortions.getFirstNode().getAttribute("price");
						price = Double.parseDouble(itemPrice);
						size = xPortions.getFirstNode().getValue();
					}
					
					if( oMenuItem.getPropertyValue("name").compareTo(name) != 0 ||
						oMenuItem.getPropertyValue("description").compareTo(description) != 0 ||
						oMenuItem.getPropertyValue("image").compareTo(image) != 0 ||
						oMenuItem.getPropertyValue_Boolean("hidden") != hidden ||
						oMenuItem.getPropertyValue("menucategory").compareTo(menucategory) != 0 ||
						oMenuItem.getPropertyValue_Real("price") != price ||
						oMenuItem.getPropertyValue("size").compareTo(size) != 0 ||
						oMenuItem.getPropertyValue_Int("item_index") != index )
					{
						ObjectSubmit subObj = new ObjectSubmit("CCMenuItem");
						subObj.addProperty("name", name);
						subObj.addProperty("description", description);
						subObj.addProperty("image", image);
						subObj.addProperty("hidden", hidden);
						subObj.addProperty("menucategory", menucategory);
						subObj.addProperty("price", price);
						subObj.addProperty("size_desc", size);
						subObj.addProperty("item_index", index);
						m_objectBean.Update(info, sItemId, subObj);
					}
					
					// Delete all sizes of this item
					
					ObjectQuery queryPortionItems = new ObjectQuery("CCMenuItemSize");
					queryPortionItems.addProperty("menuitem", sItemId);
					QueryResponse qrPortionItems = m_objectBean.Query( info, queryPortionItems );		
					RepositoryObjects oPortionItems = qrPortionItems.getObjects( queryPortionItems.getClassName() );
					for( int iPortions = 0; iPortions < oPortionItems.count(); iPortions++ )
					{
						RepositoryObject oPortionItem = oPortionItems.get(iPortions);
						String sPortionId = oPortionItem.getId();
						if( sPortionId.compareTo(sItemId) != 0 )
							m_objectBean.Delete(info, "CCMenuItemSize", sPortionId);
					}
					
					for( int iSize = 1; iSize < xPortions.getCount(); iSize++ )
					{
						XMLNode xPortion = xPortions.getNodeByIndex(iSize);
						ObjectSubmit itemPortion = new ObjectSubmit("CCMenuItemSize");
						BigDecimal bdPrice = new BigDecimal(xPortion.getAttribute("price"));
						int size_index = Integer.parseInt(xPortion.getAttribute("index"));
						itemPortion.addProperty("price", bdPrice.doubleValue());
						itemPortion.addProperty("size_desc", xPortion.getValue());
						itemPortion.addProperty("size_index", size_index);
						itemPortion.addProperty("menuitem", sItemId);
						m_objectBean.Insert(info, itemPortion);
					}
				}
				
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sync category changes
	 *
	 * @param info				Object Manager Credentials
	 * @param menuId 			Menu Id
	 * @param xmlStreamWriter 	XML Stream
	 */
	public void SyncCategories( Credentials info, String menuId, XMLDocument menuDoc )
	{
		try
		{
			ObjectQuery queryCat = new ObjectQuery( "CCMenuCategory" );
			//queryCat.setSortBy("cat_index");		// TODO: Fix this - it should reference the property, not the column
			//queryCat.setSortOrder("ASC");
			queryCat.addProperty("menu", menuId);
			QueryResponse qrCat = m_objectBean.Query( info, queryCat );
			RepositoryObjects oCats = qrCat.getObjects( queryCat.getClassName() );

			for( int iCat = 0; iCat < oCats.count(); iCat++ )
			{
				RepositoryObject oCat = oCats.get(iCat);
				String sCatId = oCat.getId();
				
				SyncItems(info, sCatId, menuDoc);
				
				XMLNode xCat = menuDoc.getSingleNode("//menu/category[@id='" + sCatId + "']");
				if( xCat.isNull() )
				{
					m_objectBean.Delete(info, "CCMenuCategory", sCatId);
				}
				else
				{
					xCat.setAttribute("processed", "true");
					
					// Check for changes
					String name = xCat.getAttribute("name");
					int index = Integer.parseInt(xCat.getAttribute("index"));
					String menu = getParentNodeId(xCat);
					boolean hidden = (xCat.getAttribute("hidden").equalsIgnoreCase("Y"))?true:false;
					
					if( oCat.getPropertyValue("name").compareTo(name) != 0 ||
						oCat.getPropertyValue("menu").compareTo(menu) != 0 ||
						oCat.getPropertyValue_Int("index") != index ||
						oCat.getPropertyValue_Boolean("hidden") != hidden )
					{
						ObjectSubmit subObj = new ObjectSubmit("CCMenuCategory");
						subObj.addProperty("name", name);
						subObj.addProperty("menu", menu);
						subObj.addProperty("cat_index", index);
						subObj.addProperty("hidden", hidden);
						m_objectBean.Update(info, sCatId, subObj);
					}
				}
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void Handle_Submit( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		if (request == null) throw new IOException("Invalid request");
		PrintWriter out = response.getWriter();
		
		HttpSession thisSession = request.getSession();
		if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
		Credentials info = (Credentials)thisSession.getAttribute( "info" );

		try
		{
			//String sResponse = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			StringBuffer sb = new StringBuffer();
	        while((line = in.readLine()) != null)
	        {
	            sb.append(line);
	            sb.append('\n');
	        }
	        String menuXml = sb.toString();
	        XMLDocument menuDoc = new XMLDocument();
	        menuDoc.loadXMLStream(menuXml);

	        // *** Perform comparison *** //
	        XMLNode locNode = menuDoc.getSingleNode("//location");
	        
	        String locId = locNode.getAttribute("id");
	        if( locId == null || locId.length() == 0 ) throw new IOException("Location Id Missing");
	        
	        String themeId = locNode.getAttribute("theme_id");
	        if( themeId != null || themeId.length() > 0 ){
				ObjectSubmit subObj = new ObjectSubmit("CELocation");
				subObj.addProperty("theme", themeId);
				m_objectBean.Update(info, locId, subObj);
	        }
	        
			ObjectQuery queryMenu = new ObjectQuery( "CCMenu" );
			queryMenu.setSortBy("menu_index");		// TODO: Fix this - it should reference the property, not the column
			queryMenu.setSortOrder("ASC");
			
			boolean singleMenuEditMode = false;
			String singleMenuId = locNode.getAttribute("single_menu");
			if( singleMenuId == null || singleMenuId.length() == 0 ){
				queryMenu.addProperty("location", locId);
			}
			else{
				queryMenu.addProperty("id", singleMenuId);
				singleMenuEditMode = true;
			}

			RepositoryObjectIterator menusIter = new RepositoryObjectIterator(m_objectBean.Query(info, queryMenu));
			while(menusIter.each()){
				RepositoryObject oMenu = menusIter.getObj();
				
				String sMenuId = oMenu.getId();
				
				SyncCategories(info, sMenuId, menuDoc);
				
				XMLNode xMenu = menuDoc.getSingleNode("//menu[@id='" + sMenuId + "']");
				if( xMenu.isNull() )
				{
					// Delete menu from repository
					m_objectBean.Delete(info, "CCMenu", sMenuId);
				}
				else
				{
					xMenu.setAttribute("processed", "true");
					
					// Check for changes
					String name = xMenu.getAttribute("name");
					boolean hidden = (xMenu.getAttribute("hidden").equalsIgnoreCase("Y"))?true:false;
					boolean show_options = (xMenu.getAttribute("show_options").equalsIgnoreCase("Y"))?true:false;
					int take_orders = Integer.parseInt(xMenu.getAttribute("take_orders"));
					String schedule_id = xMenu.getAttribute("schedule_id");
					int index = Integer.parseInt(xMenu.getAttribute("index"));
					
					if( oMenu.getPropertyValue("name").compareTo(name) != 0 ||
						oMenu.getPropertyValue_Int("menu_index") != index ||
						oMenu.getPropertyValue_Boolean("hidden") != hidden ||
						oMenu.getPropertyValue_Boolean("show_options") != show_options ||
						oMenu.getPropertyValue_Int("take_orders") != take_orders ||
						oMenu.getPropertyValue("schedule") != schedule_id )
					{
						ObjectSubmit subObj = new ObjectSubmit("CCMenu");
						subObj.addProperty("name", name);
						if(!singleMenuEditMode)
							subObj.addProperty("menu_index", index);
						subObj.addProperty("hidden", hidden);
						subObj.addProperty("show_options", show_options);
						subObj.addProperty("take_orders", take_orders);
						subObj.addProperty("schedule", schedule_id);
						m_objectBean.Update(info, sMenuId, subObj);
					}
				}
			}
			
			
			
			/*
			 * 				==============================================
			 * 				HORIZONTAL SCAN FOR NEW OR NON-PROCESSED ITEMS
			 * 				==============================================
			 */
			MenuBuilder mb = new MenuBuilder();
			
			// Import new menus
			//XMLNodeList xMenus = menuDoc.getNodeList("//menu[@id='0']");
			XMLNodeList xMenus = menuDoc.getNodeList("//menu[not(@processed)]");
			XMLNode xMenu = xMenus.getFirstNode();
			while( xMenu != null )
			{
				// NOTE: I'm checking the 'processed' attribute here, even though the above
				// xPath should be filtering these nodes, because the query is returning all
				// nodes currently. When this issue is resolved remove the conditional below.
				String proc = xMenu.getAttribute("processed");
				if( proc.length() == 0 )
				{
					String newId = mb.importMenu(info, locId, xMenu, false);
					xMenu.setAttribute("id", newId);
				}
				xMenu = xMenus.getNextNode();
			}
			
			// Import new categories
			//XMLNodeList xCategories = menuDoc.getNodeList("//menu/category[@id='0']");
			XMLNodeList xCategories = menuDoc.getNodeList("//menu/category[not(@processed)]");
			XMLNode xCategory = xCategories.getFirstNode();
			while( xCategory != null )
			{
				// NOTE: I'm checking the 'processed' attribute here, even though the above
				// xPath should be filtering these nodes, because the query is returning all
				// nodes currently. When this issue is resolved remove the conditional below.
				String proc = xCategory.getAttribute("processed");
				if( proc.length() == 0 )
				{
					String menuId = getParentNodeId(xCategory);
					String newId = mb.importCategory(info, menuId, xCategory, false);
					xCategory.setAttribute("id", newId);
				}
				xCategory = xCategories.getNextNode();
			}
			
			// Import new items
			//XMLNodeList xItems = menuDoc.getNodeList("//menu/category/item[@id='0']");
			XMLNodeList xItems = menuDoc.getNodeList("//menu/category/item[not(@processed)]");
			XMLNode xItem = xItems.getFirstNode();
			while( xItem != null )
			{
				// NOTE: I'm checking the 'processed' attribute here, even though the above
				// xPath should be filtering these nodes, because the query is returning all
				// nodes currently. When this issue is resolved remove the conditional below.
				String proc = xItem.getAttribute("processed");
				if( proc.length() == 0 )
				{
					String catId = getParentNodeId(xItem);
					String newId = mb.importItem(info, catId, xItem, false);
					xItem.setAttribute("id", newId);
				}
				xItem = xItems.getNextNode();
			}
			///////////////////////////////////////
			
			// Import new options
			//XMLNodeList xOptions = menuDoc.getNodeList("//menu/category/item/options/option[@id='0']");
			XMLNodeList xOptions = menuDoc.getNodeList("//menu/category/item/options/option[not(@processed)]");
			XMLNode xOption = xOptions.getFirstNode();
			while( xOption != null )
			{
				// NOTE: I'm checking the 'processed' attribute here, even though the above
				// xPath should be filtering these nodes, because the query is returning all
				// nodes currently. When this issue is resolved remove the conditional below.
				String proc = xOption.getAttribute("processed");
				if( proc.length() == 0 )
				{
					XMLNode itemNode = xOption.getParentNode().getParentNode();
					if( itemNode != null )
					{
						String optId = itemNode.getAttribute("id");
						//String optId = getParentNodeId(xOption);
						String newId = mb.importOption(info, optId, xOption);
						xOption.setAttribute("id", newId);
					}
				}
				xOption = xOptions.getNextNode();
			}
			///////////////////////////////////////
			
			// Logout
			//m_objectBean.Logout(info);
			//}
	        ///////////////////////////////

	        // Write out "success" response
	        response.addHeader("Content-Type", "text/xml; charset=utf-8");
	        //out.write( "<response><code>0</code><msg>success</msg></response>" );
	        out.write(new ViewResponseWriter("submit", 0, "SUCCESS").serialize());
		}
		catch(AuthenticationException ex)
		{
			response.addHeader("Content-Type", "text/xml; charset=utf-8");
			out.write(new ViewResponseWriter("submit", 1, ex.getErrMsg()).serialize());
		}
		catch(RepositoryException ex)
		{
			response.addHeader("Content-Type", "text/xml; charset=utf-8");
			out.write(new ViewResponseWriter("submit", 1, ex.getErrMsg()).serialize());
		}
		catch (Exception e)
		{
			response.addHeader("Content-Type", "text/xml; charset=utf-8");
			//out.write( "<response><code>1</code><msg>" + e.getMessage() + "</msg></response>" );
			out.write(new ViewResponseWriter("submit", 2, e.getMessage()).serialize());
		}
		//out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
	}

	//public void destroy()
	//{
	//}
	
	//public String ByteToString(byte[] b){
	//	String t="";
	//	for(int i=0;i<b.length;i++) t=t+(char)b[i];
	//	return t;
	//}
	
	public void Handle_Save( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		if (request == null) throw new IOException("Invalid request");

		try
		{
	        String menuXml = request.getParameter("data");
	        byte[] utf8 = menuXml.getBytes("UTF-8");
	        response.addHeader("Content-disposition", "attachment; filename=menus.xml");
	        response.setContentType("text/xml");
	        response.setContentLength(utf8.length);
	        OutputStream os = response.getOutputStream();
	        os.write(utf8, 0, utf8.length);
		}
		catch (Exception e)
		{
			PrintWriter out = response.getWriter();
			out.write( "<response><code>1</code><msg>" + e.getMessage() + "</msg></response>" );
		}
	}
	
	private void LoadMenuInDesigner(HttpServletResponse response, String menuXml) throws IOException, ServletException
	{
		try
		{
			response.setContentType( "text/html; charset=UTF-8" );
			//response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!doctype html><head/><body><textarea id='data' style='display:none;'>");
			out.println(menuXml.replace("&amp;", "&amp;amp;"));
			out.println("</textarea>");
			out.println("<script type='text/javascript'>");
			out.println("var data=document.getElementById('data').value;");
			//out.println("alert(data);");
			out.println("parent.drawMenuFromStream(data);");
			out.println("parent.closeDialog();");
			out.println("</script></body></html>");
			out.flush();
		}
		catch (Exception e)
		{
			PrintWriter out = response.getWriter();
			out.write( "<response><code>1</code><msg>" + e.getMessage() + "</msg></response>" );
		}
	}
	
	public void Handle_Load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{/*
		if (request == null) throw new IOException("Invalid request");
		
		String menuXml = new String("");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
		{
			// Do nothing
			//throw new IOException("File Not Uploaded");
		}
		else
		{
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = null;

			try
			{
				items = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			Iterator itr = items.iterator();
			while (itr.hasNext())
			{
				FileItem item = (FileItem) itr.next();
				if (item.isFormField())
				{
					// *** Ignore for now ***
					//String name = item.getFieldName();
					//System.out.println("name: "+name);
					//String value = item.getString();
					//System.out.println("value: "+value);
				}
				else
				{
					try
					{
						String itemName = item.getName();
						
						InputStream stream = item.getInputStream();
						InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
						BufferedReader in = new BufferedReader(reader);
						//BufferedReader in = new BufferedReader(new InputStreamReader(item.getInputStream()));
						String line = null;
						StringBuffer sb = new StringBuffer();
				        while((line = in.readLine()) != null)
				        {
				            sb.append(line);
				            sb.append('\n');
				        }
				        menuXml = sb.toString();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		LoadMenuInDesigner(response, menuXml);*/
/*		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//OutputStream os = response.getOutputStream();
        //OutputStreamWriter osw = new OutputStreamWriter(os , "UTF-8");
        //PrintWriter out = new PrintWriter(osw);
        //out.print(menuXml);
        
		out.println("<!doctype html><head/><body><textarea id='data' style='display:none;'>");
		out.println(menuXml.replace("&amp;", "&amp;amp;"));
		out.println("</textarea>");
		out.println("<script type='text/javascript'>");
		out.println("var data=document.getElementById('data').value;");
		//out.println("alert(data);");
		out.println("parent.drawMenuFromStream(data);");
		out.println("parent.closeDialog();");
		out.println("</script></body></html>");
		out.flush();*/
	}

/*	String decodeUTF8(byte[] bytes) {
	    return new String(bytes, UTF8_CHARSET);
	}

	byte[] encodeUTF8(String string) {
	    return string.getBytes(UTF8_CHARSET);
	}

	
	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}*/

	
	public void Handle_LoadFromTemplate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request == null) throw new IOException("Invalid request");
		
		// Login to object manager
		//HttpSession thisSession = request.getSession();
		//if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
		//Credentials info = (Credentials)thisSession.getAttribute( "info" );
		
		String menuXml = new String("");

		
		//////////////////////////////////////////////
		/////////////////////////////////////
		String sample_menu = request.getParameter("sample_menu");
		//if( sample_menu != null && sample_menu.length() > 0 )
		if( sample_menu.equalsIgnoreCase("none") == false )
		{
			// C R E A T E   S A M P L E   M E N U S //
			String rootAppPath = SystemServlet.getGenesysHome();//thisContext.getInitParameter("GENESYS_HOME");
			String importFile = new String(rootAppPath + "WEB-INF/import/menus/" + sample_menu);
		
			// Create DOM document
			//XMLDocument xmlDoc = new XMLDocument();
			//if( xmlDoc.loadXML(importFile) )
			//{
/*				MenuBuilder mb = new MenuBuilder();
				XMLNodeList menuNodes = xmlDoc.getNodeList("//menu");
				for( int i = 0; i < menuNodes.getCount(); i++ )
				{
					XMLNode menuNode = menuNodes.getNodeByIndex(i);
					mb.importMenu(info, loc_id, menuNode, true);
				}*/
				try
				{
					//String itemName = item.getName();
					
					File iFile = new File(importFile);
					//byte[] bytes = getBytesFromFile(iFile);
					//menuXml = decodeUTF8(bytes);
					
					InputStream is = new FileInputStream(importFile);
					InputStreamReader reader = new InputStreamReader(is, "UTF-8");
					BufferedReader in = new BufferedReader(reader);
					//BufferedReader in = new BufferedReader(new InputStreamReader(item.getInputStream()));
					String line = null;
					StringBuffer sb = new StringBuffer();
			        while((line = in.readLine()) != null)
			        {
			            sb.append(line);
			            sb.append('\n');
			        }
			        menuXml = sb.toString();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			//}
		}
		//////////////////////////////////////////////
		/////////////////////////////////////
		//os = xx.getOutputStream();
		//PrintStream ps = new PrintStream(os, true, "UTF8");
		//ps.println("Hello, world.");
		LoadMenuInDesigner(response, menuXml);
/*		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!doctype html><head/><body><textarea id='data' style='display:none;'>");
		out.println(menuXml.replace("&amp;", "&amp;amp;"));
		out.println("</textarea>");
		out.println("<script type='text/javascript'>");
		out.println("var data=document.getElementById('data').value;");
		//out.println("alert(data);");
		out.println("parent.drawMenuFromStream(data);");
		out.println("parent.closeDialog();");
		out.println("</script></body></html>");
		out.flush();*/
	}
}
