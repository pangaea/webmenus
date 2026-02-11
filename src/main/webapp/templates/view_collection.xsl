<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
	<xsl:apply-templates select="//return"/>
</xsl:template>

<xsl:template match="return">
	<return>
		<xsl:apply-templates select="//call"/>
		<xsl:apply-templates select="//code"/>
		<xsl:apply-templates select="//msg"/>
		<xsl:apply-templates select="//document"/>
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

<xsl:template match="document">
	<document>
		<xsl:apply-templates select="collection"/>
	</document>
</xsl:template>

<xsl:template match="collection">
	<collection name="{@name}" objStart="{@objStart}" objCount="{@objCount}" accessLevel="{@requestLevel}">
		<xsl:apply-templates select="object"/>
	</collection>
</xsl:template>

<xsl:template match="object">
	<object id="{@id}">
		<xsl:for-each select="property">
			<property name="{@name}">
				<xsl:apply-templates select="objectref"/>
				<xsl:value-of select="text()"/>
				<!--xsl:apply-templates select="object"/-->
			</property>
		</xsl:for-each>
	</object>
</xsl:template>

<xsl:template match="objectref">
	<objectref id="{@id}">
		<xsl:value-of select="text()"/>
	</objectref>
</xsl:template>

</xsl:stylesheet>
