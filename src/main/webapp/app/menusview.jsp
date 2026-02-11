<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	//menuOrderBean.setObjectManager(objectBean);
	String sLocId = request.getParameter("loc");
	if( sLocId == null ){
%>
		<h2>Location id missing</h2>
<%
		return;
	}
	String sFirstMenuId = new String("");
	
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
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.Tree");
dojo.require("dojo.parser");
</script>


<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>


<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="scripts/menus.js"></script>
<script type="text/javascript" src="scripts/menusview.js"></script>


</head>
<body id="menuBody" class="tundra" style="background-image: url('images/bg2.jpg')">
<webmenusCfg:GuestUser>
<script type="text/javascript">
var treeData =
{	identifier: 'id',
	label: 'name',
	items: [
<webmenusCfg:EnumMenus locId="<%=sLocId%>">
<% 	if( Integer.parseInt(menuIdx) > 0 ){ %>,<% } %>
<%
	if( sFirstMenuId.length() == 0 ) sFirstMenuId = menuId; // Set the first menu Id
%>
	{ id: '<%=menuId%>', name:'<%=menuName%>', type:'menu', children:[
	<webmenusCfg:EnumCategories menuId="<%=menuId%>">
	<% if( Integer.parseInt(catIdx) > 0 ){ %>,<% } %>
		{ id:'<%=catId%>', name:'<%=catName%>', type:'category'}
	</webmenusCfg:EnumCategories>
	]}
</webmenusCfg:EnumMenus>
]};

var sFirstMenuId="<%=sFirstMenuId%>";

function expandAllNode()
{
	var treeObj = dijit.byId('menuTreex');

	var children = treeObj.rootNode.getChildren();
	expandChildNode(children, treeObj);
}

function expandChildNode(children, treeObj)
{
	for (var i = 0; i < children.length; i++)
	{
		var node = children[i];
		if (node.isExpandable && !node.isExpanded)
		{
			treeObj._expandNode(node);
		}

		var childNodes = node.getChildren();
		if (childNodes.length > 0)
		{
			expandChildNode(childNodes, treeObj);
		}
	}
}

function init()
{
	timer = setTimeout("expandAllNode()",100);
	//highlightNode();

	//var splitter = document.getElementById("splitter");
	//if( splitter == null ) return;
	//dojo.byId('splitter').setAttribute("height", "100%");
	//splitter.height = "100%";
}
</script>

<div id="frame">
<div id="menuHeader">
<% if( menuOrderBean.getLogo().length() > 0 ){ %>
<img src="<%=request.getContextPath()%>/ImageViewer<%=menuOrderBean.getLogo()%>"/>
<% } %>
</div>
<div dojoType="dijit.layout.SplitContainer" id="splitter" orientation="horizontal" sizerWidth="7" activeSizing="true" persist="false"
	 style="width:100%;height:98%;">

	<div dojoType="dijit.layout.ContentPane" sizeShare="8" style="padding:10px;">
	
	
<%	if( exitURL.length() > 0 ){ %>
		<button dojoType="dijit.form.Button" connectId="2112" iconClass="plusIcon">
		Home
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
<%	if( bOpen ){ %>
		<button dojoType="dijit.form.Button">
		View Order
		<script type="dojo/method" event="onClick">
				viewOrder();
		</script>
		</button>
<%	} %>
		<br/><br/>
		<div dojoType="dojo.data.ItemFileReadStore" jsId="menuStore" data="treeData"></div>
		<div style="text-align:left;" dojoType="dijit.Tree" id="menuTreex" store="menuStore" query="{type:'menu'}" labelAttr="name" label="Menus">
		<script type="dojo/method" event="onClick" args="item">
			if(item)
			{
				try
				{
					var id = menuStore.getValue(item,"id");
					var type = menuStore.getValue(item,"type");
					if( type == "menu" )
						menuClick(id);
					else
						catClick(id);
				}
				catch(e){}
			}
		</script>
		</div>
	</div>

	<div dojoType="dijit.layout.ContentPane" sizeShare="20">
		<div id="menuContent"></div>
	</div>
</div>
<div id="menuFooter"></div>
</div>
</webmenusCfg:GuestUser>
</body>
</html>
