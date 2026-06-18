function renderListItem(size, price, gIndex)
{
	var html = new Array(1000);
	html.push('<li class="ui-state-default2" id="Item-' + gIndex + '" style="width:410px;">');
	html.push('<table cellspacing="0" cellpadding="2"><tr>');
	html.push('<td nowrap align="right" class="property_text">');
	html.push('<a title="Delete Portion Size" href="javascript:deleteItem(\'Item-' + gIndex + '\')"><img src="/webmenus/app/images/cross.png"/></a>');
	html.push('</td>');
	html.push('<td nowrap valign="middle" align="right" class="property_text" width="40px">');
	html.push('<div style="white-space:nowrap;">Name:</div>');
	html.push('</td><td>');
	html.push('	<div id="DIV_size_desc">');
	html.push('		<input WMrequired="false" title="Size" type="text" ID="size_desc' + gIndex + '" NAME="size_desc' + gIndex + '" maxlength="" size="" value="' + size + '"/>');
	html.push('	</div>');
	html.push('</td>');
	html.push('<td nowrap valign="middle" align="right" class="property_text" width="40px">');
	html.push('<div style="white-space:nowrap;">Price($):</div>');
	html.push('</td><td>');
	html.push('	<div id="DIV_price">');
	html.push('		<table cellpadding="0" cellspacing="0"><tr><td>');
	//html.push('		<input WMrequired="false" title="Price" type="text" ID="price' + gIndex + '" NAME="price' + gIndex + '" datatype="real" precision="2" value="' + price + '"/>');
	html.push('		<input WMrequired="false" title="Price" type="text" ID="price' + gIndex + '" NAME="price' + gIndex + '" datatype="real" precision="2" value="' + price + '"></td>');
	html.push('		</tr></table>');
	html.push('	</div>');
	html.push('</td>');
	html.push('<td>');
	html.push('<img title="Move Portion" src="/webmenus/app/images/arrow-move.png"/>');
	html.push('</td>');
	html.push('</tr>');
	html.push('</table>');
	html.push('</li>');
	return unescape(html.join(''));
}