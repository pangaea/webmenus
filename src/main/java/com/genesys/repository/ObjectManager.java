package com.genesys.repository;

//import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Vector;
//import java.sql.Date;
//import java.util.*;
import javax.script.*;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.genesys.SystemServlet;
import com.genesys.db.*;
import com.genesys.repository.script.*;
import com.genesys.repository.sql.*;
import com.genesys.repository.sql.StatementBuilder.PropType;
import com.genesys.util.RandomGUID;
import com.genesys.util.xml.*;

import com.twmacinta.util.*;

public class ObjectManager
{
	//private XMLDocument m_serverCFG;//, m_taxonomyCFG;
	private StatementBuilder m_StmtBldr;
	private ScriptHandler m_scriptHandler;
	private String m_scriptsRoot;
	private Connection m_db;
	//private JDCConnectionPool	m_connPool;
	private java.util.Map m_clientMap;
	private java.util.HashMap m_classNodes;
	private boolean m_bInitialized;
	
	// Object manager timezone
	//private SimpleTimeZone m_ObjManTZ;

	//////////////Hard-coded SQL /////////////////////////
	/////// This should be converted to a template ///////
	/////////////////////////////////////////////////////
	static final String m_szgetProfile =
		"select " +
		"T1.id as id, " +
		"T1.password as password, " +
		"T1.roleid as roleid, " +
		"T1.last_logged_in as last_logged_in, " +
		"T3.show_welcome as show_welcome, " +
		"T2.admin as admin " +
		"from " +
		"sys_client T1 " +
		"left join sys_role T2 on T1.roleid = T2.id " +
		"left join sys_user T3 on T1.id = T3.id " +
		"where T1.username='%s';";

	static final String m_szgetAccess =
		"select " +
		"T5.id as id, " +
		"T5.view as view, " +
		"T5.access as access " +
		"from " +
		"sys_client T1 " +
		"left join sys_role T2 on T1.roleid = T2.id " +
		"left join sys_role_group_ref T3 on T2.id=T3.roleid " +
		"left join sys_group T4 on T3.groupid=T4.id " +
		"left join sys_access T5 on T4.id=T5.groupid " +
		"where T1.username='%s';";
	
	static final String m_szupdateLastLoggedIn =
		"update sys_client set last_logged_in=UTC_TIMESTAMP() where username='%s';";

///////////////////////////////////////////////////////
//////////////////////////////////////////////////////
/////////////////////////////////////////////////////
	
	public ObjectManager()
	{
		m_bInitialized = false;
		m_clientMap = java.util.Collections.synchronizedMap(new java.util.HashMap());	// Multi-threaded
		m_classNodes = new java.util.HashMap();
		//String rootPath = System.getProperty("java.class.path");
		//String sysPropes = System.getenv("DOCUMENT_ROOT");
		String rootPath = SystemServlet.getGenesysHome();//System.getProperty("GENESYS_HOME");
		init( rootPath );
		//m_bInitialized = false;
	}
	
