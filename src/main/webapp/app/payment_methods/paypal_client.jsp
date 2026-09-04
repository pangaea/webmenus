<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%
	for( int idx = 0; idx < menuOrderBean.getPaymentMethodCount(); idx++ )
	{
		Integer type = menuOrderBean.getPMType(idx);
		switch (type) {
			case 1: // Paypal
				String clientId = menuOrderBean.queryPmConfig(idx, "CLIENT_ID");
				String clientSecret = menuOrderBean.queryPmConfig(idx, "CLIENT_SECRET");
				Boolean venmoSupport = menuOrderBean.queryPmConfig_Boolean(idx, "VENMO_SUPPORT");
				String venmoParam = "";
				if (venmoSupport) {
					venmoParam = "&enable-funding=venmo";
				}
%>

<script 
src="https://www.paypal.com/sdk/js?client-id=<%=clientId%>&components=buttons<%=venmoParam%>">
</script>
<div id="paypal-container-<%=clientId%>"></div>
<script>
paypal.Buttons({
    style: {
        layout: 'horizontal',
        color:  'blue',
        shape:  'rect',
        label:  'paypal'
    },

    onClick: function(data, actions) {
        const isFormValid = validateCreateParams();
        
        if (!isFormValid) {
            // Show local error message to user
            //document.querySelector('#error-msg').style.display = 'block';
            
            // Prevent the PayPal window from opening
            return actions.reject();
        } else {
            // Clear error and proceed with checkout
            //document.querySelector('#error-msg').style.display = 'none';
            return actions.resolve();
        }
    },

    // Call your server to create an order
    createOrder: function(data, actions) {
        if (validateCreateParams()) {
            return fetch('/webmenus/PayPalPortal/createOrder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    payment_index: "<%=idx%>"
                })
            }).then(function(response) {
                return response.json();
            }).then(function(orderData) {
                // Returns the order ID a.k.a. the approval URL
                return orderData.id;
            });
        }
        return 
    },

    // Call your server to capture the payment
    onApprove: function(data, actions) {
        return fetch('/webmenus/PayPalPortal/order/' + data.orderID + '/capture', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                payment_index: "<%=idx%>",
                email: document.getElementsByName('email')[0].value,
                firstname: document.getElementsByName('firstname')[0].value,
                lastname: document.getElementsByName('lastname')[0].value,
                phone_num: document.getElementsByName('phone_num')[0].value,
                delivery_option: document.getElementById('option_delivery').checked ?
                    document.getElementsByName('delivery_option')[1].value :
                    document.getElementsByName('delivery_option')[0].value,
                address: document.getElementsByName('address')[0].value,
                city: document.getElementsByName('city')[0].value,
                state: document.getElementsByName('state')[0].value,
                zip: document.getElementsByName('zip')[0].value,
                contact_number:document.getElementsByName('contact_number')[0].value
            })
        }).then(function(response) {
            return response.json();
        }).then(function(orderData) {
            // Redirect to a success page or update UI
            window.parent.document.getElementById("orderPanel").contentWindow.location.reload();
            window.location.href = '<%=request.getContextPath()%>/app/my_order.jsp?loc=<%=menuOrderBean.getCurrentLocationId()%>&id=' + orderData.order_id;
        });
    },

    // Handle errors or cancellations
    onCancel: function(data) {
        console.log('Payment cancelled', JSON.stringify(data, 0, 2));
    },

    onError: function(err) {
        console.error('PayPal error', err);
        alert('An error occurred during the transaction. Please try again.');
    }

}).render('#paypal-container-<%=clientId%>');

</script>
<%
			    break;
		}
	}
%>