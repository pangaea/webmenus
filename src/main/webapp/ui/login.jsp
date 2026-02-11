<%@ page import="com.genesys.SystemServlet"%>
<%@ page import="javax.servlet.http.*"%>

<%@ include file="clienthdr.jsp" %>

<%
//HttpSession thisSession = request.getSession();
//thisSession.invalidate();
%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>chowMagic - Login</title>
		<style>
		html, body {
			text-align: center;
			font-family: Verdana;
			font-size: 12px;
			margin: 0px;
		    height: 100%;
		}
		.main {
		    height: 100%;
		    width: 100%;
		    display: table;
		}
		.wrapper {
		    display: table-cell;
		    height: 100%;
		    vertical-align: middle;
		}
		.login-frame{
			box-shadow: 8px 8px 24px 0px #276873;
			width: 400px;
			margin: auto;
		}
		td.login_text
		{
			text-align: left;
			font-size:8pt;
			border: 1px;
			border-style: solid;
			border-top-color: lightgrey;
			border-left-color: lightgrey;
			border-bottom-color: #A7ABFB;
			border-right-color: #A7ABFB;
		}
		</style>
		<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/curvey.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		<script type="text/JavaScript" src="<%=request.getContextPath()%>/includes/common.js"></script>
		<script type="text/JavaScript">
		
			window.onload = function()
			{
				var userid = document.getElementById("userid");
				userid.focus();
			}
			
			function captureKeystoke()
			{
				if( window.event.keyCode == 13 )
				{
					method.submit();
					return false;
				}
				return true;
			}

			function buttonCallback( item )
			{
				switch( item )
				{
					case B_SUBMIT:
						method.submit();
						break;
				}
			}
		</script>
	</head>
	<body>
	<div class = "main"><div class = "wrapper">
		<%
			String errMsg = request.getParameter( "err" );
			if( errMsg != null ){ %>
				<font color="red"><%=errMsg%></font><br>
		<%	} %>

		<div class="login-frame">
		<form method="POST" action="<%=request.getContextPath()%>/ViewCmd" id="method" name="method" onkeypress="return captureKeystoke();" style="margin-top:0px;margin-bottom:0px;">
			<input type="hidden" id="call" name="call" value="login"/>
		<%
			String backUrl = request.getParameter( "backurl" );
			if( backUrl != null ){ %>
				<input type="hidden" id="backurl" name="backurl" value="<%=backUrl%>"/>
		<%	} %>
			<table cellpadding="2" align="center">
			<tr>
				<td valign="top">
					<table cellspacing="0" cellpadding="2">
					<tr>
						<td align="center"><img src="<%=request.getContextPath() + SystemServlet.getloginPageImage()%>"/></td>
					</tr>
					<tr>
						<td class="screen_title">Please Login:</td>
					</tr>
					<tr>
						<td align="center">
							<table>
							<tr>
								<td class="login_text">User ID:</td>
								<td><input style="width:200px" type="text" id="userid" name="userid"/></td>
							</tr>
							<tr>
								<td class="login_text">Password:</td>
								<td><input style="width:200px" type="password" id="password" name="password"/></td>
							</tr>
							<tr id="portal_row">
								<td class="login_text">Portal:</td>
								<td>
									<select style="width:200px" name="portal" id="portal">
									<viewCfg:EnumPortals>
										<option value="<%=portalName%>"><%=portalName%></option>
									</viewCfg:EnumPortals>
									</select>
								</td>
							</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div ID="submitButton"></div>
							<script language="JavaScript">
								if( document.all.portal.options.length <= 1 ) document.all.portal_row.style.display = "none";
								var submitButton = document.getElementById("submitButton");
								submitButton.innerHTML = buttonDraw( B_SUBMIT, "", "Login", "buttonCallback" );
							</script>
						</td>
					</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center">
					<a href="<%=request.getContextPath()%>/ui/security/forgot_password.jsp">Forgot Password</a>
				</td>
			</tr>
			</table>
		</form>
		</div>
	</div></div>
	</body>
</html>
