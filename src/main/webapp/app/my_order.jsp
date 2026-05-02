<!--
Copyright (c) 2026 Kevin Jacovelli
All Rights Reserved
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>

<%
    if (menuOrderBean.getCurrentLocationId().isEmpty()) {
        String sLocId = request.getParameter("loc");
        if( sLocId == null ){
%>
		<html><body><h2>Location id missing</h2></body></html>
<%
		return;
        }
        
        // Load location information into the order bean
        menuOrderBean.setCurrentLocation(sLocId);
    }
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
    String order_id = request.getParameter("id");
    if (order_id == null) {
%>
        Invalid order id
<%
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>My Order Status</title>
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
        <style type="text/css">
            @import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
        </style>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/my_order.js"></script>
        <style type="text/css">
            .container {
                display: grid;
                place-items: center; /* Centers both axes in one line */
                height: 100vh;
            }
            .centered {
                text-align:center;
            }
        </style>

        <script type="text/javascript">
            var contextPath = "<%=request.getContextPath()%>";
            dojo.require("dijit.form.Button");

            dojo.addOnLoad( function() {
                window.setInterval(() => {
                    getOrderStatus("<%=order_id%>", "<%=request.getContextPath()%>");
                }, 10000);
            });
        </script>
	</head>
	<body class="tundra container">
        <div>
            <div class="menuTitle"><%=menuOrderBean.getLocationName()%></div>
            <span class="menuItemTitle"><%=menuOrderBean.getLocationAddress()%></span>
            <div class="menuItemDesc"><%=menuOrderBean.getPhoneNumber()%></div>
            <table class="pageSeperator"><tr>
                <td width="100%"><hr/></td>
            </tr></table>
            <div class="menuItemTitle centered">My Order Status</div>
            <div id="status_display" class="centered">
                <img id="status_image" style="width:200px;""/>
                <div id="status_label" class="menuTitle centered"/>
            </div>
        </div>
    </body>
</html>