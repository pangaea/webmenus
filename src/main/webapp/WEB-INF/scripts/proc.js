// Genesys ObjectManger Event

// Import packages
//importPackage(javax.swing);

function eventHandler( event )
{
	//JOptionPane.showMessageDialog(null, 'Hello, world!');
	//var objMan = server.getObjectManager();
	//var queryStmt = server.createObjectQuery("CUser");
	//var response = server.QueryXML( queryStmt );
	//event.write( "<test>" + event.getEvent() + "</test>" );
	//event.write( response );
	//event.write( "<sample>test</sample>" );
	
//	var submitStmt = server.createObjectSubmit("CRole");
//	submitStmt.addProperty("name", "from script 3");
//	submitStmt.addProperty("description", "from script 3");
	//submitStmt.addProperty("parent", "");
//	submitStmt.addProperty("admin", true);
//	server.Insert(submitStmt);
	var queryStmt = server.getContext();
	//event.write( queryStmt );
	server.LogError(queryStmt);
}