///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.RepositoryException;
import com.genesys.util.ServletUtilities;
import com.genesys.util.xml.XMLDocument;
import com.genesys.util.xml.XMLNode;

public class LoginPatron extends HttpServlet
{
    /**
     * Initializes a ServerMsg instance.
     *  Also sets its logging value from application init param.
     *  Also creates its logger if not done before.
     *
     */
	public void init() throws ServletException
	{
	
	}
	
	private void redirectTo(HttpServletRequest request, HttpServletResponse response, String page)
	{
		try{
			if(ServletUtilities.isMobileDevice(request)){
				response.sendRedirect( request.getContextPath() + "/app/mobile/" + page );
			}else{
				response.sendRedirect( request.getContextPath() + "/app/" + page );
			}
		}
		catch(IOException e)
		{
			SystemServlet.g_logger.error( "Exception Caught in LoginPatron: " + e.toString() );
		}
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
		// Load menu order bean for this session
		MenuOrderBean menuOrderBean = null;
		HttpSession beanSessionContext = request.getSession(); // Session Level Scope
		synchronized (beanSessionContext)
		{
			menuOrderBean = (MenuOrderBean) beanSessionContext.getAttribute("menuOrderBean");
			if( menuOrderBean == null )
			{
				String msg = URLEncoder.encode("Invalid Session");
				redirectTo(request, response, "login_patron.jsp?msg=" + msg);
				return;
			}
		}
		
		String type = request.getParameter("type");
		if( type.equalsIgnoreCase("login") )
		{
			String email = (String)request.getParameter("email");
			if( menuOrderBean.loginPatron(email) == null )
			{
				String msg = URLEncoder.encode("Invalid Email Address");
				redirectTo(request, response, "login_patron.jsp?msg=" + msg);
				return;
			}
		}
		else if( type.equalsIgnoreCase("create") )
		{
			try
			{
				menuOrderBean.createPatron(request);
			}
			catch(AuthenticationException ex)
			{
				String msg = URLEncoder.encode(ex.getErrMsg());
				redirectTo(request, response, "login_patron.jsp?msg=" + msg);
				return;
			}
			catch(RepositoryException ex)
			{
				String msg;
				if( ex.getType() == RepositoryException.VALIDATION_ERROR )
					msg = URLEncoder.encode("Phone number is invalid.");
				else
					msg = URLEncoder.encode("Email already exists.");
				redirectTo(request, response, "login_patron.jsp?msg=" + msg);
				return;
			}
		}
		
		if( menuOrderBean.isDeliveryAvailable() )
			redirectTo(request, response, "delivery_options.jsp");
		else
			redirectTo(request, response, "ordersubmit.jsp");

	}
}
