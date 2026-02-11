///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;
/*
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import java.util.Date;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;
import com.genesys.util.xml.XMLDocument;
import com.genesys.util.xml.XMLNode;
import com.genesys.util.xml.XMLNodeList;
import com.genesys.util.xsl.XSLParser;
import com.genesys.util.email.Outbound;
*/
class OrderProcessor extends Thread
{/*
	private OrderRenderer 	m_Renderer = null;
	private String 			m_orderId, m_roleId;

	public OrderProcessor(String orderId, String roleId)
	{
		m_orderId = orderId;
		m_roleId = roleId;
	}

	public void run()
	{
		// Send email
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			Credentials info = new Credentials();
			if( objectBean.SystemLogin("guest", info ) == true )
			{
				try
				{
					// Sync up roles with location
					info.m_RoleId = m_roleId;
					
					// Query order object
					ObjectQuery queryOrder = new ObjectQuery( "CCMenuOrder" );
					queryOrder.addProperty("id", m_orderId);
					QueryResponse qrOrder = objectBean.Query( info, queryOrder );
					RepositoryObjects oOrders = qrOrder.getObjects( queryOrder.getClassName());
					if( oOrders.count() == 0 )
					{
						SystemServlet.g_logger.error( "Id not found while processing order [" + m_orderId + "]" );
						return;
					}
					RepositoryObject oOrder = oOrders.get(0);
					
					
					m_Renderer = new OrderRenderer(oOrder, info);
					m_Renderer.renderOrder();
					
					String outText = m_Renderer.getOrderText();
					String outHtml = m_Renderer.getOrderHTML();
					
					// Pull location information
					String LocName = oOrder.getPropertyValue("location.name");
					String LocEmailAddr = oOrder.getPropertyValue("location.email_addr");
		
					// Create email recipient list
					Vector toAddr = new Vector();
					toAddr.add(oOrder.getPropertyValue("email"));
					//String toAddr[] = new String[2];
					//toAddr[0] = oOrder.getPropertyValue("email");
					Outbound.postMail(toAddr, LocEmailAddr, false, "Your order from " + LocName, outText, outHtml);
		
					if( oOrder.getPropertyValue("location.email_orders").equalsIgnoreCase("Y") )
					{
						// Order email sent to location mailbox
						Vector locAddr = new Vector();
						locAddr.add(LocEmailAddr);
						String systemFromEmail = SystemServlet.getGenesysFromEmail();
						Outbound.postMail(locAddr, systemFromEmail, false, "Incoming Order for " + LocName, outText, outHtml);
					}
				}
				catch(Exception e1)
				{
					
				}
				finally
				{
					objectBean.Logout(info);
				}
			}
		}
		catch(Exception e)
		{
			SystemServlet.g_logger.error( "Exception caught trying to post new order - " + e.getMessage() );
		}
	}*/
};