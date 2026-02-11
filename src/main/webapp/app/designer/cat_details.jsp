<div>
<h2>Edit Menu Category</h2>
<h3>Change menu category settings</h3>
<hr/>
<div id="toolbar_panel"></div>

<script type="text/javascript">
function buttonCallback( item, param )
{
	switch( item )
	{
		case 0:
			commitCategoryChanges('<%=request.getParameter("id")%>', true);
			break;
		case 1:
			createItem('<%=request.getParameter("id")%>');
			break;
	}
}
var btn_html = "";
//btn_html += buttonDraw( 0, '', 'Apply Changes', 'buttonCallback', 'Button_Commit' );
//btn_html += "&nbsp;";
btn_html += buttonDraw( 1, '', 'Create New Item', 'buttonCallback', 'Button_NewItem' );
$("#toolbar_panel").html(btn_html);
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
		Hidden:
		</td><td width="100%">
					<div id="DIV_hidden">
			
					<input type="checkbox" ID="hidden" NAME="hidden"/>
			
					</div>
		</td>
	</tr>
	</table>

</form>
</div>