/*
	=================================================================================
	G L O B A L   L I S T V I E W   C O L L E C T I O N
	=================================================================================
*/
var g_VMListViewMap = new Array();
var g_oTargetObj;


/*
	=================================================================================
	S T A T I C   F U N C T I O N S
	=================================================================================
*/
function GetVMListView(elem)
{
	var currentElem = elem;
	while(currentElem.className != "vmlistview" && currentElem.parentNode != null)
	{
		currentElem = currentElem.parentNode;
	}
	if(currentElem.parentNode == null) return null;
	else return g_VMListViewMap[currentElem.id-1];
}
function VMGetEvent(e)
{
	var elem;
	if(window.event)
	{
		elem = event.srcElement;
		e = event;
	}
	else
	{
		elem = e.originalTarget;
	}
	return {src:elem,event:e};
}
function VMEventHandle_MouseMove(e)
{
	evt = VMGetEvent(e);
	var _this;
	if(g_oTargetObj) _this=g_oTargetObj;
	else _this = GetVMListView(evt.src);
	if(_this) _this.onColumnMove(evt.src,evt.event);
}
function VMEventHandle_MouseDown(e)
{
	evt = VMGetEvent(e);
	var _this=GetVMListView(evt.src);
	if(_this) _this.onColumnDown(evt.src,evt.event);
}
function VMEventHandle_MouseUp(e)
{
	evt = VMGetEvent(e);
	var _this=GetVMListView(evt.src);
	if(_this) _this.onColumnUp(evt.src,evt.event);
}


/*
	=================================================================================
	M A I N   L I S T V I E W   C L A S S
	=================================================================================
*/
function VMListView(parentid)
{
	var iID = g_VMListViewMap.push(this);

	///////////////////////////////////////////
	this.m_oTargetCol;
	
	// Focus elements
	this.m_oSelectedRow;
	this.m_oSelectedCol;
	
	// Events
	this.m_onSelectRow;
	
	//////////////////////////////////////
	this.m_onSortColumn;
	this.m_sortOrder;
	
	///////////////////////////////////

	this.imagePath = "";
	this.parent = document.getElementById(parentid);
	this.m_Columns = new Array();
	this.m_sortOrder = 0;	// 0 (asc) : 1 (desc)
	this.m_ReSizeMode = false;
	this.m_listView = document.createElement("TABLE");
	this.m_listView.className = "vmlistview";
	this.m_listView.id = iID;
	//this.m_listView.style.pixelWidth = 1000;
	//this.m_listView.cellPadding = 0;
	this.m_listView.cellSpacing = 0;
	this.m_listViewBody = document.createElement("TBODY");
	this.m_bDraggingColumn = false;
	this.m_listView.appendChild(this.m_listViewBody);
	this.parent.appendChild(this.m_listView);
	
	this.setImagePath = function(imgpath){this.imagePath=imgpath;}
}

/**
 * Set listview event callback
 *
 * @param evt		event id
 * @param callback	callback function
 */
VMListView.prototype.attachEvent = function(evt, callback)
{
	switch(evt)
	{
	case "onRowSelect":
		this.m_onSelectRow = callback;
		break
	case "onSort":
		this.m_onSortColumn = callback;
		break;
	}
}

/**
 * Set listview columns
 *
 * @param cols	array of columns objects
 */
VMListView.prototype.setColumns = function(cols)
{
	for(var i = 0; i < cols.length; i++)
	{
		var col = cols[i];

		// Push column into array
		this.m_Columns.push(col);
	}
}
 function MouseMoveColumn(e)
 {
	var elem;
	if(window.event)
	{
		elem = event.srcElement;
		e = event;
	}
	else
	{
		elem = e.originalTarget;
	}
	if(elem.className == "column_header")
	{
		var offsetX;
		if( window.event ) offsetX = e.offsetX;
		else offsetX = (e.clientX-posLeft(col));
		//debugLogWrite("offsetX="+offsetX+", col.clientWidth="+col.clientWidth);
		if(offsetX >= (elem.clientWidth-4))
		{
			//this.m_ReSizeMode = true;
			SetGlobalCursor("w-resize");	// Enable col-size cursor
		}
		else
		{
			//this.m_ReSizeMode = false;
			SetGlobalCursor("default");	// Remove col-size cursor
		}
	}
 }
 
/**
 * Draws listview frame
 *
 * @param cols	array of columns objects
 */
