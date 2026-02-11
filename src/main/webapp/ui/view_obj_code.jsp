<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<%@ page import="org.apache.commons.text.StringEscapeUtils"%>

<script type="text/javascript"><!--

// 0 - read-only
// 1 - edit mode w/existing record
// 2 - edit mode w/new record
// 3 - edit mode w/copy current record
var editMode = 0;
var sortBy = "";
var searchStr = "";
var viewFilterStr = "";
var sortOrder = "A";
var accessLevel = "";
var expandMode = 0;
var m_lastQuery = "";
var g_xmlObjList = new XDocument();
//var g_MainListView;

var prev_selected_lv_id = "-1";
var selected_lv_id = "-1";
var selected_lv_index = 0;
var g_idarray = null;
var codemirrorCrtls = [];

// Building XML for global listview
var xmlTitle = new XDocument();
var __xmlTitle = '';
__xmlTitle +=	'<listview>';
__xmlTitle += 		'<columns>';
<viewCfg:ViewList viewName="<%=viewParam%>">
__xmlTitle += 			'<column name="<%=itemField%>" enum="<%=itemEnum%>" width="<%=itemWidth%>" text="<%=itemTitle%>" valueof="<%=itemProperty%>" view="<%=itemView%>" fkey="<%=itemFKey%>"/>';
</viewCfg:ViewList>
__xmlTitle += 		'</columns>';
__xmlTitle +=	'</listview>';
xmlTitle.create( __xmlTitle );

// Create the client object for global use
var g_client = new ObjectClient();

//document.reAllSizeImages = function()
//{
	//reSizeImages();
//}

function getListLength()
{
	return 0;//listLength;
}

function getSelID( nullid )
{
	//if( lastSel != null )
	if( selected_lv_id != "-1" )
	{
		return selected_lv_id;
	}
	else
	{
		return nullid;
	}
}

function AttachToEvent( obj, evType, callback )
{
	if( obj.addEventListener )
	{
		obj.addEventListener( evType, callback, false );
		return true;
	}
	else if( obj.attachEvent )
	{
		return obj.attachEvent( "on" + evType, callback );
	}
	return false;
}

function getSelIndex()
{
	//return selected_lv_index;
	if( g_idarray == null && selected_lv_id >= 0 ) return 0;
	return g_idarray[selected_lv_id];
}

function getRandNum()
{
	var dt = new Date();
	return dt.getMilliseconds();
}

<viewCfg:ViewList viewName="<%=viewParam%>">
<% if( itemEnum.length() > 0 ){ %>
function enum_<%=itemEnum%>_conv(val){
	<viewCfg:EnumValues enumName="<%=itemEnum%>">
	if(val==<%=enumCode%>) return "<%=enumText%>";
	</viewCfg:EnumValues>
	return "";
}
<% } %>
</viewCfg:ViewList>

function createGrid()
{
	//jQuery("#gridview").GridDestroy();
    jQuery("#gridview").jqGrid({
   	    //url:'data.xml',
	    datatype:'clientSide',
   	    colModel:[
   		    {name:'id', index:'id', label: 'id', hidden:true, key:true}
   		 <viewCfg:ViewList viewName="<%=viewParam%>">
   		 	,{name:'<%=itemField%>', index:'<%=itemField%>', label:'<%=itemTitle%>', width:<%=itemWidth%>}
   		 </viewCfg:ViewList>	
   	    ],
   	    rowNum:8,
   	    autowidth: true,
   	    //rowList:[10,20,30],
   	    //pager: jQuery('#gridbox'),
   	    sortname: 'id',
        viewrecords: true,
        sortorder: "desc",
        //shrinkToFit: false,
        //autoHeight: true,
        //height: 150,
        height: "100%",
        //caption:"XML Example",
<%//	if( viewConfigBean.getFormExternal().length() == 0 ){	%>
		onSelectRow: listviewSelect,
<%//	} else { %>
		//beforeSelectRow: function(){ return false; },
		//hoverrows: false,
		//onSelectRow: function(){ document.loadScheduleDesigner() },
<%//	} %>
	    onSortCol: listviewSort
    });//.navGrid('#gridbox',{edit:false,add:false,del:false});
    
    //$("#gridview").fluidGrid();//{ example:"#view_obj_body", offset:-10 });
}

