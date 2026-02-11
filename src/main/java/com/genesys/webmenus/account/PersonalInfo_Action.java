package com.genesys.webmenus.account;

import java.net.URLEncoder;
import javax.servlet.http.*;
import org.apache.struts.action.*;

public class PersonalInfo_Action extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
		PersonalInfo_Form rForm = (PersonalInfo_Form)form;
		response.addCookie(new Cookie("personal_firstName", URLEncoder.encode(rForm.getFirstName())));
		response.addCookie(new Cookie("personal_lastName", URLEncoder.encode(rForm.getLastName())));
		response.addCookie(new Cookie("personal_address", URLEncoder.encode(rForm.getAddress())));
		response.addCookie(new Cookie("personal_city", URLEncoder.encode(rForm.getCity())));
		response.addCookie(new Cookie("personal_state", URLEncoder.encode(rForm.getState())));
		response.addCookie(new Cookie("personal_email", URLEncoder.encode(rForm.getEmail())));
		response.addCookie(new Cookie("personal_phoneNum", URLEncoder.encode(rForm.getPhoneNum())));
		
		return mapping.findForward("next");
	}
}