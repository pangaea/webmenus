function SelectImage()
{
	var dlgParam = new Object();
	dlgParam.caller = window;
	window.showModalDialog( "select_image_frames.jsp?field=body", dlgParam, "dialogWidth:600px;dialogHeight:400px;resizable:yes;status:no;" );
}
		
function eventHandler( eventid )
{
	if( eventid == 1 )
	{
		SelectImage();
	}
	return 0;
}
