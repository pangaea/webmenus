package com.genesys.feedback;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.genesys.util.ServletUtilities;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.*;

public final class Feedback_Form extends ActionForm
{
	private String _subject = null, _body = null;
	public String getSubject(){ return _subject; }
	public void setSubject(String subject){ _subject = subject; }
	public String getBody(){ return _body; }
	public void setBody(String body){ _body = body; }
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		_subject = "";
		_body = "";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		if( null == _subject || _subject.length() == 0 )
			errors.add("errors", new ActionMessage("feedback.error.subject.missing"));
		return errors;
	}
}
