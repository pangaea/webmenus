<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->

<script type="text/javascript">

// 0 - read-only
// 1 - edit mode w/existing record
// 2 - edit mode w/new record
//var editMode = 0;
var returnText = "";
var sortBy = "";
var searchStr = "";
var sortOrder = "A";
var expandMode = 0;

var xmlTitle = new XDocument();
var g_xmlObjList = new XDocument();
//var g_MainListView;

var selected_lv_id = "-1";
var selected_lv_index = 0;

// Create the client object for global use
var g_client = new ObjectClient();

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

function getSelIndex()
{
	return selected_lv_index;
}

function getRandNum()
{
	var dt = new Date();
	return dt.getMilliseconds();
}

function createGrid()
{
    jQuery("#gridview").jqGrid({
   	    //url:'data.xml',
	    datatype:'clientSide',
   	    colModel:[
   		    {name:'id', index:'id', label: 'id', hidden:true, key:true}
   		 <viewCfg:ViewList viewName="<%=viewParam%>">
   		 	,{name:'<%=itemField%>', index:'<%=itemField%>', label:'<%=itemTitle%>', width:<%=itemWidth%>}
   		 </viewCfg:ViewList>
   	    ],
   	    rowNum:6,
   	    autowidth: true,
   	    //rowList:[10,20,30],
   	    //pager: jQuery('#gridbox'),
   	    sortname: 'id',
        viewrecords: true,
        sortorder: "desc",
        //height: 80,
        //caption:"XML Example",
        onSelectRow: listviewSelect,
	    onSortCol: listviewSort
    });//.navGrid('#gridbox',{edit:false,add:false,del:false});
    //$("#gridview").fluidGrid({ example:"#view_obj_body", offset:-10 });
}

function initData()
{
	createGrid();
	
	// Make object request
	g_client.setListener( onDocumentLoaded );
	// CUSTOMIZE
	
	<%
		String sFilter = new String("");
		if( request.getParameter("filter") != null )
		{
			sFilter = "&" + request.getParameter("filter");
		}
	%>
	g_client.makeRequest("<%=request.getContextPath()%>/ViewCmd?view=<%=request.getParameter("view")%>&call=query<%=sFilter%>" + "&r=" + getRandNum());

	$(window).bind('resize', function() { 
	    // resize the datagrid to fit the page properly:
	    $('#gridview').fluidGrid({example:'#view_obj_body', offset:-50});

		//alert("test");
		//jQuery("#gridview").trigger("reloadGrid");
		//jQuery("#gridview").
		//createGrid();
		//drawGrid();
	});
}

function listviewSelect(id,index)
{
	prev_selected_lv_id = selected_lv_id;
	selected_lv_id = id;
	selected_lv_index = index;
	//viewObject(id);
}
function listviewSort(sortby,order)
{
	this.sortBy = sortby;
	if( sortOrder == "A" )
		sortOrder = "D";
	else
		sortOrder = "A";
	sortList( sortby );
}

