<div>
<h2>Edit Menu Item Option</h2>
<h3>Change menu item option settings</h3>
<hr/>
<div id="toolbar_panel"></div>

<script type="text/javascript">
$(function () {
	$("input[datatype='real']").numeric({ 
		decimal : ".",
		negative : false
	});
	$("#type").change(function(){
		showhideNotForText();
	});
});

function showhideNotForText(){
	$("tr.not_for_text").toggle( ($("#type").val()!="text") );
}

function buttonCallback( item, param )
{
	switch( item )
	{
		case 0:
			commitOptionChanges('<%=request.getParameter("id")%>', true);
			break;
	}
}
//$("#toolbar_panel").html(buttonDraw( 0, '', 'Apply Changes', 'buttonCallback', 'Button_Commit' ));
</script>
<hr/>
<form id="data">
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
		Type:
		</td><td width="100%">
		<div id="DIV_type">
	
		<select style="border:1px groove;" ID="type" NAME="type">

				<option value="select">Select as many as you want</option>
			
				<option value="select-one">Select only one</option>
			
				<option value="text">Comment</option>
			
		</select>
	
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
	
	<tr class="not_for_text"><td nowrap valign="top" align="right" class="property_text">
		Price($):
		</td><td width="100%">
					<div id="DIV_price">
			
					<table cellpadding="0" cellspacing="0"><tr><td>
					<input WMrequired="false" title="Price" type="text" ID="price" NAME="price" datatype="real" precision="2"/>
					 per selection
					</td></tr></table>
			
					</div>
		</td>
	</tr>

	<tr class="not_for_text"><td nowrap valign="top" align="right" class="property_text">
		Selections:
		</td><td width="100%">
					<div id="DIV_data">
					
					<textarea style="height:200px;width:400px;" ID="option_data" NAME="option_data"></textarea><br/>
					<strong>NOTE: Enter multiple selections one per line</strong>
					
					</div>
		</td>
	</table>

</form>
</div>