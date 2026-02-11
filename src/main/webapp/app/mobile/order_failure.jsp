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
<div data-role="page" id="page-success">

	<div data-role="header">
		<h1>Order Failed</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
	</div>

	<div data-role="content">
	<fieldset data-role="controlgroup">
		<p><h3>Order Failed: Your order has failed to be processed. Please try again.</h3></p>
		<a data-role="button" href="<%=rootMenuPath%>/orderview">View Order</a>
	</fieldset>
	</div>

</div>
</body>
</webmenusCfg:GuestUser>
