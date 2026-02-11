package com.genesys.views.tagext;

import javax.servlet.jsp.tagext.*;

public class ViewListExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "itemField", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemTitle", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemEnum", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemWidth", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemView", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemFKey", "java.lang.String", true, VariableInfo.NESTED ),
			new VariableInfo( "itemProperty", "java.lang.String", true, VariableInfo.NESTED )
		};
	}

}
