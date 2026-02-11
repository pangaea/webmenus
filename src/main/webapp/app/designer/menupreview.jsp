<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
}
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
<%@ include file="../../ui/objmanhdr.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu Preview</title>


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

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.layout.js"></script>

<link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>


<!-- Core Stylesheet -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/styles/main.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/clientio.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xutils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xmlwriter.js"></script>


<!-- Default theme includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/curvey-2.css"></link>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/handlebars/handlebars-v1.3.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/menus.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/menupreview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/handlebars.helpers.js"></script>
<script type="text/javascript">
var contextPath = "<%=request.getContextPath()%>";
var locationId = "<%=request.getParameter("loc")%>";
//var compiledTemplate;
$(function () {
	var template = "<%=menuDesignerBean.getThemeTemplate().replace("\\", "\\\\").replace("\n", "").replace("\"", "\\\"")%>";
	//compiledTemplate = Handlebars.compile(template);
	$COMPILED_TEMPLATE = Handlebars.compile(template);
	initPage();
});
</script>

<%
	String themeName = menuDesignerBean.getTheme();
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


</head>
<body>

<!-- manually attach allowOverflow method to pane --> 
<div class="ui-layout-north">
<strong>
NOTE: This is a simple preview and does not reflect the look-and-feel of a live menu system.
After publishing changes click 'View Live Menus' in the 'Main Console' to see them as a customer will.
</strong>
</div>

<!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true --> 
<div class="ui-layout-west">
	<div id="container">
		<div class="menusTree" id="menusTree"></div>
	</div>
</div>

<div id="client_area" class="ui-layout-center">
	<div id="menuPanel"></div>
</div>


</body>
</html>