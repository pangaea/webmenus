		
function eventHandler( eventid )
{
	switch(eventid)
	{
	case 1:
		var sel_id = getSelID( "-1" );
		if(sel_id == "-1" ){
			alert("Please selection an order.");
			break;
		}
		showDialog("../app/OrderView?oid=" + sel_id, "Order Details", 700, 600);
		break;
	}
	return 0;
}
