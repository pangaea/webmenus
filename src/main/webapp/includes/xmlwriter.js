var APOS = "'"; QUOTE = '"'; AMP = '&';
var ESCAPED_QUOTE = {  }
ESCAPED_QUOTE[QUOTE] = '&quot;'
ESCAPED_QUOTE[APOS] = '&apos;'
ESCAPED_QUOTE[AMP] = '&amp;'
   
/*
   Format a dictionary of attributes into a string suitable
   for inserting into the start tag of an element.  Be smart
   about escaping embedded quotes in the attribute values.
*/
function formatAttributes(attributes) {
    var att_value
    var apos_pos, quot_pos
    var use_quote, escape, quote_to_escape
    var att_str
    var re
    var result = ''
   
    for (var att in attributes) {
        att_value = attributes[att]
        
        // Find first quote marks if any
        apos_pos = att_value.indexOf(APOS)
        quot_pos = att_value.indexOf(QUOTE)
       
        // Determine which quote type to use around 
        // the attribute value
        if (apos_pos == -1 && quot_pos == -1) {
            att_str = ' ' + att + "='" + att_value +  "'"
            result += att_str
            continue
        }
        
        // Prefer the single quote unless forced to use double
        if (quot_pos != -1 && quot_pos < apos_pos) {
            use_quote = APOS
        }
        else {
            use_quote = QUOTE
        }
   
        // Figure out which kind of quote to escape
        // Use nice dictionary instead of yucky if-else nests
        escape = ESCAPED_QUOTE[use_quote]
        
        // Escape only the right kind of quote
        re = new RegExp(use_quote,'g')
        att_str = ' ' + att + '=' + use_quote + 
            escape(att_value.replace(re, escape)) + use_quote
        result += att_str
    }
    return result
}
// XML writer with attributes and smart attribute quote escaping 
function writeElement(name,content,attributes){
    var att_str = ''
    if (attributes) { // tests false if this arg is missing!
        att_str = formatAttributes(attributes)
    }
    var xml
    if (!content){
        xml='<' + name + att_str + '/>'
    }
    else {
		content = content.replace(/</g, '&lt;')
        xml='<' + name + att_str + '>' + content + '</'+name+'>'
    }
    return xml
}
function writeElementNL(name, content) {
    return writeElement(name,content) + '\n'
}






function XmlWriter()
{
	var insideTag = false;
	var elemStack = new Array();
	var saveBuf = new Array(1000);
	this.xml = "";
	this.writeStartDocument = function()
	{
		saveBuf.push( '<?xml version="1.0" encoding="UTF-8"?>' );
	}
	this.writeTextNode = function(name, value)
	{
		this.writeStartElement(name);
		this.writeCharacters(value);
		this.writeEndElement();
	}
	this.writeStartElement = function(name)
	{
		if( insideTag ) saveBuf.push('>');
		elemStack.push(name);
		saveBuf.push('<' + name + ' ');
		insideTag = true;
	}
	this.entityify = function (instr)
	{
	    return String(instr).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
	};
	this.writeAttributeString = function(name, value)
	{
	    var att_value = value;
	    var apos_pos, quot_pos;
	    var use_quote, escape, quote_to_escape;
	    var att_str;
	    var re;
	    var result = '';

        // Find first quote marks if any
        apos_pos = String(att_value).indexOf(APOS);
        quot_pos = String(att_value).indexOf(QUOTE);

        // Determine which quote type to use around 
        // the attribute value
        if (apos_pos == -1 && quot_pos == -1) {
            att_str = '"' + this.entityify(att_value) +  '"';
            result = att_str;
        }
        else
        {
	        // Prefer the single quote unless forced to use double
	        if (quot_pos != -1 && quot_pos < apos_pos) {
	            use_quote = APOS;
	        }
	        else {
	            use_quote = QUOTE;
	        }
	        escape = ESCAPED_QUOTE[use_quote];
	        
	        // Escape only the right kind of quote
	        re = new RegExp(use_quote,'g');
	        att_str = use_quote + this.entityify(att_value.replace(re, escape)) + use_quote;
	        result = att_str;//.replace(/&/g, ESCAPED_QUOTE[AMP]);
        }

	    
		saveBuf.push(name + '=' + result + ' ');
		insideTag = true;
	}
	this.writeCharacters = function(content)
	{
		if( insideTag ) saveBuf.push('>');
		//saveBuf.push(content.replace(/</g, '&lt;'));
		saveBuf.push("<![CDATA[");
		saveBuf.push(content);
		saveBuf.push("]]>");
		insideTag = false;
	}
	this.writeEndElement = function()
	{
		if( insideTag ) saveBuf.push('>');
		var name = elemStack.pop();
		saveBuf.push('</' + name + '>');
		insideTag = false;
	}
	this.writeEndDocument = function()
	{
		this.xml = saveBuf.join("");
	}
}