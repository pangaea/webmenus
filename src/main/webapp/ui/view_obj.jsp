<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="auth.jsp" %>
<%@ include file="clienthdr.jsp" %>
<%@ page import="com.genesys.repository.*"%>
<%
	Credentials info = (Credentials)request.getSession().getAttribute("info");

	// Load view data
	viewConfigBean.setView( request.getParameter("view") );
	String viewParam = viewConfigBean.getView();

	String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
	int iFormAccessLevel = clientSessionBean.getAccessRights( sAccRights, viewParam );

	// Page Level Variables
	boolean debug_parameters = false;
	
	// If the interface specifies a custom or blank template do one last check here and redirect
	String strTemplate = viewConfigBean.getTemplate();
	if( strTemplate.length() > 0 && strTemplate.equalsIgnoreCase("_blank") == true ){
		response.sendRedirect( strTemplate + "?" + request.getQueryString() );
	}
	
	boolean formOnly = false;
	String formOnlyParam = (String)request.getParameter("form_only");
	if( formOnlyParam != null && formOnlyParam.equalsIgnoreCase("1") ){
		formOnly = true;
	}
%>

<!------------------------------------------------------------------------------------------------>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"> 

<!--?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"-->

<head>
<%@ include file="view_obj_head.jsp"%>
<style type="text/css">
	.ui-row-ltr{
		min-height: <%=viewConfigBean.getListRowHeight()%>px;
		line-height: <%=viewConfigBean.getListRowHeight()%>px;
	}
</style>

<%@ include file="view_obj_code.jsp"%>

<%
	String viewEventsScript = viewConfigBean.getEventsScript();
	if( viewEventsScript.length() > 0 ){ %>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/events/<%=viewEventsScript%>"></script>
<%	} %>

<script language="JavaScript">
	//jQuery.fn.jBreadCrumb.defaults.easing = 'linear';
	jQuery(document).ready(function(){
		jQuery("#breadCrumb").jBreadCrumb({easing:'swing'});
    });
</script>
<!--%@ include file="/analytics/google-analytics.jsp"%-->
</head>
<body id="view_obj_body" style="visibility:hidden;">
	

<!-- Page Header -->
<div id="header" class="ui-layout-north">
<%@ include file="view_obj_header.jsp"%>
</div>

	
<!-- Navigation Menu -->
<div id="navbar" style="overflow:hidden;" class="ui-layout-west">
<%@ include file="view_obj_tabs.jsp"%>
</div>

<div id="viewport" style="padding:8px;" class="ui-layout-center">
<a id="external_form_new" class="external_form" href='<%=viewConfigBean.getFormExternal()%>'></a>
	
