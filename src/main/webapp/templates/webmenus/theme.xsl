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
	text-align: center;
	background: #FFFFFF;
	margin: 10px;
	display: inline-block;
	border: 3px;
	<!-- <xsl:if test="$layout_columns='1'">width: 100%;</xsl:if>
	<xsl:if test="$layout_columns='2'">width: 46%;</xsl:if>
	<xsl:if test="$layout_columns='3'">width: 28%;</xsl:if> -->
	width: <xsl:value-of select="property[@name='itemwidth']/text()"/>;
	box-shadow: 4px 2px 6px rgba(155, 155, 133, 50.25);
	border-radius: 12px;
	padding: 10px;
}

.menuTitle
{
	font: normal <xsl:value-of select="24+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #000000;
	text-align: left;
	width: 100%;
	margin-top: 20px;
}
.menuItem
{
	text-align: left;
}
.categoryTitle
{
	font: normal <xsl:value-of select="16+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: <xsl:value-of select="property[@name='cat_text_color']/text()"/>;
	width: 100%;
	background-color: #<xsl:value-of select="property[@name='titlebar_color']/text()"/>;
	display: grid;
  	place-items: center;
	min-height: 50px;
	margin-top: 20px;
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

:root {
	--primary: #d94f2b;
	--primary-dark: #ae3518;
	--accent: #f8c146;
	--background: #fffaf5;
	--surface: #ffffff;
	--text: #27231f;
	--muted: #746d66;
	--border: #eadfd5;
	--success: #218739;
	--shadow: 0 10px 30px rgba(67, 45, 29, 0.1);
	--radius: 18px;
}

.cart-drawer {
	position: fixed;
	top: 0;
	right: 0;
	z-index: 201;
	display: flex;
	width: min(420px, 100%);
	height: 100dvh;
	flex-direction: column;
	background: var(--surface);
	box-shadow: -15px 0 40px rgba(0, 0, 0, 0.16);
	transform: translateX(105%);
	transition: 220ms ease;
}

.cart-drawer.open {
	transform: translateX(0);
}

.cart-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 22px;
	border-bottom: 1px solid var(--border);
}

.cart-header h2 {
	margin: 0;
}

.close-button {
	display: grid;
	width: 38px;
	height: 38px;
	place-items: center;
	border: 1px solid var(--border);
	border-radius: 50%;
	background: white;
	font-size: 1.2rem;
}

.cart-items {
	flex: 1;
	overflow-y: auto;
	padding: 10px 22px;
}

.empty-cart {
	padding: 60px 20px;
	text-align: center;
	color: var(--muted);
}

.empty-cart span {
	display: block;
	margin-bottom: 10px;
	font-size: 3rem;
}

.cart-item {
	display: grid;
	grid-template-columns: 52px 1fr auto;
	gap: 12px;
	padding: 16px 0;
	border-bottom: 1px solid var(--border);
}

.cart-item-icon {
	display: grid;
	width: 52px;
	height: 52px;
	place-items: center;
	border-radius: 13px;
	background: #fff0dc;
	font-size: 1.9rem;
}

.cart-item h4 {
	margin: 0 0 3px;
}

.cart-item-price {
	color: var(--muted);
	font-size: 0.85rem;
}

.cart-button-container {
	text-align:right;
	width:100%;
	padding-right:20px;
}

.cart-button {
	display: flex;
	align-items: right;
	gap: 10px;
	padding: 10px 16px;
	border: 0;
	border-radius: 999px;
	background: var(--text);
	color: white;
	font-weight: 700;
	float: right;
}

.cart-button span:first-child {
	margin: auto;
}

.cart-count {
	display: grid;
	min-width: 24px;
	height: 24px;
	padding-inline: 6px;
	place-items: center;
	border-radius: 999px;
	background: var(--accent);
	color: var(--text);
	font-size: 0.8rem;
}

</xsl:template>

</xsl:stylesheet>
