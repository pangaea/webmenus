function attachToFields()
{
//	var inputElems = document.getElementsByTagName("INPUT");
//	for( var i = 0; i < inputElems.length; i++ )
//	{
//		var inputElem = inputElems[i];
//		var type = inputElem.getAttribute("datatype");
//		if( type == null ) continue;
//		//if( type == "int" || type == "real" || type == "date" || type == "time" || type == "datetime" )
//		if( false )
//		{
//			inputElem.onkeypress = validateinput;
//			inputElem.onblur = formatinput;
//			//inputElem.onpropertychange = formatinput;
//		}
//	}
	//$("input[datatype='int']").numeric();
	//$("input[datatype='real']").numeric({ decimal : "." });
	$("input[datatype='int'], .datatype_int").autoNumeric({
		aSep: '',
		mDec: 0
	});
	$("input[datatype='real'], .datatype_real").each(function(){
		$(this).autoNumeric({
			aSep: '',
			mDec: $(this).attr("precision")
		});
	});
	
	$("input[datatype='date'], .datatype_date").datepicker();
	$("input[datatype='time'], .datatype_time").timepicker({
		ampm: true,
		showOptions: {direction: 'down'} 
	});
	$("input[datatype='datetime'], .datatype_datetime").datetimepicker({
		ampm: true
	});
}

function Asc(chr)
{
	return chr.charCodeAt(0);
}

function countChars( obj, keyCode, ch )
{
	var count = 0;
	if( keyCode == ch )
		for( i = 0; i < obj.value.length; i++ )
			if( obj.value.charCodeAt( i ) == ch ) count++;
	return count;
}

function validateUSDate( newDate )
{
	var objRegExp = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
	var strValue = newDate;

	//check to see if in correct format
	if(objRegExp.test(strValue))
	{
		//var strSeparator = strValue.substring(2,3) 
		var arrayDate = strValue.split('/'); 
		//create a lookup for months not equal to Feb.
		var arrayLookup = { '01' : 31,'03' : 31, 
							'04' : 30,'05' : 31,
							'06' : 30,'07' : 31,
							'08' : 31,'09' : 30,
							'10' : 31,'11' : 30,'12' : 31}

		// Correct for single digit month / day
		if( arrayDate[0].length == 1 ) arrayDate[0] = "0" + arrayDate[0];
		if( arrayDate[1].length == 1 ) arrayDate[1] = "0" + arrayDate[1];

		// Get int value for day
		var intDay = parseInt(arrayDate[1],10); 

		//check if month value and day value agree
		if(arrayLookup[arrayDate[0]] != null)
		{
			if(intDay <= arrayLookup[arrayDate[0]] && intDay != 0)
			return (String)(arrayDate[0]) + "/" + (String)(arrayDate[1]) + "/" + (String)(arrayDate[2]);
			//return true; //found in lookup table, good date
		}
    
		//check for February (bugfix 20050322)
		//bugfix  for parseInt kevin
		//bugfix  biss year  O.Jp Voutat
		var intMonth = parseInt(arrayDate[0],10);
		if (intMonth == 2)
		{ 
			var intYear = parseInt(arrayDate[2]);
			if (intDay > 0 && intDay < 29)
			{
				return (String)(arrayDate[0]) + "/" + (String)(arrayDate[1]) + "/" + (String)(arrayDate[2]);
				//return true;
			}
			else if (intDay == 29)
			{
				if ((intYear % 4 == 0) && (intYear % 100 != 0) || (intYear % 400 == 0))
				{
					// year div by 4 and ((not div by 100) or div by 400) ->ok
					return (String)(arrayDate[0]) + "/" + (String)(arrayDate[1]) + "/" + (String)(arrayDate[2]);
					//return true;
				}   
			}
		}
	}
	
	// Determine current date
	return getCurrentDate();
}

function validateUSTime( newTime )
{
	var timeval = newTime;
	//var exp = /^([0-1]?[1-9]):([0-5][0-9])?(\s+[aApP]\.?[mM]\.?)?$/;
	var exp = /^([0][1-9]|[1][0-2]|[1-9]):([0-5][0-9])?(\s+[aApP]\.?[mM]\.?)?$/;
	if( timeval.match(exp) )
	{
		var hour = RegExp.$1;
		if( hour.length == 1 ) hour = "0" + hour;
		var min = RegExp.$2;
		min = (String)(min);//(Math.floor(min/5) * 5);
		if( min.length == 1 ) min = "0" + min;
		return hour + ":" + min + " " + (RegExp.$3).toUpperCase().trim();
	}
	else
	{
		// Determine current time
		return getCurrentTime();
	}
}

