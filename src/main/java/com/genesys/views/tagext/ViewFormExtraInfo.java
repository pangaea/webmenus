package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class ViewFormExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			//new VariableInfo( "viewLayout", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputField", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputProperty", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputType", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputText", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputDisplay", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputConstraint", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputLen", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputView", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputHeight", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputWidth", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputVisible", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputTarget", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "inputFilter", "java.lang.String", true, VariableInfo.NESTED ),
			//new VariableInfo( "inputRequired", "java.lang.String", true, VariableInfo.NESTED )
			new VariableInfo( "inputRequired", "java.lang.Boolean", true, VariableInfo.NESTED ),
			new VariableInfo( "inputDefaultVal", "java.lang.String", true, VariableInfo.NESTED )
		};
	}
	
}
