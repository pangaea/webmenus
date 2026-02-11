package com.genesys.util;

import java.net.URLDecoder;
import javax.servlet.*;
import javax.servlet.http.*;

public class ServletUtilities
{
	// Other methods in this class shown in earlier chapters.

	public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue)
	{
		if( cookies == null ) return defaultValue;
		for(int i=0; i<cookies.length; i++)
		{
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return(URLDecoder.decode(cookie.getValue()));
		}
		return(defaultValue);
	}

	public static Cookie getCookie(Cookie[] cookies, String cookieName)
	{
		for(int i=0; i<cookies.length; i++)
		{
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return(cookie);
		}
		return(null);
	}
	
	public static boolean isMobileDevice(HttpServletRequest request)
	{
		// Header value (user-agent)
		// iPhone 3G:	Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A306 Safari/6531.22.7

		return 	(
					request.getHeader("user-agent").indexOf("iPhone") >= 0 ||
					request.getHeader("user-agent").indexOf("Android") >= 0
				);
	}
}