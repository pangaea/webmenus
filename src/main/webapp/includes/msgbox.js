// D I A L O G   A P I /////////////////
//$.ui.dialog.defaults.bgiframe = true;
function showDialog(url, title, width, height)
{
	try
	{		
		if( $("#sysDialogBox").size() == 0 )
			$("body").append("<div id='sysDialogBox' title='Modal Dialog'>");

		closeDialog();
		
		
		var dlgWidth = (width!=null)?width:document.body.clientWidth / 1.2;
		var dlgHeight = (height!=null)?height:document.body.clientHeight / 1.2;
		//$("#sysDialogBox").html("<iframe height='" + dlgHeight + "px' src='" + url + "' style='width:" + dlgWidth + "px;overflow-y:scroll;'></iframe>");
		
		$("#sysDialogBox").html("<iframe src='" + url + "' style='width:100%;height:99%;overflow-y:scroll;'></iframe>");
		
		$("#sysDialogBox").dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		})
		.dialog("option", "title", title)
		.dialog("option", "width", dlgWidth + 20)
		.dialog("option", "height", dlgHeight + 20)
		.dialog("option", "zIndex", 99999)
		//.dialog("option", "height", dlgHeight + 80)
		//.dialog("option", "buttons", {"Close": function(){$(this).dialog("close").remove();}})
		.dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function showDialogNoFrame(url, title, width, height)
{
	try
	{
		if( $("#sysDialogBox").size() == 0 )
			$("body").append("<div id='sysDialogBox' title='Modal Dialog'>");

		closeDialog();
		
		
		var dlgWidth = (width!=null)?width:document.body.clientWidth / 1.2;
		var dlgHeight = (height!=null)?height:document.body.clientHeight / 1.2;
		$("#sysDialogBox").html("<div id='sysDialogBoxBody' style='width:100%;height:100%;overflow-y:scroll;'></div>");
		$.ajax({
			type: "GET",
			url: url,
			dataType: "html",
			success: function(html){
				$("#sysDialogBoxBody").html(html);
			}
		});
		////////////////////////////////
		
		$("#sysDialogBox").dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		})
		.dialog("option", "title", title)
		.dialog("option", "width", dlgWidth + 20)
		.dialog("option", "height", dlgHeight + 20)
		.dialog("option", "zIndex", 99999)
		//.dialog("option", "height", dlgHeight + 80)
		//.dialog("option", "buttons", {"Close": function(){$(this).dialog("close").remove();}})
		.dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function showDialogEx(html, title, width, height)
{
	try
	{
		if( $("#sysDialogBox").size() == 0 )
			$("body").append("<div id='sysDialogBox' title='Modal Dialog'>");

		closeDialog();
		var dlgWidth = (width!=null)?width:document.body.clientWidth / 1.2;
		var dlgHeight = (height!=null)?height:document.body.clientHeight / 1.2;
		//$("#sysDialogBox").html("<div style='width:" + dlgWidth + "px;height:" + (dlgHeight-20) + "px;overflow-y:scroll;'>" + html + "</div>");
		$("#sysDialogBox").html("<div style='width:100%;height:100%;overflow-y:scroll;'>" + html + "</div>");
		//$("#sysDialogBox").html(html);
		$("#sysDialogBox").dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		})
		.dialog("option", "title", title)
		.dialog("option", "width", dlgWidth + 20)
		.dialog("option", "height", dlgHeight + 20)
		.dialog("option", "zIndex", 99999)
		//.dialog("option", "height", dlgHeight + 80)
		//.dialog("option", "buttons", {"Close": function(){$(this).dialog("close").remove();}})
		.dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function showDialogBasic(id, title, width, height)
{
	try
	{
		$("#"+id).dialog("close");
		var dlgWidth = (width!=null)?width:document.body.clientWidth / 1.2;
		var dlgHeight = (height!=null)?height:document.body.clientHeight / 1.2;
		$("#"+id).dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		})
		.dialog("option", "title", title)
		.dialog("option", "width", dlgWidth + 20)
		.dialog("option", "height", dlgHeight + 20)
		.dialog("option", "zIndex", 99999)
		.dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function closeDialog()
{
	$("#sysDialogBox").dialog("close");
}
function startAnimation()
{
	try
	{
		if( $("#sysAnimation").size() == 0 )
			$("body").append("<div id='sysAnimation' title='Please Wait'>");
		// Run "loading" animation - also, disable all controls to prevent re-submitting
		$("#sysAnimation").html("<div id='dialog'><table style='width:200px;'><tr><td style='text-align:center;' valign='middle'><div>Loading, please hold...</div></td></tr><tr><td style='text-align:center;'><div><img src='wait30trans.gif'/></div></div></td></tr></table></div>");
		//var holdDlg = $("<div id='dialog'><table style='width:200px;'><tr><td style='text-align:center;'><div>Loading, please hold...</div></td></tr><tr><td style='text-align:center;'><div><img src='images/wait30trans.gif'/></div></div></td></tr></table></div>");
		//holdDlg.appendTo(document.body)
		$("#sysAnimation").dialog( {width: 280, height: 200, resizable: false, title: 'Loading...', modal: true} );
		$("#sysAnimation").dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function stopAnimation()
{
	$("#sysAnimation").dialog("close");
}
function msgBoxAlert(msg, title)
{
	messageBox(msg, title, {"Ok": function(){$(this).dialog("close");}});
}
function messageBox(msg, title, buttons, width, height)
{
	var dlgWidth = (width!=null)?width:500;
	var dlgHeight = (height!=null)?height:300;
	
	if( $("#sysMessageBox").size() == 0 )
		$("body").append("<div id='sysMessageBox' title='Modal Dialog'>");

	try
	{
		$("#sysMessageBox").text(msg);
		$("#sysMessageBox").dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		});
		$('#sysMessageBox').dialog("option", "width", dlgWidth + 20);
		$('#sysMessageBox').dialog("option", "height", dlgHeight + 20);
		$("#sysMessageBox").dialog("option", "buttons", buttons);
		$("#sysMessageBox").dialog("option", "title", title);
		$("#sysMessageBox").dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
function messageBoxEx(html, title, buttons, width, height)
{
	var dlgWidth = (width!=null)?width:500;
	var dlgHeight = (height!=null)?height:300;
	
	if( $("#sysMessageBox").size() == 0 )
		$("body").append("<div id='sysMessageBox' title='Modal Dialog'>");

	try
	{
		$("#sysMessageBox").html(html);
		$("#sysMessageBox").dialog({
			bgiframe: true,
			autoOpen: false,
			modal: true,
			resizable: false
		});
		$('#sysMessageBox').dialog("option", "width", dlgWidth + 20);
		$('#sysMessageBox').dialog("option", "height", dlgHeight + 20);
		$("#sysMessageBox").dialog("option", "buttons", buttons);
		$("#sysMessageBox").dialog("option", "title", title);
		$("#sysMessageBox").dialog("open");
	}
	catch(e)
	{
		alert(e.message);
	}
}
///////////////////////////////////////