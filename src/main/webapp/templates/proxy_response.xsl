<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
	<xsl:apply-templates select="//return"/>
</xsl:template>

<xsl:template match="return">
	<return>
		<xsl:apply-templates select="//call"/>
		<xsl:apply-templates select="//code"/>
		<xsl:apply-templates select="//msg"/>
	</return>
</xsl:template>

<xsl:template match="call">
	<call><xsl:value-of select="text()"/></call>
</xsl:template>

<xsl:template match="code">
	<code><xsl:value-of select="text()"/></code>
</xsl:template>

<xsl:template match="msg">
	<msg><xsl:value-of select="text()"/></msg>
</xsl:template>

</xsl:stylesheet>
