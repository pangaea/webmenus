package com.genesys.webmenus.orders;

import javax.servlet.http.HttpServletRequest;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjectIterator;

public class OrderDetailsBean {
    private HttpServletRequest m_request = null;
    private String m_orderId = null;
    private String m_status = null;
    private String m_invoice = null;

    public OrderDetailsBean(){
	}

    public void loadOrderDetails(){
		if( m_request != null && m_orderId != null ){
			try{
                Credentials info = (Credentials)m_request.getSession().getAttribute( "info" );
                ObjectQuery queryObj = new ObjectQuery( "CCMenuOrder" );
                queryObj.addProperty("id", m_orderId);
                RepositoryObjectIterator locIter = new RepositoryObjectIterator(SystemServlet.getObjectManager().Query(info, queryObj));
			    if(locIter.each()){
                    RepositoryObject oLoc = locIter.getObj();
                    m_status = OrderStatusUtil.convertStatusToLabel(oLoc.getPropertyValue_Int("status"));
                    m_invoice = oLoc.getPropertyValue("invoice");
                }
			}
			catch(AuthenticationException e){
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
			}
		}
	}

    public void setOrderId( String orderId ){
		m_orderId = orderId;
	}

    public void setRequest(HttpServletRequest request){
		m_request = request;
	}

    public String getStatus() {
        return m_status;
    }

    public String getInvoice() {
        return m_invoice;
    }
}
