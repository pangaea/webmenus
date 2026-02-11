package com.genesys.wizards;

import javax.servlet.http.*;
import org.apache.struts.action.*;

public class RegistrationAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
		RegistrationForm rForm = (RegistrationForm)form;
		String email = rForm.getEmail();
		return mapping.findForward("success");
	}
}