
<div data-role="page" id="page-create">

	<div data-role="header">
		<h1>Register</h1>
		<a data-rel="button" rel="external" data-ajax="false" href="<%=rootMenuPath%>">Home</a>
	</div>

	<div data-role="content">
		<span style="color:red;" class="errMsg"><%=errMsg%></span>
		
		<form id="patronCreate" method="post" action="<%=rootMenuPath%>/create_account" rel="external" data-ajax="false">
			<input type="hidden" name="type" value="create"/>
			
			<viewCfg:ViewForm viewName="patrons">
			<% if( inputVisible.equalsIgnoreCase("false") == false ){ %>
				<label for="<%=inputField%>"><%=inputText%>:
				<% if( inputRequired == true ){ %>
				<span style="color:red;">*</span>
				<% } %>
				</label>
				<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=inputField%>" maxlength="<%=inputLen%>"/>
			<% } %>
			</viewCfg:ViewForm>
			
			<span style="color:red;font:8pt verdana;">(* Required)</span>
		    <fieldset class="ui-grid-a"> 
		    	<div class="ui-block-a"><button onclick="return validateParams('patronCreate');">Signup</button></div>
				<div class="ui-block-b"><a href="#page-login" data-role="button">Cancel</a></div>
			</fieldset>
		</form>
	</div>

</div>
