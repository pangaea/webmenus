<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	//menuOrderBean.setObjectManager(objectBean);
	String errMsg = "There was an error encountered:-(";
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
	if( bOpen )
	{
		if( menuOrderBean.submitNewItem(request) == true )
		{
			//response.sendRedirect( "orderview.jsp" );
			response.sendRedirect( "MenuView/vieworder" );
			return;
		}
	}
	else
	{
		errMsg = "Sorry, this establishment is currently not taking orders.";
	}
%>
<html>
	<head>
		<title>Error Submitting Menu Item</title>
<%
	String themeName = menuOrderBean.getTheme();
	if( themeName.length() > 0 ){
%>
<!-- Theme based includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/MenuView/theme?id=<%=themeName%>"></link>
<%
	} else {
%>
<!-- Default theme includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
<%
	}
%>
		<!--link rel="stylesheet" type="text/css" href="../styles/menu_view.css"></link-->
	</head>
	<body>
		<br>
		<center>
		<%=errMsg%>
		</center>
	</body>
</html>
