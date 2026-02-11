<%@ include file="page_header.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>

<!DOCTYPE html>
<html>
<head>
	<title>Patron Login</title>
	<meta name="HandheldFriendly" content="true" />
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.js"></script>
	
	<script type="text/javascript">
	function validateParams(formName)
	{
		$(".errMsg").html("");
		var errors = new Array();
		$("#" + formName + " input[WMrequired=true]").each(function(){
			if( $(this).val().length == 0 ){
				errors.push("Required field '" + $(this).attr("title") + "' is missing.");
			}
		})
		if(errors.length > 0){
			$(".errMsg").html(errors.join("<br/>"));
			return false;
		}
		$("#" + formName).submit();
		return false;
	}
	$(function(){
		$("a").click(function(){
			$(".errMsg").html("");
		});
	});
	</script>
</head>
<body>
<%
	String errMsg = request.getParameter("msg");
	if( errMsg == null ) errMsg = new String("");
	
	String create = request.getParameter("create");
	if( create == null ){
%>
<%@ include file="_login_patron_login.jsp" %>
<%@ include file="_login_patron_create.jsp" %>	
<%	}else{ %>
<%@ include file="_login_patron_create.jsp" %>
<%@ include file="_login_patron_login.jsp" %>	
<% 	} %>




</body>
</html>
