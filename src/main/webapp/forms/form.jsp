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

// $ID
String ID = "";
if( request.getParameter("ID") != null )
{
	ID = request.getParameter("ID");
}

// $RESPONSE_NUMBER
int RESPONSE_NUMBER = 1;
if( request.getParameter("RESPONSE_NUMBER") != null )
{
	RESPONSE_NUMBER = Integer.parseInt( request.getParameter("RESPONSE_NUMBER") );
}

String sISSID = request.getParameter("ISSID");
//String questionnaire_id = "";
if( RESPONSE_NUMBER == 1 )	// Only check this at the beginning
{
	StringRef sb_workgroupid = new StringRef();
	StringRef sb_formid = new StringRef();
	StringRef sb_active = new StringRef();
	StringRef sb_notes = new StringRef();
	formsapi.GetFormIssuance( sISSID, sb_workgroupid, sb_formid, sb_active, sb_notes );
	//questionnaire_id = sb_questionnaireid.getStr();
}

//int access = 1;

String form_title = "";
int form_type = 0;
int questcount = 0;
String parent_form_id = "";
try
{
	StringRef sb_concept = new StringRef();
	StringRef sb_form_title = new StringRef();
	StringRef sb_form_type = new StringRef();
	StringRef sb_questcount = new StringRef();
	StringRef sb_parent = new StringRef();
	formsapi.GetFormData( ID, sb_concept, sb_form_title, sb_form_type, sb_questcount, sb_parent );
	form_title = sb_form_title.getStr();
	String _enum = sb_form_type.getStr();
	// ENUM_TRANSLATION: begin //
	if( _enum.equalsIgnoreCase( "disabled" ) == true ) form_type = 0;
	else if( _enum.equalsIgnoreCase( "standard" ) == true ) form_type = 1;
	//else if( enum.equalsIgnoreCase( "wizard(random)" ) == true ) form_type = 2;
	else if( _enum.equalsIgnoreCase( "incremental" ) == true ) form_type = 3;
	else form_type = 0;		// disable by default
	// ENUM_TRANSLATION: end //
	//form_type = sb_form_type.getInt();
	questcount = sb_questcount.getInt();
	//parent_form_id = sb_parent.getStr();
}
catch(Exception e)
{
	System.err.println( "Exception thrown @ line 54" );
}

if( form_type == 2 || form_type == 3 )
{
	for( int i = 1; i < RESPONSE_NUMBER; i++ )
	{
		String i_str = new Integer(i).toString();
		String name = "RESPONSE" + i_str;
		if( request.getParameter(name) == null )
		{
%>

<HTML>
    <HEAD>
        <TITLE>Exam</TITLE>
    </HEAD>
    <BODY bgcolor="white">
        <CENTER>
            <p align=center>
            <FONT color="#ff0000" size="3" face="Arial">
            <strong>Invalid Response, Please Answer Question.</strong><br><br>
            <a href="javascript:history.back();">Try Again</a></font>
            </p>
        </CENTER>
    </BODY>
</HTML>

<%
			return;
		}
	}
}

StringRef sb_question_count = new StringRef();
formsapi.GetFormNumQuestions( ID, sb_question_count );

int count = sb_question_count.getInt();//questcount;

////
////////////////////////////////////////

%>

<HTML>
    <HEAD>
        <TITLE>Exam</TITLE>
		<!-- Core Stylesheet -->
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		
		<!-- calendar stylesheet -->
		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/jscalendar/calendar-win2k-cold-1.css" title="win2k-cold-1" />

		<!-- main calendar program -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/jscalendar/calendar.js"></script>
		
		<!-- language for the calendar -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/jscalendar/lang/calendar-en.js"></script>
		
		<!-- the following script defines the Calendar.setup helper function, which makes
			adding a calendar a matter of 1 or 2 lines of code. -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/jscalendar/calendar-setup.js"></script>
		
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
		<style>
		table.exam_panel
		{/*
			border-top: 8px;
			border-style: solid;
			border-top-color: gray;
			border-left-color: gray;
			border-bottom-color: white;
			border-right-color: white;
			width: 100%;*/
		}
		</style>
    </HEAD>
	<BODY bgcolor="white" onload="createCalc();">

<%

