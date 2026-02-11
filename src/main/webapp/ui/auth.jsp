<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?err=" + escape("Your session time out. Please login again.");
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

<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<!--[if gte IE 7]>
<!--script src="<%//=request.getContextPath()%>/includes/DOMAssistantComplete-2.8.js" type="text/javascript"></script>
<script src="<%//=request.getContextPath()%>/includes/ie-css3.js" type="text/javascript"></script-->
<![endif]-->
