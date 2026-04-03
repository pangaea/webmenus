function eventHandler( eventid )
{
	var sel_id = getSelID( "-1" );
	if(sel_id == "-1" ){
		alert("Please selection a payment method.");
		return;
	}
	
	switch(eventid)
	{
	case 1:
		OpenPaypalSettings(sel_id);
		break;
	}
	return 0;
}

function OpenPaypalSettings( id )
{
	if($("#paypal_form_link-" + id).size()==0)
		$("<a id='paypal_form_link-" + id + "' href='../app/payment_methods/paypal_form.jsp?id=" + id + "'/>").insertAfter( $("body") );
	$("#paypal_form_link-" + id).fancybox({
	    'width' : 940,
	    'height' : 570,
	    'transitionIn' : 'none',
	    'transitionOut' : 'none',
	    'hideOnOverlayClick' : false,
	    'type' : 'iframe'
    }).click();
}