function initData()
{
	// CUSTOMIZE
	attachToFields();
	$("#tabs").tabs();
	
	var overlay = $('<div class="ui-widget-overlay"></div>').hide().appendTo('body');

	$.fn.jPicker.defaults.images.clientPath='<%=request.getContextPath()%>/xlibs/jquery/jpicker/images/';
	$('.color_picker').jPicker({
			window:{
				title: 'Select a color',
				effects:
			    {
			      type: 'fade', // effect used to show/hide an expandable picker. Acceptable values "slide", "show", "fade"
			      speed:
			      {
			        show: 'slow', // duration of "show" effect. Acceptable values are "fast", "slow", or time in ms
			        hide: 'fast' // duration of "hide" effect. Acceptable value are "fast", "slow", or time in ms
			      }
			    },
				position:{
					x: 'screenCenter',
					y: 'center'
				}
			}
			//color:{active:new $.jPicker.Color({ahex:'993300ff'})}
		},
		function(){ 
			overlay.fadeOut();
		},
		function(){ 
			
		},
		function(){ 
			overlay.fadeOut(); 
		}
	);
	
	$(".jPicker .Image").click(function(){
		overlay.fadeIn();
	});
	//}
	
	//$('.color_picker').ColorPicker({
	//	onChange: function (hsb, hex, rgb) {
	//		$(this).css('backgroundColor', '#' + hex);
	//	}
	//});
	
	  $('body').click(function(evt) {
	        if ($('div.jPicker.Container:visible').length > 0) {
	            if ($(evt.target).parents('span.jPicker').length > 0) {
	                $('div.jPicker.Container').each(function() {
	                    if ($(this).css('z-index') == '10') {
	                        $(this).hide('fast');
	                    }
	                });
	            } else if ($(evt.target).parents('div.jPicker.Container').length == 0) {
	            	overlay.fadeOut();
	                $('div.jPicker.Container').hide('fast');
	            }
	        }
	    });


	$('#tab_panel').tabs();
	//createMessageDialog();

<%	if( viewConfigBean.getListVisible() && formOnly == false ){ %>
	createGrid();
<%	} %>

//		if( !g_client.initialize() )
//		{
//			alert( "failed to initialize client control" );
//			return;
//		}
	g_client.setListener( onDocumentLoaded );

<%
	String onFormLoad_event = viewConfigBean.getEventNum_OnFormLoad();
	if( onFormLoad_event.length() > 0 ){ %>
	eventHandler(<%=onFormLoad_event%>);
	
<%	} %>

	
	//var docLocationURL;
<% 	if( request.getParameter( viewParam + "_objstart" ) != null ){ %>
	var nxt = <%=request.getParameter( viewParam + "_objstart" )%> - 1;
	m_lastQuery = "<%=request.getContextPath()%>/ViewCmd?view=<%=viewParam%>&call=query&objStart=" + nxt + viewFilterStr;
<% 	}else{ %>
	m_lastQuery = "<%=request.getContextPath()%>/ViewCmd?view=<%=viewParam%>&call=query" + viewFilterStr;
<% 	} %>
	
<%	String defaultSearch = request.getParameter( viewParam + "_searchStr" );
		if( defaultSearch != null ){ %>
	searchStr = unescape("<%=defaultSearch%>");
	m_lastQuery += searchStr;
	<%	} %>
	
	<viewCfg:ViewLinks viewName="<%=viewParam%>">
	<%
		int iAccRights = clientSessionBean.getAccessRights( sAccRights, linkView );
		if( iAccRights > 0 ){
	%>
		document.all.tab_<%=linkView%>.disabled = true;
	<%	} %>
	</viewCfg:ViewLinks>

	try
	{
		window.top.startAnimation();
	}
	catch(e){}
	g_client.makeRequest(m_lastQuery + "&r=" + getRandNum());

<%	if( viewConfigBean.getListVisible() && formOnly == false ){ %>
	$(window).bind('resize', function() { 
	    // resize the datagrid to fit the page properly:
	    //$('#gridview').fluidGrid({example:'#view_obj_body', offset:-20});
	    $('#gridview').fluidGrid();
	});
<%	} %>

	//document.reAllSizeImages();
	disableDisplay( true );

	$(".external_form").livequery(function(){
		$(this).fancybox({
		    'width' : 940,
		    'height' : 570,
		    'transitionIn' : 'none',
		    'transitionOut' : 'none',
		    'hideOnOverlayClick' : false,
		    'type' : 'iframe'
		});
	});
}

function clearImage(img_id)
{
	$('#'+img_id+'_img').attr('src','');
	$('#'+img_id).val('');
	$('#'+img_id+'_img').hide();
}

function setImage(img_id, src)
{
	$('#'+img_id+'_img').attr('src','<%=request.getContextPath()%>/ImageViewer' + src);
	$('#'+img_id).val(src);
	$('#'+img_id+'_img').show();
}

function clearObject(img_id)
{
	$('#'+img_id).val('');
	$('#display_'+img_id).val('');
}

function switchControlType( id, type, view )
{
	var oInputContainer = document.getElementById("DIV_"+id);
	var vHTML = "";
	if( type == "text" )
	{
		vHTML = "<input type=\"text\" ID=\"" + id + "\" NAME=\"" + view + "_data_" + id + "\"/>";
	}
	else if( type == "int" )
	{
		vHTML = "<input type=\"text\" ID=\"" + id + "\" NAME=\"" + view + "_data_" + id + "\" onkeypress=\"validateinput( this, 'int' );\" onblur=\"formatinput( this, 'int', 0 );\"/><button id=\"button_" + id + "\" onclick=\"showCalc(this,'" + id + "');\">...</button>";
	}
	else if( type == "boolean" )
	{
		vHTML = "<input type=\"checkbox\" ID=\"" + id + "\" NAME=\"" + view + "_data_" + id + "\"/>";
	}
	oInputContainer.innerHTML = vHTML;
}

function listviewSelect(id,index)
{
	prev_selected_lv_id = selected_lv_id;
	selected_lv_id = id;
	selected_lv_index = index;
	viewObject(id);
}
function listviewSort(sortby,order)
{
	sortBy = sortby;
	if( sortOrder == "A" )
		sortOrder = "D";
	else
		sortOrder = "A";
	sortList( sortby );
	return 'stop';
}
function changeAccessLevel(newLevel)
{
	accessLevel = newLevel;
	refreshView( B_REFRESH );
}

function validateRequiredParams()
{
	var createPatronParams = obj_sel_data.getElementsByTagName("input");
	for( i = 0; i < createPatronParams.length; i++ )
	{
		var param = createPatronParams[i];
		if( param.WMrequired == "true" && param.value.length == 0 )
		{
			msgBoxAlert("Required field '" + param.title + "' is missing.", "Invalid Parameter");
			param.focus();
			return false;
		}
	}
	return true;
}

