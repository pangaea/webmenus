<div>


<h2>Edit Menu Item</h2>
<h3>Change menu item settings</h3>
<hr/>
<div id="toolbar_panel"></div>

<script type="text/javascript">
$(function () {

	$("#sortable").sortable();
	$("#sortable").disableSelection();
/*
	$("#goButton").click(function(){
		var result = $('#sortable').sortable('toArray');
		var delim_list = "";
		for(var i = 0; i < result.length; i++)
		{
			delim_list += result[i] + "-";
		}
		alert(result);
	});
*/

	fillFields('<%=request.getParameter("id")%>');
});

function buttonCallback( item, param )
{
	switch( item )
	{
		case 0:
			commitItemChanges('<%=request.getParameter("id")%>', true);
			break;
		case 1:
			createOption('<%=request.getParameter("id")%>');
			break;
	}
}

function deleteItem(portionId)
{
	messageBox("Do you want to delete the selected portion size?", "Confirm Delete",
	{
		"No": function(){$(this).dialog("close");},
		"Yes": function(){$('#'+portionId).remove(); $(this).dialog("close");}
	});
	//$('#'+portionId).remove();
}

function addPortion(size, price)
{
	$('#sortable').append(renderListItem(size, price, gIndex));
	gIndex++;
	//attachToFields();
	$("input[datatype='real']").numeric({ 
		decimal : ".",
		negative : false
	});
}

var btn_html = "";
//btn_html += buttonDraw( 0, '', 'Apply Changes', 'buttonCallback', 'Button_Commit' );
//btn_html += "&nbsp;";
btn_html += buttonDraw( 1, '', 'Create New Option', 'buttonCallback', 'Button_NewOption' );
$("#toolbar_panel").html(btn_html);
</script>
<hr/>
	<table>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Name:
		</td><td width="100%">
			<div id="DIV_name">
	
			<input WMrequired="false" title="Name" type="text" ID="name" NAME="name" maxlength="" style="width:400px;"/>
	
			</div>
		</td>
	</tr>
	
	<tr><td nowrap valign="top" align="right" class="property_text">
		Description:
		</td><td width="100%">
			<div id="DIV_description">
	
			<textarea style="height:100px;width:400px;" ID="description" NAME="description"></textarea>
	
			</div>
		</td>
	</tr>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Hidden:
		</td><td width="100%">
			<div id="DIV_hidden">
	
			<input type="checkbox" ID="hidden" NAME="hidden"/>
	
			</div>
		</td>
	</tr>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Special Instructions:
		</td><td width="100%">
			<div id="DIV_hidden">
	
			<input type="checkbox" ID="special_instructions" NAME="special_instructions"/>
	
			</div>
		</td>
	</tr>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Image:
		</td><td width="100%">
			<div id="DIV_image_designer">
	
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<img id="image_img" src=""/>
						<input WMrequired="false" title="Image" type="hidden" ID="image" NAME="image" maxlength=""/>
					</td>
					<td valign="top">
						<img id="button_image" title="Select" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="parent.showDialog('<%=request.getContextPath()%>/ui/image_library.jsp?parentProp=image', 'Select Image');" src="<%=request.getContextPath()%>/images/browse.gif"></img>
						<img id="clear_image" title="Clear" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="$('#image_img').attr('src','');" src="<%=request.getContextPath()%>/images/cross-circle.png"></img>
					</td>
				</tr>
			</table>
	
			</div>
		</td>
	</tr>
	<tr>
		<td nowrap valign="top" align="right" class="property_text" width="60px">&nbsp;
			Portions:
		</td>
		<td colspan="3">
			<ul id="sortable" class="sort_list"></ul>
			<button onclick="addPortion('','');">Add Portion</button>
		</td>
	</tr>

	</table>

</div>