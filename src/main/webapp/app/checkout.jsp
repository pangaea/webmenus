<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.genesys.webmenus.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.*"%>
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
%>
<html>
	<head>
		<title>Place Order</title>
		
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
			.pageSeperator2 hr
			{
				background-color: black !important;
				height:2px !important;
			}
		</style>
	
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/checkout.js"></script>
		
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
	dojo.require("dijit.Dialog");
	dojo.require("dijit.form.NumberSpinner");
	dojo.require("dojo.parser");
	dojo.require("dijit.form.CheckBox");
</script>

<script type="text/javascript">
//document.onload = function()
dojo.addOnLoad( function()
{
	//window.scrollBy(0,1000);
	window.scrollTo({
		top: document.body.scrollHeight,
		behavior: 'smooth'
	});
});
// function submitOrder()
// {
// 	document.location.href = "ordersubmit.jsp";
// }
</script>
</head>

	<body class="tundra" onload="selectDeliveryOption('pickup')">
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

		<div class='menuTitle'>Your Information</div>

		<form id="orderCheckout" method="post" action="<%=request.getContextPath()%>/OrderCheckout">
		<table style="margin:auto;padding:20px;">
		<% int index = 0; %>
		<viewCfg:ViewForm viewName="patrons">
		<% if( inputVisible.equalsIgnoreCase("false") == false ){
			if((index & 1) == 0){ %>
			<tr>
			<% } %>
				<td>
					<div class='patronLoginLabel'><%=inputText%></div>
				</td>
				<td>
					<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text"
							ID="<%=inputField%>" NAME="<%=inputField%>" maxlength="<%=inputLen%>"/>
				</td>
				<td style="padding-right:20px;">
			<% if( inputRequired == true ){ %>
					<span style="color:red">*</span>
			<% } %>
				</td>
			<% if((index & 1) == 1){ %>
			</tr>
			<% }
			index++;
		} %>
		</viewCfg:ViewForm>
		</table>



<% if( menuOrderBean.isDeliveryAvailable() ) { %>
		<table class="pageSeperator"><tr>
			<td width="100%"><hr/></td>
		</tr></table>








	<table style="width:100%"><tr><td style="text-align:center">
		<span style="color:red;"><!--%=errMsg%--></span>
		<div class='menuTitle'>Select Delivery Options</div>
		<br/>
		<div class="menuItemTitle">
			<input dojoType="dijit.form.RadioButton" type="radio" id="option_pickup" name="delivery_option" checked value="pickup"  onclick="selectDeliveryOption(this.value)"/>Pickup Order
		</div>

		<table class="pageSeperator pageSeperator2"><tr>
			<td width="100%"><hr width="100%"/></td>
		</tr></table>

		<div class="menuItemTitle">
		<input dojoType="dijit.form.RadioButton" type="radio" id="option_delivery" name="delivery_option" value="delivery" onclick="selectDeliveryOption(this.value)"/>Deliver Order to...
		
	<%
		Vector<String> addrs = menuOrderBean.getPartonDeliveryAddresses();
		Iterator itr = addrs.iterator();
		int ii = 1;
	    while(itr.hasNext()){
	%>
	    	<textarea id="addr_<%=Integer.toString(ii)%>" style="display:none"><%=itr.next()%></textarea>
	<%
			ii++;
	    }
	%>
		<select id="previous_deliveries" style="width:250px;" onchange="selectDeliverAddr(this)">
		<option value="0">[New Address]</option>
	<%
		itr = addrs.iterator();
		ii = 1;
	    while(itr.hasNext()){
	    	String body = (String)itr.next();
	%>
			<option value="<%=Integer.toString(ii)%>" title="<%=body%>">
	    	<%=body%>
	    	</option>
	<%
			ii++;
		}
	%>
		</select>
		<br/><br/>
		</div>
			<textarea id="delivery_info" name="delivery_info" style="display:none;"></textarea>
			<table>
				<tr>
					<td><div class='patronLoginLabel'>Address</div></td><td>
						<input WMrequired="true" title="Address" type="text" ID="address" name="address" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>City</div></td><td>
						<input WMrequired="true" title="City" type="text" ID="city" name="city" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>State</div></td><td>
						<input WMrequired="true" title="State" type="text" ID="state" name="state" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>Zip</div></td><td>
						<input WMrequired="true" title="Zip" type="text" ID="zip" name="zip" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>Contact Phone #</div></td><td>
						<input WMrequired="true" title="Contact Phone #" type="text" ID="contact_number" name="contact_number" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td/><td colspan="2"><span style="color:red;font:8pt verdana;">* Required</span></td>
				</tr>
			</table>
			</td>
			</tr>
			</table>
			</form>
<% } %>








		<table class="pageSeperator"><tr>
			<td width="100%"><hr/></td>
		</tr></table>





<% if( menuOrderBean.itemCount() > 0 ){ %>

<%
	for( int i = 0; i < menuOrderBean.getPaymentMethodCount(); i++ )
	{
		String type = menuOrderBean.getPMType(i);
		switch (type) {
			case "PayPal":
				String clientId = menuOrderBean.queryPmConfig(i, "CLIENT_ID");
				String clientSecret = menuOrderBean.queryPmConfig(i, "CLIENT_SECRET");
%>
<!-- &enable-funding=venmo&buyer-country=US -->
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
				debugger;
				return fetch('/webmenus/PayPalPortal/order/' + data.orderID + '/capture', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						payment_index: "<%=i%>",
						email: document.getElementsByName('email')[0].value,
						firstname: document.getElementsByName('firstname')[0].value,
						lastname: document.getElementsByName('lastname')[0].value,
						phone_num: document.getElementsByName('phone_num')[0].value,
						delivery_option: document.getElementById('option_delivery').checked ?
							document.getElementsByName('delivery_option')[1].value :
							document.getElementsByName('delivery_option')[0].value,
						address: document.getElementsByName('address')[0].value,
						city: document.getElementsByName('city')[0].value,
						state: document.getElementsByName('state')[0].value,
						contact_number:document.getElementsByName('contact_number')[0].value
					})
				}).then(function(response) {
					return response.json();
				}).then(function(orderData) {
					// Show a success message to the buyer
					//submitOrder();
					//orderCheckout.submit();
					//document.getElementById("orderCheckout").submit();
					// Redirect to a success page or update UI
					window.location.href = '/success.html';
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

<% if( menuOrderBean.isPayOnPickup() ){ %>
		<button dojoType="dijit.form.Button">
			Pay at Location
			<script type="dojo/method" event="onClick">
				//submitOrder();
				orderCheckout.submit();
			</script>
		</button>
<% } } %>
		<button dojoType="dijit.form.Button">
			Go Back to Menus
			<script type="dojo/method" event="onClick">
				parent.closeOrderDialog();
			</script>
		</button>
	</body>
</html>
