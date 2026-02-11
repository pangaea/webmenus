package com.genesys.w3;

/*<Imports>*/
//Imported java classes
import java.io.*;
import java.math.BigDecimal;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.IOException;
import java.io.PrintWriter;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.session.ClientSessionBean;
import com.genesys.views.*;


public class PageServer extends HttpServlet
{
    /**
     * Initializes a Page instance.
     *  Also sets its logging value from application init param.
     *  Also creates its logger if not done before.
     */
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
	
	public void service( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException
	{
		try
		{
			//String resPath2 = request.getServletPath();
			//String rmtName = request.getRemoteHost();
			//String locName = request.getLocalName();
			String srvrName = request.getServerName();
			//boolean bLOE = false;
			//if(srvrName.equalsIgnoreCase("www.lifeoneuropa.net")) bLOE = true;
			String resPath = request.getPathInfo();
			if( resPath != null )
			{
				String pageName = resPath.substring(resPath.lastIndexOf('/')+1);
				if( pageName.length() > 0 )
				//String[] parts = resPath.split("/");
				//if( parts.length >= 2 )
				{
					//String pageName = parts[parts.length-1];
					//String viewName = parts[parts.length-2];
					//if(viewName.length() == 0) viewName = "page";
					
					String srvltPath = request.getServletPath();
					String viewName = "page";//srvltPath.substring(srvltPath.lastIndexOf('/')+1);
					
					// *** HACK ALERT ***
					if(srvrName.equalsIgnoreCase("www.lifeoneuropa.net")) viewName = "loe";
					
					//HttpSession thisSession = request.getSession();
					//String str = (String)thisSession.getAttribute( "ticket" );
					HttpSession beanContext = request.getSession();
					ClientSessionBean clientBean = (ClientSessionBean) beanContext.getAttribute("ClientSessionBean");
					//if( (String)thisSession.getAttribute( "ticket" ) == null )
					///if(str == null || clientBean == null || clientBean.m_info == null )

					ObjectManager objMan = SystemServlet.getObjectManager();
					if( clientBean == null || objMan.verifyClientInfo(clientBean.m_info) == null )
					{
						// TODO: Find a better way to do this...
						//getServletContext().getRequestDispatcher("/ViewCmd?call=login").forward(request, response);
						//ViewCmd views = (ViewCmd)getServletConfig().getServletContext().getServlet("ViewCmd");
						ViewCmd views = new ViewCmd();
						views.init();
						views.UserLogin(request);
					}
					getServletContext().getRequestDispatcher("/ViewCmd?view=" + viewName + "&name=" + pageName).forward(request, response);
				}
			}
			PrintWriter out = response.getWriter();
			out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
		}
		catch(Exception e)
		{
			PrintWriter out = response.getWriter();
			out.write( "<html><head><title>error</title></head><body>Exception Caught: " + e.getMessage() + "</body></html>" );
			return;
		}
	}
}