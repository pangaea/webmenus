///////////////////////////////////////
// Copyright (c) 2004-2012 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////
package com.genesys;

// Imported java classes
import java.sql.Date;
import java.util.*;
import java.io.*;
import java.net.URL;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.*;
import javax.servlet.http.*;

//import java.util.logging.*;
//import org.apache.log4j.Level;
import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.helpers.Loader;
//import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.varia.*;
import org.w3c.dom.Document;

import com.genesys.util.xml.*;
import com.genesys.repository.*;

import com.genesys.views.*;
import com.genesys.views.InterfaceCfg.View.Field;

/*</Imports>*/
public class SystemServlet implements ServletContextListener
{
	private static class ScheduledTaskInfo
	{
		public String name;
		public String className;
		public int delay, interval;
		public ScheduledTaskInfo(XMLNode node)
		{
			//name="orderHandler" class="com.genesys.webmenus.OrderHandler" delay="5" interval="5"
			name = node.getAttribute("name");
			className = node.getAttribute("class");
			delay = Integer.parseInt(node.getAttribute("delay"), 10);
			interval = Integer.parseInt(node.getAttribute("interval"), 10);
		}
	}
	private static class PerfBlock
	{
		public PerfBlock(String name){this.name=name;execCount=1;totalMillis=0;}
		public String name;
		public long execCount;
		public long totalMillis;
	};

	public static boolean g_debugSQLQueries = false; 
	public static Logger g_logger;
	private static HashMap g_perfMap;
	private static String m_GenesysHome;
	private static String m_GenesysImageRoot;
	private static String m_GenesysS3Bucket;
	private static String m_GenesysSMTP, m_GenesysSMTPUsr, m_GenesysSMTPPwd;
	private static String m_GenesysFromEmail;
	private static String m_GenesysBccEmail;
	private static String m_dbConnStr;
	private static int m_sessionTimeoutMins;
	private static XMLDocument m_serverCFG = null;
	private static InterfaceCfg m_interfaceCfg = null;
	private static ObjectManager m_objManager = null;
	private static String m_loginPageImage = null;
	private static String m_serviceName = null;
	private static int m_defaultQueryCount = 10;
	private static int m_taskThreadCount = 5;
	private static String m_AnonymousUserid, m_AnonymousPassword, m_defaultPortal, m_homePage;
	private static ExecutorService m_taskProcessor;
	private static ScheduledExecutorService m_scheduledTaskProcessor;
	private static List<ScheduledTaskInfo> m_scheduledTasks;
	
	public static String getGenesysHome(){ return m_GenesysHome; }
	//public static String getGenesysImageRoot(){ return m_GenesysImageRoot; }
	public static String getGenesysS3Bucket(){ return m_GenesysS3Bucket; }
	public static String getGenesysSMTP(){ return m_GenesysSMTP; }
	public static String getGenesysSMTPUsr(){ return m_GenesysSMTPUsr; }
	public static String getGenesysSMTPPwd(){ return m_GenesysSMTPPwd; }
	public static String getGenesysFromEmail(){ return m_GenesysFromEmail; }
	public static String getGenesysBccEmail(){ return m_GenesysBccEmail; }
	public static XMLDocument getGenesysServerCfg(){ return m_serverCFG; }
	public static InterfaceCfg getGenesysInterfaceCfg(){ return m_interfaceCfg; }
	public static ObjectManager getObjectManager(){ return m_objManager; }
	public static String getloginPageImage(){ return m_loginPageImage; }
	public static String getServiceName(){ return m_serviceName; }
	public static int getdefaultQueryCount(){ return m_defaultQueryCount; }
	public static String getDBConnectStr(){ return m_dbConnStr; }
	public static int getsessionTimeoutMins(){ return m_sessionTimeoutMins; }
	public static String getAnonymousUserid()	{ return m_AnonymousUserid; }
	public static String getAnonymousPassword() {	return m_AnonymousPassword; }
	public static String getDefaultPortalName() { return m_defaultPortal; }
	public static String getHomePage() { return m_homePage; }

