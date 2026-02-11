var isIE8 = false;
var iClickIdx = 0;
var opacity = 10;
var lockOut = false;

function fadeIn(elemName)
{
	var elem = $(elemName).get(0);
	//if( elem.opacity == "95"
	//alert(elem.opacity);
	if( elem.opacity == 50 ) return;
	elem.opacity -= 10;
	elem.style.opacity = 1;
	elem.style.filter = "alpha(opacity=" + elem.opacity + ")";
	window.setTimeout("fadeIn('" + elemName + "')", 10);
}

function drawPopup(link,image)
{
	var PopupMenu = $("#popupWnd").get(0);
	//alert(elem.link);
	//alert(elem.image);
	PopupMenu.innerHTML = "<center><img onclick='document.location.href=\""+link+"\"' src='"+image+"'/></center>";
/*
	switch(elem.link)
	{
	case "0":
		//alert("elis_on_whitney.gif");
		//PopupMenu.innerHTML = "<center><img style='width:100%;height:50%;' src='_images/elis_on_whitney.gif'/><br/><br/><br/><p><a href='#'>Click here to navigate</a><br/><br/><a href='#' onclick='closePopup()'>Close</a></p></center>";
		PopupMenu.innerHTML = "<center><img onclick='document.location.href=\"index.html\"' src='_images/eliswhitney.jpg'/></center>";
		break;
	case "1":
		//alert("elis_brick_oven.jpg");
		//PopupMenu.innerHTML = "<center><img style='width:100%;height:50%;' src='_images/elis_brick_oven.jpg'/><br/><br/><br/><p><a href='#'>Click here to navigate</a><br/><br/><a href='#' onclick='closePopup()'>Close</a></p></center>";
		PopupMenu.innerHTML = "<center><img onclick='document.location.href=\"http://www.elisbrickovenpizza.com\"' src='_images/elispizza2.png'/></center>";
		break;
	case "2":
		//alert("laurel_view.jpg");
		PopupMenu.innerHTML = "<center><img onclick='document.location.href=\"http://www.mawebcenters.com/laurelview\"' src='_images/BarMitzvah2web2.jpg'/></center>";
		break;
	case "3":
		//alert("laurel_view.jpg");
		PopupMenu.innerHTML = "<center><img onclick='document.location.href=\"http://www.elisonthehill.com\"' src='_images/elisonthehill.jpg'/></center>";
		break;
	}*/
	showPopupWindow();
	
	iClickIdx++
	window.setTimeout("autoCloseDlg("+iClickIdx+");", 6000);
}

function autoCloseDlg(index)
{
	if( index == iClickIdx )
		hidePopupWindow();
}

document.onclick = function closePopup()
{
	if( opacity > 10 && lockOut == false )
		hidePopupWindow();
		//PopupMenu.style.visibility = "hidden";
}

function fadeInPopupWindow()
{
	var PopupMenu = $("#popupWnd").get(0);
	if( opacity > 100 )
	{
		//opacity = 10;
		//clearTimeout();
		lockOut = false;
	}
	else
	{
		opacity += 10;
		//PopupMenu.document.body.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=" + opacity + ");";
		//PopupMenu.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=" + opacity + ");";
		PopupMenu.style.opacity = opacity/100;
		PopupMenu.style.filter = "Alpha(opacity=" + opacity + ");";
		window.setTimeout( 'fadeInPopupWindow()', 10 );
	}
}

function showPopupWindow()
{
	var PopupMenu = $("#popupWnd").get(0);
	lockOut = true;
	opacity = 10;
	//PopupMenu.document.body.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=10);";
	PopupMenu.style.visibility = "visible";
	PopupMenu.style.opacity = opacity/100;
	PopupMenu.style.filter = "Alpha(opacity=" + opacity + ");";
	//PopupMenu.show( (document.body.clientWidth / 2) - 300, (document.body.clientHeight / 2) - 100, 320, 275, document.body );
	window.setTimeout( 'fadeInPopupWindow()', 10 );
}

function fadeOutPopupWindow()
{
	var PopupMenu = $("#popupWnd").get(0);
	if( opacity <= 10 )
	{
		//opacity = 10;
		//clearTimeout();
		lockOut = false;
		PopupMenu.style.visibility = "hidden";
	}
	else
	{
		opacity -= 10;
		//PopupMenu.document.body.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=" + opacity + ");";
		//PopupMenu.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=" + opacity + ");";
		PopupMenu.style.opacity = opacity/100;
		PopupMenu.style.filter = "Alpha(opacity=" + opacity + ");";
		window.setTimeout( 'fadeOutPopupWindow()', 10 );
	}
}

function hidePopupWindow()
{
	var PopupMenu = $("#popupWnd").get(0);
	lockOut = true;
	opacity = 100;
	//PopupMenu.document.body.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=10);";
	PopupMenu.style.visibility = "visible";
	PopupMenu.style.opacity = opacity/100;
	PopupMenu.style.filter = "Alpha(opacity=" + opacity + ");";
	//PopupMenu.show( (document.body.clientWidth / 2) - 300, (document.body.clientHeight / 2) - 100, 320, 275, document.body );
	window.setTimeout( 'fadeOutPopupWindow()', 10 );
}

function openPageEditDialog(page_id)
{
	closeEditorDialog();

	// Run "loading" animation - also, disable all controls to prevent re-submitting
	var dlg = jQuery("<div id='dialog' style='padding:0px;'><iframe src='main.php?pid=" + page_id + "&form=y' style='width:800px;height:600px;padding:0px;'></iframe></div>");
	dlg.appendTo(document.body);
	$("#dialog").dialog( {autoOpen: true, width: 800, height: 600, resizable: false, title: 'Edit Page', modal: !isIE8} );
}


function openAlertsDialog()
{
	closeEditorDialog();
	
	// Run "loading" animation - also, disable all controls to prevent re-submitting
	var dlg = jQuery("<div id='dialog'><iframe src='main.php?call=query_alerts' width='95%' height='95%' style='margin:1px;'></iframe></div>");
	dlg.appendTo(document.body);
	$("#dialog").dialog( {autoOpen: true, width: 600, height: 400, resizable: false, title: 'Edit Alerts', modal: !isIE8} );
}

function closeEditorDialog()
{
	//if( $("#dialog").size() > 0 ) $("#dialog").remove();
	$("#dialog").remove();
}

$(document).ready(
	function()
	{	
		// Use this for incompatibility issues found in IE8(RC1)
		if( navigator.userAgent.indexOf("MSIE 8") >= 0 ) isIE8 = true;
		else isIE8 = false;

		//var PopupMenu = $("#popupWnd").get(0);
		//PopupMenu.innerHTML = "<center><img src='fish.jpg'></center>";
	}
);
