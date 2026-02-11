///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;
//import javax.servlet.jsp.*;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
//import com.genesys.views.ViewResponseWriter;
import com.genesys.util.xml.*;
import com.genesys.util.xsl.XSLParser;
import com.genesys.util.ServletUtilities;

public class MenuView extends HttpServlet
{
	public void init() throws ServletException
	{
	}
	
	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */
	public void service( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		// Extremely simple "REST" interface
		String resPath = request.getPathInfo();
		if( resPath == null || resPath.equalsIgnoreCase("/query") )
		{
			handleQuery( request, response );
		}
		else if( resPath.equalsIgnoreCase("/theme") )
		{
			handleTheme( request, response );
		}
		else if( resPath.equalsIgnoreCase("/sample") )
		{
			handleSample( request, response );
		}
		else if( resPath.equalsIgnoreCase("/vieworder") )
		{
			handleViewOrder( request, response );
		}
		else if( resPath.equalsIgnoreCase("/menuitemview") )
		{
			handleMenuItemView( request, response );
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
		}
	}

	public void handleQuery( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		try
		{
			String sLocId = request.getParameter("loc");
			if( sLocId == null || sLocId.length() == 0 )
				throw new ServletException("Missing or invalid location id");
			
			ObjectManager objMan = SystemServlet.getObjectManager();
			
			String menu_template = "";
			Credentials info = new Credentials();
			if( objMan.SystemLogin("guest", info) )
			{
				try
				{
					ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
					queryLoc.addProperty("id", sLocId);
					QueryResponse qrLoc = objMan.Query( info, queryLoc );
					RepositoryObjects oLocs = qrLoc.getObjects( queryLoc.getClassName() );
					if( oLocs.count() == 0 ) return;	// Invalid location id
					RepositoryObject oLoc = oLocs.get(0);
					menu_template = oLoc.getPropertyValue("template");
				}
				finally
				{
					objMan.Logout(info);
				}
			}
			
			if(ServletUtilities.isMobileDevice(request))
			{
				response.sendRedirect(request.getContextPath() + "/Menus/" + sLocId + "/");
			}
			else
			{
				if( menu_template.compareToIgnoreCase("menu-top") == 0 )
					getServletContext().getRequestDispatcher("/app/menusview2.jsp").forward(request, response); 
				else
					getServletContext().getRequestDispatcher("/app/menusview.jsp").forward(request, response);
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
	}
	
	public void handleTheme( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		try
		{
			String sThemeId = request.getParameter("id");
			if( sThemeId == null || sThemeId.length() == 0 )
				throw new ServletException("Missing or invalid theme id");
			
			ObjectManager objMan = SystemServlet.getObjectManager();
			
			//String menu_template = "";
			Credentials info = new Credentials();
			if( objMan.SystemLogin("guest", info) )
			{
				try
				{
					ObjectQuery queryTheme = new ObjectQuery( "CEThemeInfo" );
					queryTheme.addProperty("id", sThemeId);
					String themeXml = objMan.QueryXML( info, queryTheme );
					
					XSLParser xslParser = new XSLParser();
					String rootPath = SystemServlet.getGenesysHome();
	
					String xslTextUri = rootPath + "templates/webmenus/theme.xsl";
					xslParser.transform( themeXml, xslTextUri, response.getWriter() );
				}
				finally
				{
					objMan.Logout(info);
				}
			}
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown - " + ex.getMessage() );
		}
	}
	
	//public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	public void handleSample( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		ByteArrayOutputStream outXML = new ByteArrayOutputStream();
		try
		{
			XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
			xmlStreamWriter.writeStartElement("return");		// <return>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "call", "query");		// <call>query</call>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "code", "0");			// <code>0</code>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "msg", "SUCCESS");		// <msg>SUCCESS</msg>
			xmlStreamWriter.writeStartElement("document");		// <document>
			xmlStreamWriter.writeStartElement("collection");	// <collection>
			xmlStreamWriter.writeStartElement("object");	// <object>
			
			Enumeration en = request.getParameterNames();
		    while (en.hasMoreElements()) {
		    	String paramName = (String) en.nextElement();
		    	if( paramName.startsWith("themes_data_") )
		    	{
			    	xmlStreamWriter.writeStartElement("property");	// <property>
			    	xmlStreamWriter.writeAttribute("name", paramName.substring(12));
			    	xmlStreamWriter.writeCharacters(request.getParameter(paramName));
			    	xmlStreamWriter.writeEndElement();	// </property>
		    	}
		    }
			
			xmlStreamWriter.writeEndElement();	// </object>
			xmlStreamWriter.writeEndElement();	// </collection>
			xmlStreamWriter.writeEndElement();	// </document>
			xmlStreamWriter.writeEndElement();	// </return>
			
			// Close XML Document
			xmlStreamWriter.writeEndDocument();
			xmlStreamWriter.flush();
			xmlStreamWriter.close();
			
			// Create CSS
			XSLParser xslParser = new XSLParser();
			String rootPath = SystemServlet.getGenesysHome();
			String xslTextUri = rootPath + "templates/webmenus/theme.xsl";
			xslParser.transform( outXML.toString("utf-8"), xslTextUri, response.getWriter() );
		}
		catch(XMLStreamException e)
		{
			SystemServlet.g_logger.error( "XMLStreamException thrown in {ObjectManager::QueryXML} - " + e.getMessage() );
		}
		catch(FactoryConfigurationError e)
		{
			SystemServlet.g_logger.error( "FactoryConfigurationError thrown in {ObjectManager::QueryXML} - " + e.getMessage() );
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown - " + ex.getMessage() );
		}
	}
	
	//public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	public void handleViewOrder( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		getServletContext().getRequestDispatcher("/app/orderview.jsp").forward(request, response);
	}
	
	public void handleMenuItemView( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		getServletContext().getRequestDispatcher("/app/menuitemview.jsp").forward(request, response);
	}
	
	//public void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	//{
	//	PrintWriter out = response.getWriter();
	//	out.write( "<html><head><title>error</title></head><body>Invalid POST Request</body></html>" );
	//}
}