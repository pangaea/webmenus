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
		<% if(menuOrderBean.getLogo().length() > 0){ %>
		<img src="<%=request.getContextPath()%>/ImageViewer<%=menuOrderBean.getLogo()%>"/>
		<% } %>
		<%=menuOrderBean.getLocationName()%> : Menus</h1>
	</div><!-- /header -->

	<div data-role="content">	
		<ul data-role="listview" data-inset="true">
		<webmenusCfg:EnumMenus locId="<%=sLocId%>">
			<li data-role="list-divider"><%=menuName%></li>
			<webmenusCfg:EnumCategories menuId="<%=menuId%>">
			<li class=""><a href="<%=request.getContextPath()%>/app/mobile/itemsview.jsp?cat=<%=catId%>"><%=catName%></a></li>
			</webmenusCfg:EnumCategories>
		</webmenusCfg:EnumMenus>
		</ul>
	</div><!-- /content -->

</div><!-- /page -->

</body>
</webmenusCfg:GuestUser>
</html>