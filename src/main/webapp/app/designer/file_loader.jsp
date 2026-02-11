<!--
Copyright (c) 2004-2011 Kevin Jacovelli
All Rights Reserved
-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>File Loader</title>
	<style>
	</style>
	<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
	<script type="text/javascript">
		function selectImportMethod(){
		    if ($('#load_from_t').is(':checked')) {
		    	$('#load_from_template :input').removeAttr('disabled');
		    } else {
		    	$('#load_from_template :input').attr('disabled', true);
		    }
		    if ($('#load_from_f').is(':checked')) {
		    	$('#load_from_file :input').removeAttr('disabled');
		    } else {
		    	$('#load_from_file :input').attr('disabled', true);
		    }   
		}
	</script>
</head>
<body>
	Import Menus<br/><span style="color:red;font-size:8pt;">(this will overwrite the current contents of the designer, <b>don't worry</b>, the changes aren't permanent until they are published)</span>
	<hr/>
	<input type="radio" id="load_from_t" name="load_from" checked onclick="selectImportMethod()">From Template</input>
	<div id="load_from_template">
		<form method="post" action="<%=request.getContextPath()%>/MenuDesigner/loadfromtemplate">
		<table><tr>
			<td>Menu(s) Template:</td>
			<td>
				<select name="sample_menu">
					<!--option value="">-- Please Select --</option-->
					<option value="sample.xml">Basic Sample Menu</option>
					<option value="american.xml">American Cuisine</option>
					<option value="italian.xml">Pizzeria &amp; Restaurant</option>
					<option value="chinese.xml">Chinese Restaurant</option>
				</select>
			</td>
		</tr><tr>
			<td colspan="2"><input type="submit" value="Import Template"/></td>
		</tr></table>
		</form>
	</div>
	<hr/>
	<input type="radio" id="load_from_f" name="load_from" onclick="selectImportMethod()">From File</input>
	<div id="load_from_file">
		<form method="post" enctype="multipart/form-data" action="<%=request.getContextPath()%>/MenuDesigner/loadfromfile">
		<table><tr>
			<td>Menu(s) File:</td>
			<td><input type="file" name="menuFile"/></td>
		</tr><tr>
			<td colspan="2"><input type="submit" value="Load File"/></td>
		</tr></table>
		</form>
	</div>
	<hr/>
	<button onclick="parent.closeDialog();">Close Window</button>
	<script type="text/javascript">
	selectImportMethod();
	</script>
</body>
</html>