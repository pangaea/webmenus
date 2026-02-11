///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.w3;

/*<Imports>*/
// Imported java classes
import java.io.*;
import java.math.BigDecimal;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Scanner;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.*;

/*</Imports>*/
public class ImportPages extends HttpServlet
{
    /**
     * Initializes a ServerMsg instance.
     *  Also sets its logging value from application init param.
     *  Also creates its logger if not done before.
     *
     */
	public void init() throws ServletException
	{
	}
	
	private void writeToScreen( PrintWriter out, String line )
	{
		//out.println( "<tr><td align='left' style='background-color:gold;'>" );
		//out.println( "#" );
		out.println( "<td align='left' style='background-color:gold;'>" );
		out.println( line );
		out.println( "</td></tr>" );
	}

	private String importJS(Credentials info, String name, String desc, String file )
						throws AuthenticationException, RepositoryException, Exception {
		String objId = null;
		ObjectQuery queryObj;
		ObjectManager objectBean = SystemServlet.getObjectManager();
		queryObj = new ObjectQuery("CW3JavaScript");
		queryObj.addProperty("name", name);
		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
		if(objIter.each()){ RepositoryObject obj = objIter.getObj(); objId = obj.getId(); }
		
		ObjectSubmit obj_submit = null;
		String rootAppPath = SystemServlet.getGenesysHome();
		obj_submit = new ObjectSubmit("CW3JavaScript");
		obj_submit.addProperty("name", name);
		obj_submit.addProperty("description", desc);
		obj_submit.addProperty("code", new Scanner(new File(rootAppPath + file)).useDelimiter("\\Z").next());
		if(objId == null){
			objId = objectBean.Insert(info, obj_submit);
		}
		else{
			objectBean.Update(info, objId, obj_submit);
		}
		return objId;
	}
	
	private String importCSS(Credentials info, String name, String desc, String file )
						throws AuthenticationException, RepositoryException, Exception {
		String objId = null;
		ObjectQuery queryObj;
		ObjectManager objectBean = SystemServlet.getObjectManager();
		queryObj = new ObjectQuery("CW3CSS");
		queryObj.addProperty("name", name);
		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
		if(objIter.each()){ RepositoryObject obj = objIter.getObj(); objId = obj.getId(); }
		
		ObjectSubmit obj_submit = null;
		String rootAppPath = SystemServlet.getGenesysHome();
		obj_submit = new ObjectSubmit("CW3CSS");
		obj_submit.addProperty("name", name);
		obj_submit.addProperty("description", desc);
		obj_submit.addProperty("body", new Scanner(new File(rootAppPath + file)).useDelimiter("\\Z").next());
		if(objId == null){
			objId = objectBean.Insert(info, obj_submit);
		}
		else{
			objectBean.Update(info, objId, obj_submit);
		}
		return objId;
	}
	
	private String importLink(Credentials info, String name, String title, String uri )
						throws AuthenticationException, RepositoryException, Exception {
		String objId = null;
		ObjectQuery queryObj;
		ObjectManager objectBean = SystemServlet.getObjectManager();
		queryObj = new ObjectQuery("CW3Link");
		queryObj.addProperty("name", name);
		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
		if(objIter.each()){ RepositoryObject obj = objIter.getObj(); objId = obj.getId(); }
		
		ObjectSubmit obj_submit = null;
		String rootAppPath = SystemServlet.getGenesysHome();
		obj_submit = new ObjectSubmit("CW3Link");
		obj_submit.addProperty("name", name);
		obj_submit.addProperty("title", title);
		obj_submit.addProperty("uri", uri);
		if(objId == null){
			objId = objectBean.Insert(info, obj_submit);
		}
		else{
			objectBean.Update(info, objId, obj_submit);
		}

		return objId;
	}
	
	private String importPage(Credentials info, String name, String title, String styles,
								String links, String scripts, String file )
							throws AuthenticationException, RepositoryException, Exception {
		String objId = null;
		ObjectQuery queryObj;
		ObjectManager objectBean = SystemServlet.getObjectManager();
		queryObj = new ObjectQuery("CW3Page");
		queryObj.addProperty("name", name);
		RepositoryObjectIterator objIter = new RepositoryObjectIterator(objectBean.Query(info, queryObj));
		if(objIter.each()){ RepositoryObject obj = objIter.getObj(); objId = obj.getId(); }
		
		ObjectSubmit obj_submit = null;
		String rootAppPath = SystemServlet.getGenesysHome();
		obj_submit = new ObjectSubmit("CW3Page");
		obj_submit.addProperty("name", name);
		obj_submit.addProperty("title", title);
		obj_submit.addProperty("viewname", "");
		obj_submit.addProperty("show_alerts", "N");
		obj_submit.addProperty("show_giftcerts", "N");
		obj_submit.addProperty("styles", styles);
		obj_submit.addProperty("links", links);
		obj_submit.addProperty("scripts", scripts);
		obj_submit.addProperty("content", new Scanner(new File(rootAppPath + file)).useDelimiter("\\Z").next());
		if(objId == null){
			objId = objectBean.Insert(info, obj_submit);
		}
		else{
			objectBean.Update(info, objId, obj_submit);
		}
		return objId;
	}

