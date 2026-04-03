<!--
Copyright (c) 2004-2005 Kevin Jacovelli
All Rights Reserved
-->
<%@ taglib uri="/WEB-INF/tlds/views.tld" prefix="viewCfg" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<jsp:useBean id="menuOrderBean" scope="session" class="com.genesys.webmenus.MenuOrderBean"/>
<%
	boolean bOpen = menuOrderBean.isWithinOpertingHours();
%>
<html>
	<head>
		<title>Patron Login</title>
<%
	String themeName = menuOrderBean.getTheme();
	if( themeName.length() > 0 ){
%>
<!-- Theme based includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/app/MenuView/theme?id=<%=themeName%>"></link>
<%
	} else {
%>
<!-- Default theme includes -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/themes/vanilla/styles/menu_view.css"></link>
<%
	}
%>
		<!--link rel="stylesheet" type="text/css" href="../styles/menu_view.css"></link-->
		<style type="text/css">
			@import "<%=request.getContextPath()%>/xlibs/dojo/dijit/themes/tundra/tundra.css";

			:where([autocomplete=one-time-code]) {
				--otp-digits: 6; /* length */
				--otc-ls: 2ch;
				--otc-gap: 1.25;
				/* private consts */
				--_otp-bgsz: calc(var(--otc-ls) + 1ch);

				all: unset;
				background: linear-gradient(90deg, 
				var(--otc-bg, #EEE) calc(var(--otc-gap) * var(--otc-ls)),
				transparent 0
				) 0 0 / var(--_otp-bgsz) 100%;
				caret-color: var(--otc-cc, #333);
				clip-path: inset(0% calc(var(--otc-ls) / 2) 0% 0%);
				font-family: ui-monospace, monospace;
				font-size: var(--otc-fz, 2.5em);
				inline-size: calc(var(--otc-digits) * var(--_otp-bgsz));
				letter-spacing: var(--otc-ls);
				padding-block: var(--otc-pb, 1ch);
				padding-inline-start: calc(((var(--otc-ls) - 1ch) / 2) * var(--otc-gap));
			}
		</style>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/dojo/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

		<script type="text/javascript">
		dojo.require("dijit.form.Button");
		</script>
	</head>
	<body class="tundra">
        <form id="verifyEmail" method="post" action="<%=request.getContextPath()%>/VerifyEmail">
        <input
            type="text"
            id="verify-code"
            spellcheck="false"
            autocomplete="one-time-code"
            inputmode="numeric"
            maxlength="7"
            pattern="\d{7}">
            <br/>
            <button dojoType="dijit.form.Button">
                Verify
                <script type="dojo/method" event="onClick">
                    verifyEmail.submit();
                </script>
            </button>
        </form>
    </body>
</html>