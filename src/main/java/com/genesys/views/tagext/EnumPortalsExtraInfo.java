package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumPortalsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "portalName", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
