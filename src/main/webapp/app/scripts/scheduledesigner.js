function timeToMinutes(time_str){
	var d = new Date();
	var time = time_str.match(/(\d+)(?::(\d\d))?\s*([pP]?)/);
	var hours = parseInt(time[1], 10);
	var minutes = parseInt(time[2], 10);
	var pm = (time[3].length == 0) ? false : true;
	if(pm == false && hours == 12){
		hours = 0;
	}else if(pm == true && hours < 12){
		hours += 12;
	}
	return (hours * 60) + minutes;
}
function drawSchedule(){
	var days = ["sun", "mon", "tue", "wed", "thr", "fri", "sat"];
	$(days).each(function(i){
		day = days[i];
		var startTime = $(".start_time_"+day).val();
		var endTime = $(".end_time_"+day).val();
		if( startTime.length > 0 && startTime != $(".start_time_"+day).attr("title") &&
			endTime.length > 0 && endTime != $(".end_time_"+day).attr("title")){
			var start = timeToMinutes(startTime);
			var end = timeToMinutes(endTime);
			var topPX = (start / 5);
			if(end>start){
				$("#display_"+day).css({
					marginTop: topPX + "px",
					height: ((end-start)/5) + "px"
				}).show();
				$("#display2_"+day).hide();
			}else{
				$("#display2_"+day).css({
					marginTop: "0px",
					height: (end/5) + "px"
				}).show();
				$("#display_"+day).css({
					marginTop: topPX + "px",
					height: (((24*60)-start)/5) + "px"
				}).show();
			}
		}else{
			$("#display_"+day).hide();
			$("#display2_"+day).hide();
		}
	});
}