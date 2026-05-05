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

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Order Dashboard</title>
        <link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/order_dashboard.css"></link>
        <script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/order_dashboard.js"></script>
        <script>
            // var orders = [{
            //     id: "123456",
            //     status: "open",
            //     label: "Sausage, egg, cheese",
            //     invoice: "A-1231",
            //     delivery: false
            // },{
            //     id: "4564567",
            //     status: "complete",
            //     label: "Ribeye Steak(2), baked potatoes(2), ceasar salad",
            //     invoice: "A-4564",
            //     delivery: true
            // },{
            //     id: "786876",
            //     status: "inprogress",
            //     label: "Grilled cheese, chickan soup",
            //     invoice: "A-9806",
            //     delivery: false
            // },{
            //     id: "13545",
            //     status: "inprogress",
            //     label: "Hamburger, french fries, chicken sandwich",
            //     invoice: "A-4444",
            //     delivery: true
            // }];

            window.addEventListener("load", () => {
                loadOrders();
            });
        </script>
    </head>
    <body>
      <template id="order-template">
        <div class="task" id="{id}" draggable="true" ondragstart="drag(event)" onclick="select(id)">
          <table>
            <tr><td><b>Details:</b></td><td>{label}</td></tr>
            <tr><td><b>Invoice:</b></td><td>{invoice}</td></tr>
            <tr><td><b>Delivery:</b></td><td>{delivery}</td></tr>
          </table>
        </div>
      </template>

      <div class="kanban-board">
        <div class="column" id="open" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>On Deck</h3>
            <div class="task-list" id="open-tasks"></div>
        </div>

        <div class="column" id="inprogress" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>Fire</h3>
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
            <h3>Complete</h3>
            <div class="task-list" id="complete-tasks"></div>
        </div>
      </div>
    </body>
</html>