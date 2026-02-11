// Genesys ObjectManger Event

// Import packages
//importPackage(javax.swing);

var basic_maxLocations	= 2;
var basic_maxMenus		= 3;
var basic_maxCategories	= 15;
var basic_maxItem		= 60;

function countAccountObjects(creds, className)
{
	var queryStmt = server.createObjectQuery(className);
	queryStmt.addProperty("role", creds.getRole());
	var response = server.Query(queryStmt);
	return response.getObjects(className).count();
}

function eventHandler( event )
{
	//server.LogInfo("eventHandler invoked");

	var className = event.getClassName();
	var eventName = event.getEvent();
	var queryStmt = server.getContext();
	var creds = server.getCredentials();
	//server.LogInfo("className=" + className);
	//server.LogInfo("eventName=" + eventName);
	
	if(className == "CELocation" && eventName == "preInsert" &&
		countAccountObjects(creds, "CELocation") >= basic_maxLocations)
	{
		event.setAbort("You have exceeded the 'location' limit for a BASIC account");
	}
	else if(className == "CCMenu" && eventName == "preInsert" &&
			 countAccountObjects(creds, "CCMenu") >= basic_maxMenus)
	{
		event.setAbort("You have exceeded the 'menu' limit for a BASIC account");
	}
	else if(className == "CCMenuCategory" && eventName == "preInsert" &&
			 countAccountObjects(creds, "CCMenuCategory") >= basic_maxCategories)
	{
		event.setAbort("You have exceeded the 'menu category' limit for a BASIC account");
	}
	else if(className == "CCMenuItem" && eventName == "preInsert" &&
			 countAccountObjects(creds, "CCMenuItem") >= basic_maxItem)
	{
		event.setAbort("You have exceeded the 'menu item' limit for a BASIC account");
	}
}