function buttonCallback( item, param )
{
	<%	String objInitField, objInitValue; %>
	var sel_id = getSelID( "-1" );
	switch( item )
	{
		case B_EDIT:
			editMode = 1;
			disableDisplay( false );
			setupToolbar( 1 );
			break;

		case B_SUBMIT:
			if( validateRequiredParams() )
			{
				saveObject();
			}
			break;

		case B_CANCEL:
			editMode = 0;
			disableDisplay( true );
			setupToolbar( 0 );
			viewObject( getSelID( -1 ) );
			break;

		case B_EXPAND:
			expandMode ^= 1;
			refreshView( B_REFRESH );
			break;

		case B_SEARCH:
			showDialog("view_obj_search.jsp?view=<%=viewParam%>", "Search", 700, 460);
			break;

		case B_SELECT:
			if( editMode > 0 )
			{
				showDialog("select_obj.jsp?" + param, "Select", 800, 500);
			}
			break;

		case B_SELECTIMAGE:
			if( editMode > 0 )
			{
				showDialog("image_library.jsp?" + param, "Select Image", 750, 600);
			}
			break;

		case B_SUBTRACT:
			if( editMode > 0 )
			{
				var emlist = document.all.item( param );
				emlist.remove( emlist.selectedIndex );
			}
			break;

		case B_NEW:
<% if( viewConfigBean.getFormExternal().length() > 0 ){	%>
			
			$("#external_form_new").click();
			//refreshView(B_REFRESH);
<% }else{ %>
			
			editMode = 2;
			// Blur the object list - visual only

			// Blank the form entries
			<viewCfg:ViewForm viewName="<%=viewParam%>">
                <% 	String searchParam = request.getParameter( viewParam + "_filter_" + inputField );
                   	if( searchParam == null ){ %>
                   		var <%=inputField%>_default_value = "<%=StringEscapeUtils.escapeJson(inputDefaultVal)%>";
				<%		if( inputType.equalsIgnoreCase( "boolean" ) == true ){ %>
							document.all.<%=inputField%>.checked = false;
				<%		} else if (inputType.equalsIgnoreCase( "list" ) ==true ){ %>
							document.all.<%=inputField%>.length = 0;
				<%		} else if (inputType.equalsIgnoreCase( "image" ) ==true ){ %>
							$('#<%=inputField%>_img').attr('src','');
							$('#<%=inputField%>').val('');
				<%		} else if (inputType.equalsIgnoreCase( "color" ) ==true ){ %>
							var new_color = <%=inputField%>_default_value;
							if( new_color.length == 0 ) new_color = null;
							$('#<%=inputField%>').val(new_color);
							findColorCtrl("<%=inputField%>").color.active.val('ahex', new_color);
				<%		} else if (inputType.equalsIgnoreCase( "html" ) ==true ){ %>
							codemirrorCrtls['<%=inputField%>'].setCode(<%=inputField%>_default_value);
				<% 		} else { %>
							document.all.<%=inputField%>.value = <%=inputField%>_default_value;
				<%		}
				 		if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
							document.all.display_<%=inputField%>.value = "";
                <% 		}
					} else { %>
                       	document.all.<%=inputField%>.value = "<%=searchParam%>";
				<%	} %>
			</viewCfg:ViewForm>
			<%
			objInitField = viewConfigBean.getCreateObjInitField();
			objInitValue = viewConfigBean.getCreateObjInitValue();
			if( ( objInitField != null && objInitField.length() > 0 ) &&
				( objInitValue != null && objInitValue.length() > 0 ) ){
			%>
				document.all.<%=objInitField%>.value = "<%=objInitValue%>";
			<%	} %>
			disableDisplay( false );
			setupToolbar( 1 );
<% } %>
			break;

		case B_COPY:
			editMode = 3;
			<%
			objInitField = viewConfigBean.getCreateObjInitField();
			objInitValue = viewConfigBean.getCreateObjInitValue();
			if( ( objInitField != null && objInitField.length() > 0 ) &&
				( objInitValue != null && objInitValue.length() > 0 ) ){
			%>
				document.all.<%=objInitField%>.value = "<%=objInitValue%>";
			<%	} %>
			disableDisplay( false );
			setupToolbar( 1 );
			break;

		case B_ADD:
			editMode = 2;

			// Blur the object list - visual only
			<%	if( viewConfigBean.getByRef().length() > 0 ) {
				String strRefView = "";
				String strDisplayParam = ""; %>

			// Blank the form entries
			<viewCfg:ViewForm viewName="<%=viewParam%>">
                   <%	if( inputField.equalsIgnoreCase( viewConfigBean.getByRef() ) == true ){
						strRefView = inputView;
						strDisplayParam = inputDisplay;
					}
					String searchParam = request.getParameter( viewParam + "_filter_" + inputField );
                       if( searchParam == null ){
						if( inputType.equalsIgnoreCase( "boolean" ) == true ){ %>
							document.all.<%=inputField%>.checked = false;
				<% 		} else if (inputType.equalsIgnoreCase( "list" ) ==true ){ %>
							document.all.<%=inputField%>.length = 0;
				<%		} else { %>
							document.all.<%=inputField%>.value = "";
				<%		}
				 		if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
							document.all.display_<%=inputField%>.value = "";
                   <% 		}
					} else { %>
                       	document.all.<%=inputField%>.value = "<%=searchParam%>";
				<%	} %>
			</viewCfg:ViewForm>

			showDialog("select_obj.jsp?view=<%=strRefView%>&parentProp=<%=viewConfigBean.getByRef()%>&displayProp=<%=strDisplayParam%>&poptype=addbyref", "Select");

			<%	} %>
			//editMode = 0;
			break;

		case B_DELETE:
		case B_REMOVE:
			if( sel_id != "-1" )
			{
				if( confirm( "This operation will delete the selected item. Continue?" ) == true )
				{
					// CUSTOMIZE
					var objData = "";
					objData += "view=<%=viewParam%>";
					objData += "&call=delete";
					objData += "&id=" + escape(sel_id);
					window.top.startAnimation();
					g_client.makeRequest("<%=request.getContextPath()%>/ViewCmd", objData);
				}
			}
			else
			{
				alert( "No Selection" );
			}
			break;

		case B_FORWARD:
		case B_BACK:
			refreshView( item );
			break;

		default:
			alert( item + " - not implemented" );
			break;
	}
}

