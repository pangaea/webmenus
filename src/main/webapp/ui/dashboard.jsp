<!--
Copyright (c) 2004-2014 Kevin Jacovelli
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
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/styles/dashboard.css"></link>
<%
	Credentials info = (Credentials)request.getSession().getAttribute("info");
%>

<!--%@ include file="/analytics/google-analytics.jsp"%-->
</head>
<body id="view_obj_body" style="visibility:hidden;">

	<!-- Page Header -->
	<div id="header" class="ui-layout-north">
	<%@ include file="view_obj_header.jsp"%>
	</div>


	<div id="navbar" style="overflow:hidden;" class="ui-layout-west">
	<%@ include file="view_obj_tabs.jsp"%>
	</div>
	<div id="viewport" style="padding:8px;" class="ui-layout-center">


		<!-- Navigation Menu -->

		<div class="wm-dashboard-page">
		<!-- BEGINING OF DASHBOARD BODY -->
			<div class="wm-wrapper">
				<div class="wm-locations">
					<div id="location-title">Locations</div>
					<div class="wm-location-widgets">
					
					<summaryCfg:EnumLocations credentials="<%=info.m_sTicket%>">
					<div class="wm-location widget">
						<div>
							<% if(locationLogo.length() > 0){ %>
							<img src="/webmenus/ImageViewer<%=locationLogo%>"/>
							<% } %>
							<div class="wm-location-title" value="<%=locationId%>">
								<a href="view_obj.jsp?view=locations&locations_searchStr=%2526id%253D<%=locationId%>"><%=locationName%></a>
							</div>
							<div><%=locationAddress%></div>
							<div><%=locationCity%>, <%=locationState%> <%=locationZip%></div>
							<div><%=locationPhone%></div>
						</div><br/>
						

						<label style="width:100%;white-space:nowrap;font-size:12pt;">
						<a class="live-menu-link" href="javascript:OpenMenuWindow('<%=locationId%>')">view live menus</a>
						Menus Link (add this code to your site)
						</label>
						<textarea readonly style="resize:none;width:100%;height:40px;background-color:#dddddd;" id="menuLink-<%=locationId%>" class="wm-link-display"></textarea>
						<script type="text/javascript">
						$("#menuLink-<%=locationId%>").text( CreateMenusLink("<%=locationId%>") );
						</script>
						<br/>
						
						<% if(locationThemeId.length() == 0){ %>
							<span id="theme-missing-warning" style="color:red;">WARNING: No theme selected. Menus will not function unless a theme is selected. Click "Menu Designer" (below) or the location name (above) to select one.</span>
						<% } %>
						
						<br/>
						
						<button class="buttonx" id="location-designer-<%=locationIndex%>"
								onclick="OpenMenuDesigner('<%=locationId%>')">Menu Designer</button>
						<button class="buttonx" id="location-orders-<%=locationIndex%>"
								onclick="location.href = 'view_obj.jsp?view=menu_orders&menu_orders_searchStr=%2526location%253D<%=locationId%>'">View Orders</button>					
					</div>
					</summaryCfg:EnumLocations>
					
					</div>
				</div>
				
				<div class="wm-quickbar">
					<div id="quickbar-title">Customizations</div>
					<div class="wm-quickbar-widgets">
					
					<div id="themes" class="widget">
						<a class="manage-link" href="view_obj.jsp?view=themes">Manage</a>
						<h1>Themes</h1>
						<summaryCfg:EnumThemes credentials="<%=info.m_sTicket%>">
						<div>
							<a class="_button wm-show-theme" theme_id="<%=themeId%>" href="javascript:void(0)">preview</a>
							<div id="template_<%=themeId%>" style="display:none;"><%=themeTemplate%></div>
							<div><a href="view_obj.jsp?view=themes&themes_searchStr=%2526id%253D<%=themeId%>"><%=themeName%></a></div>
						</div>
						</summaryCfg:EnumThemes>
					</div>
					<div id="schedules" class="widget">
						<a class="manage-link" href="view_obj.jsp?view=schedules">Manage</a>
						<h1>Schedules</h1>
						<summaryCfg:EnumSchedules credentials="<%=info.m_sTicket%>">
						<div>
							<a href="javascript:OpenScheduleDesigner('<%=scheduleId%>')">design</a>
							<div><a href="view_obj.jsp?view=schedules&schedules_searchStr=%2526id%253D<%=scheduleId%>"><%=scheduleName%></a></div>
						</div>
						</summaryCfg:EnumSchedules>
					</div>
					
					</div>
				</div>
			</div>

		<!-- ENDING OF DASHBOARD BODY -->
		</div>

		<!-- TOUR -->
		<%@ include file="../app/slideshows/dashboard.jsp"%>

<!-- --------------- -->

	</div>
	<%@ include file="view_obj_foot.jsp"%>
	<div class="form_fields" style="display:none;"></div>
	
<script type="text/javascript">
$(function(){
	$("body").css("visibility", "visible");
	$(".wm-show-theme").click(function(){
		var template = $("#template_"+$(this).attr("theme_id")).html();
		updateSampleDisplay(template, $(this).attr("theme_id"));
		$("#sample_menu_link").click();
	});
	
	$('.wm-link-display').on("focus", function(e) {
		  e.target.select();
		  $(e.target).one('mouseup', function(e) {
		    e.preventDefault();
		  });
		});
});
</script>
</body>
</html>
