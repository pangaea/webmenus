var $COMPILED_TEMPLATE = null;

function MenuGenerator()
{
	this.getChildValue = function(obj, name)
	{
		var oProp = obj.selectNodeList(name).getNode(0);
		if( oProp != null ) return oProp.getText();
		else return "";
	}
	this.currencyFormatted = function(amount)
	{
		amount = amount.replace("$", "");
		var i = parseFloat(amount);
		if( i == 0.00 ) return "";
		if(isNaN(i)) { i = 0.00; }
		var minus = '';
		if(i < 0) { minus = '-'; }
		i = Math.abs(i);
		i = parseInt((i + .005) * 100);
		i = i / 100;
		s = new String(i);
		if(s.indexOf('.') < 0) { s += '.00'; }
		if(s.indexOf('.') == (s.length - 2)) { s += '0'; }
		s = minus + s;
		return s;
	}
	this.createMarkup = function(oMenu, catId, designMode)
	{
		if( designMode == null ) designMode = false;
		if( oMenu.isNull() == true ) return;
		var path = document.location.pathname;
		var pathParts = path.split("/");
		var rootPath = "/" + pathParts[1];
		
		// Build params from response
		var params = {};
		var menuStatus = oMenu.getAttribute("status");
		params['name'] = oMenu.getAttribute("name");
		params['menuStatus'] = menuStatus;
		
		var oCatList;
		if( catId == null )
			oCatList = oMenu.selectNodeList("category");
		else
			oCatList = oMenu.selectNodeList("category[@id='" + catId + "']");
		params['categories'] = [];
		for( var i = 0; i < oCatList.getLength(); i++ )
		{
			var oCat = oCatList.getNode(i);
			
			var oList = oCat.selectNodeList("item");
			var items = [];
			for( var ii = 0; ii < oList.getLength(); ii++ )
			{
				var oNode = oList.getNode(ii);
				var sName = this.getChildValue(oNode,"name");
				var desc_c = this.getChildValue(oNode,"description").replace(/\n/g, "<br/>");
				var imgTag = "", imgRaw = "";
				
				// Inserting image if supplied as part of the item
				var image = this.getChildValue(oNode,"image");
				if( image != null &&
					( image.indexOf(".gif") >= 0 ||
					  image.indexOf(".jpeg") >= 0 ||
					  image.indexOf(".jpg") >= 0 ||
					  image.indexOf(".png") >= 0 ) )
					imgTag = "<img width=\"100px\" height=\"75px\" src='" + rootPath + "/ImageViewer" + image + "'/>";
					imgRaw = rootPath + "/ImageViewer" + image;
				//////////////////////////////////////////////////////////////
				
				var oAltSizes = oNode.selectNodeList("portions/size");
				var sizes = [];
				for( var iii = 0; iii < oAltSizes.getLength(); iii++ )
				{
					var oAltSize = oAltSizes.getNode(iii);
					var sSizeDesc = oAltSize.getText();
					var sizePrice = oAltSize.getAttribute("price");
					
					sizes.push({	id: oAltSize.getAttribute("id"),
									size: sSizeDesc,
									price: "$" + this.currencyFormatted(sizePrice),
									name: sName.replace(/'/g, "&#39;")
							});
				}
				var oOptions = oNode.selectNodeList("options/option");
				var options = [];
				for( var iii = 0; iii < oOptions.getLength(); iii++ )
				{
					var oOption = oOptions.getNode(iii);
					var optName = oOption.getAttribute("name");
					var optPrice = oOption.getAttribute("price");
					var optType = oOption.getAttribute("type");
					var optDisplay = optName;
					
					var p = this.currencyFormatted(optPrice);
					if( p.length > 0 ){
						if( optType == "select" ){
							optDisplay = optName + " ($" + p + " ea)";
						}
						else if( optType == "select-one" ){
							optDisplay = optName + " (choose one - $" + p + ")";
						}
					}
					
					var choices = [];
					var optText = trimStr(oOption.getText());
					var results = optText.split( "\n" );
					for( var iiii = 0; iiii < results.length; iiii++ )
					{
						var sOptionTxt = trimStr(results[iiii]);
						if( sOptionTxt.length == 0 ) continue;
						choices.push({text: sOptionTxt, display: sOptionTxt + " ($" + p + " ea)"});
					}
					
					options.push({	name: optName,
									price: optPrice,
									display: optDisplay,
									type: optType,
									choices: choices});
				}
				
				items.push({	name: sName,
								description: desc_c,
								image: imgTag,
								imageurl: imgRaw,
								sizes: sizes,
								options: options
						});
			}
				

			
			params['categories'].push({name: oCat.getAttribute("name"), items: items});
		}

//		var theHTMLString = [];
//		dust.render("menu", params, function(err, out) {
//			theHTMLString.push(out);
//		});
//		return theHTMLString.join('');
		return $COMPILED_TEMPLATE(params);
	}
}