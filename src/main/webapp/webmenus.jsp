<%@ include file="ui/objmanhdr.jsp" %>

<html>
<body>
<h2>Available Menus</h2>
<hr/>
<%
	Credentials info = new Credentials();
	if( objectBean.SystemLogin( "admin", info ) == true )
	{
		ObjectQuery queryLoc = new ObjectQuery( "CELocation" );
		QueryResponse qrLoc = objectBean.Query( info, queryLoc );
		RepositoryObjects oLocs = qrLoc.getObjects( queryLoc.getClassName() );
		for( int i = 0; i < oLocs.count(); i++ )
		{
			RepositoryObject oLoc = oLocs.get(i);
			String locName = oLoc.getPropertyValue("name");
			String locId = oLoc.getId();
%>

<a href="Menus/<%=locId%>">
<%=locName%>
</a>

<!--a href="app/MenuView?loc=<%//=locId%>">
<%=locName%>
</a-->
<br/>

<%
		}
		objectBean.Logout(info);
	}
%>

</body>
</html>
