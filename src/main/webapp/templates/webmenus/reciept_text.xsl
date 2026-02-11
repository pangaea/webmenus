<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
Your Order Conformation
=====================================================
<xsl:apply-templates select="//order"/>
</xsl:template>

<xsl:template match="order">
Time:		<xsl:value-of select="@time"/>
Customer:	<xsl:value-of select="customer/email/text()"/> / <xsl:value-of select="customer/phone_num/text()"/>
Location:	<xsl:value-of select="location/text()"/>
Order Id:	<xsl:value-of select="@id"/>
Delivery:	<xsl:value-of select="delivery/text()"/>
Delivery Info:
<xsl:value-of select="delivery_info/text()"/>
=====================================================
<xsl:apply-templates select="//items"/>
=====================================================
Subtotal:				<xsl:value-of select="subtotal/text()"/>
Tax(<xsl:value-of select="tax_rate/text()"/>%):				<xsl:value-of select="tax/text()"/>
Total:				<xsl:value-of select="total/text()"/>
</xsl:template>

<xsl:template match="items">
Order Details
-----------------------------------------------------
<xsl:for-each select="item">
<xsl:value-of select="name/text()"/><xsl:if test="string-length(size/text())&gt;0"> : <xsl:value-of select="size/text()"/></xsl:if><xsl:text>
</xsl:text><xsl:value-of select="options/text()"/><xsl:text>
</xsl:text><xsl:value-of select="price/text()"/> X <xsl:value-of select="quantity/text()"/>
<xsl:text>&#9;</xsl:text><xsl:text>&#9;</xsl:text><xsl:text>&#9;</xsl:text><xsl:text>&#9;</xsl:text><xsl:text>&#9;</xsl:text>
<xsl:value-of select="itemtotal/text()"/>
-----------------------------------------------------
</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
