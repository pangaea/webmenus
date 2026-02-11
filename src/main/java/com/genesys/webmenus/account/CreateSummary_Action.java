package com.genesys.webmenus.account;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.struts.action.*;

import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.util.xml.*;
import com.genesys.util.xsl.XSLParser;

import com.genesys.util.ServletUtilities;
import com.genesys.util.email.Outbound;
import com.genesys.views.ViewResponseWriter;
import com.genesys.webmenus.MenuBuilder;
import com.genesys.views.InterfaceCfg;

public class CreateSummary_Action extends Action
{
	public void rollback_account( 	ObjectManager objectBean, Credentials info,
									String role_id,	String ref_id, String user_id,
									String acc_id, String loc_id, String theme_id )
	{
		try
		{
			if(loc_id != null) objectBean.Delete(info, "CELocation", loc_id);
			if(acc_id != null) objectBean.Delete(info, "CEAccount", acc_id);
			if(theme_id != null) objectBean.Delete(info, "CETheme", theme_id);
			if(user_id != null) objectBean.Delete(info, "CUser", user_id);
			//if(ref_id != null) objectBean.Delete(info, "CRoleGroupRef", ref_id);
			if(role_id != null) objectBean.Delete(info, "CRole", role_id);
		}
		catch(AuthenticationException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
		catch(RepositoryException ex)
		{
			SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
		}
	}
	
	public ActionForward execute(ActionMapping mapping,
								  ActionForm form,
								  HttpServletRequest request,
								  HttpServletResponse response )
	{
//		CreateSummary_Form rForm = (CreateSummary_Form)form;

		String account_username = ServletUtilities.getCookieValue(request.getCookies(), "account_username", "");
		String account_password = ServletUtilities.getCookieValue(request.getCookies(), "account_password", "");
		String account_accept = ServletUtilities.getCookieValue(request.getCookies(), "account_accept", "");

		String personal_firstName = ServletUtilities.getCookieValue(request.getCookies(), "personal_firstName", "");
		String personal_lastName = ServletUtilities.getCookieValue(request.getCookies(), "personal_lastName", "");
		String personal_address = ServletUtilities.getCookieValue(request.getCookies(), "personal_address", "");
		String personal_city = ServletUtilities.getCookieValue(request.getCookies(), "personal_city", "");
		String personal_state = ServletUtilities.getCookieValue(request.getCookies(), "personal_state", "");
		String personal_email = ServletUtilities.getCookieValue(request.getCookies(), "personal_email", "");
		String personal_phonenum = ServletUtilities.getCookieValue(request.getCookies(), "personal_phoneNum", "");
		
		String restaurant_name = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_name", "");
		String restaurant_address = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_address", "");
		String restaurant_city = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_city", "");
		String restaurant_state = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_state", "");
		String restaurant_zip = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_zip", "");
		String restaurant_timezone = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_timezone", "");
		String restaurant_email = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_email", "");
		String restaurant_phonenum = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_phoneNum", "");
		String restaurant_sample_menus = ServletUtilities.getCookieValue(request.getCookies(), "restaurant_sample_menus", "");

		  ///////////////////////////////////////////////////////////////////////
		 // 	C R E A T E   O B J E C T S   I N   R E P O S I T O R Y       //
		///////////////////////////////////////////////////////////////////////
		ObjectManager m_objectBean = SystemServlet.getObjectManager();
		Credentials info = new Credentials();
		
		String role_id = null;
		String ref_id = null;
		String user_id = null;
		String acc_id = null;
		String loc_id = null;
		String theme_id = null;
		
		//if( m_objectBean.Login( "admin", "admin", info ) == true )
		if( m_objectBean.SystemLogin("guest", info ) == true )
		{
			try
			{
				do
				{
					// Pull account info from cookies
					//String account_username = ServletUtilities.getCookieValue(request.getCookies(), "account_username", "");
					//if( account_username.length() == 0 ) return mapping.findForward("failure");
					//String account_password = ServletUtilities.getCookieValue(request.getCookies(), "account_password", "");
					//if( account_password.length() == 0 ) return mapping.findForward("failure");
					//String account_accept = ServletUtilities.getCookieValue(request.getCookies(), "account_accept", "");
					//if( account_accept.length() == 0 ) return mapping.findForward("failure");
					
					// Create new role specifically for this new account
					//ObjectSubmit account_role = new ObjectSubmit("CRole");
					ObjectSubmit account_role = new ObjectSubmit("CEAccount");
					account_role.addProperty("name", "account_" + account_username);
					account_role.addProperty("description", "Auto-generated account role");
					account_role.addProperty("parent", "");
					account_role.addProperty("admin", false);
					account_role.addProperty("type", "basic");
					account_role.addProperty("accept_license", account_accept.equalsIgnoreCase("yes")?"Y":"N");
					
					CreateSummary_Form rForm = (CreateSummary_Form)form;
					account_role.addProperty("allow_email", rForm.getEmailMe()?"Y":"N");
					
					role_id = m_objectBean.Insert(info, account_role);
					info.m_RoleId = role_id;
					
					// Role Group reference
					//ObjectQuery queryStmt = new ObjectQuery("CGroup");
					//queryStmt.addProperty("name", _username);
					//QueryResponse qResponse = m_objectBean.Query( info, queryStmt );
					//RepositoryObjects objs = qResponse.getObjects(queryStmt.getClassName());
					//if( objs.count() > 0 )
					//	errors.add("username", new ActionMessage("account.error.username.exists"));
					//"account access"
					
					// Create role-group reference
					ObjectSubmit role_access_ref = new ObjectSubmit("CRoleGroupRef");
					role_access_ref.addProperty("role", role_id);
					role_access_ref.addProperty("group", "CC9B0F80-FDEF-918A-F1EE-C7F7263BDD41");	// Pre-defined access group
					ref_id = m_objectBean.Insert(info, role_access_ref);
					
					// Create user
					ObjectSubmit user = new ObjectSubmit("CUser");
					user.addProperty("username", account_username);
					user.addProperty("password", account_password);
					user.addProperty("firstname", personal_firstName);
					user.addProperty("lastname", personal_lastName);
					user.addProperty("address", personal_address);
					user.addProperty("city", personal_city);
					user.addProperty("state", personal_state);
					user.addProperty("emailaddr", personal_email);
					user.addProperty("phonenum", personal_phonenum);
					user.addProperty("role", role_id);
					user.addProperty("show_welcome", true);
					user_id = m_objectBean.Insert(info, user);
					
					// Create Themes /////////////////////////////
					String defaultTemplate = "";
					InterfaceCfg interfaceCfg = SystemServlet.getGenesysInterfaceCfg();
					List<InterfaceCfg.View.Input> theme_inputs = interfaceCfg.getView("themes").getInputs();
					Iterator<InterfaceCfg.View.Input> _iter_inputs = theme_inputs.iterator();
					while( _iter_inputs.hasNext() )
					{
						InterfaceCfg.View.Input theme_input = _iter_inputs.next();
						String fieldName = theme_input.getField();
						if( fieldName.equalsIgnoreCase("template") ){
							defaultTemplate = theme_input.getDefaultVal();
						}
					}

					ObjectSubmit theme = new ObjectSubmit("CETheme");
					theme.addProperty("name", "default");
					theme.addProperty("menuwidth", "100%");
					theme.addProperty("font", "Arial");
					theme.addProperty("font_size", "0");
					theme.addProperty("bkcolor", "cccdfe");
					theme.addProperty("option_text_color", "9799fd");
					theme.addProperty("titlebar_color", "00007f");
					theme.addProperty("cat_text_color", "ffff56");
					theme.addProperty("item_text_color", "bf5f00");
					theme.addProperty("itemdesc_text_color", "000000");
					theme.addProperty("system_text_color", "ff0000");
					theme.addProperty("template", defaultTemplate);
					theme_id = m_objectBean.Insert(info, theme);
					//////////////////////////////////////////
					
					// Location object
					ObjectSubmit location = new ObjectSubmit("CELocation");
					location.addProperty("name", restaurant_name);
					location.addProperty("address", restaurant_address);
					location.addProperty("city", restaurant_city);
					location.addProperty("state", restaurant_state);
					location.addProperty("zip", restaurant_zip);
					location.addProperty("tax", 8.5);
					location.addProperty("timezone", restaurant_timezone);
					location.addProperty("phone_num", restaurant_phonenum);
					location.addProperty("fax_num", "");
					location.addProperty("fax_orders", false);
					location.addProperty("email_addr", restaurant_email);
					location.addProperty("email_orders", true);
					location.addProperty("email_orders_pdf", false);
					location.addProperty("logo", "");
					location.addProperty("template", "menu-top");
					location.addProperty("theme", theme_id);
					location.addProperty("exit_url", "");
					location.addProperty("delivery_avail", false);
					//location.addProperty("role", role_id);
					//location.setOverrideRole(role_id);
					//location.addProperty("role", role_id);
					loc_id = m_objectBean.Insert(info, location);
					
					
					// Add another sample theme
					ObjectSubmit theme2 = new ObjectSubmit("CETheme");
					theme2.addProperty("name", "olive");
					theme2.addProperty("menuwidth", "100%");
					theme2.addProperty("font", "Arial");
					theme2.addProperty("font_size", "0");
					theme2.addProperty("bkcolor", "f5f6b6");
					theme2.addProperty("option_text_color", "d1d345");
					theme2.addProperty("titlebar_color", "191919");
					theme2.addProperty("cat_text_color", "ffffff");
					theme2.addProperty("item_text_color", "bf5f00");
					theme2.addProperty("itemdesc_text_color", "000000");
					theme2.addProperty("system_text_color", "ff0000");
					theme2.addProperty("template", defaultTemplate);
					m_objectBean.Insert(info, theme2);
					
					
					// Add sample schedule
					ObjectSubmit schedule = new ObjectSubmit("CCSchedule");
					schedule.addProperty("name", "Sample Schedule");
					schedule.addProperty("description", "Sunday Hours");
					String scedule_id = m_objectBean.Insert(info, schedule);
					
					ObjectSubmit opHours = new ObjectSubmit("CCOpHours");
					opHours.addProperty("schedule", scedule_id);
					opHours.addProperty("on_sunday", "Y");
					opHours.addProperty("on_monday", "N");
					opHours.addProperty("on_tuesday", "N");
					opHours.addProperty("on_wednesday", "N");
					opHours.addProperty("on_thursday", "N");
					opHours.addProperty("on_friday", "N");
					opHours.addProperty("on_saturday", "N");
					opHours.addProperty("start_time", "09:00 am");
					opHours.addProperty("hours", 0);
					opHours.addProperty("minutes", 480);
					m_objectBean.Insert(info, opHours);
					
					
					// G E N E R A T E   'WELCOME ABOARD'   E M A I L ///////////
					OutputStream outXML = new ByteArrayOutputStream();
					XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outXML, "UTF-8");
					xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
					xmlStreamWriter.writeStartElement("account");								// <account>
					XMLStreamHelper.addTextNode(xmlStreamWriter,"username", account_username);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"firstname", personal_firstName);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"lastname", personal_lastName);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"city", personal_city);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"state", personal_state);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"emailaddr", personal_email);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"phonenum", personal_phonenum);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"type", "basic");
					xmlStreamWriter.writeStartElement("restaurant");							// <restaurant>
					XMLStreamHelper.addTextNode(xmlStreamWriter,"name", restaurant_name);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"city", restaurant_city);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"state", restaurant_state);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"email_addr", restaurant_email);
					XMLStreamHelper.addTextNode(xmlStreamWriter,"phone_num", restaurant_phonenum);
					xmlStreamWriter.writeEndElement();											// </restaurant>
					xmlStreamWriter.writeEndElement();											// </account>
					xmlStreamWriter.writeEndDocument();
					xmlStreamWriter.flush();
					xmlStreamWriter.close();
					String outXml = outXML.toString();
					
					
					
					//ObjectQuery queryStmt = new ObjectQuery("CUser");
					//queryStmt.addProperty("id", user_id);
					//String outXml = m_objectBean.QueryXML( info, queryStmt );
					
					
					
					// Run order XML through XSL transform
					XSLParser xslParser = new XSLParser();
					String rootPath = SystemServlet.getGenesysHome();

					// Generate text email body through XSL
					StringWriter outText = new StringWriter();
					String xslTextUri = rootPath + "templates/webmenus/welcome_text.xsl";
					xslParser.transform( outXml, xslTextUri, outText );
					
					// Generate html email body through XSL
					StringWriter outHtml = new StringWriter();
					String xslHtmlUri = rootPath + "templates/webmenus/welcome_html.xsl";
					xslParser.transform( outXml, xslHtmlUri, outHtml );
