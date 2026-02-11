function SlideDeck(divId, config)
{
	var me = this;
	
	// Set DIV id
	this.divId = divId;
	var myself = document.getElementById(divId);
	myself.style.width = config.deck.width + "px";
	myself.style.height = config.deck.height + "px";

	this.imgarr = new Array();
	this.imgstr = new Array();
	this.linkstr = new Array();
	this.clslinkstr = new Array();

	this.transition = config.deck.transition;
	this.transSec = config.deck.transition_seconds;
/*
	$.each(config.deck.slides, function(i,item)
	{
		var img = new Image();
		img.src = item.src;
		me.imgarr.push(img);
		me.imgstr.push(item.imgstr);
		me.linkstr.push(item.linkstr);
		me.clslinkstr.push(item.clslinkstr);
	});
*/

	this.slideCount = config.deck.slides.length;
	for(var i=0; i < config.deck.slides.length; i++)
	{
		var item = config.deck.slides[i];
		var img = new Image();
		img.src = item.src;
		this.imgarr.push(img);
		this.imgstr.push(item.imgstr);
		this.linkstr.push(item.linkstr);
		this.clslinkstr.push(item.clslinkstr);
	}

	this.fademi = 1;
	this.vcurr = 0;
	this.vnext = 0;
	this.vssdiv = null;
	this.stepc = 20 * (3000 / 1000);
	this.dif = 0.00;
	this.op = 1.00;
	this.dif = (1.00 / this.stepc);
	this.uagent = window.navigator.userAgent.toLowerCase();
	this.IEB = (this.uagent.indexOf('msie') != -1) ? true : false;
	var scompat = document.compatMode;
	if (scompat != "BackCompat") { }
	this.dstr1 = '<div id="';
	this.dstr2 = '" style="position:absolute;text-align:' + "center" + ';width:' + config.deck.width + 'px;height:' + config.deck.height + 'px;visibility:hidden;left:0px;top:0px;padding:0px;margin:0px;overflow:hidden;">';
	this.dstr3 = '<img id="' + this.divId + '_slide1img"';
	this.dstr4 = '" src="';
	this.dstr5 = '" style="position:relative;left:0px;top:0px;padding:0px;margin:0px;border:0px;" alt="" border="0"></img>';
	this.dstr6 = '</div>';
	
	//if( this.IEB == true )
	//{
		myself.style.filter = "progid:DXImageTransform.Microsoft.Fade(Overlap = 1.00, duration = 0.5, enabled = false)"; //Fade transition
		//myself.style.filter[0] = "progid: DXImageTransform.Microsoft.Fade(Overlap = 1.00, duration = 3, enabled = false);";
	//}

	this.random_index = function(min, max)
	{
		var next_idx = (Math.round((max - min) * Math.random() + min));
		while (next_idx == (me.slideCount - 1))
			next_idx = (Math.round((max - min) * Math.random() + min));
		return next_idx;
	}

	this.slide1dotrans = function()
	{
		if (me.IEB == true)
		{
			me.vssdiv.filters[0].apply();
		}
		me.objc = document.getElementById(me.divId + "_slide1d" + me.vcurr);
		me.objn = document.getElementById(me.divId + "_slide1d" + me.vnext);
		me.objc.style.visibility = "hidden";
		me.objn.style.visibility = "visible";
		if (me.IEB == true)
		{
			me.vssdiv.filters[0].play();
		}
		me.vcurr = me.vnext;

		if (me.transition == "random")
		{
			me.vnext = me.random_index(0, me.slideCount - 1);
		}
		else
		{
			me.vnext = me.vnext + 1;
			if (me.vnext >= me.slideCount)
			{
				me.vnext = 0;
			}
		}
		setTimeout(me.slide1dotrans, (me.transSec * 1000));
	}

	this.slide1dotransff = function()
	{
		me.op = me.op - me.dif;
		me.objc = document.getElementById(me.divId + "_slide1d" + me.vcurr);
		me.objn = document.getElementById(me.divId + "_slide1d" + me.vnext);
		if (me.op < (0.00))
		{
			me.op = 0.00;
		}
		me.objc.style.opacity = me.op;
		me.objn.style.opacity = 1.00;
		if (me.op > (0.00))
		{
			setTimeout(me.slide1dotransff, 50);
		}
		else
		{
			me.objc.style.zIndex = 2;
			me.objn.style.zIndex = 3;
			setTimeout(me.slide1beftrans, me.transSec * 1000);
		}
	}

	this.slide1beftrans = function()
	{
		me.vcurr = me.vnext;
		if (me.transition == "random")
		{
			me.vnext = me.random_index(0, me.slideCount - 1);
		}
		else
		{
			me.vnext = me.vnext + 1;
			if (me.vnext >= me.slideCount)
			{
				me.vnext = 0;
			}
		}
		me.op = 1.00;
		me.objc = document.getElementById(me.divId + "_slide1d" + me.vcurr);
		me.objn = document.getElementById(me.divId + "_slide1d" + me.vnext);
		me.objc.style.visibility = "visible";
		me.objn.style.visibility = "visible";
		me.objc.style.zIndex = 3;
		me.objn.style.zIndex = 2;
		me.objc.style.opacity = 1.00;
		me.objn.style.opacity = 1.00;
		me.slide1dotransff();
	}

	this.showInit = function()
	{
		me.vssdiv = document.getElementById(me.divId);
		if (5 > 0)
		{
			if (me.transition == "random")
				me.objc = document.getElementById(me.divId + "_slide1d" + me.random_index(0, me.slideCount - 1));
			else
				me.objc = document.getElementById(me.divId + "_slide1d" + 0);
			me.objc.style.visibility = "visible";
		}
		if (5 > 1)
		{
			if ((me.IEB == true) || (me.fademi == 0))
			{
				me.vcurr = 0;
				me.vnext = 1;
				setTimeout(me.slide1dotrans, 100);
			}
			else
			{
				me.vcurr = 0;
				me.vnext = 0;
				setTimeout(me.slide1beftrans, 100);
			}
		}
	}

	this.startShow = function()
	{
		i = 0;
		var innertxt = "";
		for (i = 0; i < me.slideCount; i++)
		{
			innertxt = innertxt + this.dstr1 + this.divId + "_slide1d" + i + this.dstr2 + this.linkstr[i] + this.dstr3 + i + this.dstr4 + this.imgstr[i] + this.dstr5 + this.clslinkstr[i] + this.dstr6;
		}
		var spage = document.getElementById(this.divId);
		spage.innerHTML = "" + innertxt;
		setTimeout(this.showInit, 200);
	}
}
