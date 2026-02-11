
<div data-role="page" id="page-login">

	<div data-role="header">
		<h1>Existing Account</h1>
		<a data-rel="button" rel="external" data-ajax="false" href="<%=rootMenuPath%>">Home</a>
	</div>

	<div data-role="content">
		<span style="color:red;margin-bottom:4px;" class="errMsg"><%=errMsg%></span>

		<form id="patronLogin" method="post" action="<%=rootMenuPath%>/login" rel="external" data-ajax="false">
			<input type="hidden" name="type" value="login"/>
		    <label for="name">Email: <span style="color:red">*</span></label>
		    <input WMrequired="true" title="Email" type="text" name="email" id="email" />
		    <span style="color:red;font:8pt verdana;">(* Required)</span>
		    <fieldset class="ui-grid-a"> 
		    	<div class="ui-block-a"><button onclick="return validateParams('patronLogin');">Login</button></div>
				<div class="ui-block-b"><a href="#page-create" data-role="button">Register</a></div>
			</fieldset>
		</form>
	</div>

</div>