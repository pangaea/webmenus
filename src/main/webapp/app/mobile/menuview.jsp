<%@ include file="page_header.jsp" %>

<%
	String sMenuId = request.getParameter("menu");
	if( sMenuId == null ){ throw new ServletException("Menu id missing"); }
%>

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
<div data-role="page" id="page-<%=sMenuId%>">

	<div data-role="header">
		<h1>Menu</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
		<a rel="external" data-ajax="false" href="<%=rootMenuPath%>/orderview">View Order</a>
	</div>
 
	<div data-role="content">
		<ul data-role="listview" data-inset="true">
			<webmenusCfg:EnumCategories menuId="<%=sMenuId%>">
			<li data-role="list-divider"><%=catName%></li>
				<webmenusCfg:EnumItems categoryId="<%=catId%>">
					<li><a href="<%=rootMenuPath%>/itemdetails?item=<%=itemId%>">
					<% if(itemImage.length() > 0){ %>
						<img width="100px" height="75px" src="<%=request.getContextPath()%>/ImageViewer<%=itemImage%>" />
					<% } %>
						<h3><%=itemName%></h3>
						<p><%=itemDescription%></p>
						<%=menuOrderBean.getCurrencyString(itemPrice)%> <%if(itemSizeDescription.length() > 0){%>(<%=itemSizeDescription%>)<%}%>
						<% boolean bExtraSizes = false; %>
						<webmenusCfg:EnumItemSizes itemId="<%=itemId%>">
						<% bExtraSizes = true; %>
							<br/><%=menuOrderBean.getCurrencyString(itemSizePrice)%> (<%=itemSizeDesc%>)
						</webmenusCfg:EnumItemSizes>
					</a>
					<% if(bExtraSizes){ %>
						<ul>
							<li><a href="<%=rootMenuPath%>/itemdetails?item=<%=itemId%>"><%=menuOrderBean.getCurrencyString(itemPrice)%> (<%=itemSizeDescription%>)</a></li>
						<webmenusCfg:EnumItemSizes itemId="<%=itemId%>">
							<li><a href="<%=rootMenuPath%>/itemdetails?item=<%=itemSizeId%>"><%=menuOrderBean.getCurrencyString(itemSizePrice)%> (<%=itemSizeDesc%>)</a></li>
						</webmenusCfg:EnumItemSizes>
						</ul>
					<% } %>
					</li>
				</webmenusCfg:EnumItems>
			</webmenusCfg:EnumCategories>
		</ul>

	</div>

</div>

</body>
</webmenusCfg:GuestUser>
</html>