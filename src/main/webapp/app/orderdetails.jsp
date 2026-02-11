<!--
Copyright (c) 2004-2011 Kevin Jacovelli
All Rights Reserved
-->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<script type="text/javascript">
function backtologin()
{
	window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
}
</script>
<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
		//response.sendRedirect( "login.jsp" ); return;
%>
		<html>
			<head></head>
			<body onload="backtologin();"><em>Your session has timed out...</em></body>
		</html>
<%
		return;
	}
%>



<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Order Details</title>
<link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>

<link rel="Stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/theme/ui.all.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/start/jquery-ui-1.7.2.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>

</head>

	<body>
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

	</body>
</html>
