package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class GuestAccess extends BodyTagSupport
{
	private RepositoryObjects oObjects;
	private int m_objectsIdx = 0;
	private int m_objectsCount = 0;
	
	public int doStartTag() throws JspTagException
	{
		m_objectsCount = 0;
		m_objectsIdx = 0;
		try
		{
			CredentialsContext credContext = (CredentialsContext)TagSupport.findAncestorWithClass(this, CredentialsContext.class);
			if( credContext == null )
				throw new JspTagException("EnumMenus tag must be within a CredentialsContext tag");
			
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				oObjects = queryObjects(objectBean, credContext);
				if( oObjects != null && oObjects.count() > 0 )
				{
					m_objectsCount = oObjects.count();
					updateVariables(oObjects.get(m_objectsIdx++));
					if( m_objectsIdx <= (m_objectsCount) ) return EVAL_BODY_INCLUDE;
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
			if( m_objectsIdx < m_objectsCount )
			{
				updateVariables(oObjects.get(m_objectsIdx++));
				if( m_objectsIdx <= (m_objectsCount) ) return EVAL_BODY_BUFFERED;
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

	public void setPageAttribute( String pageAttr, String inputAttr )
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
	public int getIndex(){ return m_objectsIdx;	}
	public int getCount(){ return m_objectsCount;	}
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		return null;
	}

	public void updateVariables( RepositoryObject obj )
	{
	}
}
