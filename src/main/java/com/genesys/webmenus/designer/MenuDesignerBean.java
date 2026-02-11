///////////////////////////////////////
//Copyright (c) 2004-2012 Kevin Jacovelli
//All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus.designer;

import javax.servlet.http.HttpServletRequest;
import com.genesys.SystemServlet;
import com.genesys.repository.*;


public class MenuDesignerBean
{
	private String m_err = null;
	private String m_loc = null;
	private String m_menu = null;
	private String m_themeTemplate = "";
	private String m_theme = "";
	HttpServletRequest m_request = null;
	
	public MenuDesignerBean(){
	}
	
	public void Process(){
		if( m_loc == null ){
			try{
				ObjectQuery queryMenus = new ObjectQuery("CCMenu");
				queryMenus.addProperty("id", m_menu);
				Credentials info = (Credentials)m_request.getSession().getAttribute( "info" );
				RepositoryObjectIterator menuIter = new RepositoryObjectIterator(SystemServlet.getObjectManager().Query(info, queryMenus));
				if(menuIter.each()){
					RepositoryObject oMenu = menuIter.getObj();
					m_loc = oMenu.getPropertyValue("location");
				}
				
				// Pull informaion about location from obj man
				ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
				queryLoc.addProperty("id", m_loc);
				RepositoryObjectIterator locIter = new RepositoryObjectIterator(SystemServlet.getObjectManager().Query(info, queryLoc));
				if(locIter.each()){
					RepositoryObject oLoc = locIter.getObj();
					m_themeTemplate = oLoc.getPropertyValue("theme.template");
				}
			}
			catch(AuthenticationException e){
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
				m_err = e.getErrMsg();
			}
		}
	}
	
	public void setRequest(HttpServletRequest request){
		m_request = request;
		setInternals();
	}
	public void setLoc( String loc ){
		m_loc = loc;
		setInternals();
	}
	public String getLoc(){ return m_loc; }
	public void setMenu( String menu ){ m_menu = menu; }
	public String getMenu(){ return m_menu; }
	public String getError(){ return m_err; }
	public String getThemeTemplate(){ return m_themeTemplate; }
	public String getTheme(){ return m_theme; }
	
	private void setInternals(){
		if( m_loc == null || m_request == null ) return;
		try{
			// Pull informaion about location from obj man
			ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
			queryLoc.addProperty("id", m_loc);
			Credentials info = (Credentials)m_request.getSession().getAttribute( "info" );
			RepositoryObjectIterator locIter = new RepositoryObjectIterator(SystemServlet.getObjectManager().Query(info, queryLoc));
			if(locIter.each()){
				RepositoryObject oLoc = locIter.getObj();
				m_theme = oLoc.getPropertyValue("theme.id");
				m_themeTemplate = oLoc.getPropertyValue("theme.template");
			}
		}
		catch(AuthenticationException e){
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
			m_err = e.getErrMsg();
		}
	}
}