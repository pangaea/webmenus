package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumCategoriesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "catId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "catName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "catIdx", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
