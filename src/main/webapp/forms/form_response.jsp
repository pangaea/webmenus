<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->

<%@ include file="../ui/auth.jsp" %>
<%@ page import="java.lang.Integer"%>
<%@ page import="java.lang.Math"%>
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

	// $ID
	String ID = "";
	if( request.getParameter("ID") != null )
	{
		ID = request.getParameter("ID");
	}

	// $DS_USER_GUID
	String DS_USER_GUID = (String)request.getSession().getAttribute( "guid" );
	String admin = (String)request.getSession().getAttribute( "admin" );

	// $MYNAME
	String PARENT_ID = request.getParameter( "PARENT_ID" );
	String ISSID = request.getParameter("ISSID");

	// User output
	String UserMessage = "";
	String BackPage = "";
	int ResponseError = 0;
	int Grade = 0;
	int ResponseID = 0;
	int access = 1;
	int COUNT = 1;
	int concept = 0;
	if( request.getParameter( "COUNT" ) != null )
	{
		COUNT = Integer.parseInt( request.getParameter( "COUNT" ) );
	}

	int result = 1;
	for( int i = 1; i <= COUNT; i++ )
	{
		String i_str = new Integer(i).toString();
		String name = "RESPONSE" + i_str;
		if( request.getParameter(name) == null )
		{
			result = 0;
			break;
		}
	}

	if( result != 0 )
	{
		String date_time = "";//date( "Y-m-d H:i:s" );

		// Add new response record
		String accountnum = "";
		ResponseID = formsapi.AddUsersResponse( DS_USER_GUID, PARENT_ID, accountnum, ID, ISSID, date_time, 0 );
		//ResponseID = db_get_indentity();

		// Entering the questions
		int correct_answers = 0;
		for( int i = 1; i <= COUNT; i++ )
		{
			// Building and executing insert query
			String i_str = new Integer(i).toString();
			String name = "RESPONSE" + i_str;
			String QuestNum;
			String Value;
			String ResponseVal = request.getParameter(name);
			int delim = ResponseVal.indexOf( ":" );
			if( delim >= 0 )
			{
					QuestNum = ResponseVal.substring( 0, delim );
					Value = ResponseVal.substring( delim+1 );
			}
			else
			{
					QuestNum = new Integer(i).toString();
					Value = ResponseVal;
			}
			formsapi.AddUsersQuestionResponse( ResponseID, i, QuestNum, Value );
		}

		// Calculate grade
		//String OBJID = "0";
		StringRef sb_userid = new StringRef();
		StringRef sb_full_name = new StringRef();
		StringRef sb_accnum = new StringRef();
		StringRef sb_OBJID = new StringRef("0");
		StringRef sb_response_time = new StringRef();
		StringRef sb_res_type = new StringRef();
		formsapi.GetUsersResponse( ResponseID, sb_userid, sb_full_name, sb_accnum, sb_OBJID, sb_response_time, sb_res_type );
		String OBJID = sb_OBJID.getStr();

		StringRef sb_concept = new StringRef();
		StringRef sb_title = new StringRef();
		StringRef sb_form_type = new StringRef();
		StringRef sb_questcount = new StringRef();
		StringRef sb_parent = new StringRef();
		formsapi.GetFormData( OBJID, sb_concept, sb_title, sb_form_type, sb_questcount, sb_parent );
		concept = sb_concept.getInt();
		String title = sb_title.getStr();
		String _enum = sb_form_type.getStr();
		int form_type = 0;
		// ENUM_TRANSLATION: begin //
		if( _enum.equalsIgnoreCase( "disabled" ) == true ) form_type = 0;
		else if( _enum.equalsIgnoreCase( "standard" ) == true ) form_type = 1;
		//else if( enum.equalsIgnoreCase( "wizard(random)" ) == true ) form_type = 2;
		else if( _enum.equalsIgnoreCase( "incremental" ) == true ) form_type = 3;
		else form_type = 0;		// disable by default
		// ENUM_TRANSLATION: end //
		//int form_type = sb_form_type.getInt();
		int questcount = sb_questcount.getInt();

		DBResultSet dbResults;
		dbResults = formsapi.GetUsersQuestionResponses( ResponseID );
		int count = 0;
		//int correct_answers = 0;
		boolean bEndOfList = true;
		do
		{
			int numCorrect = 0;
			StringRef sb_questnum = new StringRef();
			StringRef sb_selection = new StringRef();
			bEndOfList = formsapi.GetUsersQuestionResponseFromList( dbResults, sb_questnum, sb_selection );
			if( bEndOfList == false )
			{
				int questnum = sb_questnum.getInt();
				int selection = sb_selection.getInt();
	
				StringRef sb_questid = new StringRef();
				StringRef sb_type = new StringRef();
				StringRef sb_category = new StringRef();
				StringRef sb_quest_diff = new StringRef();
				StringRef sb_question = new StringRef();
				formsapi.GetQuestionData( OBJID, questnum, sb_questid, sb_type, sb_category, sb_quest_diff, sb_question );
				String questid = sb_questid.getStr();
				int type = 0;
				_enum = sb_type.getStr();
				// ENUM_TRANSLATION: begin //
				if( _enum.equalsIgnoreCase( "multiple choice" ) == true ) type = 0;
				else if( _enum.equalsIgnoreCase( "fill-in" ) == true ) type = 1;
				else type = 0;		// multiple choice by default
				// ENUM_TRANSLATION: end //
				//int type = sb_type.getInt();
				if( type == 0 )
				{
					DBResultSet dbInputs;
					dbInputs = formsapi.GetInputList( questid );
					boolean bEndOfList2 = true;
					int i = 1;
					do
					{
						StringRef sb_number = new StringRef();
						StringRef sb_display = new StringRef();
						StringRef sb_input_type = new StringRef();
						StringRef sb_body = new StringRef();
						bEndOfList2 = formsapi.GetInputDataFromList( dbInputs, sb_number, sb_display, sb_input_type, sb_body );
						if( bEndOfList2 == false )
						{
							int input_type = 0;
							_enum = sb_input_type.getStr();
							// ENUM_TRANSLATION: begin //
							if( _enum.equalsIgnoreCase( "selection" ) == true ) input_type = 0;
							else if( _enum.equalsIgnoreCase( "correct selection" ) == true ) input_type = 1;
							else input_type = 0;		// selection by default
							// ENUM_TRANSLATION: end //
							//int input_type = sb_input_type.getInt();
							if( selection == i && input_type == 1 )
							{
								correct_answers++;
							}
						}
						i++;
					}
					while( bEndOfList2 == false );
					dbInputs.close();
				}
				count++;
			}
			//count++;
			//if( numCorrect > 0 ) correct_answers++;
		}
		while( bEndOfList == false );
		dbResults.close();

		double Perc;
		if( 0 < count ) Perc = (double)correct_answers / (double)count;
		else Perc = 0;
		Grade = (int)Math.round( Perc * 100 );
		formsapi.SetUsersResponseGrade( ResponseID, Grade );

		if( access == 0 )
		{
			// Reset user permissions
			StringRef sb_auth_count = new StringRef();
			formsapi.GetUsersPermissions( DS_USER_GUID, ID, sb_auth_count );
			int auth_count = sb_auth_count.getInt();
			if( auth_count <= 0 )
			{
				auth_count = 0;
			}
			else
			{
				auth_count--;
			}
			formsapi.SetUsersPermissions( DS_USER_GUID, ID, auth_count );
		}
		//}
		//////////////////////////////////////////////

		UserMessage = "Thank you...";
		BackPage = "javascript:window.close();";
	}
	else
	{
		UserMessage = "Incomplete Submission: Please answer all questions.";
		BackPage = "javascript:history.back();";
		ResponseError = 1;
	}
