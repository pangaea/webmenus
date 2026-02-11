function OpenScheduleDesigner( id )
{
	if($("#schedule_designer_link-" + id).size()==0)
		$("<a id='schedule_designer_link-" + id + "' href='../app/scheduler/schedule_designer.jsp?id=" + id + "'/>").insertAfter( $("body") );
	$("#schedule_designer_link-" + id).fancybox({
	    'width' : 940,
	    'height' : 570,
	    'transitionIn' : 'none',
	    'transitionOut' : 'none',
	    'hideOnOverlayClick' : false,
	    'type' : 'iframe'
    }).click();
}

function eventHandler( eventid )
{
	var sel_id = getSelID( "-1" );
	if(sel_id == "-1" ){
		alert("Please selection a schedule.");
		return;
	}
	
	switch(eventid)
	{
	case 1:
		OpenScheduleDesigner(sel_id);
		break;
	}
	return 0;
}


//Highlight important buttons
$(function(){
	/*
	setTimeout( function(){
		$("#Button_Custom1").css({"background": "#ffc1c1"});
	}, 1000 );
	*/
	//$("#Button_Custom1").css({"background": "#ffc1c1", "color": "#ff0000"});
});