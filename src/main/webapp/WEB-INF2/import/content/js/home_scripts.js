$(function(){
	//$("div.service_box").corner("round 10px").parent().css('padding', '8px').corner("round 10px");
	//$(".join_now_button").corner("round 20px").parent().css('padding', '8px').corner("round 10px");
	//$("#beta_info").fancybox();
	$("#beta_info").fancybox({
		'width' : 800,
		'autoScale' : false,
		'transitionIn' : 'none',
		'transitionOut' : 'none',
		'type' : 'iframe',
		'height': 360
	});
  
//	if( $.Storage.get("show_beta_msg") != "off" ){
//		setTimeout(function(){$("#beta_info").click();}, 1000);
//	}
  
	$('.bxslider').bxSlider({
							randomStart: false,
							speed: 5000,
							auto: true,
							mode: 'fade',
							controls: false,
							pager: false,
							touchEnabled: false
						});
});