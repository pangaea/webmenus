var sample_data = '<?xml version="1.0" encoding="UTF-8"?>' +
'<query-results>' +
'<menu id="1" name="Lunch" status="open">' +
	'<category id="1" name="Appetizers">' +
		'<item id="1">' +
			'<name>Wings</name>' +
			'<description>Cajun or Buffalo style served with blue cheese &amp; celery sticks</description>' +
			'<image>/public/Wings.jpg</image>' +
			'<portions>' +
				'<size id="1" price="$6.95">7 wings</size>' +
				'<size id="3" price="$14.95">21 wings</size>' +
			'</portions>' +
			'<options></options>' +
		'</item>' +
	'</category>' +
'</menu>' +
'</query-results>';

function eventHandler( eventid ){
	switch(eventid)
	{
	case 1:
		$("#sample_menu_link").click();
		break;
	}
	return 0;
}

function loadScript(url, callback)
{
    // Adding the script tag to the head as suggested before
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    // Then bind the event to the callback function.
    // There are several events for cross browser compatibility.
    script.onreadystatechange = callback;
    script.onload = callback;

    // Fire the loading
    head.appendChild(script);
}

loadScript("../app/scripts/menus.js", function(){});
loadScript("../xlibs/handlebars/handlebars-v1.3.0.js", function(){
loadScript("../app/scripts/handlebars.helpers.js", function(){});
});

$(function(){
	//$(".form_fields").css("float","left");
	$("<div style='display:none'><div id='sample_menu'><b><em>Sample Menu:</em></b><br/><br/><div id='sample_menu_innner'></div></div></div><a href='#sample_menu' id='sample_menu_link' style='display:none;'></a>").insertAfter(".form_fields");
	$("#sample_menu").css({
		"float": "left",
		"margin": "6px 0 0 10px",
		"width": "600px",
		"height": "200px",
		"padding": "10px 10px 20px 10px",
		"border": "4px solid #DDDDFF"
	});
	
	$("input.Ok").live('click',updateSample);
	$("#Button_Custom1").live('click', updateSample);
	//CodeMirror.on("change", updateSample);
	
	$("#obj_sel_data").bind('object-select',updateSample);
	$("#sample_menu_link").fancybox();
	//$("#Button_Custom1").css({"background": "#ffc1c1", "color": "#ff0000"});
});

function updateSample(){
	template = codemirrorCrtls['template'].getCode();
	updateSampleDisplay(template);
}

function updateSampleDisplay(template, theme){
	var theme_path = "../app/MenuView/sample";
	var type = "post";
	if(typeof(theme) != "undefined"){
		theme_path = "../app/MenuView/theme?id=" + theme;
		type = "get";
	}
	$COMPILED_TEMPLATE = Handlebars.compile(template);
	var oDoc = new XDocument();
	oDoc.create(sample_data);
	var oMenu = oDoc.selectNode("//menu");
	var menuGen = new MenuGenerator();
	$("#sample_menu_innner").html('<style type="text/css" id="dyncss"></style>' + menuGen.createMarkup(oMenu));
	
	$.ajax({
		type: type,
		url: theme_path,
		dataType: "html",
		data: $("#obj_sel_data").serialize(),
		beforeSend: function(){
			$("#sample_menu_innner").hide();
		},
		success: function(data){
			try{
				$("#dyncss").text(data);
			}
			catch(error){//IE fix
				$("#dyncss").get(0).styleSheet.cssText = data;
			}
			
			$("#sample_menu_innner").show();
		}
	});
}

