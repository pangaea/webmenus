///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus;

import java.util.*;
//import java.io.PrintWriter;
//import java.io.StringWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.FactoryConfigurationError;

import org.apache.xerces.parsers.DOMParser;
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamWriter;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.RandomGUID;
import com.genesys.util.email.Outbound;
import com.genesys.util.xml.*;
import com.genesys.views.InterfaceCfg;

import org.w3c.dom.Document;

public class MenuOrderBean
{
	//private String itemId;
	private double 						taxRate;
	//private BigDecimal subTotal;
	private HashMap<String, OrderItem>	m_orderItemMap;
	private Vector<OrderItem>			m_orderItemList;
	private ObjectManager 				m_objectBean = null;
	private Vector<OpHours>				m_OpHoursList;
	private TimeZone					m_locTZ = null;
	private String						m_LocId = null;
	private String						m_theme = null;
	private String						m_themeTemplate = null;
	private String						m_locationName = null;
	private String						m_phoneNum = null;
	private String						m_emailAddr = null;
	private boolean					m_devlieryOffered = false;
	private boolean					m_emailOrders = false;
	private boolean					m_validated = false;
	private String						m_patronId = null;
	private String						m_patronEmail = null;
	private Credentials 				m_creds = null;
	private String						m_exitURL = null;
	private String						m_logo = null;
	//private String						m_menuwidth = null;
	private boolean					m_deliveryYes = false;
	private String						m_deliveryInfo = null;
	private HashMap<String, Vector<OpHours>>	m_opHoursMap;

	public MenuOrderBean()
	{
		m_orderItemMap = new HashMap<String, OrderItem>();
		m_orderItemList = new Vector<OrderItem>();
		m_OpHoursList = new Vector<OpHours>();
		m_LocId = new String("");
		m_theme = new String("");
		m_exitURL = new String("");
		m_logo = new String("");
		//m_menuwidth = new String("100%");
		m_deliveryInfo = new String("");
		m_opHoursMap = new HashMap<String, Vector<OpHours>>();
		
		m_objectBean = SystemServlet.getObjectManager();
	}
	
	public Credentials getCredentials()
	{
		return m_creds;
	}

	//public void destroy()
	protected void finalize()
	{
		exitObjMan();
	}
	
	public String getPatronId()
	{
		return m_patronId;
	}
	
	public String getPatronEmail()
	{
		return m_patronEmail;
	}
	public String getExitURL()
	{
		return m_exitURL;
	}
	public String getLogo()
	{
		return m_logo;
	}
	/*
	public String getMenuWidth()
	{
		if( m_menuwidth.length() > 0 )
			return m_menuwidth;
		else
			return "100%";
	}*/
	
	public synchronized boolean verifyObjManCreds()
	{
		if( m_creds == null )
		{
			Credentials info = new Credentials();
			if( m_objectBean.SystemLogin("guest", info ) == true )
			{
				m_creds = info;
				return true;
			}
			return false;
		}
		return true;
	}
	
	private synchronized void exitObjMan()
	{
		m_objectBean.Logout(m_creds);
		m_creds = null;
	}
	
