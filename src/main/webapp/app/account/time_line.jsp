
<table id="timeline" cellpadding="0" cellspacing="0">
<tr>
<%
int SelIdx = Integer.parseInt(request.getParameter("SelIdx"));
%>
<td nowrap><%if(SelIdx==0){%><b><%}%>Account Information<%if(SelIdx==0){%></b><%}%></td>
<!--td nowrap><%if(SelIdx==1){%><b><%}%>Personal Information<%if(SelIdx==1){%></b><%}%></td-->
<td nowrap><%if(SelIdx==2){%><b><%}%>Restaurant Information<%if(SelIdx==2){%></b><%}%></td>
<td nowrap><%if(SelIdx==3){%><b><%}%>Summary<%if(SelIdx==3){%></b><%}%></td>
</tr>
<tr>
<td width="33%"><table width="100%" cellpadding="0" cellspacing="0"><tr><td><hr style="visibility:hidden;"/></td><td width="10px"><div class="<%if(SelIdx==0){%>cur<%}else{%>dest<%}%>"></div></td><td><hr/></td></tr></table></td>
<!--td width="25%"><table width="100%" cellpadding="0" cellspacing="0"><tr><td><hr/></td><td width="10px"><div class="<%if(SelIdx==1){%>cur<%}else{%>dest<%}%>"></div></td><td><hr/></td></tr></table></td-->
<td width="33%"><table width="100%" cellpadding="0" cellspacing="0"><tr><td><hr/></td><td width="10px"><div class="<%if(SelIdx==2){%>cur<%}else{%>dest<%}%>"></div></td><td><hr/></td></tr></table></td>
<td width="33%"><table width="100%" cellpadding="0" cellspacing="0"><tr><td><hr/></td><td width="10px"><div class="<%if(SelIdx==3){%>cur<%}else{%>dest<%}%>"></div></td><td><hr style="visibility:hidden;"/></td></tr></table></td>
</tr>
</table>