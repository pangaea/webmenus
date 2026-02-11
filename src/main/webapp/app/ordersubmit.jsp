<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<!--%@ page import="com.genesys.repository.*"%-->
<%@ page import="java.util.*"%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
	if( bOpen == false )
	{
%>
		Sorry, this establishment is currently not taking orders.
<%
		return;
	}
	
	//if( request.getSession().getAttribute( "patron_id" ) == null )
	if( menuOrderBean.isValidated() == false )
	{
		response.sendRedirect( "login_patron.jsp" );
		return;
	}
%>
<html>
	<head>
		<title>Order Submitted</title>
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
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
		</style>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript">
		dojo.require("dijit.form.Button");
		dojo.require("dojo.parser");
		</script>
	</head>
	<body class="tundra">
		<br>
		<center>
<%
	// Check for delivery options and record them if present
	boolean validation_failed = false;
	String sDelivery = request.getParameter("delivery_option");
	if( sDelivery != null && sDelivery.equalsIgnoreCase("delivery") == true )
	{
//		String sAddress = request.getParameter("address");
//		String sCity = request.getParameter("city");
//		String sState = request.getParameter("state");
//		String sZip = request.getParameter("zip");
//		String sContactNumber = request.getParameter("contact_number");
//		Vector<String> errors = menuOrderBean.setDeliveryAddress( sAddress, sCity, sState, sZip, sContactNumber );
		
		String sDeliveryInfo = request.getParameter("delivery_info");
		menuOrderBean.setDeliveryAddress( sDeliveryInfo );

		//Vector<String> errors = menuOrderBean.setDeliveryAddress( sDeliveryInfo );
		//if( errors.size() > 0 ){
		//	for( int i = 0; i < errors.size(); i++ ){
//%->
//			<span style="color:red;"><%=errors.get(i)%-></span><br/>
//<%
//			}
//			validation_failed = true;
//		}
	}
	
	//menuOrderBean.setObjectManager(objectBean);
	if( validation_failed == false && menuOrderBean.submitOrder() == true ){
%>
		Thank you! Your order has been successfully placed.<br/><br/>
<%
	}
	else
	{
%>
		<span style="color:red;">Order Failed: Your order has failed to be processed. Please try again.</span><br/><br/>
		<button dojoType="dijit.form.Button">
			Go Back
			<script type="dojo/method" event="onClick">
				history.back();
			</script>
		</button>
<%
	}
%>
		<button dojoType="dijit.form.Button">
			Close
			<script type="dojo/method" event="onClick">
				parent.closeOrderDialog();
			</script>
		</button>
		</center>
	</body>
</html>
