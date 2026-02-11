<%@ page import="com.genesys.util.ServletUtilities"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>chowMagic - Account Created Successfully</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/account_styles.css"></link>
</head>
<body>
<center>
<h2>Congratulations, your account has been created successfully.</h2>
<div class="outer">
Click <a href="<%=request.getContextPath()%>/ui/login.jsp">here</a> to login to the Main Console.
<hr/>
<table id="input_params">
<tr><th class="title" colspan="2">Account Information:</th></tr>
<tr><td>UserName:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "account_username", "")%></span></td></tr>
<tr><td>First Name:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_firstName", "")%></span></td></tr>
<tr><td>Last Name:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_lastName", "")%></span></td></tr>
<tr><td>Email:</td><td><span class="form_data"><%=ServletUtilities.getCookieValue(request.getCookies(), "personal_email", "")%></span></td></tr>
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
<hr/>
Click <a href="<%=request.getContextPath()%>/ui/login.jsp">here</a> to login to the Main Console.
</div>
</center>
</body>
</html:html>