VMListView.prototype.draw = function()
{
	var oTR = document.createElement("TR");
	var totalWidth = 0;
	for(var i = 0; i < this.m_Columns.length; i++)
	{
		var oTH = document.createElement("TH");
		oTH.style.overflow = "hidden";
		oTH.noWrap = true;
		oTH.className = "column_header";
		//oTH.onmousedown = this.onSortColumn;
		oTH.onmousedown = function(){var _this=GetVMListView(this);if(_this)_this.onSortColumn(this);}
		//oTH.onmousemove = MouseMoveColumn;
		//oTH.style.pixelWidth = this.m_Columns[i].width;
		oTH.style.width = this.m_Columns[i].width + "px";
		totalWidth += this.m_Columns[i].width;
		
//		var oDIV = document.createElement("DIV");
//		oDIV.style.width = "98%";
//		oDIV.style.width = this.m_Columns[i].width + "px";
//		oDIV.noWrap = true;
//		oDIV.style.overflow = "hidden";
//		oDIV.innerText = this.m_Columns[i].title;
//		oTH.appendChild(oDIV);
/*
		var oTxt = document.createTextNode(this.m_Columns[i].title);
		//oTxt.style.overflow = "hidden";
		oTH.appendChild(oTxt);
		var oSpc = document.createTextNode(" ");
		oTH.appendChild(oSpc);
		var oImg = document.createElement("IMG");
		oImg.src = this.imagePath + "sort_asc.gif";
		oImg.align = "top";
		oImg.style.visibility = "hidden";
		oTH.appendChild(oImg);
		oTR.appendChild(oTH);
*/

		var oTbl = document.createElement("TABLE");
		oTbl.className = "vmlistviewhdr";
		oTbl.cellSpacing = 0;
		oTbl.cellpadding = 0;
		oTbl.border = 0;
		var oTblBody = document.createElement("TBODY");
		oTbl.appendChild(oTblBody);
		var oTblRow = document.createElement("TR");
		var oTblCol1 = document.createElement("TH");
		oTblCol1.className = "column_title";
		var oTxt = document.createElement("DIV");
		oTxt.innerHTML = this.m_Columns[i].title;
		//oTxt.style.height = "1px";
		oTxt.style.overflow = "hidden";
		//oTxt.style.pageBreakAfter = "";
		//oTH.appendChild(oTxt);
		
		oTblCol1.appendChild(oTxt);
		oTblRow.appendChild(oTblCol1);
		var oTblCol2 = document.createElement("TH");
		oTblCol2.className = "column_title";
		//var oSpc = document.createTextNode(" ");
		//oTH.appendChild(oSpc);
		
		var oImg = document.createElement("IMG");
		oImg.src = this.imagePath + "sort_asc.gif";
		oImg.align = "top";
		oImg.style.visibility = "hidden";
		//oImg.style.float = "left";
		//oImg.height = "100%";
		//oTH.appendChild(oImg);
		//oTH.innerHTML = "<div nowrap style=\"overflow:hidden;\">" + this.m_Columns[i].title + "</div>";
		oTblCol2.appendChild(oImg);
		oTblRow.appendChild(oTblCol2);
		
		oTblBody.appendChild(oTblRow);
		oTH.appendChild(oTbl);
		
		oTR.appendChild(oTH);

	}
	
	var oTH = document.createElement("TH");
	oTH.noWrap = true;
	//oTH.className = "column_header";
	//oTH.onmousedown = this.onSortColumn;
	//oTH.onmousedown = function(){var _this=GetVMListView(this);if(_this)_this.onSortColumn(this);}
	//oTH.onmousemove = MouseMoveColumn;
	//oTH.style.pixelWidth = this.m_Columns[i].width;
	oTH.style.width = "100%";
	//totalWidth += this.m_Columns[i].width;
	oTR.appendChild(oTH);
	
	//this.m_listView.style.pixelWidth = totalWidth+2;
	this.m_listView.width = (totalWidth+2) + "px";
	this.m_listViewBody.appendChild(oTR);
	
	AttachToEvent(document,"mousemove",VMEventHandle_MouseMove);
	AttachToEvent(document,"mousedown",VMEventHandle_MouseDown);
	AttachToEvent(document,"mouseup",VMEventHandle_MouseUp);
	//AttachToEvent(document,"mousemove",new Function("var _this;if(g_oTargetObj)_this=g_oTargetObj;else _this=GetVMListView(event.srcElement);if(_this)_this.onColumnMove(event.srcElement);"));
	//document.onmousemove = function(){var _this;if(g_oTargetObj)_this=g_oTargetObj;else _this=GetVMListView(event.srcElement);if(_this)_this.onColumnMove(event.srcElement);};
	//AttachToEvent(document,"mousedown",this.onMouseDown);
	//AttachToEvent(document,"mousedown",this.onMouseDown);
	//AttachToEvent(document,"mousedown",new Function("var _this=GetVMListView(event.srcElement);if(_this)_this.onColumnDown(event.srcElement);"));
	//AttachToEvent(document,"mouseup",this.onMouseUp);
	//AttachToEvent(document,"mouseup",new Function("var _this=GetVMListView(event.srcElement);if(_this)_this.onColumnUp(event.srcElement);"));
}

