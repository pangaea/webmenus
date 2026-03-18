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
	if( menuOrderBean.isValidated() == false )
	{
		response.sendRedirect( "login_patron.jsp" );
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
function submitOrder()
{
	document.location.href = "ordersubmit.jsp";
}
</script>
<script 
src="https://www.paypal.com/sdk/js?client-id=AU2TRC2m41gTinrJfNVas_8sFyqjC5EaUYjjgTc3sZvJk5Hs1U1mWbSNPz3lgl3rOzkeCPS0kfeSBaWX&components=buttons">
</script>
</head>

	<body class="tundra">
		<div id="paypal-container-5WK5HP852M52J"></div>
		<script>
		paypal.Buttons({
			style: {
				layout: 'vertical',
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
						cart: [{
							sku: 'YOUR_PRODUCT_SKU',
							quantity: 'YOUR_PRODUCT_QUANTITY',
						}]
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
					}
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

		}).render('#paypal-container-5WK5HP852M52J');
		</script>

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
			</script>
		</button>
	</body>
</html>