//if(false){
					Vector toAddr = new Vector();
					toAddr.add(personal_email);
					//toAddr.add(restaurant_email);
					Outbound.postMail(	toAddr,
										SystemServlet.getGenesysFromEmail(),
										null,
										true,
										"Welcome Aboard from chowMagic",
							 			outText.toString(),
							 			outHtml.toString()
							 		);
//}
					//////////////////////////////////////////////
					/////////////////////////////////////
					
					if( restaurant_sample_menus.equalsIgnoreCase("none") == false )
					{
						// C R E A T E   S A M P L E   M E N U S //
						String rootAppPath = SystemServlet.getGenesysHome();//thisContext.getInitParameter("GENESYS_HOME");
						String importFile = new String(rootAppPath + "WEB-INF/import/menus/" + restaurant_sample_menus);
					
						// Create DOM document
						XMLDocument xmlDoc = new XMLDocument();
						if( xmlDoc.loadXML(importFile) )
						{
							MenuBuilder mb = new MenuBuilder();
							XMLNodeList menuNodes = xmlDoc.getNodeList("//menu");
							for( int i = 0; i < menuNodes.getCount(); i++ )
							{
								XMLNode menuNode = menuNodes.getNodeByIndex(i);
								mb.importMenu(info, loc_id, menuNode, true);
							}
						}
					}
					//////////////////////////////////////////////
					/////////////////////////////////////

					
					// Create account
					//ObjectSubmit account = new ObjectSubmit("CCAccount");
					//account.addProperty("role", role_id);
					//account.addProperty("username", account_username);
					//account.addProperty("password", account_password);
					//account.addProperty("accept_license", account_accept.equalsIgnoreCase("yes")?"Y":"N");
					//String acc_id = m_objectBean.Insert(info, account);
				}
				while(false);
			}
			catch(AuthenticationException ex)
			{
				SystemServlet.g_logger.error( "AuthenticationException thrown - " + ex.getErrMsg() );
			}
			catch(RepositoryException e)
			{
				/// Validation failure message ///
				//ActionForward forward = mapping.findForward("success");
				//ForwardParameters fwdParams = new ForwardParameters();
				//fwdParams.add("msg", e.getErrMsg());
				////fwdParams.add("something", "fornothing");
				//return fwdParams.forward(forward);
				rollback_account(m_objectBean, info, role_id, ref_id, user_id, null, loc_id, theme_id);
				ActionForward forward = mapping.findForward("failure");
				return new ForwardParameters().add("msg", e.getErrMsg()).forward(forward);
			}
			catch( Exception e )
			{
				rollback_account(m_objectBean, info, role_id, ref_id, user_id, null, loc_id, theme_id);
				return mapping.findForward("failure");
			}
			finally
			{
				m_objectBean.Logout(info);
			}
		}
		  ///////////////////////////////////////////////////////////////
		 ///////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////

		return mapping.findForward("success");
	}
}