/**
 * Adds a new row to the listview
 *
 * @param row	array of row data objects
 */
VMListView.prototype.clearAllRows = function()
{
	while( this.m_listViewBody.rows.length > 1 )
	{
		this.m_listViewBody.removeChild(this.m_listViewBody.rows[1]);
	}
}

/**
 * Adds a new row to the listview
 *
 * @param row	array of row data objects
 */
VMListView.prototype.addRow = function(id, row)
{
	var oTR = document.createElement("TR");
	oTR.id = id;
	oTR.className = "row_normal";
	oTR.onmouseover = function(){var _this=GetVMListView(this);if(_this)_this.onRowOver(this);}
	//oTR.onmouseover = this.onRowOver;
	oTR.onmouseout = function(){var _this=GetVMListView(this);if(_this)_this.onRowOut(this);}
	//oTR.onmouseout = this.onRowOut;
	oTR.onmousedown = function(){var _this=GetVMListView(this);if(_this)_this.onRowDown(this);}
	//oTR.onmousedown = this.onRowDown;
	for(var i = 0; i < row.length; i++)
	{
		var oTD = document.createElement("TD");
		oTD.noWrap = true;

		//////////////////////////////////
//		var oDIV = document.createElement("DIV");
		//oDIV.style.width = "98%";
//		oDIV.noWrap = true;
//		oDIV.innerText = row[i].display;
		//oDIV.style.width = "200px";
//		oDIV.style.overflow = "hidden";
		//var oTxt = document.createTextNode(row[i].display);
		//oDIV.appendChild(oTxt);
//		oTD.appendChild(oDIV);
		//////////////////////////////////////////
		//////////////////////////////////
		//var oINPUT = document.createElement("INPUT");
		//oINPUT.type = "text";
		//oINPUT.value = row[i].display;
		//oTD.appendChild(oINPUT);
		
		if( row[i].html == null || row[i].html != true )
		{ 
			//////////////////////////////////////////
			var oTxt = document.createTextNode(row[i].display);
			oTD.appendChild(oTxt);
		}
		else
		{
			//////////////////////////////////
			//////////////////////////////////
			oTD.innerHTML = "<div style=\"overflow:hidden;\">" + row[i].display + "</div>";
			//////////////////////////////////////////
		}

		oTR.appendChild(oTD);
	}
	this.m_listViewBody.appendChild(oTR);
}

/**
 * Handles row mouse over
 */
VMListView.prototype.onSortColumn = function(col)
{
	//var oCol = g_Columns(this.cellIndex);
	//if( oCol.sortorder == null )
	//	oCol.sortorder = 0;
	//else
	//	oCol.sortOrder ^= 1;
	if(this.m_ReSizeMode==false)
	{
		// Reset the previously selected column header - if there is one
		if( this.m_oSelectedCol != null )
		{
			var oPrevImg = this.m_oSelectedCol.getElementsByTagName("IMG")[0];
			oPrevImg.style.visibility = "hidden";
		}
		
		// Set this as the selected column
		this.m_oSelectedCol = col;
		
		// Update column sorting data
		this.m_sortOrder ^= 1;
		this.m_sortCol = col.cellIndex;
		var oImg = col.getElementsByTagName("IMG")[0];
		if(this.m_sortOrder == 0)
		{
			oImg.src = this.imagePath + "sort_asc.gif";
			oImg.style.visibility = "visible";
		}
		else
		{
			oImg.src = this.imagePath + "sort_desc.gif";
			oImg.style.visibility = "visible";
		}
		
		// Fire event
		if( this.m_onSortColumn != null ) this.m_onSortColumn( this.m_Columns[col.cellIndex].id, this.m_sortOrder );
	}
}