	public void contextInitialized(ServletContextEvent e)
	{
		try
		{
			ServletContext servContext = e.getServletContext();	// Application Level Scope
			m_scheduledTasks = new Vector<ScheduledTaskInfo>();
			g_perfMap = new HashMap();
	
			g_logger = Logger.getLogger(this.getClass());
			
			//g_logger = LoggerFactory.makeNewLoggerInstance("genesys");
			
			// Configure logging
			//String log4jConfFile = servContext.getInitParameter("LOG4J_CONF_FILE");
			//if(new File("log4j.properties").exists())
			//PropertyConfigurator.configure("log4j.xml");
			//else
			//	BasicConfigurator.configure();
			//URL url = ResourceUtils.getURL("log4j.xml");
			//if (location.toLowerCase().endsWith(XML_FILE_EXTENSION))
			//{
			//DOMConfigurator.configure(url);
			//URL url = Loader.getResource("log4j.xml");
			//DOMConfigurator.configure(url);
			//}
			//else
			//{
			//	PropertyConfigurator.configure(url);
			//}
			boolean bDebugLogging = false;
			// Special setting fdor SQL query debugging - fills the log very quickly
			String debugSQLQueries = servContext.getInitParameter("DEBUG_TRACE_LOG");
			if( debugSQLQueries.equalsIgnoreCase("high") )
			{
				bDebugLogging = true;
				g_debugSQLQueries = true;
			}
			else if( debugSQLQueries.equalsIgnoreCase("low") )
			{
				bDebugLogging = true;
			}
				
	
			//PropertyConfigurator.configure("log4j.xml");
	
			//g_logger.info("System started");
			//g_logger.debug("SystemServlet : contextInitialized called");
			
			//ServletContext thisContext = getServletConfig().getServletContext();
			//String rootAppPath = getServletConfig().getServletContext().getRealPath( "/" );
			//rootAppPath = webappPath.substring( 0, webappPath.length() - WEBAPP_NAME_LEN - 1 );
			//Map<String, String> env = System.getenv();
			String rootAppPath = System.getenv("GENESYS_HOME");
			if(rootAppPath == null)	rootAppPath = servContext.getInitParameter("GENESYS_HOME");
			//SystemServlet.g_logger.debug("SystemServlet : rootAppPath = " + rootAppPath);
			m_GenesysHome = new String(rootAppPath);
			//System.setProperty("GENESYS_HOME", rootAppPath);
			
//			String rootImagePath = System.getenv("GENESYS_IMAGE_ROOT");
//			if(rootImagePath == null) rootImagePath = servContext.getInitParameter("GENESYS_IMAGE_ROOT");
//			m_GenesysImageRoot = new String(rootImagePath);
			String rootS3Bucket = System.getenv("GENESYS_S3_BUCKET");
			if(rootS3Bucket == null) rootS3Bucket = servContext.getInitParameter("GENESYS_S3_BUCKET");
			m_GenesysS3Bucket = new String(rootS3Bucket);
			
			try
			{
				// Configure logging
				String pattern = "%d [%t] %-5p %c - %m%n";
				PatternLayout layout = new PatternLayout(pattern);
				
				// Error logs
				RollingFileAppender errorAppender = new RollingFileAppender(layout, m_GenesysHome + "WEB-INF/logs/genesys-errors.log");
				//errorAppender.setMaxFileSize("1MB");	// Use default 10MB
				LevelRangeFilter errFilter = new LevelRangeFilter();
				errFilter.setLevelMin(Level.WARN);
				errFilter.setLevelMax(Level.FATAL);
				errorAppender.addFilter(errFilter);
				g_logger.addAppender(errorAppender);
				
				// Information logs
				RollingFileAppender infoAppender = new RollingFileAppender(layout, m_GenesysHome + "WEB-INF/logs/genesys-info.log");
				//errorAppender.setMaxFileSize("1MB");	// Use default 10MB
				LevelRangeFilter infoFilter = new LevelRangeFilter();
				infoFilter.setLevelMin(Level.INFO);
				infoFilter.setLevelMax(Level.INFO);
				infoAppender.addFilter(infoFilter);
				g_logger.addAppender(infoAppender);
				
				if( bDebugLogging == true )
				{
					// Debug logs
					RollingFileAppender debugAppender = new RollingFileAppender(layout, m_GenesysHome + "WEB-INF/logs/genesys-debug.log");
					//errorAppender.setMaxFileSize("1MB");	// Use default 10MB
					LevelRangeFilter debugFilter = new LevelRangeFilter();
					debugFilter.setLevelMin(Level.DEBUG);
					debugFilter.setLevelMax(Level.DEBUG);
					debugAppender.addFilter(debugFilter);
					g_logger.addAppender(debugAppender);
					///////////////////////////////////////////////////////////////////
				}
			}
			catch(IOException ex)
			{
				System.err.print("Failed to initialize log4j");
			}
			
			g_logger.info("System started");
			g_logger.debug("SystemServlet : contextInitialized called");
			g_logger.debug("SystemServlet : rootAppPath = " + rootAppPath);
	
			
			m_serverCFG = new XMLDocument();
			m_serverCFG.loadXML( rootAppPath + "WEB-INF/cfg/server.cfg" );
			if( m_serverCFG.isNull() )
			{
				g_logger.fatal("SystemServlet : server.cfg failed to load from path " + rootAppPath);
			}
			else
			{
				m_AnonymousUserid = m_serverCFG.getElementValue("anonymous_userid");
				m_AnonymousPassword = m_serverCFG.getElementValue("anonymous_password");
				m_defaultPortal = m_serverCFG.getElementValue("default-portal");
				m_homePage = m_serverCFG.getElementValue("home-page");
	
				//System.setProperty("GENESYS_SMTP", m_serverCFG.selectElementValue( "//email/smtp-server" ));
				m_GenesysSMTP = m_serverCFG.selectElementValue( "//email/smtp-server" );
				m_GenesysSMTPUsr = m_serverCFG.selectElementValue( "//email/smtp-username" );
				m_GenesysSMTPPwd = m_serverCFG.selectElementValue( "//email/smtp-password" );
				m_GenesysFromEmail = m_serverCFG.selectElementValue( "//email/from-email" );
				m_GenesysBccEmail = m_serverCFG.selectElementValue( "//email/bcc-email" );
				
				m_loginPageImage = m_serverCFG.selectElementValue("//global_theme/login-page-image");
				m_serviceName = m_serverCFG.selectElementValue("//global_theme/service-name");
				m_defaultQueryCount = Integer.parseInt(m_serverCFG.selectElementValue("//default-query-count"));
				m_taskThreadCount = Integer.parseInt(m_serverCFG.selectElementValue("//task-thread-count"));
				//String loginPageImage = m_serverCFG.selectElementValue("//global_theme/login-page-image");
				//if( loginPageImage.charAt(0) == '/' )
				//	m_loginPageImage = servContext.getContextPath() + loginPageImage;
				//else
				//	m_loginPageImage = loginPageImage;
				
				String genEnv = System.getenv("GENESYS_ENV");
				if(genEnv == null)	genEnv = "development";

				String connStr = m_serverCFG.selectElementValue( "//database[@name='" + genEnv + "']/connectstring" );
				String dbName = m_serverCFG.selectElementValue( "//database[@name='" + genEnv + "']/dbname" );
				String userId = m_serverCFG.selectElementValue( "//database[@name='" + genEnv + "']/userid" );
				String password = m_serverCFG.selectElementValue( "//database[@name='" + genEnv + "']/password" );
				m_dbConnStr = "jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password;
				
				m_sessionTimeoutMins = Integer.parseInt( m_serverCFG.getElementValue( "session-timeout" ), 10 );
				
				
				
				//<scheduled-tasks>
		    	//	<task name="orderHandler" class="com.genesys.webmenus.OrderHandlerTask" interval="5"/>
		    	//</scheduled-jobs>
				XMLIterator iterTasks = new XMLIterator(m_serverCFG.getNodeList("//scheduled-tasks/task"));
				while(iterTasks.each()) m_scheduledTasks.add(new ScheduledTaskInfo(iterTasks.getNode()));
			}
			
			m_interfaceCfg = new InterfaceCfg();
			loadInterfaceCFG(rootAppPath);
			
			/////////////////////////////////////////////////////////
			// Load the object manager for this application
			////////////////////////////////////////////////////
			m_objManager = new ObjectManager();
			//////////////////////////////////////////////
	
			//g_logger.debug("Here is some DEBUG");
			//g_logger.info("Here is some INFO");
			//g_logger.warn("Here is some WARN");
			//g_logger.error("Here is some ERROR");
			//g_logger.fatal("Here is some FATAL");
			//SystemServlet.g_logger.error
			
			// create ExecutorService to manage threads
			m_taskProcessor = Executors.newFixedThreadPool(m_taskThreadCount);
			
			// Launch scheduled tasks
			m_scheduledTaskProcessor = Executors.newSingleThreadScheduledExecutor();
			Iterator<ScheduledTaskInfo> _iter_tasks = m_scheduledTasks.iterator();
			while( _iter_tasks.hasNext() )
			{
				ScheduledTaskInfo taskInfo = _iter_tasks.next();
				try
				{
					Class theClass  = Class.forName(taskInfo.className);
					Thread task = (Thread)theClass.newInstance();
					m_scheduledTaskProcessor.scheduleAtFixedRate(task, taskInfo.delay, taskInfo.interval, TimeUnit.SECONDS);
				}
				catch(Exception ex)
				{
					g_logger.error("SystemServlet : Schedule task class {" + taskInfo.className + "} threw " + ex.getClass().getName() + " exception - " + ex.getMessage() );
				}
			}
	
	//		String className = "com.genesys.webmenus.OrderHandlerTask";
	//		try
	//		{
	//			Class theClass  = Class.forName(className);
	//			Thread task = (Thread)theClass.newInstance();
	//			ScheduledExecutorService m_scheduledjobProcessor = Executors.newSingleThreadScheduledExecutor();
	//			m_scheduledjobProcessor.scheduleAtFixedRate(task, 5, 5, TimeUnit.SECONDS);
	//		}
	//		catch(Exception ex)
	//		{
	//			g_logger.error("SystemServlet : Schedule task class {" + className + "} threw " + ex.getClass().getName() + " exception - " + ex.getMessage() );
	//		}
		}
		catch(Exception e1)
		{
			g_logger.error("SystemServlet : " + e1.getMessage() );
		}
	}
	
