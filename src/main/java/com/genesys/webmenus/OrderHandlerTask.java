package com.genesys.webmenus;

import java.util.*;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.email.*;

public class OrderHandlerTask extends Thread
{
	public OrderHandlerTask()
	{
	}
	
	public void run()
	{
		ObjectManager objectBean = SystemServlet.getObjectManager();
		Credentials info = new Credentials();
		if( objectBean.SystemLogin("admin", info ) == true )
		{
			try
			{
				ObjectQuery queryOrders = new ObjectQuery( "CCMenuOrder" );
				queryOrders.setSortParams("order_time", "ASC");
				queryOrders.addProperty("notification_status", "<3");
				RepositoryObjectIterator orderIter = new RepositoryObjectIterator(objectBean.Query(info,queryOrders));
				while(orderIter.each())
				{
					// Get order object
					RepositoryObject oOrder = orderIter.getObj();
					String name = oOrder.getPropertyValue("location.name");
					//boolean email_orders = oOrder.getPropertyValue_Boolean("location.email_orders");
					boolean email_orders_pdf = oOrder.getPropertyValue_Boolean("location.email_orders_pdf");
					String email_addr = oOrder.getPropertyValue("location.email_addr");
					int notification_status = oOrder.getPropertyValue_Int("notification_status");
					int new_notification_status = notification_status;
					
					// Render order
					OrderRenderer order_renderer = new OrderRenderer(oOrder, info);
					order_renderer.renderOrder();
					String outText = order_renderer.getOrderText();
					String outHtml = order_renderer.getOrderHTML();

					if( notification_status == 0 || notification_status == 2)
					{
						try
						{
							//  Notify patron
							Vector toAddr = new Vector();
							toAddr.add(oOrder.getPropertyValue("email"));
							Outbound.postMail(toAddr, SystemServlet.getGenesysFromEmail(), email_addr, false, "Your order from " + name, outText, outHtml);
							new_notification_status |= 1;
						}
						catch(Exception e)
						{
							SystemServlet.g_logger.error( "OrderHandlerTask exception thrown notifying patron - " + e.getMessage() );
						}
					}

					if( notification_status == 0 || notification_status == 1)
					{
						try
						{
							// Order email sent to location mailbox
							Vector locAddr = new Vector();
							locAddr.add(email_addr);
							String systemFromEmail = SystemServlet.getGenesysFromEmail();
							
							if( email_orders_pdf ){
								// Send order notification as email attachment
								PdfGenerator pdf = new PdfGenerator();
								ByteArrayOutputStream os = pdf.generatePdf(oOrder, info);
								EmailAttachment attach = new EmailAttachment("order.pdf", "application/pdf", os.toByteArray());
								Outbound.postMailWithAttachment(locAddr, systemFromEmail, null, false,
										"Incoming Order for " + name, outText, outHtml, attach);
							}
							else{
								// Send order notification as normal email
								Outbound.postMail(locAddr, systemFromEmail, null, false, "Incoming Order for " + name, outText, outHtml);
							}
							new_notification_status |= 2;
						}
						catch(Exception e)
						{
							SystemServlet.g_logger.error( "OrderHandlerTask exception thrown notifying location - " + e.getMessage() );
						}
					}

					// Update notification status for this order - if changed
					if( notification_status != new_notification_status )
					{
						ObjectSubmit submitOrder = new ObjectSubmit("CCMenuOrder");
						submitOrder.addProperty("notification_status", new_notification_status);
						objectBean.Update(info, oOrder.getId(), submitOrder);
					}
				}
			}
			catch(Exception e)
			{
				SystemServlet.g_logger.error( "OrderHandlerTask exception thrown - " + e.getMessage() );
			}
			finally
			{
				objectBean.Logout(info);
			}
		}
	}
}
