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

function DettachEvent( obj, evType, callback )
{
	if( obj.addEventListener )
	{
		obj.removeEventListener( evType, callback );
		return true;
	}
	else if( obj.attachEvent )
	{
		return obj.dettachEvent( "on" + evType, callback );
	}
	return false;
}

function getClientHeight()
{
	if(window.innerHeight!=window.undefined) return window.innerHeight;
	if(document.compatMode=='CSS1Compat') return document.documentElement.clientHeight;
	if(document.body) return document.body.clientHeight; 
	return window.undefined; 
}

function getClientWidth()
{
	if(window.innerWidth!=window.undefined) return window.innerWidth; 
	if(document.compatMode=='CSS1Compat') return document.documentElement.clientWidth; 
	if(document.body) return document.body.clientWidth; 
	return window.undefined; 
}

function SetGlobalCursor(cursor)
{
	for( var i = 0; i < document.all.length; i++ )
	{
		var obj = document.all[i];
		if( obj.style )
			obj.style.cursor = cursor;
	}
}

function posLeft( elmt )
{
	var l = 0;
	while( elmt.offsetParent != null )
	{
		l += elmt.offsetLeft;
		elmt = elmt.offsetParent;
	}
	return l;
}

function posTop( elmt )
{
	var t = 0;
	while( elmt.offsetParent != null )
	{
		t += elmt.offsetTop;
		elmt = elmt.offsetParent;
	}
	return t;
}