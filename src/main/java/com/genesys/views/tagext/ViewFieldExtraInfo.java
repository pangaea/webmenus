package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class ViewFieldExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "inputName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputDisplay", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputProperty", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputSearchable", "java.lang.String", true, VariableInfo.NESTED ),
		};
	}
	
}
