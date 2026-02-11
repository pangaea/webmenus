<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%//@ page import="com.genesys.util.ServletUtilities"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>Forgot Password</title>

<link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
		
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/create_account.css"></link>

<script type="text/javascript">
$(function() {
	var msg = $.trim($("#syserr").text());
	if( msg.length > 0 )
		messageBoxEx($("#syserr").html(), "Errors", {"Ok": function(){$(this).dialog("close");}});
});
function goNav(path)
{
	document.location.href = "<%=request.getContextPath()%>" + path;
}
</script>
</head>
<body>


<div id="syserr" style="display:none;">
<div class="error"><html:errors property="errors"/></div>
</div>

<h2>Recover Your Password</h2>
<hr/>
<html:form action="forgot_password.do">
<em>
Enter your username here and a new password will be generated and sent to the associated email address.<br/>
NOTE: You should immediately login and change your password since email isn't very secure.
</em>
<br/><br/>
<table id="input_params">

<tr><td>Username:</td><td><html:text property="username"/></td></tr>

</table>
<br/>
<html:submit>Submit</html:submit>
<html:button property="backButton" onclick="goNav('/ui/login.jsp');">Cancel</html:button>

</html:form>

</body>
</html:html>