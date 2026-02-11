package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumMenusExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "menuId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "menuName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "menuIdx", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
