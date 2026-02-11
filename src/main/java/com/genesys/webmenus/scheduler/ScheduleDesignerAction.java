package com.genesys.webmenus.scheduler;

//import java.net.URLEncoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.*;
import java.util.*;
import java.text.*;
import org.apache.struts.action.*;

import com.genesys.SystemServlet;
import com.genesys.repository.AuthenticationException;
import com.genesys.repository.Credentials;
import com.genesys.repository.ObjectManager;
import com.genesys.repository.ObjectQuery;
import com.genesys.repository.ObjectSubmit;
import com.genesys.repository.RepositoryException;
import com.genesys.repository.RepositoryObject;
import com.genesys.repository.RepositoryObjectIterator;
import com.genesys.webmenus.account.ForwardParameters;

public class ScheduleDesignerAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response ){
		ScheduleDesignerForm rForm = (ScheduleDesignerForm)form;

		  ///////////////////////////////////////////////////////////////////////
		 // 	C R E A T E   O B J E C T S   I N   R E P O S I T O R Y       //
		///////////////////////////////////////////////////////////////////////
		
		HttpSession thisSession = request.getSession();
		Credentials info = (Credentials)thisSession.getAttribute( "info" );
		if( info != null){
			try{
				ObjectManager objectBean = SystemServlet.getObjectManager();
				ObjectSubmit schedule = new ObjectSubmit("CCSchedule");
				schedule.addProperty("name", rForm.getName());
				schedule.addProperty("description", rForm.getDesc());
				if(rForm.getId() == null || rForm.getId().length() == 0){
					rForm.setId(objectBean.Insert(info, schedule));
				}else{
					objectBean.Update(info, rForm.getId(), schedule);
					clearAllHoursOfOperations(objectBean, info, rForm.getId());
				}
				generateHoursOfOperations(objectBean, info, rForm);
			}
			catch(AuthenticationException e){
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
			}
			catch(RepositoryException e){
				SystemServlet.g_logger.error( "RepositoryException thrown - " + e.getErrMsg() );
				//ActionForward forward = mapping.findForward("failure");
				//return new ForwardParameters().add("msg", e.getErrMsg()).forward(forward);
			}
		}
		ActionForward forward = mapping.findForward("success");
		return new ForwardParameters().add("id", rForm.getId()).forward(forward);
	}
	
	private void clearAllHoursOfOperations(ObjectManager objectBean, Credentials info, String scheduleId){
		try{
			ObjectQuery queryHours = new ObjectQuery("CCOpHours");
			queryHours.addProperty("schedule", scheduleId);
			RepositoryObjectIterator hoursIter = new RepositoryObjectIterator(objectBean.Query(info, queryHours));
			while(hoursIter.each()){
				RepositoryObject oOpHours = hoursIter.getObj();
				objectBean.Delete(info, "CCOpHours", oOpHours.getId());
			}
		}
		catch(AuthenticationException e){
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
		}
		catch(RepositoryException e){
			SystemServlet.g_logger.error( "RepositoryException thrown - " + e.getErrMsg() );
		}
	}
	
	private boolean present(String param){
		return ( param != null && param.length() > 0 );
	}
	
	private void setWeekday(ObjectSubmit opHours, int weekIdx){
		boolean on_sunday, on_monday, on_tuesday, on_wednesday, on_thursday, on_friday, on_saturday;
		on_sunday = on_monday = on_tuesday = on_wednesday = on_thursday = on_friday = on_saturday = false;
		switch(weekIdx){
		case 0:	on_sunday = true;		break;
		case 1:	on_monday = true;		break;
		case 2:	on_tuesday = true; 		break;
		case 3:	on_wednesday = true; 	break;
		case 4:	on_thursday = true; 	break;
		case 5:	on_friday = true;		break;
		case 6:	on_saturday = true; 	break;
		}
		opHours.addProperty("on_sunday", on_sunday);
		opHours.addProperty("on_monday", on_monday);
		opHours.addProperty("on_tuesday", on_tuesday);
		opHours.addProperty("on_wednesday", on_wednesday);
		opHours.addProperty("on_thursday", on_thursday);
		opHours.addProperty("on_friday", on_friday);
		opHours.addProperty("on_saturday", on_saturday);
	}
	
	private void generateHoursOfOperations(ObjectManager objectBean, Credentials info, ScheduleDesignerForm rForm){
		
		try{
			
			int dayIdx = 0;
			DateFormat df = new SimpleDateFormat(rForm._timeFormat);
			for (String wd : rForm.dayAry) {
				
				String t = rForm.getTimeMapped(wd+"StartTime");
				String t2 = rForm.getTimeMapped(wd+"EndTime");
				if( present(t) && present(t2) ){
					try{
						ObjectSubmit opHours = new ObjectSubmit("CCOpHours");
						opHours.addProperty("schedule", rForm.getId());
						setWeekday(opHours, dayIdx);
						opHours.addProperty("start_time", t);
						opHours.addProperty("hours", 0);

						long startTime = df.parse(t).getTime();
						long endTime = df.parse(t2).getTime();
						int deltaMins = 0;
						if(endTime > startTime){
							deltaMins = (int)( (endTime - startTime) / 60000 );
						}else{
							deltaMins = (int)( 1440 + ((endTime - startTime) / 60000) );
						}
						opHours.addProperty("minutes", deltaMins);
						objectBean.Insert(info, opHours);
					}
					catch(ParseException e){
						SystemServlet.g_logger.error( "ParseException thrown - " + e.getMessage() );
					}
				}
				dayIdx++;
			}
		}
		catch(AuthenticationException e){
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + e.getErrMsg() );
		}
		catch(RepositoryException e){
			SystemServlet.g_logger.error( "RepositoryException thrown - " + e.getErrMsg() );
		}
	}
}