	public String generateReport()
	{
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		synchronized(m_clientMap)
		{
			html.append("<h2/>Java Runtime<h2/><hr/>");
			
			Runtime thisApp = Runtime.getRuntime();
			html.append("<table border='1' cellspacing='4' cellpadding='4'>");
			html.append("<tr><th>Available Processors</th><td>" + Long.toString(thisApp.availableProcessors()) + "</td></tr>");
			html.append("<tr><th>Free Memory</th><td>" + Long.toString(thisApp.freeMemory()) + " bytes</td></tr>");
			html.append("<tr><th>Max Memory</th><td>" + Long.toString(thisApp.maxMemory()) + " bytes</td></tr>");
			html.append("</table>");
			
			html.append("<h2/>Object Manager Diagnostics<h2/><hr/>");
			
			html.append("<table border='1' cellspacing='4' cellpadding='4'>");
			html.append("<tr><th>Client Count</th><td>" + Long.toString(m_clientMap.size()) + "</td></tr>");
			html.append("</table>");
			
			html.append("<br/>");
			html.append("<table border='1' cellspacing='4' cellpadding='4'>");
			html.append("<tr>");
			html.append("<th>User Name</th>");
			html.append("<th>Session Ticket</th>");
			html.append("<th>Role Name</th>");
			html.append("<th>System User</th>");
			html.append("</tr>");
	        Set blockKeys = m_clientMap.keySet();
	        Iterator It = blockKeys.iterator();
	        while(It.hasNext())
	        {
                String blockName = (String)(It.next());
                Credentials info = (Credentials)m_clientMap.get(blockName);
                html.append("<tr>");
                
                html.append("<td>");
                html.append(info.m_UserName);
                html.append("</td>");
                
                html.append("<td>");
                html.append(info.m_sTicket);
                html.append("</td>");
                
                html.append("<td>");
                html.append(info.getRole());
                html.append("</td>");
                
                html.append("<td>");
                html.append(info.m_bSystemUser ? "Y" : "N");
                html.append("</td>");
                
                html.append("</tr>");
	        }
	        html.append("</table>");
		}
		html.append("</body></html>");
		return html.toString();
	}

/*
	private String XmlEncodeString( String CharData )
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
	private void connectToDB()
	{
		try
		{
			//System.out.println("DEBUG: connectToDB ===> 111");
			SystemServlet.g_logger.debug( "connectToDB..." );
			String connStr = SystemServlet.getDBConnectStr();
			//System.out.println("DEBUG: connStr ===> " + connStr);
			SystemServlet.g_logger.debug(connStr);
			m_db = DriverManager.getConnection(connStr);
			
			//XMLDocument serverCFG = SystemServlet.getGenesysServerCfg();
			//String connStr = serverCFG.selectElementValue( "//database/connectstring" );
			//String dbName = serverCFG.selectElementValue( "//database/dbname" );
			//String userId = serverCFG.selectElementValue( "//database/userid" );
			//String password = serverCFG.selectElementValue( "//password" );
			//SystemServlet.g_logger.debug("jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password);
			//m_db = DriverManager.getConnection("jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password);
			
			// Set character encoding
			//Statement resultStmt = m_db.createStatement();
			//resultStmt.execute("SET NAMES 'utf8';");
			//resultStmt.close();
		}
		catch( SQLException ex )
		{
			// System.out.println("SQLException: " + ex.getMessage());
			// System.out.println("SQLState: " + ex.getSQLState());
			// System.out.println("VendorError: " + ex.getErrorCode());

			// handle any errors
			SystemServlet.g_logger.fatal("SQLException: " + ex.getMessage());
			SystemServlet.g_logger.fatal("SQLState: " + ex.getSQLState());
			SystemServlet.g_logger.fatal("VendorError: " + ex.getErrorCode());
		}
	}

	private Statement getDBStatement()
	{
		//System.out.println("DEBUG: getDBStatement ===> 111");
		Statement resultStmt;
		try
		{
			//System.out.println("DEBUG: getDBStatement ===> 222");
			if( m_db == null || m_db.isClosed() || m_db.isValid(0) == false )
			{
				connectToDB();
			}
			resultStmt = m_db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		catch(SQLException sqlex)
		{
			//System.out.println("DEBUG: getDBStatement ===> 333" + sqlex.getMessage());
			try
			{
				connectToDB();
				resultStmt = m_db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}
			catch(Exception e)
			{
				//System.out.println("DEBUG: getDBStatement ===> 333" + e.getMessage());
				return null;
			}
		}
		return resultStmt;
	}

	private PreparedStatement getDBPrepStatement(String query)
	{
		PreparedStatement pStmt;
		try
		{
			if( m_db.isClosed() || m_db.isValid(0) == false )
			{
				connectToDB();
			}
			pStmt = m_db.prepareStatement(query);
		}
		catch(SQLException sqlex)
		{
			try
			{
				connectToDB();
				pStmt = m_db.prepareStatement(query);
			}
			catch(Exception e)
			{
				return null;
			}
		}
		return pStmt;
	}

	public void init(String rootAppPath)
	{
		// Stop from re-initializing once up and running
		if( m_bInitialized ) return;
		
		try
		{
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}
		catch (Exception ex)
		{
			// handle the error
			SystemServlet.g_logger.fatal( "Failed to load drivers" );
		}

		try
		{
			XMLDocument serverCFG = SystemServlet.getGenesysServerCfg();
			//m_serverCFG.loadXML( rootAppPath + "WEB-INF/cfg/server.cfg" );
			if( serverCFG.isNull() ) SystemServlet.g_logger.fatal( "server.cfg failed to load from path " + rootAppPath );
/*
			// Load object manager timezone settings
			int timezoneOffset = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/offset" ));
			int startMonth = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/start-month" ));
			int startDay = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/start-day" ));
			int startDayOfWeek = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/start-dayofweek" ));
			int startTime = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/start-time" ));
			int endMonth = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/end-month" ));
			int endDay = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/end-day" ));
			int endDayOfWeek = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/end-dayofweek" ));
			int endTime = Integer.parseInt(m_serverCFG.selectElementValue( "//timezone/dst/end-time" ));
			
			// get the supported ids for timezoneOffset
			String[] ids = TimeZone.getAvailableIDs(timezoneOffset * 60 * 60 * 1000);
			// if no ids were returned, something is wrong. get out.
			if( ids.length > 0)
			{
				// create a Object Manager Time zone
				m_ObjManTZ = new SimpleTimeZone(timezoneOffset * 60 * 60 * 1000, ids[0]);
	
				// set up rules for daylight savings time
				m_ObjManTZ.setStartRule(startMonth, startDay, startDayOfWeek, startTime * 60 * 60 * 1000);
				m_ObjManTZ.setEndRule(endMonth, endDay, endDayOfWeek, endTime * 60 * 60 * 1000);
			}
			else
			{
				SystemServlet.g_logger.error( "Object Manager timezone cannot be calculated. Invalid timezone offset found in server.cfg" );
			}
*/
			XMLNodeList objCfgs = serverCFG.getNodeList("//class-definitions/class-definition");
			if( objCfgs != null )
			{
				XMLNode objCfg = objCfgs.getFirstNode();
				while( objCfg != null )
				{
					String cfgPath = objCfg.getAttribute("src");
					if( cfgPath.length() > 0 )
					{
						////////////////////////////////////////////////////////
						// Load taxonomy
						XMLDocument taxonomyCFG = new XMLDocument();
						taxonomyCFG.loadXML( rootAppPath + cfgPath );
						if( taxonomyCFG.isNull() ) SystemServlet.g_logger.fatal( cfgPath + " failed to load from path " + rootAppPath );

						// Pull all the class nodes into a map for quick lookup
						XMLNodeList classList = taxonomyCFG.getNodeList("//class");
						if( classList != null )
						{
							XMLNode classNode = classList.getFirstNode();
							while( classNode != null )
							{
								String className = classNode.getAttribute("name");
								//m_classNodes.put(className, classNode);
								m_classNodes.put(className, new TaxonomyClass(classNode));
								classNode = classList.getNextNode();
							}
						}
						//////////////////////////////////////////////
						
					}
					

					
					
					objCfg = objCfgs.getNextNode();
				}
			}
/*
			// Load taxonomy
			XMLDocument taxonomyCFG = new XMLDocument();
			taxonomyCFG.loadXML( rootAppPath + "WEB-INF/cfg/taxonomy.cfg" );
			if( taxonomyCFG.isNull() ) SystemServlet.g_logger.fatal( "taxonomy.cfg failed to load from path " + rootAppPath );

			// Pull all the class nodes into a map for quick lookup
			XMLNodeList classList = taxonomyCFG.getNodeList("//class");
			if( classList != null )
			{
				XMLNode classNode = classList.getFirstNode();
				while( classNode != null )
				{
					String className = classNode.getAttribute("name");
					//m_classNodes.put(className, classNode);
					m_classNodes.put(className, new TaxonomyClass(classNode));
					classNode = classList.getNextNode();
				}
			}
*/
			//m_interfaceCFG = new XMLDocument();
			//m_interfaceCFG.loadXML( rootAppPath + "cfg/interface.cfg" );
			//if( m_interfaceCFG.isNull() ) SystemServlet.g_logger.error( "interface.cfg failed to load from path " + rootAppPath );
			//m_StmtBldr = new StatementBuilder( m_taxonomyCFG );
			m_StmtBldr = new StatementBuilder( m_classNodes );
			m_scriptHandler = new ScriptHandler();
			
			//m_scriptsRoot = serverCFG.selectElementValue( "//script-root" );
			m_scriptsRoot = SystemServlet.getGenesysHome() + "WEB-INF/scripts";
			//m_scriptsRoot = new String( rootAppPath + "/scripts" );
			connectToDB();
			m_bInitialized = true;
		}
		catch( Exception ex )
		{
			SystemServlet.g_logger.fatal("Exception Caught: " + ex.getMessage());
        }
	}

	// TIME ZONE CONVERSION ROUTINES /////////////
	////////////////////////////////////////////////////////