////////////////////////////////////////////////////////////////////////////
if( ( form_type == 2 || form_type == 3 ) && RESPONSE_NUMBER < count )
{
	String responseNum = new Integer(RESPONSE_NUMBER+1).toString();
%>
	<FORM method="post" id="formForm" action="form.jsp">
	<input type="hidden" value="<%=responseNum%>" name="RESPONSE_NUMBER">
<%
}
else
{
%>
	<FORM method="post" id="formForm" action="form_response.jsp">
	<input type="hidden" value="<%=count%>" name="COUNT">
<%
}
//////////////////////////////////////////////////////////////////////

String [] ans_quest_array = new String[RESPONSE_NUMBER];
int ans_quest_index = 0;
for( int i = 1; i < RESPONSE_NUMBER; i++ )
{
	String name = "RESPONSE" + new Integer(i).toString();
%>
		<input type="hidden" value="<%=request.getParameter(name)%>" name="<%=name%>">
<%
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
	ans_quest_array[ans_quest_index++] = QuestNum;
}

// Determine next question number to use /////
int QNUMBER = 0;

if( form_type == 2 )
{
	// Random question generation
	int num_questions = 0;
	try
	{
		StringRef sb_num_questions = new StringRef();
		formsapi.GetFormNumQuestions( ID, sb_num_questions );
		num_questions = sb_num_questions.getInt();

		int ok;
		do
		{	ok = 1;
			Random r = new Random();
			QNUMBER = r.nextInt(num_questions)+1;
			for( int i = 0; i < ans_quest_index; i++ )
			{
				String str = ans_quest_array[i];
				if( QNUMBER == Integer.parseInt( str ) )
				{
					ok = 0;
					break;
				}
			}
		}
		while( ok == 0 );
	}
	catch(Exception e)
	{
		System.err.println( "Exception thrown" );
	}
}
else if( form_type == 3 )
{
	// Static Test - just add one
	QNUMBER = RESPONSE_NUMBER;
}
//////////////////////////////////////////////
%>

	<input type="hidden" value="<%=request.getParameter("ID")%>" name="ID">
	<input type="hidden" value="<%=sISSID%>" name="ISSID">
	<!--input type="hidden" value="<-%=request.getParameter("MYNAME")%->" name="MYNAME"-->
	<input type="hidden" value="<%=formsapi.StringOut(request.getParameter("RETURN_ID"))%>" name="RETURN_ID">
	<input type="hidden" value="<%=formsapi.StringOut(request.getParameter("RETURN_ISSID"))%>" name="RETURN_ISSID">
	<input type="hidden" value="<%=formsapi.StringOut(request.getParameter("DS_USER_GUID"))%>" name="DS_USER_GUID">
	
	<CENTER>

	<p align="center">
		<font color="#000000" size="5" face="New Times Roman">

<%=form_title%>

<%
int input_number = 1;
if( form_type == 2 || form_type == 3 )
{
	input_number = RESPONSE_NUMBER;
	if( RESPONSE_NUMBER < count )
	{
%>
		<br><font size=2 face=Arial>( Question <%=RESPONSE_NUMBER%> of <%=count%> )</font>
<%
	}
	else
	{
		if( count > 1 ){
%>
		<br><font color="#ff0000" size="2" face="Arial">( Last Question )</font>
<%
		}
	}
}
%>

		</font>
	</p>

<%

