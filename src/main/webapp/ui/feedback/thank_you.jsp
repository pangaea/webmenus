<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html:html>
<head>
<title>Thank You For Your Feedback</title>

<link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
		
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/create_account.css"></link>
</head>
<body>


<h3>Thank You For Your Feedback</h3>
<html:button property="backButton" onclick="parent.$.fancybox.close()">Close</html:button>

</body>
</html:html>