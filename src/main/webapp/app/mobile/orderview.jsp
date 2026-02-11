<%@ include file="page_header.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.genesys.webmenus.*"%>
<%@ page import="java.math.BigDecimal"%>

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
$(function(){
	$("input[type=number]").live("change", function(){
		if( $(this).val().length > 0 ){
			window.setTimeout(function(){ $("#orderForm").submit();	}, 100);
		}
	})
	$("a.remove_item").live("click", function(){
		var target_count = $(this).attr("item_id")
		$("#"+target_count).val("0");
		window.setTimeout(function(){ $("#orderForm").submit();	}, 100);
	})
});
</script>

</head>

	<body>
	<div data-role="page" id="orderview">
	
	<div data-role="header">
		<h1>Place Order</h1>
		<a data-rel="button" href="<%=rootMenuPath%>">Home</a>
<% if( menuOrderBean.itemCount() > 0 ){ %>
		<a data-rel="button" rel="external" data-ajax="false" href="<%=rootMenuPath%>/submit_order">Submit</a>
<% } %>
	</div><!-- /header -->
	
	<div data-role="content">
	
	
	
		<form method="POST" id="orderForm" action="<%=rootMenuPath%>/orderview">
		<table id="itemTable" cellpadding="2" class="orderTable" style="width:100%;">

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
				<td valign="top" colspan="3">

						<div class='menuItemTitle'>
						<%=item.getName()%>
						<% if( item.getSize().length() > 0 ){ %>
							(<%=item.getSize()%>)
						<% } %>
						</div>

						<div class='menuOptions'>
							<%=item.getOptions().replaceAll("\n", "</br>")%>
						</div>

				</td>
				</tr>
				<!--tr>
					<td valign="top" class="optionTitle">Each</td>
					<td valign="top" class="optionTitle">Quantity</td>
					<td valign="top" class="optionTitle">Price</td>
				</tr-->
				<tr>
				<td><%=item.getPriceStr()%></td>
				<td>
					X&nbsp;<input data-mini="true" style="width:55px;display:inline-block;margin:0px;" type="number" name="<%=item.getId()%>" id="quantity<%=i%>" value="<%=item.getQuantity()%>" min="0" />
					&nbsp;<a href="javascript:void(0)" class="remove_item" item_id="quantity<%=i%>">remove</a>
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
		</form>
<% if( menuOrderBean.itemCount() > 0 ){ %>
		<a data-role="button" rel="external" data-ajax="false" href="<%=rootMenuPath%>/submit_order">Submit Order</a>
<% } %>
		</div>
	</div>
	</body>
</html>
