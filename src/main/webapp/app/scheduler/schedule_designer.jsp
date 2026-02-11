<!--
Copyright (c) 2004-2011 Kevin Jacovelli
All Rights Reserved
-->

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%//@ page import="com.genesys.util.ServletUtilities"%>

<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
}
</script>
<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
		//response.sendRedirect( "login.jsp" ); return;
%>
		<html>
			<head></head>
			<body onload="backtologin();"><em>Your session has timed out...</em></body>
		</html>
<%
		return;
	}
%>



<%//@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu Designer</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/excite-bike/jquery-ui-1.8.18.custom.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/scheduledesigner.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/autoNumeric-1.7.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/jquery.genesys.hint.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-sliderAccess.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/scheduledesigner.js"></script>
		
<script type="text/javascript">
$(function(){
	$("input").change(function(){
		$(this).removeClass("hint");
		drawSchedule();
	}).hint("hint");
	
	var msg = $.trim($("#syserr").text());
	if( msg.length > 0 )
		messageBoxEx($("#syserr").html(), "Errors", {"Ok": function(){$(this).dialog("close");}});
});
</script>
</head>
<body>

SCHEDULE DESIGNER
<hr/>
<html:form action="schedule_designer.do">
<html:hidden property="id"/>

<div id="syserr" style="display:none;">
	<html:errors property="name"/>
</div>

Name: <html:text property="name" title="Schedule Name"/>&nbsp;&nbsp;&nbsp;&nbsp;
Description: <html:text property="desc" title="Schedule Description" styleClass="schedule_desc"/>
<hr/>
<table cellpadding="0" cellspacing="2" id="block">
<tr>
	<th>Sunday</th>
	<th>Monday</th>
	<th>Tuesday</th>
	<th>Wednesday</th>
	<th>Thursday</th>
	<th>Friday</th>
	<th>Saturday</th>
</tr>
<tr class="cal_display">
	<td class="ui-widget-content"><div id="display2_sat" class="ui-state-default"></div><div id="display_sun" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_sun" class="ui-state-default"></div><div id="display_mon" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_mon" class="ui-state-default"></div><div id="display_tue" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_tue" class="ui-state-default"></div><div id="display_wed" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_wed" class="ui-state-default"></div><div id="display_thr" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_thr" class="ui-state-default"></div><div id="display_fri" class="ui-state-default"></div></td>
	<td class="ui-widget-content"><div id="display2_fri" class="ui-state-default"></div><div id="display_sat" class="ui-state-default"></div></td>
</tr>
<tr class="cal_starttime_row">
	<td><html:text property="timeMapped(SunStartTime)" title="Start Time" styleClass="datatype_time start_time_sun"/></td>
	<td><html:text property="timeMapped(MonStartTime)" title="Start Time" styleClass="datatype_time start_time_mon"/></td>
	<td><html:text property="timeMapped(TueStartTime)" title="Start Time" styleClass="datatype_time start_time_tue"/></td>
	<td><html:text property="timeMapped(WedStartTime)" title="Start Time" styleClass="datatype_time start_time_wed"/></td>
	<td><html:text property="timeMapped(ThrStartTime)" title="Start Time" styleClass="datatype_time start_time_thr"/></td>
	<td><html:text property="timeMapped(FriStartTime)" title="Start Time" styleClass="datatype_time start_time_fri"/></td>
	<td><html:text property="timeMapped(SatStartTime)" title="Start Time" styleClass="datatype_time start_time_sat"/></td>
</tr>
<tr class="cal_to_row">
	<td>- to -</td>
	<td>- to -</td>
	<td>- to -</td>
	<td>- to -</td>
	<td>- to -</td>
	<td>- to -</td>
	<td>- to -</td>
</tr>
<tr>
	<td><html:text property="timeMapped(SunEndTime)" title="End Time" styleClass="datatype_time end_time_sun"/></td>
	<td><html:text property="timeMapped(MonEndTime)" title="End Time" styleClass="datatype_time end_time_mon"/></td>
	<td><html:text property="timeMapped(TueEndTime)" title="End Time" styleClass="datatype_time end_time_tue"/></td>
	<td><html:text property="timeMapped(WedEndTime)" title="End Time" styleClass="datatype_time end_time_wed"/></td>
	<td><html:text property="timeMapped(ThrEndTime)" title="End Time" styleClass="datatype_time end_time_thr"/></td>
	<td><html:text property="timeMapped(FriEndTime)" title="End Time" styleClass="datatype_time end_time_fri"/></td>
	<td><html:text property="timeMapped(SatEndTime)" title="End Time" styleClass="datatype_time end_time_sat"/></td>
</tr>
</table>
<hr/>
<div style="float:right;">
<html:submit>Save</html:submit>
<button onclick="parent.$.fancybox.close();">Close</button>
</div>
</html:form>
<script type="text/javascript">
attachToFields();
drawSchedule();
//parent.refreshView();
</script>

</body>
</html:html>