///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.views;

/*<Imports>*/
// Imported java classes
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.net.*;

//import java.rmi.server.*;
//import javax.net.ssl.*;
//import javax.servlet.jsp.tagext.*;
//import org.w3c.dom.*;
//import org.xml.sax.*;
//import org.apache.xerces.dom.*;
//import org.apache.xerces.parsers.*;
//import org.apache.xalan.*;
//import java.sql.*;

// JDBC Imports
//import com.mysql.jdbc.*;
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;
//import com.mysql.jdbc.Driver;
//import com.mysql.jdbc.ResultSet;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.session.ClientSessionBean;
import com.genesys.util.RandomGUID;
import com.genesys.util.xml.*;
import com.genesys.util.xsl.XSLParser;
import com.genesys.views.InterfaceCfg.View.Field;

/*</Imports>*/
public class ViewCmd extends HttpServlet
{
	private static final int RECV_BUFFER_LEN = 1024;
	private static final int SERVLET_NAME_LEN = 9;
	private static final int WEBAPP_NAME_LEN = 6;

	//Socket conn;
	//BufferedReader serverRecv;
	//PrintWriter serverSend;
	//XMLDocument serverCFG;//, interfaceCFG;
	String rootAppPath;
	//DatagramRouter m_serverIO;
	//ObjectManager m_objectBean;
	ObjectManager m_objectBean = null;

    /**
     * Initializes a ServerMsg instance.
     *  Also sets its logging value from application init param.
     *  Also creates its logger if not done before.
     *
     */
	public void init() throws ServletException
	{
		// Instantiate m_objectBean by loading an ObjectManager bean at application scope
		/////////////////////////////
		// Loading and accessing a bean from a servlet
		//////////////////////////////////////////////////////
		rootAppPath = SystemServlet.getGenesysHome();
		
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
 		//HttpSession session = null;
		//session = request.getSession();

		// **** Here I will need to check "TYPE" before assuming "METHOD" ****

		String commandCode = (String)request.getParameter("call");

		/*if( commandCode.equals("frame") )
		{
			ExecuteServerMethod_Frame( request, response );
		}
		else **/
		if( commandCode == null )
		{
			// default operation
			//ExecuteServerMethod_Login( request, response );
			
			//Map params = request.getParameterMap();
			//params.put("call", "query");
			ExecuteServerMethod_Query( request, response );
		}
		else
		{
			if( commandCode.equalsIgnoreCase("query") )
			{
				ExecuteServerMethod_Query( request, response );
			}
			else if( commandCode.equalsIgnoreCase("report") )
			{
				ExecuteServerMethod_Report( request, response );
			}
			else if( commandCode.equalsIgnoreCase("submit") )
			{
				ExecuteServerMethod_Submit( request, response );
			}
			else if( commandCode.equalsIgnoreCase("delete") )
			{
				ExecuteServerMethod_Delete( request, response );
			}
			else if( commandCode.equalsIgnoreCase("login") )
			{
				ExecuteServerMethod_Login( request, response );
			}
			else if( commandCode.equalsIgnoreCase("logout") )
			{
				ExecuteServerMethod_Logout( request, response );
			}
			else if( commandCode.equalsIgnoreCase("profile") )
			{
				ExecuteServerMethod_Profile( request, response );
			}
			else
			{
				PrintWriter out = response.getWriter();
				out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
			}
		}
	}

