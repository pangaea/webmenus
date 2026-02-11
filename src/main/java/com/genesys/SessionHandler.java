package com.genesys;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;


public class SessionHandler implements HttpSessionListener
{
    public void sessionCreated(HttpSessionEvent event)
    {
        synchronized (this)
        {
        }
    }
 
    public void sessionDestroyed(HttpSessionEvent event)
    {
        synchronized (this)
        {
        	HttpSession thisSession = event.getSession();
        	Credentials info = (Credentials)thisSession.getAttribute("info");
        	if( info != null )
        	{
        		ObjectManager objectBean = SystemServlet.getObjectManager();
        		objectBean.Logout(info);
        	}
        }
    }
}
