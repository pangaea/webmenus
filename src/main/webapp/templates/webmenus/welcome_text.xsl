<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
Congratulation on your new account with chowMagic
=====================================================
<xsl:apply-templates select="//account"/>
</xsl:template>

<xsl:template match="account">
Username:	<xsl:value-of select="username/text()"/>
Fullname:	<xsl:value-of select="firstname/text()"/>&#160;<xsl:value-of select="lastname/text()"/>
</xsl:template>

</xsl:stylesheet>
