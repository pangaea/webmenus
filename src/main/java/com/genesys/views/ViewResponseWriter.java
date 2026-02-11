package com.genesys.views;

import java.util.*;

public class ViewResponseWriter
{
	String _call;
	int _code;
	String _msg;
	public void setCall(String call){_call = call;}
	public void setCode(int code){_code = code;}
	public void setMsg(String msg){_msg = msg;}
	public ViewResponseWriter()
	{
		_call = new String("unknown");
		_code = 2112;
		_msg = new String("");
	}
	public ViewResponseWriter(String call, int code, String msg)
	{
		_call = call;
		_code = code;
		_msg = msg;
	}
	public String serialize()
	{
		StringBuffer serverResponse = new StringBuffer();
		serverResponse.append("<return>");
		serverResponse.append("<call>" + _call + "</call>");
		serverResponse.append("<code>" + Integer.toString(_code) + "</code>");
		serverResponse.append("<msg>" + _msg + "</msg>");
		serverResponse.append("</return>");
		return serverResponse.toString();
	}
}