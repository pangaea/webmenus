package com.genesys.webmenus.summary.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumSchedules extends BodyTagSupport
{
	private RepositoryObjects queryObjects;
	private int m_queryIdx = 0;
	private int m_queryCount = 0;
	private Credentials m_info = null;
	
	public int doStartTag() throws JspTagException
	{ 
		m_queryIdx = 0;
		m_queryCount = 0;
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				ObjectQuery query = new ObjectQuery("CCSchedule");
				query.setSortBy("name");	// TODO: Fix this - it should reference the property, not the column
				query.setSortOrder("ASC");
				QueryResponse queryResults = objectBean.Query( m_info, query );
				queryObjects = queryResults.getObjects( query.getClassName() );
				if( queryObjects != null && queryObjects.count() > 0 )
				{
					m_queryCount = queryObjects.count();
					if( updateVariables(queryObjects.get(m_queryIdx++)) == true )
						return EVAL_BODY_INCLUDE;	// Continue to execute
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in EnumSchedules::doStartTag" );
		}

		return SKIP_BODY;
	}

	public int doAfterBody() throws JspTagException
	{
		try
		{
			if( m_queryIdx >= m_queryCount ) return SKIP_BODY;
			if( updateVariables(queryObjects.get(m_queryIdx++)) == true )
				return EVAL_BODY_BUFFERED;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in EnumSchedules::doAfterBody" );
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
			setPageAttribute( "scheduleId", obj.getId() );
			setPageAttribute( "scheduleName", obj.getPropertyValue("name") );
			if( m_queryIdx <= (m_queryCount) )
				return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in EnumSchedules::updateVariables" );
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
