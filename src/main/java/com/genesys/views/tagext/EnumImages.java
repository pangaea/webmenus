package com.genesys.views.tagext;

import java.io.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.*;


import com.genesys.SystemServlet;
import com.genesys.repository.S3API;


public class EnumImages extends BodyTagSupport
{
	String m_sRelPath;
	String[] m_listImgs;
	private int m_imgIdx;
	private String prefix = "image_library/";
	
	public int doStartTag() throws JspTagException
	{
		try
		{
			m_listImgs = S3API.getFiles(prefix + m_sRelPath);

			// Setting up field-property and field-path maps
			m_imgIdx = 0;
			if( m_listImgs.length > 0 )
			{
				if( updateVariables( m_listImgs[m_imgIdx++] ) == true )//;	// Do first iteration here
				{
					return EVAL_BODY_INCLUDE;
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in doStartTag" );
		}

		return SKIP_BODY;
	}

	public int doAfterBody() throws JspTagException
	{
		try
		{
			// Iterate to the next node
			if( updateVariables( m_listImgs[m_imgIdx++] ) == true )
			{
				// There is another image left in the list - go again
				return EVAL_BODY_BUFFERED;
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in doAfterBody" );
		}
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException
	{
		return EVAL_PAGE;
	}
	
	private void setPageAttribute( String pageAttr, String inputAttr )
	{
		try
		{
			// Find value in node
			pageContext.setAttribute( pageAttr, inputAttr );
		}
		catch( Exception e )
		{
			pageContext.setAttribute( pageAttr, "" );
			SystemServlet.g_logger.error( "Expection caught in setPageAttribute" );
		}
	}

	private boolean updateVariables( String imageFileName )
	{
		try
		{
			String fileName = imageFileName.substring(prefix.length());
			setPageAttribute( "imageFileName", fileName );
			//setPageAttribute( "imageFilePath", m_sRelPath + "/" + imageFileName );
			setPageAttribute( "imageFilePath", fileName );
			if( m_imgIdx < (m_listImgs.length+1) )
				return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	public void setRelPath( String a_sRelPath )
	{
		m_sRelPath = a_sRelPath;
	}
}
