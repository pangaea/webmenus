///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.w3c.dom.Document;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.*;
//import com.genesys.webmenus.designer.HttpSession;

public class OrderView extends HttpServlet
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
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		try
		{
			String sOrderId = request.getParameter("oid");
			if( sOrderId == null || sOrderId.length() == 0 )
				throw new ServletException("Missing or invalid order id");
			
			boolean bAlreadyLoggedIn = false;
			HttpSession thisSession = request.getSession();
			//if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
			Credentials info = (Credentials)thisSession.getAttribute( "info" );
			if(info != null) bAlreadyLoggedIn = true;
			
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( bAlreadyLoggedIn || objectBean.SystemLogin("guest", info ) == true )
			{
				try
				{
					// Query order object
					ObjectQuery queryOrder = new ObjectQuery( "CCMenuOrder" );
					queryOrder.addProperty("id", sOrderId);
					QueryResponse qrOrder = objectBean.Query( info, queryOrder );
					RepositoryObjects oOrders = qrOrder.getObjects( queryOrder.getClassName());
					if( oOrders.count() == 0 )
					{
						SystemServlet.g_logger.error( "Id not found while viewing order [" + sOrderId + "]" );
						return;
					}
					RepositoryObject oOrder = oOrders.get(0);
	
					OrderRenderer renderer = new OrderRenderer(oOrder, info);
					renderer.renderOrder();
					response.setContentType( "text/html; charset=UTF-8" );
					PrintWriter out = response.getWriter();
					out.write(renderer.getOrderHTML());
				}
				finally
				{
					if(!bAlreadyLoggedIn)
						objectBean.Logout(info);
				}
			}
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown - " + ex.getMessage() );
		}
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		PrintWriter out = response.getWriter();
		out.write( "<html><head><title>error</title></head><body>Invalid POST Request</body></html>" );
	}
}