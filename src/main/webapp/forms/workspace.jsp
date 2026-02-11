<!--
Copyright (c) 2004-2006 Kevin Jacovelli
All Rights Reserved
-->

<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
		response.sendRedirect( "../ui/portal_login.jsp" ); return;
	}
%>
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page import="com.genesys.util.StringRef"%>
<%@ page import="com.genesys.db.DBResultSet"%>
<%@ page import="com.genesys.SystemServlet"%>
<%@ page import="java.util.Vector"%>
<jsp:useBean id="workspace" scope="page" class="com.genesys.forms.WorkspaceBean"/>
<%
    // Set root path for instance
	//ServletContext thisContext = getServletConfig().getServletContext();
	//String webappPath = thisContext.getRealPath( "/" );
	//String rootAppPath = webappPath.substring( 0, webappPath.length() - 7 );
	workspace.init( SystemServlet.getGenesysHome() );

	// Validate session
	String DS_USER_GUID = (String)request.getSession().getAttribute( "guid" );
	String workgroup_id = (String)request.getParameter( "workgrpid" );
%>

<html>
	<head>
		<title>Datasphere Workgroup</title>

		<!-- Core Stylesheet -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		
		<!-- Tab Scripts -->
		<style type="text/css">
			@import "<%=request.getContextPath()%>/styles/tabstyles.css";
		</style>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/domtab.js"></script>
		<script language="Javascript" id="main">
		function LaunchForm( id, issid )
		{
			var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
			window.open("form.jsp?ID="+id+"&ISSID=" + issid + "&num=" + rnumber + "","","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
		}
		//function LaunchForm2( id, issid )
		//{
		//	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
		//	window.open("form.jsp?ID="+id+"&ISSID=" + issid + "&num=" + rnumber + "&NOQUEST=Y&DS_USER_GUID=<%=DS_USER_GUID%>","","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
		//}
		function LaunchStaticForm( view, issid )
		{
			var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
			window.open("objectpanels.jsp?view="+view+"&ISSID=" + issid + "&num=" + rnumber + "&formView=Y","","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
		}

		//function OpenSurveyWindow( id, issid )
		//{
		//	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
		//	window.open("form.jsp?ID="+id+"&ISSID=" + issid + "&num=" + rnumber + "&DS_USER_GUID=<%=DS_USER_GUID%>","","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
		//}
		//function StartSlideShow( id )
		//{
		//	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
		//	window.open("objectpanels.jsp?view=workspace_slideshow&call=query&show="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
		//}
		</script>
	</head>

	<body width="100%">

<%
	if( workgroup_id == null || workgroup_id.length() == 0 )
	{
%>
		<center>
		<span style="font:16pt Arial; color:red;">Invalid Workgroup Specified</span>
		</center>
<%
	}
	
	StringRef sb_name = new StringRef();
	StringRef sb_description = new StringRef();
	workspace.GetWorkspace( workgroup_id, sb_name, sb_description );
%>
<%
	boolean bEndOfList;
	
	// Create SURVEY list
	Vector vSurveys = new Vector();
	DBResultSet dbResults;
	dbResults = workspace.GetFormList( DS_USER_GUID, workgroup_id, 1 );
	bEndOfList = true;
	do
	{
		StringRef sb_id = new StringRef();
		StringRef sb_title = new StringRef();
		StringRef sb_issid = new StringRef();
		StringRef sb_notes = new StringRef();
		bEndOfList = workspace.GetFormFromList( dbResults, sb_id, sb_title, sb_issid, sb_notes );
		if( bEndOfList == false )
		{
			Vector rec = new Vector();
			rec.add( sb_id.getStr() );
			rec.add( sb_issid.getStr() );
			rec.add( sb_title.getStr() );
			rec.add( sb_notes.getStr() );
			vSurveys.add( rec );
		}
	}
	while( bEndOfList == false );
	dbResults.close();
%>
<%
	// Create EXAM list
	Vector vExams = new Vector();
	dbResults = workspace.GetFormList( DS_USER_GUID, workgroup_id, 2 );
	bEndOfList = true;
	do
	{
		StringRef sb_id = new StringRef();
		StringRef sb_title = new StringRef();
		StringRef sb_issid = new StringRef();
		StringRef sb_notes = new StringRef();
		bEndOfList = workspace.GetFormFromList( dbResults, sb_id, sb_title, sb_issid, sb_notes );
		if( bEndOfList == false )
		{
			Vector rec = new Vector();
			rec.add( sb_id.getStr() );
			rec.add( sb_issid.getStr() );
			rec.add( sb_title.getStr() );
			rec.add( sb_notes.getStr() );
			vExams.add( rec );
		}
	}
	while( bEndOfList == false );
	dbResults.close();
%>
<!--%
	// Create FORM list
	Vector vForms = new Vector();
	dbResults = workspace.GetFormList( DS_USER_GUID, workgroup_id, 0 );
	bEndOfList = true;
	do
	{
		Vector rec = new Vector();
		StringRef sb_id = new StringRef();
		StringRef sb_title = new StringRef();
		StringRef sb_issid = new StringRef();
		StringRef sb_notes = new StringRef();
		bEndOfList = workspace.GetFormFromList( dbResults, sb_id, sb_title, sb_issid, sb_notes );
		if( bEndOfList == false )
		{
			rec.add( sb_id.getStr() );
			rec.add( sb_issid.getStr() );
			rec.add( sb_title.getStr() );
			rec.add( sb_notes.getStr() );
			vForms.add( rec );
		}
	}
	while( bEndOfList == false );
	dbResults.close();
*/
%-->
<%
	// Create STATIC FORM list
	Vector vSForms = new Vector();
	dbResults = workspace.GetStaticFormList( DS_USER_GUID, workgroup_id );
	bEndOfList = true;
	do
	{
		StringRef sb_view = new StringRef();
		StringRef sb_issid = new StringRef();
		StringRef sb_title = new StringRef();
		StringRef sb_notes = new StringRef();
		bEndOfList = workspace.GetStaticFormFromList( dbResults, sb_view, sb_issid, sb_title, sb_notes );
		if( bEndOfList == false )
		{
			Vector rec = new Vector();
			rec.add( sb_view.getStr() );
			rec.add( sb_issid.getStr() );
			rec.add( sb_title.getStr() );
			rec.add( sb_notes.getStr() );
			vSForms.add( rec );
		}
	}
	while( bEndOfList == false );
	dbResults.close();
	
	String portalView = (String)request.getParameter("portal");
	if( portalView != null && portalView.equalsIgnoreCase( "Y" ) == true ){
%>
	<table width="100%"><tr><td align="right"><a href="#" onclick="parent.location='<%=request.getContextPath()%>/ServerMsg?call=logout&loginPage=portal_login.jsp';">Logout</a></td></tr></table>
<%	}else{ %>
	<table width="100%"><tr><td align="right"><a href="javascript:window.close()">Close</a></td></tr></table>
<%	} %>
	<h1><%=sb_name.getStr()%><a name="top"></a></h1><br>
	<ul id="mainnav">
		<div id="tabs_DIV"></div>
		<% if( vSurveys.size() > 0 ){ %>
		<li><a id="tabSurveys" href="#surveys" class="atab">Surveys</a></li>
		<% } %>
		<% if( vExams.size() > 0 ){ %>
		<li><a id="tabExams" href="#exams" class="atab">Exams</a></li>
		<% } %>
		<!--% if( vForms.size() > 0 ){ %#>
		<li><a id="tabForms" href="#forms" class="atab">Forms</a></li>
		<#% } %-->
		<% if( vSForms.size() > 0 ){ %>
		<li><a id="tabSForms" href="#sforms" class="atab">Public Forms</a></li>
		<% } %>
	</ul>

<% if( vSurveys.size() > 0 ){ %>
					<!-- START OF SURVEYS SECTION  -->
					<!---------------------------->
					<div id="surveys" class="tabcontent">
					<table>
<%
		// Create SURVEY list
		for( int i = 0; i < vSurveys.size(); i++ )
		{
			Vector rec = (Vector)vSurveys.get(i);
%>
			<tr><td><img src="<%=request.getContextPath()%>/images/document.png"></td><td>
			<a href="javascript:LaunchForm('<%=(String)rec.get(0)%>','<%=(String)rec.get(1)%>');"><%=(String)rec.get(2)%></a><br>
			&nbsp;&nbsp;<%=(String)rec.get(3)%><br></td></tr>
<%
		}
%>
					</table>
					</div>
<% } %>
<% if( vExams.size() > 0 ){ %>
					<!-- START OF EXAMS SECTION  -->
					<!---------------------------->
					<div id="exams" class="tabcontent">
					<table>
<%
		// Create EXAM list
		for( int i = 0; i < vExams.size(); i++ )
		{
			Vector rec = (Vector)vExams.get(i);
%>
			<tr><td><img src="<%=request.getContextPath()%>/images/document.png"></td><td>
			<a href="javascript:LaunchForm('<%=(String)rec.get(0)%>','<%=(String)rec.get(1)%>');"><%=(String)rec.get(2)%></a><br>
			&nbsp;&nbsp;<%=(String)rec.get(3)%><br></td></tr>
<%
		}
%>
					</table>
					</div>
					<!---------------------------->
					<!---------------------------->
<% } %>				
<!--% if( vForms.size() > 0 ){ %-->
					<!-- START OF FORMS SECTION  -->
					<!---------------------------->
					<!--div id="forms" class="tabcontent">
					<table-->
<!--%
		// Create FORM list
		for( int i = 0; i < vForms.size(); i++ )
		{
			Vector rec = (Vector)vForms.get(i);
%#>
			<tr><td><img src="../images/document.png"></td><td>
			<a href="javascript:LaunchForm('<#%=(String)rec.get(0)%#>','<#%=(String)rec.get(1)%#>');"><#%=(String)rec.get(2)%#></a><br>
			&nbsp;&nbsp;<#%=(String)rec.get(3)%#><br></td></tr>
<#%
		}
%-->
					<!--/table>
					</div-->
					<!---------------------------->
					<!---------------------------->
<!--% } %-->
<% if( vSForms.size() > 0 ){ %>
					<!-- START OF STATIC FORMS SECTION  -->
					<!---------------------------->
					<div id="sforms" class="tabcontent">
					<table>
<%
		// Create FORM list
		for( int i = 0; i < vSForms.size(); i++ )
		{
			Vector rec = (Vector)vSForms.get(i);
%>
			<tr><td><img src="<%=request.getContextPath()%>/images/document.png"></td><td>
			<a href="javascript:LaunchStaticForm('<%=(String)rec.get(0)%>','<%=(String)rec.get(1)%>');"><%=(String)rec.get(2)%></a><br>
			&nbsp;&nbsp;<%=(String)rec.get(3)%><br></td></tr>
<%
		}
%>
					</table>
					</div>
					<!---------------------------->
					<!---------------------------->
<% } %>
	</body>
</html>