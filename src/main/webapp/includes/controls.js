
function WidgetFactory(widgetPath)
{
	__this__ = this;
	this.widgetPath = widgetPath;
	this.widgetJSON =
	{
	items:	[
			{name: "numpad", id: "__numpad__", page: "numpad.html", width: 60, height: 80},
			{name: "timepicker", id: "__timepicker__", page: "picktime.html", width: 150, height: 22},
			{name: "calendar", id: "__calendar__", page: "calendar.html", width: 172, height: 150},
			{name: "timestamp", id: "__timestamp__", page: "timestamp.html", width: 172, height: 180}
			]
	};

	this.getWidgetInfo = function(name)
	{
		for(var i in this.widgetJSON.items)
		{
			var item = this.widgetJSON.items[i];
			if( item.name == name )
				return item;
		}
		return null;
	}
}

WidgetFactory.prototype.createAllWidgets = function()
{
	for(var i in this.widgetJSON.items)
	{
		var item = this.widgetJSON.items[i];
		var oWidget = document.createElement("iframe");
		oWidget.id = item.id;
		oWidget.name = item.id;
		oWidget.src = this.widgetPath + item.page;
		oWidget.width = item.width;
		oWidget.height = item.height;
		oWidget.border = 0;
		oWidget.style.position = "absolute";
		oWidget.style.backgroundColor = "white";
		oWidget.style.border = "1px outset black";
		oWidget.style.left = 0;
		oWidget.style.top = 0;
		oWidget.scrolling = "no";
		oWidget.style.visibility = "hidden";
		document.body.insertBefore(oWidget, null);
	}
	//document.attachEvent("onmousedown", this.closeAllWidgets);
	$(document).mousedown(this.closeAllWidgets);
}

WidgetFactory.prototype.closeAllWidgets = function()
{
	for(var i in __this__.widgetJSON.items)
	{
		var item = __this__.widgetJSON.items[i];
		var oWidget = document.getElementById(item.id);
		oWidget.style.visibility = "hidden";
	}
}

WidgetFactory.prototype.fireBlurEvent = function(displayId)
{
	// Fire onblur event to format input
	var fireOnThis = document.getElementById(displayId);
	if( document.createEvent )
	{
		var evObj = document.createEvent('HTMLEvents');
		evObj.initEvent( 'blur', true, false );
		fireOnThis.dispatchEvent(evObj);
	}
	else if( document.createEventObject )
	{
		fireOnThis.fireEvent('onblur');
	}
}

WidgetFactory.prototype.showWidget = function(name, buttonElem, displayId, wholeNum)
{
	var item = this.getWidgetInfo(name);
	var oWidget = document.getElementById(item.id);
	var buttonTop = buttonElem.offsetHeight;
	var buttonLeft = buttonElem.offsetWidth - oWidget.offsetWidth;
	var theElem = buttonElem;
	while( theElem != null )
	{
		buttonTop = buttonTop + theElem.offsetTop;
		buttonLeft = buttonLeft + theElem.offsetLeft;
		theElem = theElem.offsetParent;
	}
	if( oWidget.style.visibility == "hidden" || oWidget.style.left != (buttonLeft+"px") || oWidget.style.top != (buttonTop+"px") )
	{
		this.closeAllWidgets();
		oWidget.contentWindow.outputElem = document.getElementById(displayId);
		oWidget.contentWindow.outputElem.readOnly = true;
		oWidget.style.left = buttonLeft + "px";
		oWidget.style.top = buttonTop + "px";
		oWidget.style.visibility = "visible";
		oWidget.contentWindow.setCurrentValue(document.getElementById(displayId).value);

		//buttonElem.src = "../images/calc_show.gif";
		
		// Special functionality for numpad widget
		if( item.name == "numpad" )
		{
			// Enable/disable decimal button
			var dec = oWidget.contentWindow.document.getElementById("decimal");
			if( dec != null )
			{
				if( wholeNum == true )
					dec.disabled = true;
				else
					dec.disabled = false;
			}
		}
	}
	else
	{
		oWidget.contentWindow.outputElem.readOnly = false;
		oWidget.style.visibility = "hidden";
		//buttonElem.src = "../images/calc.gif";
		
		// Fire onblur event to format input
		this.fireBlurEvent(displayId);
	}
}
