package com.genesys.feedback;

import java.util.Vector;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.genesys.SystemServlet;
import com.genesys.util.email.Outbound;
import com.genesys.repository.*;

public class Feedback_Action extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
		Feedback_Form theForm = (Feedback_Form)form;
		Credentials info = (Credentials)request.getSession().getAttribute("info");
		String currentUser = info.m_UserName;
		sendEmail(currentUser, theForm.getSubject(), theForm.getBody());
		return mapping.findForward("success");
	}
	
	private void sendEmail(String username, String subject, String body)
	{
		////////////////////////////////////////////////////////
		ObjectManager m_objectBean = SystemServlet.getObjectManager();
		Credentials info = new Credentials();
		if( m_objectBean.SystemLogin("admin", info ) == true )
		{
			try
			{
				ObjectQuery queryStmt = new ObjectQuery("CUser");
				queryStmt.addProperty("username", username);
				QueryResponse qResponse = m_objectBean.Query( info, queryStmt );
				if( qResponse == null ) return;
				RepositoryObjects objs = qResponse.getObjects(queryStmt.getClassName());
				if( objs.count() > 0 )
				{
					RepositoryObject obj = objs.get(0);
					String sEmailAddr = obj.getPropertyValue("emailaddr");

					// Email new password to user email
					Vector<String> toAddr = new Vector<String>();
					String systemFromEmail = SystemServlet.getGenesysFromEmail();
					toAddr.add(systemFromEmail);
					Outbound.postMail(toAddr, systemFromEmail, sEmailAddr, true, subject, body + "\n\n-- " + sEmailAddr, null);
				}
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
}