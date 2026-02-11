package com.genesys.webmenus.account;

import java.net.URLEncoder;
import java.util.ArrayList;
import javax.servlet.http.*;
//import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.*;

public class RestaurantInfo_Action extends Action
{/*
    public ActionForward populate(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	RestaurantInfo_Form inputForm = (RestaurantInfo_Form) form;
        ArrayList countryList = new ArrayList();
        countryList.add(new CountryData("1", "USA"));
        countryList.add(new CountryData("2", "Canada"));
        countryList.add(new CountryData("3", "Mexico"));
        inputForm.setCountryList(countryList);
        return mapping.findForward("success");
    }*/
    
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
		RestaurantInfo_Form rForm = (RestaurantInfo_Form)form;
		response.addCookie(new Cookie("restaurant_name", URLEncoder.encode(rForm.getName())));
		response.addCookie(new Cookie("restaurant_address", URLEncoder.encode(rForm.getAddress())));
		response.addCookie(new Cookie("restaurant_city", URLEncoder.encode(rForm.getCity())));
		response.addCookie(new Cookie("restaurant_state", URLEncoder.encode(rForm.getState())));
		response.addCookie(new Cookie("restaurant_zip", URLEncoder.encode(rForm.getZip())));
		response.addCookie(new Cookie("restaurant_timezone", URLEncoder.encode(rForm.getTimezone())));
		response.addCookie(new Cookie("restaurant_email", URLEncoder.encode(rForm.getEmail())));
		response.addCookie(new Cookie("restaurant_phoneNum", URLEncoder.encode(rForm.getPhoneNum())));
		response.addCookie(new Cookie("restaurant_sample_menus", URLEncoder.encode(rForm.getSample_menus())));
		
    	//RestaurantInfo_Form inputForm = (RestaurantInfo_Form)form;
//        ArrayList countryList = new ArrayList();
//        countryList.add(new CountryData("1", "USA"));
//        countryList.add(new CountryData("2", "Canada"));
//        countryList.add(new CountryData("3", "Mexico"));
        //inputForm.setCountryList(countryList);
//		HttpSession session = request.getSession();
//        session.setAttribute( "countryList", countryList);
        
		return mapping.findForward("next");
	}
}