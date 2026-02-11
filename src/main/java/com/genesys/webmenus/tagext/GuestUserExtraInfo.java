package com.genesys.webmenus.tagext;

import javax.servlet.jsp.tagext.*;

public class GuestUserExtraInfo extends TagExtraInfo
{
	public VariableInfo[] getVariableInfo( TagData data )
	{
		return new VariableInfo[]
		{
			new VariableInfo( "guestCredentials", "com.genesys.repository.Credentials", true, VariableInfo.NESTED )
		};
	}
	
}
