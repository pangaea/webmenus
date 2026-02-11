<%
	String theBanner = viewConfigBean.getBanner();
	if( theBanner.length() > 0 ){
%>
<table width="100%"><tr><td valign="middle" align="center">
	<img src="<%=theBanner%>"></td><td valign="middle">
</td></tr></table>
<%
	}
%>
<!--link rel="stylesheet" type="text/css" href="<%//=request.getContextPath()%>/xlibs/jquery/css/lavalamp-styles.css" />
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/lavalamp.js"></script-->

<!--link rel="stylesheet" type="text/css" href="<%//=request.getContextPath()%>/xlibs/jquery/css/superfish.css" /--> 
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/hoverIntent.js"></script> 
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/superfish.js"></script-->

<!--link rel="stylesheet" type="text/css" href="<%//=request.getContextPath()%>/xlibs/jquery/css/example-noscript.css" /-->

<!--link rel="stylesheet" type="text/css" href="<%//=request.getContextPath()%>/xlibs/jquery/menueffect/style.css" /-->
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/menueffect/script.js"></script-->

<div class="menu ui-widget-header">
	<!--ul id="menu">
		<li class="active"><a style="white-space:nowrap;" href="<%//=request.getContextPath()%>/ViewCmd?call=logout">Logout</a></li>
		<li ><a id="imageLibrary" style="white-space:nowrap;" href="#" onclick="loadImageLibrary();">Image Library</a></li>
		<li ><a style="white-space:nowrap;" href="#" onclick="showLocalTour();">Take a Tour</a></li>
		<li ><a style="white-space:nowrap;" href="#" onclick="loadFeedbackPage();">Support &amp; Feedback</a></li>
	</ul>
	<div id="slide"></div-->
	
	<a id="imageLibrary" style="white-space:nowrap;" href="#" onclick="loadImageLibrary();">Image Library</a>
	<a style="white-space:nowrap;" href="#" onclick="showLocalTour();">Take a Tour</a>
	<a style="white-space:nowrap;" href="#" onclick="loadFeedbackPage();">Support &amp; Feedback</a>
	<span style="position:absolute;right:120px;color:#aaaaaa;">Hello <%=info.m_UserName%>, welcome to chowMagic.</span>
	<a style="white-space:nowrap;" href="<%=request.getContextPath()%>/ViewCmd?call=logout">Logout</a>
</div>

<script type="text/javascript">
$(function(){ 
    //$("ul.sf-menu").superfish();
    //menuSlider.init('menu','slide')
});
function showLocalTour(){
	$.fn.joyride('destroy');
	$("#help_slideshow").joyride({'autoStart' : true, 'scrollSpeed' : 0, 'localStorage' : false});
}
</script>