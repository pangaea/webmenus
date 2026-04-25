function messageBox(msg, title)
{
    var content = msg + "<br/><br/><center><button dojoType=\"dijit.form.Button\" onclick=\"dijit.byId('msgbox').destroy();\">OK</button></center>";
    theDialog = new dijit.Dialog({id:"msgbox", title:title, content: content});
    dojo.body().appendChild(theDialog.domNode);
    theDialog.startup();
    theDialog.show();
}

function validateCreateParams()
{
    var createPatronParams = orderCheckout.getElementsByTagName("input");
    for( i = 0; i < createPatronParams.length; i++ )
    {
        var param = createPatronParams[i];
        if( param.WMrequired == "true" && param.value.length == 0 )
        {
            messageBox("Required field '" + param.title + "' is missing.", "Invalid Parameter");
            param.focus();
            return false;
        }
    }
    return true;
}

function validateParams()
{
    var optionDeliveryRB = dojo.byId("option_delivery");
    if( optionDeliveryRB.checked )
    {
        var prevDelivInfoIN = dojo.byId("previous_deliveries");
        if( prevDelivInfoIN.value == "0" )
        {
            var deliveryOptionsParams = deliveryOptions.getElementsByTagName("input");
            for( i = 0; i < deliveryOptionsParams.length; i++ )
            {
                var param = deliveryOptionsParams[i];
                if( param.WMrequired == "true" && param.value.length == 0 )
                {
                    messageBox("Required field '" + param.title + "' is missing.", "Invalid Parameter");
                    param.focus();
                    return false;
                }
            }

            // Build delivery information
            var addressIN = dojo.byId("address");
            var cityIN = dojo.byId("city");
            var stateIN = dojo.byId("state");
            var zipIN = dojo.byId("zip");
            var contactNumberIN = dojo.byId("contact_number");
            var oTextArea = document.getElementById("delivery_info");
            oTextArea.innerText = addressIN.value + " / " + cityIN.value + ", " + stateIN.value + " " + zipIN.value + " / " + contactNumberIN.value;
        }
    }
    
    return true;
}
function selectDeliveryOption(sel)
{
    var bDisabled = false;
    switch(sel)
    {
    case "pickup":
        bDisabled = true;
        break;
    case "delivery":
        bDisabled = false;
        break;
    }
    disableControls(bDisabled);

    var prevAddrIN = dojo.byId("previous_deliveries");
    prevAddrIN.disabled = bDisabled;
}
function disableControls(disabled)
{
    var bDisabled = disabled;
    var sClass = "";
    if(disabled) sClass = "enabled_no";
    else sClass = "enabled_yes";

    var addressIN = dojo.byId("address");
    addressIN.disabled = bDisabled;
    addressIN.className = sClass;
    
    var cityIN = dojo.byId("city");
    cityIN.disabled = bDisabled;
    cityIN.className = sClass;
    
    var stateIN = dojo.byId("state");
    stateIN.disabled = bDisabled;
    stateIN.className = sClass;
    
    var zipIN = dojo.byId("zip");
    zipIN.disabled = bDisabled;
    zipIN.className = sClass;
    
    var contactNumberIN = dojo.byId("contact_number");
    contactNumberIN.disabled = bDisabled;
    contactNumberIN.className = sClass;
}
function fillPrevAddr(id)
{
    var oDiv = document.getElementById("addr_" + id);
    var oTextArea = document.getElementById("delivery_info");
    oTextArea.innerText = oDiv.innerText;
}
function selectDeliverAddr(oSelect)
{
    if( oSelect.value == "0" )
    {
        disableControls(false);
    }
    else
    {
        fillPrevAddr(oSelect.value);
        disableControls(true);
    }
}