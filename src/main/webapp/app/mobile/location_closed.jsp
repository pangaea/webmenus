<%@ include file="page_header.jsp" %>

<!DOCTYPE html> 
<html>
<head>

<meta name="HandheldFriendly" content="true" />
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.js"></script>

</head>
<webmenusCfg:GuestUser>
<body>
<div data-role="page" id="page-closed">

	<div data-role="header">
		<h1>Menus Closed</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
	</div>

	<div data-role="content">
	<fieldset data-role="controlgroup">
		<p><h3>Sorry, this menu is currently not taking orders online.</h3></p>
		<a data-role="button" href="<%=rootMenuPath%>">Back to the Menus</a>
	</fieldset>
	</div>

</div>
</body>
</webmenusCfg:GuestUser>
</html>