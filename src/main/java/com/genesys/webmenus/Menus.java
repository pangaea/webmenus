///////////////////////////////////////
// Copyright (c) 2004-2012 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.session.ClientSessionBean;
import com.genesys.util.ServletUtilities;

public class Menus extends HttpServlet
{
	MenuOrderBean menuOrderBean = null;
	String rootMenuPath = "";
	
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
		//System.out.println("DEBUG: View Menu ===> 111");
		try{
			HttpSession beanSessionContext = request.getSession(); // Session Level Scope
			synchronized (beanSessionContext){
				menuOrderBean = (MenuOrderBean) beanSessionContext.getAttribute("menuOrderBean");
				if( menuOrderBean == null ){
					try{
						menuOrderBean = (MenuOrderBean) java.beans.Beans.instantiate(this.getClass().getClassLoader(), "com.genesys.webmenus.MenuOrderBean");
					}
					catch (Exception exc){
						throw new ServletException("Cannot create bean of class com.genesys.session.ClientSessionBean", exc);
					}
					beanSessionContext.setAttribute("menuOrderBean", menuOrderBean);
				}
			}
			
			String resPath = request.getPathInfo();
			if( resPath == null ) {
				//System.out.println("DEBUG: Invalid Request: missing location");
				throw new Exception("Invalid Request: missing location");
			}
			
			String res_array[] = resPath.substring(1).split("/");
			if( res_array.length > 0 ){
				// Location found in path - extract it here
				String locationId = res_array[0];
				
				ObjectManager objMan = SystemServlet.getObjectManager();
				Credentials info = new Credentials();
				if( objMan.SystemLogin("guest", info) ){
					//System.out.println("DEBUG: Guest login success!!!");
					try{
						ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
						rootMenuPath = request.getContextPath() + "/Menus/" + locationId;
						queryLoc.addProperty("id", locationId);
						RepositoryObjectIterator locIter = new RepositoryObjectIterator(objMan.Query(info,queryLoc));
						if(locIter.first() == false)
							throw new Exception("Location not found");
						
						RepositoryObject oLoc = locIter.getObj();
						request.setAttribute("locationObject", oLoc);

						// Extract page view from path, if it exists, otherwise default to locationview
						String viewPage = "locationview";
						if( res_array.length > 1 && res_array[1].length() > 0 ) viewPage = res_array[1];
						
						
						// Handles 'login' route to process a customer login procedure
						if( viewPage.equalsIgnoreCase("login") ){
							loginPatron(request, response);
						} ////////////////////////////////////////////////////////
							
							
						// Handles 'create_account' route to process a customer account creation
						else if( viewPage.equalsIgnoreCase("create_account") ){
							createPatron(request, response);
						} ////////////////////////////////////////////////////////
						
							
						// Handles 'submit_item' route to process a customer adding an item to their order
						else if( viewPage.equalsIgnoreCase("submit_item") ){
							submitItem(request, response);
						} ////////////////////////////////////////////////////////
							
						
						// Handles 'submit_order' route to process a customer submitting their order
						else if( viewPage.equalsIgnoreCase("submit_order") ){
							submitOrder(request, response);
						} ////////////////////////////////////////////////////////

							
						// Handles page render request
						else{
							if(ServletUtilities.isMobileDevice(request)){
								getServletContext().getRequestDispatcher("/app/mobile/" + viewPage + ".jsp").forward(request, response);
							}else{
								response.sendRedirect( request.getContextPath() + "/app/menusview2.jsp?loc=" + locationId );
							}
						} ////////////////////////////////////////////////////////
					}
					finally{
						objMan.Logout(info);
					}
				}/* else {
					System.out.println("DEBUG: Guest login failed :-(");
				}*/
			}
		}
		catch(Exception e){
			System.out.println("DEBUG: Exception ==> " + e.getMessage());
			PrintWriter out = response.getWriter();
			out.write( "<html><head><title>error</title></head><body>" + e.getMessage() + "</body></html>" );
		}
	}
	
	public void loginPatron( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		String email = (String)request.getParameter("email");
		if( menuOrderBean.loginPatron(email) == null ){
			String msg = URLEncoder.encode("Invalid Email Address", "UTF-8");
			response.sendRedirect( rootMenuPath + "/login_patron?msg=" + msg );
		}
		handleDeliveryOption(response);
	}
	
	public void createPatron( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		try{
			menuOrderBean.createPatron(request);
		}
		catch(AuthenticationException ex){
			String msg = URLEncoder.encode(ex.getErrMsg(), "UTF-8");
			response.sendRedirect( rootMenuPath + "/login_patron?create=1&msg=" + msg );
			return;
		}
		catch(RepositoryException ex){
			
			// Make errors more user friendly
			String msg;
			if( ex.getType() == RepositoryException.VALIDATION_ERROR ){
				if( ex.getErrMsg().indexOf("phone_num") >= 0 )
					msg = URLEncoder.encode("Phone number is missing/invalid.", "UTF-8");
				else if( ex.getErrMsg().indexOf("email") >= 0 )
					msg = URLEncoder.encode("Email is missing/invalid.", "UTF-8");
				else if( ex.getErrMsg().indexOf("firstname") >= 0 )
					msg = URLEncoder.encode("First name is missing/invalid.", "UTF-8");
				else
					msg = URLEncoder.encode(ex.getErrMsg(), "UTF-8");
			}
			else{
				msg = URLEncoder.encode("Email already exists.", "UTF-8");
			}

			response.sendRedirect( rootMenuPath + "/login_patron?create=1&msg=" + msg );
			return;
		}
		handleDeliveryOption(response);
	}
	
	public void handleDeliveryOption(HttpServletResponse response) throws IOException
	{
		if( menuOrderBean.isDeliveryAvailable() ){
			response.sendRedirect( rootMenuPath + "/delivery_options" );
		}else{
			response.sendRedirect( rootMenuPath + "/submit_order" );
		}
	}
	
	public void submitItem( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		//String errMsg = "There was an error encountered:-(";
		MenuOrderBean menuOrderBean = (MenuOrderBean) request.getSession().getAttribute("menuOrderBean");
		boolean bOpen = menuOrderBean.isWithinOpertingHours();
		if( bOpen ){
			if( menuOrderBean.submitNewItem(request) == true ){
				response.sendRedirect(rootMenuPath + "/orderview");
				return;
			}
		}
		response.sendRedirect(rootMenuPath + "/location_closed");
	}
	
	public void submitOrder( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
	{
		//String errMsg = "There was an error encountered:-(";
		MenuOrderBean menuOrderBean = (MenuOrderBean) request.getSession().getAttribute("menuOrderBean");
		boolean bOpen = menuOrderBean.isWithinOpertingHours();
		if( bOpen ){
			
			if( menuOrderBean.isValidated() == false ){
				response.sendRedirect( rootMenuPath + "/login_patron" );
				return;
			}
			
			String sDelivery = request.getParameter("delivery_option");
			if( sDelivery != null && sDelivery.equalsIgnoreCase("delivery") == true ){
				String sDeliveryInfo = request.getParameter("delivery_info");
				menuOrderBean.setDeliveryAddress( sDeliveryInfo );
			}
			
			if( menuOrderBean.submitOrder() == true ){
				response.sendRedirect(rootMenuPath + "/order_success");
			}
			else{
				response.sendRedirect(rootMenuPath + "/order_failure");
			}
		}
		response.sendRedirect(rootMenuPath + "/location_closed");
	}
}