<%@ include file="page_header.jsp" %>

<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.*"%>
<%@ page import="com.genesys.webmenus.OrderItem"%>

<%
	String sItemId = request.getParameter("item");
	if( sItemId == null ){ throw new ServletException("Item id missing"); }
	String orderIndex = request.getParameter("order_index");
	String special_insrtuctions = "";
	int defaultAmount = 1;
	Set<String> selectedChoices = new HashSet<>();
	if (orderIndex != null) {
		OrderItem item = menuOrderBean.getItemByIndex(Integer.parseInt(orderIndex));
		special_insrtuctions = item.getSpecialInstructions();
		defaultAmount = item.getQuantity();
		selectedChoices = menuOrderBean.buildChoicesList(item.getOptions());
	}
%>

<!DOCTYPE html> 
<html>
<head>

<meta name="HandheldFriendly" content="true" />
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/mobile/jquery.mobile-1.1.1.min.js"></script>

</head>
<webmenusCfg:GuestUser>
<body>
<webmenusCfg:MenuItemSize itemSizeId="<%=sItemId%>">
<% boolean bMenuOpen = menuOrderBean.isWithinMenuOperatingHours(itemMenuId); %>
<div data-role="page" id="page-<%=sItemId%>">

	<div data-role="header">
		<h1><%=itemName%></h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
		<a data-rel="button" href="<%=rootMenuPath%>/menuview?menu=<%=itemMenuId%>">Back</a>
	</div>

	<div data-role="content">
	<form method="post" id="submitItem" action="<%=rootMenuPath%>/submit_item" rel="external" data-ajax="false">
<%
		if (orderIndex != null) {
%>
		<input type="hidden" name="order_index" value="<%=orderIndex%>"/>
<%
		}
%>
		<input type="hidden" name="sizeId" value="<%=itemSizeId%>"/>
<% if(itemImage.length() > 0){ %>
		<img style="float:right;padding:8px;max-width:200px" src="<%=request.getContextPath()%>/ImageViewer<%=itemImage%>" /> 
<% } %>
		<h3><%=itemName%></h3> 
		<p><%=itemDescription%></p>
		<p style="font-size:14pt;"><em><%=menuOrderBean.getCurrencyString(itemPrice)%></em></p>
<% if(bMenuOpen){ %>
		<span><b>Choose From Options:</b></span>
<% } %>
<webmenusCfg:EnumItemOptions itemId="<%=itemId%>">

	<fieldset data-role="controlgroup">
		<legend><%=optionName%>:</legend>
<%
String sOptionPrice = menuOrderBean.generateOptionPriceLabel(optionPrice);
String sOptionType = optionType;
if( sOptionType.equalsIgnoreCase("select") == true )
{
%>
	<webmenusCfg:EnumItemOptionChoices optionId="<%=optionId%>">
<%
	String sChoicePrice = menuOrderBean.generateOptionPriceLabel(choicePrice);
	String hash = optionName + "#" + choiceName;
	String selected = selectedChoices.contains(hash) ? "checked" : "";
	if(bMenuOpen){
%>
		<label><input data-mini="true" type="checkbox" <%=selected%> name="<%=optionName+choiceIdx%>" id="<%=optionName+choiceIdx%>"/><%=choiceName%><%=sChoicePrice%></label>
<%
	}else{
%>
		<div style="margin-left:8px;"><b><%=choiceName%><%=sChoicePrice%></b></div>
<%
	}
%>
	</webmenusCfg:EnumItemOptionChoices>
<%
}
else if( sOptionType.equalsIgnoreCase("select-one") == true )
{
%>
	<webmenusCfg:EnumItemOptionChoices optionId="<%=optionId%>">
<%
	String sChoicePrice = menuOrderBean.generateOptionPriceLabel(choicePrice);
	String hash = optionName + "#" + choiceName;
	boolean selected = (orderIndex == null) ? (Integer.parseInt(choiceIdx) == 0) : selectedChoices.contains(hash);
	String sSel = (selected) ? "checked" : "";
	if(bMenuOpen){
%>
		<label><input data-mini="true" data-mini="true" type="radio" <%=sSel%> name="<%=optionName%>" id="<%=choiceName+choiceIdx%>" value="<%=choiceName%>" /><%=choiceName%><%=sChoicePrice%></label>
<%
	}else{
%>
		<div style="margin-left:8px;"><b><%=choiceName%><%=sChoicePrice%></b></div>
<%
	}
%>
	</webmenusCfg:EnumItemOptionChoices>
<%
}
%>
	
	</fieldset>

</webmenusCfg:EnumItemOptions>
<%
String label = (orderIndex == null) ? "Add To Order" : "Update Order";
if(bMenuOpen){ %>
		<fieldset data-role="controlgroup">
			Quantity: <input data-mini="true" style="width:55px;display:inline-block;margin:0px;" type="number" name="quantity" id="quantity" value="1" min="1"/>
		</fieldset>
		<input type="submit" data-role="button" value="<%=label%>"/>
<% } %>
	</form>
	</div>

</div>
</webmenusCfg:MenuItemSize>
</body>
</webmenusCfg:GuestUser>
</html>