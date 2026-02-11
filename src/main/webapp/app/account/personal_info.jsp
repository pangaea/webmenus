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

</head>
<body>

	<!-- Draw timeline -->
	<jsp:include page="time_line.jsp">
		<jsp:param name="SelIdx" value="1" />
	</jsp:include>
	<hr/>
	<!--
		==================
		END OF PAGE HEADER
	-->
	
	
	<div id="syserr" style="display:none;">
	<div class="error"><html:errors property="firstName"/></div>
	<div class="error"><html:errors property="lastName"/></div>
	<div class="error"><html:errors property="address"/></div>
	<div class="error"><html:errors property="city"/></div>
	<div class="error"><html:errors property="state"/></div>
	<div class="error"><html:errors property="email"/></div>
	<div class="error"><html:errors property="phoneNum"/></div>
	</div>
	
	<center>
	
		<h2>Personal Information</h2>
		
		<div id="frame">
		
			<html:form action="personal_info.do">
			
				<div class="outer"><div class="inner">
					<table id="input_params">
						<tr><td>First Name:</td><td><html:text property="firstName"/></td></tr>
						<tr><td>Last Name:</td><td><html:text property="lastName"/></td></tr>
						<tr><td>Address:</td><td><html:text property="address"/></td></tr>
						<tr><td>City:</td><td><html:text property="city"/></td></tr>
						<tr><td>State:</td><td>
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
						<tr><td>Email:</td><td><html:text property="email"/></td></tr>
						<tr><td>Phone #:</td><td><html:text property="phoneNum"/></td></tr>
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
</body>

<script type="text/javascript">
	//$("div.inner").corner("fray 10px").parent().css('padding', '10px').corner("fray 10px");
</script>

</html:html>