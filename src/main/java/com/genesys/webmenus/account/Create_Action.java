package com.genesys.webmenus.account;

import java.util.*;
import java.net.URLEncoder;
import javax.servlet.http.*;
//import org.apache.struts.action.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Create_Action extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response ) throws Exception
	{
		Create_Form rForm = (Create_Form)form;
		try{
			response.addCookie(new Cookie("personal_firstName", URLEncoder.encode(rForm.getFirstName(), "UTF-8")));
			response.addCookie(new Cookie("personal_lastName", URLEncoder.encode(rForm.getLastName(), "UTF-8")));
			response.addCookie(new Cookie("personal_email", URLEncoder.encode(rForm.getEmail(), "UTF-8")));
			
			response.addCookie(new Cookie("account_username", URLEncoder.encode(rForm.getUsername(), "UTF-8")));
			//response.addCookie(new Cookie("account_password", URLEncoder.encode(rForm.getPassword(), "UTF-8")));
			//response.addCookie(new Cookie("account_accept", (rForm.getAcceptEula())?"yes":"no"));
			response.addCookie(new Cookie("restaurant_sample_menus", rForm.getSample_menus()));
			
			HashMap<String,String> accountParams = new HashMap<String,String>();
			accountParams.put("personal_firstName", 		rForm.getFirstName());
			accountParams.put("personal_lastName", 			rForm.getLastName());
			accountParams.put("personal_email", 			rForm.getEmail());
			accountParams.put("account_username", 			rForm.getUsername());
			accountParams.put("account_password", 			rForm.getPassword());
			accountParams.put("account_accept", 			(rForm.getAcceptEula()?"Y":"N"));
			accountParams.put("restaurant_name", 			"chowMagic Sample Restaurant");
			accountParams.put("restaurant_address",			"100 Broadway");
			accountParams.put("restaurant_city", 			"New York");
			accountParams.put("restaurant_state", 			"NY");
			accountParams.put("restaurant_zip", 			"11002");
			accountParams.put("restaurant_timezone",		"US/Eastern");
			accountParams.put("restaurant_email", 			rForm.getEmail());
			accountParams.put("restaurant_phoneNum", 		"(212) 111-2222");
			accountParams.put("restaurant_sample_menus",	rForm.getSample_menus());
			accountParams.put("allow_email",				"N");
			accountParams.put("restaurant_logo",			"/public/chowmagic-med.png");

			AccountGenerator account = new AccountGenerator(accountParams);
			if( account.generateAccount() == 0 ){
				request.getRequestDispatcher("/ViewCmd?call=login&userid="+rForm.getUsername()+"&password="+rForm.getPassword()+"&portal=main_console").forward(request, response);
				//return mapping.findForward("success");
			}
			else{
				return mapping.findForward("failure");
			}
		}
		catch( Exception e ){}
		return mapping.findForward("next");
	}
}