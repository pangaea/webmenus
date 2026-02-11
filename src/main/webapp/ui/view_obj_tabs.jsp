
<div id="accordion">
<%
int openTab = -1, i = 0;
%>
<viewCfg:NavbarTabs access="<%=sAccRights%>">
<% if( tabEmpty.equalsIgnoreCase( "true" ) == false ){	%>
<div>
	<h3><a href="#" style="font-size:10pt;"><%=tabText%></a></h3>
	<div style="padding:4px;">
	<viewCfg:NavbarButtons tab="<%=tabText%>" portal="<%=(String)request.getSession().getAttribute(\"portal\")%>">
	<%
		if( viewParam.equalsIgnoreCase(navView) ) openTab = i;
		//int iAccRights = clientSessionBean.getAccessRights( sAccRights, navView );
		if( navFilter.equalsIgnoreCase( "href" ) == true ||
			navFilter.equalsIgnoreCase( "js" ) == true ||
			clientSessionBean.getAccessRights( sAccRights, navView )  > 0 )
		{
			String sHREF = "";
			if( navFilter.length() <= 0 )
			{
				sHREF = "view_obj.jsp?view=" + navView;
				//sHREF = "view_obj.jsp?view=" + navView;
			}
			else
			{
				if( navFilter.equalsIgnoreCase( "href" ) == true || navFilter.equalsIgnoreCase( "js" ) == true )
				{
					sHREF = navView;
				}
				else
				{
					sHREF = "view_obj.jsp?view=" + navView + "&" + navView + "_filter_" + navFilter;
					//sHREF = "view_obj.jsp?view=" + navView + "&" + navView + "_filter_" + navFilter;
				}
			}
	%>
			<div style="width:100%;text-align:center;overflow:hidden;">
			<div style="text-align:left;">
			<table><tr><td valign="middle"></td><td valign="middle" class="nav_shortcut"
				style="background: url(<%=navImage%>) no-repeat 6px 10px #062134;">
			<% if( navFilter.equalsIgnoreCase( "js" ) == false ){ %>
				<a class="nav_link" href="javascript:void(0)" onclick="startAnimation(); setTimeout(function(){location.href='<%=sHREF%>';}, 1000);"><%=navText%></a>
			<% } else { %>
				<a class="nav_link" href="javascript:void(0)" onclick="setTimeout(function(){<%=sHREF%>}, 100);"><%=navText%></a>
			<% } %>
				<span class="nav_details"><%=navDetails%></span>
				<!--a class="nav_link" href="#" onclick="javascript:navGo('<-%=sHREF%->');startAnimation();"><-%=navText%-></a-->
			</td></tr></table>
			</div></div>
	<% } %>
	</viewCfg:NavbarButtons>
	</div>
</div>
<% i++;} %>
</viewCfg:NavbarTabs>
</div>
<!-- pre-load wait image -->
<img style="display:none;" src='../images/wait30trans.gif'/>

<script type="text/javascript">
//$(function(){
var tabActive = <%=openTab%>;
if( tabActive >= 0 ){
	//$.Storage.set("tabActive", "<%=openTab%>");
	sessionStorage.setItem("tabActive", "<%=openTab%>");
} else {
	//tabActive = parseInt($.Storage.get("tabActive"));
	tabActive = parseInt(sessionStorage.getItem("tabActive"));
}
// Accordion
$("#accordion").accordion({ header: "h3", autoHeight: false, active: tabActive });

//});
</script>