	public static void processTask(Thread job)
	{
		m_taskProcessor.execute(job);
	}
	
	public void contextDestroyed(ServletContextEvent e)
	{
		m_scheduledTaskProcessor.shutdown();
		m_taskProcessor.shutdown(); // shutdown worker threads
		g_logger.info("System shutdown");
		g_logger.debug("SystemServlet : contextDestroyed called");
	}
	
	private static void loadInterfaceCFG(String rootAppPath)
	{
		XMLNodeList viewCfgs = m_serverCFG.getNodeList("//view-definitions/view-definition");
		if( viewCfgs != null )
		{
			XMLNode viewCfg = viewCfgs.getFirstNode();
			while( viewCfg != null )
			{
				String cfgPath = viewCfg.getAttribute("src");
				if( cfgPath.length() > 0 )
				{
					////////////////////////////////////////////////////////
					// Load interface
					XMLDocument interfaceCFG = new XMLDocument();
					interfaceCFG.loadXML( rootAppPath + cfgPath );
					if( interfaceCFG.isNull() ) SystemServlet.g_logger.fatal( cfgPath + " failed to load from path " + rootAppPath );
					m_interfaceCfg.append(interfaceCFG);
					
				}
				viewCfg = viewCfgs.getNextNode();
			}
		}
	}
	
