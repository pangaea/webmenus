function LaunchForm( id, issid )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../forms/form.jsp?TITLEPAGE=Y&ID="+id+"&ISSID=0&num=" + rnumber,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
}

function OpenExamWindow( id )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../forms/exam_results.jsp?RESID="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
}

function OpenSurveyReultsWindow( id )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../forms/survey_results.jsp?ISSID="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
}

function OpenExamSummaryWindow( id )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../forms/exam_summary.jsp?ISSID="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
}

function eventHandler( eventid )
{
	var sel_id = getSelID( "-1" );
	if( eventid == 1 )
	{
		// Publish Exam
		if( confirm( "This operation will make the selected item readonly as well as make it available for use in the test center, Continue?" ) == true )
		{
			buttonCallback( B_EDIT );
			document.all.status.value = "Published";
			//saveObject();
			buttonCallback( B_SUBMIT );
		}
	}
	else if( eventid == 2 )
	{
		// View Form
		LaunchForm( sel_id );
	}
	else if( eventid == 3 )
	{
		OpenExamWindow( sel_id );
	}
	else if( eventid == 4 )
	{
		OpenSurveyReultsWindow( sel_id );
	}
	else if( eventid == 5 )
	{
		OpenExamSummaryWindow( sel_id );
	}
	return 0;
}