function drawGrid()
{
	g_idarray = new Object();
	
	jQuery("#gridview").clearGridData();
	var colsList = xmlTitle.getNodeList( "column" );
	var rowsList = g_xmlObjList.getNodeList( "object" );
	var selRow = null;
	var no_results = false;

	var length;
	if( expandMode == 0 ){
		length = <jsp:getProperty name="viewConfigBean" property="listSize"/>;
	}
	else{
		length = <jsp:getProperty name="viewConfigBean" property="listExpandSize"/>;
	}

	$("#no_results_msg").remove();
	if( rowsList.getLength() > 0 ){
		$("#tabs").block({ message:null, showOverlay: false, overlayCSS: { opacity: 0.1 } });
		$("#edittoolbar").block({ message:null, showOverlay: false, overlayCSS: { opacity: 0.1 } });
		var rowID = getSelID( "-1" );
		for( i = 0; i < rowsList.getLength() && i < length; i++ )
		{
			var rowNode = rowsList.getNode( i );
			var vRow = new Object();
			for( var j = 0; j < colsList.getLength(); j++ )
			{
				var colNode = colsList.getNode( j );
				var propEnum = colNode.getAttribute("enum");
				var propValueOf = colNode.getAttribute("valueof");
				var propView = colNode.getAttribute("view");
				var propNode = rowNode.selectNodeList( "property[@name='" + propValueOf + "']" ).getNode(0);
				var propVal = "";
				var dataDisplay = "";
				if( propNode != null )
				{
					if(propEnum.length>0){
						propVal = eval("enum_"+propEnum+"_conv('"+propNode.getText()+"')");
					}else{
						propVal = propNode.getText();
					}
				}
				
				var linkColYes = (colNode.getAttribute("name") == "<%=viewConfigBean.getFormExternalLinkColumn()%>") && <%=(viewConfigBean.getFormExternal().length() > 0)%>;
				
				if( linkColYes ){
					var propID = rowNode.getAttribute("id");
					dataDisplay = "<a style='color:blue;text-decoration:underline;' class='external_form' href='<%=viewConfigBean.getFormExternal()%>?id=" + propID + "'>" + propVal + "</a>";
				} else {
					if( /*mode == "I" ||*/ propView == null || propView.length == 0 )
					{
						dataDisplay = propVal
					}
					else
					{
						var propFKey = colNode.getAttribute("fkey");
						var propFKeyNode = rowNode.selectNodeList( "property[@name='" + propFKey + "']" ).getNode(0);//.item(0);
						var propViewID = propFKeyNode.getText();
						if( propVal.length > 0 ){
							dataDisplay = "<a style='color:#45A2C9;text-decoration:underline;' href='view_obj.jsp?view=" + propView + "&" + propView+ "_searchStr=%2526id%253D" + propViewID + "'>" + propVal + "</a>";
						}
					}
				}
	
				vRow[colNode.getAttribute("name")] = dataDisplay;
			}
		    jQuery("#gridview").addRowData(rowNode.getAttribute("id"), vRow);
		    if( selRow == null ) selRow = rowNode.getAttribute("id");
		    if( rowID == rowNode.getAttribute("id") ) selRow = rowNode.getAttribute("id");	// Override selected row based on previous selected id
		    g_idarray[rowNode.getAttribute("id")] = i+1;
		}
		$('#gridview').fluidGrid();
	}
	else{
		no_results = true;
	}
	
	setTimeout( function(){
		selected_lv_id = "-1";
		if( selRow != null ) jQuery("#gridview").setSelection(selRow);
		if(no_results){
			// Show 'No Results' message
			$("#no_results_msg_row").append("<div id='no_results_msg'>There are no results for this search</div>");
			$("#tabs").block({ message:null, showOverlay: true, overlayCSS: { opacity: 0.1 } });
			$("#edittoolbar").block({ message:null, showOverlay: true, overlayCSS: { opacity: 0.1 } });
		}
	}, 400 );
	
	//$(".ui-row-ltr").css({
	//	"minHeight": "<%//=viewConfigBean.getListRowHeight()%>px",
	//	"lineHeight": "<%//=viewConfigBean.getListRowHeight()%>px"
	//});
}
function onDocumentLoaded( xml_doc )
{
	try
	{
		window.top.stopAnimation();
	}
	catch(e){}

	// Verify response status code
	if( g_client.getStatusCode() != 200 )
	{
		messageBox("Invalid response - " + g_client.getStatusCode(), "Error", {"OK": function(){$(this).dialog("close");}});
		return;
	}

	// Verify the response body
	if( xml_doc == null )//|| xml_doc.xml.length == 0 )
	{
		messageBox("Invalid response recieved", "Error", {"OK": function(){$(this).dialog("close");}});
		return;
	}
/*
	try
	{
		if( xml_doc.xml.length == 0 )
		{
			messageBox("Your session has timed out. Press OK to return to the login page.", "Error", {"OK": function(){window.top.location = 'login.jsp';}});
			return;
		}
	}
	catch(e){}
*/
	// Attach response body to XDocument abstraction class
	var xmlResponse = new XDocument();
	xmlResponse.attach( xml_doc );
	//alert(xml_doc.xml);

	// Break out the code, msg, and the call to which this originated
	var sCall = "";
	var sCode = "-1";
	var sMsg = "";
	var callNode = xmlResponse.getNodeList( "call" ).getNode(0);
	if( callNode != null ) sCall = callNode.getText();
	var codeNode = xmlResponse.getNodeList( "code" ).getNode(0);
	if( codeNode != null ) sCode = codeNode.getText();
	var msgNode = xmlResponse.getNodeList( "msg" ).getNode(0);
	if( msgNode != null ) sMsg = msgNode.getText();

	try
	{
		if( sCode == 88 || sCode == 90 )
		{
			messageBox("Your session has timed out. Press OK to return to the login page.", "Error", {"OK": function(){window.top.location = 'login.jsp';}});
			return;
		}
	}
	catch(e){}
	
	if( displayResponse( sCode, sMsg ) == true )
	{
		if( sCall == "query" )
		{
			var _col = xmlResponse.getNodeList( "collection" );
			if( _col == null )
			{
				return;
			}

			//var xmlstr = xml_doc.xml ? xml_doc.xml : (new XMLSerializer()).serializeToString(xml_doc);
			//g_xmlObjList.create(xmlstr);
			
			g_xmlObjList.attach( xml_doc );
			<%	if( viewConfigBean.getByRef().length() > 0 ){ %>
			var mode = "X";
			<%	}else{ %>
			var mode = "Y";
			<%	} %>
			
			var accessLevel = "";
			var listBox = _col.getNode(0);
			if( listBox.isNull() == false )
			{
				var accessParam = listBox.getAttribute("requestLevel");
				if( accessParam != null )
				{
					accessLevel = accessParam;
				}
			}

			var listHTML = listboxHeader( g_xmlObjList, mode, "<%=request.getSession().getAttribute( "admin" )%>", "<jsp:getProperty name="viewConfigBean" property="access"/>", accessLevel, "<jsp:getProperty name="viewConfigBean" property="title"/>" );
			ObjList.innerHTML = listHTML;

<%	if( viewConfigBean.getListVisible() && formOnly == false ){ %>
			drawGrid();
<%	} %>


			buttonEnable( document.getElementById("Button_Delete"), false );
<%	String noEditUI = request.getParameter( "objNoEdit" );
	if( noEditUI != null &&
		noEditUI.equalsIgnoreCase( "Y" ) == true ){ %>
			buttonEnable( document.getElementById("Button_New"), false );
			buttonEnable( document.getElementById("Button_Copy"), false );
<%	} %>

			viewObject( getSelID( "-1" ) );
			disableDisplay( true );
			
<%	if( viewConfigBean.getListVisible() == false || formOnly == true ){ %>
			var rowsList = g_xmlObjList.getNodeList("object");
			if( rowsList.getLength() > 0 )
			{
				var rowNode = rowsList.getNode(0);
				setTimeout(function(){
					listviewSelect(rowNode.getAttribute("id"), 0);
				}, 500 );
			}
<%	} %>

		}
		else if( sCall == "insert" ||
				 sCall == "copy" ||
				 sCall == "update" ||
				 sCall == "submit" ||
				 sCall == "delete" )
		{
			editMode = 0;
			disableDisplay( true );
			setupToolbar( 0 );
			//setCursorStyles( "wait" );
			window.top.startAnimation();
			g_client.makeRequest(m_lastQuery + "&r=" + getRandNum());
		}
	}
}

