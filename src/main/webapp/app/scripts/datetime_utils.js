function getTzDate(timeZone) {
  const now = new Date();
  
  // Get target timezone string in a fixed ISO format
  const tzString = now.toLocaleString("en-US", { timeZone, hour12: false });
  
  // Format local time into the exact same format
  const localString = now.toLocaleString("en-US", { hour12: false });
  
  // Calculate the difference in milliseconds between the two strings
  const diff = Date.parse(tzString) - Date.parse(localString);
  
  // Return a new Date object shifted by that difference
  return new Date(now.getTime() + diff);
}

function convertDatetimeToString(datetime) {
    const yyyy = datetime.getFullYear();
    const mm = datetime.getMonth() + 1;
    const dd = datetime.getDate();
    var hh;
    var ampm = "AM";
    if (datetime.getHours() == 0) {
        hh = "12";
    } else if (datetime.getHours() > 12) {
        hh = datetime.getHours() - 12;
        ampm = "PM";
    } else if (datetime.getHours() == 12) {
        hh = "12";
        ampm = "PM";
    } else {
        hh = datetime.getHours();
    }
    const MM = datetime.getMinutes()
    return `${mm}/${dd}/${yyyy} ${hh}:${MM} ${ampm}`;
}

function getCurDate() {
    const d = new Date();
    d.setMinutes(d.getMinutes() + 1);
    return d;
}

function adjustDatetimeUp(a) {
    const cur = (a.length > 0) ? convertStringToDate(a) : getCurDate();
    cur.setMinutes(cur.getMinutes() - 1 + 5);
    return convertDatetimeToString(cur);
}

function adjustDatetimeDown(a) {
    const cur = (a.length > 0) ? convertStringToDate(a) : getCurDate();
    cur.setMinutes(cur.getMinutes() - 1 - 5);
    return convertDatetimeToString(cur);
}

function convertStringToDate(date) {
    // Format e.g... 5/18/2026 11:00 PM
    var arr = date.match(/(\d+)\/(\d+)\/(\d+) (\d+)\:(\d+) (.+)/);
    if (arr == null || arr.length < 7) {
        return null;
    }
    var hours = +arr[4];
    if (arr[6].toUpperCase() == "PM" && hours < 12) {
        hours += 12;
    } else if (arr[6].toUpperCase() == "AM" && hours == 12) {
        hours = 0;
    }
    return new Date(arr[3], arr[1] - 1, arr[2], hours, +arr[5] + 1);
}

function convertDateToLabel(a) {
    return convertToLabel(timeDiffInMinutes(a));
}

function timeDiffInMinutes(a) {
    var estimated_time = convertStringToDate(a);
    if (estimated_time != null) {
        const localISOTime = getTzDate("America/New_York");
        const diffMs = estimated_time - localISOTime;
        return Math.floor(diffMs / (1000 * 60));
    } else {
        return null;
    }
}

function convertToLabel(delta_minutes) {
    if (delta_minutes == null) {
        return "None";
    }
    const delta_abs = Math.abs(delta_minutes);
    if (delta_abs > 60) {
        var hrs = Math.ceil(Math.abs(delta_abs / 60));
        var min = delta_abs % 60;
        if (delta_minutes >= 0) {
            return hrs + "hrs " + min + "min"
        } else {
            return "-" + hrs + "hrs " + min + "min"
        }
    } else {
        return delta_minutes + " minutes";
    }
}