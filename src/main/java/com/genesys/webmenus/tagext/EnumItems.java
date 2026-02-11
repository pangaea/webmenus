package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class EnumItems extends GuestAccess
{
	private String m_catId = null, m_itemId = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			ObjectQuery queryMenuItem = new ObjectQuery( "CCMenuItem" );
			queryMenuItem.setSortBy("item_index");	// TODO: Fix this - it should reference the property, not the column
			queryMenuItem.setSortOrder("ASC");
			queryMenuItem.addProperty("hidden", "N");
			if( m_catId != null ) queryMenuItem.addProperty("menucategory", m_catId);
			if( m_itemId != null ) queryMenuItem.addProperty("id", m_itemId);
			QueryResponse qrMenuItem = objectBean.Query( credContext.getCredentials(), queryMenuItem );
			return qrMenuItem.getObjects( qrMenuItem.getClassName() );
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
			setPageAttribute( "itemId", obj.getId() );
			setPageAttribute( "itemName", obj.getPropertyValue("name") );
			setPageAttribute( "itemDescription", obj.getPropertyValue("description") );
			//setPageAttribute( "itemHidden", obj.getPropertyValue("hidden") );
			String image = obj.getPropertyValue("image");
			if( image.indexOf(".gif") >= 0 ||
				image.indexOf(".jpeg") >= 0 ||
				image.indexOf(".jpg") >= 0 ||
				image.indexOf(".png") >= 0 ){
				setPageAttribute( "itemImage", image );
			}else{
				setPageAttribute( "itemImage", "" );
			}
			setPageAttribute( "itemSizeDescription", obj.getPropertyValue("size_desc") );
			setPageAttribute( "itemPrice", obj.getPropertyValue("price") );
			setPageAttribute( "itemIdx", Integer.toString(getIndex()-1) );
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
	}

	public void setCategoryId( String catId )
	{
		m_catId = catId;
	}
	
	public void setItemId( String itemId )
	{
		m_itemId = itemId;
	}
}