function displayResponse( sCode, sMsg )
{
	if( sCode != null && sCode != "-1" )	// frame: noop
	{
		if( sCode == "1" )	// Validation Error
		{
			messageBox("(" + sCode + ") " + sMsg, "Validation Error", {"OK": function(){
				$(this).dialog("close");
				//window.top.startAnimation();
				//g_client.makeRequest(m_lastQuery + "&r=" + getRandNum());
			}});
			return false;
		}
		else if( sCode != "0" )
		{
			messageBox("(" + sCode + ") " + sMsg, "Server Error", {"OK": function(){$(this).dialog("close");}});
			//alert( "(" + sCode + ") " + sMsg );
			//return false;
		}
		return true;
	}
	return false;
}

function refreshView( action )
{
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	var start = listBox.getAttribute("objStart");
	var length;

	if( expandMode == 0 ){
		length = <jsp:getProperty name="viewConfigBean" property="listSize"/>;
	}
	else{
		length = <jsp:getProperty name="viewConfigBean" property="listExpandSize"/>;
	}

		
	var new_start;
	if( action == B_BACK )
	{
		new_start = parseInt(start) + parseInt(length);
	}
	else if( action == B_FORWARD )
	{
		new_start = parseInt(start) - parseInt(length);
		if( new_start < 0 ) new_start = 0;
	}
	else
	{
		new_start = start;
	}
	var docURL = "<%=request.getContextPath()%>/ViewCmd?view=<%=viewParam%>&call=query&objCount=" + length + "&objStart=" + new_start;
	if( sortBy.length >  0 )
	{
		docURL += "&sortBy=" + sortBy + "&sortOrder=" + sortOrder;
	}
	if( searchStr.length >  0 )
	{
		//alert( searchStr );
		docURL += searchStr;
	}
	if( accessLevel.length >  0 )
	{
		docURL += "&accessLevel=" + accessLevel;
	}

       // Append view filter
	docURL += viewFilterStr;

	//alert( docURL );
	m_lastQuery = docURL
	//setCursorStyles( "wait" );
	window.top.startAnimation();
	g_client.makeRequest(m_lastQuery + "&r=" + getRandNum());
}

function sortList( column_name )
{
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	var start = listBox.getAttribute("objStart");
	var length = listBox.getAttribute("objCount");
	m_lastQuery = "<%=request.getContextPath()%>/ViewCmd?view=<%=viewParam%>&call=query&objCount=" + length + "&objStart=" + start + "&sortBy=" + column_name + "&sortOrder=" + sortOrder + "&access=" + accessLevel + viewFilterStr;
	window.top.startAnimation();
	g_client.makeRequest(m_lastQuery + "&r=" + getRandNum());
}

function swapSelectionVals()
{
	var temp = selected_lv_id;
	selected_lv_id = prev_selected_lv_id;
	prev_selected_lv_id = temp;
}

var selectedRowId;
function viewObject( rowID )
{
	//setTimeout( function(){
	selectedRowId = rowID;
	if( rowID != "-1" )
	{
		if( editMode == 1 || editMode == 2 || editMode == 3 )
		{
			swapSelectionVals();
			messageBox("You are currently in edit mode. Continue without saving and discard any changes?", "Save Changes",
			{"OK": function(){
				//saveObject();
				swapSelectionVals();
				editMode = 0;
				disableDisplay( true );
				setupToolbar( 0 );
				viewObjectConfirmed();
				$(this).dialog("close");
			}, "Cancel": function(){$(this).dialog("close");}});
			return;
		}
		viewObjectConfirmed();
	}
	//}, 200 );
}

