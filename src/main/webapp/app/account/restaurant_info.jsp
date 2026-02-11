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
<style type="text/css">
input[type="text"]{
	width: 270px !important;
}
select{
	width: 276px !important;
}
</style>

<script type="text/javascript">
$(function() {
	//$('div.inner').wrap('<div class="outer"></div>');
	//$("div.inner").corner("rounded 25px");//.parent().css('padding', '10px').corner("bevel 14px");
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
	<jsp:param name="SelIdx" value="2" />
</jsp:include>

<!--
	==================
	END OF PAGE HEADER
-->



<div id="syserr" style="display:none;">
<div class="error"><html:errors property="name"/></div>
<div class="error"><html:errors property="address"/></div>
<div class="error"><html:errors property="city"/></div>
<div class="error"><html:errors property="state"/></div>
<div class="error"><html:errors property="zip"/></div>
<div class="error"><html:errors property="timezone"/></div>
<div class="error"><html:errors property="email"/></div>
<div class="error"><html:errors property="phoneNum"/></div>
<div class="error"><html:errors property="sample_menus"/></div>
</div>

<center>

<!--h2>Restaurant Information</h2-->

<div id="frame">

<html:form action="restaurant_info.do">



<div class="outer">

<div style="float: left; font: 16pt verdana; color: navy; text-align: left;">Restaurant Information <span style="font-size:11pt; color:#FF0000;">(FYI... This information can be changed later.)</span></div>
<div style="clear:both"></div>
<hr style="border: 1px solid navy;"/>

<div class="inner">
<table id="input_params">
<tr><th>Name:</th><td><html:text property="name"/></td></tr>
<tr><th>Address:</th><td><html:text property="address"/></td></tr>
<tr><th>City:</th><td><html:text property="city"/></td></tr>
<tr><th>State:</th><td>
<html:select property="state">
	<html:option value="AL">Alabama</html:option>
	<html:option value="AK">Alaska</html:option>
	<html:option value="AZ">Arizona</html:option>
	<html:option value="AR">Arkansas</html:option>
	<html:option value="CA">California</html:option>
	<html:option value="CO">Colorado</html:option>
	<html:option value="CT">Connecticut</html:option>
	<html:option value="DE">Delaware</html:option>
	<html:option value="DC">District of Columbia</html:option>
	<html:option value="FL">Florida</html:option>
	<html:option value="GA">Georgia</html:option>
	<html:option value="HI">Hawaii</html:option>
	<html:option value="ID">Idaho</html:option>
	<html:option value="IL">Illinois</html:option>
	<html:option value="IN">Indiana</html:option>
	<html:option value="IA">Iowa</html:option>
	<html:option value="KS">Kansas</html:option>
	<html:option value="KY">Kentucky</html:option>
	<html:option value="LA">Louisiana</html:option>
	<html:option value="ME">Maine</html:option>
	<html:option value="MD">Maryland</html:option>
	<html:option value="MA">Massachusetts</html:option>
	<html:option value="MI">Michigan</html:option>
	<html:option value="MN">Minnesota</html:option>
	<html:option value="MS">Mississippi</html:option>
	<html:option value="MO">Missouri</html:option>
	<html:option value="MT">Montana</html:option>
	<html:option value="NE">Nebraska</html:option>
	<html:option value="NV">Nevada</html:option>
	<html:option value="NH">New Hampshire</html:option>
	<html:option value="NJ">New Jersey</html:option>
	<html:option value="NM">New Mexico</html:option>
	<html:option value="NY">New York</html:option>
	<html:option value="NC">North Carolina</html:option>
	<html:option value="ND">North Dakota</html:option>
	<html:option value="OH">Ohio</html:option>
	<html:option value="OK">Oklahoma</html:option>
	<html:option value="OR">Oregon</html:option>
	<html:option value="PA">Pennsylvania</html:option>
	<html:option value="RI">Rhode Island</html:option>
	<html:option value="SC">South Carolina</html:option>
	<html:option value="SD">South Dakota</html:option>
	<html:option value="TN">Tennessee</html:option>
	<html:option value="TX">Texas</html:option>
	<html:option value="UT">Utah</html:option>
	<html:option value="VT">Vermont</html:option>
	<html:option value="VA">Virginia</html:option>
	<html:option value="WA">Washington</html:option>
	<html:option value="WV">West Virginia</html:option>
	<html:option value="WI">Wisconsin</html:option>
	<html:option value="WY">Wyoming</html:option>
</html:select>
</td></tr>

<tr><th>Zip:</th><td><html:text property="zip"/></td></tr>

<tr><th>Timezone:</th><td>
<html:select property="timezone">
		<html:option value="US/Pacific">(GMT-08:00) Pacific Time (US &amp; Canada)</html:option>
		<html:option value="US/Mountain">(GMT-07:00) Mountain Time (US &amp; Canada)</html:option>
		<html:option value="US/Central">(GMT-06:00) Central Time (US &amp; Canada)</html:option>
		<html:option value="US/Eastern">(GMT-05:00) Eastern Time (US &amp; Canada)</html:option>
</html:select>
</td></tr>

<tr><th>Email:</th><td><html:text property="email"/></td></tr>

<tr><th>Phone #:</th><td><html:text property="phoneNum"/></td></tr>

<%--tr><td>Country:</td><td>
<html:select property="country" >
	<html:option value="0">Select Country</html:option>
	<html:optionsCollection property="countryList" label="countryName" value="countryId" />
</html:select>
</td></tr--%>

<tr><th>Choose sample menus:</th><td valign="top">
<html:select property="sample_menus">
	<html:option value="">-- Please Select --</html:option>
	<html:option value="sample.xml">Basic Sample Menus</html:option>
	<html:option value="american.xml">American Cuisine</html:option>
	<html:option value="italian.xml">Pizzeria &amp; Restaurant</html:option>
	<html:option value="chinese.xml">Chinese Restaurant</html:option>
</html:select>
</td></tr>
</table>
</div></div>

<table style="width:600px;"><tr><td style="text-align:left;">
<html:button property="backButton" onclick="goNav('create_account.jsp');">Back</html:button>
</td><td style="text-align:right;">
<html:submit>Next</html:submit>
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