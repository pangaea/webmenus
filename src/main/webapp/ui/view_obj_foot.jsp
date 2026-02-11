<!--a id="welcome_page" href="<%//=viewConfigBean.getWelcomePage()%>" style="display:none;">Welcome Page</a-->
<a id="feedback_page" href="feedback/feedback.jsp" style="display:none;">Feedback Page</a>
<a id="image_library" href="image_library.jsp" style="display:none;">Image Library</a>
<a id="schedule_designer" href="../app/scheduler/schedule_designer.jsp" style="display:none;">Schedule Designer</a>

<script type="text/javascript">
//////////////////////////////////
///////////////////////////////////
////////////////////////////////////

	myLayout = $('body').layout({

	//	enable showOverflow on west-pane so popups will overlap north pane
		west__showOverflowOnHover: false

	//	reference only - these options are NOT required because are already the 'default'
	,	closable:				true	// pane can open & close
	,	resizable:				true	// when open, pane can be resized 
	,	slidable:				true	// when closed, pane can 'slide' open over other panes - closes on mouse-out

	//	some resizing/toggling settings
	,	north__resizable:		false
	,	north__closable:		false
	,	north__size:			43
	,	north__slidable:		false	// OVERRIDE the pane-default of 'slidable=true'
	,	north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
	,	north__spacing_closed:	20		// big resizer-bar when open (zero height)
	,	south__resizable:		false	// OVERRIDE the pane-default of 'resizable=true'
	,	south__spacing_open:	0		// no resizer-bar when open (zero height)
	,	south__spacing_closed:	20		// big resizer-bar when open (zero height)
	//	some pane-size settings
	,	west__resizable:		false
	,	west__closable:			false
	,	west__slidable:			false
	,	west__minSize:			240
	,	west__size:				240
	/*,	east__size:				300*/
	,	east__minSize:			200
	,	east__maxSize:			Math.floor(screen.availWidth / 2) // 1/2 screen width
	});
	

	function initMenu()
	{
	    menuOffsetWidth = 2;
	    //menuInit();
	}
	
	document.loadWelcomePage = function()
	{
		//$("#welcome_page").click();
	}
	
	document.loadFeedbackPage = function()
	{
		$("#feedback_page").click();
	}
	
	document.loadImageLibrary = function()
	{
		$("#image_library").click();
	}
	
	document.loadScheduleDesigner = function()
	{
		$("#schedule_designer").click();
	}
	
	$("#image_library, #schedule_designer").fancybox({
	    'width' : '98%',
	    'height' : '98%',
	    'autoScale' : false,
	    'transitionIn' : 'none',
	    'transitionOut' : 'none',
	    'type' : 'iframe'
	});
	
	$("#feedback_page").fancybox({
	    'width' : 650,
	    'height' : 400,
	    'autoScale' : false,
	    'transitionIn' : 'none',
	    'transitionOut' : 'none',
	    'type' : 'iframe'
	});

	$(function(){
		$("body").css("visibility", "visible");
		<%
		//boolean show_welcome = (Boolean)request.getSession().getAttribute("show_welcome");
		//String welcomPage = viewConfigBean.getWelcomePage();
		//if(show_welcome && welcomPage.length() > 0){
		%>
		//	setTimeout(function(){
		//		document.loadWelcomePage();
		//	}, 1000);
		<%// }
		//request.getSession().setAttribute("show_welcome", false);
		%>
	});
	
	document.startAnimation = function __startAnimation(){ startAnimation(); }
	document.stopAnimation = function __stopAnimation(){ stopAnimation(); }

</script>