package com.genesys.security;

import java.util.Vector;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.genesys.SystemServlet;
import com.genesys.util.email.Outbound;
import com.genesys.repository.*;

public class ForgotPassword_Action extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
		ForgotPassword_Form theForm = (ForgotPassword_Form)form;
		sendEmail(theForm.getUsername());
		return mapping.findForward("success");
	}
	
	private void sendEmail(String username)
	{
		// Verify the username exists
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
					String sId = obj.getId();
					String sEmailAddr = obj.getPropertyValue("emailaddr");

					// Generate new password and save
					RandomString randGen = new RandomString(8);
					String newPass = randGen.nextString();
					ObjectSubmit subObj = new ObjectSubmit("CClient");
					subObj.addProperty("password", newPass);
					m_objectBean.Update(info, sId, subObj);

					// Email new password to user email
					Vector toAddr = new Vector();
					toAddr.add(sEmailAddr);
					String systemFromEmail = SystemServlet.getGenesysFromEmail();
					Outbound.postMail(toAddr, systemFromEmail, null, false, "Support", "Your new password is: " + newPass, null);
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