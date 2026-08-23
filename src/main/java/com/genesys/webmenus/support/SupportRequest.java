package com.genesys.webmenus.support;

import java.io.IOException;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.genesys.SystemServlet;
import com.genesys.util.email.Outbound;

public class SupportRequest extends HttpServlet {
    public void init() throws ServletException {

    }

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String resPath = request.getPathInfo();
		if (resPath == null)
		{
			Handle_Question(request, response);
		}
		else
		{
			if( resPath.equalsIgnoreCase("/question") )
			{
				Handle_Question(request, response);
			}
        }
    }

	public void Handle_Question(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String resturantName = request.getParameter("restaurant");
        String topic = request.getParameter("topic");
        String priority = request.getParameter("priority");
        String subject = request.getParameter("subject");
        String question = request.getParameter("message");
        StringBuffer sb = new StringBuffer();
        sb.append("Name: " + name + "\n");
        sb.append("Resturant Name: " + resturantName + "\n");
        sb.append("Topic: " + topic + "\n");
        sb.append("Priority: " + priority + "\n");
        sb.append("Subject: " + subject + "\n\n");
        sb.append(question);

        Vector<String> toAddr = new Vector<String>();
        String systemFromEmail = SystemServlet.getGenesysFromEmail();
        toAddr.add(systemFromEmail);
        try {
            Outbound.postMail(toAddr, systemFromEmail, email, true, subject, sb.toString(), null);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.sendRedirect("/");
    }
}
