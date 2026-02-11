package com.genesys.webmenus;


import java.math.BigDecimal;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectSubmit;
import com.genesys.repository.RepositoryException;
import com.genesys.util.xml.XMLNode;
import com.genesys.util.xml.XMLNodeList;

public class MenuBuilder
{
	ObjectManager m_objectBean = null;
	
	public MenuBuilder()
	{
		m_objectBean = SystemServlet.getObjectManager();
	}
	
	//private String getChildNodeValue(XMLNode itemNode, String tagName)
	//{
	//	XMLNode childNode = itemNode.getSingleNode(tagName);
	//	if( childNode == null )
	//		return new String("");
	//	else
	//		return childNode.getValue();
	//}

	public String importOption(Credentials info, String itemId, XMLNode optionNode)
	{
		//String optionName = optionNode.getAttribute( "name" );
		//String optionPrice = optionNode.getAttribute( "price" );
		//String optionType = optionNode.getAttribute( "type" );
		//String optionData = optionNode.getValue();
		//ObjectSubmit item = new ObjectSubmit("CCMenuItemOption");
		//item.addProperty("name", optionName);
		//item.addProperty("price", optionPrice);
		//item.addProperty("type", optionType);
		//item.addProperty("data", optionData);
		//item.addProperty("menuitem", itemId);
		//item.addProperty("option_index", Integer.parseInt(optionNode.getAttribute("index")));
		//return m_objectBean.Insert(info, item);
		
		ObjectSubmit itemOption = new ObjectSubmit("CCMenuItemOption");
		itemOption.addProperty("name", optionNode.getAttribute("name"));
		try{
			BigDecimal bdPrice = new BigDecimal(optionNode.getAttribute("price"));
			itemOption.addProperty("price", bdPrice.doubleValue());
		}
		catch(Exception e){
			itemOption.addProperty("price", 0.00);
		}
		//BigDecimal bdPrice = new BigDecimal(optionNode.getAttribute("price"));
		//itemOption.addProperty("price", bdPrice.doubleValue());
		itemOption.addProperty("type", optionNode.getAttribute("type"));
		try{
			itemOption.addProperty("option_index", Integer.parseInt(optionNode.getAttribute("index")));
		}
		catch(Exception e){
			itemOption.addProperty("option_index", 0);
		}
		
		// Import option data body by first cleaning it up
		String adjData = new String("");
		String optionData = optionNode.getValue();
		String results[] = optionData.trim().split("\n");
		for(int ii =0; ii < results.length; ii++)
		{
			String sOptionTxt = results[ii].trim();
			if( sOptionTxt.length() == 0 ) continue;
			if( adjData.length() > 0 ) adjData += "\n";
			adjData += sOptionTxt;
		}
		itemOption.addProperty("data", adjData);
		itemOption.addProperty("menuitem", itemId);
		try
		{
			return m_objectBean.Insert(info, itemOption);
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importOption} - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importOption} - " + ex.getErrMsg() );
		}
		return null;
	}
	
	public String importItem(Credentials info, String catId, XMLNode itemNode, boolean includeChildren)
	{
		String itemName = itemNode.getChildNodeValue( "name" );
		String itemDesc = itemNode.getChildNodeValue( "description" );
		String itemImage = itemNode.getChildNodeValue( "image" );

		// Import menu object
		ObjectSubmit item = new ObjectSubmit("CCMenuItem");
		
		// Set properties for object
		item.addProperty("name", itemName);
		item.addProperty("description", itemDesc);
		item.addProperty("image", itemImage);

		XMLNodeList sizeList = itemNode.getNodeList("portions/size");
		if( sizeList.getCount() > 0 )
		{
			XMLNode sizeNode = sizeList.getNodeByIndex(0);
			try{
				BigDecimal bdPrice = new BigDecimal(sizeNode.getAttribute("price"));
				item.addProperty("price", bdPrice.doubleValue());
			}
			catch(Exception e){
				item.addProperty("price", 0.00);
			}
			item.addProperty("size_desc", sizeNode.getValue());
		}

		item.addProperty("menucategory", catId);
		//item.addProperty("hidden", "N");
		String hidden = itemNode.getAttribute("hidden");
		if( hidden.equalsIgnoreCase("Y") ) item.addProperty("hidden", "Y");
		else item.addProperty("hidden", "N");
		
		try{
			item.addProperty("item_index", Integer.parseInt(itemNode.getAttribute("index")));
		}
		catch(Exception e){
			item.addProperty("item_index", 0);
		}
		
		String itemId = null;
		try
		{
			itemId = m_objectBean.Insert(info, item);
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importItem} - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importItem} - " + ex.getErrMsg() );
			return null;
		}
		
		// Add additional portions/prices
		for( int i = 1; i < sizeList.getCount(); i++ )
		{
			ObjectSubmit itemPortion = new ObjectSubmit("CCMenuItemSize");
			XMLNode sizeNode = sizeList.getNodeByIndex(i);
			try{
				BigDecimal bdPrice = new BigDecimal(sizeNode.getAttribute("price"));
				itemPortion.addProperty("price", bdPrice.doubleValue());
			}
			catch(Exception e){
				itemPortion.addProperty("price", 0.00);
			}
			//BigDecimal bdPrice = new BigDecimal(sizeNode.getAttribute("price"));
			//itemPortion.addProperty("price", bdPrice.doubleValue());
			itemPortion.addProperty("size_desc", sizeNode.getValue());
			try{
				itemPortion.addProperty("size_index", Integer.parseInt(sizeNode.getAttribute("index")));
			}
			catch(Exception e){
				itemPortion.addProperty("size_index", 0);
			}
			itemPortion.addProperty("menuitem", itemId);
			try
			{
				m_objectBean.Insert(info, itemPortion);
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importOption} - " + ex.getErrMsg() );
			}
			catch(RepositoryException ex)
			{
				SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importOption} - " + ex.getErrMsg() );
				return null;
			}
		}
		
		if( includeChildren )
		{	
			XMLNodeList optionList = itemNode.getNodeList("options/option");
			for( int i = 0; i < optionList.getCount(); i++ )
			{
				XMLNode optionNode = optionList.getNodeByIndex(i);
				importOption(info, itemId, optionNode);
			}
		}
		return itemId;
	}

	public String importCategory(Credentials info, String menuId, XMLNode catNode, boolean includeChildren)
	{
		String catName = catNode.getAttribute("name");

		// Import category object
		ObjectSubmit cat = new ObjectSubmit("CCMenuCategory");
		
		// Set properties for object
		cat.addProperty("name", catName);
		cat.addProperty("menu", menuId);
		//cat.addProperty("hidden", "N");
		String hidden = catNode.getAttribute("hidden");
		if( hidden.equalsIgnoreCase("Y") ) cat.addProperty("hidden", "Y");
		else cat.addProperty("hidden", "N");
		
		try{
			cat.addProperty("cat_index", Integer.parseInt(catNode.getAttribute("index")));
		}
		catch(Exception e){
			cat.addProperty("cat_index", 0);
		}

		String cat_id = null;
		try
		{
			cat_id = m_objectBean.Insert(info, cat);
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importCategory} - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importCategory} - " + ex.getErrMsg() );
			return null;
		}
		
		if( includeChildren )
		{
			XMLNodeList itemList = catNode.getNodeList("item");
			for( int i = 0; i < itemList.getCount(); i++ )
			{
				XMLNode itemNode = itemList.getNodeByIndex(i);
				importItem(info, cat_id, itemNode, includeChildren);
			}
		}
		return cat_id;
	}

	public String importMenu(Credentials info, String locId, XMLNode menuNode, boolean includeChildren)
	{
		String menuName = menuNode.getAttribute("name");

		// Import menu object
		ObjectSubmit menu = new ObjectSubmit("CCMenu");

		// Set properties for object
		menu.addProperty("name", menuName);
		menu.addProperty("location", locId);
		menu.addProperty("schedule", menuNode.getAttribute("schedule_id"));
		menu.addProperty("take_orders", menuNode.getAttribute("take_orders"));
		//menu.addProperty("hidden", "N");
		
		String showOptions = menuNode.getAttribute("show_options");
		if( showOptions.equalsIgnoreCase("Y") ) menu.addProperty("show_options", "Y");
		else menu.addProperty("show_options", "N");
		
		String hidden = menuNode.getAttribute("hidden");
		if( hidden.equalsIgnoreCase("Y") ) menu.addProperty("hidden", "Y");
		else menu.addProperty("hidden", "N");
		
		try{
			menu.addProperty("menu_index", Integer.parseInt(menuNode.getAttribute("index")));
		}
		catch(Exception e){
			menu.addProperty("menu_index", 0);
		}
		
		String menu_id = null;
		try
		{
			menu_id = m_objectBean.Insert(info, menu);
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importMenu} - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex)
		{
			SystemServlet.g_logger.error( "Exception thrown in {MenuBuilder::importMenu} - " + ex.getErrMsg() );
		}
		
		if( includeChildren )
		{
			XMLNodeList catList = menuNode.getNodeList("category");
			for( int i = 0; i < catList.getCount(); i++ )
			{
				XMLNode catNode = catList.getNodeByIndex(i);
				importCategory(info, menu_id, catNode, includeChildren);
			}
		}
		return menu_id;
	}
}