	/**
	 * Main entry point for all web requests
	 *
	 * @param request 			HttpServletRequest
	 * @param response 			HttpServletResponse
	 * @throws IOException
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response )
	                     throws IOException, ServletException
	{
		PrintWriter out = response.getWriter();
		out.println( "<html><head><title>Page Data Import</title></head><body>" );
		out.println( "<table style='background-color:yellow;'>" );
		out.println( "<tr>" );
		out.println( "<td colspan='1' align='center' style='font-size:16;'>Page Data Import Utility</td>" );
		out.println( "</tr>" );
		
		if(request.getSession().getAttribute("admin") != "Y"){
			writeToScreen(out, "Only an admin can access this page");
		}
		else{
			writeToScreen(out, "Import Started...");
			
			ObjectManager objectBean = SystemServlet.getObjectManager();
			Credentials info = new Credentials();
			if( objectBean.SystemLogin("guest", info ) == true )
			{
				try
				{
					writeToScreen(out, "Importing JavaScripts...");
					
					// Insert 'home_script' js
					String js_home_scripts = importJS(info,
													  "home_scripts",
													  "JavaScript for Home Page",
													  "WEB-INF/import/content/js/home_scripts.js");
					// Insert 'welcome' js
					String js_welcome = importJS(info,
							  					"welcome",
							  					"JavaScript for Welcome Page",
							  					"WEB-INF/import/content/js/welcome.js");
					// Insert 'beta_info' js
					String js_beta_info = importJS(info,
		  											"beta_info",
		  											"JavaScript for Beta Info Page",
		  											"WEB-INF/import/content/js/beta_info.js");
					
					
					writeToScreen(out, "Importing Stylesheets...");
					
					// Insert 'core_styles' css
					String css_core_styles = importCSS(info,
													"core_styles",
													"Core Styles",
													"WEB-INF/import/content/styles/core_styles.css");
					// Insert 'home' css
					String css_home = importCSS(info,
													"home",
													"Home Page CSS",
													"WEB-INF/import/content/styles/home.css");
					// Insert 'welcome' css
					String css_welcome = importCSS(info,
													"welcome",
													"Welcome Page CSS",
													"WEB-INF/import/content/styles/welcome.css");
					// Insert 'beta_styles' css
					String css_beta_styles = importCSS(info,
													"beta_styles",
													"Beta Info CSS",
													"WEB-INF/import/content/styles/beta_styles.css");

					
					writeToScreen(out, "Importing Page Links...");
					
					// Insert 'home' link
					String link_home = importLink(info, "home", "Home", "home");
					// Insert 'contact_us' link
					String link_contact_us = importLink(info, "contact_us", "Contact Us", "contact_us");
					// Insert 'details' link
					String link_details = importLink(info, "details", "Product Details", "product_details");
					// Insert 'free_account' link
					String link_free_account = importLink(info, "free_account", "Signup", "../app/account/create_account.jsp");
					// Insert 'login' link
					String link_login = importLink(info, "login", "Login", "../ui/login.jsp");

					
					writeToScreen(out, "Importing Pages...");
					
					// Insert 'home' page
					String page_home = importPage(info,
							"home", "Home", css_core_styles + ";" + css_home,
							link_home + ";" + link_details + ";" + link_contact_us + ";" + link_free_account + ";" + link_login,
							js_home_scripts,
							"WEB-INF/import/content/pages/home.html");

					// Insert 'contact_us' page
					String page_contact_us = importPage(info,
							"contact_us", "Contact Us", css_core_styles,
							link_home + ";" + link_details + ";" + link_contact_us + ";" + link_free_account + ";" + link_login,
							"",
							"WEB-INF/import/content/pages/contact_us.html");

					// Insert 'welcome' page
					String page_welcome = importPage(info,
							"welcome", "Welcome", css_welcome,
							"",
							js_welcome,
							"WEB-INF/import/content/pages/welcome.html");

					// Insert 'product_details' page
					String page_product_details = importPage(info,
							"product_details", "Product Details", css_core_styles,
							link_home + ";" + link_details + ";" + link_contact_us + ";" + link_free_account + ";" + link_login,
							"",
							"WEB-INF/import/content/pages/product_details.html");

					// Insert 'beta_info' page
					String page_beta_info = importPage(info,
							"beta_info", "Beta Info", css_beta_styles,
							"",
							js_beta_info,
							"WEB-INF/import/content/pages/beta_info.html");
					
					writeToScreen(out, "Import Complete!");
				}
				catch(AuthenticationException e){
					writeToScreen(out,"Exception thrown during import..." + e.getErrMsg() );
				}
				catch(RepositoryException e){
					writeToScreen(out,"Exception thrown during import..." + e.getErrMsg() );
				}
				catch( Exception e ){
					writeToScreen(out,"Exception thrown during import..." + e.getMessage() );
				}
				finally{
					objectBean.Logout(info);
				}
			}
			else{
				writeToScreen(out, "System Login Failed!");
			}
		}

		out.println( "</table></body></html>" );
	}

	public void doPost( HttpServletRequest request, HttpServletResponse response )
    					throws IOException, ServletException
	{
		PrintWriter out = response.getWriter();
		out.write( "<html><head><title>error</title></head><body>Invalid Request</body></html>" );
	}

	//public void destroy()
	//{
	//}
}
