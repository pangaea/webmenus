$(function(){
  $("div#wrapper").css("width","100%");
  $("#footer").hide().prev().hide();
  
  if( $.Storage.get("show_beta_msg") == "off" ){
    $("#hide_popup").attr("checked", true);
  }
  
  $("#close_dialog").click(function(){
    parent.$.fancybox.close();
  });
  
  $("#hide_popup").click(function(){
    if( $("#hide_popup").attr("checked") == false )
      $.Storage.set("show_beta_msg", "on");
    else if( $("#hide_popup").attr("checked") == true )
      $.Storage.set("show_beta_msg", "off");
  });
});