<%
	String hiddenFields = "";
	hiddenFields += "<input type='hidden' id='objNoEdit' name='objNoEdit' value='" + request.getParameter( "objNoEdit" ) + "'/>\n";
	int viewIndex = 0;
	String form_view = request.getParameter("formView");
	if( form_view != null && form_view.equalsIgnoreCase("Y") == true )
	{
%>
	<script type="text/javascript">
	$(function(){
		editMode=2;
		disableDisplay(false);
	});
	</script>
	<span class="view_title"><jsp:getProperty name="viewConfigBean" property="title"/>:</span>
	<div id="bigPanel" style="margin-right:4px;position:relative;">
<%
	}
	else
	{
%>
	<script type="text/javascript">
	$(function(){
		initData();
	});
	</script>

	<div id="bigPanel" style="margin-right:4px;position:relative;">
		<table style="margin:0 20px 0 4px; width: 98%;" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top" width="100%">
									
									

						<% 	String rootView = request.getParameter("root_view_0");
							if( rootView != null ){
						%>
						
<!--div class="breadCrumbLeft" style="float:left;">
Return To...
</div>
<div style="float:left;"-->
              					<!--div class="breadcrumb_row"-->
							<div class="breadCrumbHolder module">
              					<div id="breadCrumb" class="breadCrumb module">
              					<ul>
						
						
						
						
						<%--
							Beginning of page navigation stack
						--%>
						
						
						
						
						
                            <% 	do{ %>
							<jsp:setProperty name="viewConfigBean" property="view" value="<%=rootView%>"/>
                                  <%	if( rootView.equalsIgnoreCase( viewParam ) == false ){
									hiddenFields += "<input type='hidden' name='" + rootView + "_objstart' value='" + request.getParameter( rootView + "_objstart" ) + "'/>\n";
								}
								if( viewIndex == 0 ){ %>

							<%	}
								else
								{
									hiddenFields += "<input type='hidden' name='root_view_" + Integer.toString(viewIndex) + "' value='" + rootView + "'/>\n";
								}
								
								// Beginning of breadcrumb list item
							%>
									<li>
                          					<a href="javascript:executeDrillup( '<%=viewConfigBean.getView()%>', <%=viewIndex%> );">
                          						<% if( request.getParameter( rootView + "_data_name") != null ){ %>
                          							<%=viewConfigBean.getTitle()%>[<%=request.getParameter( rootView + "_data_name")%>]
                          						<% }else if( request.getParameter( rootView + "_data_email") != null ){ %>
                          							<%=viewConfigBean.getTitle()%>[<%=request.getParameter( rootView + "_data_email")%>]
                          						<% }else{ %>
                          							<%=viewConfigBean.getTitle()%>
                          						<% } %>
                          					</a>
                      					</li>
                      			<%
                      				// End of breadcumb list item
                      			%>

										
										
										<viewCfg:ViewForm viewName="<%=viewConfigBean.getView()%>">
										<%
											if( request.getParameter( rootView + "_filter_" + inputField ) != null )
											{
												hiddenFields += "<input type='hidden' id='" + rootView + "_filter_" + inputField + "' name='" + rootView + "_filter_" + inputField + "' value='" + request.getParameter( rootView + "_filter_" + inputField ) + "'/>\n";
											}
											if( inputVisible.equalsIgnoreCase( "false" ) == false )//&&
												//inputType.equalsIgnoreCase( "textarea" ) == false ){ 
											{
												if( inputType.equalsIgnoreCase( "object" ) == true )
												{
													hiddenFields += "<input type='hidden' id='" + rootView + "_display_" + inputField + "' name='" + rootView + "_display_" + inputField + "' value='" + request.getParameter( rootView + "_display_" + inputField ) + "'/>\n";
												}
												else if( inputType.equalsIgnoreCase( "boolean" ) == true )
												{
													String boolData = request.getParameter( rootView + "_data_" + inputField );
													if( boolData == null ){boolData = "off";}
													hiddenFields += "<input type='hidden' id='" + rootView + "_data_" + inputField + "' name='" + rootView + "_data_" + inputField + "' value='" + boolData + "'/>\n";
												}
												else
												{
													
													String data = request.getParameter( rootView + "_data_" + inputField );
													if( data != null )
														hiddenFields += "<input type='hidden' id='" + rootView + "_data_" + inputField + "' name='" + rootView + "_data_" + inputField + "' value=\"" + request.getParameter( rootView + "_data_" + inputField ).replace("\"", "\\\"") + "\"/>\n";
													else
														hiddenFields += "<input type='hidden' id='" + rootView + "_data_" + inputField + "' name='" + rootView + "_data_" + inputField + "' value=''/>\n";
												}

											}
										%>
										</viewCfg:ViewForm>

							<%
								int tabIndex = 1;
								hiddenFields += "<input type='hidden' name='" + rootView + "_seltab' value='" + request.getParameter( rootView + "_seltab" ) + "'/>\n";
								hiddenFields += "<input type='hidden' name='" + rootView + "_searchStr' value='" + request.getParameter( rootView + "_searchStr" ) + "'/>\n";
							%>

							<viewCfg:ViewLinks viewName="<%=viewConfigBean.getView()%>">
							<%  // D R A W   T A B S - S T A R T
								if( linkView.equalsIgnoreCase( viewParam ) == false &&
									request.getParameter( linkView + "_objstart" ) != null ){
									hiddenFields += "<input type='hidden' name='" + linkView + "_objstart' value='" + request.getParameter( linkView + "_objstart" ) + "'/>\n";
								}
								if( request.getParameter( linkView + "_filter_" + linkReference ) != null ){
									hiddenFields += "<input type='hidden' name='" + linkView + "_filter_" + linkReference + "' value='" + request.getParameter( linkView + "_filter_" + linkReference ) + "'/>\n";
								}
								tabIndex++;
								// D R A W   T A B S - E N D
						%>
							</viewCfg:ViewLinks>
						<%
							
							viewIndex++;
							rootView = request.getParameter("root_view_"+Integer.toString(viewIndex));
							}
							while( rootView != null );
						%>
							
							
							
							
							
						<%--
							Ending of page navigation stack
						--%>

							<jsp:setProperty name="viewConfigBean" property="view" value="<%=viewParam%>"/>
							<li>
								<span class="view_breadcumb_title"><jsp:getProperty name="viewConfigBean" property="title"/></span>
							</li>
						</ul>
						</div>
						</div>
						<div style="clear:both;">
						<%	} %>
<!--/div>
<div style="clear:left;"></div-->

					<div style="clear:both;margin-bottom:4px;">
					
					<span class="view_title"><jsp:getProperty name="viewConfigBean" property="title"/>:</span>
					<span class="view_description"><jsp:getProperty name="viewConfigBean" property="description"/></span>
					</div>
					
					<div id="wm-list-view-frame">
					
					<div style="margin-top:0px;" ID="ObjList"></div>
					<% if( viewConfigBean.getListVisible() && formOnly == false ){ %><div class="ui-widget-content"><% } %>
					<table id="gridview"></table>
					<!--div id="gridbox" class="scroll"></div-->
					<% if( viewConfigBean.getListVisible() && formOnly == false ){ %></div><% } %>
					<script language="JavaScript">
						var __xmlBlank = '';
						__xmlBlank +=	'<document>';
						__xmlBlank += 		'<collection objStart="0" objCount="<jsp:getProperty name="viewConfigBean" property="listSize"/>"></collection>';
						__xmlBlank +=	'</document>';
						var xmlBlank = new XDocument();
						//alert( document.getElementById( "__xmlBlank" ).innerHTML );
						xmlBlank.create( __xmlBlank );
						<%	if( viewConfigBean.getListVisible() == false || formOnly == true ){ %>
							ObjList.style.display = "none";
							gridview.style.display = "none";
							//gridbox.style.display = "none";
						<%	} %>
						<%	if( iFormAccessLevel != 2 ){ %>
						var mode = "Z";
						<%	}else if( viewConfigBean.getByRef().length() > 0 ){ %>
						var mode = "X";
						<%	}else{ %>
						var mode = "Y";
						<%	} %>
						var listHTML = listboxHeader( xmlBlank, mode, "<%=request.getSession().getAttribute( "admin" )%>", "<jsp:getProperty name="viewConfigBean" property="access"/>", "0", "<jsp:getProperty name="viewConfigBean" property="title"/>" );
						//var colsList = xmlTitle.m_xmldoc.selectNodes( "//columns/column" );
						//alert( colsList.length );
						//listHTML += listboxDraw( xmlTitle, xmlBlank, <jsp:getProperty name="viewConfigBean" property="listSize"/>, listboxCallback, buttonCallback, mode );
						ObjList.innerHTML = listHTML;
						<%	String noEdit = request.getParameter( "objNoEdit" );
							if( noEdit != null &&
								noEdit.equalsIgnoreCase( "Y" ) == true ){ %>
								buttonEnable( document.all.Button_New, false );
								buttonEnable( document.all.Button_Copy, false );
						<% 	} %>
						//document.all.access_level.disabled = true;
					</script>
					
					</div>
				</td>
			</tr>
			<tr><td id="no_results_msg_row"></td></tr>
			<%	if( viewConfigBean.getFormVisible().equalsIgnoreCase( "false" ) ){ %>
			<tr style="visibility:hidden;">
			<%	}else{ %>
			<tr>
			<%	} %>
				<td style="padding-top:8px;padding-bottom:0px;">
					
					<div id="tabs" style="border-width:0px;">
						<ul style="border-width:0px;">
							<li><a href="#">Details</a></li>
							<% 	int tabIdx = 1; %>
							<viewCfg:ViewLinks viewName="<%=viewConfigBean.getView()%>">
							<%
								//String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
								int iAccRights = clientSessionBean.getAccessRights( sAccRights, linkView );
								if( iAccRights > 0 ){
							%>
							<li style="top:6px;"><a style="cursor:pointer;" "href="#" id="tab_<%=linkView%>" onclick="executeDrilldown( '<%=linkView%>', '<%=viewConfigBean.getView()%>', '<%=linkReference%>', <%=tabIdx%>, <%=viewIndex%> );"><%=linkText%></a></li>
								<%	tabIdx++; %>
							<%	} %>
							</viewCfg:ViewLinks>
						</ul>
						<div style="clear:both;"></div>
					</div>

				
				
					
				</td>
			</tr>
			<%	if( viewConfigBean.getFormVisible().equalsIgnoreCase( "false" ) == true ){ %>
			<tr style="visibility:hidden;">
			<%	}else{ %>
			<tr>
			<%	} %>
				<td>
					<table align="left" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td valign="top">
								<table align="center" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
										<div id="edittoolbar" class="ui-widget-header">
											<script language="JavaScript">
											<%	if( iFormAccessLevel != 2 ){ %>
											setupToolbar( 2 );
											<%	}else{ %>
											setupToolbar( 0 );
											<%	} %>
											</script>
										</div>
										</td>
									</tr>
									<tr>
										<td class="form_panel">
<%
	}
