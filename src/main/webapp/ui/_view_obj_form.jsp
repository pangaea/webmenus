<div class="form_fields">
<table>
	<tr>
		<td valign="top">
		<form id="obj_sel_data" method="POST" action="view_obj.jsp" onsubmit="enableEnums();">
			<table>
				<%  String filterStr = ""; %>
				<viewCfg:ViewForm viewName="<%=viewConfigBean.getView()%>">
					<%
						if( inputConstraint.equalsIgnoreCase( "nextCol" ) == true ){
					%>
							</table></td><td valign="top"><table>
					<% 	}
						String searchParam = request.getParameter( viewParam + "_filter_" + inputField );
						if( searchParam != null ){	// No persistent search param
							filterStr += "&" + inputField + "=" + searchParam;
					%>
						<input type="hidden" NAME="<%=viewParam%>_filter_<%=inputField%>" VALUE="<%=searchParam%>"/>
					<%
						}
						if( inputVisible.equalsIgnoreCase( "false" ) == false ){
					%>
							<tr><td valign="top">
							<table width="100%"><tr><td nowrap valign="top" align="right" class="property_text">
							<%=inputText%>:
							</td></tr></table>
							</td><td width="100%" valign="top">
							<div id="DIV_<%=inputField%>" style="line-height:32px;vertical-align:top;">
					<% 		if( inputType.equalsIgnoreCase( "text" ) == true ){ %>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" maxlength="<%=inputLen%>" size="<%=inputWidth%>"/>
					<%		}else if( inputType.equalsIgnoreCase( "password" ) == true ){ %>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="password" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" maxlength="<%=inputLen%>"/>
							/ <input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="password" ID="<%=inputField%>_vrfy" NAME="<%=viewParam%>_data_<%=inputField%>_vrfy" maxlength="<%=inputLen%>"/>
					<%		}else if( inputType.equalsIgnoreCase( "boolean" ) == true ){ %>
							<input type="checkbox" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"/>
					<% 		}else if( inputType.equalsIgnoreCase( "textarea" ) == true ){ %>
							<textarea style="height:<%=inputHeight%>;width:<%=inputWidth%>;" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"></textarea>
					<% 		}else if( inputType.equalsIgnoreCase( "html" ) == true ){ %>
							<div style="border:1px solid black;width:100%">
							<textarea ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"></textarea>
							</div>
							<script type="text/javascript"> 
							codemirrorCrtls['<%=inputField%>'] = CodeMirror.fromTextArea('<%=inputField%>', {
							    height: "dynamic",
							    width: "100%",
							    parserfile: ["parsexml.js", "parsecss.js", "tokenizejavascript.js", "parsejavascript.js", "parsehtmlmixed.js"],
							    stylesheet: ["<%=request.getContextPath()%>/xlibs/CodeMirror/css/xmlcolors.css",
							                 "<%=request.getContextPath()%>/xlibs/CodeMirror/css/jscolors.css",
							                 "<%=request.getContextPath()%>/xlibs/CodeMirror/css/csscolors.css"],
							    path: "<%=request.getContextPath()%>/xlibs/CodeMirror/js/"
							  });
							</script>
					<% 		}else if( inputType.equalsIgnoreCase( "css" ) == true ){ %>
							<div style="border:1px solid black;width:100%">
							<textarea ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"></textarea>
							</div>
							<script type="text/javascript"> 
							codemirrorCrtls['<%=inputField%>'] = CodeMirror.fromTextArea('<%=inputField%>', {
							    height: "dynamic",
							    width: "100%",
							    parserfile: "parsecss.js",
							    stylesheet: "<%=request.getContextPath()%>/xlibs/CodeMirror/css/csscolors.css",
							    path: "<%=request.getContextPath()%>/xlibs/CodeMirror/js/"
							  });
							</script> 
					<% 		}else if( inputType.equalsIgnoreCase( "js" ) == true ){ %>
							<div style="border:1px solid black;width:100%">
							<textarea ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"></textarea>
							</div>
							<script type="text/javascript"> 
							codemirrorCrtls['<%=inputField%>'] = CodeMirror.fromTextArea('<%=inputField%>', {
							    height: "dynamic",
							    width: "100%",
							    parserfile: ["tokenizejavascript.js", "parsejavascript.js"],
							    stylesheet: "<%=request.getContextPath()%>/xlibs/CodeMirror/css/jscolors.css",
							    path: "<%=request.getContextPath()%>/xlibs/CodeMirror/js/"
							  });
							</script> 
					<% 		}else if( inputType.equalsIgnoreCase( "date" ) == true ){ %>
						<!-- New Calendar -->
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" datatype="date"/></td><td><img id="button_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" src="<%=request.getContextPath()%>/images/calendar.gif" onclick="widgets.showWidget('calendar',this,'<%=inputField%>');"></img>
							</td></tr></table>
							<!--script type="text/javascript">
								Calendar.setup({
									inputField     :    "<-%=inputField%->",      // id of the input field
									//ifFormat       :    "%m/%d/%Y %I:%M %p",       // format of the input field
									ifFormat       :    "%m/%d/%Y",       // format of the input field
									//showsTime      :    true,            // will display a time selector
									showsTime      :    false,            // will display a time selector
									button         :    "button_<-%=inputField%->",   // trigger for the calendar (button ID)
									singleClick    :    false,           // double-click mode
									step           :    1                // show all years in drop-down boxes (instead of every other year as default)
								});
							</script-->
						<!-- New Calendar -->
							<!--input type="text" ID="<-%=inputField%->" NAME="<-%=viewParam%->_data_<-%=inputField%->" onkeypress="validateinput( this, 'date' );" onblur="formatinput( this, 'date', 0 );"/-->
					<% 		}else if( inputType.equalsIgnoreCase( "datetime" ) == true ){ %>
						<!-- New Calendar -->
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" datatype="datetime"/>
							</td></tr></table>
							<!--script type="text/javascript">
								Calendar.setup({
									inputField     :    "<-%=inputField%->",      // id of the input field
									ifFormat       :    "%m/%d/%Y %I:%M %p",
									showsTime      :    true,            // will display a time selector
									button         :    "button_<-%=inputField%->",   // trigger for the calendar (button ID)
									singleClick    :    false,           // double-click mode
									step           :    1,                // show all years in drop-down boxes (instead of every other year as default)
									timeFormat	   :    "12"
								});
							</script-->
						<!-- New Calendar -->
							<!--input type="text" ID="<-%=inputField%->" NAME="<-%=viewParam%->_data_<-%=inputField%->" onkeypress="validateinput( this, 'date' );" onblur="formatinput( this, 'date', 0 );"/-->
					<% 		}else if( inputType.equalsIgnoreCase( "time" ) == true ){ %>
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" datatype="time"/>
							</td></tr></table>
					<% 		}else if( inputType.equalsIgnoreCase( "real" ) == true ){  %>
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" datatype="real" precision="2"/>
							</td></tr></table>
					<% 		}else if( inputType.equalsIgnoreCase( "int" ) == true ){ %>
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" datatype="int"/>
							</td></tr></table>
					<% 		}else if( inputType.equalsIgnoreCase( "color" ) == true ){  %>
							<table cellpadding="0" cellspacing="0"><tr><td>
							<input class="color_picker" WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="text" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" maxlength="<%=inputLen%>" size="<%=inputWidth%>"/>
							</td></tr></table>
					<% 		}else if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="hidden" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>"/>
										<input type="text" ID="display_<%=inputField%>" NAME="<%=viewParam%>_display_<%=inputField%>" readonly="true"/>
									</td>
									<td>
										<img id="button_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="buttonCallback(B_SELECT,'view=<%=inputView%>&parentProp=<%=inputField%>&displayProp=<%=inputDisplay%>');" src="<%=request.getContextPath()%>/images/browse.gif"></img>
										<img id="clear_obj_<%=inputField%>" title="Clear" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="clearObject('<%=inputField%>');" src="<%=request.getContextPath()%>/images/cross-circle.png"></img>
									</td>
								</tr>
							</table>
					<% 		}else if( inputType.equalsIgnoreCase( "image" ) == true ){ %>
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<img id="<%=inputField%>_img" class="resizeme_image" maxwidth="140" maxheight="140" onload="reSizeImages()" onclick="window.open(this.src, '_blank');" style="cursor:hand;"/>
										<input WMrequired="<%=inputRequired%>" title="<%=inputText%>" type="hidden" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" maxlength="<%=inputLen%>"/>
									</td>
									<td valign="top">
										<img id="button_<%=inputField%>" title="Select" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="buttonCallback(B_SELECTIMAGE,'parentProp=<%=inputField%>');" src="<%=request.getContextPath()%>/images/browse.gif"></img>
										<img id="clear_image_<%=inputField%>" title="Clear" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="clearImage('<%=inputField%>');" src="<%=request.getContextPath()%>/images/cross-circle.png"></img>
									</td>
								</tr>
							</table>
					<% 		}else if( inputType.equalsIgnoreCase( "list" ) == true ){ %>
							<table border="0" cellspacing="0" cellpadding="0">
							<tr><td>
							<select style="width:145px;" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" size="<%=inputHeight%>">
							</select>
							</td><td valign="top">
								<table border="0" cellspacing="0" cellpadding="0"><tr><td>
								<%
									String sFilter = new String("");
									if( inputFilter.length() > 0 ){
										String filterParamName = viewParam + "_filter_" + inputFilter;
										sFilter = "&filter=" + inputFilter + "%3D" + request.getParameter(filterParamName);
									}
								%>
								<!--SPAN ID="add_<#%=inputField%#>"></SPAN-->
								<img id="button_add_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="buttonCallback(B_SELECT,'view=<%=inputView%>&parentProp=<%=inputField%>&displayProp=<%=inputDisplay%><%=sFilter%>&poptype=eol');" src="<%=request.getContextPath()%>/images/add.gif"></img>
								</td></tr><tr><td>
								<!--SPAN ID="remove_<#%=inputField%#>"></SPAN-->
								<img id="button_remove_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" onclick="buttonCallback(B_SUBTRACT,'<%=inputField%>');" src="<%=request.getContextPath()%>/images/remove.gif"></img>
								</td></tr><tr><td>
								<img id="button_moveup_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" src="<%=request.getContextPath()%>/images/moveup.png" alt="Move Up" onclick="moveOptionsUp('<%=inputField%>')" />
								</td></tr><tr><td>
								<img id="button_movedown_<%=inputField%>" onmouseover="this.className='browse_over';" onmouseout="this.className='browse_up';" class="browse_up" src="<%=request.getContextPath()%>/images/movedown.png" alt="Move Down" onclick="moveOptionsDown('<%=inputField%>')" />
								</td></tr></table>
							</td></tr>
							</table>
					<% 		}else if( inputType.equalsIgnoreCase( "views" ) == true ){ %>
							<select ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>">
								<!--option value=""></option-->
								<!--option value="(default)">(default)</option-->
								<viewCfg:EnumViews>
									<option value="<%=viewName%>"><%=viewName%> [<%=viewTitle%>]</option>
								</viewCfg:EnumViews>
							</select>
					<% 		}else if( inputType.startsWith( "enum::" ) == true ){ %>
							<% if( inputTarget.length() > 0 ){	%>
							<select ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" onpropertychange="switchControlType( '<%=inputTarget%>', this.options(this.selectedIndex).className, '<%=viewParam%>' );">
							<% }else{ %>
							<select ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>">
							<% } %>
								<!--option value=""></option-->
								<% String enumName = inputType.substring( 6 ); %>
								<viewCfg:EnumValues enumName="<%=enumName%>">
									<option value="<%=enumCode%>" class="<%=enumType%>"><%=enumText%></option>
								</viewCfg:EnumValues>
							</select>
					<%		} %>
							</div>
							</td>
						</tr>
					<%	}else if( inputVisible.equalsIgnoreCase( "readonly" ) == true ){ %>
							<tr><td nowrap valign="top" align="right" style="font-family:Verdana;font-size:10pt;">
							<%=inputText%>:
							</td><td width="100%">
						<%	if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
							<input type="hidden" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" VALUE="<%=searchParam%>"/>
							<input type="text" READONLY ID="display_<%=inputField%>" NAME="<%=viewParam%>_display_<%=inputField%>" readonly="true"/>
						<%	} else { %>
							<input type="text" READONLY ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" VALUE="<%=searchParam%>"/>
						<%	} %>
							</td></tr>
					<%	}else{ %>
							<tr><td colspan"2">
							<input type="hidden" ID="<%=inputField%>" NAME="<%=viewParam%>_data_<%=inputField%>" VALUE="<%=searchParam%>"/>
						<%	if( inputType.equalsIgnoreCase( "object" ) == true ){ %>
							<input type="hidden" ID="display_<%=inputField%>" NAME="<%=viewParam%>_display_<%=inputField%>" readonly="true"/>
							</td></tr>
						<%	}
						} %>
             	</viewCfg:ViewForm>
				</table>
				<script language="JavaScript">
					viewFilterStr = "<%=filterStr%>";
				</script></td></tr>
			</table>

			<%=hiddenFields%>
			<input type="hidden" id="postView" name="view" value=""/>
			<input type="hidden" id="postReference" name="" value=""/>
			<%	if( request.getParameter("root_view_0") == null ){ %>
			<input type="hidden" name="root_view_0" value="<%=viewParam%>"/>
			<%	}else{ %>
			<input type="hidden" name="root_view_0" value="<%=request.getParameter("root_view_0")%>"/>
			<input type="hidden" name="root_view_<%=Integer.toString(viewIndex)%>" value="<%=viewParam%>"/>
			<%	} %>
			<input type="hidden" id="<%=viewParam%>_searchStr" name="<%=viewParam%>_searchStr" value=""/>

			<viewCfg:ViewLinks viewName="<%=viewParam%>">
				<input type="hidden" id="<%=linkView%>_filter_<%=linkReference%>" name="<%=linkView%>_filter_<%=linkReference%>" value=""/>
				<% String viewSchParam = (String)request.getParameter(linkView+"_searchStr");
					if( viewSchParam == null || viewSchParam.equalsIgnoreCase( "null" ) == true )
					{
						viewSchParam = "";
					}
				%>
				<input type="hidden" id="<%=linkView%>_searchStr" name="<%=linkView%>_searchStr" value="<%=viewSchParam%>"/>
			</viewCfg:ViewLinks>

			<%	String objStart = "0";
				if( request.getParameter( viewParam + "_objstart" ) != null ){
                                                     objStart = request.getParameter( viewParam + "_objstart" );
				} %>
			<input type="hidden" id="<%=viewParam%>_objstart" name="<%=viewParam%>_objstart" value="<%=objStart%>"/>
			<input type="hidden" id="<%=viewParam%>_seltab" name="<%=viewParam%>_seltab" value="0"/>
		</form>
		</td>
	</tr>
</table>
</div>