//function viewObjectConfirmed( rowID )
function viewObjectConfirmed()
{
	var rowID = getSelID( "-1" );
	if( rowID != "-1" )
	{
		if( g_xmlObjList != null )
		{
			var propNode;
			var boolVal;
			var embObjList
			// CUSTOMIZE
			<viewCfg:ViewForm viewName="<%=viewParam%>">
				<% 	if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
						// Update object reference
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0)
						if( propNode != null )
							document.all.<%=inputField%>.value = propNode.getText();
						else
                           	document.all.<%=inputField%>.value = "";

						// Update display value for object
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputField%>.<%=inputDisplay%>']" ).getNode(0);
						if( propNode != null )
							document.all.display_<%=inputField%>.value = propNode.getText();
						else
							document.all.display_<%=inputField%>.value = "";
				<% 	} else if( inputType.equalsIgnoreCase( "boolean" ) == true ){ %>
						boolVal = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0).getText();
						if( boolVal == "Y" )	document.all.<%=inputField%>.checked = true;
						else document.all.<%=inputField%>.checked = false;
				<% 	} else if( inputType.equalsIgnoreCase( "list" ) == true ){ %>
						// Remove all previous objectrefs
						document.all.<%=inputField%>.length = 0;

						// Add objectrefs from selected object
						embObjList = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']/objectref" );
						if( embObjList != null )
						{
							for( i = 0; i < embObjList.getLength(); i++ )
							{
								var embObj = embObjList.getNode( i );
								var oNewOption = document.createElement( "OPTION" );
								document.all.<%=inputField%>.options.add( oNewOption );
								var propRef = embObj.selectNode("propref[@name='<%=inputDisplay%>']");
								$(oNewOption).text(propRef.getText());
								
								oNewOption.value = embObj.getAttribute("id");
							}
						}
				<% 	} else if( inputType.startsWith( "enum::" ) == true ){ %>
						document.all.<%=inputField%>.value = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0).getText();
				<% 	} else if( inputType.equalsIgnoreCase( "html" ) == true ||
							   inputType.equalsIgnoreCase( "css" ) == true ||
							   inputType.equalsIgnoreCase( "js" ) == true ){ %>
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0);
						codemirrorCrtls['<%=inputField%>'].setCode(propNode.getText());
				<% 	} else if( inputType.equalsIgnoreCase( "image" ) == true ){ %>
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0);
						//document.all.<%=inputField%>.value = propNode.getText();
						if( propNode.getText().length > 0 )
							setImage('<%=inputField%>', propNode.getText());
							//document.all.<%=inputField%>_img.src = "<%=request.getContextPath()%>/ImageViewer" + propNode.getText();
						else
							clearImage('<%=inputField%>');
							//document.all.<%=inputField%>_img.src = "";
				<% 	} else if( inputType.equalsIgnoreCase( "color" ) == true ){ %>
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0);
						document.all.<%=inputField%>.value = propNode.getText();
						if(propNode.getText().length > 0 )
							findColorCtrl("<%=inputField%>").color.active.val('ahex', propNode.getText());
						else
							findColorCtrl("<%=inputField%>").color.active.val('ahex', null);
				<% 	} else if( inputType.equalsIgnoreCase( "password" ) == true ){ %>
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0);
						document.all.<%=inputField%>.value = propNode.getText();
						document.all.<%=inputField%>_vrfy.value = propNode.getText();
				<% 	} else { %>
						propNode = g_xmlObjList.selectNodeList( "//object[@id='" + rowID + "']/property[@name='<%=inputProperty%>']" ).getNode(0);
						document.all.<%=inputField%>.value = propNode.getText();
				<%	} %>
			</viewCfg:ViewForm>
		}

		// Checking for restrictions on editing - by object
		<%
			String readonlyField = viewConfigBean.getSelObjReadonlyField();
			String readonlyValue = viewConfigBean.getSelObjReadonlyValue();
			if( ( readonlyField == null || readonlyField.length() == 0 ) ||
				( readonlyValue == null || readonlyValue.length() == 0 ) ){
		%>
				if( document.all.objNoEdit.value != "Y" )
				{
					buttonEnable( document.getElementById("Button_Edit"), true );
					buttonEnable( document.getElementById("Button_Delete"), true );
					buttonEnable( document.getElementById("Button_New"), true );
					buttonEnable( document.getElementById("Button_Copy"), true );
				}
				else
				{
					buttonEnable( document.getElementById("Button_Edit"), false );
					buttonEnable( document.getElementById("Button_Delete"), false );
					buttonEnable( document.getElementById("Button_New"), false );
					buttonEnable( document.getElementById("Button_Copy"), false );
				}
		<%
			} else {
			/// PLACE ALL LOCAL CONDITION CHECKING HERE ///
		%>
			if( document.all.<%=readonlyField%>.value == "<%=readonlyValue%>" )
			{
				buttonEnable( document.getElementById("Button_Edit"), false );
				buttonEnable( document.getElementById("Button_Delete"), false );
				document.getElementById("objNoEdit").value = "Y";
			}
			else
			{
				buttonEnable( document.getElementById("Button_Edit"), true );
				buttonEnable( document.getElementById("Button_Delete"), true );
				document.getElementById("objNoEdit").value = "";
			}
		<%
			/// END ///
			}
		%>
		
	<viewCfg:ViewLinks viewName="<%=viewParam%>">
	<%
		int iAccRights = clientSessionBean.getAccessRights( sAccRights, linkView );
		if( iAccRights > 0 ){
	%>
		document.all.tab_<%=linkView%>.disabled = false;
		//document.all.tab_<%//=linkView%>.className = classname;
		//document.all.tab_<%//=linkView%>.onmouseover="this.className='tab_over';";
		//document.all.tab_<%//=linkView%>.onmouseout="this.className='tab';";
	<%	} %>
	</viewCfg:ViewLinks>
	}
	//document.reAllSizeImages();
	//reSizeImages();
	/*
	$(".image_preview").qtip({
	   content: "<img src='" + $(this).attr("img") + "' style='width:100%'/>",
	   show: 'mouseover',
	   hide: 'mouseout'
	});*/
	
	$('.resizeme_image').unbind();
	$('.resizeme_image').each(function(){
	      var title = $(this).attr('title'); // has width,length attributes in format "x,y"
	      var alt = $(this).attr('src');   //has a path to the image  
	      $(this).qtip( {
	        content: "<img src='"+alt+"' style='width:100%;'>",
	  	   	show: 'mouseover',
		   	hide: 'mouseout'
	      });
	   });
	
	$("#obj_sel_data").trigger('object-select');
}

function findColorCtrl(id)
{
	for (var i = 0; i < $.jPicker.List.length; i++)
	{
		var ctrl = $.jPicker.List[i];
		if( ctrl.id == id ){
			return ctrl;
		}
	}
	return null;
}

function cancelEvent(event)
{
	if(window.event)
	{
		window.event.returnValue = false;
		//window.event.cancelBubble = true;
	}
	else
	{
		event.preventDefault();
		//event.stopPropagation();
	}
}

function disableDisplay( disableInput ) {
    if (disableInput) {
    	$(".form_fields").block({ message:null, showOverlay: true, overlayCSS: { opacity: 0.1 } });
    	$(".form_fields .blockOverlay").click(function(){
    		var sel_id = getSelID( "-1" );
    		if(sel_id != "-1" ){
    			$("#Button_Edit").click();
    		}
    	});
    } else {
    	$(".form_fields").block({ message:null, showOverlay: false, overlayCSS: { opacity: 0.1 } });
    }
}

function __escape(str){
	return encodeURIComponent(str);
}

