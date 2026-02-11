function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name, "=");
        if (c_start != -1) {
            c_start = add(add(c_start,c_name.length),1);
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}

function turnOffWelcomePage(val){
  $.ajax({
    type: 'POST',
    url: "../ViewCmd",
    data: add("call=profile&data_show_welcome=",val),
    complete: function(){drawWelcomeButton();},
    dataType: "xml"
  });
}

function drawWelcomeButton(){
  if(getCookie("show_welcome") == "Y"){
    //$("#optout").html("<button onclick='turnOffWelcomePage(\"N\")'>Turn Off Welcome Page</button>");
    $("#optout").html("<input type='checkbox' checked onclick='turnOffWelcomePage(\"N\")'>Show welcome page at start</input>");
  }else{
    //$("#optout").html("<button onclick='turnOffWelcomePage(\"Y\")'>Turn On Welcome Page</button>");
    $("#optout").html("<input type='checkbox' onclick='turnOffWelcomePage(\"Y\")'>Show welcome page at start</input>");
  }
}

$(function(){
  $("#page_links")
    //.append("<li>Welcome to chowMagic</li>")
    //.append("<li><div id='optout'></div></li>")
    .append("<li><button onclick='parent.$.fancybox.close();'>Close</button></li>")
    .append("<li><div id='optout'></div></li>")
  drawWelcomeButton();
  $("div#wrapper").css("width","100%");
  $("#footer").hide().prev().hide();
});


