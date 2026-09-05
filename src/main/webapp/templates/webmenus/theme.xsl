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
h1.locationName
{
	padding-top: 20px;
    padding-left: 20px;
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
.menuItemImage
{
	max-width: 400px;
	max-height: 400px;
}
.menuItemButtons
{
	padding: 4px;
	height: 20px;
}
.menuItemButtons a:first-child {
	float: left;
}
.menuItemButtons a:last-child {
	float: right;
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
	padding: 4px;
}
.menuItemSizeDesc
{
  font-weight: 300;
  color: #6c757d;
}
.menuOptions
{
	border-top: 1px solid chocolate;
	/*font: normal 8pt <xsl:value-of select="property[@name='font']/text()"/>;*/
	font: normal <xsl:value-of select="8+$font_size"/>pt <xsl:value-of select="property[@name='font']/text()"/>;
	color: #<xsl:value-of select="property[@name='item_text_color']/text()"/>;
	text-align: left;
	width: 100%;
	clear: both;
	padding-top: 10px;
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

body.cart-page {
	margin: 0;
	padding: 24px;
	background: var(--background);
	color: var(--text);
	font: 14px/1.6 "Segoe UI", Arial, sans-serif;
}

.cart-page *,
.cart-page *::before,
.cart-page *::after {
	box-sizing: border-box;
}

.cart-page-header,
.cart-page #orderForm,
.cart-page .cart-checkout {
	width: 100%;
	max-width: 900px;
	margin-inline: auto;
}

.cart-page-header {
	margin-bottom: 24px;
}

.cart-page .cart-eyebrow {
	margin: 0 0 6px;
	color: var(--primary-dark);
	font-size: 11px;
	font-weight: 800;
	letter-spacing: 0.14em;
}

.cart-page-header h1 {
	margin: 0;
	font-size: clamp(26px, 5vw, 36px);
	line-height: 1.2;
	letter-spacing: -0.035em;
}

.cart-page-header > p:last-child {
	margin: 8px 0 0;
	color: var(--muted);
}

/* Table surface */

.cart-page #orderForm {
	padding: 8px 20px;
	border: 1px solid var(--border);
	border-radius: var(--radius);
	background: var(--surface);
	box-shadow: var(--shadow);
}

.cart-page #itemTable {
	width: 100%;
	table-layout: fixed;
	border-collapse: collapse;
}

.cart-page #itemTable tr th,
.cart-page #itemTable tr td {
	padding: 18px 10px;
	border: 0;
	vertical-align: top;
}

.cart-page #itemTable .cart-column-headings th {
	width: 16%;
	border-bottom: 1px solid var(--border);
	color: var(--muted);
	font-size: 11px;
	font-weight: 700;
	letter-spacing: 0.06em;
	text-align: right;
	text-transform: uppercase;
}

.cart-page #itemTable .cart-column-headings th:first-child {
	width: 52%;
	text-align: left;
}

.cart-page #itemTable .cart-product-row td {
	border-bottom: 1px solid var(--border);
	text-align: right;
	font-variant-numeric: tabular-nums;
}

.cart-page #itemTable .cart-product-row td:first-child {
	text-align: left;
}

.cart-page #itemTable .cart-product-row td:last-child {
	font-weight: 750;
}

/* Item details */

.cart-page .menuItemTitle {
	margin-bottom: 6px;
	color: var(--text);
	font: 700 16px/1.4 "Segoe UI", Arial, sans-serif;
	overflow-wrap: anywhere;
}

.cart-page .menuItemDesc {
	color: var(--muted);
	font: 400 13px/1.6 "Segoe UI", Arial, sans-serif;
}

.cart-page .menuItemButtons {
	display: flex;
	align-items: center;
	gap: 10px;
	height: auto;
	padding: 12px 0;
}

.cart-page .menuItemButtons a {
	display: inline-flex;
	min-height: 36px;
	align-items: center;
	padding: 6px 12px;
	float: none;
	border-radius: 8px;
	background: #fff1e8;
	color: var(--primary-dark);
	font-size: 12px;
	font-weight: 700;
	text-decoration: none;
	transition: background-color 160ms ease;
}

.cart-page .menuItemButtons a:hover {
	background: #ffe0cd;
}

.cart-page .menuItemButtons a:last-child {
	background: #fdf0ee;
	color: #a63030;
}

.cart-page .menuItemButtons a:last-child:hover {
	background: #f9dcd7;
}

.cart-page .menuOptions {
	padding: 10px 12px;
	border: 1px solid var(--border);
	border-radius: 10px;
	background: var(--background);
	color: var(--muted);
	font: 400 12px/1.7 "Segoe UI", Arial, sans-serif;
	overflow-wrap: anywhere;
}

.cart-page .menuOptions b {
	color: var(--text);
	font-weight: 600;
}

/* Dojo quantity spinner */

.cart-page .dijitNumberSpinner {
	width: 76px;
	min-height: 36px;
	border: 1px solid var(--border);
	border-radius: 8px;
	background: var(--surface);
	color: var(--text);
}