function saveObject()
{
	var sel_id = getSelID( "-1" );
	var objData = "";
	objData += "view=<%=viewParam%>";
	objData += "&call=submit";
	if( editMode == 1 )
	{
		objData += "&id=" + escape(sel_id);
	}
	else if( editMode == 3 )
	{
		objData += "&id=" + escape("-" + sel_id);
	}
	else// if( editMode == 2 || parent.frames.objProxy.document.all.id.value == "" )
	{
		objData += "&id=-1";
	}

	var listSize = getListLength();
	// Customize - start
	<viewCfg:ViewForm viewName="<%=viewParam%>">
		<% if( inputType.equalsIgnoreCase( "boolean" ) == true ){ %>
			if( document.all.<%=inputField%>.checked == true )
			{
				objData += "&data_<%=inputField%>=Y";
			}
			else
			{
				objData += "&data_<%=inputField%>=N";
			}
		<% } else if( inputType.equalsIgnoreCase( "list" ) == true ){ %>
			var i;
			var sList = "";
			for( i = 0; i < document.all.<%=inputField%>.length; i++ )
				sList += document.all.<%=inputField%>.options.item( i ).value + ";";
			//alert( sList );
			//document.all.__data_<-%=inputField%>.value = sList;
			objData += "&data_<%=inputField%>=" + __escape(sList);
		<% } else if( inputType.equalsIgnoreCase( "html" ) == true ||
					  inputType.equalsIgnoreCase( "css" ) == true ||
					  inputType.equalsIgnoreCase( "js" ) == true ){ %>
			objData += "&data_<%=inputField%>=" + __escape( codemirrorCrtls['<%=inputField%>'].getCode() );
		<% } else if( inputType.equalsIgnoreCase( "password" ) == true ){ %>
			objData += "&data_<%=inputField%>=" + __escape(document.all.<%=inputField%>.value);
			objData += "&data_<%=inputField%>_vrfy=" + __escape(document.all.<%=inputField%>_vrfy.value);
		<% } else if( inputField.equalsIgnoreCase( "id" ) == false ){ %>
           	//document.all.__data_<-%=inputField%>.value = document.all.<-%=inputField%>.value;
			objData += "&data_<%=inputField%>=" + __escape(document.all.<%=inputField%>.value);
		<% } %>
	</viewCfg:ViewForm>
	window.top.startAnimation();
	g_client.makeRequest("<%=request.getContextPath()%>/ViewCmd", objData);
}

function setupToolbar( type )
{
	var toolbarHTML = "";
	if( type == 0 )
	{
		toolbarHTML += buttonDraw( B_EDIT, '', 'Edit', 'buttonCallback', 'Button_Edit' );
		<% if( viewConfigBean.getListVisible() && formOnly == false ){ %>
		toolbarHTML += buttonDraw( B_COPY, '', 'Copy', 'buttonCallback', 'Button_Copy' );
		toolbarHTML += buttonDraw( B_DELETE, '', 'Delete', 'buttonCallback', 'Button_Delete' );
		<% } %>
	}
	else if( type == 1 )
	{
		toolbarHTML += buttonDraw( B_SUBMIT, '', '<%=viewConfigBean.getButtonText("Save")%>', 'buttonCallback', 'Button_Save' );
		toolbarHTML += buttonDraw( B_CANCEL, '', 'Cancel', 'buttonCallback', 'Button_Cancel' );
	}
	
	toolbarHTML += "&nbsp;&nbsp;"
	<viewCfg:ViewToolbar viewName="<%=viewParam%>" buttonType="form">
	toolbarHTML += buttonDraw( <%=buttonEventNum%>, '', '<%=buttonText%>', 'eventHandler', 'Button_Custom<%=buttonEventNum%>' );
	</viewCfg:ViewToolbar>

	$("#edittoolbar").html(toolbarHTML);

	if( type == 0 )
	{
		// Disable EDIT button since there is no data to edit... yet!
		buttonEnable( document.all.Button_Edit, false );
		buttonEnable( document.all.Button_Delete, false );
	}

	//return toolbarHTML;
}

function listboxHeader( xmlData, curEditMode, adminRights, accessLevel, accessDisplay, viewTitle )
{
	var listboxHTML = "";

	var listBox = xmlData.getNodeList( "collection" ).getNode(0);
	var start = parseInt( listBox.getAttribute("objStart") );
	var length = parseInt( listBox.getAttribute("objCount") );
	var dataCount = xmlData.getNodeList( "object" ).getLength();
	
	listboxHTML += "<div id='toolbar' class='ui-widget-header'>";
	listboxHTML += "<div style='float:left;'>";
	if( curEditMode == "Y" )
	{
		listboxHTML += buttonDraw( B_NEW, '', 'New', 'buttonCallback', 'Button_New' );
		//listboxHTML += buttonDraw( B_COPY, '', 'Copy', 'buttonCallback', 'Button_Copy' );
		//listboxHTML += buttonDraw( B_DELETE, '', 'Delete', 'buttonCallback', 'Button_Delete' );
	}
	else if( curEditMode == "X" )
	{
		listboxHTML += buttonDraw( B_ADD, '', 'Add', 'buttonCallback', 'Button_New' );
		listboxHTML += buttonDraw( B_REMOVE, '', 'Remove', 'buttonCallback', 'Button_Delete' );
	}
	listboxHTML += buttonDraw( B_SEARCH, '', 'Search', 'buttonCallback', 'Button_Search' );
	var expandText;
	if( expandMode == 0 )
		expandText = 'Expand';
	else
		expandText = 'Collapse';

	if( length == dataCount || expandMode != 0){
		listboxHTML += buttonDraw( B_EXPAND, '', expandText, 'buttonCallback', 'Button_Expand' );
	}
	
	listboxHTML += "&nbsp;&nbsp;"
	
	//listboxHTML += "&nbsp;&nbsp;"

	<viewCfg:ViewToolbar viewName="<%=viewParam%>" buttonType="list">
	listboxHTML += buttonDraw( <%=buttonEventNum%>, '', '<%=buttonText%>', 'eventHandler', 'Button_Custom<%=buttonEventNum%>' );
	</viewCfg:ViewToolbar>

	if( accessLevel == "user"  || accessLevel == "role" )
	{
		if( accessDisplay == null || accessDisplay.length == 0 ) accessDisplay = "0";
		if( adminRights == "Y" )
		{
			listboxHTML += "<select id='access_level' onchange='changeAccessLevel(this.value);'>";
			listboxHTML += "<option ";
			if( accessDisplay == "2" ) listboxHTML += "selected";
			listboxHTML += " value='2'>All " + viewTitle + "</option>";
			listboxHTML += "<option ";
			if( accessDisplay == "0" ) listboxHTML += "selected";
			listboxHTML += " value='0'>My " + viewTitle + "</option>";
			if( false )//accessLevel == "role" )
			{
				listboxHTML += "<option ";
				if( accessDisplay == "1" ) listboxHTML += "selected";
				listboxHTML += " value='1'>My Teams " + viewTitle + "</option>";
			}
			listboxHTML += "</select>";
		}
	}

	listboxHTML += "</div><div style='float:right;'>";
	if( start > 0 )
	{
		listboxHTML += buttonDraw( B_FORWARD, '', '<<', 'buttonCallback', 'Button_Forward' );
	}

	if( length == dataCount ){
		listboxHTML += buttonDraw( B_BACK, '', '>>', 'buttonCallback', 'Button_Back' );
		$("#Button_Expand").attr("disabled", false);
	}
	else{
		$("#Button_Expand").attr("disabled", true);
	}

	listboxHTML += "</div><div style='clear:both;'></div>";
	listboxHTML += "</div>";
	return listboxHTML;
}

