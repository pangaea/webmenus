function getOrderStatus(id, context) {
	// Make request to the server
    dojo.xhrGet({
        url: "/webmenus/MyOrder/getstatus",
        content: {
            id: id,
            r: Math.floor(Math.random() * (1000000))
        },
        load: function(response) {
            /* Success */
            //alert(response);
            var json = JSON.parse(response);
            var statusImage = document.getElementById("status_image");
            var statusLabel = document.getElementById("status_label");
            switch(parseInt(json.status, 10)) {
                case 0:
                    statusImage.src = context + "/app/images/orderstatus/download.gif";
                    statusLabel.innerText = "Order Submitted";
                    break;
                case 3:
                    statusImage.src = context + "/app/images/orderstatus/pan.gif";
                    statusLabel.innerText = "Being Prepared";
                    break;
                case 4:
                    statusImage.src = context + "/app/images/orderstatus/delivery-scooter.gif";
                    statusLabel.innerText = "Out for Delivery";
                    break;
                case 5:
                    statusImage.src = context + "/app/images/orderstatus/food-pickup.gif";
                    statusLabel.innerText = "Ready for Pickup";
                    break;
                case 6:
                    statusImage.src = context + "/app/images/orderstatus/verified.gif";
                    statusLabel.innerText = "Complete";
                    break;
            }

        },
        error: function(err){ /* Error */ }
    });

}
