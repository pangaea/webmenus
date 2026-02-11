<%@ include file="page_header.jsp" %>
<%@ page import="java.util.*"%>
<%
	String errMsg = request.getParameter("msg");
	if( errMsg == null ) errMsg = new String("");
%>
	
<!DOCTYPE html>
<html>
<head>
<meta name="HandheldFriendly" content="true" />
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.js"></script>

<style type="text/css">
input.enabled_no{
	background-color: #C0C0C0;
}
</style>

<script type="text/javascript">

function validateParams()
{
	if( $('#option_delivery').is(':checked') && $("#previous_deliveries").val() == "0" )
	{
		var deliveryOptionsParams = deliveryOptions.getElementsByTagName("input");
		for( i = 0; i < deliveryOptionsParams.length; i++ )
		{
			var param = deliveryOptionsParams[i];
			if( param.WMrequired == "true" && param.value.length == 0 )
			{
				alert("Required field '" + param.title + "' is missing.", "Invalid Parameter");
				param.focus();
				return false;
			}
		}

		// Build delivery information
		var addressIN = $("#address").val();
		var cityIN = $("#city").val();
		var stateIN = $("#state").val();
		var zipIN = $("#zip").val();
		var contactNumberIN = $("#contact_number").val();
		$("#delivery_info").text(addressIN + "\r\n" + cityIN + ", " + stateIN + " " + zipIN + "\r\n" + contactNumberIN + "\r\n");
	}
	
	return true;
}
function selectDeliveryOption(sel)
{
	var bDisabled = false;
	switch(sel)
	{
	case "pickup":
		bDisabled = true;
		break;
	case "delivery":
		bDisabled = false;
		break;
	}
	disableControls(bDisabled);
	$("#previous_deliveries").attr("disabled", bDisabled);
}
function disableControls(disabled)
{
	var bDisabled = disabled;
	$.each(["address", "city", "state", "zip", "contact_number"], function(index, value){
		$("#"+value).attr("disabled", bDisabled).toggleClass("enabled_no");
	});
}
function fillPrevAddr(id)
{
	var oDiv = document.getElementById("addr_" + id);
	var oTextArea = document.getElementById("delivery_info");
	oTextArea.innerText = oDiv.innerText;
}
function selectDeliverAddr(oSelect)
{
	if( oSelect.value == "0" )
	{
		disableControls(false);
	}
	else
	{
		fillPrevAddr(oSelect.value);
		disableControls(true);
	}
}

</script>

</head>

<body onload="selectDeliveryOption('pickup')">

<div data-role="page" id="page-delivery">

	<div data-role="header">
		<h1>Delivery Options</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
		<a data-rel="button" onclick="if( validateParams() ) deliveryOptions.submit();">Submit</a>
	</div>

	<div data-role="content">
		<form id="deliveryOptions" method="post" action="<%=rootMenuPath%>/submit_order">
		<span style="color:red;"><%=errMsg%></span>

		<fieldset data-role="controlgroup">
			<legend>Choose from delivery options:</legend>
			
     		<input type="radio" id="option_pickup" name="delivery_option" checked value="pickup" checked="checked" onclick="selectDeliveryOption(this.value)"/>
     		<label for="option_pickup">Pickup Order</label>

	
			
			
			<input type="radio" id="option_delivery" name="delivery_option" value="delivery" onclick="selectDeliveryOption(this.value)"/>
    		<label for="option_delivery">Deliver Order to...</label>
    		
    	</fieldset>
		<fieldset data-role="controlgroup">
			<div>
<%
	Vector<String> addrs = menuOrderBean.getPartonDeliveryAddresses();
	Iterator itr = addrs.iterator();
	int i = 1;
    while(itr.hasNext()){
%>
		    <textarea id="addr_<%=Integer.toString(i)%>" style="display:none"><%=itr.next()%></textarea>
<%
		i++;
    }
%>
			<select id="previous_deliveries" onchange="selectDeliverAddr(this)" data-native-menu="false">
				<option value="0">[New Address]</option>
<%
	itr = addrs.iterator();
	i = 1;
    while(itr.hasNext()){
    	String body = (String)itr.next();
%>
				<option value="<%=Integer.toString(i)%>" title="<%=body%>"><%=body%></option>
<%
		i++;
	}
%>
			</select>
			</div>

    	</fieldset>
		<fieldset data-role="controlgroup">
		
			<textarea id="delivery_info" name="delivery_info" style="display:none;"></textarea>
			<table>
				<tr>
					<td><div class='patronLoginLabel'>Address</div></td><td>
						<input WMrequired="true" title="Address" type="text" ID="address" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>City</div></td><td>
						<input WMrequired="true" title="City" type="text" ID="city" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>State</div></td><td>
						<input WMrequired="true" title="State" type="text" ID="state" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>Zip</div></td><td>
						<input WMrequired="true" title="Zip" type="text" ID="zip" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td><div class='patronLoginLabel'>Contact Phone #</div></td><td>
						<input WMrequired="true" title="Contact Phone #" type="text" ID="contact_number" maxlength="64"/>
					</td>
					<td>
						<span style="color:red">*</span>
					</td>
				</tr>
				<tr>
					<td/><td colspan="2"><span style="color:red;font:8pt verdana;">* Required</span></td>
				</tr>
				<tr>
					<td valign="middle" colspan="3" style="text-align:center;">
						<button onclick="if( validateParams() == false ) return false; deliveryOptions.submit();">Submit Order</button>
					</td>
					<td/>
				</tr>
			</table>
			
		</fieldset>
		</form>
	</div>
</div>

</body>
</html>
