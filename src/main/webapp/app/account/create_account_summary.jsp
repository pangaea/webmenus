<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="com.genesys.util.ServletUtilities"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>chowMagic - Create Account</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<!-- JQuery : Core -->
<!--link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link-->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/base2.css" /-->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/ui.theme.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
		
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.corner-1.7.js"></script>
		
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/account_styles.css"></link>

<script type="text/javascript">
$(function() {
	var msg = $.trim($("#syserr").text());
	if( msg.length > 0 )
		messageBoxEx($("#syserr").html(), "Errors", {"Ok": function(){$(this).dialog("close");}});
});
function goNav(page)
{
	document.location.href = "<%=request.getContextPath()%>/app/account/" + page;
}
</script>
<link href="<%=request.getContextPath()%>/w3/styles/masterpage.css" type="text/css" rel="stylesheet">
</head>
<body style="text-align:center;">




<div id="wrapper">
<div id="header_left"></div>
<div id="header">
<h1 style="margin-top: 8px;">
<a href=""><img src="<%=request.getContextPath()%>/w3/images/cooltext1734059047.png"></a>
</h1>
<ul id="page_links">
<li>
<a href="<%=request.getContextPath()%>/page/home">Home</a>
</li>
<li>
<a href="<%=request.getContextPath()%>/page/product_details">Product Details</a>
</li>
<li>
<a href="<%=request.getContextPath()%>/page/contact_us">Contact Us</a>
</li>
<li>
<a href="<%=request.getContextPath()%>/app/account/create_account.jsp">Create a Free Account</a>
</li>
</ul>
</div>
<br/>




<!-- Draw timeline -->
<jsp:include page="time_line.jsp">
	<jsp:param name="SelIdx" value="3" />
</jsp:include>

<!--
	==================
	END OF PAGE HEADER
-->

<center>

<div id="syserr" style="display:none;">
</div>

<!--h2>Account Summary</h2-->

<div id="frame">

<html:form action="create_account_summary.do" onsubmit="startAnimation()">



<div class="outer">

<div style="float: left; font: 16pt verdana; color: navy; text-align: left;">Account Summary</div>
<div style="float: right;"><html:submit>Create Account</html:submit></div>
<div style="clear:both"></div>
<hr style="border: 1px solid navy;"/>

<div class="inner">
<table id="input_params">
<tr><th class="title" colspan="2">Account Information:</th></tr>
<tr><td>First Name:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_firstName", "")%></span></td></tr>
<tr><td>Last Name:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_lastName", "")%></span></td></tr>
<tr><td>Email:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_email", "")%></span></td></tr>

<tr><td>UserName:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "account_username", "")%></span></td></tr>
<tr><th class="title" colspan="2">Restaurant Information:</th></tr>
<tr><td>Name:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_name", "")%></span></td></tr>
<tr><td>Address:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_address", "")%></span></td></tr>
<tr><td>City:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_city", "")%></span></td></tr>
<tr><td>State:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_state", "")%></span></td></tr>
<tr><td>Zip:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_zip", "")%></span></td></tr>
<tr><td>Timezone:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_timezone", "")%></span></td></tr>
<tr><td>Email:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_email", "")%></span></td></tr>
<tr><td>Phone #:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_phoneNum", "")%></span></td></tr>
<tr><td>Sample Menu(s):</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "restaurant_sample_menus", "")%></span></td></tr>
</table>
<html:checkbox property="emailMe" style="width:20px;" value="yes"><span>Notify me, via email, of promotions and offers</span></html:checkbox>
</div></div>

<table style="width:600px;"><tr><td style="text-align:left;">
<html:button property="backButton" onclick="goNav('restaurant_info.jsp');">Back</html:button>
</td><td style="text-align:right;">
<html:submit>Create Account</html:submit>
</td></tr></table>

</html:form>

</div>

</center>
</div>
</body>

<script type="text/javascript">
	//$("div.inner").corner("fray 10px").parent().css('padding', '10px').corner("fray 10px");
</script>

</html:html>