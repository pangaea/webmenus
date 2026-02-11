package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class MenuItemSizeExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "itemId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemDescription", "java.lang.String", true, VariableInfo.NESTED ),
			//new VariableInfo( "itemHidden", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemImage", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemSizeDescription", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemPrice", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemIdx", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemMenuId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemSizeId", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
