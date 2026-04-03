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
	if( bOpen == false )
	{
%>
		Sorry, this establishment is currently not taking orders.
<%
		return;
	}
	
	//if( request.getSession().getAttribute( "patron_id" ) == null )
	if( menuOrderBean.isValidated() == false ) {
		response.sendRedirect( "login_patron.jsp" );
		return;
	}

	// Check for delivery options and record them if present
	String sDelivery = request.getParameter("delivery_option");
	if( sDelivery != null && sDelivery.equalsIgnoreCase("delivery") == true )
	{	
		String sDeliveryInfo = request.getParameter("delivery_info");
		menuOrderBean.setDeliveryAddress( sDeliveryInfo );
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
function submitOrder()
{
	document.location.href = "ordersubmit.jsp";
}
</script>
</head>

	<body class="tundra">
		<table id="itemTable" cellpadding="2" class="orderTable">
			<tr>
				<th valign="top">Order</th>
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
				<td valign="top"><%=item.getQuantity()%></td>
				<td valign="top"><%=item.getTotalStr()%></td>
			</tr>
<%
	}
	if( menuOrderBean.itemCount() == 0 )
	{
%>
			<tr>
				<td colspan="4"><span style="width:100%;text-align:center;">Empty Order Form</span></td>
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






		<table class="pageSeperator"><tr>
			<td width="100%"><hr/></td>
		</tr></table>


<%
	for( int i = 0; i < menuOrderBean.getPaymentMethodCount(); i++ )
	{
		String type = menuOrderBean.getPMType(i);
		switch (type) {
			case "PayPal":
				String clientId = menuOrderBean.queryPmConfig(i, "CLIENT_ID");
				String clientSecret = menuOrderBean.queryPmConfig(i, "CLIENT_SECRET");
%>
		<script 
		src="https://www.paypal.com/sdk/js?client-id=<%=clientId%>&components=buttons">
		</script>
		<div id="paypal-container-<%=clientId%>"></div>
		<script>
		paypal.Buttons({
			style: {
				layout: 'horizontal',
				color:  'blue',
				shape:  'rect',
				label:  'paypal'
			},

			// Call your server to create an order
			createOrder: function(data, actions) {
				return fetch('/webmenus/PayPalPortal/createOrder', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						payment_index: "<%=i%>"
					})
				}).then(function(response) {
					return response.json();
				}).then(function(orderData) {
					// Returns the order ID a.k.a. the approval URL
					return orderData.id;
				});
			},

			// Call your server to capture the payment
			onApprove: function(data, actions) {
				return fetch('/webmenus/PayPalPortal/order/' + data.orderID + '/capture', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						payment_index: "<%=i%>"
					})
				}).then(function(response) {
					return response.json();
				}).then(function(orderData) {
					// Show a success message to the buyer
					submitOrder();
					// Redirect to a success page or update UI
					// window.location.href = '/success.html';
				});
			},

			// Handle errors or cancellations
			onCancel: function(data) {
				console.log('Payment cancelled', JSON.stringify(data, 0, 2));
			},

			onError: function(err) {
				console.error('PayPal error', err);
				alert('An error occurred during the transaction. Please try again.');
			}

		}).render('#paypal-container-<%=clientId%>');
		</script>
<%
			break;
		}
	}
%>

<% if( menuOrderBean.itemCount() > 0 ){ %>
		<button dojoType="dijit.form.Button">
			Pay at Location
			<script type="dojo/method" event="onClick">
				submitOrder();
			</script>
		</button>
<% } %>
		<button dojoType="dijit.form.Button">
			Go Back to Menus
			<script type="dojo/method" event="onClick">
				parent.closeOrderDialog();
			</script>
		</button>
	</body>
</html>