//if( form_type == 1 )
{
%>
	<table class="exam_panel">
	<tr>
<%
	while( input_number <= count )
	{
		String questid = "";
		int type = 0;
		int category = 0;
		//int quest_diff = 0;
		String question = "";
		try
		{
			StringRef sb_questid = new StringRef();
			StringRef sb_type = new StringRef();
			StringRef sb_category = new StringRef();
			StringRef sb_quest_diff = new StringRef();
			StringRef sb_question = new StringRef();
			formsapi.GetQuestionData( ID, input_number, sb_questid, sb_type, sb_category, sb_quest_diff, sb_question );
			questid = sb_questid.getStr();
			String _enum = sb_type.getStr();
			// ENUM_TRANSLATION: begin //
			if( _enum.equalsIgnoreCase( "multiple choice" ) == true ) type = 0;
			else if( _enum.equalsIgnoreCase( "fill-in" ) == true ) type = 1;
			else if( _enum.equalsIgnoreCase( "fill-in (number)" ) == true ) type = 2;
			else if( _enum.equalsIgnoreCase( "fill-in (date)" ) == true ) type = 3;
			else if( _enum.equalsIgnoreCase( "fill-in (paragraph)" ) == true ) type = 4;
			else if( _enum.equalsIgnoreCase( "yes/no" ) == true ) type = 5;
			else type = 1;		// fill-in by default
			// ENUM_TRANSLATION: end //
			//type = sb_type.getInt();
			category = sb_category.getInt();
			//quest_diff = sb_quest_diff.getInt();
			question = sb_question.getStr();
		}
		catch(Exception e)
		{
			System.err.println( "Exception thrown" );
		}
		if( category != 0 )
		{
			String description = "";
			int difficulty = 0;
			try
			{
				StringRef sb_description = new StringRef();
				StringRef sb_difficulty = new StringRef();
				formsapi.GetFormCategory( ID, category, sb_description, sb_difficulty );
				description = sb_description.getStr();
				String _enum = sb_difficulty.getStr();
				// ENUM_TRANSLATION: begin //
				if( _enum.equalsIgnoreCase( "none" ) == true ) difficulty = 0;
				else if( _enum.equalsIgnoreCase( "easy" ) == true ) difficulty = 1;
				else if( _enum.equalsIgnoreCase( "normal" ) == true ) difficulty = 2;
				else if( _enum.equalsIgnoreCase( "hard" ) == true ) difficulty = 3;
				else difficulty = 0;		// none by default
				// ENUM_TRANSLATION: end //
				//difficulty = sb_difficulty.getInt();
			}
			catch(Exception e)
			{
				System.err.println( "Exception thrown" );
			}
%>
			category : <%=description%>
<%
		}
		StringRef sb_extendRow = new StringRef();
		formsapi.GetQuestionParam( "", questid, "extend-row", sb_extendRow );
		if( sb_extendRow.getStr().equalsIgnoreCase( "on" ) == false )
		{
%>
	</tr><tr>
<%
		}
		if( type == 0 )
		{
			//////////////////////////////////////////////////////////////////////////
			// MULTIPLE-CHOICE QUESTION /////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////
%>
			<td  colspan="2">
				<table cols="2">
				<tr>
					<td valign="top" align="right" width="6">
						<strong><%=input_number%>) </strong>
					</td>
					<td>
						<table border="0" align="center">
						<tr>
							<td>
								<font color="#000000" size="2" face="Arial">
                      			<strong><div DATAFORMATAS="HTML"><%=question%></div></strong>
								</font>
                      		</td>
						</tr>
						<tr>
							<td>
<%
			//formsapi.GetInputList( questid );
			DBResultSet dbResultSet = formsapi.GetInputList( questid );
%>
							<font color="#000000" size="1" face="Arial">
							<table>
<%
			int i = 1;
			boolean bEndOfList = true;
			int responseNum = RESPONSE_NUMBER;
			do
			{
				String display = "";
				int input_type = 0;
				String body = "";
				try
				{
					StringRef sb_number = new StringRef();
					StringRef sb_display = new StringRef();
					StringRef sb_input_type = new StringRef();
					StringRef sb_body = new StringRef();
					bEndOfList = formsapi.GetInputDataFromList( dbResultSet, sb_number, sb_display, sb_input_type, sb_body );
					if( bEndOfList == false )
					{
						display = sb_display.getStr();
						String _enum = sb_input_type.getStr();
						// ENUM_TRANSLATION: begin //
						if( _enum.equalsIgnoreCase( "selection" ) == true ) input_type = 0;
						else if( _enum.equalsIgnoreCase( "correct selection" ) == true ) input_type = 1;
						else input_type = 0;		// selection by default
						// ENUM_TRANSLATION: end //
						//input_type = sb_input_type.getInt();
						body = sb_body.getStr();
			
						Integer iinput_number = new Integer(input_number);
						Integer iRESPONSE_NUMBER = new Integer(input_number);
						String i_str = new Integer(i).toString();
%>
							<tr>
							<!--td><input type="radio" value="<%=iinput_number.toString()%>:<%=i_str%>" name="RESPONSE<%=iRESPONSE_NUMBER.toString()%>"></td-->
							<td><input type="radio" value="<%=i_str%>" name="RESPONSE<%=iRESPONSE_NUMBER.toString()%>"></td>
							<td align='right'><%=display%>) </td>
							<td><div DATAFORMATAS="HTML"><%=body%></span></td>
							</tr>
<%
					}
					i++;
				}
				catch(Exception e)
				{
					System.err.println( "Exception thrown" );
				}
          	}
			while( bEndOfList == false );
			dbResultSet.close();
%>
							</table>
		  					</font>
              				</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
<%
			//////////////////////////////////////////////////////////////////////////
			/////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////
		}
		else
		{
			//////////////////////////////////////////////////////////////////////////
			// FILL-IN (ETC) QUESTION ///////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////
			
			Integer iinput_number = new Integer(input_number);
			Integer iRESPONSE_NUMBER = new Integer(input_number);
%>
			<td valign="top">
				<table cols="2">
				<tr>
					<td valign="top" align="right" width="6">
						<strong><%=input_number%>) </strong>
					</td>
					<td>
						<table border="0" align="center" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<font color="#000000" size="2" face="Arial">
								<strong><div DATAFORMATAS="HTML"><%=question%></div></strong>
								</font>
					  		</td>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
			<td>
