package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumItemOptionsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "optionId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "optionName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "optionPrice", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "optionType", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "optionData", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "optionIdx", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
