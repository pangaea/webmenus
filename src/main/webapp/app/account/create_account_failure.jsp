<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>chowMagic - Account Created Failed</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/account_styles.css"></link>
</head>
<body>
<center>
<h2>Sorry, there was a failure during the creation of your account.</h2>
<%
	String msg = request.getParameter("msg");
	if(msg != null && msg.length() > 0){
%>
<h3><%=msg%></h3>
<%
	}
%>
<a href="<%=request.getContextPath()%>/app/account/create_account.jsp">Try Again?</a>
</center>
</body>
</html:html>