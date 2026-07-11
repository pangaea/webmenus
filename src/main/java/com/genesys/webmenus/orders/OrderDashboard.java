package com.genesys.webmenus.orders;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.ObjectSubmit;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryException;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;
import com.genesys.util.ServletUtilities;
import com.genesys.util.xml.XMLStreamHelper;
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
		if( resPath != null ) {
			if( resPath.equalsIgnoreCase("/ping") )
			{
				request.getSession();	// Keep the session alive
				PrintWriter out = response.getWriter();
				response.addHeader("Content-Type", "text/xml; charset=utf-8");
				out.write(new ViewResponseWriter("ping", 0, "PONG").serialize());
			}
			else if( resPath.startsWith("/getorders/") )
			{
				String id = resPath.substring(11);
				Handle_Query( id, request, response );
			}
			else if( resPath.startsWith("/updateorder/") )
			{
				String id = resPath.substring(13);
				Handle_Update( id, request, response );
			}
			else if( resPath.equalsIgnoreCase("/cancelorder") )
			{
				//Handle_Cancel( request, response );
			}
			else
			{
				PrintWriter out = response.getWriter();
				out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
			}
		}
    }

    public void Handle_Query( String id, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		try
		{
            HttpSession thisSession = request.getSession();
            if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
            Credentials info = (Credentials)thisSession.getAttribute( "info" );

			JSONArray orders = new JSONArray();

			// Filter out closed orders
			ObjectQuery queryObj = new ObjectQuery( "CCMenuOrder" );
			queryObj.addProperty("status", "!= 6");
			queryObj.addProperty("location", id);
			queryObj.setSortBy("modified");
			queryObj.setSortOrder("ASC");
			//queryObj.setLogicalAnd(false);
			QueryResponse qrMenuItems = m_objectBean.Query( info, queryObj );		
			addOrderNodes(info, qrMenuItems.getObjects(queryObj.getClassName()), orders);
			// RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryObj.getClassName() );
			// for( int i = 0; i < oMenuItems.count(); i++ ) {
            //     RepositoryObject obj = oMenuItems.get(i);
            //     JSONObject jsonOrder = new JSONObject();
            //     jsonOrder.put("id", obj.getPropertyValue("id"));
            //     jsonOrder.put("status", convertStatusToLabel(obj.getPropertyValue_Int("status")));
            //     jsonOrder.put("label", obj.getPropertyValue("email"));
            //     jsonOrder.put("invoice", obj.getPropertyValue("subtotal"));
            //     jsonOrder.put("delivery", obj.getPropertyValue_Boolean("delivery"));
            //     orders.put(jsonOrder);
            // }

			// Query closed order within the 8 hours
			ObjectQuery queryObj2 = new ObjectQuery( "CCMenuOrder" );
			queryObj2.addProperty("status", "6");
			queryObj2.addProperty("location", id);
			queryObj2.setSortBy("modified");
			queryObj2.setSortOrder("ASC");
			LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
			String today = formatter.format(currentTime.minusHours(8));
			queryObj2.addProperty("modified", "> " + today);
			QueryResponse qrMenuItems2 = m_objectBean.Query( info, queryObj2 );		
			addOrderNodes(info, qrMenuItems2.getObjects(queryObj2.getClassName()), orders);
			// RepositoryObjects oMenuItems2 = qrMenuItems2.getObjects( queryObj2.getClassName() );
			// for( int i = 0; i < oMenuItems2.count(); i++ ) {
            //     RepositoryObject obj = oMenuItems2.get(i);
            //     JSONObject jsonOrder = new JSONObject();
            //     jsonOrder.put("id", obj.getPropertyValue("id"));
            //     jsonOrder.put("status", convertStatusToLabel(obj.getPropertyValue_Int("status")));
            //     jsonOrder.put("label", obj.getPropertyValue("email"));
            //     jsonOrder.put("invoice", obj.getPropertyValue("subtotal"));
            //     jsonOrder.put("delivery", obj.getPropertyValue_Boolean("delivery"));
            //     orders.put(jsonOrder);
            // }

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

	private String generateDescription(Credentials info, String orderId) throws AuthenticationException {
		ObjectQuery queryOrderItem = new ObjectQuery( "CCMenuOrderItem" );
		queryOrderItem.addProperty("menuorder", orderId);
		QueryResponse qrOrderItem = m_objectBean.Query(info, queryOrderItem );
		RepositoryObjects oOrderItems = qrOrderItem.getObjects( queryOrderItem.getClassName() );
		StringBuffer desc = new StringBuffer();
		for( int i = 0; i < oOrderItems.count(); i++ )
		{
			RepositoryObject oOrderItem = oOrderItems.get(i);

			if (desc.isEmpty()) {
				desc.append(oOrderItem.getPropertyValue("name"));
			} else {
				desc.append(", ");
				desc.append(oOrderItem.getPropertyValue("name"));
			}
		}
		return desc.toString();
	}

	private void addOrderNodes(Credentials info, RepositoryObjects oMenuItems, JSONArray orders) throws JSONException, AuthenticationException {
		for( int i = 0; i < oMenuItems.count(); i++ ) {
			RepositoryObject obj = oMenuItems.get(i);
			JSONObject jsonOrder = new JSONObject();
			jsonOrder.put("id", obj.getPropertyValue("id"));
			jsonOrder.put("status", OrderStatusUtil.convertStatusToLabel(obj.getPropertyValue_Int("status")));
			jsonOrder.put("label", generateDescription(info, obj.getPropertyValue("id")));
			jsonOrder.put("invoice", obj.getPropertyValue("invoice"));
			jsonOrder.put("delivery", obj.getPropertyValue_Boolean("delivery"));
			jsonOrder.put("estimated_time", obj.getPropertyValue("estimated_time"));
			jsonOrder.put("notes", obj.getPropertyValue("notes"));
			orders.put(jsonOrder);
		}
	}

	public void Handle_Update( String id, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException {
		HttpSession thisSession = request.getSession();
		if( thisSession.getAttribute( "info" ) == null ) throw new IOException("Missing credentials");
		Credentials info = (Credentials)thisSession.getAttribute( "info" );

		ObjectSubmit order = new ObjectSubmit("CCMenuOrder");
		JsonNode node = ServletUtilities.extractJsonBody(request);
		order.addProperty("status", OrderStatusUtil.convertLabelToStatus(node.get("status").asText()));
		JsonNode inv = node.get("invoice");
		if (inv != null) {
			order.addProperty("invoice", inv.asText());
		}
		JsonNode notes = node.get("notes");
		if (notes != null) {
			order.addProperty("notes", notes.asText());
		}
		JsonNode est = node.get("estimated_time");
		if (est != null) {
			if (!est.asText().isBlank()) {
				order.addProperty("estimated_time", est.asText());
			} else {
				order.addProperty("estimated_time", null);
			}
		}
		try {
			m_objectBean.Update(info, id, order);
			response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"id\":\"" + id + "\"}");
		}
		catch(AuthenticationException ex) {
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex) {
			SystemServlet.g_logger.error( "Exception thrown updating order in {OrderDashboard::Handle_Update} - " + ex.getErrMsg() );
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
    // public static String convertStatusToLabel(int status) {
    //     switch(status) {
    //         case 0: return "open";
    //         case 1: return "paymentpending";
    //         case 2: return "payment_failed";
    //         case 3: return "inprogress";
    //         case 4: return "readyforpickup";
    //         case 5: return "outfordelivery";
    //         case 6: return "complete";
    //     }
    //     return "open";
    // }

	// int convertLabelToStatus(String label) {
    //     switch(label) {
    //         case "open": return 0;
    //         case "paymentpending": return 1;
    //         case "payment_failed": return 2;
    //         case "inprogress": return 3;
    //         case "readyforpickup": return 4;
    //         case "outfordelivery": return 5;
    //         case "complete": return 6;
    //     }
    //     return 0;
    // }
}
