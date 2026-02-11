package com.genesys.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.io.StringWriter;

import org.w3c.dom.Document;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.XMLDocument;
import com.genesys.util.xml.XMLNode;

public class ViewStub
{

	public ViewStub()
	{
		
	}
	
	public static String processSubView(String name)
	{
		try
		{
			if( name.length() == 0 ) return "";
			//return "<strong>Good Morning</strong> " + name;
			/*
			RequestDispatcher dispatcher = request.getRequestDispatcher("ViewCmd?view=" + name);
			dispatcher.forward(request,response);
			
	        String requestUrl = "http://10.178.1.90:8088/genesys/ViewCmd?view=" + name;
	        try {
	            URL url = new URL(requestUrl.toString());
	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	            String output = "";
	            String inputLine;
	            //System.out.println("-----RESPONSE START-----");
	            while ((inputLine = in.readLine()) != null) {
	            	output += inputLine;
	            }
	            in.close();
	            return output;
	            //System.out.println("-----RESPONSE END-----");
	            
	        } catch (IOException e) {
	            //e.printStackTrace();
	            return e.getMessage();
	        }*/
			String serverResponse = "";
			ObjectManager objMan = SystemServlet.getObjectManager();
			Credentials info = new Credentials();
			if( objMan.SystemLogin("admin", info ) == true )
			{
				try
				{
					InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
					InterfaceCfg.View viewNode = interfaceCfg.getView(name);
					if(viewNode==null) return "Invalid view name";
					ObjectQuery queryStmt = new ObjectQuery(viewNode.getClassName());
					
					//Document interfaceCFG = SystemServlet.getGenesysInterface().getDocument();
		//			XMLDocument interfaceCFG = new XMLDocument(SystemServlet.getGenesysInterface().getDocument());
		//			String query = "//view[@name='" + name + "']";
		//			XMLNode viewNode = interfaceCFG.getNodeList( query ).getFirstNode();
					//return viewNode.getAttribute( "class" );
		//			ObjectQuery queryStmt = new ObjectQuery( viewNode.getAttribute( "class" ) );
					//ObjectManager objMan = SystemServlet.getObjectManager();
					serverResponse = objMan.QueryXML( info, queryStmt );

				
		//			String xslt = viewNode.getAttribute( "transform" );
					String xslt = viewNode.getTransform();
					if( xslt == null || xslt.length() == 0 )
					{
						return serverResponse;
					}
					else
					{
						String xslUri = SystemServlet.getGenesysHome() + "templates/" + xslt;
						StringWriter out = new StringWriter();
						try
						{
							ViewCmd.ServeTemplate( xslUri, serverResponse, out );
							return out.toString();
						}
						catch(IOException e){return e.getMessage();}
					}
				}
				finally
				{
					objMan.Logout(info);
				}
			}
			return serverResponse;
		}
		catch(AuthenticationException ex)
		{
			return "Invalid Credentials";
		}
	}
}
