package com.genesys.webmenus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyOrder extends HttpServlet {

    MenuOrderBean menuOrderBean;

    public void init() throws ServletException {
        try {
		} catch (Exception e) {
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
	public void service( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		try {
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

			// Extremely simple "REST" interface
			String resPath = request.getPathInfo();
			if( resPath != null )
			{
                response.setContentType("text/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
				if( resPath.equalsIgnoreCase("/getstatus") ) {
                    String orderId = (String)request.getParameter("id");
                    Map<String, Object> res = menuOrderBean.getOrderStatus(orderId.toString());
                    out.write("{\"status\":\"" + res.get("status") + "\",\"estimated_time\":\"" + res.get("estimated_time") + "\"}");
				}
				else {
                    out.write("{\"status\":\"-1\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
