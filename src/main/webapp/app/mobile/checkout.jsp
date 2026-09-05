<%@ include file="page_header.jsp" %>
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.genesys.webmenus.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.fasterxml.jackson.databind.JsonNode"%>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<!DOCTYPE html>
<html>
<head>
<title>Place Order</title>
		
<meta name="HandheldFriendly" content="true" />
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.js"></script>

<script type="text/javascript">
function selectDeliveryOption(sel) {
	disableDeliveryForm((sel != "delivery"));
}
function disableDeliveryForm(bDisabled) {
	if (bDisabled) {
		$("#submit_order .ui-btn-text").text("Pay at Pickup");
	} else {
		$("#submit_order .ui-btn-text").text("Pay on Delivery");
	}
	// Target your table by its ID and disable all form elements inside it
	$("#delivery_info :input").prop("disabled", bDisabled);
	// Disables all mouse clicks, hovers, and pointer events on the table
	$("#delivery_info").css("pointer-events", (bDisabled) ? "none" : "auto");
	// Optional: Make it look faded out to visually signal it is disabled
	$("#delivery_info").css("opacity", (bDisabled) ? "0.5" : "1.0");
}
function submitForm() {
	if (validateCreateParams()) {
		$("#orderForm").submit();
	}
}
function validateCreateParams() {
    var createPatronParams = orderForm.getElementsByTagName("input");
    var errs = [];
    for( i = 0; i < createPatronParams.length; i++ ) {
        var param = createPatronParams[i];
        if( !param.disabled && param.attributes["wmrequired"]?.value === "true" && param.value.length == 0 ) {
            errs.push("Required field '" + param.title + "' is missing.");
            param.style.backgroundColor = "#ffd4d4";
        } else {
            param.style.backgroundColor = "#ffffff";
        }
    }
    return (errs.length === 0);
}
$(function(){
	$("input[type=number]").live("change", function(){
		if( $(this).val().length > 0 ){
			window.setTimeout(function(){ $("#orderForm").submit();	}, 100);
		}
	})
	$("a.remove_item").live("click", function(){
		var target_count = $(this).attr("item_id");
		$("#"+target_count).val("0");
		window.setTimeout(function(){ $("#orderForm").submit();	}, 100);
	})
	$("a.edit_item").live("click", function(){
		var item_id = $(this).attr("item_id");
		var item_index = $(this).attr("item_index");
		location.href = "<%=rootMenuPath%>/itemdetails?item=" + item_id + "&order_index=" + item_index;
	})
	disableDeliveryForm(true);
});
</script>

</head>

	<body>
	<div data-role="page" id="orderview">
	
	<div data-role="header">
		<h1>Place Order</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
		<a rel="external" data-ajax="false" href="<%=rootMenuPath%>/orderview">Cart<%=menuOrderBean.generateItemCountLabel()%></a>
	</div><!-- /header -->
	
	<div data-role="content">
	
		<form method="POST" id="orderForm" action="<%=rootMenuPath%>/submit_order">
		<table id="itemTable" cellpadding="2" class="orderTable" style="width:100%;">

<%
	for( int i = 0; i < menuOrderBean.itemCount(); i++ )
	{
		OrderItem item = menuOrderBean.getItemByIndex(i);
		if( item == null ) break;
%>
			<tr>
				<td valign="top" colspan="3">

						<div class='menuItemTitle'>
						<%=item.getName()%>
						<% if( item.getSize().length() > 0 ){ %>
							(<%=item.getSize()%>)
						<% } %>
						</div>

						<div class='menuOptions'>

<%
		String optionsJson = item.getOptions();
		if (optionsJson != null && !optionsJson.isBlank()) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode node = mapper.readTree(optionsJson);
				JsonNode options = node.get("options");
				if (options.isArray()) {
					for (JsonNode option : options) {
						%><b><%=option.get("name").asText()%></b><br/><%
						JsonNode choices = option.get("selected_choices");
						if (choices.isArray()) {
							for (JsonNode choice : choices) {
								%>&nbsp;&nbsp;<%=choice.get("name").asText()%><%
								if (choice.get("price").asDouble() > 0) {
									%> (<%=menuOrderBean.getCurrencyString(choice.get("price").asText())%>)<%
								}
								%><br/><%
							}
						}
					}
				}
			} catch (Exception e){}
		}

		String si = item.getSpecialInstructions();
		if (si != null && !si.isEmpty()) {
			%><br/><b>Special Instructions</b><pre><%=si%></pre><%
		}
