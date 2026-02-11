package com.genesys.webmenus.account;

import javax.servlet.http.*;
import java.util.regex.*;
import org.apache.struts.action.*;
import com.genesys.util.ServletUtilities;

public final class PersonalInfo_Form extends ActionForm
{
	private String _fname = null;
	private String _lname = null;
	private String _address = null;
	private String _city = null;
	private String _state = null;
	private String _email = null;
	private String _phonenum = null;
	
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

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		_fname = ServletUtilities.getCookieValue(request.getCookies(), "personal_firstName", "");
		_lname = ServletUtilities.getCookieValue(request.getCookies(), "personal_lastName", "");
		_address = ServletUtilities.getCookieValue(request.getCookies(), "personal_address", "");
		_city = ServletUtilities.getCookieValue(request.getCookies(), "personal_city", "");
		_state = ServletUtilities.getCookieValue(request.getCookies(), "personal_state", "");
		_email = ServletUtilities.getCookieValue(request.getCookies(), "personal_email", "");
		_phonenum = ServletUtilities.getCookieValue(request.getCookies(), "personal_phoneNum", "");
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		if( null == _fname || _fname.length() == 0 )
			errors.add("firstName", new ActionMessage("personal.error.firstname.missing"));
		
		if( null == _lname || _lname.length() == 0 )
			errors.add("lastName", new ActionMessage("personal.error.lastname.missing"));

		if( null == _address || _address.length() == 0 )
			errors.add("address", new ActionMessage("personal.error.address.missing"));
		
		if( null == _city || _city.length() == 0 )
			errors.add("city", new ActionMessage("personal.error.city.missing"));
		
		if( null == _state || _state.length() == 0 )
			errors.add("state", new ActionMessage("personal.error.state.missing"));
		
		if( null == _email || _email.length() == 0 )
			errors.add("email", new ActionMessage("personal.error.email.missing"));
		
		if( Pattern.matches("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", _email) == false )
			errors.add("email", new ActionMessage("personal.error.email.invalid"));
		
		if( null == _phonenum || _phonenum.length() == 0 )
			errors.add("phoneNum", new ActionMessage("personal.error.phonenum.missing"));

		if( Pattern.matches("^(1\\s*[-\\/\\.]?)?(\\((\\d{3})\\)|(\\d{3}))\\s*[-\\/\\.]?\\s*(\\d{3})\\s*[-\\/\\.]?\\s*(\\d{4})\\s*(([xX]|[eE][xX][tT])\\.?\\s*(\\d+))*$", _phonenum) == false )
			errors.add("phoneNum", new ActionMessage("personal.error.phonenum.invalid"));

		return errors;
	}
}