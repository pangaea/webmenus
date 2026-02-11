
function OpenWorkspaceWindow( id )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../forms/workspace.jsp?workgrpid="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
	//window.open("objectpanels.jsp?view=workspace&workgrpid="+id,"","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=640,height=480");
}
		
function eventHandler( eventid )
{
	if( eventid == 1 )
	{
		// View Exam
		var sel_id = getSelID( "-1" );
		OpenWorkspaceWindow( sel_id );
	}
	return 0;
}
