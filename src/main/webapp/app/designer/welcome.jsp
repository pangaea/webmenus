<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<%@ page import="com.genesys.repository.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Menu Designer - Welcome Page</title>
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/menudesigner.css"></link>
	<style type="text/css">
	.bigbutton{
		margin: 4px 4px 4px 22px;
	}
	.bigbutton button{
		height:50px !important;
		width:400px;
		text-align: left;
		font-size: 14pt;
		background: #A0A0A0;
	}
	.bigbutton button .button_text{
		color: #FFFFFF;
	}
	</style>
</head>
<body bgcolor="#E5E5E5" text="#000000" link="#006699" vlink="#006699">
<h2>Menu Designer</h2>
<h3>Use this utility to create and edit menus for this location</h3>
<hr/>

Click <a href="javascript:showHelp()">here</a> for the User Manual

<br/><br/>

<div id="help" class="bigbutton"></div>
<div id="importMenus" class="bigbutton"></div>
<div id="createMenu" class="bigbutton"></div>
<br/>
<%
	Credentials info = (Credentials)request.getSession().getAttribute("info");
%>
Selected theme for this location: 
<select ID="theme_id" NAME="theme_id">
	<option value="">None ( WARNING: Menus will not function )</option>
	<webmenusCfg:EnumThemes credentials="<%=info.m_sTicket%>">
	<option value="<%=themeId%>"><%=themeName%></option>
	</webmenusCfg:EnumThemes>
</select>
<br/>
<span id="theme-missing-warning" style="color:red;display:none;">WARNING: Menus will not function unless a theme is selected</span>

<script type="text/javascript">
function buttonCallback( item, param )
{
	switch( item )
	{
		case 0:
			showHelp();
			break;
		case 1:
			loadFile('<%=request.getParameter("loc")%>')
			break;
		case 2:
			createMenu();
	}
}
$("#help").html(buttonDraw( 0, '', '<table><tr><td><img src="../../images/document.png"/></td><td><span class="button_text">Learn how to use the "Menu Designer"</span></td></tr></table>', 'buttonCallback' ));
$("#importMenus").html(buttonDraw( 1, '', '<table><tr><td><img src="../../images/exchange.png"/></td><td><span class="button_text">Import saved or new menus</span></td></tr></table>', 'buttonCallback' ));
$("#createMenu").html(buttonDraw( 2, '', '<table><tr><td><img src="../../images/pencil.png"/></td><td><span class="button_text">Create new "empty" menu</span></td></tr></table>', 'buttonCallback' ));
$("#theme_id").change(function(){
	location_theme_id = $(this).val();
	$("#theme-missing-warning").toggle( ($(this).val().length == 0) )
});
</script>
</body>
</html>