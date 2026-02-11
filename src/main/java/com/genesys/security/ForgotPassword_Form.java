package com.genesys.security;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.genesys.util.ServletUtilities;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.*;

public final class ForgotPassword_Form extends ActionForm
{
	private String _username = null;
	public String getUsername(){ return _username; }
	public void setUsername(String username){ _username = username; }
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		_username = "";//ServletUtilities.getCookieValue(request.getCookies(), "username", "");
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		if( null == _username || _username.length() == 0 )
		{
			errors.add("errors", new ActionMessage("account.error.username.missing"));
			return errors;
		}

		// Verify the username exists
		////////////////////////////////////////////////////////
		ObjectManager m_objectBean = SystemServlet.getObjectManager();
		Credentials info = new Credentials();
		if( m_objectBean.SystemLogin("admin", info ) == true )
		{
			try
			{
				ObjectQuery queryStmt = new ObjectQuery("CClient");
				queryStmt.addProperty("username", _username);
				QueryResponse qResponse = m_objectBean.Query( info, queryStmt );
				RepositoryObjects objs = qResponse.getObjects(queryStmt.getClassName());
				if( objs.count() == 0 )
					errors.add("errors", new ActionMessage("account.error.username.invalid"));
			}
			catch( Exception e ){}
			finally
			{
				m_objectBean.Logout(info);
			}
		}
		///////////////////////////////////////////////////////
		///////////////////////////////////////////

		return errors;
	}
}