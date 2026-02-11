<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
%>
<html>
	<head>
		<title>Patron Login</title>
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
		dojo.require("dijit.Dialog");
		dojo.require("dojo.parser");
		</script>
	</head>
	<body class="tundra">
	<%
		String errMsg = request.getParameter("msg");
		if( errMsg == null ) errMsg = new String("");
	%>
	<table style="width:100%"><tr><td style="text-align:center">
		<span style="color:red;"><%=errMsg%></span>
		<div class='patronLoginTitle'>Existing Account</div>
		<form id="patronLogin" method="post" action="<%=request.getContextPath()%>/LoginPatron">
			<input type="hidden" name="type" value="login"/>
			<table style="margin:auto;">
				<tr><td><div class='patronLoginLabel'>Email:</div></td><td><input type="text" name="email"/></td><td rowspan="2">
								<!--img src="<-%=request.getContextPath()%->/images/1rightarrow-64.png"/-->
				</td></tr>
			<tr><td valign="middle" colspan="2" style="text-align:center;">
				<button dojoType="dijit.form.Button">
					Login
					<script type="dojo/method" event="onClick">
						patronLogin.submit();
					</script>
				</button>
			</td><td/></tr>
			</table>
		</form>

		<table class="pageSeperator"><tr>
			<td width="45%"><hr/></td>
			<td width="10%" style="text-align:center;"><span>OR</span></td>
			<td width="45%"><hr/></td>
		</tr></table>

		<script type="text/javascript">
		function messageBox(msg, title)
		{
			var content = msg + "<br/><br/><center><button dojoType=\"dijit.form.Button\" onclick=\"dijit.byId('msgbox').destroy();\">OK</button></center>";
			theDialog = new dijit.Dialog({id:"msgbox", title:title, content: content});
			dojo.body().appendChild(theDialog.domNode);
			theDialog.startup();
			theDialog.show();
		}

		function validateCreateParams()
		{
			var createPatronParams = patronCreate.getElementsByTagName("input");
			for( i = 0; i < createPatronParams.length; i++ )
			{
				var param = createPatronParams[i];
				if( param.WMrequired == "true" && param.value.length == 0 )
				{
					messageBox("Required field '" + param.title + "' is missing.", "Invalid Parameter");
					param.focus();
					return false;
				}
			}
			return true;
		}
		</script>
		<div class='patronLoginTitle'>Create New Account</div>
		<form id="patronCreate" method="post" action="<%=request.getContextPath()%>/LoginPatron">
			<input type="hidden" name="type" value="create"/>
			<table style="margin:auto;">
			<viewCfg:ViewForm viewName="patrons">
			<% if( inputVisible.equalsIgnoreCase("false") == false ){ %>
				<tr>
					<td><div class='patronLoginLabel'><%=inputText%></div></td><td>
						<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=inputField%>" maxlength="<%=inputLen%>"/>
					</td>
					<td>
				<% if( inputRequired == true ){ %>
						<span style="color:red">*</span>
				<% } %>
					</td>
				</tr>
			<% } %>
			</viewCfg:ViewForm>
				<tr>
					<td/><td colspan="2"><span style="color:red;font:8pt verdana;">* Required</span></td>
				</tr>
				<tr><td valign="middle" colspan="3" style="text-align:center;">
					<button dojoType="dijit.form.Button">
						Create New Account
						<script type="dojo/method" event="onClick">
						if( validateCreateParams() )
							patronCreate.submit();
					</script>
					</button>
				</td><td/></tr>
			</table>
		</form>
	</td></tr></table>
	</body>
</html>
