<!--
Copyright (c) 2026 Kevin Jacovelli
All Rights Reserved
-->

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

<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Order Dashboard</title>
        <link rel="icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="<%=request.getContextPath()%>/favicon.ico" type="image/x-icon" />

        <style>
            .kanban-board {
            display: flex;
            gap: 20px;
            padding: 20px;
            background-color: #f4f4f9;
            }

            .column {
            flex: 1;
            background-color: #ebedf0;
            border-radius: 5px;
            padding: 10px;
            min-height: 400px;
            }

            .task {
            background-color: white;
            margin: 10px 0;
            padding: 15px;
            border-radius: 3px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.12);
            cursor: grab;
            }

            .task:active {
            cursor: grabbing;
            }
        </style>
        <script>
            // Allow elements to be dropped into this zone
            function allowDrop(ev) {
            ev.preventDefault();
            }

            // Store the ID of the element being dragged
            function drag(ev) {
            ev.dataTransfer.setData("text", ev.target.id);
            }

            // Move the dragged element to the new column
            function drop(ev) {
            ev.preventDefault();
            const data = ev.dataTransfer.getData("text");
            const draggedElement = document.getElementById(data);
            
            // Find the closest task-list container in the drop target
            const targetList = ev.target.closest('.column').querySelector('.task-list');
            targetList.appendChild(draggedElement);
            
            // Optional: Save state to localStorage here
            saveBoardState();
            }
        </script>
    </head>
    <body>
        <div class="kanban-board">
        <!-- Column 1: To Do -->
        <div class="column" id="todo" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>To Do</h3>
            <div class="task-list" id="todo-tasks">
            <div class="task" id="task1" draggable="true" ondragstart="drag(event)">Task 1</div>
            <div class="task" id="task2" draggable="true" ondragstart="drag(event)">Task 2</div>
            </div>
        </div>

        <!-- Column 2: In Progress -->
        <div class="column" id="inprogress" ondrop="drop(event)" ondragover="allowDrop(event)">
            <h3>In Progress</h3>
            <div class="task-list" id="inprogress-tasks"></div>
        </div>
        </div>

    </body>
</html>