/*
	public SimpleTimeZone GetObjManTimeZone()
	{
		return m_ObjManTZ;
	}

	public java.util.Date getObjManTime()
	{
		SimpleTimeZone omtz = GetObjManTimeZone();		

		//Calendar omTime = new GregorianCalendar(omtz);
		//omTime.setTime(new Date());
		//return omTime.getTime();

		TimeZone tz = TimeZone.getDefault();
		Date locTime = new Date();
		Date ret = new Date( locTime.getTime() + (omtz.getRawOffset()-tz.getRawOffset()) );

		// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
		if ( omtz.inDaylightTime( ret ))
		{
			Date dstDate = new Date( ret.getTime() + (omtz.getDSTSavings()-tz.getDSTSavings()) );

			// check to make sure we have not crossed back into standard time
			// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
			if ( omtz.inDaylightTime( dstDate ))
			{
				ret = dstDate;
			}
		}
		return ret;
	}

	public String convertFromGMT( String TimeStr, boolean timeOnly )
	{
		try
		{
			SimpleDateFormat dateFormat;
			if( timeOnly )
				dateFormat = new SimpleDateFormat("hh:mm a");
			else
				dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

			Date gmtDate = dateFormat.parse(TimeStr);
		
			SimpleTimeZone omtz = GetObjManTimeZone();
			Date ret = new Date( gmtDate.getTime() + omtz.getRawOffset() );

			// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
			if ( omtz.inDaylightTime( ret ))
			{
				Date dstDate = new Date( ret.getTime() + omtz.getDSTSavings() );

				// check to make sure we have not crossed back into standard time
				// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
				if ( omtz.inDaylightTime( dstDate ))
				{
					ret = dstDate;
				}
			}
			return dateFormat.format(ret);
		}
		catch( java.text.ParseException e )
		{
			SystemServlet.g_logger.error( "Parse Exception Caught in ObjectManager::convertFromGMT: " + e.getMessage() );
		}

	   return "...";
	}
	
	public java.util.Date convertToGMT( String TimeStr, boolean timeOnly )
	{
		try
		{
			SimpleDateFormat dateFormat;
			if( timeOnly )
				dateFormat = new SimpleDateFormat("hh:mm a");
			else
				dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

			Date omDate = dateFormat.parse(TimeStr);
		
			SimpleTimeZone omtz = GetObjManTimeZone();
			Date ret = new Date( omDate.getTime() - omtz.getRawOffset() );

			// if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
			if ( omtz.inDaylightTime( ret ))
			{
				Date dstDate = new Date( ret.getTime() - omtz.getDSTSavings() );

				// check to make sure we have not crossed back into standard time
				// this happens when we are on the cusp of DST (7pm the day before the change for PDT)
				if ( omtz.inDaylightTime( dstDate ))
				{
					ret = dstDate;
				}
			}
			return ret;
		}
		catch( java.text.ParseException e )
		{
			SystemServlet.g_logger.error( "Parse Exception Caught in ObjectManager::convertFromGMT: " + e.getMessage() );
		}

	   return null;
	}
*/
	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	public String GetClassPropertyType( String className, String fieldName )
	{
		// Execute script
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + className + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(className);
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + className );
		//	return "";
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(className);
		if( classNode == null ) return "";
		
		//XMLNode fieldNode = classNode.getSingleNode("properties/property[@name='" + fieldName + "']");
		TaxonomyClass.Property fieldNode = classNode.getNamedProperty(fieldName);
		if( fieldNode == null )
		{
			//////////////////////////////////////////////////////
			///////////////////////////////////////////////////////
			// Check for a base class
			//XMLNodeList baseclasses = classNode.getNodeList("baseclass");
			//if( baseclasses.getCount() > 0 )
			for( int i = 0; i < classNode.getBaseclassCount(); i++ )
			{
				//XMLNode baseclass = baseclasses.getFirstNode();
				TaxonomyClass.Baseclass baseclass = classNode.getBaseclass(i);
				//while( baseclass != null )
				//{
					//String sBaseClass = baseclass.getAttribute("name");
					String type = GetClassPropertyType(baseclass.getName(), fieldName);
					if( type.length() > 0 ) return type;
					//baseclass = baseclasses.getNextNode();
				//}
			}
			SystemServlet.g_logger.error( "ERROR: Loading field " + fieldName );
			return "";
		}
		
		return fieldNode.getType();
	}

/*
	public String GetClassPropertyTypeOfCol( String className, String colName )
	{
		// Execute script
		XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + className + "']" );
		if( classNode.isNull() )
		{
			SystemServlet.g_logger.error( "ERROR: Loading class " + className );
			return "";
		}
		
		XMLNode colNode = classNode.getSingleNode("properties/property[@column='" + colName + "']");
		if( colNode.isNull() )
		{
			//////////////////////////////////////////////////////
			///////////////////////////////////////////////////////
			// Check for a base class
			XMLNodeList baseclasses = classNode.getNodeList("baseclass");
			if( baseclasses.getCount() > 0 )
			{
				XMLNode baseclass = baseclasses.getFirstNode();
				while( baseclass != null )
				{
					String sBaseClass = baseclass.getAttribute("name");
					String type = GetClassPropertyTypeOfCol(sBaseClass, colName);
					if( type.length() > 0 ) return type;
					baseclass = baseclasses.getNextNode();
				}
			}
			//SystemServlet.g_logger.error( "ERROR: Loading field " + colName );
			return "";
		}
		
		return colNode.getAttribute("type");
	}
*/
	/**
	 * Auto Login
	 *
	 */
	public boolean SystemLogin( String sUserName, Credentials info )
	{
		return DoLogin( sUserName, "", info, true );
	}
	
	/**
	 * Login
	 *
	 */
	public boolean Login( String sUserName, String sPassword, Credentials info )
	{
		return DoLogin( sUserName, sPassword, info, false );
	}
	
	private char charAt_safe(String str, int index)
	{
		if(str != null && str.length() > index )
			return str.charAt(index);
		else
			return 0;
	}
	
	/**
	 * Internal login process
	 *
	 */
	public boolean DoLogin( String sUserName, String sPassword, Credentials info, boolean bSystemUser )
	{
		boolean bSuccess = false;
		
		try
		{
			//System.out.println("DEBUG: ===> getDBStatement");
			Statement resultStmt = getDBStatement();
//			try
//			{
//				resultStmt = m_db.createStatement();
//			}
//			catch(SQLException sqlex)
//			{
//				connectToDB();
//				resultStmt = m_db.createStatement();
//			}

			// Query for client profile
			String sQuery = String.format( m_szgetProfile, sUserName );
			ResultSet resultRows = resultStmt.executeQuery( sQuery );
			//System.out.println("DEBUG: Login Query: ===> " + sQuery);
			//ResultSetMetaData rsmd = resultRows.getMetaData();
			//int numberOfColumns = rsmd.getColumnCount();
			if( resultRows.first() )
			{
				//System.out.println("DEBUG: Login Query: ===> SUCCESS");
				String pass = resultRows.getString("password");
				//String hashPassword = EncodeMD5.generate(sPassword);
				
				String hash = "";
				try
				{
					MD5 md5 = new MD5();
					md5.Update(sPassword, null);
					hash = md5.asHex();
					//System.out.println("DEBUG: Encrypted Password: ===> " + sPassword + " to " +  hash);
				}
				catch(Exception e){}
			    
			    
				if( bSystemUser == true || pass.equals(hash) == true )
				{
					// Create new client object
					RandomGUID guid = new RandomGUID();
					info.m_UserName = sUserName;
					info.m_bSystemUser = bSystemUser;
					info.m_UserId = resultRows.getString("id");
					info.m_sTicket = guid.toString();
					info.m_RoleId = resultRows.getString("roleid");
					
					//String admin = resultRows.getString("admin");
					//if( admin.length() > 0 && admin.charAt(0) == 'Y' )
					//	info.m_bAdmin = true;
					//else
					//	info.m_bAdmin = false;
					info.m_bAdmin = (charAt_safe(resultRows.getString("admin"),0) == 'Y');
					
					info.m_prevLogin = resultRows.getTimestamp("last_logged_in");
					
					//String show_welcome = resultRows.getString("admin");
					info.m_showWelcome = (charAt_safe(resultRows.getString("show_welcome"),0) == 'Y');
					
					// TODO: Put this code back and check for null
					//String admin = resultRows.getString("admin");
					//if( resultRows.getString("admin").charAt(0) == 'Y' ) info.m_bAdmin = true;
					//else info.m_bAdmin = false;
					//info.m_bAdmin = true;
					
					m_clientMap.put(info.m_sTicket, info);
					bSuccess = true;
					
					// Update last_logged_in
					if( !bSystemUser )
						RegisterLoginEvent(sUserName);
				}
			}


			// Query for access rights
			sQuery = String.format( m_szgetAccess, sUserName );
			resultRows = resultStmt.executeQuery( sQuery );
			//rsmd = resultRows.getMetaData();
			//int numberOfColumns = rsmd.getColumnCount();
			while( resultRows.next() )
			{
				String view = resultRows.getString("view");
				String access = resultRows.getString("access");
				String access_right = new String(view + ":" + access);
				info.m_AccessList.add(access_right);
			}

			resultStmt.close();
		}
		catch(SQLException ex)
		{
			//System.out.println("DEBUG: Login Exception: ===> " + ex.getMessage());
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::Login} - " + ex.getMessage() );
			ex.printStackTrace();
		}

		return bSuccess;
	}

	public void Logout( Credentials info )
	{
		if( info != null )
			m_clientMap.remove( info.m_sTicket );
	}
	
	private EventCallback ExecuteScript( Credentials client_info, TaxonomyClass classNode, String eventName, Object context )
	{
		EventCallback event = new EventCallback( classNode.getName(), eventName );
		try
		{
			String eventScript = classNode.getEventScript(eventName);
			if( eventScript != null && eventScript.length() > 0 )
			{
				ServerCallback server_callback = new ServerCallback(client_info, this, context);
				String sScriptFile = m_scriptsRoot + "/" + eventScript;
				if( m_scriptHandler.ExecuteScript( event, server_callback, sScriptFile ) == false )
					return null;
				//return event.getOutput();
			}
		}
		catch(Exception e)
		{
			SystemServlet.g_logger.error( "Exception Caught in ObjectManager::ExecuteScript: " + e.getMessage() );
			return null;
		}
		return event;
	}

