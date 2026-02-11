<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="../ui/auth.jsp" %>
<%@ page import="java.lang.Integer"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.genesys.util.StringRef"%>
<%@ page import="com.genesys.db.DBResultSet"%>
<%@ page import="com.genesys.SystemServlet"%>
<jsp:useBean id="formsapi" scope="page" class="com.genesys.forms.FormsDataBean"/>
<%
    // Set root path for instance
	//ServletContext thisContext = getServletConfig().getServletContext();
	//String webappPath = thisContext.getRealPath( "/" );
	//String rootAppPath = webappPath.substring( 0, webappPath.length() - 7 );
	formsapi.init( SystemServlet.getGenesysHome() );

	String sISSID = (String)request.getParameter("ISSID");
	if( sISSID == null )
	{
%>
		<HTML>
			<HEAD>
				<TITLE>Exam Summary</TITLE>
			</HEAD>
			<BODY bgcolor=#ffffff>
				<center>
					<font color=#ff0000 size=4 face=Arial>Error: Please select a survey issuance</font>
				</center>
				<center>
					<br><br>
					<a href=javascript:window.close();><img border=0 src=images/close.gif></a>
				</center>
			</BODY>
		</HTML>
<%
  		return;
	}

	StringRef sb_workgroupid = new StringRef();
	StringRef sb_formid = new StringRef();
	StringRef sb_active = new StringRef();
	StringRef sb_notes = new StringRef();
	formsapi.GetFormIssuance( sISSID, sb_workgroupid, sb_formid, sb_active, sb_notes );
	String ID = sb_formid.getStr();

	StringRef sb_concept = new StringRef();
	StringRef sb_title = new StringRef();
	StringRef sb_form_type = new StringRef();
	StringRef sb_questcount = new StringRef();
	StringRef sb_parent = new StringRef();
	formsapi.GetFormData( ID, sb_concept, sb_title, sb_form_type, sb_questcount, sb_parent );
	String title = sb_title.getStr();
	int questcount;// = sb_questcount.getInt();
	formsapi.GetFormNumQuestions( ID, sb_questcount );
	questcount = sb_questcount.getInt();//questcount;
%>

<HTML>
<HEAD>
	<TITLE>Survey Results - <%=title%></TITLE>
	<style>
			div.perc_line
			{
				font-family: Verdana;
				font-size: 12pt;
				background-image: url(<%=request.getContextPath()%>/images/top.gif);
				background-color: blue;
				color: white;
				font-family: Verdana;
				font-size: 12pt;
				height: 100%;
			}
	</style>
</HEAD>
<BODY bgcolor="white">
	<CENTER>
		<p align="center"><font color="#000000" size="5" face="New Times Roman">
			<%=title%>
		</font></p>
		<strong>Average Grade: <%=formsapi.GetUsersAverageGrade(sISSID)%>%</strong>
		<div style="font:7pt Verdana;"><img src="<%=request.getContextPath()%>/images/correct.png"> = Correct Answer</div>
		<hr>
<%
	int correct_answers = 0;
	boolean bEndOfList = true;
	int input_number = 1;
	while( input_number <= questcount )
	{
		StringRef sb_questid = new StringRef();
		StringRef sb_type = new StringRef();
		StringRef sb_category = new StringRef();
		StringRef sb_quest_diff = new StringRef();
		StringRef sb_question = new StringRef();
		formsapi.GetQuestionData( ID, input_number, sb_questid, sb_type, sb_category, sb_quest_diff, sb_question );
		String questid = sb_questid.getStr();
		int type = 0;
		String enum = sb_type.getStr();
		
		// ENUM_TRANSLATION: begin //
		if( enum.equalsIgnoreCase( "multiple choice" ) == true ) type = 0;
		else if( enum.equalsIgnoreCase( "muliple select" ) == true ) type = 2;
		else type = 0;		// multiple choice by default
		// ENUM_TRANSLATION: end //
		//int type = sb_type.getInt();
		int category = sb_category.getInt();
		String quest_diff = sb_quest_diff.getStr();
		String question = sb_question.getStr();
		if( type == 0 )
		{
			//Integer iinput_number = new Integer(input_number);
%>
 			<table cols="2"><tr><td valign="top" align="right" width="6">
 				<strong><%=input_number%>) </strong></td><td>
 	 	 		<table border="0" align="center" width="500"><font color="#000000" size="2" face="Arial">
			<tr><td>
			<div DATAFORMATAS="HTML"><strong><%=question%></strong>
			</div></td></tr><tr><td><table>
<%
			// Get total responses based on the question number
			StringRef sb_totals = new StringRef();
			formsapi.GetResponseCount( sISSID, input_number, null, sb_totals );
			int totals = sb_totals.getInt();

			DBResultSet dbResultSet = formsapi.GetInputList( questid );
			boolean bEndOfList2 = true;
			int i = 1;
			do
			{
				StringRef sb_number = new StringRef();
				StringRef sb_display = new StringRef();
				StringRef sb_input_type = new StringRef();
				StringRef sb_body = new StringRef();
				bEndOfList2 = formsapi.GetInputDataFromList( dbResultSet, sb_number, sb_display, sb_input_type, sb_body );
				if( bEndOfList2 == false )
				{
					String display = sb_display.getStr();
					int correctinput = 0;
					enum = sb_input_type.getStr();
					int input_type;
					if( enum.equalsIgnoreCase( "selection" ) == true ) input_type = 0;
					else if( enum.equalsIgnoreCase( "correct selection" ) == true ) input_type = 1;
					else input_type = 0;		// selection by default

					StringRef sb_selection = new StringRef();
					formsapi.GetResponseCount( sISSID, input_number, sb_number.getStr(), sb_selection );
					int selection = sb_selection.getInt();
					
					double Perc;
					if( 0 < totals ) Perc = (double)selection / (double)totals;
					else Perc = 0;
					int percSel = (int)Math.round( Perc * 100 );

%>
				<!--tr>
					<td width="400" colspan="3"><div width="50%" class="perc_line">50%</div></td>
				</tr-->
				<tr><td valign="top"><%if(input_type==1){%><img src="<%=request.getContextPath()%>/images/correct.png"><%}%></td><td valign="top" width="20">
				<%=display%>) </td><td width="180">
				<div DATAFORMATAS="HTML" align="left"><%=sb_body.getStr()%></div>
				</td><td valign="top" align="left"><%=percSel%>%</td><td width="300"><div style="width:<%=percSel%>%;" class="perc_line"></div></td><td valign="top" nowrap><%=selection%></td></tr>
<%
				}
				i++;
			}
			while( bEndOfList2 == false );
			dbResultSet.close();
%>
				<tr><td colspan="6" align="right">
					<strong>Total:</strong>&nbsp;<%=totals%>
				</td></tr>
				</table>
<%
		}
		else if( type == 2 )
		{
%>
  			<table border=0 align=center width=500><font color=#000000 size=2 face=Arial>
 			<tr><td valign=bottom>
	 		<strong><div DATAFORMATAS=HTML><%=input_number%>. <%=question%></div></strong></td><td>
<%
		}
%>
			</td></tr></font></table></td></tr>
			</table></p>
<%
			input_number++;
	}
%>

<p>
	<center>
		<br><br>
		<a href="javascript:window.close();">
			<img border="0" src="<%=request.getContextPath()%>/images/buttons/close.gif"/>
		</a>
	</center>
</p>
</CENTER>
</FORM>
</BODY>
</HTML>
