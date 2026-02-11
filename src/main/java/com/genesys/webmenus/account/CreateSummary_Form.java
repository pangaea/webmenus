package com.genesys.webmenus.account;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public final class CreateSummary_Form extends ActionForm
{
	/*
	private String _fname = null;
	private String _lname = null;
	private String _address = null;
	private String _city = null;
	private String _state = null;
	private String _email = null;
	private String _phonenum = null;
	private String _username = null;
	private String _password = null;
	
	public String getFirstName(){ return _fname; }
	public void setFirstName(String fname){ _fname = fname; }
	
	public String getLastName(){ return _lname; }
	public void setLastName(String lname){ _lname = lname; }
	
	public String getAddress(){ return _address; }
	public void setAddress(String address){ _address = address; }
	
	public String getCity(){ return _city; }
	public void setCity(String city){ _city = city; }
	
	public String getState(){ return _state; }
	public void setState(String state){ _state = state; }
	
	public String getEmail(){ return _email; }
	public void setEmail(String email){ _email = email; }
	
	public String getPhoneNum(){ return _phonenum; }
	public void setPhoneNum(String phonenum){ _phonenum = phonenum; }
	
	public String getUsername(){ return _username; }
	public void setUsername(String username){ _username = username; }
	
	public String getPassword(){ return _password; }
	public void setPassword(String password){ _password = password; }
	*/
	
	private boolean _emailMe = false;
	public boolean getEmailMe(){ return _emailMe; }
	public void setEmailMe(boolean emailMe){ _emailMe = emailMe; }

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		//if( null == _email )
		//{
		//	errors.add("email", new ActionMessage("reg.error.email.missing"));
		//}
		return errors;
	}
}