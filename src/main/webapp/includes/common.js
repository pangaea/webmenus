var alphas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
var digits = "0123456789";
function isAlpha( c )
{
	return( alphas.indexOf( c ) != -1 );
}
function isDigit( c )
{
	return( digits.indexOf( c ) != -1 );
}
function isAlphaNum( c )
{
	return( isAlpha( c ) || isDigit( c ) );
}
function trimStr( str )
{
	var tstr = str.toString();
	while( isAlphaNum( tstr.charAt(0) ) == false )
		tstr = tstr.substring(1);
	while( isAlphaNum( tstr.charAt( tstr.length - 1 ) ) == false )
		tstr = tstr.substring( 0, tstr.length - 1 );
	return tstr;
}

function getCurrentDate()
{
	// Determine current date
	var today = new Date();
	var mon = (String)(today.getMonth()+1);
	if( mon.length == 1 ) mon = "0" + mon;
	var day = (String)(today.getDate());
	if( day.length == 1 ) day = "0" + day;
	return mon + "/" + day + "/" + today.getFullYear();//false; //any other values, bad date
}

function getCurrentTime()
{
	// Determine current time
	var today = new Date();
	var hour = today.getHours();
	var min = today.getMinutes();
	var ampm = (hour<12)?"AM":"PM";
	if( hour == 0 ){ hour = "12"; }
	else if( hour > 0 && hour < 12 ){ hour = (String)(hour); }
	else if( hour == 12 ){ hour = (String)(hour); }
	else{ hour = (String)(hour-12); }
	if( hour.length == 1 ) hour = "0" + hour;
	var min = (String)(min);//(Math.floor(min/5) * 5);
	if( min.length == 1 ) min = "0" + min;
	return hour + ":" + min + " " + ampm;
}

// Global Prototypes /////////
String.prototype.trim = function (){return this.replace(/^\s*/, "").replace(/\s*$/, "");}

// GLOBAL IDs
///////////////////////////////////////////
var B_NEW = 5;
var B_EDIT = 6;
var B_DELETE = 7;
var B_FRONT = 8;
var B_FORWARD = 9;
var B_BACK = 10;
var B_END = 11;
var B_SUBMIT = 12;
var B_CANCEL = 13;
var B_SEARCH = 14;
var B_REFRESH = 15;
var B_SELECT = 16;
var B_ADD = 17;
var B_REMOVE = 18;
var B_SUBTRACT = 19;
var B_EXPAND = 20;
var B_COPY = 21;
var B_CLOSE = 22;
var B_SELECTIMAGE = 23;
var B_UPLOADIMAGE = 24;
//////////////////////////////////////////


function buttonEnable( buttonObj, enable )
{
	if( buttonObj != null )
	{
		if( enable == true )
		{
			buttonObj.disabled = false;
		}
		else
		{
			buttonObj.disabled = true;
		}
	}
}

function buttonDraw( id, param, text, callback, name )
{
	// Open button
	var buttonHTML = "";
	buttonHTML += "<button id=\"" + name + "\" class=\"buttonx ui-button ui-state-default ui-button-text-only\" style='' onclick=\"if(this.disabled==false)"+callback+"( " + id + ", '" + param + "' );\">";
	buttonHTML += text;
	
	// Close button
	buttonHTML += "</button>";
	
	return buttonHTML;
}

function buttonThemeDraw( id, param, text, callback, name )
{
	return buttonDraw( id, param, text, callback, name );
}

function reSizeImage(img)
{
	var imageSrc = jQuery(img).attr("src");
	if( imageSrc == null ) return;
	
	if( imageSrc.indexOf(".gif") >= 0 ||
		imageSrc.indexOf(".jpeg") >= 0 ||
		imageSrc.indexOf(".jpg") >= 0 ||
		imageSrc.indexOf(".png") >= 0 )
	{
		// Reset to allow for on-the-fly updates
		jQuery(img).css("width", "");
		jQuery(img).css("height", "");
		try {
	        var maxWidth = jQuery(img).attr("maxwidth"); // Max width for the image
	        var maxHeight = jQuery(img).attr("maxheight");    // Max height for the image
	        var ratio = 0;  // Used for aspect ratio
	        var width = jQuery(img).width();    // Current image width
	        if (width == 0) width = img.width;
	        var height = jQuery(img).height();  // Current image height
	        if (height == 0) height = img.height;
	
	        // Check if the current width is larger than the max
	        if (width > maxWidth) {
	            ratio = maxWidth / width;   // get ratio for scaling image
	            jQuery(img).css("width", maxWidth); // Set new width
	            jQuery(img).css("height", (height * ratio));  // Scale height based on ratio
	            height = height * ratio;    // Reset height to match scaled image
	            width = width * ratio;    // Reset width to match scaled image
	        }
	
	        // Check if current height is larger than max
	        if (height > maxHeight) {
	            ratio = maxHeight / height; // get ratio for scaling image
	            jQuery(img).css("height", maxHeight);   // Set new height
	            jQuery(img).css("width", (width * ratio));    // Scale width based on ratio
	            width = width * ratio;    // Reset width to match scaled image
	        }
	    }
	    catch (e) {
	    	alert(e);
	    }
	    jQuery(img).css("visibility", "visible"); // Set visible
	}
	else
	{
		jQuery(img).css("width", "0px");
		jQuery(img).css("height", "0px");
	}
}

function reSizeImages()
{
	jQuery("img[class='resizeme_image']").each(function() {
		reSizeImage(this);
	});
}