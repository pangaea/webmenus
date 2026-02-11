//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//// MAIN OBJECT /////////////////////////////////
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

/**
 *	method:		RenderMonth
 *	purpose:	Constructor
 */
function RenderMonth(canvas)
{
	/**
	 *	Class properties
	 */
	this.monthNames = ["January","Feburary","March","April","May","June","July","August","September","October","November","December"];
	this.monthDays = [31,28,31,30,31,30,31,31,30,31,30,31];
	this.weekDays = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
	this.showingDay = 0;
	this.showingMonth = 1;
	this.showingYear = 0;
	this.CalendarWidth = 350;
	this.cellHeight = 50;
	this.last_obj = null;
	this.calendarBlock = canvas;
	this.dayCells = null;
	//this.showingDay = 0;	// initialized
	
	this.m_selectDay = null;						// callback function
	this.m_navigateMonth = null;					// callback function

	/**
	 *	Class methods
	 */
	this.setEventSelectDay = function( callback )
	{
		this.m_selectDay = callback;
	}
	this.setEventNavigateMonth = function( callback )
	{
		this.m_navigateMonth = callback;
	}
}

/**
 *	Draw Calendar HTML Markup
 *
 * @param width		width of calendar in pixels
 * @param height	height of calendar cell in pixels
 * @param month		start month by index (0-11)
 * @param year		start year (4-digits)
 */
RenderMonth.prototype.drawMonth = function( cellheight, width, month, year )
{
	this.CalendarWidth = width;
	this.cellHeight = cellheight;

	this.dayCells = new Array();
	
	// Determine the selected month/year
	var now = new Date();
	if( month == null ) this.showingMonth = now.getMonth();
	else this.showingMonth = month;
	if( year == null ) this.showingYear = now.getFullYear();
	else this.showingYear = year;
	if( this.showingDay == 0 ) this.showingDay = now.getDate();
	
	// Calculate cell width
	var cellWidth = this.CalendarWidth / 7;

	// Calculate month information based on year
	var leapYear = this.showingYear%4==0?1:0;
	var daysIntoCurYear = 0;
	for( i = 0; i < this.showingMonth-1; i++ )
	{
		if( i == 1 && leapYear > 0 ) daysIntoCurYear += 29;
		else daysIntoCurYear += this.monthDays[i];
	}
	var curMonthDays = this.monthDays[this.showingMonth-1];
	if( this.showingMonth == 2 && leapYear > 0 ) curMonthDays++;
	var calcYear = this.showingYear - 1;
	var weekday = ((calcYear * 365) + Math.floor(calcYear/4) + daysIntoCurYear) % 7;

	// Draw calendar block
	var gridCount = 0;
	var monthCount = "";

	////////////////////////////////////////
	// Append main table
	this.calendarBlock.innerHTML = "";
	var oMainTable = this.calendarBlock.appendChild(document.createElement("TABLE"));
	oMainTable.cellPadding = 1;
	oMainTable.cellSpacing = 0;
	oMainTable.width = (this.CalendarWidth+"px");					//*** Overrides stylesheet
	var oOutterRow = oMainTable.insertRow(-1);
	var oOutterCell = oOutterRow.insertCell(-1);
	oOutterCell.valign = "top";

	// Draw calendar title
	var oCalendarTable = oOutterCell.appendChild(document.createElement("TABLE"));
	oCalendarTable.cellSpacing = 0;
	oCalendarTable.align = "center";
	oCalendarTable.width = "100%";
	var oMainRow = oCalendarTable.insertRow(-1);
	oMainRow.className = "candendar_heading";

	// Append 'back' button
	var oMainCellL = oMainRow.insertCell(-1);
	oMainCellL.align = "left";
	var oBackButton = document.createElement("INPUT");
	oBackButton.type = "button";
	oBackButton.value = "<<";
	oBackButton.parentCal = this;
	oBackButton.onclick = function(){this.parentCal.retractMonth();};
	oMainCellL.appendChild(oBackButton);

	// Append month display
	var oMainCellC = oMainRow.insertCell(-1);
	oMainCellC.align = "center";
	oMainCellC.innerHTML =  this.monthNames[this.showingMonth-1] + " " + this.showingYear;

	// Append 'next' button
	var oMainCellR = oMainRow.insertCell(-1);
	oMainCellR.align = "right";
	var oNextButton = document.createElement("INPUT");
	oNextButton.type = "button";
	oNextButton.value = ">>";
	oNextButton.parentCal = this;
	oNextButton.onclick = function(){this.parentCal.advanceMonth();};
	oMainCellR.appendChild(oNextButton);	

	// Create calendar body
	var oOutterRow2 = oMainTable.insertRow(-1);
	var oOutterCell2 = oOutterRow2.insertCell(-1);
	var oBodyTable = oOutterCell2.appendChild(document.createElement("TABLE"));
	oBodyTable.height = "100%";
	oBodyTable.width = "100%";
	var oCalTitleRow = oBodyTable.insertRow(-1);
	for( i = 0; i < 7; i++ )
	{
		var oTitleCell = oCalTitleRow.insertCell(-1);
		oTitleCell.className = "week_heading";
		oTitleCell.align = "center";
		oTitleCell.innerText = this.weekDays[i];
	}

	// Create calendar rows (weeks and days) 
	for( weekCount = 0; weekCount < 7; weekCount++ )
	{
		var oCalBodyRow = oBodyTable.insertRow(-1);
		for( weekDaysCount = 0; weekDaysCount < 7; weekDaysCount++ )
		{
			if( gridCount == weekday ) monthCount = 1;
			if( monthCount > curMonthDays ) monthCount = "";
			if( monthCount == "" )
			{
				var oDayCell = oCalBodyRow.insertCell(-1);
				oDayCell.className = "daycell_disabled";
				if( this.cellHeight != null )
					oDayCell.style.height = (this.cellHeight+"px");
				oDayCell.style.width = (cellWidth+"px");
			}
			else
			{
				var oDayCell = oCalBodyRow.insertCell(-1);
				oDayCell.id = "monthday";
				oDayCell.name = "monthday";
				oDayCell.vAlign = "top";
				oDayCell.className = "daycell";
				if( this.cellHeight != null )
					oDayCell.style.height = (this.cellHeight+"px");
				oDayCell.style.width = (cellWidth+"px");
				this.dayCells.push(oDayCell);
				var oDayInnerTable = oDayCell.appendChild(document.createElement("TABLE"));
				oDayInnerTable.width = "100%";
				oDayInnerTable.cellPadding = 0;
				oDayInnerTable.cellSpacing = 0;
				var oDayInnerRow = oDayInnerTable.insertRow(-1);
				var oDayInnerCell = oDayInnerRow.insertCell(-1);
				var oDayInnerSpan = oDayInnerCell.appendChild(document.createElement("SPAN"));
				oDayInnerSpan.id = "day_num";
				oDayInnerSpan.name = "day_num";
				oDayInnerSpan.className = "data_entry";
				oDayInnerSpan.innerHTML = monthCount;
				oDayInnerSpan.onmousemove = function(){this.className='data_entry_sel';};
				oDayInnerSpan.onmouseout = function(){this.className='data_entry';};
				
				oDayInnerSpan.parentCal = this;
				oDayInnerSpan.parentCell = oDayCell;
				oDayInnerSpan.onclick = function(){this.parentCal.onSelectDay(this, monthCount);};

				var oDayInnerCell2 = oDayInnerRow.insertCell(-1);
				var oDayInnerSpan2 = oDayInnerCell2.appendChild(document.createElement("SPAN"));
				oDayInnerSpan2.align = "center";
				oDayInnerSpan2.id = "monthdaymsg";
				oDayInnerSpan2.name = "monthdaymsg";
				oDayInnerSpan2.className = "data_entry_msg";
				var oDayInnerRow2 = oDayInnerTable.insertRow(-1);
				var oDayInnerBottomCell = oDayInnerRow2.insertCell(-1);
				oDayInnerBottomCell.colSpan = 2;
				var oDayInnerDiv = oDayInnerBottomCell.appendChild(document.createElement("DIV"));
				oDayInnerDiv.id = "monthdaytext";
				oDayInnerDiv.name = "monthdaytext";
				oDayInnerDiv.className = "cal_events";
				if( this.cellHeight != null )
					oDayInnerDiv.style.height = ((this.cellHeight-25)+"px");
				oDayInnerDiv.style.width = (cellWidth+"px");
			}
			gridCount++;
			if( monthCount != "" ) monthCount++;
		}
		if( monthCount == "" || monthCount > curMonthDays ) break;
	}

	// Select current day - if showing
	if( this.showingYear == now.getFullYear() &&
		this.showingMonth == (now.getMonth()+1) )
	{
		//this.dayCells[now.getDate()-1].className = "daycell_today";
		//this.dayCells[now.getDate()-1].style.backgroundColor = "red";
	}
	if( this.showingDay > 0 )
	{
		if( this.showingDay > this.monthDays[this.showingMonth-1] ) this.showingDay = this.monthDays[this.showingMonth-1];
		this.dayCells[this.showingDay-1].className = "daycell_sel";
		this.last_obj = this.dayCells[this.showingDay-1];
	}
	
	if( this.m_navigateMonth != null ) this.m_navigateMonth( this.showingMonth, this.showingYear );
}

