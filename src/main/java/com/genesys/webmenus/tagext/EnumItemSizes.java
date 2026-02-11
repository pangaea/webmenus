package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumItemSizes extends GuestAccess
{
	private String m_itemId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			ObjectQuery queryMenuItemSize = new ObjectQuery( "CCMenuItemSize" );
			queryMenuItemSize.setSortBy("size_index");	// TODO: Fix this - it should reference the property, not the column
			queryMenuItemSize.setSortOrder("ASC");
			if( m_itemId != null ) queryMenuItemSize.addProperty("menuitem", m_itemId);
			QueryResponse qrMenuItemSize = objectBean.Query( credContext.getCredentials(), queryMenuItemSize );
			return qrMenuItemSize.getObjects( qrMenuItemSize.getClassName() );
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
			setPageAttribute( "itemSizeId", obj.getId() );
			setPageAttribute( "itemSizeDesc", obj.getPropertyValue("size_desc") );
			setPageAttribute( "itemSizePrice", obj.getPropertyValue("price") );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
	}
	
	public void setItemId( String itemId )
	{
		m_itemId = itemId;
	}
}
