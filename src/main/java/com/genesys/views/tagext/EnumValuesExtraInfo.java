package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumValuesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			//new VariableInfo( "viewLayout", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "enumText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "enumCode", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "enumType", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
