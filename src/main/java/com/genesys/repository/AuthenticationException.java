package com.genesys.repository;

import java.util.*;

public class AuthenticationException extends Exception
{
	private String ErrMsg;
	
	// Public interface
	public AuthenticationException(String errMsg)
	{
		ErrMsg = new String(errMsg);
	}
	
	public String getErrMsg()
	{
		return ErrMsg;
	}
}