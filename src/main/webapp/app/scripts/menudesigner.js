/*
 * Globals
 */
var g_designMode = true;
var g_pingThread = null;
var g_connected = false;
var xmlPreviewAreaCtrl = null;

///////////////////////////////
// Initialize slider frames //
var timeout         = 500;
var closetimer		= 0;
var ddmenuitem      = 0;

function jsddm_open()
{	jsddm_canceltimer();
	jsddm_close();
	ddmenuitem = $(this).find('ul').eq(0).css('visibility', 'visible');}

function jsddm_close()
{	if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');}

function jsddm_timer()
{	closetimer = window.setTimeout(jsddm_close, timeout);}

function jsddm_canceltimer()
{	if(closetimer)
	{	window.clearTimeout(closetimer);
		closetimer = null;}}

$(document).ready(function()
{	$('#jsddm > li').bind('mouseover', jsddm_open);
	$('#jsddm > li').bind('mouseout',  jsddm_timer);});

document.onclick = jsddm_close;
///////////////////////////////////////
//////////////////////////////////////
/////////////////////////////////////
var g_curSelNode = null;
var g_curSelObjId = null;
var g_localRepos = new Object();

var menus_image = "books.png";
var menu_image = "book--pencil.png";
var cat_image = "document-text.png";
var item_image = "leaf.png";
var option_image = "question-white.png";
var delete_image = "cross.png";

var location_theme_id = null;

function isSameString( s1, s2 )
{
	var s1_c = s1.replace(/\r\n/g, "\n");
	var s2_c = s2.replace(/\r\n/g, "\n");
	
/*
	var s1_c = "s1=";
	var s2_c = "s2=";
	for (var c=0; c<s1.length; c++) {
		s1_c += "{ + " + s1.charCodeAt(c) + "}";
	}
	for (c=0; c<s2.length; c++) {
		s2_c += "{ + " + s2.charCodeAt(c) + "}";
	}
	alert(s1_c);
	alert(s2_c);
*/
	return (s1_c == s2_c);
}

function showArray(str)
{
	var str_c = "";
	for (var c=0; c < str.length; c++) {
		str_c += "{ + " + str.charCodeAt(c) + "}";
	}
	alert(str_c);
}


function convertFromCrLf( instr )
{
	return instr.replace(/\r\n/g, "\n");
}

function convertToCrLf( instr )
{
	return instr.replace(/\r\n/g, "\n").replace(/\n/g, "\r\n");
}

function initPage(sel)
{
	//$(".ui-dialog")
	myLayout = $('body').layout({

	//	enable showOverflow on west-pane so popups will overlap north pane
		west__showOverflowOnHover: false

	//	reference only - these options are NOT required because are already the 'default'
	,	closable:				true	// pane can open & close
	,	resizable:				true	// when open, pane can be resized 
	,	slidable:				true	// when closed, pane can 'slide' open over other panes - closes on mouse-out

	//	some resizing/toggling settings
	,	north__resizable:		false
	,	north__closable:		false
	,	north__slidable:		false	// OVERRIDE the pane-default of 'slidable=true'
	,	north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
	,	north__spacing_closed:	20		// big resizer-bar when open (zero height)
	,	south__resizable:		false	// OVERRIDE the pane-default of 'resizable=true'
	,	south__spacing_open:	0		// no resizer-bar when open (zero height)
	,	south__spacing_closed:	20		// big resizer-bar when open (zero height)
	//	some pane-size settings
	,	west__minSize:			100
	,	west__size:				350
	/*,	east__size:				300*/
	,	east__minSize:			200
	,	east__maxSize:			Math.floor(screen.availWidth / 2) // 1/2 screen width
	});

	// add event to the 'Close' button in the East pane dynamically...
	//myLayout.addCloseBtn('#btnCloseEast', 'east');

	// add event to the 'Toggle South' buttons in Center AND South panes dynamically...
	//myLayout.addToggleBtn('.south-toggler', 'south');

	// add MULTIPLE events to the 'Open All Panes' button in the Center pane dynamically...
	///myLayout.addOpenBtn('#openAllPanes', 'north');
	//myLayout.addOpenBtn('#openAllPanes', 'south');
	//myLayout.addOpenBtn('#openAllPanes', 'west');
	//myLayout.addOpenBtn('#openAllPanes', 'east');
	$("#menusTree").tree({
		selected : sel,
		rules : {
			// I have not defined any of these so disabling the checks will save CPU cycles
			use_max_children : false,
			use_max_depth : true,
			clickable : true,
			max_depth : 5,
			type_attr : "type"
		},

		plugins : {
			contextmenu : {
				items : {
					remove : false,
					create : false,
					rename : false,
					create_menu : {
						label   : "Create Menu",
						icon    : contextPath + "/app/images/" + menu_image,
						visible : function (NODE, TREE_OBJ) {
							var nodeType = $(NODE).children("a:eq(0)").attr("type");
							if( nodeType == "root" ) return 1;
							return -1;
						},
						action  : function (NODE, TREE_OBJ) {
							createMenu();
						}
					},
					create_category : {
						label   : "Create Category",
						icon    : contextPath + "/app/images/" + cat_image,
						visible : function (NODE, TREE_OBJ) {
							var nodeType = $(NODE).children("a:eq(0)").attr("type");
							if( nodeType == "menu" ) return 1;
							return -1;
						},
						action  : function (NODE, TREE_OBJ) {
							var id = $(NODE).children("a:eq(0)").attr("id");
							createCategory(id, NODE);
						}
					},
//					preview_menu : {
//						label   : "Preview Menu",
//						icon    : contextPath + "/app/images/" + cat_image,
//						visible : function (NODE, TREE_OBJ) {
//							var nodeType = $(NODE).children("a:eq(0)").attr("type");
//							if( nodeType == "menu" ) return 1;
//							return -1;
//						},
//						action  : function (NODE, TREE_OBJ) {
//							var id = $(NODE).children("a:eq(0)").attr("id");
//							//createCategory(id, NODE);
//							previewMenu(id);
//						}
//					},
					create_item : {
						label   : "Create Item",
						icon    : contextPath + "/app/images/" + item_image,
						visible : function (NODE, TREE_OBJ) {
							var nodeType = $(NODE).children("a:eq(0)").attr("type");
							if( nodeType == "category" ) return 1;
							return -1;
						},
						action  : function (NODE, TREE_OBJ) {
							var id = $(NODE).children("a:eq(0)").attr("id");
							createItem(id, NODE);
						}
					},
					create_option : {
						label   : "Create Option",
						icon    : contextPath + "/app/images/" + option_image,
						visible : function (NODE, TREE_OBJ) {
							var nodeType = $(NODE).children("a:eq(0)").attr("type");
							if( nodeType == "item" ) return 1;
							return -1;
						},
						action  : function (NODE, TREE_OBJ) {
							var id = $(NODE).children("a:eq(0)").attr("id");
							createOption(id, NODE);
						}
					},
					delete_node : {
						label   : "Delete",
						icon    : contextPath + "/app/images/" + delete_image,
						visible : function (NODE, TREE_OBJ) {
							var nodeType = $(NODE).children("a:eq(0)").attr("type");
							if( nodeType == "root" ) return -1;
							return 1;
						},
						action  : function (NODE, TREE_OBJ) {
							var id = $(NODE).children("a:eq(0)").attr("id");
							deleteNode(NODE);
						}
					},
					separator_before : false
				}
			}
		},
		callback : {
			beforechange : function (NODE, TREE_OBJ) {
				commitChanges(false);
/*
				if( g_curSelObjId != null && haveFieldsChanged(g_curSelObjId) == true )
				{
					if( confirm( "Would you like to committed changes to the local repository?" ) == true )
					{
						commitChanges(g_curSelObjId, false);
					}

					//messageBox("Would you like to committed changes to the local repository", "Changes Committed",
					//{
					//	"Yes": function(){commitChanges(g_curSelObjId); $.tree.focused().select(NODE); $(this).dialog("close");},
					//	"No": function(){$(this).dialog("close");},
					//	"Cancel": function(){$(this).dialog("close");}
					//});

					//return false;
				}
*/
				return true;
			},
			onchange : function (NODE, TREE_OBJ) {
				var id = $(NODE).children("a:eq(0)").attr("id");
				var type = $(NODE).children("a:eq(0)").attr("type");
				g_curSelNode = $(NODE);
				g_curSelObjId = id;
				showDetails(id, type);
			},
			beforemove : function (NODE, REF_NODE, TYPE, TREE_OBJ) {
				var nodeType = $(NODE).children("a:eq(0)").attr("type");
				var refNodeType = $(REF_NODE).children("a:eq(0)").attr("type");
				if(
					(nodeType == refNodeType) ||
					(TYPE == "inside" && ( (refNodeType == "menus" && nodeType == "menu") ||
										   (refNodeType == "menu" && nodeType == "category") ||
										   (refNodeType == "category" && nodeType == "item") ||
										   (refNodeType == "item" && nodeType == "option") ) )
				){
					return true;
				}else{
					alert("Invalid operation");
					return false;
				}
			}
		}
	});
	//$.tree.focused().open_all();
	loadLocationMenus();
	
	// Prevent session timeout while in designer
	g_pingThread = window.setInterval("pingServer();", 5000);
}

function previewMenu(menu_id)
{
	commitChanges(false);
	
	var xmlMarkup = compileSingleMenu(locationId, menu_id);
	//alert(xmlMarkup);
	
	var oDoc = new XDocument();
	oDoc.create(xmlMarkup);
	
	var oMenu = oDoc.selectNode("//menu");
	//var previewPanel = "<div style='overflow:scroll;width:750px;height:500px;'>"
	var menuGen = new MenuGenerator();
	//previewPanel += menuGen.createMarkup(oMenu);
	//previewPanel += "</div>";
	var previewPanel = menuGen.createMarkup(oMenu, null, true);
	//function messageBoxEx(html, title, {});
	showDialogEx(previewPanel, "Menu Preview");
//	{	
//		"Close": function(){$(this).dialog("close").remove();}
//	});
}

function showHelp()
{
	//parent.frames.viewport.location = "help/designerhelp.html";
	$("#viewpanel").load(noCache("help/designerhelp.html"));
}

function previewMenus()
{
	commitChanges(false);
	showDialog(contextPath + "/app/designer/menupreview.jsp?loc=" + locationId, "Menus Preview");
}

function noCache(url)
{
	if(url.indexOf('?')<0)
		return url + "?r=" + escape(new Date().getTime());
	else
		return url + "&r=" + escape(new Date().getTime());
}

function loadDetails(template, id)
{
	$.ajax({
		type: "GET",
		//url: template + "?id=" + id,
		url: noCache(template + "?id=" + id),
		dataType: "html",
		success: function(html){
			$("#viewpanel").html(html);
			fillFields(id);
		}
	});
}

function showDetails(id, type)
{
	startAnimation();
	switch(type)
	{
	case "root":
		$("#viewpanel").load(noCache("welcome.jsp"), function(){
			$("#theme_id").val(location_theme_id);
			$("#theme_id").change();
			stopAnimation();
		});
		break;
	case "menu":
		$("#viewpanel").load(noCache("menu_details.jsp?id=" + id), function(){fillFields(id);});
		break;
	case "category":
		$("#viewpanel").load(noCache("cat_details.jsp?id=" + id), function(){fillFields(id);});
		break;
	case "item":
		gIndex = 0;
		$("#viewpanel").load(noCache("item_details.jsp?id=" + id));
		break;
	case "option":
		$("#viewpanel").load(noCache("option_details.jsp?id=" + id), function(){fillFields(id);});
		break;
	}
	//startAnimation();
}

function fillFields(id)
{
	stopAnimation();
	var obj = g_localRepos[id];
	var type = obj["type"];
	//alert(obj["hidden"]);
	switch(type)
	{
	case "menu":
		$("#name").val(obj["name"]);
		$("#take_orders").val(obj["take_orders"]);
		if( obj["take_orders"] == 2 ){
			$("#hoop").show();
		}else{
			$("#hoop").hide();
		}
		$("#schedule_id").val(obj["schedule_id"]);
		if( obj["show_options"] == "Y") $("#show_options").attr("checked", true);
		else $("#show_options").attr("checked", false);
		if( obj["hidden"] == "Y") $("#hidden").attr("checked", true);
		else $("#hidden").attr("checked", false);
		break;
	case "category":
		$("#name").val(obj["name"]);
		if( obj["hidden"] == "Y") $("#hidden").attr("checked", true);
		else $("#hidden").attr("checked", false);
		break;
	case "item":
		$("#name").val(obj["name"]);
		$("#description").val(obj["description"]);
		$("#image_img").attr("src", obj["image"]);
		var portions = obj["portions"];
		if( portions.length > 0 )
		{
			for( var i = 0; i < portions.length; i++ )
			{
				if( i == 0 )
				{
					addPortion(portions[i]["size"], portions[i]["price"]);
				}
				else
				{
					addPortion(portions[i]["size"], portions[i]["price"]);
				}
			}
		}
		else
		{
			addPortion("","");
		}
		
		if( obj["hidden"] == "Y") $("#hidden").attr("checked", true);
		else $("#hidden").attr("checked", false);
		break;
	case "option":
		$("#name").val(obj["name"]);
		$("#type").val(obj["option_type"]);
		$("#type").trigger("change");
		$("#price").val(obj["price"]);
		$("#price").blur();
		$("#option_data").val(obj["option_data"]);
		if( obj["hidden"] == "Y") $("#hidden").attr("checked", true);
		else $("#hidden").attr("checked", false);
	}
	//adjustFrames();
}
/*
function haveFieldsChanged(id)
{
	if( id == "menus_ID" ) return;
	var obj = g_localRepos[id];
	var type = obj["type"];
	switch(type)
	{
	case "menu":
		if( $("#name").val() != obj["name"] ) return true;
		if( ( obj["show_options"] == "Y" && $("#show_options").attr("checked") == false ) ||
			( obj["show_options"] != "Y" && $("#show_options").attr("checked") == true ) ) return true;
		if( ( obj["hidden"] == "Y" && $("#hidden").attr("checked") == false ) ||
			( obj["hidden"] != "Y" && $("#hidden").attr("checked") == true ) ) return true;
		break;
	case "category":
		if( parent.frames.viewport.document.all.name.value != obj["name"] ) return true;
		if( obj["hidden"] == "Y") parent.frames.viewport.document.all.hidden.checked = true;
		else parent.frames.viewport.document.all.hidden.checked = false;
		break;
	case "item":
		if( parent.frames.viewport.document.all.name.value != obj["name"] ) return true;
		if( parent.frames.viewport.document.all.description.value != obj["description"] ) return true;
		//alert(parent.frames.viewport.document.all.image_img.src);
		//alert(obj["image"].substr(5));
		//alert( parent.frames.viewport.document.all.image_img.src.indexOf(obj["image"].substr(5)) );
		if( parent.frames.viewport.document.all.image_img.src.indexOf(obj["image"].substr(5)) < 0 ) return true;
		//alert( parent.frames.viewport.document.all.size_desc.value + "!=" + obj["size"] );
		//alert( parent.frames.viewport.document.all.price.value + "!=" + obj["price"] );
		
		//if( parent.frames.viewport.document.all.size_desc.value != obj["size"] ) return true;
		//if( parent.frames.viewport.document.all.price.value != obj["price"] ) return true;
		if( ( obj["hidden"] == "Y" && parent.frames.viewport.document.all.hidden.checked == false ) ||
			( obj["hidden"] != "Y" && parent.frames.viewport.document.all.hidden.checked == true ) ) return true;
		break;
	case "option":
		if( parent.frames.viewport.document.all.name.value != obj["name"] ) return true;
		if( parent.frames.viewport.document.all.type.value != obj["option_type"] ) return true;
		if( parent.frames.viewport.document.all.price.value != obj["price"] ) return true;
		if( isSameString(parent.frames.viewport.document.all.option_data.value, obj["option_data"]) == false ) return true;
		//if( parent.frames.viewport.document.all.option_data.value != obj["option_data"] ) return true;
		if( ( obj["hidden"] == "Y" && parent.frames.viewport.document.all.hidden.checked == false ) ||
			( obj["hidden"] != "Y" && parent.frames.viewport.document.all.hidden.checked == true ) ) return true;
	}
	return false;
}
*/
function deleteNode(node)
{
	var nodeTitle = "";
	if( node != null )
		nodeTitle = $.tree.focused().get_text(node);
	else
		nodeTitle = $.tree.focused().get_text(g_curSelNode);
	messageBox("Delete '" + nodeTitle + "'?", "Confirm Delete",
	{	
		"No": function(){$(this).dialog("close");},
		"Yes": function()
		{
			$.tree.focused().remove(node);
			$(this).dialog("close");
		}
	});
}

function createOption(id, node)
{
	var newName = prompt("Please enter new option name.", "");
	if (newName==null || newName=="") return;

	var id = getNewID();
	var data = { title : newName, icon: contextPath + "/app/images/" + option_image, attributes : { id : id, type : "option" } };
	addOptionToRepo(id, newName, "select", "", "", 0, "N");
	if( node != null )
		$.tree.focused().select_branch($.tree.focused().create({ data : data }, $(node)));
	else
		$.tree.focused().select_branch($.tree.focused().create({ data : data }));
}

function createItem(id, node)
{
	var newName = prompt("Please enter new item name.", "");
	if (newName==null || newName=="") return;

	var id = getNewID();
	var data = { title : newName, icon: contextPath + "/app/images/" + item_image, attributes : { id : id, type : "item" } };
	addItemToRepo(id, newName, "Item Description", "", "", "", 0, "N");
	if( node != null )
		$.tree.focused().select_branch($.tree.focused().create({ data : data }, $(node)));
	else
		$.tree.focused().select_branch($.tree.focused().create({ data : data }));
}

function createCategory(id, node)
{
	var newName = prompt("Please enter new category name.", "");
	if (newName==null || newName=="") return;

	var id = getNewID();
	var data = { title : newName, icon: contextPath + "/app/images/" + cat_image, attributes : { id : id, type : "category" } };
	addMenuToRepo(id, newName, "N", "N");
	if( node != null )
		$.tree.focused().select_branch($.tree.focused().create({ data : data }, $(node)));
	else
		$.tree.focused().select_branch($.tree.focused().create({ data : data }));
}

//var gNewObjId = 0;
function S4()
{
   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
function guid()
{
   return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}
function small_guid()
{
	return (S4()+S4()+"-"+S4());
}
function getNewID()
{
	////return "NEW:" + gNewObjId++;
	return "NEW:" + small_guid();
}

function createMenu()
{
	var newName = prompt("Please enter new menu name.", "");
	if (newName==null || newName=="") return;

	var id = getNewID();
	var data = { title : newName, icon : contextPath + "/app/images/" + menu_image,	attributes : { id : id, type : "menu" } };
	addMenuToRepo(id, newName, "N", "N");
	$.tree.focused().select_branch($.tree.focused().create({ data : data }, $.tree.focused().get_node('#menus_ID')));
}

function renameSelectedNode(name, feedback)
{
	$.tree.focused().rename(g_curSelNode, name);
	if( feedback )
		messageBox("Your changes have been committed to the local repository", "Changes Committed",{"OK": function(){$(this).dialog("close");}});
}


//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//  S A V E   -   M e t h o d s
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
/*
function utf8_encode ( argString ) {
    // Encodes an ISO-8859-1 string to UTF-8  
    // 
    // version: 909.322
    // discuss at: http://phpjs.org/functions/utf8_encode
    // +   original by: Webtoolkit.info (http://www.webtoolkit.info/)
    // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +   improved by: sowberry
    // +    tweaked by: Jack
    // +   bugfixed by: Onno Marsman
    // +   improved by: Yves Sucaet
    // +   bugfixed by: Onno Marsman
    // +   bugfixed by: Ulrich
    // *     example 1: utf8_encode('Kevin van Zonneveld');
    // *     returns 1: 'Kevin van Zonneveld'
    var string = (argString+''); // .replace(/\r\n/g, "\n").replace(/\r/g, "\n");
 
    var utftext = "";
    var start, end;
    var stringl = 0;
 
    start = end = 0;
    stringl = string.length;
    for (var n = 0; n < stringl; n++) {
        var c1 = string.charCodeAt(n);
        var enc = null;
 
        if (c1 < 128) {
            end++;
        } else if (c1 > 127 && c1 < 2048) {
            enc = String.fromCharCode((c1 >> 6) | 192) + String.fromCharCode((c1 & 63) | 128);
        } else {
            enc = String.fromCharCode((c1 >> 12) | 224) + String.fromCharCode(((c1 >> 6) & 63) | 128) + String.fromCharCode((c1 & 63) | 128);
        }
        if (enc !== null) {
            if (end > start) {
                utftext += string.substring(start, end);
            }
            utftext += enc;
            start = end = n+1;
        }
    }
 
    if (end > start) {
        utftext += string.substring(start, string.length);
    }
 
    return utftext;
}
*/
//This function decodes the any string
//that's been encoded using URL encoding technique
function URLDecode(psEncodeString)
{
	// Create a regular expression to search all +s in the string
	var lsRegExp = /\+/g;
	// Return the decoded string
	return unescape(String(psEncodeString).replace(lsRegExp, " "));
}

function loadFile()
{
	showDialog(contextPath + "/app/designer/file_loader.jsp?loc=" + locationId, "Import Menus", 500, 370);
}

function viewXmlSource(id)
{
	commitChanges(false);
	var xmlMarkup = compileLocation(id);
	//clipboardData.setData("text", xmlMarkup);
	//xmlPreviewAreaCtrl.setCode(xmlMarkup);
	$("#xmlPreviewArea").text(xmlMarkup);
	showDialogBasic("xmlPreviewFrame", "Menus Preview");
}

function makePostRequest( action, data )
{
	var oForm = document.getElementById( "MENU_FILE_SAVE" );
	if( oForm != null ) document.body.removeChild(oForm);
	//{
	oForm = document.createElement( "FORM" );
	oForm.id = "MENU_FILE_SAVE";
	//}
	oForm.action = action;
	oForm.method = "POST";
	oForm.target = "uploadFrame";
	document.body.appendChild( oForm );
	var aInputs = data.split( "&" );
	for( var i = 0; i < aInputs.length; i++ )
	{
		var aInput = aInputs[i].split( "=" );
		var oInput = document.createElement( "INPUT" );
		oInput.type = "hidden";
		oInput.name = unescape(aInput[0]);
		oInput.value = URLDecode( aInput[1] );
		oForm.appendChild( oInput );
	}
	oForm.submit();
}
function saveAsFile(id)
{
	commitChanges(false);
	//pingServer(saveAsFileYes);
	var xmlMarkup = compileLocation(id);
	var data = "data=" + escape(xmlMarkup);
	makePostRequest(contextPath + "/MenuDesigner/savetofile?r=" + Math.floor( Math.random() * (1000000) ), data);
	
	// Allow ping error to show if the connection is lost since this post will give no error
	g_connected = true;
}



// PING METHODS //
function pingServer(callback)
{
	var proc = (callback!=null)?callback:pingResponse;
	var sReq = contextPath + "/MenuDesigner/ping?r=" + Math.floor( Math.random() * (1000000) );
	$.ajax({
		type: "GET",
		url: sReq,
		dataType: "xml",
		success: proc,
		error: processPingError
	});
}
function pingResponse(xml)
{
	g_connected = true;
}
function processPingError()
{
	stopAnimation();
	if( g_connected ) alertLostConnection();
	g_connected = false;
}
/////////////////////////////////////////


function saveMenus(id)
{
	commitChanges(false);
	messageBoxEx("Publish changes to server? <em>Click <a href='javascript:saveBeforePublish()'>here</a> to backup your changes to a file.</em>", "Confirm Publish",
	{	
		"No": function(){$(this).dialog("close");},
		"Yes": function()
		{
			saveMenusYes(id)
			$(this).dialog("close");
		}
	});
}

function saveBeforePublish(){
	$('#sysMessageBox').dialog("close");
	saveAsFile(locationId);
}
function saveMenusYes(id)
{
	commitChanges(false);
	
	startAnimation();
	var xmlMarkup = compileLocation(id);
	//alert(xmlMarkup);
	
	// Make request to the server
	var sReq = contextPath + "/MenuDesigner/save?r=" + Math.floor( Math.random() * (1000000) );
	//$.post(sReq, xmlbld.xml, processSaveResponse, "xml");
	$.ajax({
		type: "POST",
		url: sReq,
		//data: utf8_encode(xmlMarkup),
		data: xmlMarkup,
		dataType: "xml",
		success: processSaveResponse,
		error: processError
	});
	//clipboardData.setData("text", xmlMarkup);
}
function processError()
{
	stopAnimation();
	alertLostConnection();
	g_connected = false;
}



function alertLostConnection()
{
	messageBoxEx("Lost connection to server. Follow these steps to save your data<ul><li>Save your changes to the clipboard</li><li>Open Notepad</li><li>Paste menu data into notepad</li><li>Save to disk</li><li>Changes can be loaded back into designer next time you login</li></ul>", "Connection Failure", {
		"Close": function(){$(this).dialog("close");},
		"Save To Clipboard": function(){saveToClipboard(locationId);}
	}, 500, 500);
}



function processSaveResponse(xml)
{
	stopAnimation();
	//alert(xml.xml);
	var code = $(xml).find("code").text();
	if( code != "0")
	{
		//var msg = $(xml).find("msg")[0].text;
		var msg = "There was an error while publishing. Your session may have timed out.";
		messageBox(msg, "Server Error", {
			"Close": function(){$(this).dialog("close");},
			"Save To File": function(){	saveAsFile(locationId);$(this).dialog("close");}
		});
		return;
	}
	/*
	.each(function() {
		if( $(this).text() != "0")
		{
			//message("There was an error while publishing you menus.", "Error");
			$(xml).find("error").find("code").each(function() {
				
				message($(this).children("msg").text(), "Server Error", {"OK": function(){$(this).dialog("close");}});
				return;
				
			});
			//alert("Your session has timed out. Press OK to reload page.");
		}
	});
	*/
	//messageBox("Publishing Complete", "Success", {"OK": function(){document.location.search = "loc=" + locationId;}});
	messageBox("Publishing Complete", "Success", {"OK": function(){$(this).dialog("close");}});
	
	//if( g_curSelObjId != null )
	//document.location.search = "loc=" + locationId;// + "&oid=" + g_curSelObjId;
}

function compileLocation(id)
{
	var xmlbld = new XmlWriter();
	xmlbld.writeStartDocument();
	xmlbld.writeStartElement("location");
	xmlbld.writeAttributeString("id", id);
	xmlbld.writeAttributeString("theme_id", location_theme_id);
	//if(selectedMenuId!=null && selectedMenuId.length > 0){
	//	xmlbld.writeAttributeString("single_menu", selectedMenuId);
	//}
	var menuTree = jQuery.tree.reference('#menus_ID');
	var rootNode = menuTree.get_node('#menus_ID');
	var menuNodes = menuTree.children(rootNode);
	for (var i = 0; i < menuNodes.length; ++i)
	{
		compileMenu(menuTree, menuNodes[i], xmlbld, i);
	}
	xmlbld.writeEndElement();	// location
	xmlbld.writeEndDocument();
	return xmlbld.xml;
}
function compileSingleMenu(id, menuid)
{
	var xmlbld = new XmlWriter();
	xmlbld.writeStartDocument();
	xmlbld.writeStartElement("location");
	xmlbld.writeAttributeString("id", id);
	var menuTree = jQuery.tree.reference('#menus_ID');
	var rootNode = menuTree.get_node('#menus_ID');
	var menuNodes = menuTree.children(rootNode);
	for (var i = 0; i < menuNodes.length; ++i)
	{
		var menu_id = $(menuNodes[i]).children("a:eq(0)").attr("id");
		if( menuid == menu_id) compileMenu(menuTree, menuNodes[i], xmlbld, i);
	}
	xmlbld.writeEndElement();	// location
	xmlbld.writeEndDocument();
	return xmlbld.xml;
}
function compileMenu(menuTree, node, xmlbld, index) 
{
	var nodeTitle = $.tree.focused().get_text($(node));
	var nodeId = $(node).children("a:eq(0)").attr("id");
	var nodeType = $(node).children("a:eq(0)").attr("type");
	
	var obj = g_localRepos[nodeId];
	
	xmlbld.writeStartElement("menu");
	//xmlbld.writeAttributeString("id", (nodeId.indexOf("NEW:")==0)?"0":nodeId);
	xmlbld.writeAttributeString("id", nodeId);
	xmlbld.writeAttributeString("name", obj.name);
	xmlbld.writeAttributeString("index", index);
	xmlbld.writeAttributeString("show_options", obj.show_options);
	xmlbld.writeAttributeString("take_orders", obj.take_orders);
	xmlbld.writeAttributeString("schedule_id", obj.schedule_id);
	xmlbld.writeAttributeString("hidden", obj.hidden);
	var catNodes = menuTree.children(node); 
	for (var i = 0; i < catNodes.length; ++i)
	{
		compileCategory(menuTree, catNodes[i], xmlbld, i);
	}
	xmlbld.writeEndElement();	// menu
}

function compileCategory(menuTree, node, xmlbld, index) 
{
	var nodeTitle = $.tree.focused().get_text($(node));
	var nodeId = $(node).children("a:eq(0)").attr("id");
	var nodeType = $(node).children("a:eq(0)").attr("type");
	
	var obj = g_localRepos[nodeId];
	
	xmlbld.writeStartElement("category");
	//xmlbld.writeAttributeString("id", (nodeId.indexOf("NEW:")==0)?"0":nodeId);
	xmlbld.writeAttributeString("id", nodeId);
	xmlbld.writeAttributeString("name", obj.name);
	xmlbld.writeAttributeString("index", index);
	xmlbld.writeAttributeString("hidden", obj.hidden);
	var childNodes = menuTree.children(node); 
	for (var i = 0; i < childNodes.length; ++i)
	{
		compileItem(menuTree, childNodes[i], xmlbld, i);
	}
	xmlbld.writeEndElement();	// category
}

function compileItem(menuTree, node, xmlbld, index) 
{
	var nodeTitle = $.tree.focused().get_text($(node));
	var nodeId = $(node).children("a:eq(0)").attr("id");
	var nodeType = $(node).children("a:eq(0)").attr("type");
	xmlbld.writeStartElement("item");
	//xmlbld.writeAttributeString("id", (nodeId.indexOf("NEW:")==0)?"0":nodeId);
	xmlbld.writeAttributeString("id", nodeId);
	xmlbld.writeAttributeString("index", index);
	
	var obj = g_localRepos[nodeId];
	xmlbld.writeAttributeString("hidden", obj.hidden);
	xmlbld.writeTextNode("name", obj.name);
	xmlbld.writeTextNode("description", obj.description);
	var a = obj.image.split(contextPath + "/ImageViewer/");
	if( a.length > 1 )
		xmlbld.writeTextNode("image", "/" + a[1]);
	else
		xmlbld.writeTextNode("image", obj.image.substr(17));
	xmlbld.writeStartElement("options");
	/////////////////////////////////////////////
	var childNodes = menuTree.children(node); 
	for (var i = 0; i < childNodes.length; ++i)
	{
		compileOption(menuTree, childNodes[i], xmlbld, i);
	}
	xmlbld.writeEndElement();	// options
	/////////////////////////////////////////////
	xmlbld.writeStartElement("portions");
	for (var i = 0; i < obj.portions.length; ++i)
	{
		xmlbld.writeStartElement("size");
		xmlbld.writeAttributeString("price", obj.portions[i].price);
		xmlbld.writeAttributeString("index", i);
		xmlbld.writeCharacters(obj.portions[i].size);
		xmlbld.writeEndElement();	// size
	}
	//saveBuf.push('</portions>');
	xmlbld.writeEndElement();	// portions
	xmlbld.writeEndElement();	// item
}

function compileOption(menuTree, node, xmlbld, index) 
{
	var nodeTitle = $.tree.focused().get_text($(node));
	var nodeId = $(node).children("a:eq(0)").attr("id");
	var nodeType = $(node).children("a:eq(0)").attr("type");
	var obj = g_localRepos[nodeId];
	xmlbld.writeStartElement("option");
	//xmlbld.writeAttributeString("id", (nodeId.indexOf("NEW:")==0)?"0":nodeId);
	xmlbld.writeAttributeString("id", nodeId);
	xmlbld.writeAttributeString("name", obj.name);
	xmlbld.writeAttributeString("price", obj.price);
	xmlbld.writeAttributeString("type", obj.option_type);
	xmlbld.writeAttributeString("index", index);
	xmlbld.writeAttributeString("hidden", obj.hidden);
	//showArray(obj.option_data);
	xmlbld.writeCharacters(obj.option_data);
	xmlbld.writeEndElement();	// option
}


function commitChanges(feedback)
{
	try
	{
		if( g_curSelObjId == null ) return;
		var id = g_curSelObjId;
		var obj = g_localRepos[g_curSelObjId];
		var type = obj["type"];
		switch(type)
		{
		case "menu":
			commitMenuChanges(id, feedback);
			break;
		case "category":
			commitCategoryChanges(id, feedback);
			break;
		case "item":
			commitItemChanges(id, feedback);
			break;
		case "option":
			commitOptionChanges(id, feedback);
		}
	}
	catch(e){}
}

function commitMenuChanges(id, feedback)
{
	var obj = g_localRepos[id];
	obj["name"] = $("#name").val();
	obj["take_orders"] = $("#take_orders").val();
	obj["schedule_id"] = $("#schedule_id").val();
	if($("#show_options:checked").is(':checked')) obj["show_options"] = "Y";
	else obj["show_options"] = "N";
	if($("#hidden:checked").is(':checked')) obj["hidden"] = "Y";
	else obj["hidden"] = "N";
	renameSelectedNode($("#name").val(), feedback);
}

function commitCategoryChanges(id, feedback)
{
	var obj = g_localRepos[id];
	obj["name"] = $("#name").val();
	if($("#hidden:checked").is(':checked')) obj["hidden"] = "Y";
	else obj["hidden"] = "N";
	renameSelectedNode($("#name").val(), feedback);
}

function commitItemChanges(id, feedback)
{
	var obj = g_localRepos[id];
	obj["name"] = $("#name").val();
	obj["description"] = $("#description").val();
	obj["image"] = $("#image_img").attr("src");

	var portions = new Array();
	var sizes = $('#sortable').sortable('toArray');//buildSizeArray();
	for(var i = 0; i < sizes.length; i++)
	{
		var size = sizes[i];
		var a = size.split("-");
		var itemSize = $("#size_desc" + a[1]).val();
		var itemPrice = $("#price" + a[1]).val();
		portions.push({size: itemSize, price: itemPrice});
	}
	obj["portions"] = portions;

	if($("#hidden:checked").is(':checked')) obj["hidden"] = "Y";
	else obj["hidden"] = "N";
	renameSelectedNode($("#name").val(), feedback);
}

function commitOptionChanges(id, feedback)
{
	var obj = g_localRepos[id];
	obj["name"] = $("#name").val();
	obj["option_type"] = $("#type").val();
	if( obj["option_type"] == "text" ){
		obj["price"] = "0";
	}
	else{
		obj["price"] = $("#price").val();
	}
	obj["option_data"] = convertFromCrLf($("#option_data").val());
	if($("#hidden:checked").is(':checked')) obj["hidden"] = "Y";
	else obj["hidden"] = "N";
	renameSelectedNode($("#name").val(), feedback);
}

function loadLocationMenus()
{
	startAnimation();
	var sReq = contextPath + "/MenuDesigner/query";
	sReq += "?" + "loc=" + locationId;
	sReq += "&r=" + Math.floor( Math.random() * (1000000) );
	$.ajax({
		type: "GET",
		url: sReq,
		dataType: "xml",
		contentType: "text/xml",
		cache: false,
		success: drawMenu,
		error: errorLoadingMenus
	});
}

function errorLoadingMenus(request,error,msg)
{
	stopAnimation();
	messageBox(msg, error, {"OK": function(){$(this).dialog("close");}});
}

function addMenuToRepo(id, name, show_options, hidden, take_orders, schedule_id)
{
	if(show_options==null) show_options = "N";
	if(hidden==null) hidden = "N";
	var obj = {
			type:			"menu",
			id:				id,
			name:			name,
			show_options:	show_options,
			take_orders: 	take_orders,
			schedule_id:	schedule_id,
			hidden:			hidden
	};
	g_localRepos[id] = obj;
}
function addCategoryToRepo(id, name, hidden)
{
	var obj = {
			type:			"category",
			id:				id,
			name:			name,
			hidden:			hidden
	};
	g_localRepos[id] = obj;
}
function addItemToRepo(id, name, description, image, portions, index, hidden)
{
	var obj = {
			type:			"item",
			id:				id,
			name:			name,
			description:	description,
			image:			"../../ImageViewer" + image,
			portions:		portions,
			hidden:			hidden
			//index:			index
	};
	g_localRepos[id] = obj;
}
function addOptionToRepo(id, name, type, price, data, index, hidden)
{
	var obj = {
			type:			"option",
			id:				id,
			name:			name,
			option_type:	type,
			price:			price,
			option_data:	data,
			hidden:			hidden
			//index:			index
	};
	g_localRepos[id] = obj;
}

function addToLocalRepository(node, type)
{
	var id = node.attr("id");
	if(id == null || id.length==0) id = getNewID();

	switch(type)
	{
	case "menu":
		addMenuToRepo(id, node.attr("name"), node.attr("show_options"), node.attr("hidden"), node.attr("take_orders"), node.attr("schedule_id"));
		break;
		
	case "category":
		addCategoryToRepo(id, node.attr("name"), node.attr("hidden"));
		break;
		
	case "item":
		var itemAdded = false;
		var name = node.children("name").text();
		var description = node.children("description").text();
		var image = node.children("image").text();
		var index = node.children("index").text();
		var hidden = node.children("hidden").text();
		var portions = new Array();
		//var first_size = node.children("portions").children("size:first");
		//var itemSize = first_size.text();
		//var itemPrice = first_size.attr("price");
		//node.children("portions").children("size").each( function(){
		//	itemSize = $(this).text();
		//	itemPrice = $(this).attr("price");
		//});
		node.children("portions").children("size").each( function(){
			var itemSize = $(this).text();
			var itemPrice = $(this).attr("price");
			portions.push({size: itemSize, price: itemPrice});
		});
		
		addItemToRepo(id, name, description, image, portions, index, hidden);
		break;
		
	case "option":
		addOptionToRepo(id, node.attr("name"), node.attr("type"), node.attr("price"), node.text(), 0, "N");
		break;
		
	default:
		break;
	}
	return id;
}

function drawMenuFromStream(xml_str)
{
	try
	{
		var oDoc = new XDocument();
		oDoc.create(xml_str);
		drawMenu(oDoc.getDocument());
	}
	catch(e)
	{
		alert(e);
	}
}

function drawMenu(xml)
{
	stopAnimation();
	//alert(xml.xml);
	
	//clipboardData.setData("text", xml);
	try
	{
		/*
		 * Look for any error messages
		 */

		try
		{
			$(xml).find("error").find("code").each(function() {
				
				if( $(this).text() == "1")
				{
					messageBox("Your session has timed out. Press OK login.", "Error", {"OK": function(){backtologin();}});
					return;
				}
			});
		}
		catch(ee){}

		do
		{
			// Check to see if there is already some menus in the tree
			var menuTree = jQuery.tree.reference('#menus_ID');
			if(menuTree==null) break;
			var rootNode = menuTree.get_node('#menus_ID');
			if(rootNode==null) break;
			var menuNodes = menuTree.children(rootNode);
			if( menuNodes!=null && menuNodes.length > 0)
			{
				var msg = "Importing new menus will overwrite your current ones. You will lose any unsaved or unpublished data. Continue?";
				messageBox(msg, "Continue Loading Menus", {
					"No": function(){$(this).dialog("close");},
					"Yes": function(){drawMenuYes(xml);$(this).dialog("close");}
				});
				return;
			}
		}
		while(false);
		
		drawMenuYes(xml);
		
	}
	catch(e)
	{
		alert(e);
	}
}

function drawMenuYes(xml)
{
	stopAnimation();
	
	//clipboardData.setData("text", xml.xml);
	try
	{
		var jsonString = new Array(1000);
		
		/*
		 * Build menu tree
		 */
		var lName = $(xml).find("location").attr("name");
		location_theme_id = $(xml).find("location").attr("theme_id");
		var location_id = $(xml).find("location").attr("id");
		$("#theme_id").val(location_theme_id);
		$("#theme_id").change();
		var locTitle = lName;
		//var lSingleMenuId = $(xml).find("location").attr("single_menu");
		//if(lSingleMenuId){
		//	selectedMenuId = lSingleMenuId;
		//	$("#menu_edit_mode").show();
		//}
		//else{
		$("#menu_edit_mode").hide();
		locTitle += " - All Menus";
		//}
			
		if( $(xml).find("menu").length == 0 ){
			messageBox("This location has no menus. Would you like to import some menus from a template?", "Setup New Menus", {
				"No": function(){$(this).dialog("close");},
				"Yes": function(){$(this).dialog("close");loadFile(location_id);}
			});
		}
		jsonString.push('[{data:{title:"' + locTitle + '",icon:"'+contextPath+'/app/images/' + menus_image + '",attributes:{ id:"menus_ID",type:"root"}},children:[');
		$(xml).find("menu").each(function(i) {
			if( i > 0 ) jsonString.push(',');
			//var menu_image;
			//if($(this).children("hidden").text() == "Y") menu_image = "hidden_menu2.png";
			jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + menu_image + '",attributes:{id:"' + addToLocalRepository($(this), "menu") + '",type:"menu"}},children:[');
			//addToLocalRepository($(this), "menu");
			$(this).find("category").each(function(ii) {
				if( ii > 0 ) jsonString.push(',');
				//var cat_image;
				//if($(this).children("hidden").text() == "Y") cat_image = "hidden_category.png";
				jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + cat_image + '",attributes:{id:"' + addToLocalRepository($(this), "category") + '",type:"category"}},children:[');
				//addToLocalRepository($(this), "category");
				$(this).find("item").each(function(iii) {
					if( iii > 0 ) jsonString.push(',');
					//var item_image;
					//if($(this).children("hidden").text() == "Y") item_image = "hidden_item1.png";
					jsonString.push('{data:{title:"'+ $(this).children("name").text() + '",icon:"' + contextPath + '/app/images/' + item_image + '",attributes:{id:"' + addToLocalRepository($(this), "item") + '",type:"item"}},children:[');
					//addToLocalRepository($(this), "item");
					$(this).find("options").find("option").each(function(iiii) {
						if( iiii > 0 ) jsonString.push(',');
						//var item_image;
						//if($(this).children("hidden").text() == "Y") item_image = "hidden_item1.png";
						jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + option_image + '",attributes:{id:"' + addToLocalRepository($(this), "option") + '",type:"option"}}}');
						//addToLocalRepository($(this), "option");
					});
					jsonString.push(']}');
				});
				jsonString.push(']}');
			});
			jsonString.push(']}');
		});
		jsonString.push(']}]');
		
		var jsonMenus = eval(jsonString.join(""));
		$.tree.focused().settings.data.type = "json";
		$.tree.focused().settings.data.opts = {static:jsonMenus};
		$.tree.focused().refresh();
		
		if(selectedMenuId!=null && selectedMenuId.length > 0){
			$.tree.focused().open_branch("a#"+selectedMenuId, false, null);
			$.tree.focused().select_branch("a#"+selectedMenuId, false);
			$.tree.focused().open_all("a#"+selectedMenuId);
		}
		else{
			$.tree.focused().open_all();
		}
	}
	catch(e)
	{
		alert(e);
	}
}
