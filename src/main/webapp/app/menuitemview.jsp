<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.*"%>
<%@ page import="com.genesys.SystemServlet"%>
<%@ page import="com.genesys.repository.*"%>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
%>
<html>
	<head>
		<title>Menu Item View</title>
		
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
		</style>
	
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
		<!--script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script-->
<%
	String themeName = menuOrderBean.getTheme();
	if( themeName.length() > 0 ){
%>
<!-- Theme based includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/MenuView/theme?id=<%=themeName%>"></link>
<%
	} else {
%>
<!-- Default theme includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
<%
	}
%>		
		<script type="text/javascript">
			dojo.require("dijit.form.Button");
			dojo.require("dijit.form.NumberSpinner");
			dojo.require("dijit.form.CheckBox");
			dojo.require("dojo.parser");
		</script>
		
		<script type="text/javascript">
		//window.onload = function()
		dojo.addOnLoad( function()
		{
			//createCalc();
		});
		function submitMenuItem()
		{
			submitItem.submit();
		}
		</script>
	</head>
	<body class="tundra">
<%
	//Credentials info = new Credentials();
	//if( objectBean.Login( "guest", "guest", info ) == true )
	if( menuOrderBean.verifyObjManCreds() )
	{
%>
	<form method="post" id="submitItem" action="../menuitemsubmit.jsp">
<%
		String sizeid = request.getParameter("id");
		if( sizeid == null ) return;
%>
		<input type="hidden" name="sizeId" value="<%=sizeid%>"/>
<%
		ObjectQuery queryMenuItemSize = new ObjectQuery( "CCMenuItemSize" );
		queryMenuItemSize.addProperty("id",sizeid);
		ObjectManager objectBean = SystemServlet.getObjectManager();
		QueryResponse qrMenuItemSizes = objectBean.Query( menuOrderBean.getCredentials(), queryMenuItemSize );
		RepositoryObjects oMenuItemSizes = qrMenuItemSizes.getObjects( queryMenuItemSize.getClassName() );
		if( oMenuItemSizes.count() == 0 )
		{
			return;
		}
		RepositoryObject oMenuItemSize = oMenuItemSizes.get(0);
		String sMenuItemPrice = oMenuItemSize.getPropertyValue("price");
		String sMenuItemSizeDesc = oMenuItemSize.getPropertyValue("size_desc");
		String sMenuItemId = oMenuItemSize.getPropertyValue("menuitem");
		if( sMenuItemId.equalsIgnoreCase("null") == true || sMenuItemId.length() == 0 )
			sMenuItemId = sizeid;

		ObjectQuery queryMenuItem = new ObjectQuery( "CCMenuItem" );
		queryMenuItem.addProperty("id",sMenuItemId);
		QueryResponse qrMenuItems = objectBean.Query( menuOrderBean.getCredentials(), queryMenuItem );
		RepositoryObjects oMenuItems = qrMenuItems.getObjects( queryMenuItem.getClassName() );
		if( oMenuItems.count() > 0 )
		{
			RepositoryObject oMenuItem = oMenuItems.get(0);
			String sMenuItemName = oMenuItem.getPropertyValue("name");
			if( sMenuItemSizeDesc.length() > 0 )
				sMenuItemName = sMenuItemName + " (" + sMenuItemSizeDesc + ")";

			String sMenuItemImg = oMenuItem.getPropertyValue("image");
			String sMenuItemDesc = oMenuItem.getPropertyValue("description");
			//String sMenuItemPrice = oMenuItem.getPropertyValue("price");
%>
			<table cellspacing='4'><tr><td>
<%
			if( sMenuItemImg.length() > 0 &&
				( sMenuItemImg.indexOf(".gif") >= 0 ||
				  sMenuItemImg.indexOf(".jpeg") >= 0 ||
				  sMenuItemImg.indexOf(".jpg") >= 0 ||
				  sMenuItemImg.indexOf(".png") >= 0 ) ){
%>
				<img src='../../ImageViewer<%=sMenuItemImg%>'/>
			<% } %>
			</td><td valign='top'>
			
			<div class="menuItemTitle"><%=sMenuItemName%></div>
			<div class="menuItem"><%=sMenuItemDesc%></div>
			<div><%=menuOrderBean.getCurrencyString(sMenuItemPrice)%></div>
			</td></tr></table>
			<hr>
<%
		}
%>
		<table id="itemOptionsTable" cellpadding="2" align="center">
			<tr>
				<th colspan="2" valign="top" class="OptionsTitle">Select from options...</th>
			</tr>
<%
		ObjectQuery queryOptions = new ObjectQuery( "CCMenuItemOption" );
		queryOptions.setSortBy("option_index");		// TODO: Fix this - it should reference the property, not the column
		queryOptions.setSortOrder("ASC");
		queryOptions.addProperty("menuitem",sMenuItemId);
		QueryResponse qrOptions = objectBean.Query( menuOrderBean.getCredentials(), queryOptions );
		RepositoryObjects oOptions = qrOptions.getObjects( queryOptions.getClassName() );
		for( int i = 0; i < oOptions.count(); i++ )
		{
			RepositoryObject obj = oOptions.get( i );
			String sOptionId = obj.getId();
			String sOptionPrice = new String("");
			String sPrice = obj.getPropertyValue("price");
			if( sPrice.length() > 0 )
			{
				BigDecimal bdOptionPrice = new BigDecimal(obj.getPropertyValue("price"));
				if( bdOptionPrice.doubleValue() > 0.00 )
				{
					NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
					sOptionPrice = "<em> (add " + n.format(bdOptionPrice) + ")</em>";
				}
			}
%>
			<tr>
				<th valign="top"><%=obj.getPropertyValue("name")%></th>
				<td>
<%
			String sOptionType = obj.getPropertyValue("type");
			if( sOptionType.equalsIgnoreCase("select") == true )
			{
				String sData = obj.getPropertyValue("data");
				String results[] = sData.trim().split("\n");
				for(int ii =0; ii < results.length; ii++)
				{
					String sOptionTxt = results[ii].trim();
					if( sOptionTxt.length() == 0 ) continue;
%>
					<input dojoType="dijit.form.CheckBox" type="checkbox" name="<%=obj.getPropertyValue("name")+ii%>"><%=sOptionTxt%><%=sOptionPrice%></input><br/>
<%
				}
			}
			else if( sOptionType.equalsIgnoreCase("select-one") == true )
			{
%>
				<input dojoType="dijit.form.RadioButton" type="radio" checked name="<%=obj.getPropertyValue("name")%>" value="_none_">No Thanks</input><br/>
<%
				String sData = obj.getPropertyValue("data");
				String results[] = sData.split("\n");
				for(int ii =0; ii < results.length; ii++)
				{
					//String extra = new String("");
					//if( ii == 0 ) extra = "checked";
%>
					<input dojoType="dijit.form.RadioButton" type="radio" name="<%=obj.getPropertyValue("name")%>" value="<%=results[ii].trim()%>"><%=results[ii].trim()%><%=sOptionPrice%></input><br/>
<%
				}
			}
			else if( sOptionType.equalsIgnoreCase("text") == true )
			{
%>
					<textarea name="<%=obj.getPropertyValue("name")%>" style="width:100%;height:80px"></textarea><br/>
<%
			}
%>
				</td>
			</tr>
<%
		}
		//objectBean.Logout(info);
	}
%>
			<tr>
				<th valign="top">Quantity</th>
				<td>
					<input dojoType="dijit.form.NumberSpinner" style="width:80px" constraints="{min:1,max:200}" type="text" id="quantity" name="quantity" value="1" onkeypress="validateinput( this, 'int' );" onblur="formatinput( this, 'int', 0 );"/>
				</td>
			</tr>
		</table>
		</form>
		<br>
<%		if( bOpen ){ %>
		<button dojoType="dijit.form.Button">
			Add To Order
			<script type="dojo/method" event="onClick">
				submitMenuItem();
			</script>
		</button>
<%		} %>
		<button dojoType="dijit.form.Button">
			Close
			<script type="dojo/method" event="onClick">
				parent.closeOrderDialog();
			</script>
		</button>
	</body>
</html>
