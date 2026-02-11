<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu Designer</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/excite-bike/jquery-ui-1.8.18.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
</head>
<body>

SCHEDULE SUCCESSFULLY SAVED
<hr/>

<div style="float:right;">
<button onclick="location.href='<%=request.getContextPath()%>/app/scheduler/schedule_designer.jsp?id=<%=request.getParameter("id")%>'">Continue Editing</button>
<button onclick="parent.$.fancybox.close();">Close</button>
</div>

<script type="text/javascript">
parent.refreshView();
</script>

</body>
</html:html>