%>

						</div>

				</td>
				</tr>
				<tr>
				<td><%=item.getPriceStr()%></td>
				<td>
					X&nbsp;<%=item.getQuantity()%>
				</td>
				<td  class="itemPrice"><%=item.getTotalStr()%></td>
			</tr>
			<tr>
			<td colspan="3" style="border: 1px solid black;background-color:#C0C0C0;">
			</td>
			</tr>
<%
	}
	if( menuOrderBean.itemCount() == 0 )
	{
%>
			<tr>
				<td colspan="3"><span style="width:100%;text-align:center;">Empty Order Form</span></td>
			</tr>
<%
	}
%>
			<tr>
				<th valign="top" colspan="2">Subtotal</th>
				<td valign="top" class="itemPrice"><%=menuOrderBean.getSubTotalStr()%></td>
			</tr>
			<tr>
				<th valign="top" colspan="2">Tax<small>(%<%=menuOrderBean.getTaxRate()%>)</small></th>
				<td valign="top" class="itemPrice"><%=menuOrderBean.getTaxTotalStr()%></td>
			</tr>
			<tr>
				<th valign="top" colspan="2">Total</th>
				<td valign="top" class="itemPrice"><%=menuOrderBean.getTotalStr()%></td>
			</tr>
		</table>

        <h2>Your Information</h2>
		<table style="width:100%"><tr><td style="text-align:center"></td>
        <viewCfg:ViewForm viewName="patrons">
        <% if( inputVisible.equalsIgnoreCase("false") == false ){ %>
			<tr><td>
            <label for="<%=inputField%>"><%=inputText%>:
            </label>
			</td><td>
            <input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=inputField%>" maxlength="<%=inputLen%>"/>
		    </td><td>
			<% if( inputRequired == true ){ %>
            <span style="color:red;">*</span>
            <% } %>	
			</td></tr>
        <% } %>
        </viewCfg:ViewForm>
		</table>

<% if( menuOrderBean.isDeliveryAvailable() ) { %>
        <h2>Delivery</h2>
        <fieldset data-role="controlgroup">

		<table style="width:100%"><tr><td style="text-align:center">
			<span style="color:red;"><!--%=errMsg%--></span>
			<div class="menuItemTitle">
				<input dojoType="dijit.form.RadioButton" type="radio" id="option_pickup" name="delivery_option" checked value="pickup"  onclick="selectDeliveryOption(this.value)"/>
			<label for="option_pickup">Pickup Order</label>
			</div>

			<div class="menuItemTitle">
			<input dojoType="dijit.form.RadioButton" type="radio" id="option_delivery" name="delivery_option" value="delivery" onclick="selectDeliveryOption(this.value)"/>
			<label for="option_delivery">Deliver Order to...</label>
			</div>
				<table id="delivery_info" style="width:100%;">
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
				</table>
			</td>
		</tr>
		</table>
			
		</fieldset>
<% } %>
		<span style="color:red;font:8pt verdana;">* Required</span>
		</form>

<% if( menuOrderBean.itemCount() > 0 ){ %>
		<%@ include file="../payment_methods/pos_client.jsp"%>
		<% if( menuOrderBean.isPayOnPickup() ){ %>
		<a data-role="button" id="submit_order" rel="external" data-ajax="false" onclick="submitForm()">Pay at Pickup</a>
<% }} %>
		</div>
	</div>
	</body>
</html>
