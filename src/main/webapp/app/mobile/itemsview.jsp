<%@ include file="page_header.jsp" %>

<%
	String sCatId = request.getParameter("cat");
	if( sCatId == null ){ throw new ServletException("Category id missing"); }
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
<div data-role="page" id="page-<%=sCatId%>">

	<div data-role="header">
		<h1>Location Menus</h1>
		<a data-rel="back">Back</a>
	</div><!-- /header -->

	<div data-role="content">
			
		<div class="content-primary">
			<ul data-role="listview"> 
			<webmenusCfg:EnumItems categoryId="<%=sCatId%>">
				<li><a href="<%=request.getContextPath()%>/app/MenuView/menuitemview?item=<%=itemId%>">
				<% if(itemImage.length() > 0){ %>
					<img width="100px" height="75px" src="<%=request.getContextPath()%>/images<%=itemImage%>" /> 
				<% } %>
					<h3><%=itemName%></h3> 
					<p><%=itemDescription%></p>
				</a></li> 
			</webmenusCfg:EnumItems>
			</ul>
		</div><!--/content-primary -->	


	</div><!-- /content -->

</div><!-- /page -->

</body>
</webmenusCfg:GuestUser>
</html>