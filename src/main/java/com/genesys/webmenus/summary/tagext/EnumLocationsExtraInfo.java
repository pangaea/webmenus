package com.genesys.webmenus.summary.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumLocationsExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "locationIndex", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationAddress", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationCity", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationState", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationZip", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationPhone", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationLogo", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "locationThemeId", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