	public String loginPatron( String email )
	{
		String retId = null;
		if( verifyObjManCreds() )
		{
			try
			{
				ObjectQuery queryPatron = new ObjectQuery( "CEPatron" );
				//queryPatron.setRequestLevel("2");
				queryPatron.addProperty("email", email);
				queryPatron.addProperty("role", m_creds.m_RoleId);
				QueryResponse qrPatron = m_objectBean.Query( m_creds, queryPatron );
				RepositoryObjects oPatrons = qrPatron.getObjects( queryPatron.getClassName() );
				if( oPatrons.count() > 0 )
				{
					RepositoryObject oPatron = oPatrons.get(0);
					retId = new String(oPatron.getPropertyValue("id"));
					m_validated = true;
					
					m_patronId = retId;
					m_patronEmail = email;
				}
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
		}
		return retId;
	}
	
	public String createPatron( HttpServletRequest request ) throws AuthenticationException, RepositoryException
	{
		String retId = null;
		if( verifyObjManCreds() )
		{
			//ObjectQuery queryPatron = new ObjectQuery( "CEPatron" );
			ObjectSubmit submitStmt = new ObjectSubmit("CEPatron");
			InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
			InterfaceCfg.View viewNode = interfaceCfg.getView("patrons");
			if(viewNode!=null)
			{
				List<InterfaceCfg.View.Input> _inputs = viewNode.getInputs();
				Iterator<InterfaceCfg.View.Input> _iter_inputs = _inputs.iterator();
				while( _iter_inputs.hasNext() )
				{
					InterfaceCfg.View.Input input = _iter_inputs.next();
					String visible = input.getVisible();
					if( visible.equalsIgnoreCase("false") == true ) continue;
					String field = input.getField();
					String value = (String)request.getParameter(field);
					submitStmt.addProperty(field, value);
				}
			}
			
			String newId = null;
			try
			{
				newId = m_objectBean.Insert(m_creds, submitStmt);
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
				throw ex;
			}
			catch(RepositoryException ex)
			{
				SystemServlet.g_logger.error( "Exception thrown in {MenuOrderBean::createPatron} - " + ex.getErrMsg() );
				throw ex;
			}
			
			if( newId != null )
			{
				retId = newId;
				m_validated = true;
				
				m_patronId = retId;
				m_patronEmail = (String)request.getParameter("email");
			}
			//RepositoryObjects oPatrons = qrPatron.getObjects( queryPatron.getClassName() );
			//if( oPatrons.count() > 0 )
			//{
			//	RepositoryObject oPatron = oPatrons.get(0);
			//	retId = new String(oPatron.getPropertyValue("id"));
			//	m_validated = true;
			//}
			//m_objectBean.Logout(info);
		}
		return retId;
	}

	public java.util.Date getCurrentLocationTime()
	{
		//SimpleTimeZone omtz = m_locTZ;		

		//Calendar omTime = new GregorianCalendar(omtz);
		//omTime.setTime(new Date());
		//return omTime.getTime();

		TimeZone tz = TimeZone.getDefault();
		Date locTime = new Date();
		Date ret = new Date( locTime.getTime() + (m_locTZ.getRawOffset()-tz.getRawOffset()) );

		// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
		if ( m_locTZ.inDaylightTime( ret ))
		{
			Date dstDate = new Date( ret.getTime() + (m_locTZ.getDSTSavings()-tz.getDSTSavings()) );

			// check to make sure we have not crossed back into standard time
			// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
			if ( m_locTZ.inDaylightTime( dstDate ))
			{
				ret = dstDate;
			}
		}
		return ret;
	}
	
	public void setCurrentLocation( String sLocId )
	{
		if( verifyObjManCreds() )
		{
			try
			{
				// Pull informaion about location from obj man
				ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
				queryLoc.addProperty("id", sLocId);
				QueryResponse qrLoc = m_objectBean.Query( m_creds, queryLoc );
				RepositoryObjects oLocs = qrLoc.getObjects( queryLoc.getClassName() );
				if( oLocs.count() == 0 ) return;	// Invalid location id
				setCurrentLocation( oLocs.get(0) );
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
		}
	}

	public void setCurrentLocation( RepositoryObject oLoc )
	{
		m_LocId = oLoc.getId();

		if( verifyObjManCreds() )
		{
			try
			{
				// Pull informaion about location from obj man
//				ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
//				queryLoc.addProperty("id", sLocId);
//				QueryResponse qrLoc = m_objectBean.Query( m_creds, queryLoc );
//				RepositoryObjects oLocs = qrLoc.getObjects( queryLoc.getClassName() );
//				if( oLocs.count() == 0 ) return;	// Invalid location id
//				RepositoryObject oLoc = oLocs.get(0);
				String temp = oLoc.getPropertyValue("tax");
				m_locationName = oLoc.getPropertyValue("name");
				m_phoneNum = oLoc.getPropertyValue("phone_num");
				m_emailAddr = oLoc.getPropertyValue("email_addr");
				BigDecimal bdTaxRate = new BigDecimal(oLoc.getPropertyValue("tax"));
				this.taxRate = bdTaxRate.doubleValue();
				String sTZ = oLoc.getPropertyValue("timezone");
				m_locTZ = TimeZone.getTimeZone(sTZ);
				m_theme = oLoc.getPropertyValue("theme.id");
				m_themeTemplate = oLoc.getPropertyValue("theme.template");
				m_exitURL = oLoc.getPropertyValue("exit_url");
				m_logo = oLoc.getPropertyValue("logo");
				//m_menuwidth = oLoc.getPropertyValue("theme.menuwidth");
				m_devlieryOffered = oLoc.getPropertyValue_Boolean("delivery_avail");
				m_emailOrders = oLoc.getPropertyValue_Boolean("email_orders");
				String sRole = oLoc.getPropertyValue("role");
				
				// H.I.V.E.
				// Use the location role to for all objects
				m_creds.m_RoleId = sRole;
				////////////////////////////
	
//				// Query for the ophours associated to the selected location
//				m_OpHoursList.clear();
//				ObjectQuery queryHours = new ObjectQuery( "CCOpHours" );
//				queryHours.addProperty("location", sLocId);
//				QueryResponse qrHours = m_objectBean.Query( m_creds, queryHours );
//				RepositoryObjects oHours = qrHours.getObjects( queryHours.getClassName() );
//				for( int i = 0; i < oHours.count(); i++ )
//				{
//					RepositoryObject oOpTime = oHours.get( i );
//					
//					OpHours hours = new OpHours();
//					//hours.setWeekday(oOpTime.getPropertyValue("weekday"));
//					long weekdays = 0;
//					if( oOpTime.getPropertyValue("on_sunday").equalsIgnoreCase("Y") ) weekdays |= OpHours.SUNDAY;
//					if( oOpTime.getPropertyValue("on_monday").equalsIgnoreCase("Y") ) weekdays |= OpHours.MONDAY;
//					if( oOpTime.getPropertyValue("on_tuesday").equalsIgnoreCase("Y") ) weekdays |= OpHours.TUESDAY;
//					if( oOpTime.getPropertyValue("on_wednesday").equalsIgnoreCase("Y") ) weekdays |= OpHours.WEDNESDAY;
//					if( oOpTime.getPropertyValue("on_thursday").equalsIgnoreCase("Y") ) weekdays |= OpHours.THURSDAY;
//					if( oOpTime.getPropertyValue("on_friday").equalsIgnoreCase("Y") ) weekdays |= OpHours.FRIDAY;
//					if( oOpTime.getPropertyValue("on_saturday").equalsIgnoreCase("Y") ) weekdays |= OpHours.SATURDAY;
//					hours.setWeekdays(weekdays);
//					hours.setStartTime(oOpTime.getPropertyValue("start_time"));
//					String sHours = oOpTime.getPropertyValue("hours");
//					String sMinutes = oOpTime.getPropertyValue("minutes");
//					hours.setMinLen(sHours, sMinutes);
//					
//					// Assign menus to the operations hour object
//					RepositoryObjectRefList menuList = oOpTime.getPropertyObjectRefs("menus");
//					for( int ii = 0; ii < menuList.count(); ii++ )
//					{
//						RepositoryObjectRef objRef = menuList.get(ii);
//						ObjectProperty prop = objRef.getProperty("name");
//						hours.addMenu( objRef.getId(), prop.getText() );
//					}
//					
//					// Add operating hours to location
//					m_OpHoursList.add( hours );
//				}
				
				
				// Query for the ophours associated to the selected location
				m_opHoursMap.clear();
				ObjectQuery queryMenus = new ObjectQuery("CCMenu");
				queryMenus.addProperty("location", m_LocId);
				QueryResponse qrMenus = m_objectBean.Query( m_creds, queryMenus );
				RepositoryObjects oMenus = qrMenus.getObjects( queryMenus.getClassName() );
				for( int i = 0; i < oMenus.count(); i++ )
				{
					RepositoryObject oMenu = oMenus.get( i );
					Vector<OpHours> opHours = new Vector<OpHours>();
					m_opHoursMap.put( oMenu.getId(), opHours );
					int take_orders = oMenu.getPropertyValue_Int("take_orders");
					switch(take_orders){
					case 2:	// Use schedule
						// Query for the ophours associated to the selected menu
						ObjectQuery queryHours = new ObjectQuery("CCOpHours");
						queryHours.addProperty("schedule", oMenu.getPropertyValue("schedule"));
						QueryResponse qrHours = m_objectBean.Query( m_creds, queryHours );
						RepositoryObjects oHours = qrHours.getObjects( queryHours.getClassName() );
						for( int ii = 0; ii < oHours.count(); ii++ )
						{
							RepositoryObject oOpTime = oHours.get( ii );
						
							OpHours hours = new OpHours();
							//hours.setWeekday(oOpTime.getPropertyValue("weekday"));
							long weekdays = 0;
							if( oOpTime.getPropertyValue("on_sunday").equalsIgnoreCase("Y") ) weekdays |= OpHours.SUNDAY;
							if( oOpTime.getPropertyValue("on_monday").equalsIgnoreCase("Y") ) weekdays |= OpHours.MONDAY;
							if( oOpTime.getPropertyValue("on_tuesday").equalsIgnoreCase("Y") ) weekdays |= OpHours.TUESDAY;
							if( oOpTime.getPropertyValue("on_wednesday").equalsIgnoreCase("Y") ) weekdays |= OpHours.WEDNESDAY;
							if( oOpTime.getPropertyValue("on_thursday").equalsIgnoreCase("Y") ) weekdays |= OpHours.THURSDAY;
							if( oOpTime.getPropertyValue("on_friday").equalsIgnoreCase("Y") ) weekdays |= OpHours.FRIDAY;
							if( oOpTime.getPropertyValue("on_saturday").equalsIgnoreCase("Y") ) weekdays |= OpHours.SATURDAY;
							hours.setWeekdays(weekdays);
							hours.setStartTime(oOpTime.getPropertyValue("start_time"));
							String sHours = oOpTime.getPropertyValue("hours");
							String sMinutes = oOpTime.getPropertyValue("minutes");
							hours.setMinLen(sHours, sMinutes);
						
							// Add operating hours to location
							opHours.add( hours );
						}
						break;
					case 1: // Always open
						OpHours hours = new OpHours();
						hours.setWeekdays(OpHours.SUNDAY|OpHours.MONDAY|OpHours.TUESDAY|
										  OpHours.WEDNESDAY|OpHours.THURSDAY|OpHours.FRIDAY|OpHours.SATURDAY);
						hours.setStartTime("12:00 AM");
						hours.setMinLen("24", "0");
						opHours.add( hours );
						break;
					}
				}
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
		}
	}
	
	public String getCurrentLocationId()
	{
		return m_LocId;
	}
	
	private int getSundayOffset( int weekday, int hour, int minute)
	{
		 // Calculate minutes from Sunday @ midnight
		 int iSunOffset = weekday * (24*60);
		 iSunOffset += hour * 60;
		 iSunOffset += minute;
		 return iSunOffset;
	}

	public boolean isWithinOpertingHours()
	{
		return isWithinMenuOpertingHours("");
	}

	public boolean isWithinMenuOpertingHours(String menuId)
	{
		if( m_locTZ == null ) return false;
		 //SimpleTimeZone pdt = m_objectBean.GetObjManTimeZone();

		 // create a GregorianCalendar with the local time zone
		 // and the current date and time

		//SimpleTimeZone gmt = new SimpleTimeZone(0, "GMT");
		Calendar testTime = new GregorianCalendar(m_locTZ);
		testTime.setTime(getCurrentLocationTime());

		 // Calculate minutes from Sunday @ midnight
		int iSunOffset = getSundayOffset( 	testTime.get(Calendar.DAY_OF_WEEK),
				 							testTime.get(Calendar.HOUR_OF_DAY),
				 							testTime.get(Calendar.MINUTE) );

		if( menuId != null && menuId.length() > 0 ){
			Vector<OpHours> hours = m_opHoursMap.get(menuId);
			if( hours != null){
				if(isWithinOpertaingHours(hours, iSunOffset)) return true;
			}
		} else {
			Iterator it = m_opHoursMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				Vector<OpHours> hours = (Vector<OpHours>)pairs.getValue();
				if(isWithinOpertaingHours(hours, iSunOffset)) return true;
			}
		}
		
		return false;
	}
	
	boolean isWithinOpertaingHours(Vector<OpHours> opHours, int iSunOffset)
	{
		Iterator itr = opHours.iterator();
		while(itr.hasNext()){
		
			OpHours hours = (OpHours)itr.next();
			
			// Verify this specific menu Id is included in this ophours object
			for( int ii = 0; ii < 7; ii++ )
			{
				// Get weekday from bitmask and determine if this HOOP is set
				int istartWeekday = (int)Math.pow(2, ii);
				if( hours.isWeekdaySet(istartWeekday) == false ) continue;
				//int istartWeekday = hours.getWeekday();
				
				// Determine start time as offset from Sunday and midnight
				Calendar startTime = new GregorianCalendar(m_locTZ);
				startTime.setTime(hours.getStartTime());
				int startHour = startTime.get(Calendar.HOUR_OF_DAY);
				int startMin = startTime.get(Calendar.MINUTE);
				int iSunOffsetStart = getSundayOffset( ii + 1, startHour, startMin );
				
				// Determine end time as offset from Sunday at midnight
				int iSunOffsetEnd = iSunOffsetStart + hours.getMinLen();
				
				// Verify if the current time is within this interval
				if( iSunOffset >= iSunOffsetStart && iSunOffset < iSunOffsetEnd )
					return true;
				
				// Look for "over Saturday night" scenario
				int iMinsInWeek = (24*7*60);
				if( iSunOffsetEnd > iMinsInWeek)
				{
					int iNextSunOffset = iSunOffset + iMinsInWeek;
					
					// Verify if the current time is within this interval
					if( iNextSunOffset >= iSunOffsetStart && iNextSunOffset < iSunOffsetEnd )
						return true;
				}
			}
		}
		return false;
	}

	public double getTaxRate()
	{
		return this.taxRate;
	}
	
	public String getLocationName(){ return this.m_locationName; }
	public String getPhoneNumber(){ return this.m_phoneNum; }
	public String getEmailAddress(){ return this.m_emailAddr; }
	
	public boolean isValidated()
	{
		return m_validated;
	}
	
	public boolean submitNewItem(HttpServletRequest request)
	{
		String sizeId = request.getParameter("sizeId");
		
		if( verifyObjManCreds() )
		{
			try
			{
				// Query for Item size
				ObjectQuery queryMenuItemSize = new ObjectQuery( "CCMenuItemSize" );
				queryMenuItemSize.addProperty("id",sizeId);
				QueryResponse qrMenuItemSizes = m_objectBean.Query( m_creds, queryMenuItemSize );
				RepositoryObjects oMenuItemSizes = qrMenuItemSizes.getObjects( queryMenuItemSize.getClassName() );
				if( oMenuItemSizes.count() == 0 )
				{
					return false;
				}
				RepositoryObject oMenuItemSize = oMenuItemSizes.get(0);
				//String sMenuItemPrice = oMenuItemSize.getPropertyValue("price");
				BigDecimal bdPrice = new BigDecimal(oMenuItemSize.getPropertyValue("price"));
				String sMenuItemSizeDesc = oMenuItemSize.getPropertyValue("size_desc");
				String sMenuItemId = oMenuItemSize.getPropertyValue("menuitem");
				if( sMenuItemId.equalsIgnoreCase("null") == true || sMenuItemId.length() == 0 )
					sMenuItemId = sizeId;
				
				// Query Menu Item
				ObjectQuery queryMenuItem = new ObjectQuery( "CCMenuItem" );
				queryMenuItem.addProperty("id",sMenuItemId);
				QueryResponse qrMenuItems = m_objectBean.Query( m_creds, queryMenuItem );
				RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryMenuItem.getClassName() );
				if( oMenuItems.count() > 0 )
				{
					RepositoryObject oMenuItem = oMenuItems.get(0);
					String sMenuItemName = oMenuItem.getPropertyValue("name");
					//if( sMenuItemSizeDesc.length() > 0 )
					//	sMenuItemName = sMenuItemName + " (" + sMenuItemSizeDesc + ")";
					String sMenuItemDesc = oMenuItem.getPropertyValue("description");
					//BigDecimal bdPrice = new BigDecimal(oMenuItem.getPropertyValue("price"));
					String sMenuCategory = oMenuItem.getPropertyValue("menucategory");
					ObjectQuery queryMenuCat = new ObjectQuery( "CCMenuCategory" );
					queryMenuCat.addProperty("id", sMenuCategory);
					QueryResponse qrMenuCats = m_objectBean.Query( m_creds, queryMenuCat );
					RepositoryObjects oMenuCats = qrMenuCats.getObjects( queryMenuCat.getClassName() );
					if( oMenuCats.count() == 0 )
					{
						return false;
					}
					RepositoryObject oMenuCategory = oMenuCats.get(0);
					String sMenuId = oMenuCategory.getPropertyValue("menu");
					if( isWithinMenuOpertingHours(sMenuId) == false )
					{
						return false;
					}
					
					// Build options list
					String sOptions = new String("");
					ObjectQuery queryOptions = new ObjectQuery( "CCMenuItemOption" );
					queryOptions.addProperty("menuitem",sMenuItemId);
					QueryResponse qrOptions = m_objectBean.Query( m_creds, queryOptions );
					RepositoryObjects oOptions = qrOptions.getObjects( queryOptions.getClassName() );
					for( int i = 0; i < oOptions.count(); i++ )
					{
						RepositoryObject obj = oOptions.get( i );
						String sOptionName = obj.getPropertyValue("name");
						String sOptionType = obj.getPropertyValue("type");
						BigDecimal bdOptionPrice = new BigDecimal(obj.getPropertyValue("price"));
						if( sOptionType.equalsIgnoreCase("select") == true )
						{
							String sSelections = new String("");
							String sData = obj.getPropertyValue("data");
							String results[] = sData.trim().split("\n");
							for(int ii =0; ii < results.length; ii++)
							{
								String sOptionTxt = results[ii].trim();
								if( sOptionTxt.length() == 0 ) continue;
								if( request.getParameter(sOptionName+ii) != null )
								{
									bdPrice = bdPrice.add(bdOptionPrice);
									String opt = sOptionTxt;
									if( sSelections.length() > 0 )
										sSelections += ", " + opt;
									else
										sSelections += opt;
								}
							}
							if( sSelections.length() > 0 )
							{
								if( sOptions.length() > 0 ) sOptions += "\r\n";
								//sOptions += "<strong>";
								sOptions += sOptionName + ": ";
								sOptions += sSelections;
							}
						}
						else
						{
							String sOptionTxt = (String)request.getParameter(sOptionName);
							if( sOptionTxt != null && sOptionTxt.length() > 0 && sOptionTxt.equalsIgnoreCase("_none_") == false )
							{
								bdPrice = bdPrice.add(bdOptionPrice);
								if( sOptions.length() > 0 ) sOptions += "\r\n";
								//sOptions += "<strong>";
								sOptions += sOptionName + ": ";
								sOptions += sOptionTxt;// + "</em>";
							}
						}
					}
					///////////////////////////////////////////
					
					// Add new item to order
					int iQuantity = 1;
					String sQuantity = (String)request.getParameter("quantity");
					if( sQuantity != null )
					{
						try
						{
							iQuantity = new Long(sQuantity).intValue();
						}
						catch(NumberFormatException e)
						{
							iQuantity = 1;
						}
					}
	
					// Get size value of this item
					//String sSize = oMenuItem.getPropertyValue("size_desc");
					
					RandomGUID guid = new RandomGUID();
					OrderItem item = new OrderItem(guid.toString(), sMenuItemName, sMenuItemDesc, sMenuItemSizeDesc, bdPrice, iQuantity, sOptions);
	
					// Add new order item to order
					m_orderItemMap.put( guid.toString(), item );
					m_orderItemList.add( item );
	
					//BigDecimal bdSumPrice = bdPrice.multiply(new BigDecimal(lQuantity));
					//subTotal = subTotal.add(bdSumPrice);
				}
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
		}
		return true;
	}
	public String getTheme()
	{
		return m_theme;
	}
	public String getThemeTemplate()
	{
		return m_themeTemplate;
	}
	public boolean isDeliveryAvailable()
	{
		return m_devlieryOffered;
	}
	public double getSubTotal()
	{
		BigDecimal subTotal = new BigDecimal(0);
		for( int i = 0; i < itemCount(); i++ )
		{
			OrderItem item = getItemByIndex(i);
			double dPrice = item.getPrice();
			int iQuantity = item.getQuantity();
			BigDecimal bdSumPrice = new BigDecimal(dPrice * iQuantity);
			subTotal = subTotal.add(bdSumPrice);
		}

		// Display sub total
		BigDecimal adjSubtotal = subTotal.setScale(2,BigDecimal.ROUND_HALF_UP);
		return adjSubtotal.doubleValue();
	}
	public String getSubTotalStr()
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(getSubTotal());
	}
	public double getTaxTotal()
	{
		// Calculate total using tax rate
		BigDecimal rate = new BigDecimal(this.taxRate);
		BigDecimal adjrate = rate.divide(new BigDecimal(100));
		BigDecimal subTotal = new BigDecimal(getSubTotal());
		BigDecimal tax = subTotal.multiply(adjrate);
		return tax.doubleValue();
	}
	public String getTaxTotalStr()
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(getTaxTotal());
	}
	public double getTotal()
	{
		// Calculate total using tax rate
		BigDecimal rate = new BigDecimal(this.taxRate);
		BigDecimal adjrate = rate.divide(new BigDecimal(100));
		BigDecimal subTotal = new BigDecimal(getSubTotal());
		BigDecimal tax = subTotal.multiply(adjrate);
		BigDecimal total = subTotal.add(tax).setScale(2,BigDecimal.ROUND_HALF_UP);
		return total.doubleValue();
	}
	public String getTotalStr()
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(getTotal());
	}
	public String getCurrencyString(double price)
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		return n.format(price);
	}
	public String getCurrencyString(String price)
	{
		BigDecimal bdTaxRate = new BigDecimal(price);
		return getCurrencyString(bdTaxRate.doubleValue());
	}
	public int itemCount()
	{
		return m_orderItemList.size();
	}

	public OrderItem getItemByIndex( int index )
	{
		try
		{
			return (OrderItem)m_orderItemList.get( index );
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public OrderItem getItemById( String id )
	{
		return (OrderItem)m_orderItemMap.get( id );
	}
	
	public void removeItemByIndex( int index )
	{
		OrderItem item = m_orderItemList.remove(index);
		m_orderItemMap.remove(item.getId());
	}
/*
	private static Date cvtToGmt( Date date )
	{
	   TimeZone tz = TimeZone.getDefault();
	   Date ret = new Date( date.getTime() - tz.getRawOffset() );

	   // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
	   if ( tz.inDaylightTime( ret ))
	   {
	      Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

	      // check to make sure we have not crossed back into standard time
	      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
	      if ( tz.inDaylightTime( dstDate ))
	      {
	         ret = dstDate;
	      }
	   }

	   return ret;
	}
*/
	
	public Vector<String> getPartonDeliveryAddresses()
	{
		Vector<String> prevDelAddr = new Vector<String>();
		if( verifyObjManCreds() )
		{
			try
			{
				// Query for Item size
				ObjectQuery queryMenuOrder = new ObjectQuery( "CCMenuOrder" );
				queryMenuOrder.addProperty("email",m_patronEmail);
				queryMenuOrder.addProperty("delivery","Y");
				queryMenuOrder.setSortBy("order_time");
				queryMenuOrder.setSortOrder("DESC");
				queryMenuOrder.setCount(10);
				QueryResponse qrMenuOrders = m_objectBean.Query( m_creds, queryMenuOrder );
				RepositoryObjects oMenuOrders = qrMenuOrders.getObjects( queryMenuOrder.getClassName() );
				for( int i = 0; i < oMenuOrders.count(); i++ )
				{
					RepositoryObject oMenuOrder = oMenuOrders.get(i);
					String devlivInfo = oMenuOrder.getPropertyValue("delivery_info").trim();
					if( devlivInfo.length() > 0 && prevDelAddr.indexOf(devlivInfo) < 0 )
						prevDelAddr.add(devlivInfo);
				}
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
		}
		
		return prevDelAddr;
	}
	
	public void setDeliveryAddress( String deliveryInfo )
	{
		m_deliveryInfo = deliveryInfo;//.replaceAll("\r\n", "\n");
		m_deliveryYes = true;
	}
/*
	public Vector<String> setDeliveryAddress( String address, String city, String state, String zip, String contactNum )
	{
		Vector<String> errors = new Vector<String>();
		
		// TODO: validate all delivery fields
		if( address == null || address.length() == 0 )
			errors.add("Address is missing");
		
		if( city == null || city.length() == 0 )
			errors.add("City is missing");
		
		if( state == null || state.length() == 0 )
			errors.add("State is missing");
		
		if( zip == null || zip.length() == 0 )
			errors.add("Zip is missing");
		else if( zip.matches("^(\\d{5}-\\d{4})|(\\d{5})$") == false )
			errors.add("Zip code is invalid");
			
		if( contactNum == null || contactNum.length() == 0 )
			errors.add("Contact number is missing");
		else if( contactNum.matches("^(1\\s*[-\\/\\.]?)?(\\((\\d{3})\\)|(\\d{3}))\\s*[-\\/\\.]?\\s*(\\d{3})\\s*[-\\/\\.]?\\s*(\\d{4})\\s*(([xX]|[eE][xX][tT])\\.?\\s*(\\d+))*$") == false )
			errors.add("Contact number is invalid");
		
		if( errors.size() == 0 )
		{
			StringBuffer sbDInfo = new StringBuffer();
			sbDInfo.append(address);
			sbDInfo.append("\n");
			sbDInfo.append(city);
			sbDInfo.append(", ");
			sbDInfo.append(state);
			sbDInfo.append(" ");
			sbDInfo.append(zip);
			sbDInfo.append("\n");
			sbDInfo.append(contactNum);
			sbDInfo.append("\n\n");
			m_deliveryInfo = sbDInfo.toString();
			m_deliveryYes = true;
		}
		return errors;
	}
*/
	public boolean submitOrder()
	{
		if( verifyObjManCreds() )
		{
			ObjectSubmit order = new ObjectSubmit("CCMenuOrder");
			try
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				String today = dateFormat.format(getCurrentLocationTime());
				order.addProperty("order_time", today);
				order.addProperty("email", m_patronEmail);
				order.addProperty("subtotal", getSubTotal());
				order.addProperty("tax_rate", getTaxRate());
				order.addProperty("tax", getTaxTotal());
				order.addProperty("total", getTotal());
				order.addProperty("location", m_LocId);
				order.addProperty("delivery", m_deliveryYes);
				order.addProperty("delivery_info", m_deliveryInfo);
				order.addProperty("fulfilled", false);
				order.addProperty("notification_status", (m_emailOrders)?0:2);
				
				String order_id = null;
				try
				{
					order_id = m_objectBean.Insert(m_creds, order);
				}
				catch(AuthenticationException ex)
				{
					SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
				}
				catch(RepositoryException ex)
				{
					SystemServlet.g_logger.error( "Exception thrown inserting order in {MenuOrderBean::submitOrder} - " + ex.getErrMsg() );
					return false;
				}
	
				for( int i = 0; i < m_orderItemList.size(); i++ )
				{
					OrderItem item = (OrderItem)m_orderItemList.get( i );
					ObjectSubmit order_item = new ObjectSubmit("CCMenuOrderItem");
					order_item.addProperty("name", item.getName());
					order_item.addProperty("description", item.getDesc());
					order_item.addProperty("options", item.getOptions());
					order_item.addProperty("size", item.getSize());
					order_item.addProperty("price", item.getPrice());
					order_item.addProperty("quantity", item.getQuantity());
					order_item.addProperty("menuorder", order_id);
					
					try
					{
						m_objectBean.Insert(m_creds, order_item);
					}
					catch(AuthenticationException ex)
					{
						SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
					}
					catch(RepositoryException ex)
					{
						SystemServlet.g_logger.error( "Exception thrown inserting order item in {MenuOrderBean::submitOrder} - " + ex.getErrMsg() );
						return false;
					}
				}
				
				// Start order processing
				
				
				////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////
//				OrderProcessor orderproc = new OrderProcessor(order_id, m_creds.m_RoleId);
//				SystemServlet.processTask(orderproc);
				//orderproc.start();
			}
			catch(FactoryConfigurationError e)
			{
				SystemServlet.g_logger.error(e.getMessage());
				//e.printStackTrace();
			}

			// Clear items from order after successful submission
			m_orderItemMap.clear();
			m_orderItemList.clear();
		}

		return true;
	}
}
