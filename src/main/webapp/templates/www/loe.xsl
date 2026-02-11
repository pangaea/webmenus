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

<link rel="icon" href="../loe.ico" type="image/x-icon" />
<link rel="shortcut icon" href="../loe.ico" type="image/x-icon" />

<link rel="stylesheet" type="text/css" href="../loe/css/humanity/jquery-ui-1.8.13.custom.css" />
<link rel="stylesheet" type="text/css" href="../loe/css/styles.css" />
<link rel="stylesheet" type="text/css" href="../loe/topnav/topnav.css" />
<xsl:apply-templates select="property[@name='styles']"/>

<script type="text/javascript" src="../loe/scripts/jquery-1.6.1.min.js"></script>
<script type="text/javascript" src="../loe/scripts/jquery-ui-1.8.13.custom.min.js"></script>
<script type="text/javascript" src="../loe/scripts/jquery.extensions.js"></script>
<script type="text/javascript" src="../loe/topnav/topnav.js"></script>
<xsl:apply-templates select="property[@name='scripts']"/>

<title>LifeOnEuropa - Cloud Based Solutions</title>

</head>

<body>
    <div id="header">
        <!--div>
            <img src="../loe/images/logo3.png" alt="Europa The Moon" style="float:left;margin-left:2px;" />
            <img src="../loe/images/europa3_small.png" alt="Europa The Moon" style="float:left;margin-left:2px;width:64px;" />
            <div id="title_right" style="float:right;"></div>
        </div-->
        <div id="title">
            <!--h2>LifeOnEuropa</h2-->
            <div class="sub-title">Solutions in the Clouds</div>
            <div class="page_title">
            <span class="left">LIFE</span><span class="middle">on</span><span class="right">EUR<img src="../loe/images/europa3_small.png" alt="Europa The Moon" style="width:44px;margin-bottom:-4px;margin-left:-4px;" />PA</span>
            </div>
            
        </div>
        <div style="clear:both;"></div>
        
        <div id="nav">
            <ul class="topnav">
            	<xsl:apply-templates select="property[@name='links']"/>
            </ul>
        </div>
        <div style="clear:both;"></div>
    </div>
	
	<div id="content">
	<!--
		M A I N   F R A M E
	-->
		<xsl:value-of disable-output-escaping="yes" select="property[@name='content']/text()"/>	
	<!--
		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	-->
	</div>
	
    <div id="footer">
        <a href="loe_home">Home</a> | 
        <a href="loe_contact_us">Contact Us</a> | 
        <a href="loe_about_us">About Us</a>
    </div>

</body>

</html>

</xsl:template>
<!-- End of Page -->


</xsl:stylesheet>