function changeAccessLevel(newLevel)
{
	accessLevel = newLevel;
	refreshView( B_REFRESH );
}
/*
function listboxCallback( code, item )
{
	switch( code )
	{
	case 0:
		//var sel_id = getSelID( -1 );
		//viewObject( getSelID( "-1" ) );
		//viewObject( item );
		break;
	case 1:
		sortBy = item;
		if( sortOrder == "A" )
			sortOrder = "D";
		else
			sortOrder = "A";
		sortList( item );
		break;
	}
}
*/
function buttonCallback( item, param )
{
	var sel_id = getSelID( "-1" );
	switch( item )
	{
	case B_SUBMIT:
<%
		String popType = (String)request.getParameter( "poptype" );
		do{
			if( popType != null ){
				if( popType.equalsIgnoreCase( "eol" ) ){
%>
		var oNewOption = parent.document.createElement( "OPTION" );
		parent.document.getElementById("<%=request.getParameter("parentProp")%>").options.add( oNewOption );
		var text = g_xmlObjList.selectNodeList( "//object[@id='" + sel_id + "']/property[@name='<%=request.getParameter("displayProp")%>']" ).getNode(0).getText();
		//oNewOption.innerText = text;
		$(oNewOption).text(text);
		oNewOption.value = sel_id;
<%
					break;	// Only break from do...while
				}
				if( popType.equalsIgnoreCase( "addbyref" ) ){
%>
		parent.document.getElementById("<%=request.getParameter("parentProp")%>").value = sel_id;
		//parent.document.all.display_<%=request.getParameter("parentProp")%>.value = g_xmlObjList.selectNodeList( "//object[@id='" + sel_id + "']/property[@name='<%=request.getParameter("displayProp")%>']" ).getNode(0).getText();
		parent.saveObject();
		parent.editMode = 0;
<%
					break;	// Only break from do...while
				}
			}
%>
		parent.document.getElementById("<%=request.getParameter("parentProp")%>").value = sel_id;
		parent.document.getElementById("display_<%=request.getParameter("parentProp")%>").value = g_xmlObjList.selectNodeList( "//object[@id='" + sel_id + "']/property[@name='<%=request.getParameter("displayProp")%>']" ).getNode(0).getText();
<%
		}
		while(false);
%>
	
	case B_CANCEL:
		//window.close();
		//parent.hidePopWin();
		parent.closeDialog();
		break;
	
	case B_SEARCH:
		//refreshView( B_REFRESH );
		////var param = new Object();
		////param.caller = window;
		////window.showModalDialog( "view_obj_search.jsp?view=<%=request.getParameter("view")%>", param, "dialogHeight:200px;dialogWidth:250px;resizable:yes;status:no;" );
		//showPopWin( "view_obj_search.jsp?view=<%=request.getParameter("view")%>", 350, 200, null );
		showDialog('view_obj_search.jsp?view=<%=request.getParameter("view")%>', 'Search', 700, 360);
					break;

		//case B_FRONT:
		//case B_FORWARD:
			//moveView( A_FORWORD );
			//break;
		//case B_BACK:
		//case B_END:

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
	//g_idarray = new Object();
	
	jQuery("#gridview").clearGridData();
	var colsList = xmlTitle.getNodeList( "column" );
	var rowsList = g_xmlObjList.getNodeList( "object" );
	var selRow = null;

	var length;
	if( expandMode == 0 )
		length = <jsp:getProperty name="viewConfigBean" property="listSize"/>;
	else
		length = <jsp:getProperty name="viewConfigBean" property="listExpandSize"/>;

	//xmlGrid.push(rowsList.getLength());
	//xmlGrid.push('</records>');
	//alert(rowsList.getLength());
	//alert(length);
	for( i = 0; i < rowsList.getLength() && i < length; i++ )
	{
		//xmlGrid.push('<row>');
		var rowNode = rowsList.getNode( i );
		var vRow = new Object();
		for( var j = 0; j < colsList.getLength(); j++ )
		{
			var colNode = colsList.getNode( j );
			var propValueOf = colNode.getAttribute("valueof");
			var propView = colNode.getAttribute("view");
			var propNode = rowNode.selectNodeList( "property[@name='" + propValueOf + "']" ).getNode(0);
			var propVal = "";
			if( propNode != null )
			{
				propVal = propNode.getText();
			}
			//var dataDisplay;
			//if( mode == "I" || propView == null || propView == "" )
			//{
				//dataDisplay = propVal;
				//vRow[j] = {display:propVal,data:0,html:false};
				//alert(colNode.getAttribute("name"));
			vRow[colNode.getAttribute("name")] = propVal;
				//xmlGrid.push('<cell>');
				//xmlGrid.push(escape(propVal));
				//xmlGrid.push('</cell>');
			//}
			//else
			//{
			//	var propFKey = colNode.getAttribute("fkey");
			//	var propFKeyNode = rowNode.selectNodeList( "property[@name='" + propFKey + "']" ).getNode(0);//.item(0);
			//	var propViewID = propFKeyNode.getText();
//					//dataDisplay = "<a target='viewarea' href='objectpanels.jsp?view=" + propView + "&" + propView+ "_filter_id=" + propViewID + "'>" + propVal + "</a>";
			//	if( propVal.length == 0 ) propVal = "NULL";
			//	var dataDisplay = "<a href='objectpanels.jsp?view=" + propView + "&" + propView+ "_searchStr=%2526id%253D" + propViewID + "'>" + propVal + "</a>";
//					vRow[j] = {display:dataDisplay,data:0,html:true};
			//	vRow[colNode.getAttribute("name")] = dataDisplay;
			//}
			//alert(vRow[j]);
		}
		//xmlGrid.push('</row>');
		//g_MainListView.addRow(rowNode.getAttribute("id"), vRow);
//alert(vRow);
	    jQuery("#gridview").addRowData(rowNode.getAttribute("id"), vRow);
	    if( selRow == null ) selRow = rowNode.getAttribute("id");
	    //g_idarray[rowNode.getAttribute("id")] = i+1;
	}
	if( selRow != null ) jQuery("#gridview").setSelection(selRow);
}

//function onDocumentLoaded( sCode, sMsg )
function onDocumentLoaded( xml_doc )
{
	// Verify response status code
	if( g_client.getStatusCode() != 200 )
	{
		alert( "invalid response - " + g_client.getStatusCode() );
		return;
	}

	// Verify the response body
	if( xml_doc == null )
	{
		alert( "empty response recieved" );
		return;
	}

	// Attach response body to XDocument abstraction class
	g_xmlObjList.attach( xml_doc );

	// Break out the code, msg, and the call to which this originated
	var sCall = "";
	var sCode = "-1";
	var sMsg = "";
	var callNode = g_xmlObjList.getNodeList( "call" ).getNode(0);
	if( callNode != null ) sCall = callNode.getText();
	var codeNode = g_xmlObjList.getNodeList( "code" ).getNode(0);
	if( codeNode != null ) sCode = codeNode.getText();
	var msgNode = g_xmlObjList.getNodeList( "msg" ).getNode(0);
	if( msgNode != null ) sMsg = msgNode.getText();
	
	if( displayResponse( sCode, sMsg ) == true )	// frame: noop
	{
		<%	if( viewConfigBean.getByRef().length() > 0 ){ %>
		var mode = "X";
		<%	}else{ %>
		var mode = "Y";
		<%	} %>
			
		var accessLevel = "";
		//var length;
		//if( expandMode == 0 )
		//	length = <jsp:getProperty name="viewConfigBean" property="listSize"/>;
		//else
		//	length = <jsp:getProperty name="viewConfigBean" property="listExpandSize"/>;
	
		var listHTML = listboxHeader( g_xmlObjList, mode, "<%=request.getSession().getAttribute( "admin" )%>", "<jsp:getProperty name="viewConfigBean" property="access"/>", accessLevel, "<jsp:getProperty name="viewConfigBean" property="title"/>" );
			//listHTML += listboxDraw( xmlTitle, g_xmlObjList, <jsp:getProperty name="viewConfigBean" property="listSize"/>, listboxCallback, buttonCallback, "I" );
		ObjList.innerHTML = listHTML;
		drawGrid();

		/*
		g_MainListView.clearAllRows();
		
		// NEW LISTVIEW /////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		var colsList = xmlTitle.getNodeList( "column" );
		var rowsList = g_xmlObjList.getNodeList( "object" );
		for( i = 0; i < rowsList.getLength() && i < length; i++ )
		{
			var rowNode = rowsList.getNode( i );
			var vRow = new Array();
			for( var j = 0; j < colsList.getLength(); j++ )
			{
				var colNode = colsList.getNode( j );
				var propValueOf = colNode.getAttribute("valueof");
				var propView = colNode.getAttribute("view");
				var propNode = rowNode.selectNodeList( "property[@name='" + propValueOf + "']" ).getNode(0);
				var propVal = "";
				if( propNode != null )
				{
					propVal = propNode.getText();
				}
				//var dataDisplay;
				if( mode == "I" || propView == null || propView == "" )
				{
					//dataDisplay = propVal;
					vRow[j] = {display:propVal,data:0};
				}
				else
				{
					var propFKey = colNode.getAttribute("fkey");
					var propFKeyNode = rowNode.selectNodeList( "property[@name='" + propFKey + "']" ).getNode(0);//.item(0);
					var propViewID = propFKeyNode.getText();
					//dataDisplay = "<a target='viewarea' href='objectpanels.jsp?view=" + propView + "&" + propView+ "_filter_id=" + propViewID + "'>" + propVal + "</a>";
					var dataDisplay = "<a target='viewarea' href='objectpanels.jsp?view=" + propView + "&" + propView+ "_searchStr=%2526id%253D" + propViewID + "'>" + propVal + "</a>";
					vRow[j] = {display:dataDisplay,data:0};
				}
				//alert(vRow[j]);
			}
			g_MainListView.addRow(rowNode.getAttribute("id"), vRow);
		}*/
		///////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////
	}
}

function displayResponse( sCode, sMsg )
{
	if( sCode != "-1" )	// frame: noop
	{
		if( sCode != "0" )
		{
			alert( "(" + sCode + ") " + sMsg );
		}
		return true;
	}
	return false;
}

function refreshView( action )
{
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	var start = listBox.getAttribute("objStart");
	var length = listBox.getAttribute("objCount");
	var new_start;
	if( action == B_BACK )
	{
		new_start = parseInt(start) + parseInt(length);
	}
	else if( action == B_FORWARD )
	{
		new_start = parseInt(start) - parseInt(length);
	}
	else
	{
		new_start = 0;
	}
	var docURL = "<%=request.getContextPath()%>/ViewCmd?view=<%=request.getParameter("view")%>&call=query&objCount=" + length + "&objStart=" + new_start;
	if( sortBy != "" )
	{
		docURL += "&sortBy=" + sortBy + "&sortOrder=" + sortOrder;
	}
	if( searchStr != "" )
	{
		docURL += searchStr;
	}
	g_client.makeRequest(docURL + "&r=" + getRandNum());
}

function sortList( column_name )
{
	var listBox = g_xmlObjList.getNodeList( "collection" ).getNode(0);
	var start = listBox.getAttribute("objStart").getText();
	var length = listBox.getAttribute("objCount").getText();
	g_client.makeRequest("<%=request.getContextPath()%>/ViewCmd?view=<%=request.getParameter("view")%>&call=query&objCount=10&objStart=" + start + "&sortBy=" + column_name + "&sortOrder=" + sortOrder + "&r=" + getRandNum());
}

function setupButtons()
{
	var toolbarHTML = "<table align=\"right\">";
	toolbarHTML += "<tr>";
	toolbarHTML += "<td align=\"right\">";
	toolbarHTML += buttonDraw( B_SUBMIT, '', 'Select', 'buttonCallback', 'Button_Select' );
	toolbarHTML += "</td>";
	toolbarHTML += "<td align=\"right\">";
	toolbarHTML += buttonDraw( B_CANCEL, '', 'Cancel', 'buttonCallback', 'Button_Cancel' );
	toolbarHTML += "</td>";
	toolbarHTML += "</tr>";
	toolbarHTML += "</table>";
	buttonToolbar.innerHTML = toolbarHTML
	//return toolbarHTML;
}

function listboxHeader( xmlData, curEditMode, adminRights, accessLevel, accessDisplay, viewTitle )
{
	var listboxHTML = "";
	
	listboxHTML += "<div id='toolbar' class='ui-widget-header'>";
	listboxHTML += "<div style='float:left;'>";

	listboxHTML += buttonDraw( B_SEARCH, '', 'Search', 'buttonCallback', 'Button_Search' );

	listboxHTML += "&nbsp;&nbsp;"

	if( accessLevel == "user"  || accessLevel == "role" )
	{
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
	
	var listBox = xmlData.getNodeList( "collection" ).getNode(0);
	var start = parseInt( listBox.getAttribute("objStart") );
	var length = parseInt( listBox.getAttribute("objCount") );
	var dataCount = xmlData.getNodeList( "object" ).getLength();
	/*if( start > 0 )
	{
		listboxHTML += "<td>";
		listboxHTML += buttonDraw( B_FORWARD, '', '<<', 'buttonCallback', 'Button_Forward' );
		listboxHTML += "</td>";
	}
	var length = parseInt( listBox.getAttribute("objCount") );
	var dataCount = xmlData.getNodeList( "object" ).getLength();
	if( length == dataCount )
	{
		listboxHTML += "<td>";
	}
	else
	{
		listboxHTML += "<td style=\"visibility:hidden;\">";
	}
	listboxHTML += buttonDraw( B_BACK, '', '>>', 'buttonCallback', 'Button_Back' );*/
	//listboxHTML += "</td></tr></table></td></tr></table></td></tr><tr><td align='center'>";
	
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
</script>
