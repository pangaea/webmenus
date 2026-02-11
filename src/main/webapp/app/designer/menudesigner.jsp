<!--
Copyright (c) 2004-2011 Kevin Jacovelli
All Rights Reserved
-->

<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
}
var gIndex = 0;
</script>
<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
		//response.sendRedirect( "login.jsp" ); return;
%>
		<html>
			<head></head>
			<body onload="backtologin();"><em>Your session has timed out...</em></body>
		</html>
<%
		return;
	}
%>


<jsp:useBean id="menuDesignerBean" class="com.genesys.webmenus.designer.MenuDesignerBean" scope="page"/>
<jsp:setProperty name="menuDesignerBean" property="*"/> 
<%
menuDesignerBean.setRequest(request); // pass request object to bean
menuDesignerBean.Process();
%>

<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu Designer</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/menudesigner.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
		
<!--script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/lib/jquery.js"></script-->
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/lib/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/lib/jquery.hotkeys.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/lib/sarissa.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/jquery.tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.checkbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.contextmenu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.hotkeys.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.themeroller.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.xml_flat.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jsTree/plugins/jquery.tree.xml_nested.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/CodeMirror/js/codemirror.js"></script>

<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jquery-ui-sliderAccess.js"></script>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/autoNumeric-1.7.4.js"></script-->
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.numeric.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.layout.js"></script>

<!--link rel="Stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link-->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" /-->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/redmond/jquery-ui-1.7.2.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>

<!-- Core Stylesheet -->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/styles/main.css"></link-->
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/includes/fields.js"></script-->
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/clientio.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xutils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xmlwriter.js"></script>

<!-- Default theme includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/curvey-2.css"></link>

<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/menus.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/menudesigner.js"></script>
<script type="text/javascript">
var contextPath = "<%=request.getContextPath()%>";
var locationId = "<%=menuDesignerBean.getLoc()%>";
var selectedMenuId = "<%= menuDesignerBean.getMenu() != null ? menuDesignerBean.getMenu() : "" %>";
$(function () {
	$("#menu_edit_mode").toggle( selectedMenuId != null && selectedMenuId.length > 0 );
	$("#viewpanel").load(noCache("welcome.jsp"));
<%	if(request.getParameter("oid") == null ){ %>
		initPage(false);
<% }else{ %>
		initPage("<%=request.getParameter("oid")%>");
<% } %>
});

//window.onbeforeunload = function(){return "Are you sure you want to leave this page";}
</script>


</head>
<body>

<iframe id="uploadFrame" name="uploadFrame" style="display:none;"></iframe>

<div id="xmlPreviewFrame" style="width:100%;display:none;">
	<textarea id="xmlPreviewArea" style="width:100%;height:100%;"></textarea>
</div>

<!-- manually attach allowOverflow method to pane --> 
<div class="ui-layout-north" style="overflow-y:hidden;"> 
<ul id="jsddm">
	<li><a href="javascript:showDetails(null, 'root');">Home</a></li>
    <li><a href="javascript:createMenu()">Create Menu</a></li>
    <li><a href="javascript:saveMenus('<%=menuDesignerBean.getLoc()%>')">Save/Publish Changes</a></li>
    <li><a href="javascript:saveAsFile('<%=menuDesignerBean.getLoc()%>')">Save To File</a></li>
    <li><a href="javascript:loadFile('<%=menuDesignerBean.getLoc()%>')">Import Menus</a></li>
    <li><a href="javascript:viewXmlSource('<%=menuDesignerBean.getLoc()%>')">View Menu XML</a></li>
    <li><a href="javascript:deleteNode()">Delete Selected</a></li>
    <li><a href="javascript:previewMenus('<%=menuDesignerBean.getLoc()%>')">Preview All Menus</a></li>
    <li><a href="javascript:showHelp()">Help</a></li>
    <li><a href="javascript:parent.$.fancybox.close();">Close</a></li>
</ul>
<div id="menu_edit_mode" style="float:right;color:red;">You're in Single Menu Edit Mode</div>
</div>

<!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
<div class="ui-layout-west">
	<div id="container">
		<div class="menusTree" id="menusTree"></div>
	</div>
</div>

<div id="client_area" class="ui-layout-center">
	<div id="viewpanel"></div>
</div>

</body>
</html>