    /**
     * Load/create instance of ClientSessionBean
     *
     */
	ClientSessionBean loadClientBean(HttpServletRequest request)
	{
		ClientSessionBean clientBean = null;
		
		// Instantiate clientBean by loading an ClientSessionBean bean at session scope
		/////////////////////////////
		// Loading and accessing a bean from a servlet
		//////////////////////////////////////////////////////
		try
		{
			//ServletContext beanContext = getServletContext();						// Page Level Scope
			HttpSession beanContext = request.getSession();						// Session Level Scope
			//ServletContext servContext = getServletConfig().getServletContext();	// Application Level Scope
			//rootAppPath = servContext.getInitParameter("GENESYS_HOME");
			//rootAppPath = System.getProperty("GENESYS_HOME");
			//int timeoutMins = SystemServlet.getsessionTimeoutMins();
			//beanContext.setMaxInactiveInterval( timeoutMins * 60 );
			synchronized (beanContext)
			{
				clientBean = (ClientSessionBean) beanContext.getAttribute("ClientSessionBean");
				if(clientBean == null)
				{
					try
					{
						clientBean = (ClientSessionBean) java.beans.Beans.instantiate(this.getClass().getClassLoader(), "com.genesys.session.ClientSessionBean");
						//clientBean.init( rootAppPath );
						//clientBean.m_glbObjMan = m_objectBean;
					}
					catch (Exception exc)
					{
						throw new ServletException("Cannot create bean of class com.genesys.session.ClientSessionBean", exc);
					}
					beanContext.setAttribute("ClientSessionBean", clientBean);
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error("loadClientBean...");
			SystemServlet.g_logger.error( e.getMessage() );
			//e.printStackTrace();
		}
		///////////////////
		//////////////
		
		return clientBean;
	}
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
	// 0-none, 1-read only, 2-read/write
	int getAccessRights( String AccessRights, String ViewName )
	{
		boolean bForceDeny = false;
		int iAccRights = 0, iDefRights = 0;
		String sAccessStr = AccessRights;
		String sAccess;
		//String sAccessArray[] = sAccessStr.split( "|" );
		int iStartIdx = 0;
		int iIdx = sAccessStr.indexOf( "|", iStartIdx );
		while( iIdx >= 0 )
		{
			sAccess = sAccessStr.substring( iStartIdx, iIdx );
			iStartIdx = iIdx + 1;
		/////////////////
		//for( int i = 0; i < sAccessArray.length; i++ )
		if( sAccess.length() > 0 )
		{
			//sAccess = sAccessArray[i];
			int iIndex = sAccess.indexOf( ":" );
			if( iIndex >= 0 )
			{
				String sView = sAccess.substring( 0, iIndex );
				String sRights = sAccess.substring( iIndex + 1 );
				if( sView.equalsIgnoreCase( ViewName ) == true )
				{
					if( sRights.equalsIgnoreCase( "read/write" ) == true )
					{
						iAccRights = 2;
					}
					else if( sRights.equalsIgnoreCase( "read only" ) == true )
					{
						iAccRights = 1;
					}
					else if( sRights.equalsIgnoreCase( "denied" ) == true )
					{
						bForceDeny = true;
					}
				}
				else if( sView.equalsIgnoreCase( "(default)" ) == true )
				{
					if( sRights.equalsIgnoreCase( "read/write" ) == true )
					{
						iDefRights = 2;
					}
					else if( sRights.equalsIgnoreCase( "read only" ) == true )
					{
						iDefRights = 1;
					}
					//else if( sRights.equalsIgnoreCase( "denied" ) == true )
					//{
					//	iDefRights = 0;
					//}
				}
			}
		}
		/////////////////////
		////////////////
			iIdx = sAccessStr.indexOf( "|", iStartIdx );
		}
		if( iAccRights == 0 && bForceDeny == false ) iAccRights = iDefRights;
		return iAccRights;
	}

	public int UserLogin(HttpServletRequest request)
	{
		HttpSession thisSession = request.getSession();
		
		// Clear any previous client sessions
		m_objectBean.Logout( (Credentials)thisSession.getAttribute( "info" ) );
		
		//if( clientBean == null )
		//	clientBean = loadClientBean(request);
		
		//InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();

		// Determine the portal requested or use the default
		String sPortal = (String)request.getParameter("portal");
		if( sPortal == null || sPortal.length() == 0 )
		{
			String portalName = SystemServlet.getDefaultPortalName();
			if( portalName != null && portalName.length() > 0 )
				sPortal = portalName;
		}
		///////////////////////////////////////////

		//System.out.println("DEBUG: ===> 111");


		// Pull the authentication information from the request
		String sUserID = (String)request.getParameter("userid");
		String sPassword = (String)request.getParameter("password");
		if( sUserID == null || sUserID.length() == 0 )
		{
			sUserID = SystemServlet.getAnonymousUserid();
			sPassword = SystemServlet.getAnonymousPassword();
		}
		else
		{
			if( sPassword == null )
			{
				sPassword = "";
			}
		}

		//System.out.println("DEBUG: LOGIN ===> " + sUserID + ":" + sPassword);

		try
		{
			Credentials info = new Credentials();
			if( m_objectBean.Login(sUserID, sPassword, info) == true )
			{
				int timeoutMins = SystemServlet.getsessionTimeoutMins();
				thisSession.setMaxInactiveInterval( timeoutMins * 60 );
				
				// Attach new credtials to client session bean
				ClientSessionBean clientBean = loadClientBean(request);
				clientBean.m_info = info;
				
				//XMLDocument serverCFG = SystemServlet.getGenesysServerCfg();
				//int timeoutMins = Integer.parseInt( serverCFG.getElementValue( "session-timeout" ), 10 );

				// Assign GUID
				//thisSession.setAttribute( "guid", info.m_sTicket );
				
				/////////////////////////////////////////////////
				thisSession.setAttribute( "info", info );
				thisSession.setAttribute( "portal", sPortal );
				thisSession.setAttribute( "role", info.m_RoleId );
				thisSession.setAttribute( "ticket", info.m_sTicket );
				thisSession.setAttribute( "show_welcome", info.m_showWelcome );
				String sAccess = "";
				if( info.m_bAdmin == true ) // Verify this client has ADMIN rights
				{
					thisSession.setAttribute( "admin", "Y" );
					sAccess += "(default):read/write|";
				}
				else
				{
					thisSession.setAttribute( "admin", "N" );
				}
				
				// Iterate access rights returned from Object Manager
				for( int i = 0; i < info.m_AccessList.size(); i++ )
				{
					String access_right = (String)info.m_AccessList.get(i);
					sAccess += access_right + "|";
				}
				////////////////////////////////////////////
				thisSession.setAttribute( "access_rights", sAccess );
				return 0;
			}
			else
			{
				return 1;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Exception Caught: " + e.toString() );
			return 2;
		}
	}

	void ExecuteServerMethod_Login( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		//HttpSession thisSession = request.getSession();
		///////////////////////////////////////////
		//ClientSessionBean clientBean = loadClientBean(request);
//		XMLDocument interfaceCFG = new XMLDocument(clientBean.getDocument());
		SystemServlet.g_logger.info( ">>>>>> ExecuteServerMethod_Login");
		InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();

		int iRet = UserLogin(request);
		switch(iRet)
		{
		case 0:
			// Look for a target view
			ClientSessionBean clientBean = loadClientBean(request);
			response.addCookie(new Cookie("show_welcome", (clientBean.m_info.m_showWelcome) ? "Y" : "N" ));
			String sViewParam = (String)request.getParameter("view");
			if( sViewParam != null && sViewParam.length() > 0 )
			{
				// Iterate all view fields
				// Use view fields to search for filters
				String filter = "";
				
				// Get view
				InterfaceCfg.View viewNode = interfaceCfg.getView(sViewParam);
				
				// Query view fields
				HashMap<String, InterfaceCfg.View.Field> _fieldMap = viewNode.getFields();
				Iterator<InterfaceCfg.View.Field>  _iter_fields = _fieldMap.values().iterator();
				while( _iter_fields.hasNext() )
				{
					Field field = _iter_fields.next();
					String fieldValue = (String)request.getParameter( field.getName() );
					if( fieldValue != null && fieldValue.length() > 0 )
					{
						filter += field.getName() + "=" + fieldValue;
					}
				}
			}
			
			String home_page = SystemServlet.getHomePage();
			//String back_url = request.getParameter("backurl");
			if( home_page != null && home_page.length() > 0 )
			{
				response.sendRedirect( home_page );
			}
			else
			{
				///////////////////////////////////////////////////
				response.sendRedirect( "ui/view_obj.jsp" );
				////////////////////////////////////////
			}
			break;

		case 1:
			response.sendRedirect( "ui/" + GetLoginPage(request) + "?err=Invalid%20credentials" );
			break;

		default:
			response.sendRedirect( "ui/" + GetLoginPage(request) + "?err=Server%20not%20responding" );
			break;
		}
	}
	
	void ExecuteServerMethod_Profile( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		///////////////////////////////////////////
		PrintWriter out = response.getWriter();
/*		HttpSession thisSession = request.getSession();
		if( (String)thisSession.getAttribute( "ticket" ) == null )
		{
			out.write(new ViewResponseWriter("query", 88, "Session not found").serialize());
			out.close();
			return;
		}*/
		
		ClientSessionBean clientBean = loadClientBean(request);
		if( clientBean.m_info == null )
		{
			out.write(new ViewResponseWriter("query", 90, "Session not found").serialize());
			out.close();
			return;
		}
		
		try
		{
			ObjectSubmit profileStmt = new ObjectSubmit("CProfile");
			String showWelcome = (String)request.getParameter( "data_show_welcome" );
			profileStmt.addProperty("show_welcome", showWelcome);
			m_objectBean.Update(clientBean.m_info, clientBean.m_info.m_UserId, profileStmt);
			response.addCookie(new Cookie("show_welcome", showWelcome ));
			out.write(new ViewResponseWriter("profile", 0, "SUCCESS").serialize());
		}
		catch(AuthenticationException ex)
		{
			out.write(new ViewResponseWriter("profile", 3, ex.getErrMsg()).serialize());
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Exception thrown in {ViewCmd::ExecuteServerMethod_Profile} - " + e.getMessage() );
			out.write(new ViewResponseWriter("profile", 9999, "Request failed due to server error").serialize());
		}
	}

	void ExecuteServerMethod_Logout( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		HttpSession thisSession = request.getSession();
		///////////////////////////////////////////
		ClientSessionBean clientBean = loadClientBean(request);
		m_objectBean.Logout( clientBean.m_info );
		if( thisSession.getAttribute( "ticket" ) == null )
		{
			response.sendRedirect( "ui/" + GetLoginPage(request) );
			return;
		}
		thisSession.invalidate();
		response.sendRedirect( "ui/" + GetLoginPage(request) );
	}

	void ExecuteServerMethod_Query( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		response.setCharacterEncoding("utf-8");
		//PrintWriter out = response.getWriter();
		try
		{
			///////////////////////////////////////////
			//ClientSessionBean clientBean = loadClientBean(request);
			/////////////////////////
/*			HttpSession thisSession = request.getSession();
			if( (String)thisSession.getAttribute( "ticket" ) == null )
			{
				PrintWriter out = response.getWriter();
				out.write(new ViewResponseWriter("query", 88, "Session not found").serialize());
				out.close();
				return;
			}*/
			
			ClientSessionBean clientBean = loadClientBean(request);
			if( clientBean.m_info == null )
			{
				PrintWriter out = response.getWriter();
				out.write(new ViewResponseWriter("query", 90, "Session not found").serialize());
				out.close();
				return;
			}
			//XMLDocument interfaceCFG = new XMLDocument(clientBean.getDocument());
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			
			// Validate view access //////////////
			String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
			String sViewParam = (String)request.getParameter("view");
			int iAccRights = getAccessRights( sAccRights, sViewParam );
			if( iAccRights == 0 )
			{
				PrintWriter out = response.getWriter();
				//PrintWriter out = response.getWriter();
				//out.println( new String( "Access denied!" ) );
				out.write(new ViewResponseWriter("query", 86, "Access denied!").serialize());
				out.close();
				return;
			}
			//////////////////////////////////////

			///////////////////////////////////////////
			InterfaceCfg.View viewNode = interfaceCfg.getView(sViewParam);
			ObjectQuery queryStmt = new ObjectQuery( viewNode.getClassName() );

			String sObjStart = (String)request.getParameter("objStart");
			if( sObjStart == null ) sObjStart = new String("0");
			String sObjCount = (String)request.getParameter("objCount");
			if( sObjCount == null ) sObjCount = Integer.toString(SystemServlet.getdefaultQueryCount());
			//SystemServlet.g_logger.debug( "objStart = " + sObjStart );
			//SystemServlet.g_logger.debug( "objCount = " + sObjCount );
			queryStmt.setStart( (long)Integer.parseInt( sObjStart ) );
			queryStmt.setCount( (long)Integer.parseInt( sObjCount ) );
			//SystemServlet.g_logger.debug( "success" );

			// Determine if default sort by and sort order is needed
			String sortBy = (String)request.getParameter("sortBy");
			if( sortBy != null && sortBy.length() > 0 )
				queryStmt.setSortBy( viewNode.getField(sortBy).getProperty() );
			else
				queryStmt.setSortBy( viewNode.getSortBy() );

			String sortOrder = (String)request.getParameter("sortOrder");
			if( sortOrder == null || sortOrder.length() == 0 )
				sortOrder = viewNode.getSortOrder();
			if( sortOrder.equalsIgnoreCase("A") ) sortOrder = new String("ASC");
			else if( sortOrder.equalsIgnoreCase("D") ) sortOrder = new String("DESC");
			queryStmt.setSortOrder( sortOrder );

			String requestLevel = "0";
			String accessLevel = (String)request.getParameter("accessLevel");
			if( accessLevel != null )
			{
				if( accessLevel.equalsIgnoreCase( "2"/*"all_objects"*/ ) == true )
					queryStmt.setRequestLevel("2");
				else if( accessLevel.equalsIgnoreCase( "1"/*"child_objects"*/ ) == true )
					queryStmt.setRequestLevel("1");
				else if( accessLevel.equalsIgnoreCase( "0"/*"my_objects"*/ ) == true )
					queryStmt.setRequestLevel("0");
			}

			//SystemServlet.g_logger.debug( "view = " + sViewParam );
			// Iterate all view fields
			HashMap<String,InterfaceCfg.View.Field> fieldList = viewNode.getFields();
			Iterator itr = fieldList.values().iterator();
		    while(itr.hasNext())
		    {
		    	InterfaceCfg.View.Field iField = (InterfaceCfg.View.Field)itr.next();
				do
				{
					String propertyFilter = iField.getFilter();
					if( propertyFilter == null || propertyFilter.length() == 0 )
					{
						propertyFilter = (String)request.getParameter( iField.getName() );
						if( propertyFilter == null || propertyFilter.length() == 0 )
							break;
					}
				
					// Add filter property
					ObjectProperty objProp = new ObjectProperty(iField.getProperty(),propertyFilter);
					//objProp.setName( fieldNode.getAttribute( "property" ) );
					//objProp.setValue( propertyFilter );
					queryStmt.getProperties().add( objProp );
					SystemServlet.g_logger.debug( "param name=" + objProp.getName() );
					//SystemServlet.g_logger.debug( "param name=" + objProp.getName() + "; value=" + objProp.getText() );
				}
				while(false);
		    }

			//SystemServlet.g_logger.debug( "m_objectBean = " + m_objectBean );
			String serverResponse = m_objectBean.QueryXML( clientBean.m_info, queryStmt );
			//SystemServlet.g_logger.debug( "serverResponse = " + serverResponse );
			String xslt = viewNode.getTransform();
			if( xslt == null || xslt.length() == 0 )
			{
				response.setContentType("text/xml");
				response.setCharacterEncoding("utf-8");
				PrintWriter out = response.getWriter();
				out.write(serverResponse);
			}
			else
			{
				response.setContentType( "text/html; charset=UTF-8" );
				String xslUri = rootAppPath + "templates/" + xslt;
				ServeTemplate( xslUri, serverResponse, response.getWriter() );
			}
		}
		catch(AuthenticationException ex)
		{
			PrintWriter out = response.getWriter();
			out.write(new ViewResponseWriter("query", 3, ex.getErrMsg()).serialize());
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Exception thrown in {ViewCmd::ExecuteServerMethod_Query} - " + e.getMessage() );
			PrintWriter out = response.getWriter();
			out.write(new ViewResponseWriter("query", 9999, "Request failed due to server error").serialize());
		}
		//out.close();
	}

	void ExecuteServerMethod_Report( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
	}

	void ExecuteServerMethod_Submit( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		PrintWriter out = response.getWriter();
		try
		{
			///////////////////////////////////////////
			ClientSessionBean clientBean = loadClientBean(request);
			if( clientBean == null )
			{
				response.sendRedirect( "ui/" + GetLoginPage(request) );
				return;
			}
			
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();

			////////////////////////
/*			HttpSession thisSession = request.getSession();
			if( thisSession.getAttribute( "ticket" ) == null )
			{
				response.sendRedirect( "ui/" + GetLoginPage(request) );
				return;
			}*/

			// Validate view access //////////////
			String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
			String sViewParam = (String)request.getParameter("view");
			int iAccRights = getAccessRights( sAccRights, sViewParam );
			if( iAccRights != 2 )
			{
				//PrintWriter out = response.getWriter();
				//out.println( new String( "Access denied!" ) );
				out.write(new ViewResponseWriter("submit", 86, "Access denied!").serialize());
				out.close();
				return;
			}
			//////////////////////////////////////

			// Query for view object
			InterfaceCfg.View viewNode = interfaceCfg.getView(sViewParam);

			// Build submit object
			ObjectSubmit submitStmt = new ObjectSubmit( viewNode.getClassName() );
			ObjectProperties props = submitStmt.getProperties();

			///////////////////////////////////////////
			List<InterfaceCfg.View.Input> _inputs = viewNode.getInputs();
			Iterator<InterfaceCfg.View.Input>_iter_inputs = _inputs.iterator();
			while( _iter_inputs.hasNext() )
			{
				InterfaceCfg.View.Input inputNode = _iter_inputs.next();

				ObjectProperty prop = new ObjectProperty();

				String fieldName = inputNode.getField();
				InterfaceCfg.View.Field fieldNode = viewNode.getFields().get(fieldName);
				
				//prop.setName( fieldPropertyName );
				prop.setName( fieldNode.getProperty() );

				//String propertyFilter = fieldNode.getAttribute( "filter" );
				String propertyFilter = fieldNode.getFilter();
				if( propertyFilter != null && propertyFilter.length() > 0 )
				{
					//serverRequest += XmlEncodeString( propertyFilter );
					prop.setValue( propertyFilter );
				}
				else
				{
					//String inputType = m_objectBean.GetClassPropertyType(viewNode.getAttribute( "class" ), fieldName);
					String inputType = m_objectBean.GetClassPropertyType(viewNode.getClassName(), fieldName);
					if( inputType.equalsIgnoreCase( "boolean" ) == true )
					{
						String boolVal = (String)request.getParameter( "data_" + fieldName );
						if( boolVal.equalsIgnoreCase( "Y" ) == true )
							prop.setValue( "Y" );
						else
							prop.setValue( "N" );
					}
					else if( inputType.equalsIgnoreCase("int") == true )
					{
						try
						{
							Integer iParam = new Integer((String)request.getParameter( "data_" + fieldName ));
							prop.setValue(iParam.intValue());
						}
						catch(Exception e)
						{
							prop.setValue(0);
						}
					}
					else if( inputType.equalsIgnoreCase("real") == true )
					{
						try
						{
							Double dParam = new Double((String)request.getParameter( "data_" + fieldName ));
							prop.setValue(dParam.doubleValue());
						}
						catch(Exception e)
						{
							prop.setValue(0.0);
						}
					}
/*
						else if( inputType.equalsIgnoreCase( "list" ) == true )
						{
							String sObjList = (String)request.getParameter( "data_" + fieldName );
							int iStartIdx = 0;
							
							//int iIdx = -1;//sObjList.indexOf( ";", iStartIdx );
							//while( (iIdx = sObjList.indexOf( ";", iStartIdx )) >= 0 )
							//{
							//	String sObjID = sObjList.substring( iStartIdx, iIdx );
							//	iStartIdx = iIdx + 1;
							//	if( sObjID.length() > 0 )
							//	{
							//		serverRequest += "<objectref>" + sObjID + "</objectref>";
							//	}
							//	//iIdx = sObjList.indexOf( ";", iStartIdx );
							//}
						}
*/					else if( inputType.equalsIgnoreCase("password") == true )
					{
						String pass1 = request.getParameter( "data_" + fieldName );
						if( pass1 != null )
						{
							String pass2 = request.getParameter( "data_" + fieldName + "_vrfy" );
							if( pass1.equals(pass2) == false )
								throw new RepositoryException("Password Mismatch", RepositoryException.VALIDATION_ERROR);
							
							prop.setValue( (String)request.getParameter( "data_" + fieldName ) );
						}
					}
					else
					{
						//serverRequest += XmlEncodeString( (String)request.getParameter( "data_" + fieldName ) );
						String param = request.getParameter( "data_" + fieldName );
						if( param != null )
						{
							prop.setValue( (String)param );
						}
					}
				}
				props.add( prop );
			}

			ViewResponseWriter resp = new ViewResponseWriter();
			String submitted_id = (String)request.getParameter("id");
			if( submitted_id == null ||
				submitted_id.length() == 0 ||
				submitted_id.equalsIgnoreCase( "-1" ) == true )
			{
				m_objectBean.Insert( clientBean.m_info, submitStmt );
				//serverResponse.append("<call>insert</call>");
				resp.setCall("insert");
			}
			else if( submitted_id.startsWith( "-" ) == true )
			{
				// Setup server COPY method call
				String copy_id = submitted_id.substring( 1 );
				m_objectBean.Copy( clientBean.m_info, copy_id, submitStmt );
				//serverResponse.append("<call>copy</call>");
				resp.setCall("copy");
			}
			else
			{
				m_objectBean.Update( clientBean.m_info, submitted_id, submitStmt );
				//serverResponse.append("<call>update</call>");
				resp.setCall("update");
			}
			resp.setCode(0);
			resp.setMsg("SUCCESS");
			
			// Return raw XML
			//PrintWriter out = response.getWriter();
			//out.write(serverResponse.toString());
			out.write(resp.serialize());
		}
		catch(AuthenticationException ex)
		{
			out.write(new ViewResponseWriter("submit", 3, ex.getErrMsg()).serialize());
		}
		catch(RepositoryException ex)
		{
			/// Validation failure message ///
			
			//SystemServlet.g_logger.error( "Exception thrown in {ViewCmd::ExecuteServerMethod_Submit} - " + ex.getErrMsg() );
			//PrintWriter out = response.getWriter();
			//ViewResponseWriter resp = new ViewResponseWriter("submit", 1, ex.getErrMsg());
			//out.write(resp.serialize());
			out.write(new ViewResponseWriter("submit", 1, ex.getErrMsg()).serialize());
			
			//StringBuffer serverResponse = new StringBuffer();
			//serverResponse.append("<return>");
			//serverResponse.append("<call>submit</call>");
			//serverResponse.append("<code>0</code>");
			//serverResponse.append("<msg>" + ex.getErrMsg() + "</msg>");
			//serverResponse.append("</return>");
			//out.write(serverResponse.toString());
		}
		catch( IOException e )
		{
			//PrintWriter out = response.getWriter();
			//out.println( new String( "Exception caught!!!" ) );
			out.write(new ViewResponseWriter("submit", 2, e.getMessage()).serialize());
			//out.close();
		}
		out.close();
	}

	void ExecuteServerMethod_Delete( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		PrintWriter out = response.getWriter();
		try
		{
			///////////////////////////////////////////
			ClientSessionBean clientBean = loadClientBean(request);
			if( clientBean == null )
			{
				response.sendRedirect( "ui/" + GetLoginPage(request) );
				return;
			}
			
			//XMLDocument interfaceCFG = new XMLDocument(clientBean.getDocument());
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();

			////////////////////
/*			HttpSession thisSession = request.getSession();
			if( thisSession.getAttribute( "ticket" ) == null )
			{
				response.sendRedirect( "ui/" + GetLoginPage(request) );
				return;
			}*/

			// Validate view access //////////////
			String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
			String sViewParam = (String)request.getParameter("view");
			int iAccRights = getAccessRights( sAccRights, sViewParam );
			if( iAccRights != 2 )
			{
				//PrintWriter out = response.getWriter();
				//out.println( new String( "Access denied!" ) );
				out.write(new ViewResponseWriter("delete", 86, "Access denied!").serialize());
				out.close();
				return;
			}
			//////////////////////////////////////

			InterfaceCfg.View viewNode = interfaceCfg.getView(sViewParam);
			m_objectBean.Delete( clientBean.m_info, viewNode.getClassName(), (String)request.getParameter("id") );
			
			//PrintWriter out = response.getWriter();
			out.write(new ViewResponseWriter("delete", 0, "SUCCESS").serialize());
			//String xslUri = rootAppPath + "templates/proxy_response.xsl";
			//ServeTemplate( xslUri, serverResponse, response );
		}
		catch(AuthenticationException ex)
		{
			out.write(new ViewResponseWriter("delete", 3, ex.getErrMsg()).serialize());
		}
		catch(RepositoryException ex)
		{
			out.write(new ViewResponseWriter("delete", 1, ex.getErrMsg()).serialize());
		}
		catch( IOException e )
		{
			out.write(new ViewResponseWriter("delete", 2, e.getMessage()).serialize());
		}
		out.close();
	}
	
	public static void ServeTemplate( String xslUri, String xmlDoc, Writer out ) throws IOException
	{
		// parse and load document
		try
		{
			// Generate page through XSL
			XSLParser xslParser = new XSLParser();
			xslParser.transform( xmlDoc/*parser.getXMLString()*/, xslUri, out );//, out );
			//out.println( outDoc );
		}
		catch(Exception e) {
			SystemServlet.g_logger.error( "Exception thrown in {ViewCmd::ServeTemplate} - " + e.getMessage() );
		}
		//out.close();
	}
	
	String GetLoginPage( HttpServletRequest request )
	{
		if( request.getParameter("loginPage") != null )
			return request.getParameter("loginPage");
		else
			return "login.jsp";
	}
	
}// end class ServerMsg
