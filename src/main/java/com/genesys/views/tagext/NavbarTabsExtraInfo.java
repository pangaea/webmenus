package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class NavbarTabsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "tabText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "tabEmpty", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
