///////////////////////////////////////
// Copyright (c) 2004-2012 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.repository;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

import com.genesys.SystemServlet;

public class ImageViewer extends HttpServlet
{
	public void init() throws ServletException
	{}
	
	// This method is called by the servlet container to process a GET request.
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    // Get the absolute path of the image
	    ServletContext sc = getServletContext();
	    
	    String resPath = req.getPathInfo();
	    String filename = "image_library/" + resPath.substring(1);

	    // Get the MIME type of the image
	    String mimeType = sc.getMimeType(filename);
	    if (mimeType == null) {
	        sc.log("Could not get MIME type of "+filename);
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        return;
	    }
	    
	    // RESPOND WITH REDIRECT
	    String locationPath = S3API.getSignedFileUrl(filename);
	    if(locationPath != null){
	    	resp.sendRedirect(locationPath);
	    }
	    else{
	        sc.log("Could not get signed path for "+filename);
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }

	    
	    
	    
	    // RESPOND WITH IMAGE CONTENT
	    
//	    // Set content type
//	    resp.setContentType(mimeType);
//	    ImageLibraryItem item = S3API.getFile(filename);
//
//	    // Set content size
//	    resp.setContentLength((int)item.size);
//
//	    // Open the file and output streams
//	    OutputStream out = resp.getOutputStream();
//
//	    // Copy the contents of the file to the output stream
//	    byte[] buf = new byte[1024];
//	    int count = 0;
//	    while ((count = item.content.read(buf)) >= 0) {
//	        out.write(buf, 0, count);
//	    }
//	    out.close();
	}
}