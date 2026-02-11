package com.genesys.webmenus.account;

import javax.servlet.http.*;
//import java.util.ArrayList;
import java.util.regex.*;
import org.apache.struts.action.*;
import com.genesys.util.ServletUtilities;

public final class RestaurantInfo_Form extends ActionForm
{
	private String _name = null;
	private String _address = null;
	private String _city = null;
	private String _state = null;
	private String _zip = null;
	private String _timezone = null;
	private String _email = null;
	private String _phonenum = null;
	private String country;
	//private ArrayList countryList;
	private String _sample_menus = null;
	
	public String getName(){ return _name; }
	public void setName(String name){ _name = name; }
	
	public String getAddress(){ return _address; }
	public void setAddress(String address){ _address = address; }
	
	public String getCity(){ return _city; }
	public void setCity(String city){ _city = city; }
	
	public String getState(){ return _state; }
	public void setState(String state){ _state = state; }
	
	public String getZip(){ return _zip; }
	public void setZip(String zip){ _zip = zip; }
	
	public String getTimezone(){ return _timezone; }
	public void setTimezone(String timezone){ _timezone = timezone; }

	public String getPhoneNum(){ return _phonenum; }
	public void setPhoneNum(String phonenum){ _phonenum = phonenum; }

	public String getEmail(){ return _email; }
	public void setEmail(String email){ _email = email; }
	
	
    public RestaurantInfo_Form() {
        super();
        // TODO Auto-generated constructor stub
    }
//    public String getCountry() {
//        return country;
//    }
//    public void setCountry(String country) {
//        this.country = country;
//    }
//    public ArrayList getCountryList() {
//        return countryList;
//    }
//    public void setCountryList(ArrayList countryList) {
//        this.countryList = countryList;
//    }

	public String getSample_menus(){ return _sample_menus; }
	public void setSample_menus(String sample_menus){ _sample_menus = sample_menus; }

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		_name = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_name", 					"Sample Restaurant");
		_address = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_address",				"100 Broadway");
		_city = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_city", 					"New York");
		_state = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_state", 					"NY");
		_zip = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_zip", 						"11002");
		_timezone = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_timezone",			"US/Eastern");
		_email = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_email", 					"orders@sample.com");
		_phonenum = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_phoneNum", 			"(212) 111-2222");
		_sample_menus = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_sample_menus",	"");
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		if( null == _name || _name.length() == 0 )
			errors.add("name", new ActionMessage("restaurant.error.name.missing"));
		
		if( null == _address || _address.length() == 0 )
			errors.add("address", new ActionMessage("restaurant.error.address.missing"));
		
		if( null == _city || _city.length() == 0 )
			errors.add("city", new ActionMessage("restaurant.error.city.missing"));
		
		if( null == _state || _state.length() == 0 )
			errors.add("state", new ActionMessage("restaurant.error.state.missing"));
		
		if( null == _zip || _zip.length() == 0 )
			errors.add("zip", new ActionMessage("restaurant.error.zip.missing"));
		
		if( Pattern.matches("^(\\d{5}-\\d{4})|(\\d{5})$", _zip) == false )
			errors.add("zip", new ActionMessage("restaurant.error.zip.invalid"));
		
		if( null == _timezone || _timezone.length() == 0 )
			errors.add("timezone", new ActionMessage("restaurant.error.timezone.missing"));
		
		if( null == _sample_menus || _sample_menus.length() == 0 )
			errors.add("sample_menus", new ActionMessage("restaurant.error.sample_menus.missing"));
		
		if( null == _email || _email.length() == 0 )
			errors.add("email", new ActionMessage("restaurant.error.email.missing"));
		
		if( Pattern.matches("[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", _email) == false )
			errors.add("email", new ActionMessage("restaurant.error.email.invalid"));

		if( null == _phonenum || _phonenum.length() == 0 )
			errors.add("phoneNum", new ActionMessage("restaurant.error.phonenum.missing"));
		
		if( Pattern.matches("^(1\\s*[-\\/\\.]?)?(\\((\\d{3})\\)|(\\d{3}))\\s*[-\\/\\.]?\\s*(\\d{3})\\s*[-\\/\\.]?\\s*(\\d{4})\\s*(([xX]|[eE][xX][tT])\\.?\\s*(\\d+))*$", _phonenum) == false )
			errors.add("phoneNum", new ActionMessage("restaurant.error.phonenum.invalid"));

		return errors;
	}
}