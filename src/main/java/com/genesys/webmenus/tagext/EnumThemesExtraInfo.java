package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumThemesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "themeId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "themeName", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
