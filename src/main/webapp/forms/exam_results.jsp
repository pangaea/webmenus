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

	String sRESID = (String)request.getParameter("RESID");
	if( sRESID == null )
	{
%>
		<HTML>
			<HEAD>
				<TITLE>Exam Results</TITLE>
			</HEAD>
			<BODY bgcolor=#ffffff>
				<center>
					<font color=#ff0000 size=4 face=Arial>Error: Please select exam result</font>
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
	int RESID = Integer.parseInt( sRESID );

	// Display exam results
	/////////////////////////////////////////////////
	StringRef sb_userid = new StringRef();
	StringRef sb_questionnaire = new StringRef();
	StringRef sb_accnum = new StringRef();
	StringRef sb_ID = new StringRef("0");
	StringRef sb_response_time = new StringRef();
	StringRef sb_res_type = new StringRef();
	formsapi.GetUsersResponse( RESID, sb_userid, sb_questionnaire, sb_accnum, sb_ID, sb_response_time, sb_res_type );
	String questionnaire = sb_questionnaire.getStr();
	String ID = sb_ID.getStr();

	StringRef sb_concept = new StringRef();
	StringRef sb_title = new StringRef();
	StringRef sb_form_type = new StringRef();
	StringRef sb_questcount = new StringRef();
	StringRef sb_parent = new StringRef();
	formsapi.GetFormData( ID, sb_concept, sb_title, sb_form_type, sb_questcount, sb_parent );
	String title = sb_title.getStr();
	int questcount = sb_questcount.getInt();
%>

<HTML>
<HEAD>
	<TITLE>Exam Results</TITLE>
	<style>
	td.user_selection
	{
		font: 14pt Arial;
		font-style: italic;
	}
	td.question_body
	{
		font: 12pt Verdana;
		font-weight: bold;
	}
	td.question_selection
	{
		font: 12pt Verdana;
	}
	</style>
</HEAD>
<BODY bgcolor="white">
	<CENTER>
		<p align=center><font color="#000000" size="5" face="New Times Roman">
			<%=title%>
		</font></p>
		<!--table border=0>
			<tr><td bgcolor=#c8fbfb>
				Blue...
			</td><td>
				Correct Answer
			</td></tr>
			<tr><td><font color="#000000" size="4" face="Arial">
				Bold...
			</font></td><td>
				Selected Answer
			</td></tr>
		</table-->
		<hr>
 		<p align="center">
 				<table><tr><td><font color="red" size="6" face="Comic Sans MS"><div id="mygrade"></div></font></td>
				<td><font color="red" size="6" face="Comic Sans MS">%</font></td></tr></table>
		</p>
		<table>
<%
	DBResultSet dbResults, dbResults2;
	dbResults = formsapi.GetUsersQuestionResponses( RESID );
	int count = 0;
	int correct_answers = 0;
	boolean bEndOfList = true;
	do
	{
%>
		<tr>
<%
		int numCorrect = 0;
		StringRef sb_questnum = new StringRef();
		StringRef sb_selection = new StringRef();
		bEndOfList = formsapi.GetUsersQuestionResponseFromList( dbResults, sb_questnum, sb_selection );
		if( bEndOfList == false )
		{
			int questnum = sb_questnum.getInt();
			//int selection = sb_selection.getInt();

			StringRef sb_questid = new StringRef();
			StringRef sb_type = new StringRef();
			StringRef sb_category = new StringRef();
			StringRef sb_quest_diff = new StringRef();
			StringRef sb_question = new StringRef();
			formsapi.GetQuestionData( ID, questnum, sb_questid, sb_type, sb_category, sb_quest_diff, sb_question );
			String questid = sb_questid.getStr();
			int type = 0;
			String enum = sb_type.getStr();
			// ENUM_TRANSLATION: begin //
			if( enum.equalsIgnoreCase( "multiple choice" ) == true ) type = 0;
			else if( enum.equalsIgnoreCase( "fill-in" ) == true ) type = 1;
			else type = 0;		// multiple choice by default
			// ENUM_TRANSLATION: end //
			//int type = sb_type.getInt();
			int category = sb_category.getInt();
			String quest_diff = sb_quest_diff.getStr();
			String question = sb_question.getStr();
%>
			<!--p align=center><font color=#000000 size=1 face=Arial-->
<%
			//StringRef sb_description = new StringRef();
			//StringRef sb_difficulty = new StringRef();
			//formsapi.GetFormCategory( ID, category, sb_description, sb_difficulty );
			//String description = sb_description.getStr();
 			//if( description != "" )
			//{
%>
 				<!--category: <-%=description%-->
<%
 			//}
 			//if( quest_diff != null && quest_diff.equalsIgnoreCase( "none" ) == false )
			//{
%>
 				<!--br>difficulty : <-%=quest_diff%-->
<%
 			//}
%>
			<!--/font><br-->
<%
			if( type == 0 ){
				int selection = sb_selection.getInt();
%>
			<td colspan="2">
	 			<table cols="2">
				<tr>
					<td class="question_body" valign="top" align="right" width="6"><div id="penmark_<%=sb_questnum.getStr()%>"></div>
 						<%=sb_questnum.getStr()%>)&nbsp;</td>
					<td valign="top">
 	 	 				<table border="0" align="center">
						<tr>
							<td class="question_body">
								<div DATAFORMATAS="HTML"><%=question%></div>
							</td>
						</tr>
						<tr>
							<td>
								<table>
<%
				dbResults2 = formsapi.GetInputList( questid );
				boolean bEndOfList2 = true;
				int i = 1;
				do
				{
					StringRef sb_number = new StringRef();
					StringRef sb_display = new StringRef();
					StringRef sb_input_type = new StringRef();
					StringRef sb_body = new StringRef();
					bEndOfList2 = formsapi.GetInputDataFromList( dbResults2, sb_number, sb_display, sb_input_type, sb_body );
					if( bEndOfList2 == false )
					{
						String display = sb_display.getStr();
						int correctinput = 0;
						enum = sb_input_type.getStr();
						// ENUM_TRANSLATION: begin //
						if( enum.equalsIgnoreCase( "selection" ) == true ) correctinput = 0;
						else if( enum.equalsIgnoreCase( "correct selection" ) == true ) correctinput = 1;
						else correctinput = 0;		// selection by default
						// ENUM_TRANSLATION: end //
						//int correctinput = sb_input_type.getInt();
						String body = sb_body.getStr();
						//if( correctinput == 1 ){
%>
								<!--tr>
									<td bgcolor="#c8fbfb"><%=display%>) </td>
									<td-->
<%
						//}else{
%>
								<tr>
									<td><%=display%>) </td>
<%
						//}
						if( selection == i ){
%>
									<!--font color="#000000" size="4" face="Arial"-->
									<td class="user_selection">
<%
							if( correctinput == 1 ){
%>
								<script language="JavaScript">
									document.all.penmark_<%=sb_questnum.getStr()%>.innerHTML = "<img align='left' src='<%=request.getContextPath()%>/images/ok.gif'/>";
								</script>
<%
								correct_answers++;
							}
							else
							{
%>
								<script language="JavaScript">
									document.all.penmark_<%=sb_questnum.getStr()%>.innerHTML = "<img align='left' src='<%=request.getContextPath()%>/images/wrong.gif'/>";
								</script>
<%
							}
						}
						else
						{
%>
									<td class="question_selection">
<%
						}
%>
										<div DATAFORMATAS="HTML"><%=body%></div>
									</font>
									</td>
								</tr>
<%
					}
					i++;
				}
				while( bEndOfList2 == false );
				dbResults2.close();
%>
								</table>
							</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
<%
			}else if( type == 1 ){
%>
			<td>
	 			<table cols="2">
				<tr>
					<td class="question_body" valign="top" align="right" width="6"><div id="penmark_<%=sb_questnum.getStr()%>"></div>
 						<%=sb_questnum.getStr()%>)&nbsp;</td>
					<td valign="top">
 	 	 				<table border="0" align="center">
						<tr>
							<td class="question_body">
								<div DATAFORMATAS="HTML"><%=question%></div>
							</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
			<td class="user_selection"><%=sb_selection.getStr()%>
			</td>
