package com.genesys.webmenus.orders;

import javax.servlet.http.HttpServletRequest;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.QueryResponse;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjects;

public class OrderDashboardBean {
    private HttpServletRequest m_request = null;
    private String m_name = null;
    private String m_address = null;
    private String m_city = null;
    private String m_state = null;
    private String m_zip = null;
    private String m_phoneNum = null;

    public OrderDashboardBean() {

    }

    public void setRequest(HttpServletRequest request){
		m_request = request;
	}

    public void loadLocationDetails(String id){
        if( m_request != null ){
            try{
                Credentials info = (Credentials)m_request.getSession().getAttribute( "info" );
                // Pull informaion about location from obj man
                ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
                queryLoc.addProperty("id", id);
                QueryResponse qrLoc = SystemServlet.getObjectManager().Query( info, queryLoc );
                RepositoryObjects oLocs = qrLoc.getObjects( queryLoc.getClassName() );
                if( oLocs.count() >= 1 ) {
                    RepositoryObject oLoc = oLocs.get(0);
                    m_name = oLoc.getPropertyValue("name");
                    m_address = oLoc.getPropertyValue("address");
                    m_city = oLoc.getPropertyValue("city");
                    m_state = oLoc.getPropertyValue("state");
                    m_zip = oLoc.getPropertyValue("zip");
                    m_phoneNum = oLoc.getPropertyValue("phone_num");
                }
            }
            catch(AuthenticationException e){
                SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
            }
        }
    }

    public String getName() {
        return m_name;
    }

    public String getAddress() {
        return m_address;
    }

    public String getCity() {
        return m_city;
    }

    public String getState() {
        return m_state;
    }

    public String getZip() {
        return m_zip;
    }

    public String getPhone() {
        return m_phoneNum;
    }
}
