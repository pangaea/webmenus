<!--
Copyright (c) 2004-2009 Kevin Jacovelli
All Rights Reserved
-->
<%@ page import="com.genesys.SystemServlet"%>
<%@ page import="com.genesys.repository.*"%>
<%//@ include file="auth.jsp" %>
<%@ include file="clienthdr.jsp" %>
<%
ObjectManager objectBean = SystemServlet.getObjectManager();
%>
<%=objectBean.generateReport()%>