	//////////////////////////////////////////////////////////////
	public static long enterCodeBlock(String blockName)
	{
		synchronized(g_perfMap)
		{
			PerfBlock block = (PerfBlock)g_perfMap.get(blockName);
			if(block != null)
			{
				block.execCount++;
			}
			else
			{
				block = new PerfBlock(blockName);
				g_perfMap.put(blockName, block);
			}
			return System.currentTimeMillis();
		}
	}
	public static void exitCodeBlock(String blockName, long startMillis)
	{
		synchronized(g_perfMap)
		{
			PerfBlock block = (PerfBlock)g_perfMap.get(blockName);
			if(block != null)
			{
				block.totalMillis += ( System.currentTimeMillis() - startMillis );
			}
		}
	}
	public static String printCodeBlocksHTML()
	{
		StringBuilder html = new StringBuilder();
		html.append("<diagnostics>");//<tr><th>name</th><th>count</th><th>time(ms)</th></tr>");
		synchronized(g_perfMap)
		{
	        Set blockKeys = g_perfMap.keySet();
	        Iterator It = blockKeys.iterator();
	        while(It.hasNext())
	        {
                String blockName = (String)(It.next());
                PerfBlock block = (PerfBlock)g_perfMap.get(blockName);
                html.append("<block>");
                
                html.append("<name>");
                html.append(block.name);
                html.append("</name>");
                
                html.append("<count>");
                html.append(Long.toString(block.execCount));
                html.append("</count>");

                html.append("<time>");
                html.append(Long.toString(block.totalMillis));
                html.append("</time>");
                
                html.append("</block>");
	        }
		}
		html.append("</diagnostics>");
		return html.toString();
	}
	///////////////////////////////////////////////////////////////////
}
