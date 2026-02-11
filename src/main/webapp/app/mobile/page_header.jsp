<!--
Copyright (c) 2004-2012 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%@ page import="com.genesys.repository.*"%>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	// Load location information into the order bean
	RepositoryObject currentLocation = (RepositoryObject)request.getAttribute("locationObject");
	menuOrderBean.setCurrentLocation( currentLocation );
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
	String rootMenuPath = request.getContextPath() + "/Menus/" + currentLocation.getId();
	String exitURL = menuOrderBean.getExitURL();
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/mobile/styles/main.css"></link>
