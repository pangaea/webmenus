package com.genesys.forms;

import com.genesys.SystemServlet;
import com.genesys.db.DBResultSet;
import com.genesys.util.StringRef;
//import com.genesys.util.xsl.XSLParser;
//import com.genesys.util.xml.XMLDocument;

import java.sql.*;

public class WorkspaceBean
{
	Connection m_db;

	public WorkspaceBean()
	{
		//m_serverCfg = null;
	}

	public void init( String rootPath )
	{
		try
		{
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		 catch (Exception ex)
		 {
             // handle the error
			 System.err.println( "Failed to load drivers" );
         }

		try
		{
//			XMLDocument serverCfg = new XMLDocument();
//			serverCfg.loadXML( rootPath + "cfg/server.cfg" );
//			String connStr = serverCfg.getElementValue( "connectstring" );
//			String dbName = serverCfg.getElementValue( "dbname" );
//			String userId = serverCfg.getElementValue( "userid" );
//			String password = serverCfg.getElementValue( "password" );
//			System.out.println("jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password);
//
//             m_db = DriverManager.getConnection("jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password);
//          
             // Do something with the Connection
             m_db = DriverManager.getConnection(SystemServlet.getDBConnectStr());
          
         
         } catch (SQLException ex) {
             // handle any errors
             System.out.println("SQLException: " + ex.getMessage());
             System.out.println("SQLState: " + ex.getSQLState());
             System.out.println("VendorError: " + ex.getErrorCode());
         }

	}
	
	/**
	* Get a list of forms
	*
	*/
	public DBResultSet GetFormList( String userid, String workgrpid, int concept )
	{
		try
		{
			String concept_str = new Integer(concept).toString();
			String query =
			"select " +
			"T3.id as id," +
			"T3.title as title," +
			"T1.id as issid," +
			"T1.notes as notes " +
			"from " +
			"workgroups_forms_issuance T1 " +
			"left join workgroups_users_ref T2 on T1.workgroupid=T2.workgroupid " +
			"left join forms T3 on T1.formid=T3.id " +
			"where " +
			"T1.workgroupid = '" + workgrpid + "' and " +
			"T1.active = 'Y' and " +
			"T3.status = 'Published' and " +
			"T3.concept = " + concept_str + " and " +
			"T2.userid = '" + userid + "'";
			//m_examsStmt = m_db.createStatement();
			//m_examsRows = m_examsStmt.executeQuery( query );
			DBResultSet dbResults = new DBResultSet(m_db,query);
			return dbResults;
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormList}" );
		}
		return null;
	}
	
	/**
	* Get the data for a specified form
	*
	*/
	public boolean GetFormFromList( DBResultSet results, StringRef id, StringRef title, StringRef issid, StringRef notes )
	{
		try
		{
			if( results.getNext() )
			{
				id.m_str = results.getStr("id");
				title.m_str = results.getStr("title");
				issid.m_str = results.getStr("issid");
				notes.m_str = results.getStr("notes");
				return false;
			}
		
			// Close statement when end is reached
			//m_examsStmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormFromList}" );
		}
		return true;
	}

	/**
	* Get a list of forms
	*
	*/
	public DBResultSet GetStaticFormList( String userid, String workgrpid )
	{
		try
		{
			String query =
			"select " +
			"T1.id as issid," +
			"T1.view as view," +
			"T1.title as title, " +
			"T1.notes as notes " +
			"from " +
			"workgroups_sforms_issuance T1 " +
			"left join workgroups_users_ref T2 on T1.workgroupid=T2.workgroupid " +
			"where  " +
			"T1.workgroupid = '" + workgrpid + "' and " +
			"T1.active = 'Y' and " +
			"T2.userid = '" + userid + "'";

			DBResultSet dbResults = new DBResultSet(m_db,query);
			return dbResults;
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetStaticFormList}" );
		}
		return null;
	}

	/**
	* Get the data for a specified static form
	*
	*/
	public boolean GetStaticFormFromList( DBResultSet results, StringRef view, StringRef issid, StringRef title, StringRef notes )
	{
		try
		{
			if( results.getNext() )
			{
				view.m_str = results.getStr("view");
				issid.m_str = results.getStr("issid");
				title.m_str = results.getStr("title");
				notes.m_str = results.getStr("notes");
				return false;
			}
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetStaticFormFromList}" );
		}
		return true;
	}

	/**
	* Get a list of workspacees
	*
	*/
	public DBResultSet GetWorkspaceList( String userid )
	{
		try
		{
			String query =
			"select " +
			"T1.id as id, " +
			"T1.name as name, " +
			"T1.description as description " +
			"from " +
			"workgroups T1 " +
			"left join workgroups_users_ref T2 on T1.id=T2.workgroupid " +
			"where " +
			"T2.userid = '" + userid + "'";
			//m_surveysStmt = m_db.createStatement();
			//m_surveysRows = m_surveysStmt.executeQuery( query );
			DBResultSet dbResults = new DBResultSet(m_db,query);
			return dbResults;
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetSurveyList}" );
		}
		return null;
	}

	/**
	* Get the data for a specified survey
	*
	*/
	public boolean GetWorkspaceFromList( DBResultSet result, StringRef id, StringRef name, StringRef description )
	{
		try
		{
			if( result.getNext() )
			{
				id.m_str = result.getStr("id");
				name.m_str = result.getStr("name");
				description.m_str = result.getStr("description");
				return false;
			}
		
			// Close statement when end is reached
			//m_surveysStmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetWorkspaceFromList}" );
		}
		return true;
	}
		
	/**
	* Get the data for a specified survey
	*
	*/
	public void GetWorkspace( String id, StringRef name, StringRef description )
	{
		try
		{
			String query =
			"select " +
			"T1.id as id, " +
			"T1.name as name, " +
			"T1.description as description " +
			"from " +
			"workgroups T1 " +
			"where " +
			"T1.id = '" + id + "'";
			
			//System.err.println( query );
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				name.m_str = row.getString("name");
				description.m_str = row.getString("description");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetWorkspace}" );
		}
	}
}