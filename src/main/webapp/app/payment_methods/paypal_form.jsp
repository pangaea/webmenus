<!--
Copyright (c) 2004-2011 Kevin Jacovelli
All Rights Reserved
-->

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%//@ page import="com.genesys.util.ServletUtilities"%>

<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
}
</script>
<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
		//response.sendRedirect( "login.jsp" ); return;
%>
		<html>
			<head></head>
			<body onload="backtologin();"><em>Your session has timed out...</em></body>
		</html>
<%
		return;
	}
%>



<%//@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Paypal Settings</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/excite-bike/jquery-ui-1.8.18.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
</head>
<body>
PAYPAL SETTINGS
<hr/>
<style>
.settings {
	width: 100%;
}
.settings td:last-child {
	width: 100%;
}
.settings td:first-child {
	min-width:100px;
}
.settings input {
	width: 100%;
}
</style>
<html:form action="paypal_form.do">
	<html:hidden property="id"/>
	<table class="settings"><tr>
	<td>Client Id:</td> <td>
		<html:text property="clientId" title="Client ID"/></td>
	</tr><tr>
	<td>Client Secret:</td> <td>
		<html:text property="clientSecret" title="Client Secret"/></td>
	</tr></table>
	<hr/>
	<div style="float:right;">
		<html:submit>Save</html:submit>
		<button onclick="parent.$.fancybox.close();">Close</button>
	</div>
</html:form>
</body>
</html:html>