%>

			<%@ include file="view_obj_form.jsp"%>

<%
	if( form_view == null || form_view.equalsIgnoreCase("Y") == false )
	{
%>
										</td>
									</tr>
									<!--tr><td height="50px"></td></tr-->
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>		
		
		<script language="JavaScript">
		disableDisplay( true );
		</script>
<%
	}
	else
	{
%>
		<center>
		<button onclick="saveObject();window.close();">Submit</button>
		</center>
<%
	}
%>


<!-- This code will output all posted form parameters -->
<%	if( debug_parameters == true ){ %>
		<table style="background-color:yellow;">
			<tr>
				<td colspan="2" align="center" style="font-size:22;">All Request Parameters</td>
			</tr>
<%
		// Iterate the form data
		java.util.Enumeration names = request.getParameterNames();
		while( names.hasMoreElements() )
		{
			String paramName = (String)names.nextElement();
%>
			<tr>
				<td align="left" style="background-color:gold;">
					<%=paramName%>:
				</td>
				<td align="left" style="background-color:khaki;">
					<%=request.getParameter(paramName)%>
				</td>
			</tr>
<%		} %>
		</table>
<%	} %>
<!-- ================================================== -->
</div>

</div>

<!-- div id="viewpanel"></div-->
<%@ include file="view_obj_foot.jsp"%>

<!-- TOUR -->
<%@ include file="../app/slideshows/item_management.jsp"%>



</body>
</html>
