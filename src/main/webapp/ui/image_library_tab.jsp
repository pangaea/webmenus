<!--
Copyright (c) 2004-2012 Kevin Jacovelli
All Rights Reserved
-->

<%
	String parentProp = (String)request.getParameter("parentProp");
%>

<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>

<table height="100%" width="100%">
<tr>
	<td  valign="top" style="height:100%;">

	<div>
<%
HttpSession thisSession = request.getSession();
String type = request.getParameter("type");
String imgPath = "";

if( type.equalsIgnoreCase("image") == true )
{
	imgPath += "public";
}
else if( type.equalsIgnoreCase("myimage") == true )
{
	imgPath += "personal/" + (String)thisSession.getAttribute("role");
}
%>
	<viewCfg:EnumImages relPath="<%=imgPath%>">
<%
	String imageSrc = request.getContextPath() + "/ImageViewer/" + imageFilePath;
%>
		<div style="float:left;padding-bottom:4px;">
		<div style="text-align:center;">
		
<%	if( parentProp != null && parentProp.length() > 0 ){%>
		<a title="Select '<%=imageFileName%>'" href="#" onclick="SelectImageByTitle('<%=imageFilePath%>'); buttonCallback(B_SELECT);"/>
<% 	}else{ %>
		<a target="_blank" title="<%=imageFilePath%>" href="<%=imageSrc%>" rel="facebox" onclick="SelectImageByTitle('<%=imageFilePath%>');">
<%	} %>
			<img title="<%=imageFilePath%>" style="cursor:hand;border:4px solid #FFFFFF;width:100px;height:75px;" maxwidth="100" maxheight="100" src="<%=imageSrc%>"/>
		</a>
		</div>
		<div style="text-align:center;display:none;">
			<a target="_blank" title="<%=imageFilePath%>" href="<%=imageSrc%>" rel="facebox"><!--%=imageFileName%></a-->
				<img title="<%=imageFilePath%>" style="cursor:hand;border:4px solid #FFFFFF;width:100px;height:75px;" maxwidth="100" maxheight="100" onclick="SelectImage(this,'<%=imageFileName%>');" src="<%=request.getContextPath()%>/<%=imageFilePath%>"/>
			</a>
		</div>
		<div style="text-align:center;">
			<a title="View '<%=imageFileName%>'" href="<%=imageSrc%>" rel="facebox" onclick="SelectImageByTitle('<%=imageFilePath%>');"><img border="0" src="<%=request.getContextPath()%>/images/browse.gif"/></a>
			<a title="Delete '<%=imageFileName%>'" class="admin-only" href="#"><img border="0" src="<%=request.getContextPath()%>/images/cross-circle.png" onclick="SelectImageByTitle('<%=imageFilePath%>'); buttonCallback(B_DELETE);"/></a>
<%	if( parentProp != null && parentProp.length() > 0 ){%>
			<a title="Select '<%=imageFileName%>'" href="#"><img border="0" src="<%=request.getContextPath()%>/images/correct.png" onclick="SelectImageByTitle('<%=imageFilePath%>'); buttonCallback(B_SELECT);"/></a>
<% 	} %>
		</div>
		</div>
	</viewCfg:EnumImages>
	</div>
	</td>
</tr>
</table>
