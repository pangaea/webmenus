package com.genesys.webmenus.account;

import javax.servlet.http.*;
import java.util.regex.*;
import org.apache.struts.action.*;

import com.genesys.util.ServletUtilities;
import com.genesys.SystemServlet;
import com.genesys.repository.*;
//import com.genesys.util.xml.*;

public final class Create_Form extends ActionForm
{
	private String _fname = null;
	private String _lname = null;
	private String _email = null;
	private String _username = null;
	private String _password = null;
	private String _password2 = null;
	private boolean _accept = false;
	private String _sample_menus = null;
	
	public String getFirstName(){ return _fname; }
	public void setFirstName(String fname){ _fname = fname; }
	
	public String getLastName(){ return _lname; }
	public void setLastName(String lname){ _lname = lname; }

	public String getEmail(){ return _email; }
	public void setEmail(String email){ _email = email; }
	
	public String getUsername(){ return _username; }
	public void setUsername(String username){ _username = username; }
	
	public String getPassword(){ return _password; }
	public void setPassword(String password){ _password = password; }
	
	public String getPassword2(){ return _password2; }
	public void setPassword2(String password2){ _password2 = password2; }
	
	public boolean getAcceptEula(){ return _accept; }
	public void setAcceptEula(boolean accept){ _accept = accept; }
	
	public String getSample_menus(){ return _sample_menus; }
	public void setSample_menus(String sample_menus){ _sample_menus = sample_menus; }
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		_fname = ServletUtilities.getCookieValue(request.getCookies(), "personal_firstName", "");
		_lname = ServletUtilities.getCookieValue(request.getCookies(), "personal_lastName", "");
		_email = ServletUtilities.getCookieValue(request.getCookies(), "personal_email", "");
		
		_username = ServletUtilities.getCookieValue(request.getCookies(), "account_username", "");
		//String accept = ServletUtilities.getCookieValue(request.getCookies(), "account_accept", "");
		//_accept = (accept.equalsIgnoreCase("yes"))?true:false;
		_sample_menus = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_sample_menus",	"");
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		if( null == _fname || _fname.length() == 0 )
			errors.add("firstName", new ActionMessage("personal.error.firstname.missing"));
		
		if( null == _lname || _lname.length() == 0 )
			errors.add("lastName", new ActionMessage("personal.error.lastname.missing"));

		if( null == _email || _email.length() == 0 )
			errors.add("email-missing", new ActionMessage("personal.error.email.missing"));
		
		if( Pattern.matches("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", _email) == false )
			errors.add("email-invalid", new ActionMessage("personal.error.email.invalid"));
		
		if( null == _username || _username.length() == 0 )
		{
			errors.add("username", new ActionMessage("account.error.username.missing"));
		}
		else
		{
			// Verify the username is unique
			////////////////////////////////////////////////////////
			ObjectManager m_objectBean = SystemServlet.getObjectManager();
			Credentials info = new Credentials();
			//if( m_objectBean.Login( "admin", "admin", info ) == true )
			if( m_objectBean.SystemLogin("guest", info ) == true )
			{
				try
				{
					ObjectQuery queryStmt = new ObjectQuery("CClient");
					queryStmt.addProperty("username", _username);
					QueryResponse qResponse = m_objectBean.Query( info, queryStmt );
					RepositoryObjects objs = qResponse.getObjects(queryStmt.getClassName());
					if( objs.count() > 0 )
						errors.add("username", new ActionMessage("account.error.username.exists"));
				}
				catch( Exception e ){}
				finally
				{
					m_objectBean.Logout(info);
				}
			}
			///////////////////////////////////////////////////////
			///////////////////////////////////////////
		}
		
		if( null == _password || _password.length() == 0 )
			errors.add("password", new ActionMessage("account.error.password.missing"));

		if( null == _password2 || _password2.equalsIgnoreCase(_password) == false )
			errors.add("password2", new ActionMessage("account.error.password.mismatch"));
		
		if( null == _sample_menus || _sample_menus.length() == 0 )
			errors.add("sample_menus", new ActionMessage("restaurant.error.sample_menus.missing"));

		if( _accept == false )
			errors.add("accept", new ActionMessage("account.error.accept.missing"));

		return errors;
	}
}