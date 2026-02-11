<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
	<html>
	<head>
		<title>Reciept</title>
		<style>
		.menuItemTitle
		{
			font: normal bolder 14pt Arial;
			color: brown;
			text-align: left;
			width: 100%;
		}
		.menuItemDesc
		{
			font: normal bolder 10pt Arial;
			color: black;
			font-weight: bold;
			text-align: left;
			width: 100%;
		}
		.usd
		{
			text-align: right !important;
		}
		.quantity
		{
			text-align: center !important;
		}
		.orderTable
		{
			/*border:1px solid #000000;*/
		}
		.orderTable th
		{
			margin: 2px;
			padding: 2px;
			/*border:1px solid #000000;*/
			text-align:left;
			background-color: #999999;
		}
		.orderTable td
		{
			margin: 2px;
			padding: 2px;
			/*border:1px solid #000000;*/
			text-align:left;
		}

		</style>
	</head>
	<body>
	<xsl:apply-templates select="//order"/>
	</body>
	</html>
</xsl:template>

<xsl:template match="order">
	<table>
	<tr><th style="text-align:left;">Time:</th><td><xsl:value-of select="@time"/></td></tr>
	<tr><th style="text-align:left;">Customer email/phone:</th><td><xsl:value-of select="customer/email/text()"/> / <xsl:value-of select="customer/phone_num/text()"/></td></tr>
	<tr><th style="text-align:left;">Location:</th><td><xsl:value-of select="location/text()"/></td></tr>
	<tr><th style="text-align:left;">Order Id:</th><td><xsl:value-of select="@id"/></td></tr>
	<tr><th style="text-align:left;">Delivery:</th><td><xsl:value-of select="delivery/text()"/></td></tr>
	<tr><th style="text-align:left;" valign="top">Delivery Info:</th><td>
<pre>
<xsl:value-of select="delivery_info/text()"/>
</pre>
	</td></tr>
	<tr><th style="text-align:left;" colspan="2"><hr width="100%"/></th></tr>
	<tr><td colspan="2">
		<table class="orderTable">
		<tr><th width="400px">Order</th>
		<th>Each</th>
		<th>Quantity</th>
		<th>Price</th></tr>
			<xsl:apply-templates select="//items"/>
		<tr>
		<td colspan="4"><hr/></td>
		</tr><tr>
		<td colspan="3">Subtotal:</td>
		<td class="usd"><xsl:value-of select="subtotal/text()"/></td>
		</tr>
		<tr>
		<td colspan="3">Tax(<xsl:value-of select="tax_rate/text()"/>%)</td>
		<td class="usd"><xsl:value-of select="tax/text()"/></td>
		</tr>
		<tr>
		<td colspan="3">Total:</td>
		<td class="usd"><xsl:value-of select="total/text()"/></td>
		</tr>
		</table>
	</td></tr>
	</table>
</xsl:template>

<xsl:template match="items">
	<xsl:for-each select="item">
	<tr>
	<td valign="top">
		<div class='menuItemTitle'><xsl:value-of select="name/text()"/><xsl:if test="string-length(size/text())&gt;0"> : <xsl:value-of select="size/text()"/></xsl:if></div>
		<!--div class='menuItemDesc'><xsl:value-of select="description/text()"/></div-->
<pre>
<xsl:value-of select="options/text()"/>
</pre>
	</td>
	<td valign="top" class="usd"><xsl:value-of select="price/text()"/></td>
	<td valign="top" class="quantity"><xsl:value-of select="quantity/text()"/></td>
	<td valign="top" class="usd"><xsl:value-of select="itemtotal/text()"/></td>
	</tr>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