/**
 * Handles row mouse over
 */
VMListView.prototype.onRowOver = function(row)
{
	if( this.m_oSelectedRow == null || row.id != this.m_oSelectedRow.id )
		row.className = "row_over";
}

/**
 * Handles row mouse over
 */
VMListView.prototype.onRowOut = function(row)
{
	if( this.m_oSelectedRow == null || row.id != this.m_oSelectedRow.id )
		row.className = "row_normal";
}

/**
 * Handles row mouse over
 */
VMListView.prototype.onRowDown = function(row)
{
	if( this.m_oSelectedRow != null )
		this.m_oSelectedRow.className = "row_normal";
	row.className = "row_down";
	this.m_oSelectedRow = row;
	if( this.m_onSelectRow != null ) this.m_onSelectRow( row.id, row.rowIndex );
}

/**
 * Handles mouse movement events
 */
VMListView.prototype.onColumnMove = function(col, e)
{
	if(this.m_oTargetCol != null)
	{
		if(this.m_bDraggingColumn == true)
		{
			//var clientX
			//if( window.event ) offsetX = e.offsetX;
			//else offsetX = (e.clientX-posLeft(col));
			
			// Re-size column based on cursor position
			//debugLogWrite("e.clientX="+e.clientX);
			var loc = e.clientX - posLeft(this.m_oTargetCol);
			var mainTable = this.m_oTargetCol.parentNode.parentNode.parentNode;
			var containerDIV = mainTable.parentNode;

			var newColWidth = loc + containerDIV.scrollLeft;// - ((mainTable.rows(0).cells.length+1)*5);
			//if( newColWidth < (this.m_oTargetCol.clientWidth-25) ) return;
			
			//mainTable.style.pixelWidth += (loc - this.m_oTargetCol.style.pixelWidth) + containerDIV.scrollLeft;
			if( window.event )
			{
				mainTable.style.pixelWidth += (newColWidth - this.m_oTargetCol.style.pixelWidth);
				this.m_oTargetCol.style.pixelWidth = newColWidth;// - this.m_oTargetCol.style.paddingLeft - this.m_oTargetCol.style.paddingRight;
			}
			else
			{
				mainTable.style.width += (newColWidth - this.m_oTargetCol.style.pixelWidth) + "px";
				this.m_oTargetCol.style.width = newColWidth + "px";
			//	debugLogWrite("this.m_oTargetCol.style.width="+newColWidth);
			}
			//window.status = loc + ": pixelWidth=" + (newColWidth) + ", clientWidth=" + this.m_oTargetCol.clientWidth;
			// Update all cells in column
			
			////for(i=1; i < mainTable.rows.length; i++)
			////{
			////	var td_cell = mainTable.rows(i).cells(this.m_oTargetCol.cellIndex);
			////	td_cell.style.pixelWidth = newColWidth;
			////}
			////////////////////////////////////////
			
			//window.status = "cellindex=" + this.m_oTargetCol.cellIndex;
		}
	}
	else
	{
		//var col = event.srcElement;
		//debugLogWrite("col.className="+col.className);
		if(col.className == "column_header")
		{
			var mainTable = col.parentNode.parentNode.parentNode;
			var containerDIV = mainTable.parentNode;
			//debugLogWrite("containerDIV.scrollLeft="+containerDIV.scrollLeft);

			var offsetX;
			if( window.event ) offsetX = e.offsetX;
			else offsetX = (e.clientX-posLeft(col)) + containerDIV.scrollLeft;
			//debugLogWrite("offsetX="+offsetX+", col.clientWidth="+col.clientWidth);
			if(offsetX >= (col.clientWidth-4))
			{
				this.m_ReSizeMode = true;
				SetGlobalCursor("w-resize");	// Enable col-size cursor
			}
			else
			{
				this.m_ReSizeMode = false;
				SetGlobalCursor("default");	// Remove col-size cursor
			}
		}
		//else SetGlobalCursor("default");	// Remove col-size cursor
		else if( col.style.cursor != "default" ) SetGlobalCursor("default");	// Remove col-size cursor
	}
}

/**
 * Handles mouse click events
 */
