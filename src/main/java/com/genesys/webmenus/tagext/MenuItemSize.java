package com.genesys.webmenus.tagext;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class MenuItemSize extends GuestAccess
{
	private String m_itemSizeId = null;
	Credentials m_info = null;
	
	public RepositoryObjects queryObjects(ObjectManager objectBean, CredentialsContext credContext)
	{
		try
		{
			// Query menu item size here and get the rest of item when filling in variables
			ObjectQuery queryMenuItemSize = new ObjectQuery( "CCMenuItemSize" );
			queryMenuItemSize.addProperty("id", m_itemSizeId);
			m_info = credContext.getCredentials();
			QueryResponse qrMenuItemSizes = objectBean.Query( m_info, queryMenuItemSize );
			return qrMenuItemSizes.getObjects( queryMenuItemSize.getClassName() );
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
			// Query menu item
			ObjectManager objectBean = SystemServlet.getObjectManager();
			ObjectQuery queryMenuItem = new ObjectQuery( "CCMenuItem" );
			String item_id = obj.getPropertyValue("menuitem");
			if( item_id.equalsIgnoreCase("null") == true || item_id.length() == 0 ) item_id = obj.getId();
			queryMenuItem.addProperty("id", item_id );
			queryMenuItem.addProperty("hidden", "N");
			QueryResponse qrMenuItems = objectBean.Query( m_info, queryMenuItem );
			RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryMenuItem.getClassName() );
			if( oMenuItems.count() > 0 )
			{
				RepositoryObject oMenuItem = oMenuItems.get(0);
				
				setPageAttribute( "itemSizeId", obj.getId() );
				setPageAttribute( "itemName", oMenuItem.getPropertyValue("name") );
				setPageAttribute( "itemDescription", oMenuItem.getPropertyValue("description") );
				//setPageAttribute( "itemHidden", obj.getPropertyValue("hidden") );
				String image = oMenuItem.getPropertyValue("image");
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
				setPageAttribute( "itemId", item_id );

				// Query menu category and determine the menu id
				ObjectQuery queryMenuCat = new ObjectQuery( "CCMenuCategory" );
				queryMenuCat.addProperty("id", oMenuItem.getPropertyValue("menucategory") );
				QueryResponse qrMenuCats = objectBean.Query( m_info, queryMenuCat );
				RepositoryObjects oMenuCats = qrMenuCats.getObjects( queryMenuCat.getClassName() );
				if( oMenuCats.count() > 0 ){
					RepositoryObject oMenuCat = oMenuCats.get(0);
					String x = oMenuCat.getPropertyValue("menu");
					setPageAttribute( "itemMenuId", x );
				}
			}
		}
		catch( Exception e )
		{
			SystemServlet.g_logger.error( "Expection caught in updateVariables" );
		}
	}
	
	public void setItemSizeId( String itemSizeId )
	{
		m_itemSizeId = itemSizeId;
	}
}
