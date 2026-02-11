
function eventHandler( event )
{
	//server.LogInfo("eventHandler invoked");

	var className = event.getClassName();
	var eventName = event.getEvent();
	var queryStmt = server.getContext();
	var creds = server.getCredentials();
	var take_orders  = queryStmt.getProperties().get("take_orders").getInt();
	
	if(className == "CCMenu" && eventName == "preUpdate" && take_orders < 2 )
	{
		queryStmt.getProperties().get("schedule").setValue("");
	}
}
