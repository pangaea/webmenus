<!--
Copyright (c) 2026 Kevin Jacovelli
All Rights Reserved
-->

<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%	
	// Validate session
	if( request.getSession().getAttribute( "ticket" ) == null )
	{
%>
		<html>
			<head>
				<script type="text/javascript">
				function backtologin()
				{
					window.top.location = "<%=request.getContextPath()%>/ui/login.jsp?backurl=" + escape("<%=request.getRequestURL()%>?<%=request.getQueryString()%>")  + "&err=" + escape("Your session time out. Please login again.");
				}
				var gIndex = 0;
				</script>
			</head>
			<body onload="backtologin();"><em>Your session has timed out...</em></body>
		</html>
<%
		return;
	}
%>

<jsp:useBean id="orderDashboardBean" class="com.genesys.webmenus.orders.OrderDashboardBean" scope="page"/>
<jsp:setProperty name="orderDashboardBean" property="*"/> 
<%
orderDashboardBean.setRequest(request); // pass request object to bean
orderDashboardBean.loadLocationDetails(request.getParameter("loc"));
%>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Order Dashboard</title>
        <link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/order_dashboard.css"></link>
        <!-- Default theme includes -->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/curvey-2.css"></link>
        <link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/redmond/jquery-ui-1.7.2.custom.css" />

        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xlibs/jquery/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/fancybox/jquery.fancybox-1.3.4.pack.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/datetime_utils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/order_dashboard.js"></script>
        <script type="text/javascript">
            currentLocationId = "<%=request.getParameter("loc")%>";
            window.addEventListener("load", () => {
                loadOrders();
                window.setInterval(() => { loadOrders() }, 30000);
            });
            document.addEventListener('reLoadOrder', (e) => {
                loadOrders();
            });
        </script>
    </head>
    <body>

      <div style="height:60px;">
        <h2 style="padding-top:10px;"><%=orderDashboardBean.getName()%></h2>
        <div style="position:absolute;right:10px;top:10px;">
          <div><%=orderDashboardBean.getAddress()%></div>
          <div><%=orderDashboardBean.getCity()%>, <%=orderDashboardBean.getState()%> <%=orderDashboardBean.getZip()%></div>
          <div><%=orderDashboardBean.getPhone()%></div>
        </div>
      </div>
      
      <hr/>

      <template id="order-template">
        <div class="task" id="{id}" draggable="true" ondragstart="drag(event)" onclick="select(id)">
          <table class="order_card">
            <tr><td><b>Details:</b></td><td>{label}</td></tr>
            <tr><td><b>Invoice:</b></td><td class="invoice">{invoice}</td></tr>
            <tr><td><b>Delivery:</b></td><td><img class="delivery_image" src="{delivery}"></img></td></tr>
            <tr><td><b>Due:</b></td><td class="estimated-time">{estimated_time_label}</td></tr>
          </table>
        </div>
      </template>

      <div class="kanban-board">
        <div class="column" id="open" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>On Deck</h3>
            <div class="task-list" id="open-tasks"></div>
        </div>

        <div class="column" id="inprogress" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>In the Kitchen</h3>
            <div class="task-list" id="inprogress-tasks"></div>
        </div>

        <div class="column" id="readyforpickup" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>Order Up</h3>
            <div class="task-list" id="readyforpickup-tasks"></div>
        </div>

        <div class="column" id="outfordelivery" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>Out for Delivery</h3>
            <div class="task-list" id="outfordelivery-tasks"></div>
        </div>

        <div class="column" id="complete" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>Complete (within last 8 hours)</h3>
            <div class="task-list" id="complete-tasks"></div>
        </div>
      </div>
    </body>
</html>