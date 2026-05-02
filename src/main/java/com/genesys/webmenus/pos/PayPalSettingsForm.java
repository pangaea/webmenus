package com.genesys.webmenus.pos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;

public class PayPalSettingsForm extends ActionForm {
    private String _id = null;
	private String _clientId = null;
	private String _clientSecret = null;

    public PayPalSettingsForm() {
    }

	public String getId(){ return _id; }
	public void setId(String id){ _id = id; }

	public String getClientId(){ return _clientId; }
	public void setClientId(String clientId){ _clientId = clientId; }

	public String getClientSecret(){ return _clientSecret; }
	public void setClientSecret(String clientSecret){ _clientSecret = clientSecret; }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        _id = (String)request.getParameter("id");
		if(_id != null){
			HttpSession thisSession = request.getSession();
			Credentials info = (Credentials)thisSession.getAttribute("info");
			if( info != null){
				try{
					ObjectManager objectBean = SystemServlet.getObjectManager();
					ObjectQuery queryPM = new ObjectQuery("CCPaymentMethod");
					queryPM.addProperty("id", _id);
					QueryResponse qrPM = objectBean.Query( info, queryPM );
					RepositoryObjects oPMs = qrPM.getObjects( queryPM.getClassName() );
					if( oPMs.count() == 1 ){
						RepositoryObject oPM = oPMs.get(0);
                        String config = oPM.getPropertyValue("config");

                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode node = mapper.readTree(config);
                            setClientId(node.get("CLIENT_ID").asText());
                            setClientSecret(node.get("CLIENT_SECRET").asText());
                        }
                        catch (Exception e) {}
					}
                }
				catch(AuthenticationException e){
					SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
				}
            }
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)	{
        ActionErrors errors = new ActionErrors();
        if( null == _clientId || _clientId.length() == 0 )
			errors.add("clientId", new ActionMessage("paypal.error.clientId.missing"));
        if( null == _clientSecret || _clientSecret.length() == 0 )
			errors.add("clientSecret", new ActionMessage("paypal.error.clientSecret.missing"));
        return errors;
    }
}
