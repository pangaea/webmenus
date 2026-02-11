/*
 * Globals
 */
var g_designMode = false;

function RandomNum()
{
	return ( Math.floor( Math.random() * (1000000) ) );
}

function startAnim()
{
	var animBody = document.createElement("div");
	animBody.innerHTML = "<table style='width:200px;'><tr><td align='center'><div>Loading, please hold...</div><div><img src='../images/wait30trans.gif'/></div></td></tr></table>";
	var animDlg = new dijit.Dialog({id:"wm_transistion_anim", title:"Loading..."}, animBody);
	animDlg.closeButtonNode.style.display = "none";
	dojo.body().appendChild(animDlg.domNode);
	animDlg.startup();
	animDlg.show();
}

function stopAnim()
{
	var animDlg = dijit.byId('wm_transistion_anim');
	if( animDlg != undefined )
		animDlg.destroy();
}

function showOrderDialog(url)
{
	closeOrderDialog();
	setTimeout("openOrderDialog('" + url + "')", 10);
}

function openOrderDialog(url)
{
	var dlgWidth = 800;//(document.body.clientWidth / 1.2);
	var dlgHeight = 400;//(document.body.clientHeight / 1.4);
	var txtContent = "<iframe width='" + dlgWidth + "px' height='" + dlgHeight + "px' src='" + url + "'></iframe>";
	var theDialog = new dijit.Dialog({
		id: "wm_modal_dialog",
		title: "My Order",
		content: txtContent,
		layout: "none"
	});
	dojo.body().appendChild(theDialog.domNode);
	theDialog.startup();
	theDialog.show();
	theDialog._underlay.hide();
}

function closeOrderDialog()
{
	var theDialog = dijit.byId("wm_modal_dialog");
	if( theDialog != undefined )
		theDialog.destroy();
}

function createMessageDialog()
{
    pane = document.createElement('div');

    // create the message place holder and add it to the dialog
    promptMessage = document.createElement('div');
    promptMessage.id = "promptMessage";
    pane.appendChild(promptMessage);
    
    // create the buttons and add them to the dialog
    buttons = document.createElement('div');
    buttons.setAttribute("style", "float:right");
    okButton = new dijit.form.Button({id: "ok", type: "submit", label: "OK"})
    buttons.appendChild(okButton.domNode);
    cancelButton = new dijit.form.Button({id: "cancel", type: "reset", label: "Cancel"});
    cancelButton.onClick = function(){
        dijit.byId('message').hide();
    };
    buttons.appendChild(cancelButton.domNode);
    pane.appendChild(buttons);
    
    // create the dialog and add it to domNode
    dialog = new dijit.Dialog({id: "message", style: "dispaly:none"}, pane);
    // dialogs MUST be added to document.body
    document.body.appendChild(dialog.domNode);
    dialog.startup();
    return dialog;
}

function message(/*string*/msg, /*string*/ title, /*function*/ execute)
{
    if (!title) {
        title = "Message";
    }
    dojo.byId('promptMessage').innerHTML = msg;
    dijit.byId('message').attr("title", title);
    // need to test this...
    if (execute) {
        dijit.byId('message').attr('execute', execute);
    }
    dijit.byId('message').show();
}

function loadMenu(type,id)
{
	var sReq = "../QueryMenuItems";
	sReq += "?" + type + "=" + id;
	sReq += "&r=" + Math.floor( Math.random() * (1000000) );
	$.ajax({
		type: "GET",
		url: sReq,
		dataType: "xml",
		contentType: "text/xml",
		cache: false,
		success: function(xml){drawMenu(xml);stopAnim();}
	});
}

function loadMenu__(type,id)
{
	var xmlhttp;
	if( window.XMLHttpRequest )			// IE7, Firefox, Safari,...
	{
		xmlhttp = new XMLHttpRequest();
		if( xmlhttp.overrideMimeType )
		{
			xmlhttp.overrideMimeType( "text/xml" );
		}
	}
	else if( window.ActiveXObject )	// IE6
	{
		try
		{
			xmlhttp = new ActiveXObject( "Msxml2.XMLHTTP" );
		}
		catch(e)
		{
			try
			{
				xmlhttp = new ActiveXObject( "Microsoft.XMLHTTP" );
			}
			catch(e)
			{
				return false;
			}
		}
	}
	else
	{
		return false;
	}
	var sReq = "../QueryMenuItems";
	sReq += "?" + type + "=" + id;
	sReq += "&r=" + RandomNum();
	xmlhttp.open("GET", sReq, true);
	xmlhttp.onreadystatechange = function()
	{
		if(xmlhttp.readyState == 4 && xmlhttp.status == 200)
		{
			drawMenu(xmlhttp.responseXML);
			stopAnim();
		}
	};
	xmlhttp.send(null);
}
function fireMenuEvent(id)
{
	window.setTimeout("loadMenu('menu','" + id + "')",10);
}
function fireCatEvent(id)
{
	window.setTimeout("loadMenu('cat','" + id + "')",10);
}

function orderDlgProc(param)
{
	if(param==1) return;
	
	// Place order here
}

function viewOrder()
{
	showOrderDialog("MenuView/vieworder?r=" + RandomNum());
}
function viewMenuItem(id, title)
{
	var url = "MenuView/menuitemview?id=" + id + "&r=" + RandomNum();
	showOrderDialog(url);
}

function drawMenu(xml)
{
	var element = document.getElementById("menuContent");
	if( element == null ) return;

	try
	{
		var oDoc = new XDocument();
		oDoc.attach(xml);
		
		var oErrCode = oDoc.selectNode("//error/code");
		if( oErrCode.isNull() == false && oErrCode.getText() == "1" )
		{
			message("Your session has timed out. Press OK to reload page.", "Error", function(){document.location.reload();});
			return;
		}
		
		var oMenu = oDoc.selectNode("//menu");
		var menuGen = new MenuGenerator();
		element.innerHTML = menuGen.createMarkup(oMenu);
		
		// Bind to order links
		$(".liveOrderItem").click(function(){
			viewMenuItem($(this).attr("data-id"),$(this).attr("data-name"));
			//  onclick='viewMenuItem("{{id}}","{{name}}"
		});
		
		////////////////////////////////////////////
		// DESIGNER
		if( g_designMode )
		{
			for( var i = 0; i < oCatList.getLength(); i++ )
			{
				if( typeof(jQuery) == "function" )	// In-case there is no jQuery
				{
					$("#sortable"+i).sortable();
					$("#sortable"+i).disableSelection();
				}
			}
		}
		///////////////////////////////////////////
	}
	catch(e)
	{
		alert(e);
	}

	//stopAnim();
}

function menuClick(id)
{
	if(id)
	{
		startAnim();
		fireMenuEvent(id);
	}
}

function catClick(id)
{
	if(id)
	{
		startAnim();
		fireCatEvent(id);
	}
}

dojo.addOnLoad( function()
{
	if(typeof(sFirstMenuId) != "undefined" && sFirstMenuId != null )
	{
		startAnim();
		fireMenuEvent(sFirstMenuId);
		createMessageDialog();
	}
	else{
		createMessageDialog();
		message("ERROR: This location has no menus defined.", "Error", function(){document.location.reload();});
	}
	
	if(typeof(init) != "undefined" && init != null )
	{
		init();
	}
});