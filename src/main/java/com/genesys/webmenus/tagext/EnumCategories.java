package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumCategories extends GuestAccess
{
	private String m_menuId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			ObjectQuery queryMenuCat = new ObjectQuery( "CCMenuCategory" );
			queryMenuCat.setSortBy("cat_index");	// TODO: Fix this - it should reference the property, not the column
			queryMenuCat.setSortOrder("ASC");
			queryMenuCat.addProperty("hidden", "N");
			queryMenuCat.addProperty("menu", m_menuId);
			QueryResponse qrMenuCat = objectBean.Query( credContext.getCredentials(), queryMenuCat );
			return qrMenuCat.getObjects( queryMenuCat.getClassName() );
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
			setPageAttribute( "catId", obj.getId() );
			setPageAttribute( "catName", obj.getPropertyValue("name") );
			setPageAttribute( "catIdx", Integer.toString(getIndex()-1) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
	}

	public void setMenuId( String menuId )
	{
		m_menuId = menuId;
	}
}
