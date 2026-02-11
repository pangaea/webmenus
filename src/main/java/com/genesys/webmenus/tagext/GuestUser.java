package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class GuestUser extends BodyTagSupport implements CredentialsContext
{
	private Credentials m_info = null;
	
	public int doStartTag() throws JspTagException
	{
		try
		{
			ObjectManager objectBean = SystemServlet.getObjectManager();
			if( objectBean != null )
			{
				m_info = new Credentials();
				if( objectBean.SystemLogin("guest", m_info ) == true )
				{
					pageContext.setAttribute( "guestCredentials", m_info );
					return EVAL_BODY_INCLUDE;	// Continue to execute
				}
				else
				{
					throw new JspTagException("GuestUser Tag failed to login as guest user");
				}
			}
			else
			{
				throw new JspTagException("GuestUser Tag failed to load object manager");
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
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException
	{
		ObjectManager objectBean = SystemServlet.getObjectManager();
		objectBean.Logout(m_info);
		return EVAL_PAGE;
	}
	
	public Credentials getCredentials()
	{
		return m_info;
	}

}
