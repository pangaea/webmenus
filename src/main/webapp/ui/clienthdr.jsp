<!--
Copyright (c) 2004-2016 Kevin Jacovelli
All Rights Reserved
-->

<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<jsp:useBean id="clientSessionBean" scope="session" class="com.genesys.session.ClientSessionBean"/>
<jsp:useBean id="viewConfigBean" scope="page" class="com.genesys.views.ViewConfigBean"/>
<%
	//viewConfigBean.setDocument( clientSessionBean.getDocument() );
%>
