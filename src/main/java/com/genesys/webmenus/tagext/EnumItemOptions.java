package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumItemOptions extends GuestAccess
{
	private String m_itemId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			ObjectQuery queryOptions = new ObjectQuery( "CCMenuItemOption" );
			queryOptions.setSortBy("option_index");		// TODO: Fix this - it should reference the property, not the column
			queryOptions.setSortOrder("ASC");
			queryOptions.addProperty("menuitem",m_itemId);
			QueryResponse qrOptions = objectBean.Query( credContext.getCredentials(), queryOptions );
			return qrOptions.getObjects( queryOptions.getClassName() );
		}
		catch(AuthenticationException e)
		{
			SystemServlet.g_logger.error( "Expection caught in EnumItemOptions::queryObjects" );
		}
		return null;
	}

	public void updateVariables( RepositoryObject obj )
	{
		try
		{
			setPageAttribute( "optionId", obj.getId() );
			setPageAttribute( "optionName", obj.getPropertyValue("name") );
			setPageAttribute( "optionPrice", obj.getPropertyValue("price") );
			setPageAttribute( "optionType", obj.getPropertyValue("type") );
			setPageAttribute( "optionData", obj.getPropertyValue("data") );
			setPageAttribute( "optionIdx", Integer.toString(getIndex()-1) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in EnumItemOptions::updateVariables" );
		}
	}

	public void setItemId( String itemId )
	{
		m_itemId = itemId;
	}
}
