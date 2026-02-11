<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="auth.jsp" %>
<%@ include file="clienthdr.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/summary.tld" prefix="summaryCfg" %>
<%@ page import="com.genesys.repository.*"%>
<%
	// Load view data
	viewConfigBean.setView( request.getParameter("view") );
	String viewParam = viewConfigBean.getView();
	String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
%>

<!------------------------------------------------------------------------------------------------>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"> 

<head>
<%@ include file="view_obj_head.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/events/location_events.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/events/schedule_events.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/events/themes_events.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/styles/summary.css"></link>


<!--%@ include file="/analytics/google-analytics.jsp"%-->
</head>
<body id="view_obj_body">

<!-- Page Header -->
<div id="header" class="ui-layout-north">
<%@ include file="view_obj_header.jsp"%>
</div>

c
<div id="navbar" style="overflow:hidden;" class="ui-layout-west">
<%@ include file="view_obj_tabs.jsp"%>
</div>
<div id="viewport" style="padding:8px;" class="ui-layout-center">


<!-- Navigation Menu -->

<%
	Credentials info = (Credentials)request.getSession().getAttribute("info");
%>
<div class="wm-summary-page">

<div class="wm-heading">
Hello <%=info.m_UserName%>, welcome to chowMagic.
</div>

<div class="wm-help wm-location-help" style="display:none;">
Below is a summary of your account. From this page you can design menus for your location, edit the theme to make it look the way you want and schedule times for each menu to be open for online orders.
Click 'view live menus' to see your location's live menu system. Click 'view menus link' to see the link to the live menus. You can place the link on any web page you want.
</div>

<div class="wm-location-widget">
<div class="wm-page-widget">
<div id="location-title">Locations</div>
<div class="wm-help wm-location-help">
Locations represent either a single restaurant or a grouping of restaurants sharing menu items.
Click 'view live menus' to see your location's live menu system. Click 'view menus link' to see the link to the live menus. You can place the link on any web page you want.
</div>
<table>
<summaryCfg:EnumLocations credentials="<%=info.m_sTicket%>">
<tr>
	<td class="item-name">
		<div value="<%=locationId%>"><%=locationName%>
		</div>
	</td>
	<td class="item-action">
		<a class="_button" href="view_obj.jsp?view=locations&locations_searchStr=%2526id%253D<%=locationId%>">view/edit</a>
	</td>
	<td class="item-action">
		<a class="_button" href="javascript:OpenMenuDesigner('<%=locationId%>')">design menus</a>
	</td>
	<td class="item-action">
		<a class="_button" href="javascript:OpenMenuWindow('<%=locationId%>')">view live menus</a>
	</td>
	<td class="item-action">
		<a class="_button" href="javascript:OpenMenusLink('<%=locationId%>')">view menus link</a>
	</td>
	<td class="item-action">
		<a class="_button" href="view_obj.jsp?view=menu_orders&menu_orders_searchStr=%2526location%253D<%=locationId%>">view orders</a>
	</td>
</tr>
<tr><td colspan="6" class="separator"></td></tr>
</summaryCfg:EnumLocations>
</table>
</div>
</div>




<div style="clear:both;"></div>





<div class="wm-theme-widget">
<div class="wm-page-widget">
<div id="theme-title">Themes</div>
<div class="wm-help wm-theme-help">
Themes define the look and feel of the menus associated with a location. Along with changing colors and fonts, themes allow for customizing the menu template itself.
</div>
<table>
<summaryCfg:EnumThemes credentials="<%=info.m_sTicket%>">
<tr>
	<td class="item-name">
		<div value="<%=themeId%>"><%=themeName%>
		</div>
	</td>
	<td class="item-action">
		<a class="_button" href="view_obj.jsp?view=themes&themes_searchStr=%2526id%253D<%=themeId%>">view/edit</a>
	</td>
	<td class="item-action">
		<a class="_button wm-show-theme" theme_id="<%=themeId%>" href="javascript:void(0)">preview</a>
		<div id="template_<%=themeId%>" style="display:none;"><%=themeTemplate%></div>
	</td>
</tr>
<tr><td colspan="3" class="separator"></td></tr>
</summaryCfg:EnumThemes>
</table>
</div>
</div>




<div class="wm-schedule-widget">
<div class="wm-page-widget">
<div id="schedule-title">Schedules</div>
<div class="wm-help wm-schedule-help">
Schedules define times when a menu will be open to take online orders. A schedule can be associated with any individual menu so it will open and close automatically.
</div>
<table>
<summaryCfg:EnumSchedules credentials="<%=info.m_sTicket%>">
<tr>
	<td class="item-name">
		<div value="<%=scheduleId%>"><%=scheduleName%>
		</div>
	</td>
	<td class="item-action">
		<a class="_button" href="view_obj.jsp?view=schedules&schedules_searchStr=%2526id%253D<%=scheduleId%>">view/edit</a>
	</td>
	<td class="item-action">
		<a class="_button" href="javascript:OpenScheduleDesigner('<%=scheduleId%>')">design</a>
	</td>
</tr>
<tr><td colspan="3" class="separator"></td></tr>
</summaryCfg:EnumSchedules>
</table>
</div>
</div>




<div style="clear:both;"></div>

<div class="form_fields" style="display:none;"></div>


</div>

<!-- TOUR -->
<%@ include file="../app/slideshows/dashboard.jsp"%>


<script type="text/javascript">
$(function(){
	$(".wm-show-theme").click(function(){
		var template = $("#template_"+$(this).attr("theme_id")).html();
		updateSampleDisplay(template, $(this).attr("theme_id"));
		$("#sample_menu_link").click();
	});
});
</script>

<!-- --------------- -->

</div>
<%@ include file="view_obj_foot.jsp"%>
</body>
</html>