function executeDrilldown( view, parent_view, reference, tab, view_idx )
{
	window.top.startAnimation();
	
	//this.location = "objectpanels.jsp?view=" + view + "&" + reference + "=" + getSelID( "-1" );

       // Set the 'view' form parameter to the link view
	document.all.postView.value = view;
	<viewCfg:ViewLinks viewName="<%=viewParam%>">
		document.all.item( "<%=linkView%>_filter_<%=linkReference%>" ).value = getSelID( "-1" );
	</viewCfg:ViewLinks>

	// Set TAB select setting for retun info
	document.all.item( parent_view + "_seltab" ).value = tab;
	document.all.item( "<%=viewParam%>_searchStr" ).value = searchStr;

       // Set objects START setting for reutrn info and maintaining of object selection
	//var xmlDoc = g_client.getXMLResponse();
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	//var listBox = parent.frames.objDoc.xmlObjList.XMLDocument.getElementsByTagName( "collection" ).item(0);
	var start = listBox.getAttribute("objStart");
	document.all.item( "<%=viewParam%>_objstart" ).value = parseInt(start) + getSelIndex();

	//////////////////////////////////////////////////////////////////////////////
	// This is really ugly but... I had to stop the runaway nesting somehow:-)
	var kill_index = view_idx + 1;
       if( document.all.item( "root_view_" + kill_index ) != null )
		document.all.item( "root_view_" + kill_index ).name = "";
	//////////////////////////////////////////////////////////////////////////////
	document.getElementById("obj_sel_data").submit();
}

function executeLink( view, parent_view, reference, tab, view_idx )
{
	window.top.startAnimation();
	
    // Set the 'view' form parameter to the link view
	document.all.postView.value = view;
	<viewCfg:ViewLinks viewName="<%=viewParam%>">
		document.all.item( "<%=linkView%>_filter_<%=linkReference%>" ).value = getSelID( "-1" );
	</viewCfg:ViewLinks>

	// Set TAB select setting for retun info
	document.all.item( parent_view + "_seltab" ).value = tab;

       // Set objects START setting for reutrn info and maintaining of object selection
	//var xmlDoc = g_client.getXMLResponse();
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	//var listBox = parent.frames.objDoc.xmlObjList.XMLDocument.getElementsByTagName( "collection" ).item(0);
	var start = listBox.getAttribute("objStart");
	document.all.item( "<%=viewParam%>_objstart" ).value = parseInt(start) + getSelIndex();

	//////////////////////////////////////////////////////////////////////////////
	// This is really ugly but... I had to stop the runaway nesting somehow:-)
	var kill_index = view_idx + 1;
       if( document.all.item( "root_view_" + kill_index ) != null )
		document.all.item( "root_view_" + kill_index ).name = "";
	//////////////////////////////////////////////////////////////////////////////
	document.getElementById("obj_sel_data").submit();
}

function executeDrillup( parent_view, view_idx )
{
	window.top.startAnimation();
	
	// Set the 'view' form parameter to the link view
	document.all.postView.value = parent_view;

       // Set TAB select setting for retun info
	document.all.item( parent_view + "_seltab" ).value = 0;

       // Set objects START setting for reutrn info and maintaining of object selection
	//if( document.all.item( parent_view + "_objstart" ) != null )
	//{
	//	document.all.objStart.value = document.all.item( parent_view + "_objstart" ).value;
	//}

	//////////////////////////////////////////////////////////////////////////////
	// This is really ugly but... I had to stop the runaway nesting somehow:-)
	var kill_index = view_idx;
	if( kill_index < 0 ) kill_index = 0;
       if( document.all.item( "root_view_" + kill_index ) != null )
		document.all.item( "root_view_" + kill_index ).name = "";
	//////////////////////////////////////////////////////////////////////////////
	document.getElementById("obj_sel_data").submit();
}

function executePropertiesTab( view )
{
	if( document.all.item( view + "_objstart" ) != null )
	{
           this.location = "view_obj.jsp?view=" + view + "&objStart=" + document.all.item( view + "_objstart" ).value;
	}
	else
	{
		this.location = "view_obj.jsp?view=" + view;
	}
}

// moveOptionsUp
//
// move the selected options up one location in the select list
//
function moveOptionsUp(selectId) {
 var selectList = document.getElementById(selectId);
 var selectOptions = selectList.getElementsByTagName('option');
 for (var i = 1; i < selectOptions.length; i++) {
  var opt = selectOptions[i];
  if (opt.selected) {
   selectList.removeChild(opt);
   selectList.insertBefore(opt, selectOptions[i - 1]);
     }
    }
}

// moveOptionsDown
//
// move the selected options down one location in the select list
//
function moveOptionsDown(selectId) {
 var selectList = document.getElementById(selectId);
 var selectOptions = selectList.getElementsByTagName('option');
 for (var i = selectOptions.length - 2; i >= 0; i--) {
  var opt = selectOptions[i];
  if (opt.selected) {
   var nextOpt = selectOptions[i + 1];
   opt = selectList.removeChild(opt);
   nextOpt = selectList.replaceChild(opt, nextOpt);
   selectList.insertBefore(nextOpt, opt);
     }
    }
}

--></script>