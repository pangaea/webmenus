package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumItemSizesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "itemSizeId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemSizeDesc", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemSizePrice", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
