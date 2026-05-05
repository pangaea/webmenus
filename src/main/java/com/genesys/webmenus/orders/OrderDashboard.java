package com.genesys.webmenus.orders;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;
import com.genesys.views.ViewResponseWriter;

public class OrderDashboard extends HttpServlet {

    ObjectManager m_objectBean = null;

    public void init() throws ServletException {
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

    public void service( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException {
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
			else if( resPath.equalsIgnoreCase("/getorders") )
			{
				Handle_Query( request, response );
			}
			else if( resPath.equalsIgnoreCase("/updateorder") )
			{
				//Handle_Query( request, response );
			}
			else if( resPath.equalsIgnoreCase("/cancelorder") )
			{
				//Handle_Submit( request, response );
			}
			else
			{
				PrintWriter out = response.getWriter();
				out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
			}
		}
    }

    public void Handle_Query( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		try
		{
            HttpSession thisSession = request.getSession();
            if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
            Credentials info = (Credentials)thisSession.getAttribute( "info" );
			ObjectQuery queryObj = new ObjectQuery( "CCMenuOrder" );
			QueryResponse qrMenuItems = m_objectBean.Query( info, queryObj );		
			RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryObj.getClassName() );
            JSONArray orders = new JSONArray();
			for( int i = 0; i < oMenuItems.count(); i++ ) {
                RepositoryObject obj = oMenuItems.get(i);
                JSONObject jsonOrder = new JSONObject();
                jsonOrder.put("id", obj.getPropertyValue("id"));
                jsonOrder.put("status", convertStatusToLabel(obj.getPropertyValue_Int("status")));
                jsonOrder.put("label", obj.getPropertyValue("email"));
                jsonOrder.put("invoice", obj.getPropertyValue("subtotal"));
                jsonOrder.put("delivery", obj.getPropertyValue_Boolean("delivery"));
                orders.put(jsonOrder);
            }

            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            //out.write(jsonResponse.toCharArray());
            out.write(orders.toString());
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

    /*
    	<value text="0 - open" code="0"/>
		<value text="1 - payment_pending" code="1"/>
		<value text="2 - payment_failed" code="2"/>
		<value text="3 - processing" code="3"/>
		<value text="4 - out_for_devilery" code="4"/>
		<value text="5 - read_for_pickup" code="5"/>
		<value text="6 - processed" code="6"/>
        */
    String convertStatusToLabel(int status) {
        switch(status) {
            case 0: return "open";
            case 1: return "paymentpending";
            case 2: return "payment_failed";
            case 3: return "inprogress";
            case 4: return "readyforpickup";
            case 5: return "outfordelivery";
            case 6: return "complete";
        }
        return "open";
    }
}
