<%@ include file="page_header.jsp" %>

<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.text.*"%>
<%@ page import="com.genesys.SystemServlet"%>

<%
	String sItemId = request.getParameter("item");
	if( sItemId == null ){ throw new ServletException("Item id missing"); }
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
<% boolean bMenuOpen = menuOrderBean.isWithinMenuOpertingHours(itemMenuId); %>
<div data-role="page" id="page-<%=sItemId%>">

	<div data-role="header">
		<h1><%=itemName%></h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
		<a data-rel="back">Back</a>
	</div>

	<div data-role="content">
	<form method="post" id="submitItem" action="<%=rootMenuPath%>/submit_item" rel="external" data-ajax="false">
		<input type="hidden" name="sizeId" value="<%=itemSizeId%>"/>
<% if(itemImage.length() > 0){ %>
		<img style="float:right;padding:8px;" src="<%=request.getContextPath()%>/ImageViewer<%=itemImage%>" /> 
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
String sOptionPrice = new String("");
if( optionPrice.length() > 0 )
{
	BigDecimal bdOptionPrice = new BigDecimal(optionPrice);
	if( bdOptionPrice.doubleValue() > 0.00 )
	{
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
		sOptionPrice = "<em> (add " + n.format(bdOptionPrice) + ")</em>";
	}
}
String sOptionType = optionType;
if( sOptionType.equalsIgnoreCase("select") == true )
{
	String sData = optionData;
	String results[] = sData.trim().split("\n");
	for(int ii =0; ii < results.length; ii++)
	{
		String sOptionTxt = results[ii].trim();
		if( sOptionTxt.length() == 0 ) continue;
		if(bMenuOpen){ %>
			<label><input data-mini="true" type="checkbox" name="<%=optionName+ii%>" id="<%=optionName+ii%>"/><%=sOptionTxt%><%=sOptionPrice%></label>
<%
		}
		else{
%>
			<div style="margin-left:8px;"><b><%=sOptionTxt%><%=sOptionPrice%></b></div>
<%
		}
	}
}
else if( sOptionType.equalsIgnoreCase("select-one") == true )
{
	if(bMenuOpen){ %>
			<input data-mini="true" type="radio" name="<%=optionName%>" id="_<%=optionName%>_" value="_none_" checked="checked" />
     		<label for="_<%=optionName%>_">No Thanks</label>
<%
	}
	String sData = optionData;
	String results[] = sData.split("\n");
	for(int ii =0; ii < results.length; ii++)
	{
		if(bMenuOpen){ %>
			<input data-mini="true" data-mini="true" type="radio" name="<%=optionName%>" id="<%=optionName+ii%>" value="<%=results[ii].trim()%>" />
     		<label for="<%=optionName+ii%>"><%=sOptionPrice%></label>
<%
		}
		else{
%>
			<div style="margin-left:8px;"><b><%=results[ii].trim()%><%=sOptionPrice%></b></div>
<%
		}
	}
}
else if( sOptionType.equalsIgnoreCase("text") == true )
{
	if(bMenuOpen){ %>
			<textarea data-mini="true" name="<%=optionName%>" style="width:100%;height:80px"></textarea>
<%
	}
}
%>
		</fieldset>

</webmenusCfg:EnumItemOptions>
<% if(bMenuOpen){ %>
		<fieldset data-role="controlgroup">
			Quantity: <input data-mini="true" style="width:55px;display:inline-block;margin:0px;" type="number" name="quantity" id="quantity" value="1" min="1"/>
		</fieldset>
		<input type="submit" data-role="button" value="Add To Order"/>
<% } %>
	</form>
	</div>

</div>
</webmenusCfg:MenuItemSize>
</body>
</webmenusCfg:GuestUser>
</html>