<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.genesys.webmenus.*"%>
<%@ page import="java.math.BigDecimal"%>

<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
	if( bOpen == false ){
%>
		Sorry, this establishment is currently not taking orders.
<%
		return;
	}
%>
<html>
	<head>
		<title>Place Order</title>
		
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
		</style>
	
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
		
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
	dojo.require("dojo.parser");
</script>

<script type="text/javascript">
//document.onload = function()
dojo.addOnLoad( function()
{
	window.scrollBy(0,1000);
});
function updateOrder(qid)
{
	//var qt = (String)dijit.byId(id).value;
	var qt = document.getElementById(qid).value;
	if(qt.length > 0)
		window.setTimeout('orderForm.submit();',500);
}
function submitOrder()
{
	document.location.href = "../ordersubmit.jsp";
}
</script>
</head>

	<body class="tundra">
		<form method="POST" id="orderForm"><!--action="../orderview.jsp"-->
		<table id="itemTable" cellpadding="2" class="orderTable">
			<tr>
				<th valign="top" style="width:100%;">Order</th>
				<th valign="top">Each</th>
				<th valign="top">Quantity</th>
				<th valign="top">Price</th>
			</tr>
<%
	for( int i = 0; i < menuOrderBean.itemCount(); i++ )
	{
		OrderItem item;
		while(true)
		{
			item = menuOrderBean.getItemByIndex(i);
			if( item == null ) break;
			String newQuan = request.getParameter(item.getId());
			if( newQuan != null )
			{
				try
				{
					int newQuantity = new Long(newQuan).intValue();
					if( newQuantity == 0 )
					{
						menuOrderBean.removeItemByIndex(i);
						continue;
						//item = menuOrderBean.getItemByIndex(i);
						//if( item == null ) break;
					}
					else
					{
						item.setQuantity(newQuantity);
					}
				}
				catch(NumberFormatException e){}
			}
			break;
		}
		if( item == null ) break;
%>
			<tr>
				<td valign="top">

						<div class='menuItemTitle'>
						<%=item.getName()%>
						<% if( item.getSize().length() > 0 ){ %>
							(<%=item.getSize()%>)
						<% } %>
						</div>
						<div class='menuItemDesc'>
						<%=item.getDesc()%>
						</div>
						<div class='menuOptions'>
<pre>
<%=item.getOptions()%>
</pre>
						</div>

				</td>
				<td valign="top"><%=item.getPriceStr()%></td>
				<td valign="top">
					<input dojoType="dijit.form.NumberSpinner" intermediateChanges="true" style="height:0px;width:60px;" constraints="{min:0,max:200}" type="text" id="quantity<%=i%>" name="<%=item.getId()%>" value="<%=item.getQuantity()%>" onChange="updateOrder('quantity<%=i%>');"/>
				</td>
				<td valign="top"><%=item.getTotalStr()%></td>
			</tr>
<%
	}
	if( menuOrderBean.itemCount() == 0 )
	{
%>
			<tr>
				<td colspan="4"><div style="width:100%;text-align:center;">Empty Order Form</div></td>
			</tr>
<%
	}
%>
			<tr>
				<th valign="top"></th>
				<th valign="top"></th>
				<th valign="top">Subtotal</th>
				<td valign="top"><%=menuOrderBean.getSubTotalStr()%></td>
			</tr>
			<tr>
				<th valign="top"></th>
				<th valign="top"></th>
				<th valign="top">Tax<small>(%<%=menuOrderBean.getTaxRate()%>)</small></th>
				<td valign="top"><%=menuOrderBean.getTaxTotalStr()%></td>
			</tr>
			<tr>
				<th valign="top"></th>
				<th valign="top"></th>
				<th valign="top">Total</th>
				<td valign="top"><%=menuOrderBean.getTotalStr()%></td>
			</tr>
		</table>
		</form>
<% if( menuOrderBean.itemCount() > 0 ){ %>
		<button dojoType="dijit.form.Button">
			Submit Order
			<script type="dojo/method" event="onClick">
				submitOrder();
			</script>
		</button>
<% } %>
		<button dojoType="dijit.form.Button">
			Go Back to Menus
			<script type="dojo/method" event="onClick">
				parent.closeOrderDialog();
				//parent.dijit.byId('myorder').destroy();
				//dijit.parent.byId('myorder').destroy();
				//dijit.byId('myorder').destroy();
			</script>
		</button>
	</body>
</html>
