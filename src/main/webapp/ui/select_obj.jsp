<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="auth.jsp" %>
<%@ include file="clienthdr.jsp" %>
<%
	// Load view data
	String viewParam = request.getParameter( "view" );
	viewConfigBean.setView( viewParam );

	String sAccRights = (String)request.getSession().getAttribute( "access_rights" );
	int iFormAccessLevel = clientSessionBean.getAccessRights( sAccRights, viewParam );
%>

<!DOCTYPE html>
<html>
	<head>
		<title>Genesys - Object Selection</title>
		
		<!-- Core Stylesheet -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/common.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/clientio.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xutils.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/utils.js"></script>
		
		<!-- JQuery : Core -->
		<%@ include file="jquery_includes.jsp"%>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>

		<!-- JQuery : jqGrid -->
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/css/ui.jqgrid.css" />	
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.layout.js" type="text/javascript"></script> 
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/i18n/grid.locale-en.js" type="text/javascript"></script> 
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> 
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.tablednd.js" type="text/javascript"></script> 
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.contextmenu.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/xlibs/jquery/jquery.jqGrid.fluid.js" type="text/javascript"></script>

		<%@ include file="select_obj_code.jsp" %>
		<!--jsp:include page="select_obj_code.jsp" flush="true"/-->
	</head>
	<body id="view_obj_body" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0" onload="initData();">
	
		<script language="JavaScript">
			var __xmlTitle = '';
			__xmlTitle +=	'<listbox>';
			__xmlTitle += 		'<columns>';
			<viewCfg:ViewList viewName="<%=viewConfigBean.getView()%>">
			__xmlTitle += 			'<column name="<%=itemField%>" width="<%=itemWidth%>" text="<%=itemTitle%>" valueof="<%=itemProperty%>" view="" fkey="<%=itemFKey%>"/>';
			</viewCfg:ViewList>
			__xmlTitle += 		'</columns>';
			__xmlTitle +=	'</listbox>';
			xmlTitle.create( __xmlTitle );

			var __xmlBlank = '';
			__xmlBlank +=	'<document>';
			__xmlBlank += 		'<collection objStart="0" objCount="<jsp:getProperty name="viewConfigBean" property="listSize"/>"></collection>';
			__xmlBlank +=	'</document>';
			var xmlBlank = new XDocument();
			xmlBlank.create( __xmlBlank );
		</script>

		<table cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td valign="top">
					<table align="center" cellspacing="5" width="100%">
						<tr>
							<td>
								<div style="margin-top:0px;" ID="ObjList"></div>
								<table id="gridview"></table>
								<div id="gridbox" class="scroll"></div>
								<script language="JavaScript">
								var listHTML = listboxHeader( xmlBlank, "I", "<%=request.getSession().getAttribute( "admin" )%>", "<jsp:getProperty name="viewConfigBean" property="access"/>", "0", "<jsp:getProperty name="viewConfigBean" property="title"/>" );
								//listHTML += listboxDraw( xmlTitle, xmlBlank, <jsp:getProperty name="viewConfigBean" property="listSize"/>, listboxCallback, buttonCallback, "I" );
								ObjList.innerHTML = listHTML;
								//document.all.access_level.disabled = true;
								</script>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="right">
                    <SPAN ID="buttonToolbar"></SPAN>
					<script language="JavaScript">
						setupButtons();
					</script>
				</td>
			</tr>
		</table>
	</body>
</html>
