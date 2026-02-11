package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class ViewLinksExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "linkText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "linkView", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "linkReference", "java.lang.String", true, VariableInfo.NESTED ),
		};
	}
	
}
