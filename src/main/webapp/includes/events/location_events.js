
function OpenMenuWindow( id )
{
	var rnumber = Math.round( Math.random() * ( 200 - 1 ) );
	window.open("../Menus/" + id,"_blank","resizable=yes,menubar=no,location=no,toolbar=no,status=no,scrollbars=yes,directories=no,width=1040,height=600");
}

function OpenMenuDesigner( id )
{
	if($("#menu_designer_link-" + id).size()==0)
		$("<a id='menu_designer_link-" + id + "' href='../app/designer/menudesigner.jsp?loc=" + id + "'/>").insertAfter( $("body") );
	$("#menu_designer_link-" + id).fancybox({
        'width' : '98%',
        'height' : '98%',
        'autoScale' : false,
        'transitionIn' : 'none',
        'transitionOut' : 'none',
        'type' : 'iframe'
    }).click();
}

function OpenMenusLink( id )
{
	var path = document.location.pathname;
	var pathParts = path.split("/");
	var rootPath = "/" + pathParts[1];
	showDialogEx("<h5 style='margin-top:2px;'>Copy the link below and place it on a page of your web site. This link will open your live menu system.</h5><textarea style='width:100%;height:80%;'><a target='_blank' href='" + document.location.protocol + "//" + document.location.host + rootPath + "/Menus/" + id + "'>View Menus</a></textarea>", "Menu / Ordering Link", 600, 250);
}

function CreateMenusLink( id )
{
	var path = document.location.pathname;
	var pathParts = path.split("/");
	var rootPath = "/" + pathParts[1];
	return "<a target='_blank' href='" + document.location.protocol + "//" + document.location.host + rootPath + "/Menus/" + id + "'>View Menus</a>";
}

function eventHandler( eventid )
{
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
		// View Menu
		OpenMenuWindow( sel_id );
		break;
	case 1:
		// Copy Link
		OpenMenusLink( sel_id );
		break;
	}
	return 0;
}

// Highlight important buttons
$(function(){
//	setTimeout( function(){
//		$("#Button_Custom3").css({"background": "#ff0000"});
//		$("#Button_Custom2").css({"background": "#ff0000"});
//	}, 1000 );
});
