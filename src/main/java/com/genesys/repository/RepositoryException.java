package com.genesys.repository;

import java.util.*;

public class RepositoryException extends Exception
{
	private String ErrMsg;
	private int Type;
	
	public static int VALIDATION_ERROR		= 1;
	public static int SQL_ERROR			= 2;
	
	// Public interface
	public RepositoryException(String errMsg, int type)
	{
		this.ErrMsg = new String(errMsg);
		this.Type = type;
	}
	
	public String getErrMsg()
	{
		return this.ErrMsg;
	}
	
	public int getType()
	{
		return this.Type;
	}
}