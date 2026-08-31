function messageBox(msg, title) {
    var content = msg + "<br/><br/><center><button dojoType=\"dijit.form.Button\" onclick=\"dijit.byId('msgbox').destroy();\">OK</button></center>";
    theDialog = new dijit.Dialog({id:"msgbox", title:title, content: content});
    dojo.body().appendChild(theDialog.domNode);
    theDialog.startup();
    theDialog.show();
}

function validateCreateParams() {
    var createPatronParams = orderCheckout.getElementsByTagName("input");
    var errs = [];
    for( i = 0; i < createPatronParams.length; i++ ) {
        var param = createPatronParams[i];
        if( !param.disabled && param.attributes["wmrequired"]?.value === "true" && param.value.length == 0 ) {
            errs.push("Required field '" + param.title + "' is missing.");
            param.style.backgroundColor = "#ffd4d4";
        } else {
            param.style.backgroundColor = "#ffffff";
        }
    }
    if (errs.length > 0) {
        var msg = "<ul><li>" + errs.join("</li><li>") + "</li></ul>";
        messageBox(msg, "Invalid Parameters");
        return false;
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
        dojo.byId("dijit_form_Button_0_label").textContent = "Pay at Pickup";
        break;
    case "delivery":
        dojo.byId("dijit_form_Button_0_label").textContent = "Pay on Delivery";
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