//function validateinput( obj, type, decpnt )
function validateinput(e)
{
	//if( window.event != null ) e = window.event;
	var dataType, keyCode, obj;
	if(window.event)
	{
		obj = window.event.srcElement;
		keyCode = window.event.keyCode;
	}
	else
	{
		obj = e.target;
		keyCode = e.which;
	}

	//alert( keyCode);
	if( keyCode == 8 || keyCode == 0 ) return;

	switch(obj.getAttribute("datatype"))
	{
	case 'int':
		if( ( keyCode < Asc('0') || keyCode > Asc('9') ) )
			if(window.event) window.event.returnValue = false; else e.preventDefault();
		break;
	case 'real':
		if( ( keyCode >= Asc('0') && keyCode <= Asc('9') ) || keyCode == Asc('.') )
		{
			if( countChars( obj, keyCode, Asc('.') ) > 0 )
				if(window.event) window.event.returnValue = false; else e.preventDefault();
		}
		else
			if(window.event) window.event.returnValue = false; else e.preventDefault();
		break;
	case 'date':
		if( ( keyCode >= Asc('0') && keyCode <= Asc('9') ) || keyCode == Asc('/') )
		{
			if( countChars( obj, keyCode, Asc('/') ) > 1 )
				if(window.event) window.event.returnValue = false; else e.preventDefault();
		}
		else
			if(window.event) window.event.returnValue = false; else e.preventDefault();
		break;

	case 'time':
		if( ( keyCode >= Asc('0') && keyCode <= Asc('9') ) || keyCode == Asc(':') || keyCode == Asc(' ') ||
				keyCode == Asc('a') || keyCode == Asc('A') ||
				keyCode == Asc('p') || keyCode == Asc('P') ||
				keyCode == Asc('m') || keyCode == Asc('M') )
		{
			if( countChars( obj, keyCode, Asc(':') ) > 0 )
				if(window.event) window.event.returnValue = false; else e.preventDefault();
		}
		else
			if(window.event) window.event.returnValue = false; else e.preventDefault();
		break;
	}
}

function makeChange(obj,val)
{
	if( obj.value != val )
	{
		obj.value = val;
	}
}

//function formatinput( obj, type, precision )
function formatinput(e)
{
	var dataType, keyCode, obj;
	if(window.event)
	{
		obj = window.event.srcElement;
	}
	else
	{
		obj = e.target;
	}

	switch(obj.getAttribute("datatype"))
	{
	case 'int':
		if( obj.value.length > 0 )
		{
			// Format the string into 0.00
			for( i = 0; i < obj.value.length; i++ )
			{
				var chr = obj.value.charAt( i );
				if( chr == '.' )
				{
					//obj.value = obj.value.substr(0, i);
					makeChange(obj, obj.value.substr(0, i));
					break
				}
				if( chr < '0' || chr > '9' )
				{
					//obj.value = "0";
					makeChange(obj, "0");
					break;
				}
			}
		}
		else
		{
			//obj.value = "0";
			makeChange(obj, "0");
		}
		break;	
	case 'real':
		var precision = parseInt(obj.getAttribute("precision"));
		if( precision == 0 ) precision = 2;
		if( obj.value.length > 0 )
		{
			// Format the string into 0.00
			var prec = precision;
			var newVal = "";
			var fd = 0;
			for( i = 0; i < obj.value.length; i++ )
			{
				var chr = obj.value.charAt( i );
				if( ( chr < '0' || chr > '9' ) && chr != '.' )
				{
					//obj.value = "0.";
					//for(ii = 0; ii < precision; ii++) obj.value += "0";
					var _value = "0.";
					for(ii = 0; ii < precision; ii++) _value += "0";
					makeChange(obj, _value);
					break;
				}
				if( prec == 0 ) break;
				if( fd == 1 ) prec--;
				if( chr == '.' ) fd = 1;
				newVal += obj.value.charAt( i );
			}
			if( fd == 0 ) newVal += '.';
			for( i = 0; i < prec; i++ ) newVal += '0';
			//obj.value = newVal;
			makeChange(obj, newVal);
		}
		else
		{
			//obj.value = "0.";
			//for(ii = 0; ii < precision; ii++) obj.value += "0";
			var _value = "0.";
			for(ii = 0; ii < precision; ii++) _value += "0";
			makeChange(obj, _value);
		}
		break;
	case 'date':
		//obj.value = validateUSDate(obj.value);
		makeChange(obj, validateUSDate(obj.value));
		break;
	case "time":
		//obj.value = validateUSTime(obj.value);
		makeChange(obj, validateUSTime(obj.value));
		break;
	case 'datetime':
		var curTimestamp = obj.value;
		var timestamp = curTimestamp.split(' ');
		var newDate = timestamp[0];
		var newTime = timestamp[1] + " " + timestamp[2];
		//obj.value = validateUSDate(newDate) + " " + validateUSTime(newTime);
		makeChange(obj, validateUSDate(newDate) + " " + validateUSTime(newTime));
		break;
	}
}
