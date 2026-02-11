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
<div data-role="page" id="page-<%=currentLocation.getId()%>">

	<div data-role="header">
		<h1>
		<%=menuOrderBean.getLocationName()%> : Menus
		</h1>
		<a rel="external" data-ajax="false" href="<%=rootMenuPath%>/orderview">View Order</a>
	</div>

	<div data-role="content">
		<div style="text-align:center;">
		<% if(menuOrderBean.getLogo().length() > 0){ %>
		<img src="<%=request.getContextPath()%>/ImageViewer<%=menuOrderBean.getLogo()%>" style="width:300px;"/>
		<% } %><br/>
		<h1><%=menuOrderBean.getLocationName()%></h1>
		<a href="mailto:<%=menuOrderBean.getEmailAddress()%>"><%=menuOrderBean.getEmailAddress()%></a><br/>
		<%=menuOrderBean.getPhoneNumber()%><br/>
		</div>
		<hr/>
		
		<div data-role="controlgroup">
			<webmenusCfg:EnumMenus locId="<%=currentLocation.getId()%>">
			<a href="<%=rootMenuPath%>/menuview?menu=<%=menuId%>" data-role="button"><%=menuName%></a>
			</webmenusCfg:EnumMenus>
		</div>
	</div>

</div>
</body>
</webmenusCfg:GuestUser>

</html>