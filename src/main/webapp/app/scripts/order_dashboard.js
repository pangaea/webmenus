var currentLocationId = null;

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
    console.log("order id = " + data + ", status=" + targetList.id.slice(0, -6))
    updateOrderStatus(data, targetList.id.slice(0, -6), () => {
        loadOrders();
    });
}

function select(ev) {
    console.log("Selected id => " + ev);
    //showDialog("/webmenus/app/orders/order_details.jsp?id=" + ev, "Order Details", 1280);

    if($("#order_detail_link-" + ev).size()==0)
		$("<a id='order_detail_link-" + ev + "' href='/webmenus/app/orders/order_details.jsp?id=" + ev + "'/>").insertAfter( $("body") );
	$("#order_detail_link-" + ev).fancybox({
        'width' : '12',
        'height' : '6',
        'autoScale' : false,
        'transitionIn' : 'none',
        'transitionOut' : 'none',
        'type' : 'iframe'
    }).click();
}

function loadOrders() {
    $.ajax({
		type: "GET",
		url: "/webmenus/OrderDashboard/getorders/" + currentLocationId,
		dataType: "json",
		success: function(json){
			initKanban(json);
		}
	});
}

function updateOrderStatus(id, status, callback) {
    $.ajax({
		type: "POST",
		url: "/webmenus/OrderDashboard/updateorder/" + id,
		dataType: "json",
        data: JSON.stringify({ status: status }),
		success: function(json){
			//loadOrders();
            callback();
		}
	});
}

function updateOrder(id, status, invoice, estimated_time, callback) {
    $.ajax({
		type: "POST",
		url: "/webmenus/OrderDashboard/updateorder/" + id,
		dataType: "json",
        data: JSON.stringify({ status: status, invoice: invoice, estimated_time: estimated_time }),
		success: function(json){
			//loadOrders();
            callback();
		}
	});
}

function getColumn(name) {
    const div = document.getElementById(name);
    div.replaceChildren();
    return div;
}

function initKanban(orders) {
    const openDiv = getColumn('open-tasks');
    const inprogressDiv = getColumn('inprogress-tasks');
    const readyforpickupDiv = getColumn('readyforpickup-tasks');
    const outfordeliveryDiv = getColumn('outfordelivery-tasks');
    const completeDiv = getColumn('complete-tasks');
    var orderHTML = document.querySelector('#order-template').innerHTML;
    orders.forEach(o => {
        // Create order DIV
        const time_diff = (o.estimated_time != null && o.estimated_time.length > 0) ? timeDiffInMinutes(o.estimated_time) : null;
        const order = document.createElement('div');
        order.innerHTML = orderHTML.replace("{id}", o.id)
                                    .replace("{label}", o.label)
                                    .replace("{invoice}", o.invoice)
                                    .replace("{delivery}", (o.delivery) ? "/webmenus/app/images/close.png" : "/webmenus/app/images/checkmark.png")
                                    .replace("{estimated_time_label}", convertToLabel(time_diff));

        if (time_diff != null) {
            if (time_diff < 0) {
                order.getElementsByClassName("estimated-time")[0].className = "late";
            } else {
                order.getElementsByClassName("estimated-time")[0].className = "early";
            }
        } else {
            order.getElementsByClassName("estimated-time")[0].className = "";
        }

        switch(o.status) {
        case "open":
            openDiv.appendChild(order);
            break;
        case "inprogress":
            inprogressDiv.appendChild(order);
            break;
        case "readyforpickup":
            readyforpickupDiv.appendChild(order);
            break;
        case "outfordelivery":
            outfordeliveryDiv.appendChild(order);
            break;
        case "complete":
            completeDiv.appendChild(order);
            break;
        }
    });
}