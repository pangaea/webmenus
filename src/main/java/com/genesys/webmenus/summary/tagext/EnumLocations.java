package com.genesys.webmenus.summary.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumLocations extends BodyTagSupport
{
	private RepositoryObjects oLocations;
	private int m_locationIdx = 0;
	private int m_locationCount = 0;
	private Credentials m_info = null;
	
	public int doStartTag() throws JspTagException
	{ 
		m_locationIdx = 0;
		m_locationCount = 0;
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				ObjectQuery queryLoc = new ObjectQuery("CELocation");
				queryLoc.setSortBy("name");	// TODO: Fix this - it should reference the property, not the column
				queryLoc.setSortOrder("ASC");
				QueryResponse qrLocations = objectBean.Query( m_info, queryLoc );
				oLocations = qrLocations.getObjects( queryLoc.getClassName() );
				if( oLocations != null && oLocations.count() > 0 )
				{
					m_locationCount = oLocations.count();
					if( updateVariables(oLocations.get(m_locationIdx++)) == true )
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
			if( m_locationIdx >= m_locationCount ) return SKIP_BODY;
			if( updateVariables(oLocations.get(m_locationIdx++)) == true )
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
			setPageAttribute( "locationIndex", new Integer(m_locationIdx-1).toString() );
			setPageAttribute( "locationId", obj.getId() );
			setPageAttribute( "locationName", obj.getPropertyValue("name") );
			setPageAttribute( "locationAddress", obj.getPropertyValue("address") );
			setPageAttribute( "locationCity", obj.getPropertyValue("city") );
			setPageAttribute( "locationState", obj.getPropertyValue("state") );
			setPageAttribute( "locationZip", obj.getPropertyValue("zip") );
			setPageAttribute( "locationPhone", obj.getPropertyValue("phone_num") );
			setPageAttribute( "locationLogo", obj.getPropertyValue("logo") );
			setPageAttribute( "locationThemeId", obj.getPropertyValue("theme") );
			if( m_locationIdx <= (m_locationCount) )
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
