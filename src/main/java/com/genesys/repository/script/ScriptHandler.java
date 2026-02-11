package com.genesys.repository.script;

import java.io.*;
import javax.script.*;

import com.genesys.SystemServlet;

public class ScriptHandler
{
	ScriptEngineManager m_mgr;
	public ScriptHandler()
	{
		m_mgr = new ScriptEngineManager();
	}
	public boolean ExecuteScript( EventCallback event, ServerCallback server, String sScriptFile )
	{
		boolean bRet = false;
		
		try
		{
			//ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine jsEngine = m_mgr.getEngineByName("JavaScript");
			Invocable invocableEngine = (Invocable)jsEngine;
			//String home = System.getProperty("user.dir");
			//sResponse = home;
			//File testFile = new File("test");
			//sResponse = testFile.getAbsolutePath();
			//BeanInfo info = Introspector.getBeanInfo("MathBean");
	
			// TODO: Remove file caching issues (changes aren't showing unless tomcat is re-started)
			//sResponse += "<name>" + m_scriptsRoot + "/proc.js" + "</name>";

			//String sScriptFile = m_scriptsRoot + script;
			SystemServlet.g_logger.debug( "Load Script: " + sScriptFile );
			//InputStream is =  this.getClass().getResourceAsStream(sScriptFile);
			InputStream is = new FileInputStream(sScriptFile);
			//InputStream is =  this.getClass().getResourceAsStream("C:\\home\\Datasphere\\webapp\\WEB-INF\\scripts\\proc.js");
			if( is == null ) throw new Exception("Failed to load script: " + sScriptFile);
			//List<String> namesList = new ArrayList<String>();
			//namesList.add("Kevin");
			//namesList.add("Terri");
			//ServerCallback server_callback = new ServerCallback(this);
			jsEngine.put("server", server);
			//jsEngine.put("server", callback);
			SystemServlet.g_logger.debug("Executing in script environment...");
			try
			{
				Reader reader = new InputStreamReader(is);
				jsEngine.eval(reader);
				invocableEngine.invokeFunction("eventHandler", event);
				//reader.close();
				bRet = true;	// Success
			}
			catch(ScriptException ex)
			{
				SystemServlet.g_logger.error( ex.getMessage() );
				//ex.printStackTrace();
			}
			catch(NoSuchMethodException ex)
			{
				SystemServlet.g_logger.error( ex.getMessage() );
				//ex.printStackTrace();
			}
			//SystemServlet.g_logger.debug("Executing in Java environment...");
			//for(String name: namesList)
			//{
				//sResponse += "<name>" + name + "</name>";
			//}
	
			try
			{
				is.close();
			}
			catch(IOException ex)
			{
				SystemServlet.g_logger.error( ex.getMessage() );
				//ex.printStackTrace();
			}
		}
		catch(Exception e)
		{
			SystemServlet.g_logger.error( "Exception Caught in ScriptHandler::ExecuteScript: " + e.getMessage() );
		}
		
		return bRet;
	}
}