VMListView.prototype.onColumnDown = function(col, e)
{
	//var col = event.srcElement;
	if(col.className == "column_header")
	{
		//AttachToEvent(document,"mousemove",VMEventHandle_MouseMove);
		//AttachToEvent(document,"mouseup",VMEventHandle_MouseUp);
		
		var mainTable = col.parentNode.parentNode.parentNode;
		var containerDIV = mainTable.parentNode;
			
		var offsetX;
		if( window.event ) offsetX = e.offsetX;
		else offsetX = (e.clientX-posLeft(col)) + containerDIV.scrollLeft;
		//debugLogWrite("offsetX="+offsetX+", col.clientWidth="+col.clientWidth);
		if(offsetX >= (col.clientWidth-4))
		{
			this.m_bDraggingColumn = true;
			g_oTargetObj = this;
			this.m_oTargetCol = col;
			
			// Turn off selection - it looks funny while dragging a column
			if( window.event ) 	document.onselectstart = function() {return false;} // ie			
		}
		else
		{
			this.m_bDraggingColumn = false;
			
			// Release target column
			g_oTargetObj = null
			this.m_oTargetCol = null;
			
			// Turn selection bake to default
			if( window.event ) 	document.onselectstart = null;
		}
	}
}

/**
 * Handles mouse click events
 */
VMListView.prototype.onColumnUp = function(col)
{	
	this.m_bDraggingColumn = false;
	g_oTargetObj = null
	this.m_oTargetCol = null;
	//DettachEvent(document,"mousemove",VMEventHandle_MouseMove);
	//DettachEvent(document,"mouseup",VMEventHandle_MouseUp);
	
	// Turn selection bake to default
	if( window.event ) 	document.onselectstart = null;
	
	// Remove col-size cursor
	SetGlobalCursor("default");
}

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
//	D E B U G   L O G   M E T H O D S
//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

function keyPress(e)
{
	var altKey = 0;
	var ctrlKey = 0;
	var keyCode = 0;
	if(window.event)
	{
		altKey = event.altKey;
		ctrlKey = event.ctrlKey;
		keyCode = event.keyCode;
	}
	else
	{
		altKey = e.altKey;
		ctrlKey = e.ctrlKey;
		keyCode = e.which;
	}
	if( ctrlKey == true && altKey == true && keyCode == 68 )
		debugLogDisplay();
	else if( keyCode == 27 )
		debugLogRemove();
}

var g_dbgLog = new Array();

// Attach web collab events
AttachToEvent( document, "keydown", keyPress );

function debugLogWrite( msg )
{
	g_dbgLog.push( msg );
}

function debugLogDisplay()
{
	var theDebugDisp = document.getElementById( "THE_DEBUG_WINDOW" );
	if( theDebugDisp == null )
	{
		var screenwidth = getClientWidth();
		var screenHeight = getClientHeight();
		var winWidth = 460;
		var winHeight = 400;

		// Create display area
		theDebugDisp 						= document.createElement( "SPAN" );
		theDebugDisp.id 					= "THE_DEBUG_WINDOW";
		theDebugDisp.style.position 		= "absolute";
		theDebugDisp.style.visibility		= "visible";
		theDebugDisp.style.top 				= ((screenHeight - winHeight) / 2) + "px";
		theDebugDisp.style.left 			= ((screenwidth - winWidth) / 2) + "px";
		theDebugDisp.style.width			= winWidth + "px";
		theDebugDisp.style.height			= winHeight + "px";
		theDebugDisp.style.backgroundColor 	= "yellow";
		theDebugDisp.style.color 			= "navy";
		theDebugDisp.style.overflow			= "scroll";
		theDebugDisp.style.border 			= "1px solid navy";
		theDebugDisp.style.font 			= "8pt verdana";
		document.body.appendChild( theDebugDisp );
		theDebugDisp.innerHTML += "<br><strong>Debug Log:</strong>";
		for(var x = 0; x < g_dbgLog.length; x++)
			theDebugDisp.innerHTML += "<xmp style='border:0px;padding:0px;margin:0px;'>" + g_dbgLog[x] + "</xmp>";
	}
}

function debugLogRemove()
{
	var theDebugDisp = document.getElementById( "THE_DEBUG_WINDOW" );
	if( theDebugDisp != null )
	{
		document.body.removeChild( theDebugDisp );
	}
}

///////////////////////////////////////////////////
//////////////////////////////////////////////////
/////////////////////////////////////////////////