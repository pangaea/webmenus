<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>chowMagic - Management Console</title>

<!-- Core Stylesheet -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/styles/main.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/fields.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/controls.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/clientio.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/xutils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/includes/utils.js"></script>
      
<!-- Tab Styles -->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/styles/tabs.css"></link-->
<!--link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/styles/curvey.css"></link-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/styles/menudesigner.css"></link>

<!-- JQuery : Core -->
<%@ include file="jquery_includes.jsp"%>

<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.layout.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/autoNumeric-1.7.4.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.livequery.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-sliderAccess.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xlibs/jquery/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<!--script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/fancybox/jquery.easing-1.4.pack.js"></script-->

<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/jpicker/css/jPicker-1.1.5.min.css" />
<!--link rel="stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/jquery/jpicker/jPicker.css" /-->
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jpicker/jquery-1.4.2.min.js"></script-->
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jpicker/jpicker-1.1.5.min.js"></script>

<!-- JQuery : jqGrid -->
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/css/ui.jqgrid.css" />	
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.layout.js"></script--> 
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/i18n/grid.locale-en.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.jqGrid.min.js"></script> 
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.tablednd.js"></script> 
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/jquery/jqGrid/js/jquery.contextmenu.js"></script-->

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.jqGrid.fluid.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xlibs/jquery/Styles/Base.css"></link>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xlibs/jquery/Styles/BreadCrumb.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery.easing.1.3.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery.jBreadCrumb.1.1.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/CodeMirror/js/codemirror.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery.qtip-1.0.0-rc3.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jqfontselector.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/xlibs/jquery/joyride/joyride-2.1.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/joyride/jquery.joyride-2.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/joyride/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/joyride/modernizr.mq.js"></script>
      
<!--script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/dust/dust-full.min.js"></script>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/dust/dust-helpers.min.js"></script-->
      
<!--link rel="Stylesheet" type="text/css" media="screen" href="<%//=request.getContextPath()%>/xlibs/ColorFadingMenu/main.css"></link>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/ColorFadingMenu/jquery-color.js"></script>
<script type="text/javascript" src="<%//=request.getContextPath()%>/xlibs/ColorFadingMenu/main.js"></script-->
