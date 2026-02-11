
function showErrors()
{
	//if ($("#syserr").length == 0) return;

	// Initialize error dialog
	$("#syserr").dialog({
		autoOpen: false,
		width: 500,
		height: 300,
		buttons: {
		"Ok": function() {
				$(this).dialog("close");
			}
		}
	});
}

    
    
function goNav(page)
{
	//document.location.href = "<%=request.getContextPath()%>/app/account/" + page;
}