<%
				dbResults2 = formsapi.GetInputList( questid );
				boolean bEndOfList2 = true;
				int i = 1;
				boolean bCorrect = false;
				do
				{
					StringRef sb_number = new StringRef();
					StringRef sb_display = new StringRef();
					StringRef sb_input_type = new StringRef();
					StringRef sb_body = new StringRef();
					bEndOfList2 = formsapi.GetInputDataFromList( dbResults2, sb_number, sb_display, sb_input_type, sb_body );
					if( bEndOfList2 == false )
					{
						String body = sb_body.getStr();
						String display = sb_display.getStr();
						enum = sb_input_type.getStr();
						int correctinput = 0;
						// ENUM_TRANSLATION: begin //
						if( enum.equalsIgnoreCase( "selection" ) == true ) correctinput = 0;
						else if( enum.equalsIgnoreCase( "correct selection" ) == true ) correctinput = 1;
						else correctinput = 0;		// selection by default
						// ENUM_TRANSLATION: end //
						if( correctinput == 1 && body.equalsIgnoreCase( sb_selection.getStr() )){
							bCorrect = true;
							break;
						}
					}
				}
				while( bEndOfList2 == false );
				dbResults2.close();

				if( bCorrect == true ){
%>
			<script language="JavaScript">
				document.all.penmark_<%=sb_questnum.getStr()%>.innerHTML = "<img align='left' src='<%=request.getContextPath()%>/images/ok.gif'/>";
			</script>
<%
					correct_answers++;
				}else{
%>
			<script language="JavaScript">
				document.all.penmark_<%=sb_questnum.getStr()%>.innerHTML = "<img align='left' src='<%=request.getContextPath()%>/images/wrong.gif'/>";
			</script>
<%
				}
			}
%>
		</tr>
<%
			count++;
		}
	}
	while( bEndOfList == false );
	
	dbResults.close();
%>

</table>

<%
	double Perc;
 	if( count > 0 ) Perc = (double)correct_answers / (double)count;
 	else Perc = 0;
 	int Grade = (int)Math.round( Perc * 100 );
%>
	<script language="JavaScript">
		document.getElementById("mygrade").innerHTML="<%=Grade%>";
	</script>
	<center><br><br><a href="javascript:window.close();"><img border="0" src="<%=request.getContextPath()%>/images/buttons/close.gif"/></a></center>

</p>
</CENTER>
</FORM>
</BODY>
</HTML>