<%
			if( type == 1 )				// fill-in
			{
%>
				<input type="text" name="RESPONSE<%=iRESPONSE_NUMBER%>" class="enabled_yes">
<%
			}
			else if( type == 2 )		// number
			{
%>
				<input type="text" ID="RESPONSE<%=iRESPONSE_NUMBER%>" name="RESPONSE<%=iRESPONSE_NUMBER%>"  class="enabled_yes" onkeypress="validateinput( this, 'int' );" onblur="formatinput( this, 'int', 0 );">&nbsp;<img onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" src="<%=request.getContextPath()%>/images/calc.gif" onclick="showCalc(this,'RESPONSE<%=iRESPONSE_NUMBER%>');"></img>
<%
			}
			else if( type == 4 )		// paragraph
			{
				String sStyle = "background-color:#FFFFFF;border:1px groove;";
				StringRef sb_Value = new StringRef();
				formsapi.GetQuestionParam( "", questid, "width", sb_Value );
				if( sb_Value.getStr().length() > 0 )
				{
					sStyle = sStyle + "width:" + sb_Value.getStr() + ";";
				}
				formsapi.GetQuestionParam( "", questid, "height", sb_Value );
				if( sb_Value.getStr().length() > 0 )
				{
					sStyle = sStyle + "height:" + sb_Value.getStr() + ";";
				}
%>
				<textarea ID="RESPONSE<%=iRESPONSE_NUMBER%>" name="RESPONSE<%=iRESPONSE_NUMBER%>" style="<%=sStyle%>"></textarea>
<%
			}
			else if( type == 5 )		// yes/no
			{
%>
				<input type="checkbox" ID="RESPONSE<%=iRESPONSE_NUMBER%>" name="RESPONSE<%=iRESPONSE_NUMBER%>"  class="enabled_yes">
<%
			}
			else if( type == 3 )		// date
			{
%>
				<!-- New Calendar -->						
				<input type="text" ID="RESPONSE<%=iRESPONSE_NUMBER%>" NAME="RESPONSE<%=iRESPONSE_NUMBER%>"  class="enabled_yes" onblur="formatinput( this, 'date' );"/>&nbsp;<img id="button_<%=iRESPONSE_NUMBER%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" src="<%=request.getContextPath()%>/images/calendar.gif"></img>
				<script type="text/javascript">
					Calendar.setup({
						inputField     :    "RESPONSE<%=iRESPONSE_NUMBER%>",      // id of the input field
						//ifFormat       :    "%m/%d/%Y %I:%M %p",       // format of the input field
						ifFormat       :    "%m/%d/%Y",       // format of the input field
						//showsTime      :    true,            // will display a time selector
						showsTime      :    false,            // will display a time selector
						button         :    "button_<%=iRESPONSE_NUMBER%>",   // trigger for the calendar (button ID)
						singleClick    :    false,           // double-click mode
						step           :    1                // show all years in drop-down boxes (instead of every other year as default)
					});
				</script>
				<!-- New Calendar -->
<%
			}
%>
			</td>
<%
			  //////////////////////////////////////////////////////////////////////////
			  /////////////////////////////////////////////////////////////////////////
			  ////////////////////////////////////////////////////////////////////////
		}
		
		if( form_type == 2 || form_type == 3 )
		{
			break;
		}
		input_number++;
	}
%>
	</tr>
	</table>
<%
}
%>

<% if( ( form_type == 2 || form_type == 3 ) && RESPONSE_NUMBER < count ){ %>
       <INPUT type="image" src="<%=request.getContextPath()%>/images/buttons/next.gif" name="submit" alt="Submit" border="0">
<% }else{ %>
       <INPUT type="image" src="<%=request.getContextPath()%>/images/buttons/submit.gif" name="submit" alt="Submit" border="0">
<% } %>

    </p>
	</CENTER>
	</FORM>
	</BODY>
</HTML>