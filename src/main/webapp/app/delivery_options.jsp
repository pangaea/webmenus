<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*"%>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
%>
<html>
	<head>
		<title>Patron Login</title>
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
		<!--link rel="stylesheet" type="text/css" href="../styles/menu_view.css"></link-->
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";
		</style>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript">
		dojo.require("dijit.form.Button");
		dojo.require("dijit.Dialog");
		dojo.require("dojo.parser");
		dojo.require("dijit.form.CheckBox");
		</script>
	</head>
	<body class="tundra" onload="selectDeliveryOption('pickup')">
	<%
		String errMsg = request.getParameter("msg");
		if( errMsg == null ) errMsg = new String("");
	%>
	
	<form id="deliveryOptions" method="post" action="<%=request.getContextPath()%>/app/ordersubmit.jsp">
	<table style="width:100%"><tr><td style="text-align:center">
		<span style="color:red;"><%=errMsg%></span>
		<div class='menuTitle'>Select Delivery Options</div>
		<br/>
		<div class="menuItemTitle">
			<input dojoType="dijit.form.RadioButton" type="radio" id="option_pickup" name="delivery_option" checked value="pickup"  onclick="selectDeliveryOption(this.value)"/>Pickup Order
		</div>

		<table class="pageSeperator"><tr>
			<td width="100%"><hr width="100%"/></td>
		</tr></table>

		<script type="text/javascript">
		function messageBox(msg, title)
		{
			var content = msg + "<br/><br/><center><button dojoType=\"dijit.form.Button\" onclick=\"dijit.byId('msgbox').destroy();\">OK</button></center>";
			theDialog = new dijit.Dialog({id:"msgbox", title:title, content: content});
			dojo.body().appendChild(theDialog.domNode);
			theDialog.startup();
			theDialog.show();
		}

		function validateParams()
		{
			var optionDeliveryRB = dojo.byId("option_delivery");
			if( optionDeliveryRB.checked )
			{
				var prevDelivInfoIN = dojo.byId("previous_deliveries");
				if( prevDelivInfoIN.value == "0" )
				{
					var deliveryOptionsParams = deliveryOptions.getElementsByTagName("input");
					for( i = 0; i < deliveryOptionsParams.length; i++ )
					{
						var param = deliveryOptionsParams[i];
						if( param.WMrequired == "true" && param.value.length == 0 )
						{
							messageBox("Required field '" + param.title + "' is missing.", "Invalid Parameter");
							param.focus();
							return false;
						}
					}

					// Build delivery information
					var addressIN = dojo.byId("address");
					var cityIN = dojo.byId("city");
					var stateIN = dojo.byId("state");
					var zipIN = dojo.byId("zip");
					var contactNumberIN = dojo.byId("contact_number");
					var oTextArea = document.getElementById("delivery_info");
					oTextArea.innerText = addressIN.value + "\r\n" + cityIN.value + ", " + stateIN.value + " " + zipIN.value + "\r\n" + contactNumberIN.value + "\r\n";
				}
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

			var prevAddrIN = dojo.byId("previous_deliveries");
			prevAddrIN.disabled = bDisabled;
		}
		function disableControls(disabled)
		{
			var bDisabled = disabled;
			var sClass = "";
			if(disabled) sClass = "enabled_no";
			else sClass = "enabled_yes";

			var addressIN = dojo.byId("address");
			addressIN.disabled = bDisabled;
			addressIN.className = sClass;
			
			var cityIN = dojo.byId("city");
			cityIN.disabled = bDisabled;
			cityIN.className = sClass;
			
			var stateIN = dojo.byId("state");
			stateIN.disabled = bDisabled;
			stateIN.className = sClass;
			
			var zipIN = dojo.byId("zip");
			zipIN.disabled = bDisabled;
			zipIN.className = sClass;
			
			var contactNumberIN = dojo.byId("contact_number");
			contactNumberIN.disabled = bDisabled;
			contactNumberIN.className = sClass;
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
		<div class="menuItemTitle">
		<input dojoType="dijit.form.RadioButton" type="radio" id="option_delivery" name="delivery_option" value="delivery" onclick="selectDeliveryOption(this.value)"/>Deliver Order to...
		
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
		<select id="previous_deliveries" style="width:250px;" onchange="selectDeliverAddr(this)">
		<option value="0">[New Address]</option>
	<%
		itr = addrs.iterator();
		i = 1;
	    while(itr.hasNext()){
	    	String body = (String)itr.next();
	%>
			<option value="<%=Integer.toString(i)%>" title="<%=body%>">
	    	<%=body%>
	    	</option>
	<%
			i++;
		}
	%>
		</select>
		<br/><br/>
		</div>
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
				<tr><td valign="middle" colspan="3" style="text-align:center;">
					<button dojoType="dijit.form.Button">
						Submit Order
						<script type="dojo/method" event="onClick">
						if( validateParams() )
							deliveryOptions.submit();
					</script>
					</button>
				</td><td/></tr>
			</table>
	</td></tr></table>
	</form>
	</body>
</html>
