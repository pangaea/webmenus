package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class EnumItemOptionChoicesExtraInfo extends TagExtraInfo {
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "choiceId", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "choiceName", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "choicePrice", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "choiceIdx", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
}
