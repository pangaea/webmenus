package com.genesys.webmenus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderCheckout extends HttpServlet {
	MenuOrderBean menuOrderBean = null;

    public void init() throws ServletException {
	
	}
	
	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */
	public void service( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException {
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

			// Save order
			String email = request.getParameter("email");
			Map<String, String> attrs = request.getParameterMap().entrySet().stream()
				.filter(entry -> entry.getValue() != null && entry.getValue().length > 0)
				.collect(Collectors.toMap(
					Map.Entry::getKey,
					entry -> entry.getValue()[0] // Take the first value
				));

			String orderId = menuOrderBean.processOrder(email, 0, attrs);
			if(orderId != null ){
				response.sendRedirect( request.getContextPath() + "/app/my_order.jsp?loc=" + menuOrderBean.getCurrentLocationId() + "&id=" + orderId );
			}
		}
		catch(Exception e){
			System.out.println("DEBUG: Exception ==> " + e.getMessage());
			PrintWriter out = response.getWriter();
			out.write( "<html><head><title>error</title></head><body>" + e.getMessage() + "</body></html>" );
		}
    }
}