RenderMonth.prototype.setDayContentHTML = function( dayNum, htmlContent )
{
	if( this.dayCells == null ) return;
	var day_cell = this.dayCells[dayNum-1];
	var day_txt = day_cell.getElementsByTagName("DIV")[0];
	day_txt.innerHTML = htmlContent;
}

/**
 *	method:		onSelectDay
 *	purpose:	Day Selection Event
 */
RenderMonth.prototype.onSelectDay = function(src, monthIdx)
{
	var day_cell = src.parentCell;
	if( this.last_obj != null ) this.last_obj.className = "daycell";
	day_cell.className = "daycell_sel";
	this.last_obj = day_cell;
	this.showingDay = monthIdx;
	if( this.m_selectDay != null ) this.m_selectDay();
}

/**
 *	method:		advanceMonth
 *	purpose:	Month advance event
 */
RenderMonth.prototype.advanceMonth = function()
{
	var nextMonth, nextYear;
	if( this.showingMonth < 12 )
	{
		nextMonth = this.showingMonth + 1;
		nextYear = this.showingYear;
	}
	else
	{
		nextMonth = 1;
		nextYear = this.showingYear + 1;
	}
	this.drawMonth( this.cellHeight, this.CalendarWidth, nextMonth, nextYear );
	if( this.m_navigateMonth != null ) this.m_navigateMonth( nextMonth, nextYear );
}

/**
 *	method:		retractMonth
 *	purpose:	Month retract event
 */
RenderMonth.prototype.retractMonth = function(e)
{
	var lastMonth, lastYear;
	if( this.showingMonth > 1 )
	{
		lastMonth = this.showingMonth - 1;
		lastYear = this.showingYear;
	}
	else
	{
		lastMonth = 12;
		lastYear = this.showingYear - 1;
	}
	this.drawMonth( this.cellHeight, this.CalendarWidth, lastMonth, lastYear );
	if( this.m_navigateMonth != null ) this.m_navigateMonth( lastMonth, lastYear );
}
