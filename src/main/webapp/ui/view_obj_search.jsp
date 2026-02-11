<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="auth.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>

<jsp:useBean id="clientSessionBean" scope="session" class="com.genesys.session.ClientSessionBean"/>
<jsp:useBean id="viewConfigBean" scope="page" class="com.genesys.views.ViewConfigBean"/>
<%
	//viewConfigBean.setDocument( clientSessionBean.getDocument() );
%>

<jsp:setProperty name="viewConfigBean" property="view" value="<%=request.getParameter(\"view\")%>"/>

<html>
	<head>
		<title>Search</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/modal/style.css"></link>
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/common.js"></script>
		<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
		<script language="JavaScript">
			function buttonCallback( item )
			{
				switch( item )
				{
					case B_SUBMIT:
						var searchStr = "";
						<viewCfg:ViewField viewName="<%=viewConfigBean.getView()%>">
                            <% if( inputSearchable.equalsIgnoreCase( "false" ) == false ){ %>
								if( document.all.<%=inputName%>.value != "" ) searchStr += "&<%=inputName%>=" + document.all.<%=inputName%>.value;
							<% } %>
						</viewCfg:ViewField>
						parent.searchStr = searchStr;
						
						// Because of timing issues on FIrefox I couldn't call refreshView directly
						// before closing the dialog
						//parent.refreshView( B_REFRESH );
						parent.setTimeout( "refreshView( B_REFRESH )", 10 );

					case B_CANCEL:
						//window.close();
						parent.closeDialog();
						break;
				}
			}
		</script>
	</head>
	<% int row_counter = 0; %>
	<body class="view_obj_body" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
		<table height="100%" width="100%">
		<tr>
			<td valign="top" align="center">
				<table cellpadding="0" cellspacing="0" align="center">
				
				<tr>
					<td align="center">

						<hr/>
						<em>
						<b>HINTS:</b>
						Use * as wild cards and &gt;, &lt;, = for mathematical comparisons,
						dates and times must be in 'short' format (i.e. 5/24/2004 1:30 pm).</em>
						<hr/>

					</td>
				</tr>
				
				<tr>
					<td align="center" style="padding-bottom:6px;">
						<span ID="submitButtonTop"></span>
						<script language="JavaScript">
						submitButtonTop.innerHTML = buttonDraw( B_SUBMIT, "", "Submit", 'buttonCallback' );
						</script>
						&nbsp;
						<span ID="cancelButtonTop"></span>
						<script language="JavaScript">
						cancelButtonTop.innerHTML = buttonDraw( B_CANCEL, "", "Cancel", 'buttonCallback' );
						</script>
					</td>
				</tr>
			
				<tr>
					<td align="center">
			
						<div class="form_fields" style="padding: 15px;">
						<viewCfg:ViewForm viewName="<%=viewConfigBean.getView()%>">
						<!--viewCfg:ViewField viewName="<%//=viewConfigBean.getView()%>"-->
						<% //if( inputSearchable.equalsIgnoreCase( "false" ) == false ){ %>

							<div id="DIV_<%=inputField%>" style="width:50%;float:left;text-align:left;">

							<label for="<%=inputField%>" class="property_text" style="overflow-x: hidden; white-space: nowrap; display: inline-block; width: 100px;">
							<%=inputText%>
							</label>

							<input type="text" ID="<%=inputField%>" NAME="<%=inputField%>"/>
							</div>
							
						<% //} %>
						<!--/viewCfg:ViewField-->
						</viewCfg:ViewForm>
						</div>
			
					</td>
				</tr>
				
				<tr>
					<td align="center" style="padding-top:6px;padding-bottom:6px;">
						<span ID="submitButtonBottom"></span>
						<script language="JavaScript">
						submitButtonBottom.innerHTML = buttonDraw( B_SUBMIT, "", "Submit", 'buttonCallback' );
						</script>
						&nbsp;
						<span ID="cancelButtonBottom"></span>
						<script language="JavaScript">
						cancelButtonBottom.innerHTML = buttonDraw( B_CANCEL, "", "Cancel", 'buttonCallback' );
						</script>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		</table>
	</body>
</html>
