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
    updateOrder(data, targetList.id.slice(0, -6));
}

function select(ev) {
    console.log("Selected id => " + ev);
    window.location = "order.html?id=" + ev;
}

function loadOrders() {
    $.ajax({
		type: "GET",
		url: "/webmenus/OrderDashboard/getorders",
		dataType: "json",
		success: function(json){
			initKanban(json);
		}
	});
}

function updateOrder(id, status, invoice) {
    $.ajax({
		type: "POST",
		url: "/webmenus/OrderDashboard/updateorder/" + id,
		dataType: "json",
        data: JSON.stringify({ status: status, invoice: invoice }),
		success: function(json){
			loadOrders();
		}
	});
}

function initKanban(orders) {
    const openDiv = document.getElementById('open-tasks');
    const inprogressDiv = document.getElementById('inprogress-tasks');
    const readyforpickupDiv = document.getElementById('readyforpickup-tasks');
    const outfordeliveryDiv = document.getElementById('outfordelivery-tasks');
    const completeDiv = document.getElementById('complete-tasks');
    var orderHTML = document.querySelector('#order-template').innerHTML;
    orders.forEach(o => {
        // Create order DIV
        const order = document.createElement('div');
        order.innerHTML = orderHTML.replace("{id}", o.id)
                                    .replace("{label}", o.label)
                                    .replace("{invoice}", o.invoice)
                                    .replace("{delivery}", o.delivery);
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