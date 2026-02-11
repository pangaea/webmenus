package com.genesys.webmenus.summary.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumSchedulesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "scheduleId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "scheduleName", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
