/*
 * Globals
 */
var g_designMode = true;

var g_curSelNode = null;
var g_curSelObjId = null;
var g_localRepos = new Object();
var g_menusXML = new XDocument();

var menus_image = "books.png";
var menu_image = "book--pencil.png";
var cat_image = "document-text.png";
var item_image = "block.png";
var option_image = "question-white.png";
var delete_image = "cross.png";

function initPage()
{
	myLayout = $('body').layout({

	//	enable showOverflow on west-pane so popups will overlap north pane
		west__showOverflowOnHover: false

	//	reference only - these options are NOT required because are already the 'default'
	,	closable:				true	// pane can open & close
	,	resizable:				true	// when open, pane can be resized 
	,	slidable:				true	// when closed, pane can 'slide' open over other panes - closes on mouse-out

	//	some resizing/toggling settings
	,	north__resizable:		false
	,	north__closable:		false
	,	north__slidable:		false	// OVERRIDE the pane-default of 'slidable=true'
	,	north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
	,	north__spacing_closed:	20		// big resizer-bar when open (zero height)
	,	south__resizable:		false	// OVERRIDE the pane-default of 'resizable=true'
	,	south__spacing_open:	0		// no resizer-bar when open (zero height)
	,	south__spacing_closed:	20		// big resizer-bar when open (zero height)
	//	some pane-size settings
	,	west__minSize:			100
	,	west__size:				250
	,	east__size:				300
	,	east__minSize:			200
	,	east__maxSize:			Math.floor(screen.availWidth / 2) // 1/2 screen width
	});

	// add event to the 'Close' button in the East pane dynamically...
	//myLayout.addCloseBtn('#btnCloseEast', 'east');

	// add event to the 'Toggle South' buttons in Center AND South panes dynamically...
	//myLayout.addToggleBtn('.south-toggler', 'south');

	// add MULTIPLE events to the 'Open All Panes' button in the Center pane dynamically...
	///myLayout.addOpenBtn('#openAllPanes', 'north');
	//myLayout.addOpenBtn('#openAllPanes', 'south');
	//myLayout.addOpenBtn('#openAllPanes', 'west');
	//myLayout.addOpenBtn('#openAllPanes', 'east');
	$("#menusTree").tree({
		rules : {
			use_max_children : false,
			use_max_depth : true,
			clickable : true,
			type_attr : "type"
		},
		"all" : {
			draggable : false
		},
		callback : {
			onchange : function (NODE, TREE_OBJ) {
				var id = $(NODE).children("a:eq(0)").attr("id");
				var type = $(NODE).children("a:eq(0)").attr("type");
				g_curSelNode = $(NODE);
				g_curSelObjId = id;
				showDetails(id, type);
			},
			beforemove : function (NODE, REF_NODE, TYPE, TREE_OBJ) {
				return false;
			}
		}
	});

	var xmlMarkup = parent.compileLocation(locationId);
	//alert(xmlMarkup);
	g_menusXML.create(xmlMarkup);
	drawMenuTree(g_menusXML.getDocument());
}

function showDetails(id, type)
{
	var menuGen = new MenuGenerator();
	var previewCode;
	
	if( type == "menu" )
	{
		var oMenu = g_menusXML.selectNode("//menu[@id='" + id + "']");
		previewCode = menuGen.createMarkup(oMenu, null, true);
	}
	else if( type == "category" )
	{
		var oCat = g_menusXML.selectNode("//menu/category[@id='" + id + "']");
		var oMenu = oCat.selectNode("..");
		previewCode = menuGen.createMarkup(oMenu, id, true);
	}

	$("#menuPanel").html(previewCode);
}

function drawMenuTree(xml)
{
	var firstMenu = null;
	
	try
	{
		var jsonString = new Array(1000);
		
		/*
		 * Build menu tree
		 */
		jsonString.push('[{data:{title:"Menus",icon:"'+contextPath+'/app/images/' + menus_image + '",attributes:{ id:"menus_ID",type:"root"}},children:[');
		$(xml).find("location").find("menu").each(function(i) {
			if( i > 0 ) jsonString.push(',');
			else firstMenu = $(this).attr("id");
			//var menu_image;
			//if($(this).children("hidden").text() == "Y") menu_image = "hidden_menu2.png";
			jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + menu_image + '",attributes:{id:"' + $(this).attr("id") + '",type:"menu"}},children:[');
			//addToLocalRepository($(this), "menu");
			$(this).find("category").each(function(ii) {
				if( ii > 0 ) jsonString.push(',');
				//var cat_image;
				//if($(this).children("hidden").text() == "Y") cat_image = "hidden_category.png";
				jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + cat_image + '",attributes:{id:"' + $(this).attr("id") + '",type:"category"}},children:[');
//				addToLocalRepository($(this), "category");
//				$(this).find("item").each(function(iii) {
//					if( iii > 0 ) jsonString.push(',');
					//var item_image;
					//if($(this).children("hidden").text() == "Y") item_image = "hidden_item1.png";
//					jsonString.push('{data:{title:"'+ $(this).children("name").text() + '",icon:"' + contextPath + '/app/images/' + item_image + '",attributes:{id:"' + $(this).attr("id") + '",type:"item"}},children:[');
//					addToLocalRepository($(this), "item");
//					$(this).find("options").find("option").each(function(iiii) {
//						if( iiii > 0 ) jsonString.push(',');
						//var item_image;
						//if($(this).children("hidden").text() == "Y") item_image = "hidden_item1.png";
//						jsonString.push('{data:{title:"'+ $(this).attr("name") + '",icon:"' + contextPath + '/app/images/' + option_image + '",attributes:{id:"' + $(this).attr("id") + '",type:"option"}}}');
//						addToLocalRepository($(this), "option");
//					});
//					jsonString.push(']}');
//				});
				jsonString.push(']}');
			});
			jsonString.push(']}');
		});
		jsonString.push(']}]');
		
		//alert(jsonString.join(""));
		//clipboardData.setData("text", jsonString.join(""));
		var jsonMenus = eval(jsonString.join(""));
		$.tree.focused().settings.data.type = "json";
		$.tree.focused().settings.data.opts = {static:jsonMenus};
		$.tree.focused().refresh();
		$.tree.focused().open_all();
		
		//var selnode = $.tree.focused().get_node("237D12B2-B050-4E2A-F9CC-2293EC459EAF");
		//$.tree.focused().select_branch(selnode);
		showDetails(firstMenu, "menu");
	}
	catch(e)
	{
		alert(e);
	}
}
