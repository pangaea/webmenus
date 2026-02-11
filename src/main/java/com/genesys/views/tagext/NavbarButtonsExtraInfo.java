package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class NavbarButtonsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "navText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "navImage", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "navView", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "navFilter", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "navDetails", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
