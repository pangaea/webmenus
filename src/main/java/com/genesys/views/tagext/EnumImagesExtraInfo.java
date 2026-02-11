package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class EnumImagesExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "imageFileName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "imageFilePath", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
