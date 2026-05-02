package com.genesys.webmenus.pos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectSubmit;
import com.genesys.repository.RepositoryException;
import com.genesys.webmenus.account.ForwardParameters;

public class PayPalSettingsAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        PayPalSettingsForm rForm = (PayPalSettingsForm)form;

        HttpSession thisSession = request.getSession();
		Credentials info = (Credentials)thisSession.getAttribute( "info" );
		if( info != null){
			try{
				ObjectManager objectBean = SystemServlet.getObjectManager();
				ObjectSubmit paymentMethod = new ObjectSubmit("CCPaymentMethod");
                String config = "{\n\t\"CLIENT_ID\":\"" + rForm.getClientId() + "\",\n\t\"CLIENT_SECRET\":\"" + rForm.getClientSecret() + "\"\n}";
				paymentMethod.addProperty("config", config);
				if(rForm.getId() != null && rForm.getId().length() > 0){
					objectBean.Update(info, rForm.getId(), paymentMethod);
				}
			}
			catch(AuthenticationException e){
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
			}
			catch(RepositoryException e){
				SystemServlet.g_logger.error( "RepositoryException thrown - " + e.getErrMsg() );
			}
		}
        
        ActionForward forward = mapping.findForward("success");
		return new ForwardParameters().add("id", rForm.getId()).forward(forward);
    }
}
