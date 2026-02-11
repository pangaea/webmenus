<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
	<html>
	<head>
		<title>Welcome to chowMagic</title>
	</head>
	<body>
	<h2>Thank you, for making a new account with chowMagic</h2>
	<subscript>(Please verify that the information below is correct)</subscript>
	<hr/>
	<xsl:apply-templates select="//account"/>
	<p>
	Click <a href="http://www.chowmagic.com/webmenus/ui/login.jsp">here</a> to login to the chowMagic administration console or visit <a href="http://www.chowmagic.com">chowMagic</a>.
	</p>
	</body>
	</html>
</xsl:template>

<xsl:template match="account">
	<table>
	<tr>
		<th style="text-align:left;width:180px;">Username:</th>
		<td><xsl:value-of select="username/text()"/></td>
	</tr>
	<tr>
		<th style="text-align:left;">Fullname:</th>
		<td><xsl:value-of select="firstname/text()"/>&#160;<xsl:value-of select="lastname/text()"/></td>
	</tr>
	<tr>
		<th style="text-align:left;">Restaurant Name:</th>
		<td><xsl:value-of select="restaurant/name/text()"/></td>
	</tr>
	<tr>
		<th style="text-align:left;">Restaurant Email:</th>
		<td><xsl:value-of select="restaurant/email_addr/text()"/></td>
	</tr>
	</table>
</xsl:template>

</xsl:stylesheet>
