package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class ViewToolbarExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "buttonText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "buttonEventNum", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
