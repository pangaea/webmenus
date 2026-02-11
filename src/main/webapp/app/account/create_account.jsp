<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="com.genesys.util.ServletUtilities"%>

<!DOCTYPE struts-config PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>chowMagic - Create Account</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/account_styles.css"></link>

<!-- JQuery : Core -->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/jquery/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/jquery/css/ui.theme.css" /-->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/cupertino/jquery-ui-1.9.2.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.corner-1.7.js"></script>

<script type="text/javascript">
$(function() {
	var msg = $.trim($("#syserr").text());
	if( msg.length > 0 )
		messageBoxEx($("#syserr").html(), "Errors", {"Ok": function(){$(this).dialog("close");}});
});
function goNav(page)
{
	//document.location.href = "<%=request.getContextPath()%>/app/account/" + page;
	document.location.href = page;
}
</script>

<link href="<%=request.getContextPath()%>/w3/styles/masterpage.css" type="text/css" rel="stylesheet"/>

<!--%@ include file="/analytics/google-analytics.jsp"%-->
</head>
<body>




<div id="wrapper">
	<div id="header_left"></div>
	<div id="header">
		<h1 style="margin-top: 28px;">
			<a href="/">
				<!--img src="<%//=request.getContextPath()%>/w3/images/cooltext1734059047.png"/-->
				chow<span style="color:#1B99E8">MAGIC</span>
			</a>
		</h1>
		<ul id="page_links">
			<li><a href="/">Home</a></li>
			<li><a href="/product-details">Product Details</a></li>
			<li><a href="/contact-us">Contact Us</a></li>
			<li><a href="<%=request.getContextPath()%>/app/account/create_account.jsp">Signup</a></li>
			<li><a href="<%=request.getContextPath()%>/ui/login.jsp">Login</a></li>
		</ul>
	</div>
	<br/>


	<div id="mainx">
	<div id="syserr" style="display:none;">
		<div class="error"><html:errors property="firstName"/></div>
		<div class="error"><html:errors property="lastName"/></div>
		<div class="error"><html:errors property="email-missing"/></div>
		<div class="error"><html:errors property="email-invalid"/></div>
		<div class="error"><html:errors property="username"/></div>
		<div class="error"><html:errors property="password"/></div>
		<div class="error"><html:errors property="password2"/></div>
		<div class="error"><html:errors property="sample_menus"/></div>
		<div class="error"><html:errors property="accept"/></div>
	</div>

	<center>

		<div id="frame">
			<html:form action="create_account.do">

				<div class="outer">
					<div style="float: left; font: 16pt verdana; color: navy; text-align: left;">Create a FREE Account</div>
					<div style="clear:both"></div>
					<hr style="border: 1px solid navy;"/>

					<div class="inner">

						<table id="input_params">

							<tr><th>First Name:</th><td><html:text property="firstName"/></td></tr>
							<tr><th>Last Name:</th><td><html:text property="lastName"/></td></tr>
							<tr><th>Email:</th><td><html:text property="email"/></td></tr>
							<tr><th>Username:</th><td><html:text property="username"/></td></tr>
							<tr><th>Password:</th><td><html:password property="password"/></td></tr>
							<tr><th>Password (Verify):</th><td><html:password property="password2"/></td></tr>
							<tr><th>Choose sample menus:</th><td valign="top">
							<html:select property="sample_menus">
								<html:option value="">-- Please Select --</html:option>
								<html:option value="sample">Basic Sample Menus</html:option>
								<html:option value="american">American Cuisine</html:option>
								<html:option value="italian">Pizzeria &amp; Restaurant</html:option>
								<html:option value="chinese">Chinese Restaurant</html:option>
							</html:select>
							</td></tr>
							<tr><th></th><td style="text-align:left;">
								<html:checkbox property="acceptEula" style="width:20px;" value="yes"><span>I accept <a href="<%=request.getContextPath()%>/app/account/license_agreement.html" target="_blank">license</a></span></html:checkbox>
							</td></tr>

						</table>
					</div>
				</div>

				<table style="width:600px;">
					<tr><td colspan="2" style="text-align:right;">
						<html:button property="backButton" onclick="goNav('/');">Cancel</html:button>
						<html:submit>Sign Up Now</html:submit>
					</td></tr>
				</table>

			</html:form>
		</div>

	</center>
	</div>

</div>
</body>

<script type="text/javascript">
	//$("div.inner").corner("fray 10px").parent().css('padding', '10px').corner("fray 10px");
</script>

</html:html>
