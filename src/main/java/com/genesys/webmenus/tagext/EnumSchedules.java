package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumSchedules extends BodyTagSupport
{
	private RepositoryObjects oSchedules;
	//private String m_locId = null;
	private int m_scheduleIdx = 0;
	private int m_scheduleCount = 0;
	private Credentials m_info = null;
	
	public int doStartTag() throws JspTagException
	{ 
		m_scheduleIdx = 0;
		m_scheduleCount = 0;
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				ObjectQuery querySchedules = new ObjectQuery( "CCSchedule" );
				querySchedules.setSortBy("name");	// TODO: Fix this - it should reference the property, not the column
				querySchedules.setSortOrder("ASC");
				//querySchedules.addProperty("hidden", "N");
				//querySchedules.addProperty("location", m_locId);
				QueryResponse qrSchedules = objectBean.Query( m_info, querySchedules );
				oSchedules = qrSchedules.getObjects( querySchedules.getClassName() );
				if( oSchedules != null && oSchedules.count() > 0 )
				{
					m_scheduleCount = oSchedules.count();
					if( updateVariables(oSchedules.get(m_scheduleIdx++)) == true )
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
			if( m_scheduleIdx >= m_scheduleCount ) return SKIP_BODY;
			if( updateVariables(oSchedules.get(m_scheduleIdx++)) == true )
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
		//ObjectManager objectBean = SystemServlet.getObjectManager();
		//objectBean.Logout(m_info);
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
			setPageAttribute( "scheduleDescription", obj.getPropertyValue("description") );
			if( m_scheduleIdx <= (m_scheduleCount) )
				return true;
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return false;
	}

	//public void setObjectManager( ObjectManager objectBean )
	//{
	//	m_objectBean = objectBean;
	//}
	
	//public void setCredentials( Credentials info )
	//{
	//	m_info = info;
	//}
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

	//public void setLocId( String locId )
	//{
	//	m_locId = locId;
	//}
}
