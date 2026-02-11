///////////////////////////////////////
// Copyright (c) 2004-2012 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.webmenus.scheduler;

import javax.servlet.http.*;

import java.util.*;
import java.text.*;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.apache.struts.action.*;
//import org.apache.commons.beanutils.*;

import com.genesys.util.ServletUtilities;
import com.genesys.SystemServlet;
import com.genesys.repository.*;

public final class ScheduleDesignerForm extends ActionForm
{
	private String _id = null;
	private String _name = null;
	private String _desc = null;
	public String _timeFormat = "hh:mm a";
	private HashMap<String,Date> map = new HashMap<String,Date>();
	public String dayAry[] = { "Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat" };
	
    public ScheduleDesignerForm() {
    	for (String wd : dayAry) {
    		map.put(wd+"StartTime", null);
    		map.put(wd+"EndTime", null);
    	}
    }

	public String getId(){ return _id; }
	public void setId(String id){ _id = id; }

	public String getName(){ return _name; }
	public void setName(String name){ _name = name; }

	public String getDesc(){ return _desc; }
	public void setDesc(String desc){ _desc = desc; }
    
    public String getTimeMapped(String key) {
    	try{
    		DateFormat df = new SimpleDateFormat(_timeFormat);
    		return df.format(map.get(key)).toLowerCase();
    	}
    	catch(Exception e){
    		return null;
    	}
    }
    
    public void setTimeMapped(String key, String value) { 
    	try{
    		DateFormat df = new SimpleDateFormat(_timeFormat);
    		map.put(key, df.parse(value.toLowerCase()));
    	}
    	catch(ParseException e){
    		map.put(key, null);
		}
    }
    
    private void setDayRange(String name, String startTime, String endTime) {
		setTimeMapped(name+"StartTime", startTime);
		setTimeMapped(name+"EndTime", endTime);
    }
    
    private String CalcEndTime(String time, int deltaMins){
    	try{
    		DateFormat df = new SimpleDateFormat(_timeFormat);
    		Date startTime = df.parse(time);
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(startTime);
    		cal.add(Calendar.MINUTE, deltaMins);
    		return df.format(cal.getTime()).toLowerCase();
    	}
    	catch(ParseException e){
    		SystemServlet.g_logger.error( "ParseException thrown - " + e.getMessage() );
		}
    	return "";
    }

	public void reset(ActionMapping mapping, HttpServletRequest request) {	
		
		_id = (String)request.getParameter("id");
		if(_id != null){
			HttpSession thisSession = request.getSession();
			Credentials info = (Credentials)thisSession.getAttribute("info");
			if( info != null){
				try{
					ObjectManager objectBean = SystemServlet.getObjectManager();
					ObjectQuery querySchedule = new ObjectQuery("CCSchedule");
					querySchedule.addProperty("id", _id);
					QueryResponse qrSchedule = objectBean.Query( info, querySchedule );
					RepositoryObjects oSchedules = qrSchedule.getObjects( querySchedule.getClassName() );
					if( oSchedules.count() == 1 ){
						RepositoryObject oSchedule = oSchedules.get(0);
						setName(oSchedule.getPropertyValue("name"));
						setDesc(oSchedule.getPropertyValue("description"));
					}
					
					ObjectQuery queryHours = new ObjectQuery("CCOpHours");
					queryHours.addProperty("schedule", _id);
					RepositoryObjectIterator hoursIter = new RepositoryObjectIterator(objectBean.Query(info, queryHours));
					while(hoursIter.each()){
						RepositoryObject oOpHours = hoursIter.getObj();
						if( oOpHours.getPropertyValue_Boolean("on_sunday") ){
							setTimeMapped("SunStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("SunEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_monday") ){
							setTimeMapped("MonStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("MonEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_tuesday") ){
							setTimeMapped("TueStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("TueEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_wednesday") ){
							setTimeMapped("WedStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("WedEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_thursday") ){
							setTimeMapped("ThrStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("ThrEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_friday") ){
							setTimeMapped("FriStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("FriEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}else if( oOpHours.getPropertyValue_Boolean("on_saturday") ){
							setTimeMapped("SatStartTime", oOpHours.getPropertyValue("start_time"));
							setTimeMapped("SatEndTime", CalcEndTime(oOpHours.getPropertyValue("start_time"), oOpHours.getPropertyValue_Int("minutes")));
						}
					}
				}
				catch(AuthenticationException e){
					SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
				}
			}
		}
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)	{
		ActionErrors errors = new ActionErrors();

		if( null == _name || _name.length() == 0 )
			errors.add("name", new ActionMessage("schedule.error.name.missing"));

		return errors;
	}
}
