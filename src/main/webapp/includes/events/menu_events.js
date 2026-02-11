function OpenMenuDesigner( id )
{
	if($("#menu_designer_link-" + id).size()==0)
		$("<a id='menu_designer_link-" + id + "' href='../app/designer/menudesigner.jsp?menu=" + id + "'/>").insertAfter( $("body") );
	$("#menu_designer_link-" + id).fancybox({
        'width' : '98%',
        'height' : '98%',
        'autoScale' : false,
        'transitionIn' : 'none',
        'transitionOut' : 'none',
        'type' : 'iframe',
        onClosed: function(){
        	parent.refreshView();
        }
    }).click();
}

function eventHandler( eventid ){
	var sel_id = getSelID( "-1" );
	if(sel_id == "-1" ){
		alert("Please selection a location.");
		return;
	}
	
	switch(eventid)
	{
	case 3:
		// Open Menu Designer
		OpenMenuDesigner( sel_id );
		break;
	case 2:
		break;
	}
	return 0;
}

$(function(){
	//$("#Button_Custom3").css({"background": "#ffc1c1"});
	$("#take_orders").change(function(){
		display_schedule();
	})
	$("#obj_sel_data").bind('object-select',display_schedule);
});
	
function display_schedule(){
	if( $("#take_orders").val() == 2 ){
		$("#DIV_schedule").show();
	}else{
		$("#DIV_schedule").hide();
	}
}