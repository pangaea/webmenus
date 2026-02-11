///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////
package com.genesys.repository;

import java.io.*;
//import java.sql.*;
import java.util.*;
//import java.text.*;
//import java.util.regex.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.genesys.SystemServlet;
import com.genesys.session.ClientSessionBean;

public class ImageManager extends HttpServlet
{	
    /**
     * Load/create instance of ClientSessionBean
     *
     */
	ClientSessionBean loadClientBean(HttpServletRequest request)
	{
		ClientSessionBean clientBean = null;
		
		// Instantiate clientBean by loading an ClientSessionBean bean at session scope
		/////////////////////////////
		// Loading and accessing a bean from a servlet
		//////////////////////////////////////////////////////
		try
		{
			HttpSession beanContext = request.getSession();	// Session Level Scope
			synchronized (beanContext)
			{
				clientBean = (ClientSessionBean) beanContext.getAttribute("ClientSessionBean");
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( e.getMessage() );
		}
		///////////////////
		//////////////
		
		return clientBean;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			ClientSessionBean clientBean = loadClientBean(request);
			if( clientBean == null )
			{
				response.sendRedirect("ui/login.jsp");
			}
			
			String op = request.getParameter("op");
			String imgName = request.getParameter("name");
			String retQuery = request.getParameter("ret");
			if(op.equalsIgnoreCase("del"))
			{
				if( clientBean.m_info.m_bAdmin == false && imgName.contains("public/")  )
					throw new Exception("You have insufficient right to access the public image library");
				if( clientBean.m_info.m_bAdmin == false && imgName.contains("personal/" + clientBean.m_info.m_RoleId) == false )
					throw new Exception("You can only access your own image library");
//				String imgPath = SystemServlet.getGenesysImageRoot() + "/";
				
//				File doomedFile = new File(imgPath+imgName);
//				if( doomedFile.isFile() == true )
//					doomedFile.delete();
				
				S3API.deleteFile("image_library/"+imgName);
			}
			response.sendRedirect("ui/image_library.jsp?" + retQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{/*
		try
		{
			ClientSessionBean clientBean = loadClientBean(request);
			if( clientBean == null )
			{
				response.sendRedirect("ui/login.jsp");
			}
			
			String imgType = request.getParameter("type");
			String retQuery = request.getParameter("ret");
			
			if( imgType.equalsIgnoreCase("image") == true && clientBean.m_info.m_bAdmin == false )
				throw new Exception("You have insufficient right to access the public image library");
			
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart)
			{
				// Do nothing
				//System.out.println("File Not Uploaded");
			}
			else
			{
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = null;
	
				try
				{
					items = upload.parseRequest(request);
				}
				catch (FileUploadException e)
				{
					e.printStackTrace();
				}
				Iterator itr = items.iterator();
				while (itr.hasNext())
				{
					FileItem item = (FileItem) itr.next();
					if (item.isFormField())
					{
						// *** Ignore for now ***
						//String name = item.getFieldName();
						//System.out.println("name: "+name);
						//String value = item.getString();
						//System.out.println("value: "+value);
					}
					else
					{
						try
						{
							String itemName = item.getName();
							String fileName = new File(itemName).getName();
	
							String imgPath = "";//SystemServlet.getGenesysImageRoot() + "/";
							if( imgType.equalsIgnoreCase("image") == true )
							{
								imgPath = "image_library/public/";
							}
							else if( imgType.equalsIgnoreCase("myimage") == true )
							{
								imgPath = "image_library/personal/" + clientBean.m_info.m_RoleId + "/";
								
								// Create multiple directories
//								File perIMGDir = new File(imgPath);
//								if( perIMGDir.isDirectory() == false )
//								{
//								    boolean success = perIMGDir.mkdirs();
//								    if(!success)
//								    {
//								      //System.out.println("Directories: " + strManyDirectories + " created");
//								    	SystemServlet.g_logger.error( "Failed to create personal library : " + imgPath );
//								    }
//								}
							}
							
//							File savedFile = new File(imgPath+fileName);
//							item.write(savedFile);
							S3API.uploadFile(imgPath+fileName, item.getInputStream());
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			response.sendRedirect("ui/image_library.jsp?" + retQuery);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}
}