%>

<HTML>
<HEAD>
<TITLE>Test Response</TITLE>
</HEAD>
<BODY bgcolor="white">
<CENTER>
<FONT color="#000000" size="3" face="Arial">
<strong><%=UserMessage%></strong>
</FONT>
<!--br>concept=<%=concept%><br-->
<%
	if( concept == 1 && ResponseError == 0 )
	{
%>
		<br><a href="survey_results.jsp?ISSID=<%=ISSID%>">See Results</a><br>
<%
	}
	else if( ( concept == 2 && admin.equalsIgnoreCase( "Y" ) == true ) && ResponseError == 0 )
	{
		Integer iResponseID = new Integer(ResponseID);
%>
		<br><a href="exam_results.jsp?RESID=<%=iResponseID.toString()%>">See Results</a><br>
<%
	}
	if( ResponseError > 0 )
	{
%>
		<br><br>
		<center><br><br><a href="<%=BackPage%>"><img border="0" src="<%=request.getContextPath()%>/images/buttons/goback.gif"></a></center>
<%	}
	else
	{
		String RETURN_ID = request.getParameter("RETURN_ID");
		if( RETURN_ID != null && RETURN_ID.length() > 0 )
		{
			Integer iResponseID = new Integer(ResponseID);
%>
		<br><br>
		<table height="100%" width="100%" align="center" valign="middle"><tr><td>
        <center>
            <font face="arial" size="2">Continue...</font>
            <FORM  method="post" id="formForm" action="form.jsp">
                <input type="hidden" value="<%=request.getParameter("RETURN_ID")%>" name="ID">
				<input type="hidden" value="<%=request.getParameter("RETURN_ISSID")%>" name="ISSID">
                <input type="hidden" value="" name="RETURN_ID">
                <input type="hidden" value="" name="RETURN_ISSID">
				<input type="hidden" value="<%=iResponseID.toString()%>" name="PARENT_ID">
				<input type="hidden" value="Y" name="NOQUEST">
                <INPUT type="image" src="<%=request.getContextPath()%>/images/buttons/begin.gif" name="submit" alt="Submit" border="0">
            </FORM>
        </center>
		</td></tr></table>
<%
		}
		else
		{
%>
		<br><br>
		<center><br><br><a href="<%=BackPage%>"><img border="0" src="<%=request.getContextPath()%>/images/buttons/close.gif"></a></center>
<%
		}
	}
%>

</CENTER>
</HTML>