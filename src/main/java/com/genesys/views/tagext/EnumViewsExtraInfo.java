package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumViewsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			//new VariableInfo( "viewLayout", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "viewName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "viewTitle", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
