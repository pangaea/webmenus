package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumThemes extends BodyTagSupport
{
	private RepositoryObjects oThemes;
	private int m_themeIdx = 0;
	private int m_themeCount = 0;
	private Credentials m_info = null;
	
	public int doStartTag() throws JspTagException
	{ 
		m_themeIdx = 0;
		m_themeCount = 0;
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				ObjectQuery queryThemes = new ObjectQuery( "CETheme" );
				queryThemes.setSortBy("name");	// TODO: Fix this - it should reference the property, not the column
				queryThemes.setSortOrder("ASC");
				QueryResponse qrThemes = objectBean.Query( m_info, queryThemes );
				oThemes = qrThemes.getObjects( queryThemes.getClassName() );
				if( oThemes != null && oThemes.count() > 0 )
				{
					m_themeCount = oThemes.count();
					if( updateVariables(oThemes.get(m_themeIdx++)) == true )
						return EVAL_BODY_INCLUDE;	// Continue to execute
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
			if( m_themeIdx >= m_themeCount ) return SKIP_BODY;
			if( updateVariables(oThemes.get(m_themeIdx++)) == true )
				return EVAL_BODY_BUFFERED;
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
		}
	}

	private boolean updateVariables( RepositoryObject obj )
	{
		try
		{
			setPageAttribute( "themeId", obj.getId() );
			setPageAttribute( "themeName", obj.getPropertyValue("name") );
			if( m_themeIdx <= (m_themeCount) )
				return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	public void setCredentials( String ticket )
	{
		ObjectManager objectBean = SystemServlet.getObjectManager();
		if( objectBean != null )
		{
			Credentials info = new Credentials();
			info.m_sTicket = ticket;
			m_info = objectBean.verifyClientInfo(info);
		}
	}
}
