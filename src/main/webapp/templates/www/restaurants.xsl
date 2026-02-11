<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE stylesheet [
<!ENTITY nbsp "<xsl:text disable-output-escaping='yes'>&amp;nbsp;</xsl:text>">
]>

<xsl:stylesheet version="1.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
			doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

<xsl:template match="/">
<table class="main_panel">
	<xsl:apply-templates select="//document/collection/object"/>
</table>
</xsl:template>

<!-- Start of Page -->
<xsl:template match="object">

	<tr>
	<td align="center" valign="top" width="100%">
		<table class="grouping1" height="20px">
		<tr>
			<td class="grouptext" valign="top">
				<img class="groupimg" src="images/elisw/eliswhitney.jpg"/>
			</td>
			<td valign="top">
				<div class="grouptext">
					<a href="#"><xsl:value-of select="property[@name='name']/text()"/></a>
				</div>
				<div class="groupdescription">
				    <xsl:value-of select="property[@name='address']/text()"/><br/>
				    <xsl:value-of select="property[@name='city']/text()"/>, <xsl:value-of select="property[@name='state']/text()"/>
				</div>
			</td>
		</tr>
		</table>
	</td>
	</tr>
	
</xsl:template>

</xsl:stylesheet>
