<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
	<xsl:apply-templates select="//object"/>
</xsl:template>

<xsl:template match="object">
<xsl:variable name = "font_size" ><xsl:value-of select="property[@name='font_size']/text()"/></xsl:variable>
<xsl:variable name = "layout_columns" ><xsl:value-of select="property[@name='columns']/text()"/></xsl:variable>
a
{
	color: blue;
}
a::link
{
	color: blue;
}
a::visited
{
	color: blue;
}
a::hover
{
	color: blue;
}

a.liveOrderItem
{
	color: blue;
	font: normal 9pt <xsl:value-of select="property[@name='font']/text()"/>;
}

#menuBody
{
	text-align: center;
}
#frame
{
	/*width:100%;*/
	width: <xsl:value-of select="property[@name='menuwidth']/text()"/>;
	min-width: <xsl:value-of select="property[@name='menuwidth']/text()"/>;
	height:100%;
	background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/>;
	position: relative;
	margin: 0 auto;
	text-align: left;
}
#sample_menu_innner
{
	background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/>;
}
#menuHeader
{
	background-repeat: no-repeat;
	background-color: white;
	/*padding:0px 4px 0px 4px;*/
	/*height: 95px;*/
	margin-left: 0px;
	margin-top: 0px;
	margin-bottom: 4px;
	border-bottom: 2px solid black;
}
#menuFooter
{
	/*padding: 5px;*/
}
#menuTree
{
	position: absolute;
	top: 0px;
	left: 0px;
	width: 200px;
	background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/>;
}
#menuMenu
{
	/*padding: 5px;*/
}
#menuContent
{
	color: black;
	padding: 4px;
	background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/>;
	filter:shadow(color:gray, strength:10, direction:135);
}
#itemTable{
	width: 100%;
}
#itemTable tr th{
	/*border: 1px solid #<xsl:value-of select="property[@name='bkcolor']/text()"/>;*/
}
#itemTable tr td{
	border: 1px solid #<xsl:value-of select="property[@name='titlebar_color']/text()"/>;
	text-align: right;
}

.patronLoginTitle
{
	font: normal bolder 16pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: navy;
}

.patronLoginLabel
{
	font: normal bolder 9pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='item_text_color']/text()"/>;
}

.pageSeperator
{
	width:100%;
}

.pageSeperator hr
{
	background-color: #<xsl:value-of select="property[@name='system_text_color']/text()"/>;
	height:4px;
}

.pageSeperator span
{
	color: #<xsl:value-of select="property[@name='system_text_color']/text()"/>;
	font: normal <xsl:value-of select="12+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	font-weight: bold;
}

.menuItemContainter
{
	min-height: 100px;
	vertical-align: top;
	text-align: left;
	background: transparent;
	margin:2px;
	display: inline-block;
	border-width:0 3px;
	<xsl:if test="$layout_columns='1'">width: 100%;</xsl:if>
	<xsl:if test="$layout_columns='2'">width: 49%;</xsl:if>
	<xsl:if test="$layout_columns='3'">width: 32%;</xsl:if>
}

.menuTitle
{
	font: normal <xsl:value-of select="24+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='titlebar_color']/text()"/>;
	text-align: left;
	width: 100%;
}
.menuItem
{
	text-align: left;
}
.categoryTitle
{
	font: normal <xsl:value-of select="16+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: <xsl:value-of select="property[@name='cat_text_color']/text()"/>;
	text-align: center;
	width: 100%;
	background-color: #<xsl:value-of select="property[@name='titlebar_color']/text()"/>;
}
.menuItemTitle
{
	/*font: normal 14pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="14+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='item_text_color']/text()"/>;
	/*font-weight: bold;*/
	text-align: left;
	width: 100%;
}
.menuButton
{
	font: normal 9pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: blue;
}
.menuItemDesc
{
	/*font: normal 10pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="10+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='itemdesc_text_color']/text()"/>;
	/*font-weight: bold;*/
	text-align: left;
	width: 100%;
}
.menuItemPrices
{
	/*font: normal 9pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="9+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='item_text_color']/text()"/>;
}
.menuOptions
{
	border-top: 1px solid chocolate;
	/*font: normal 8pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="8+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='item_text_color']/text()"/>;
	text-align: left;
	width: 100%;
}

/*
	STYLES for menuitemview.jsp
*/
.menuItemOptions{
	margin-top: 2px;
}
.menuItemOptions table
{
	width: 100%;
}
.menuItemOptions div
{
	/*font: normal 10pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="10+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	float: left;
	color: #<xsl:value-of select="property[@name='option_text_color']/text()"/>;
	padding: 2px;
	vertical-align: middle;
	line-height: 18px;
}
.menuItemOptions div.menuItemOptionsTitle
{
	/*font: normal 12pt <xsl:value-of select="property[@name='font']/text()"/> !important;*/
	font: normal <xsl:value-of select="12+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/> !important;
	font-weight: bold;
}
span.alert
{
	font-size: 12pt;
	color: #<xsl:value-of select="property[@name='system_text_color']/text()"/>;
}

/*
	Dojo - Dijit overrides
*/

.tundra .dijitTreeNode{
      background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/> !important;
}
.tundra .dijitTreeNodeHover {
	background-color: #<xsl:value-of select="property[@name='option_text_color']/text()"/> !important;
}
.tundra .dijitSplitContainer-dijitContentPane,
.tundra .dijitBorderContainer-dijitContentPane {
    border: 1px #ccc solid;
    background-color: #<xsl:value-of select="property[@name='bkcolor']/text()"/>;
    padding: 5px;
}
#sample_menu_innner .menuItemContainter{
 width: 100% !important;
}

</xsl:template>

</xsl:stylesheet>
