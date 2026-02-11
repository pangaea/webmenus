<!--
Copyright (c) 2004-2016 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.List"%>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	//menuOrderBean.setObjectManager(objectBean);
	String sLocId = request.getParameter("loc");
	if( sLocId == null ){
%>
		<html><body><h2>Location id missing</h2></body></html>
<%
		return;
	}
	boolean bFirstMenuFound = false;
	
	// Load location information into the order bean
	menuOrderBean.setCurrentLocation(sLocId);
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
	String exitURL = menuOrderBean.getExitURL();
%>
<html>
<head>

<style type="text/css">
	@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
</style>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xutils.js"></script>

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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/curvey-2.css"></link>
<%
	}
%>

<script type="text/javascript">
dojo.require("dijit.form.Button");
dojo.require("dijit.Dialog");
dojo.require("dijit.layout.SplitContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.Menu");
dojo.require("dojo.parser");
</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/handlebars/handlebars-v1.3.0.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="scripts/menus.js"></script>
<script type="text/javascript" src="scripts/menusview.js"></script>
<script type="text/javascript" src="scripts/handlebars.helpers.js"></script>

</head>
<body id="menuBody" class="tundra" style="background-image: url('images/bg2.jpg')">
<webmenusCfg:GuestUser>
<div id="frame">

	<div id="menuHeader">
	<% if( menuOrderBean.getLogo().length() > 0 ){ %>
	<img src="<%=request.getContextPath()%>/ImageViewer<%=menuOrderBean.getLogo()%>"/>
	<% } %>
	</div>
	<div id="menuMenu">

		<table><tr style="white-space: nowrap;">
		<td valign="middle">
<%	if( exitURL.length() > 0 ){ %>
		<button dojoType="dijit.form.Button" connectId="2112" iconClass="plusIcon">
<%
		String[] params = exitURL.split("buttontitle=");
%>
		<%= params.length > 1 ? params[1] : "Home" %>
		<script type="dojo/method" event="onClick">
				document.location = "<jsp:getProperty name='menuOrderBean' property='exitURL'/>";
		</script>
		</button>
<%	}else{ %>
		<button dojoType="dijit.form.Button" connectId="2112" iconClass="plusIcon">
		Close
		<script type="dojo/method" event="onClick">
				window.close();
		</script>
		</button>
<%	} %>
		</td>
		<td valign="middle">
<%	if( bOpen ){ %>
		<button dojoType="dijit.form.Button" connectId="2112" iconClass="plusIcon">
		View Order
		<script type="dojo/method" event="onClick">
				viewOrder();
		</script>
		</button>
<%	} %>
		</td>
		<td valign="middle">

		<webmenusCfg:EnumMenus locId="<%=sLocId%>">
<%
		if( bFirstMenuFound == false ){ // Set the first menu Id
			bFirstMenuFound = true;
%>
		<script type="text/javascript">
			var sFirstMenuId="<%=menuId%>";
			var template = "<%=menuOrderBean.getThemeTemplate().replace("\\", "\\\\").replace("\n", "").replace("\"", "\\\"")%>";
			if(template.length == 0){
				createMessageDialog();
				message("ERROR: missing template: invalid theme selected.", "Error", function(){document.location.reload();});
			}
			$COMPILED_TEMPLATE = Handlebars.compile(template);
		</script>
<%
		}
%>
		<div dojoType="dijit.form.ComboButton" onclick="menuClick('<%=menuId%>')"><span style="font:8pt;"><%=menuName%></span>
			<div dojoType="dijit.Menu" toggle="fade" style="display:none;">
			<webmenusCfg:EnumCategories menuId="<%=menuId%>">
				<div dojoType="dijit.MenuItem" iconClass="dijitEditorIcon dijitEditorIconCopy" onclick="catClick('<%=catId%>')"><%=catName%></div>
			</webmenusCfg:EnumCategories>
			</div>
		</div>&nbsp;
		</webmenusCfg:EnumMenus>
		</td></tr></table>

	</div>

	<div id="menuContent"></div>
	
	<div id="menuFooter"></div>

</div>
</webmenusCfg:GuestUser>
</body>
</html>
