package com.genesys.repository;

import java.util.Vector;
import java.util.*;

public class Credentials
{
	public String 	m_UserName;
	public String	m_UserId;
	public String 	m_RoleId;
	public String 	m_sTicket;
	public String 	m_sAccess;
	public boolean 	m_bAdmin;
	public Vector	m_AccessList;
	public boolean m_bSystemUser;
	public Date m_prevLogin;
	public boolean m_showWelcome;
	
	public String getRole(){ return m_RoleId; };
	
	public Credentials()
	{
		m_UserName = new String("");
		m_UserId = new String("");
		m_RoleId = new String("");
		m_sTicket = new String("");
		m_sAccess = new String("");
		m_bAdmin = false;
		m_AccessList = new Vector();
		m_bSystemUser = false;
		m_prevLogin = null;
		m_showWelcome = false;
	}
}