.cart-page .dijitNumberSpinner .dijitInputField {
	padding: 6px 2px;
}

.cart-page .dijitNumberSpinner input {
	font: 600 14px/1.4 "Segoe UI", Arial, sans-serif;
	text-align: center;
}

.cart-page .dijitNumberSpinner .dijitArrowButton {
	background: #fff1e8;
}

.cart-page .dijitNumberSpinnerFocused {
	border-color: var(--primary);
	outline: 3px solid rgba(217, 79, 43, 0.18);
	outline-offset: 2px;
}

/* Order summary */

.cart-page #itemTable .cart-summary-row th,
.cart-page #itemTable .cart-summary-row td {
	padding-block: 10px;
	text-align: right;
	font-variant-numeric: tabular-nums;
}

.cart-page #itemTable .cart-summary-row th {
	color: var(--muted);
	font-size: 13px;
	font-weight: 500;
}

.cart-page .cart-summary-row small {
	display: block;
	font-size: 11px;
}

.cart-page #itemTable .cart-total-row th,
.cart-page #itemTable .cart-total-row td {
	padding-block: 18px;
	border-top: 1px solid var(--border);
	color: var(--text);
	font-size: 19px;
	font-weight: 800;
}

.cart-page #itemTable .cart-total-row td {
	color: var(--primary-dark);
}

/* Dojo checkout button */

.cart-page .cart-checkout {
	margin-top: 20px;
}

.cart-page .cart-checkout .dijitButton {
	/* display: block;
	width: 100%; */
	margin: 0;
}

.cart-page .cart-checkout .dijitButtonNode {
	display: block;
	width: 100%;
	padding: 15px 20px;
	border: 1px solid var(--primary-dark);
	border-radius: 12px;
	background: var(--primary-dark);
	box-shadow: 0 6px 16px rgba(174, 53, 24, 0.18);
	color: #fff;
	cursor: pointer;
	transition: background-color 160ms ease, box-shadow 160ms ease;
}

.cart-page .cart-checkout .dijitButtonText {
	font: 700 16px/1.4 "Segoe UI", Arial, sans-serif;
}

.cart-page .cart-checkout .dijitButtonHover .dijitButtonNode {
	background: #922b14;
	box-shadow: 0 8px 20px rgba(174, 53, 24, 0.24);
}

.cart-page .cart-checkout .dijitButtonActive .dijitButtonNode {
	background: #792411;
	box-shadow: none;
}

.cart-page a:focus-visible,
.cart-page .cart-checkout .dijitButtonFocused .dijitButtonNode {
	outline: 3px solid var(--primary);
	outline-offset: 3px;
}

/* Compact cart / iframe layout */

@media (max-width: 600px) {
	body.cart-page {
		padding: 16px;
	}

	.cart-page #orderForm {
		padding: 6px 14px;
	}

	.cart-page #itemTable,
	.cart-page #itemTable tbody {
		display: block;
	}

	/* Visually hide headings while retaining them for accessibility. */
	.cart-page #itemTable .cart-column-headings {
		position: absolute;
		width: 1px;
		height: 1px;
		overflow: hidden;
		clip-path: inset(50%);
		white-space: nowrap;
	}

	.cart-page #itemTable .cart-product-row {
		display: grid;
		grid-template-columns: repeat(3, minmax(0, 1fr));
		gap: 14px 8px;
		padding: 18px 0;
		border-bottom: 1px solid var(--border);
	}

	.cart-page #itemTable .cart-product-row td {
		min-width: 0;
		padding: 0;
		border: 0;
		text-align: left;
	}

	.cart-page #itemTable .cart-product-row td:first-child {
		grid-column: 1 / -1;
	}

	.cart-page .cart-product-row td:not(:first-child)::before {
		display: block;
		margin-bottom: 6px;
		color: var(--muted);
		font-size: 11px;
		font-weight: 600;
	}

	.cart-page .cart-product-row td:nth-child(2)::before {
		content: "Each";
	}

	.cart-page .cart-product-row td:nth-child(3)::before {
		content: "Quantity";
	}

	.cart-page .cart-product-row td:nth-child(4)::before {
		content: "Price";
	}

	.cart-page #itemTable .cart-product-row td:last-child {
		text-align: right;
	}

	.cart-page #itemTable .cart-summary-row {
		display: grid;
		grid-template-columns: minmax(0, 1fr) auto;
		align-items: baseline;
	}

	.cart-page #itemTable .cart-summary-row th:nth-child(-n+2) {
		display: none;
	}

	.cart-page #itemTable .cart-summary-row th,
	.cart-page #itemTable .cart-summary-row td {
		padding-inline: 0;
	}

	.cart-page #itemTable .cart-summary-row th {
		text-align: left;
	}

	.cart-page .cart-summary-row small {
		display: inline;
		margin-left: 4px;
	}
}

@media (prefers-reduced-motion: reduce) {
	.cart-page .menuItemButtons a,
	.cart-page .cart-checkout .dijitButtonNode {
		transition: none;
	}
}

</xsl:template>

</xsl:stylesheet>
