
<!--jsp:useBean id="clientSessionBean" scope="session" class="com.genesys.session.ClientSessionBean"/-->
<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<%@ page import="com.genesys.repository.*"%>
<div>

<h2>Edit Menu</h2>
<h3>Change menu settings</h3>
<hr/>
<div id="toolbar_panel"></div>

<script type="text/javascript">
function buttonCallback( item, param )
{
	switch( item )
	{
		case 0:
			commitMenuChanges('<%=request.getParameter("id")%>', true);
			break;
		case 1:
			createCategory('<%=request.getParameter("id")%>');
			break;
		case 2:
			previewMenu('<%=request.getParameter("id")%>');
			break;
	}
}
var btn_html = "";
//btn_html += buttonDraw( 0, '', 'Apply Changes', 'buttonCallback', 'Button_Commit' );
//btn_html += "&nbsp;";
//btn_html += buttonDraw( 2, '', 'Preview Menu', 'buttonCallback', 'Button_Preview' );
//btn_html += "&nbsp;";
btn_html += buttonDraw( 1, '', 'Create New Category', 'buttonCallback', 'Button_NewCategory' );
$("#toolbar_panel").html(btn_html);

$("#DIV_open select").change(function(){
	if( $(this).val() == 2 ){
		$("#hoop").show();
	}else{
		$("#hoop").hide();
	}
})
</script>

<hr/>
<form id="data">
	<table>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Name:
		</td><td width="100%">
			<div id="DIV_name">
	
			<input title="Name" type="text" ID="name" NAME="name" maxlength="" style="width:380px;"/>
	
			</div>
		</td>
	</tr>
	<tr><td nowrap valign="top" align="right" class="property_text">
		Show with Options:
		</td><td width="100%">
					<div id="DIV_show_options">
			
					<input type="checkbox" ID="show_options" NAME="show_options"/>
			
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
		Accept Orders:
		</td><td width="100%">
		<div id="DIV_open">
	
		<select style="border:1px groove;" ID="take_orders" NAME="take_orders">
				<option value="0">No</option>
				<option value="1">Yes</option>
				<option value="2">Schedule</option>
		</select>
		

	
		</div>
		</td>
	</tr>
	
	</table><br/>
	
	<div id="hoop" style="-moz-border-radius: 15px;border-radius: 15px;display:none; padding: 8px; background:#F3F3F3;">
		Choose a schedule defining the times that this menu is open for orders...
		<%
			Credentials info = (Credentials)request.getSession().getAttribute("info");
		%>

		<select ID="schedule_id" NAME="schedule_id">
		<option value="">none</option>
		<webmenusCfg:EnumSchedules credentials="<%=info.m_sTicket%>">
		<option value="<%=scheduleId%>"><%=scheduleName%></option>
		</webmenusCfg:EnumSchedules>
		</select>

		
	</div>

</form>
</div>
