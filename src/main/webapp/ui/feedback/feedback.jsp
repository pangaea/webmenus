<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%//@ page import="com.genesys.util.ServletUtilities"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>Feedback</title>

<link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>

<script type="text/javascript">
$(function() {
	var msg = $.trim($("#syserr").text());
	if( msg.length > 0 )
		messageBoxEx($("#syserr").html(), "Errors", {"Ok": function(){$(this).dialog("close");}});
});
</script>
</head>
<body>


<div id="syserr" style="display:none;">
<div class="error"><html:errors property="errors"/></div>
</div>

<h2>Tell us what you think of chowMagic.</h2>
<hr/>
<html:form action="feedback.do">
Subject:
<html:text style="width:600px;" property="subject"/>
<br/>
Details:
<html:textarea style="width:600px;height:180px;" property="body"/>
<br/><br/>
<html:submit >Submit</html:submit>
<html:button property="backButton" onclick="parent.$.fancybox.close()">Cancel</html:button>

</html:form>

</body>
</html:html>