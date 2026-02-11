///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.session;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

//import java.io.File;
//import java.util.StringTokenizer;

//import org.apache.xerces.parsers.DOMParser;
//import org.apache.xpath.XPath;
//import org.apache.xpath.XPathAPI;

/*import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;*/

public class ClientSessionBean
{
	// Public properties
	//public ObjectManager m_glbObjMan = null;
	public Credentials m_info = null;

	// Private properties
	//private Document m_cfgDoc;
	//private String m_sRootPath = "";

	public ClientSessionBean()
	{
		//m_cfgDoc = null;
		//String rootPath = SystemServlet.getGenesysHome();//System.getProperty("GENESYS_HOME");
		//init( rootPath );
	}
	
	protected void finalize()
	{
		//if( m_info != null )
		//{
		//	ObjectManager objectBean = SystemServlet.getObjectManager();
		//	objectBean.Logout(m_info);
		//}
	}

	//public Document getDocument()
	//{
	////	return SystemServlet.getGenesysInterface().getDocument();
	//}

	// 0-none, 1-read only, 2-read/write
	public int getAccessRights( String AccessRights, String ViewName )
	{
		boolean bForceDeny = false;
		int iAccRights = 0, iDefRights = 0;
		String sAccessStr = AccessRights;
		String sAccess;
		//String sAccessArray[] = sAccessStr.split( "|" );
		int iStartIdx = 0;
		int iIdx = sAccessStr.indexOf( "|", iStartIdx );
		while( iIdx >= 0 )
		{
			sAccess = sAccessStr.substring( iStartIdx, iIdx );
			iStartIdx = iIdx + 1;
			/////////////////
			//for( int i = 0; i < sAccessArray.length; i++ )
			if( sAccess.length() > 0 )
			{
				//sAccess = sAccessArray[i];
				int iIndex = sAccess.indexOf( ":" );
				if( iIndex >= 0 )
				{
					String sView = sAccess.substring( 0, iIndex );
					String sRights = sAccess.substring( iIndex + 1 );
					if( sView.equalsIgnoreCase( ViewName ) == true )
					{
						if( sRights.equalsIgnoreCase( "read/write" ) == true )
						{
							iAccRights = 2;
						}
						else if( sRights.equalsIgnoreCase( "read only" ) == true )
						{
							iAccRights = 1;
						}
						else if( sRights.equalsIgnoreCase( "denied" ) == true )
						{
							bForceDeny = true;
						}
					}
					else if( sView.equalsIgnoreCase( "(default)" ) == true )
					{
						if( sRights.equalsIgnoreCase( "read/write" ) == true )
						{
							iDefRights = 2;
						}
						else if( sRights.equalsIgnoreCase( "read only" ) == true )
						{
							iDefRights = 1;
						}
						//else if( sRights.equalsIgnoreCase( "denied" ) == true )
						//{
						//	iDefRights = 0;
						//}
					}
				}
			}
			/////////////////////
			////////////////
			iIdx = sAccessStr.indexOf( "|", iStartIdx );
		}
		if( iAccRights == 0 && bForceDeny == false ) iAccRights = iDefRights;
		return iAccRights;
	}
}
