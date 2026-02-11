<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE stylesheet [
<!ENTITY nbsp "<xsl:text disable-output-escaping='yes'>&amp;nbsp;</xsl:text>">
]>

<xsl:stylesheet version="1.0" extension-element-prefixes="view"	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:view="xalan://com.genesys.views.ViewStub">

<xsl:output method="html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>



<xsl:template match="/">
	<xsl:apply-templates select="//document/collection/object"/>
</xsl:template>



<xsl:template match="property[@name='links']">
	<xsl:for-each select="objectref">
		<li><a href="{propref[@name='uri']/text()}">
		<xsl:apply-templates select="propref[@name='title']/text()"/>
		</a></li>
	</xsl:for-each>
</xsl:template>

<xsl:template match="property[@name='styles']">
	<xsl:for-each select="objectref">
		<style type="text/css">
		<xsl:apply-templates select="propref[@name='body']/text()"/>
		</style>
	</xsl:for-each>
</xsl:template>

<xsl:template match="property[@name='scripts']">
	<xsl:for-each select="objectref">
		<script type="text/javascript">
		<xsl:apply-templates select="propref[@name='code']/text()"/>
		</script>
	</xsl:for-each>
</xsl:template>



<!-- Start of Page -->
<xsl:template match="object">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<meta name="Keywords" content="restaurant menus online easy setup take orders free account" />
<meta name="Description" content="Manage restaurant menus and take orders online for free"  />

<link rel="icon" href="../favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="../favicon.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" media="screen" href="../xlibs/jquery/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../xlibs/jquery/css/ui.theme.css" />
<link rel="stylesheet" type="text/css" href="../w3/styles/masterpage.css"></link>
<link rel="stylesheet" type="text/css" href="../xlibs/jquery/jquery.bxslider/jquery.bxslider.css"></link>
<xsl:apply-templates select="property[@name='styles']"/>

<script type="text/javascript" src="../xlibs/jquery/js/jquery-1.7.2.min.js"></script>
<!--script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script-->
<script type="text/javascript" src="../xlibs/jquery/jquery.corner.js"></script>
<link rel="stylesheet" type="text/css" href="../xlibs/jquery/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<script type="text/javascript" src="../xlibs/jquery/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="../xlibs/jquery/jquery.Storage.js"></script>
<script type="text/javascript" src="../xlibs/jquery/jquery.bxslider/jquery.bxslider.js"></script>

<xsl:apply-templates select="property[@name='scripts']"/>

<title>chowMagic - <xsl:value-of select="property[@name='title']/text()"/></title>
<script type="text/javascript">
function add(x,y){return x+y};
</script>

<script>
if(document.location.hostname.contains("chowmagic.com")){
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
  ga('create', 'UA-56056666-1', 'auto');
  ga('send', 'pageview');
}
</script>
</head>

<body>
<div id="wrapper">

	<div id="header_left"></div>
	<div id="header">
		<h1 style="margin-top: 8px;"><a href=""><img src="../w3/images/cooltext1734059047.png"/></a></h1>
    	<ul id="page_links">
    		<xsl:apply-templates select="property[@name='links']"/>
        </ul>
	</div>
	
	<!--div id="nav">
		<h2>Navigation</h2>
		<ul>
		<xsl:apply-templates select="property[@name='links']"/>
		</ul>
	</div-->
	
	<div id="main">
	<!--
		M A I N   F R A M E
	-->
		<xsl:value-of disable-output-escaping="yes" select="property[@name='content']/text()"/>	
	<!--
		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	-->
	</div>
	
	<div class="clear_float"></div>
	<hr/>
	<div id="footer">
		<a href="home">Home</a>&nbsp;&#8226;
		<a href="product_details">Product Details</a>&nbsp;&#8226;
		<a href="contact_us">Contact Us</a>&nbsp;&#8226;
		<a href="../app/account/create_account.jsp">Create a Free Account</a>
	</div>

</div>
</body>

</html>

</xsl:template>
<!-- End of Page -->


</xsl:stylesheet>