//	private void addTextNode(XMLStreamWriter xmlStreamWriter, String tagName, String textVal)
//	throws XMLStreamException
//	{
//		xmlStreamWriter.writeStartElement(tagName);		// <textVal>
//		xmlStreamWriter.writeCharacters(textVal);
//		xmlStreamWriter.writeEndElement();				// </textVal>
//	}

	public Credentials verifyClientInfo( Credentials info )
	{
		if( info == null ) return null;
		Credentials info_found = (Credentials)m_clientMap.get(info.m_sTicket);
		if( info_found != null )
			return info_found;
		else
			return null;
	}
	

	//TODO: /////////////////////////////////////////////////////////////////////
	// - Query and QueryXML should be converted, at some time in the future, to work
	// - inversely from how they work now and, ObjectResponse should be converted to
	// - read directly from the db. This is enough work to warrent postponing for now.
	public QueryResponse Query( Credentials info, ObjectQuery query ) throws AuthenticationException
	{
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");

		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(query.getClassName());
		if( classNode == null ) return null;

		// preSelect Event ///////////////////////////
		EventCallback event = ExecuteScript( client_info, classNode, "preSelect", (Object)query );
		//if( eventResponse != null && eventResponse.length() > 0 ) return eventResponse;
		if( event == null || event.isAborted() == true || classNode.getAbstractType().equalsIgnoreCase("none")) return null;
		//////////////////////////////////

		//long startMillis = SystemServlet.enterCodeBlock("GenerateSelect");
		QueryPropertyList qryPropList = new QueryPropertyList();
		String sQuery = m_StmtBldr.GenerateSelect( client_info, query, qryPropList );
		//SystemServlet.exitCodeBlock("GenerateSelect",startMillis);

		// Check for special SQL debug flag
		if( SystemServlet.g_debugSQLQueries )
		{
			// Used for debug purposes
			SystemServlet.g_logger.debug("\n" + sQuery);
		}
		
		QueryResponse oResponse = new QueryResponse(query.getClassName());
		try
		{
			Statement resultStmt = getDBStatement();
			ResultSet resultRows = resultStmt.executeQuery( sQuery );
			ResultSetMetaData rsmd = resultRows.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			
			
			if(query.getCountOnly()){
				if(resultRows.next()){
					oResponse.setCount(resultRows.getLong("count"));
				}
			}
			else{
				while( resultRows.next() )
				{
					RepositoryObject oObj = new RepositoryObject(resultRows.getString("id"));
					for( int i = 0; i < qryPropList.count(); i++ )
					{
						QueryProperty property = qryPropList.get(i);
						String colName = property.getName();//rsmd.getColumnName(i);
						StatementBuilder.PropType propType = property.getType();//property.getAttribute("type");
						//xmlStreamWriter.writeStartElement("property");	// <object>
						//xmlStreamWriter.writeAttribute("name", colName);
						if( propType == StatementBuilder.PropType.LIST )
						{
							SQLEmbeddedObjStmt embedObjs = (SQLEmbeddedObjStmt)property;
							oObj.addPropertyObjRefs(colName, BuildEmbeddedObjList(resultRows.getString("id"), embedObjs));
						}
						else
						{
							ObjectProperty objProp = new ObjectProperty();
							objProp.setName(colName);
							switch(propType)
							{
		//					case TIME:
		//						sCollection += "<![CDATA[";
		//						sCollection += convertFromGMT(resultRows.getString(colName),true);
		//						sCollection += "]]>";
		//						break;
		//					case DATETIME:
		//						sCollection += "<![CDATA[";
		//						sCollection += convertFromGMT(resultRows.getString(colName),false);
		//						sCollection += "]]>";
		//						break;
							case INT:
								//xmlStreamWriter.writeCharacters(Integer.toString(resultRows.getInt(colName)));
								objProp.setValue(resultRows.getInt(colName));
								break;
							case REAL:
								//xmlStreamWriter.writeCharacters(Double.toString(resultRows.getDouble(colName)));
								objProp.setValue(resultRows.getDouble(colName));
								break;
							default:
								//xmlStreamWriter.writeCharacters(resultRows.getString(colName));
								objProp.setValue(resultRows.getString(colName));
								break;
							}
							oObj.addProperty(objProp);
						}
					}
					oResponse.addObject(oObj);
				}
			}
			
			
			
			
			resultStmt.close();
		}
		catch(SQLException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::Query} - " + ex.getMessage() );
			ex.printStackTrace();
		}
		
		// postSelect Event ///////////////////////////
		ExecuteScript( client_info, classNode, "postSelect", (Object)query );
		//////////////////////////////////
		
		//SystemServlet.exitCodeBlock("Query",startMillis);
		
		return oResponse;
	}

	/////////////////////////////////////////////////////////////////////////////
	public String QueryXML( Credentials info, ObjectQuery query ) throws AuthenticationException
	{		
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");

		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(query.getClassName());
		if( classNode == null ) return "ERROR";

		// preSelect Event ///////////////////////////
		EventCallback event = ExecuteScript( client_info, classNode, "preSelect", (Object)query );
		if( event == null ) return "<return><call>query</call><code>999</code><msg>Failed to execute script</msg></return>";
		if( event.isAborted() == true ) return event.getOutput();
		if( classNode.getAbstractType().equalsIgnoreCase("none")) return "<return><call>query</call><code>0</code><msg>SUCCESS</msg></return>";
		//////////////////////////////////

		QueryPropertyList qryPropList = new QueryPropertyList();
		String sQuery = m_StmtBldr.GenerateSelect( client_info, query, qryPropList );

		// Check for special SQL debug flag
		if( SystemServlet.g_debugSQLQueries )
		{
			// Used for debug purposes
			SystemServlet.g_logger.debug("\n" + sQuery);
		}

		//StringWriter stringWriter = new StringWriter();
		ByteArrayOutputStream outXML = new ByteArrayOutputStream();
		try
		{
			XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
			
			// Open XML Document
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
			xmlStreamWriter.writeStartElement("return");		// <return>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "call", "query");		// <call>query</call>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "code", "0");			// <code>0</code>
			XMLStreamHelper.addTextNode(xmlStreamWriter, "msg", "SUCCESS");		// <msg>SUCCESS</msg>
			xmlStreamWriter.writeStartElement("document");		// <document>
			xmlStreamWriter.writeStartElement("collection");	// <collection>
			xmlStreamWriter.writeAttribute("name", query.getClassName());
			xmlStreamWriter.writeAttribute("objStart", Long.toString(query.getStart()));
			xmlStreamWriter.writeAttribute("requestLevel", query.getRequestLevel());
			xmlStreamWriter.writeAttribute("objCount", Long.toString(query.getCount()));
	  
			try
			{
				Statement resultStmt = getDBStatement();
//				try
//				{
//					resultStmt = m_db.createStatement();
//				}
//				catch(SQLException sqlex)
//				{
//					connectToDB();
//					resultStmt = m_db.createStatement();
//				}
				ResultSet resultRows = resultStmt.executeQuery( sQuery );
				ResultSetMetaData rsmd = resultRows.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				String sCollection = "";
				while( resultRows.next() )
				{
					xmlStreamWriter.writeStartElement("object");	// <object>
					xmlStreamWriter.writeAttribute("id", resultRows.getString("id"));
					//sCollection += "<object id='" + resultRows.getString("id") + "'>";
					xmlStreamWriter.writeStartElement("property");	// <property>
					xmlStreamWriter.writeAttribute("name", "id");
					xmlStreamWriter.writeCharacters(resultRows.getString("id"));
					xmlStreamWriter.writeEndElement();	// </property>
					//sCollection += "<property name='id'>" + resultRows.getString("id") + "</property>";
					
					
					
					for( int i = 0; i < qryPropList.count(); i++ )
					{
						QueryProperty property = qryPropList.get(i);
						String colName = property.getName();//rsmd.getColumnName(i);
						StatementBuilder.PropType propType = property.getType();//property.getAttribute("type");
						xmlStreamWriter.writeStartElement("property");	// <property>
						xmlStreamWriter.writeAttribute("name", colName);
						//sCollection += "<property name='" + colName + "'>";
						switch(propType)
						{
	//					case TIME:
	//						sCollection += "<![CDATA[";
	//						sCollection += convertFromGMT(resultRows.getString(colName),true);
	//						sCollection += "]]>";
	//						break;
	//					case DATETIME:
	//						sCollection += "<![CDATA[";
	//						sCollection += convertFromGMT(resultRows.getString(colName),false);
	//						sCollection += "]]>";
	//						break;
						case INT:
							xmlStreamWriter.writeCharacters(Integer.toString(resultRows.getInt(colName)));
							//sCollection += resultRows.getInt(colName);
							break;
						case REAL:
							xmlStreamWriter.writeCharacters(Double.toString(resultRows.getDouble(colName)));
							//sCollection += resultRows.getDouble(colName);
							break;
						case LIST:
							SQLEmbeddedObjStmt embedObjs = (SQLEmbeddedObjStmt)property;
							//String objQuery = embedObjs.generateQuery(resultRows.getString("id"));
							//sCollection += BuildEmbeddedObjXML(resultRows.getString("id"), embedObjs);
							BuildEmbeddedObjXML(xmlStreamWriter, resultRows.getString("id"), embedObjs);
							break;
						default:
							String val = resultRows.getString(colName);
							//String utfStr = new String(val.getBytes("UTF-8"), "UTF-8");
							//xmlStreamWriter.writeCData(val);
							xmlStreamWriter.writeCharacters(val);
							//sCollection += "<![CDATA[";
							//sCollection += resultRows.getString(colName);
							//sCollection += "]]>";
							break;
						}
						//sCollection += "</property>";
						xmlStreamWriter.writeEndElement();	// </property>
					}

					//sCollection += "</object>";
					xmlStreamWriter.writeEndElement();	// </object>
				}
				resultStmt.close();
				//sResponse += sCollection;	// Ensures that only complete collections will be appended
			}
			catch(SQLException ex)
			{
				SystemServlet.g_logger.error( "SQLException thrown in {ObjectManager::QueryXML} - " + ex.getMessage() );
				return "<return><call>query</call><code>9999</code><msg>Request failed due to server error</msg></return>";
				//ex.printStackTrace();
			}
			catch(Exception ex2)
			{
				SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::QueryXML} - " + ex2.getMessage() );
				return "<return><call>query</call><code>9999</code><msg>Request failed due to server error</msg></return>";
			}
	
			xmlStreamWriter.writeEndElement();	// </collection>
			xmlStreamWriter.writeEndElement();	// </document>
			xmlStreamWriter.writeEndElement();	// </return>
			//sResponse += "</collection></document></return>";
	
			// Close XML Document
			xmlStreamWriter.writeEndDocument();
			xmlStreamWriter.flush();
			xmlStreamWriter.close();
		}
		catch(XMLStreamException e)
		{
			//e.printStackTrace();
			SystemServlet.g_logger.error( "XMLStreamException thrown in {ObjectManager::QueryXML} - " + e.getMessage() );
			return "<return><call>query</call><code>9999</code><msg>Request failed due to server error</msg></return>";
		}
		catch(FactoryConfigurationError e)
		{
			//e.printStackTrace();
			SystemServlet.g_logger.error( "FactoryConfigurationError thrown in {ObjectManager::QueryXML} - " + e.getMessage() );
			return "<return><call>query</call><code>9999</code><msg>Request failed due to server error</msg></return>";
		}
		
		// postSelect Event ///////////////////////////
		ExecuteScript( client_info, classNode, "postSelect", (Object)query );
		//////////////////////////////////

		//SystemServlet.g_logger.debug( "step 3 : " + Long.toString(System.currentTimeMillis()-lStart) );
		
		//return sResponse;
		try
		{
			return outXML.toString("utf-8");
		}
		catch(UnsupportedEncodingException e)
		{
			SystemServlet.g_logger.error( "UnsupportedEncodingException thrown in {ObjectManager::QueryXML} - " + e.getMessage() );
			return "<return><call>query</call><code>9999</code><msg>Request failed due to server error</msg></return>";

		}
	}

	private void BuildEmbeddedObjXML(XMLStreamWriter xmlStreamWriter, String id, SQLEmbeddedObjStmt embedObjs)
	{
		//String sResult = new String("");
		String objQuery = embedObjs.generateQuery(id);
		try
		{
			Statement resultStmt = getDBStatement();
//			try
//			{
//				resultStmt = m_db.createStatement();
//			}
//			catch(SQLException sqlex)
//			{
//				connectToDB();
//				resultStmt = m_db.createStatement();
//			}
			ResultSet resultRows = resultStmt.executeQuery( objQuery );
			while( resultRows.next() )
			{
				if( resultRows.getString("id") != null )
				{
					xmlStreamWriter.writeStartElement("objectref");		// <objectref>
					xmlStreamWriter.writeAttribute("id", resultRows.getString("id"));
					
					for( int i = 0; i < embedObjs.m_Cols.size(); i++ )
					{
						String propName = (String)embedObjs.m_Cols.get(i);
						xmlStreamWriter.writeStartElement("propref");	// <propref>
						xmlStreamWriter.writeAttribute("name", propName);
						xmlStreamWriter.writeCharacters(resultRows.getString(propName));
						xmlStreamWriter.writeEndElement();	// </propref>
					}
					
					//xmlStreamWriter.writeCharacters(resultRows.getString("display"));
					//sResult += "<objectref id=\"" + resultRows.getString("id") + "\">" + resultRows.getString("display") + "</objectref>";
					xmlStreamWriter.writeEndElement();	// </objectref>
				}
			}
			resultStmt.close();
		}
		catch(SQLException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::BuildEmbeddedObjXML} - " + ex.getMessage() );
			//ex.printStackTrace();
		}
		catch(XMLStreamException e)
		{
			//e.printStackTrace();
			SystemServlet.g_logger.error( "XMLStreamException thrown in {ObjectManager::BuildEmbeddedObjXML} - " + e.getMessage() );
		}
		catch(FactoryConfigurationError e)
		{
			//e.printStackTrace();
			SystemServlet.g_logger.error( "FactoryConfigurationError thrown in {ObjectManager::BuildEmbeddedObjXML} - " + e.getMessage() );
		}
		//return sResult;
	}

	private RepositoryObjectRefList BuildEmbeddedObjList(String id, SQLEmbeddedObjStmt embedObjs)
	{
		RepositoryObjectRefList objList = new RepositoryObjectRefList();
		//String sResult = new String("");
		String objQuery = embedObjs.generateQuery(id);
		try
		{
			Statement resultStmt = getDBStatement();
//			try
//			{
//				resultStmt = m_db.createStatement();
//			}
//			catch(SQLException sqlex)
//			{
//				connectToDB();
//				resultStmt = m_db.createStatement();
//			}
			ResultSet resultRows = resultStmt.executeQuery( objQuery );
			while( resultRows.next() )
			{
				RepositoryObjectRef objRef = new RepositoryObjectRef( resultRows.getString("id") );
				
				for( int i = 0; i < embedObjs.m_Cols.size(); i++ )
				{
					String propName = (String)embedObjs.m_Cols.get(i);
					ObjectProperty newProp = new ObjectProperty(propName, resultRows.getString(propName));
					objRef.addProperty(newProp);
				}
				//xmlStreamWriter.writeStartElement("objectref");		// <objectref>
				//xmlStreamWriter.writeAttribute("id", resultRows.getString("id"));
				//xmlStreamWriter.writeCharacters(resultRows.getString("display"));
				//sResult += "<objectref id=\"" + resultRows.getString("id") + "\">" + resultRows.getString("display") + "</objectref>";
				//xmlStreamWriter.writeEndElement();	// </objectref>
				objList.add(objRef);
			}
			resultStmt.close();
		}
		catch(SQLException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::BuildEmbeddedObjXML} - " + ex.getMessage() );
			//ex.printStackTrace();
		}
		catch(FactoryConfigurationError e)
		{
			//e.printStackTrace();
			SystemServlet.g_logger.error( "FactoryConfigurationError thrown in {ObjectManager::BuildEmbeddedObjXML} - " + e.getMessage() );
		}
		return objList;
	}

	private String GenerateGUID()
	{
		RandomGUID guid = new RandomGUID();
		return guid.toString();
	}
	
	public String Insert( Credentials info, ObjectSubmit data ) throws AuthenticationException, RepositoryException
	{
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");

		// Execute script
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + data.getClassName() + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(data.getClassName());
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + data.getClassName() );
		//	return null;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(data.getClassName());
		if( classNode == null ) return null;

		// preInsert Event ///////////////////////////
		EventCallback event = ExecuteScript( client_info, classNode, "preInsert", (Object)data );
		if( event == null )
		{
			throw new RepositoryException("Script failure", RepositoryException.VALIDATION_ERROR);
		}
		if( event.isAborted() == true )
		{
			throw new RepositoryException(event.getErrorMessage(), RepositoryException.VALIDATION_ERROR);
			//return null;
		}
		if( classNode.getAbstractType().equalsIgnoreCase("none")) return null;
		//////////////////////////////////

		// Execute request //////////////
		String sNewGUID = GenerateGUID();
		if( Submit( client_info, sNewGUID, data, true ) == false )
			return null;
		/////////////////////////////////////////

		// postInsert Event ///////////////////////////
		ExecuteScript( client_info, classNode, "postInsert", (Object)data );
		//////////////////////////////////

		return sNewGUID;
	}
	
	public boolean Update( Credentials info, String id, ObjectSubmit data ) throws AuthenticationException, RepositoryException
	{
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");

		// Execute script
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + data.getClassName() + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(data.getClassName());
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + data.getClassName() );
		//	return false;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(data.getClassName());
		if( classNode == null ) return false;

		// preUpdate Event ///////////////////////////
		//EventCallback event = ExecuteScript( client_info, classNode, "preUpdate", (Object)data );
		//if( event.isAborted() == true ) return true;
		//if( classNode.getAbstractType().equalsIgnoreCase("none")) return true;
		//////////////////////////////////

		EventCallback event = ExecuteScript( client_info, classNode, "preUpdate", (Object)data );
		if( event == null )
		{
			throw new RepositoryException("Script failure", RepositoryException.VALIDATION_ERROR);
		}
		if( event.isAborted() == true )
		{
			throw new RepositoryException(event.getErrorMessage(), RepositoryException.VALIDATION_ERROR);
			//return null;
		}
		if( classNode.getAbstractType().equalsIgnoreCase("none")) return false;
		
		// Execute request //////////////
		if( Submit( client_info, id, data, false ) == false )
			return false;
		////////////////////////////////////

		// postUpdate Event ///////////////////////////
		ExecuteScript( client_info, classNode, "postUpdate", (Object)data );
		//////////////////////////////////

		return true;
	}
	
	public boolean Copy( Credentials info, String id, ObjectSubmit data ) throws AuthenticationException, RepositoryException
	{
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");
		
		String sNewGUID = Insert( client_info, data );
		
		// TODO: Implement copy operations of all reference objects associated
		//		 with the specified object

		///////////////////////////////////////////////////////////
		// Get class name from request and retrieve
		// class interface
		// Execute script
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + data.getClassName() + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(data.getClassName());
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + data.getClassName() );
		//	return false;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(data.getClassName());
		if( classNode == null ) return false;

		//////////////////////////////////////////////////////////
		// Iterate the class references and copy them as well
		//XMLNodeList pReferenceNodeList = classNode.getNodeList("references/reference");
		//XMLNode pReferenceNode = pReferenceNodeList.getFirstNode();
		//while( pReferenceNode != null )
		for( int i = 0; i < classNode.getReferenceCount(); i++ )
		{
			TaxonomyClass.Reference pReferenceNode = classNode.getRefernce(i);
			String szRefClassName = pReferenceNode.getClassName();//.getAttribute("class");
			//XMLNode pRefClassNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + szRefClassName + "']" );
			TaxonomyClass pRefClassNode = (TaxonomyClass)m_classNodes.get(szRefClassName);
			//if( pRefClassNode.isNull() == false )
			//{
				// Query for all objects referencing the current object
				String szFKey = pReferenceNode.getFKey();//.getAttribute( "fkey" );
				ObjectQuery queryStmt = new ObjectQuery( szRefClassName );
				queryStmt.getProperties().add(new ObjectProperty(szFKey,id));
				QueryResponse oResponse = Query( client_info, queryStmt );
				RepositoryObjects oObjects = oResponse.getObjects( queryStmt.getClassName() );
				for( int ii = 0; ii < oObjects.count(); ii++ )
				{
					RepositoryObject obj = oObjects.get( ii );
	
					// Perform copy operation on this node
					ObjectSubmit submitStmt = new ObjectSubmit( szRefClassName );
					ObjectProperties props = submitStmt.getProperties();
					//XMLNodeList propNodes = m_taxonomyCFG.getNodeList( "//class[@name='" + szRefClassName + "']/properties/property" );
					//XMLNodeList propNodes = pRefClassNode.getNodeList( "properties/property" );
					//XMLNode propNode = propNodes.getFirstNode();
					//while( propNode != null )
					for( int iii = 0; iii < pRefClassNode.getReferenceCount(); iii++ )
					{
						String sName = pReferenceNode.getClassName();//.getAttribute( "name" );
						String sValue;
						if( sName.equalsIgnoreCase(szFKey) == true )
							sValue = sNewGUID;
						else
							sValue = obj.getPropertyValue(sName);
						props.add( new ObjectProperty( sName, sValue ) );
						//propNode = propNodes.getNextNode();
					}
					Copy( client_info, obj.getId(), submitStmt );
				}
	
				//pReferenceNode = pReferenceNodeList.getNextNode();
			//}
		}
		return true;
	}

	private boolean HasPasswordChanged(SQLParam param)
	{
		Statement resultStmt = getDBStatement();
		try
		{
			try
			{
				ResultSet resultRows = resultStmt.executeQuery(param.getPasswordQuery());
				if( resultRows.next() )
				{
					String curr_password = resultRows.getString(param.getName());
					if( curr_password.equals(param.getText()) ) return false;
				}
			}
			finally
			{
				resultStmt.close();
			}
		}
		catch(SQLException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::HasPasswordChanged} - " + ex.getMessage() );
		}
		return true;
	}
	
	private void SetStmtValue( PreparedStatement pStmt, SQLParam param, int index )
	{
		try
		{
			switch(param.getType())
			{
			case TEXT:
			case BOOLEAN:
			{
				pStmt.setString(index+1, param.getText());
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + param.getText());
				
				break;
			}
			case PASSWORD:
			{
				String passVal = param.getText();
				if(HasPasswordChanged(param))
				{
					try
					{
						MD5 md5 = new MD5();
						md5.Update(passVal, null);
						passVal = md5.asHex();
					}
					catch(Exception e){}
				}
				pStmt.setString(index+1, passVal);
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + param.getText());
				
				break;
			}
			case DATE:
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date utilDate = dateFormat.parse(param.getText());
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				pStmt.setDate(index+1, sqlDate);
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + param.getText());
				
				break;
			}
			case TIME:
			{
				//java.util.Date gmtDate = convertToGMT(param.getText(),true);
				//java.sql.Time sqlTime = new java.sql.Time(gmtDate.getTime());
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
				java.util.Date utilDate = dateFormat.parse(param.getText());
				java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
				pStmt.setTime(index+1, sqlTime);
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + param.getText());
				
				break;
			}
			case DATETIME:
			{
				//java.util.Date gmtDate = convertToGMT(param.getText(),false);
				//java.sql.Timestamp sqlTimedate = new java.sql.Timestamp(gmtDate.getTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				java.util.Date utilDate = dateFormat.parse(param.getText());
				java.sql.Timestamp sqlTimedate = new java.sql.Timestamp(utilDate.getTime());
				pStmt.setTimestamp(index+1, sqlTimedate);
				//pStmt.setDate(index+1, utilDate.valueOf());
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + param.getText());
				
				break;
			}
			case INTEGER:
			{
				pStmt.setInt(index+1, param.getInt());
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + Integer.toString(param.getInt()));
				
				break;
			}
			case REAL:
			{
				pStmt.setDouble(index+1, param.getReal());
				
				if( SystemServlet.g_debugSQLQueries )
					SystemServlet.g_logger.debug(param.getName() + "=" + Double.toString(param.getReal()));
				
				break;
			}
			}
		}
		catch(java.text.ParseException e)
		{
			SystemServlet.g_logger.error( "ParseException thrown in {ObjectManager::SetStmtValue} - " + e.getMessage() );
		}
		catch( SQLException ex )
		{
			SystemServlet.g_logger.error( "SQLException thrown in {ObjectManager::SetStmtValue} - " + ex.getMessage() );
		}
	}
	
	private void RegisterLoginEvent(String sUserName)
	{
		try
		{
			String sQuery = String.format( m_szupdateLastLoggedIn, sUserName );
			PreparedStatement pStmt = getDBPrepStatement(sQuery);
			try
			{
				pStmt.executeUpdate();
			}
			finally
			{
				pStmt.close();
			}
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::RegisterLoginEvent} - " + ex.getMessage() );
		}
	}
	
	private boolean ExecuteSubmit( boolean bTryInsert, SQLStatement stmt ) throws RepositoryException
	{
		String query;
		if( stmt.m_type == SQLStatement.StmtType.DELETE )
		{
			if(stmt.m_vParams.size()==0) return true;
			query = stmt.generateDelete();
		}
		else if( bTryInsert || stmt.m_type == SQLStatement.StmtType.INSERT )
		{
			if(stmt.m_vParams.size()==0) return true;
			query = stmt.generateInsert();
		}
		else
		{
			if(stmt.m_vParams.size()<=3) return true;
			query = stmt.generateUpdate("id");
		}
		//{
			//
		//	if( bTryInsert )
		//		query = stmt.generateInsert();
		//	else
		//		query = stmt.generateUpdate("id");
		//}
		// Check for special SQL debug flag
		if( SystemServlet.g_debugSQLQueries )
		{
			// Used for debug purposes
			SystemServlet.g_logger.debug("\n" + query);
		}

		try
		{
			PreparedStatement pStmt = getDBPrepStatement(query);
			try
			{
	//				pStmt = m_db.prepareStatement(query);
	//			}
	//			catch(SQLException sqlex)
	//			{
	//				connectToDB();
	//				pStmt = m_db.prepareStatement(query);
	//			}
				int iParamIdx = 0;
				String updateKey = new String("");
				for( int i = 0; i < stmt.m_vParams.size(); i++ )
				{
					SQLParam param = (SQLParam)stmt.m_vParams.get(i);
					if( !bTryInsert )
					{
						if( param.getName().equalsIgnoreCase("id") )
							updateKey = param.getText();
						else if ( param.getName().equalsIgnoreCase("owner") == false &&
								   param.getName().equalsIgnoreCase("role") == false )
							SetStmtValue( pStmt, param, iParamIdx++ );
					}
					else
					{
						SetStmtValue( pStmt, param, iParamIdx++ );
					}
				}
				if( updateKey.length() > 0 )
				{
					pStmt.setString(iParamIdx+1, updateKey);
				}
				if( pStmt.executeUpdate() > 0 || stmt.m_type == SQLStatement.StmtType.DELETE )
					return true;
				//resultStmt.execute( query );
				//return true;
			}
			finally
			{
				pStmt.close();
			}
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::ExecuteSubmit} - " + ex.getMessage() );
			throw new RepositoryException("Error: " + ex.getMessage(), RepositoryException.SQL_ERROR);
		}
		return false;
	}
	
	private boolean Submit( Credentials info, String sId, ObjectSubmit submit, boolean bTryInsertFirst ) throws RepositoryException
	{
		//try
		//{
		// Create new GUID for this new object
		ObjectProperties props = submit.getProperties();
		ObjectProperty key = props.get("id");

		if( key == null )
			props.add( new ObjectProperty( "id", sId ) );
		else
			key.setValue( sId );
		/////////////////////////////////////////////

		SQLStatements stmts = m_StmtBldr.SubmitObj( info, submit, bTryInsertFirst );
		for( int i = 0; i < stmts.count(); i++ )
		{
			SQLStatement stmt = stmts.getAt(i);
			if( ExecuteSubmit( bTryInsertFirst, stmt ) == false )
				if( ExecuteSubmit( !bTryInsertFirst, stmt ) == false )
						return false;
		}
		return true;
		//}
		//catch(Exception ex)
		//{
		//	SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::Submit} - " + ex.getMessage() );
		//}
		//return false;
	}

	public boolean Delete( Credentials info, String className, String id ) throws AuthenticationException, RepositoryException
	{
		Credentials client_info = verifyClientInfo(info);
		if( client_info == null ) throw new AuthenticationException("Invalid credentials");

		// Execute script
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + className + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(className);
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + className );
		//	return false;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(className);
		if( classNode == null ) return false;

		// preDelete Event ///////////////////////////
		//EventCallback event = ExecuteScript( client_info, classNode, "preDelete", (Object)id );
		//if( event.isAborted() == true ) return true;
		//if( classNode.getAbstractType().equalsIgnoreCase("none")) return true;
		//////////////////////////////////
		EventCallback event = ExecuteScript( client_info, classNode, "preDelete", (Object)id );
		if( event == null )
		{
			throw new RepositoryException("Script failure", RepositoryException.VALIDATION_ERROR);
		}
		if( event.isAborted() == true )
		{
			throw new RepositoryException(event.getErrorMessage(), RepositoryException.VALIDATION_ERROR);
			//return null;
		}
		if( classNode.getAbstractType().equalsIgnoreCase("none")) return false;
		//////////////////////////////////
		
		try
		{
			//////////////////////////////////////////////////////
			// Delete class references
			//XMLNodeList pReferenceNodeList = pNode.getNodeList("references/reference");
			//XMLNode pReferenceNode = pReferenceNodeList.getFirstNode();
			//while( pReferenceNode != null )
			for( int i = 0; i < classNode.getReferenceCount(); i++ )
			{
				TaxonomyClass.Reference pReferenceNode = classNode.getRefernce(i);
				String szRefClassName = pReferenceNode.getClassName();//.getAttribute("class");
				//XMLNode pRefClassNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + szRefClassName + "']" );
				TaxonomyClass pRefClassNode = (TaxonomyClass)m_classNodes.get(szRefClassName);
				if( pRefClassNode != null )
				{
					String szFKey = pReferenceNode.getFKey();//.getAttribute( "fkey" );
					
					////////////////////////////////////////////////////////////
					// Get actual column name from fkey property value
					//XMLNodeList pRefObjPropNodeList = pRefClassNode.getNodeList( "properties/property[@name='" + szFKey + "']" );
					//XMLNode pRefObjPropNode = pRefObjPropNodeList.getFirstNode();
					TaxonomyClass.Property pRefObjPropNode = pRefClassNode.getNamedProperty(szFKey);
					if( pRefObjPropNode != null )
					{
						// Find the type property
						String szFKeyCol = pRefObjPropNode.getColumn();//.getAttribute("column");
						Vector vIds = new Vector();
						String fId = null;
						String query = "select id from " + pRefClassNode.getTable() + " where " + szFKeyCol + "='" + id + "'";
						Statement resultStmt = getDBStatement();
						ResultSet resultRows = resultStmt.executeQuery( query );
						while( resultRows.next() )
						{
							// Avoid nested database connections
							fId = resultRows.getString("id");
							if( fId != null ) vIds.add( fId );
						}
						resultStmt.close();
						
						//get an Iterator object for Vector using iterator() method.
						java.util.Iterator itr = vIds.iterator();
					 
					    //use hasNext() and next() methods of Iterator to iterate through the elements
					    //System.out.println("Iterating through Vector elements...");
					    while(itr.hasNext())
					    {
					      //System.out.println(itr.next());
					      Delete( client_info, szRefClassName, (String)itr.next() );
					    }
					}
				}
				//pReferenceNode = pReferenceNodeList.getNextNode();
			}
			
			
			
			
			SQLStatements stmts = m_StmtBldr.DeleteObj( className, id );
			//for( int i = 0; i < stmts.count(); i++ )
			//for( int i = stmts.count() - 1; i >= 0; i-- )
			for( int i = 0; i < stmts.count(); i++ )
			{
				SQLStatement stmt = stmts.getAt(i);
				String query = stmt.generateDelete();
				
				// Check for special SQL debug flag
				if( SystemServlet.g_debugSQLQueries )
				{
					// Used for debug purposes
					SystemServlet.g_logger.debug("\n" + query);
				}
				
				PreparedStatement pStmt = getDBPrepStatement(query);
				try
				{
	//				PreparedStatement pStmt;
	//				try
	//				{
	//					pStmt = m_db.prepareStatement(query);
	//				}
	//				catch(SQLException sqlex)
	//				{
	//					connectToDB();
	//					pStmt = m_db.prepareStatement(query);
	//				}
					for( int j = 0; j < stmt.m_vParams.size(); j++ )
					{
						SQLParam param = (SQLParam)stmt.m_vParams.get(j);
						SetStmtValue( pStmt, param, j );
					}
					if( pStmt.executeUpdate() > 0 )
						return true;
				}
				finally
				{
					pStmt.close();
				}
			}
		}
		catch(Exception ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {ObjectManager::Delete} - " + ex.getMessage() );
		}
						
		// postDelete Event ///////////////////////////
		ExecuteScript( client_info, classNode, "postDelete", (Object)id );
		//////////////////////////////////

		return true;
	}
	
}