<%@ include file="auth.jsp" %>
<%@ page import="javax.servlet.*"%>

<!DOCTYPE html>

<%
	String queryStr = "";
	String parentProp = (String)request.getParameter("parentProp");
	if( request.getParameter("parentProp") != null )
		queryStr += "parentProp=" + parentProp + "&";
%>

<html>
<head>
	<title>Genesys - Image Library Frame</title>
	
		<!-- JQuery : Core -->
		<%@ include file="jquery_includes.jsp"%>

		<script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/main.css"></link>
		<script language="JavaScript" src="<%=request.getContextPath()%>/includes/common.js"></script>
		
		<link href="<%=request.getContextPath()%>/xlibs/facefiles/facebox.css" media="screen" rel="stylesheet" type="text/css" />
		<script src="<%=request.getContextPath()%>/xlibs/facefiles/facebox.js" type="text/javascript"></script>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/image_library.css"></link>
		
	<script type="text/javascript">
		var $tabs = null;
		var selObj = null;
		$(function(){
			<% if(request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("myimage")){ %>
			disableControls(false);
			<% }else{ %>
			disableControls(true);
			<% } %>
			$('a[rel*=facebox]').facebox();
			$(document).bind('show.facebox', function() {
				var title = $(selObj).attr("title");
				$('#facebox .footer_text').html( "<input type='text' value='" + title + "'/><a style='font-size:10pt;float:left;' target='_blank' href='<%=request.getContextPath()%>/ImageViewer/" + title + "'>View in new tab</a>"  );
			});
		});

		function disableControls(disable)
		{
			if(disable){
				<% if(request.getSession().getAttribute("admin")!="Y"){	%>
				$(".admin-only").hide();
				<% } %>
			}else{
				$(".admin-only").show();
			}
		}
		function SelectImageByTitle(title)
		{
			selObj = $("img[title='"+title+"']").get(0);
		}
		function buttonCallback( item )
		{
			//var $tabs = $('#tabs').tabs();
			var selected = $tabs.tabs('option', 'selected');
			var libType = (selected==0) ? "image" : "myimage";
			switch( item )
			{
				case B_SELECT:
				case B_DELETE:
					if( selObj == null )
					{
						msgBoxAlert("Please select an image", "No Selection");
						break;
					}
					
					var imgPath = selObj.src.indexOf("/ImageViewer/");
					var imgName = selObj.src.substr(imgPath+12);
					if( item == B_SELECT )
					{
<%	if( parentProp != null && parentProp.length() > 0 ){%>
						var elem;
						var elem_img;
						var doc_img;
						if( $("#fancybox-frame", parent.document).contents().find("#DIV_image_designer").size() > 0 ){
							elem = $("#fancybox-frame", parent.document).contents().find("#image");
							elem_img = $("#fancybox-frame", parent.document).contents().find("#image_img");
							doc_img = parent.document;
						}
						else{
							elem = $("#<%=request.getParameter("parentProp")%>", parent.document);//.val(imgName);
							elem_img = $("#<%=request.getParameter("parentProp")%>_img", parent.document);
							doc_img = parent.document;
						}
						elem.val(imgName);
						elem_img.attr("src", "<%=request.getContextPath()%>/ImageViewer" + imgName);
						elem_img.show();
						//if( doc_img != null && typeof(doc_img.reAllSizeImages) == "function" ) doc_img.reAllSizeImages();
<%	} else { %>
							msgBoxAlert("Invalid Operation");
<% 	} %>
						// Let the request fall through to the close routine
					}
					else
					{
						messageBox("Are you sure you want to delete the image '" + $(selObj).attr("title") + "'?", "Delete Image",
						{"Yes": function(){
							document.location.href = "<%=request.getContextPath()%>/ImageManager?op=del&type=" + libType + "&name=" + imgName + "&ret=" + escape("<%=request.getQueryString()%>&type=" + libType);
							$(this).dialog("close");
						}, "No": function(){$(this).dialog("close");}});
						break;
					}
					

				case B_CANCEL:
<%	if( parentProp != null && parentProp.length() > 0 ){%>
					parent.closeDialog();
<%	}else{ %>
					parent.$.fancybox.close();
<% } %>
					break;

				case B_UPLOADIMAGE:
					if( $("#file").attr("value").length == 0 )
					{
						msgBoxAlert("Please select an image to upload", "No Selection");
						break;
					}
					$("#imageUpload").attr("action", "<%=request.getContextPath()%>/ImageManager?type=" + libType + "&ret=" + escape("<%=request.getQueryString()%>&type=" + libType));
					$("#imageUpload").submit();
					$("#reset").trigger('click');
					break;
			}
		}
	</script>
	
	<style type="text/css">
	.admin-only{
		display: none;
	}
	</style>

</head>
<body id="image_body" style="height:100%;">
	<table width="100%">
	<tr>
		<td align="left" style="height:25px;" valign="top">
		<table id="toolbar"><tr><td style="padding-left:10px;" valign="top">
			<span ID="cancelButton" style="width:20px;"></span>
			<script type="text/javascript">
			$("#cancelButton").html(buttonDraw( B_CANCEL, "", "Close", 'buttonCallback' ));
			</script>
		</td><td style="padding-left:30px;" valign="top">
			<span ID="uploadButton" class="admin-only" style="width:20px;"></span>
			<script type="text/javascript">
			$("#uploadButton").html(buttonDraw( B_UPLOADIMAGE, "", "Upload", 'buttonCallback', "Button_Upload" ));
			</script>
		</td><td valign="top">
			<form method="post" enctype="multipart/form-data" name="imageUpload" id="imageUpload" action="#" class="admin-only">
				<input type="file" name="file" id="file" style="width:350px;"/>
				<input type="reset" id="reset" style="display:none;" />
			</form>
			<script type="text/javascript">
			$("#imageUpload").attr("action", "<%=request.getContextPath()%>/ImageManager?type=<%=request.getParameter("type")%>&ret=" + escape("<%=request.getQueryString()%>"));
			</script>
		</td></tr></table>
		</td>
	</tr>
	</table>
	<div id="tabs">
		<ul>
			<li><a href="#public_library">Public Library</a></li>
			<li><a href="#personal_library">Personal Library</a></li>
		</ul>
		<div id="public_library" style="background:#FFFFFF;">
			<jsp:include page="image_library_tab.jsp" >
   				<jsp:param name="type" value="image" />
   				<jsp:param name="parentProp" value='<%=parentProp!=null ? parentProp : ""%>'/>
			</jsp:include>
		</div>
		<div id="personal_library" style="background:#FFFFFF;">
			<jsp:include page="image_library_tab.jsp" >
   				<jsp:param name="type" value="myimage" />
   				<jsp:param name="parentProp" value='<%=parentProp!=null ? parentProp : ""%>'/>
			</jsp:include>
		</div>
		<div style="clear:both;"></div>
	</div>
</body>
<script type="text/javascript">
//$(function(){
	$tabs = $('#tabs').tabs( {
		// which tab to start on when page loads
        selected: <%=(request.getParameter("type") != null && request.getParameter("type").equalsIgnoreCase("myimage")) ? 1 : 0%>,
        fxAutoHeight: true,
        select: function(e, ui) {
            if(ui.index > 0) disableControls(false);
            else disableControls(true);
            return true;
        }
	});
//});
</script>
</html>