package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumMenus extends GuestAccess
{
	private String m_locId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			// Query for the menu associated to the selected location
			ObjectQuery queryMenus = new ObjectQuery( "CCMenu" );
			queryMenus.setSortBy("menu_index");	// TODO: Fix this - it should reference the property, not the column
			queryMenus.setSortOrder("ASC");
			queryMenus.addProperty("hidden", "N");
			queryMenus.addProperty("location", m_locId);
			QueryResponse qrMenus = objectBean.Query( credContext.getCredentials(), queryMenus );
			return qrMenus.getObjects( queryMenus.getClassName() );
		}
		catch(AuthenticationException e)
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
		return null;
	}

	public void updateVariables( RepositoryObject obj )
	{
		try
		{
			setPageAttribute( "menuId", obj.getId() );
			setPageAttribute( "menuName", obj.getPropertyValue("name") );
			setPageAttribute( "menuIdx", Integer.toString(getIndex()-1) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
	}

	public void setLocId( String locId )
	{
		m_locId = locId;
	}
}
