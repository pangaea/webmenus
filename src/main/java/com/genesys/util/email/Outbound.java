///////////////////////////////////////
// Copyright (c) 2004-2014 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.util.email;

//import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import java.util.Date;

//import javax.activation.*;
import javax.mail.*;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
//import javax.mail.PasswordAuthentication;
import javax.mail.util.ByteArrayDataSource;

import com.genesys.SystemServlet;

public class Outbound
{
	public static void postMail( Vector recipients, String from, String replyTo, boolean bcc, String subject, String textbody, String htmlbody ) throws MessagingException
	{
		Outbound.postMailWithAttachment( recipients, from, replyTo, bcc, subject, textbody, htmlbody, null );
	}
	
	public static void postMailWithAttachment( Vector recipients, String from, String replyTo, boolean bcc, String subject, String textbody, String htmlbody, EmailAttachment attach ) throws MessagingException
	{
		try
		{
			boolean debug = false;
	
			//Set the host smtp address
			Properties props = new Properties();
			String smtpHost = SystemServlet.getGenesysSMTP();
			props.put("mail.smtp.host", smtpHost);
			
			// Auth
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(SystemServlet.getGenesysSMTPUsr(),
									SystemServlet.getGenesysSMTPPwd());
						}
					  });
			
			// create some properties and get the default Session
			//Session session = Session.getDefaultInstance(props, null);
		    session.setDebug(debug);
	
		    // create a message
		    Message msg = new MimeMessage(session);
	
		    // set the from and to address
		    InternetAddress addressFrom = new InternetAddress(from);
		    msg.setFrom(addressFrom);
		    
		    // Set reply-to if supplied
		    if(replyTo != null){
		    	msg.setReplyTo(new javax.mail.Address[]{new javax.mail.internet.InternetAddress(replyTo)});
		    }
	
		    InternetAddress[] addressTo = new InternetAddress[recipients.size()];
		    for (int i = 0; i < recipients.size(); i++)
		    {
	    		addressTo[i] = new InternetAddress((String)recipients.get(i));
		    }
		    msg.setRecipients(Message.RecipientType.TO, addressTo);
		    
		    if( bcc )
		    {
			    InternetAddress[] addressBcc = new InternetAddress[1];
			    addressBcc[0] = new InternetAddress(SystemServlet.getGenesysBccEmail());
			    msg.setRecipients(Message.RecipientType.BCC, addressBcc);
		    }
	
		    // Optional : You can also set your custom headers in the Email if you Want
		    //msg.addHeader("MyHeaderName", "myHeaderValue");
		    
		    ///////////////////////////////////////////////////////////
		    // MimeMultipart content = new MimeMultipart("alternative");
		    
		    // MimeBodyPart text = new MimeBodyPart();
		    // text.setText(textbody);
		    // //text.setHeader("MIME-Version", "1.0");
		    // String textMimeType = text.getContentType();
		    // text.setHeader("Content-Type", textMimeType);
		    // content.addBodyPart(text);
		    
		    // if(htmlbody != null)
		    // {
			//     MimeBodyPart html = new MimeBodyPart();
			//     //html.setContent(arg0, arg1)
			    
			//     html.setContent(htmlbody, "text/html");
			//     //html.setHeader("MIME-Version", "1.0");
			//     //String textHtmlType = html.getContentType();
			//     html.setHeader("Content-Type", "text/html");
			//     content.addBodyPart(html);
		    // }
		    
		    // if(attach != null){
			//     // Add file attachment
			//     MimeBodyPart messageBodyPart = new MimeBodyPart();
		    //     DataHandler handler = new DataHandler(new ByteArrayDataSource(attach.getContent(), attach.getMimeType()));
		    //     messageBodyPart.setDataHandler(handler);
		    //     messageBodyPart.setFileName(attach.getName());
		    //     content.addBodyPart(messageBodyPart);
		    //     /////////////////////////////////////////////
		    // }
	        
		    // msg.setSubject(subject);
		    // msg.setContent(content);
		    // //msg.setHeader("MIME-Version", "1.0");
		    // String mimeType = content.getContentType();
		    // msg.setHeader("Content-Type", mimeType);
		    // msg.setSentDate(new Date());
		    Transport.send(msg);
		    //////////////////////////////////////////////////////////
	
	//	    // Setting the Subject and Content Type
	//	    msg.setSubject(subject);
	//	    //msg.setContent(htmlbody, "text/html");
	//	    msg.setText(textbody);
	//	    Transport.send(msg);
		}
		catch(MessagingException e)
		{
			SystemServlet.g_logger.error( "MessagingException caught trying to post email - " + e.getMessage() );
			try {
				throw new Exception("Error sending email");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}