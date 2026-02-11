///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.forms;

//import com.datasphere.util.xsl.XSLParser;
import com.genesys.db.DBResultSet;
//import com.genesys.util.xml.XMLDocument;
import com.genesys.util.StringRef;
import com.genesys.SystemServlet;

// JDBC Imports
import java.sql.*;

public class FormsDataBean
{
	Connection m_db;
	
	public FormsDataBean()
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
//			//System.out.println( "jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId+"&password="+password );
//			m_db = DriverManager.getConnection("jdbc:mysql://"+connStr+"/"+dbName+"?user="+userId);//+"&password="+password);
			
			m_db = DriverManager.getConnection(SystemServlet.getDBConnectStr());
		}
		catch (SQLException ex)
		{
			// handle any errors
			System.out.println( "SQLException: " + ex.getMessage() );
			System.out.println( "SQLState: " + ex.getSQLState() );
			System.out.println( "VendorError: " + ex.getErrorCode() );
		}
	}

	/**
	* Free a list
	*
	* @param form				[in]	Resultant forms list
	*/
	//public void FreeList( XMLNodeList list )
	//{
	//}
	
	/**
	* Get a list of forms
	*
	* @param dbase				[in]	Database object
	* @param form_list			[out]	Resultant forms list
	* @returns					Number of forms
	*/
	public int GetFormList()
	{
		return 0;
	}
	
	/**
	* Get the data for a specified form
	*
	* @param form_list			[in]	Form list
	* @param number				[in]	Form number
	* @param id					[out]	Form id
	* @param type				[out]	Form type
	* @param title				[out]	Form title
	* @param questcount			[out]	Form question count
	*/
	public void GetFormDataFromList( int number, StringRef id,
										StringRef type, StringRef title, StringRef questcount )
	{
	}

	/**
	* Get the data for a single form issuance
	*
	* @param id					[in]	Form issuance id
	* @param workgroupid		[out]	Workgroup id
	* @param formid				[out]	Form id
	* @param notes				[out]	notes
	*/
	public void GetFormIssuance( String id, StringRef workgroupid, StringRef formid,
								 StringRef active, StringRef notes )
	{
		try
		{
			String query = "SELECT workgroupid,formid,active,notes FROM workgroups_forms_issuance WHERE id='" + id + "'";
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				workgroupid.m_str = row.getString("workgroupid");
				formid.m_str = row.getString("formid");
				active.m_str = row.getString("active");
				notes.m_str = row.getString("notes");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormIssuance}" );
		}
	}

	/**
	* Get the data for a single form
	*
	* @param dbase				[in]	Database object
	* @param id					[in]	Form id
	* @param title				[out]	Title of form
	* @param type				[out]	Type of form
	* @param questcount			[out]	Form question count
	*/
	public void GetFormData( String id, StringRef concept, StringRef title, StringRef type,
							 StringRef questcount, StringRef parent )
	{
		try
		{
			String query = "SELECT concept,parentid,type,title,questcount FROM forms WHERE id='" + id + "'";
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				concept.m_str = row.getString("concept");
				title.m_str = row.getString("title");
				type.m_str = row.getString("type");
				questcount.m_str = row.getString("questcount");
				parent.m_str = row.getString("parentid");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormData}" );
		}
	}
	
	/**
	* Get the value of a single form
	*
	* @param dbase				[in]	Database object
	* @param id					[in]	Form id
	* @param dollars			[out]	Dollars
	* @param cents				[out]	Cents
	* @param type				[out]	Type
	*/
	public void GetFormValue( String id, StringRef dollars, StringRef cents, StringRef type )
	{
	}
	
	/**
	* Get the  a form category
	*
	* @param dbase				[in]	Database object
	* @param id					[in]	Form id
	* @param category			[in]	Form category
	* @param description		[out]	Dollars
	* @param difficulty			[out]	Cents
	*/
	public void GetFormCategory( String id, int category, StringRef description, StringRef difficulty )
	{
		try
		{
			String category_str = new Integer(category).toString();
			String query = "SELECT formid,category,description,difficulty FROM forms_categories WHERE formid='" + id + "' AND category=" + category_str;
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				description.m_str = row.getString("description");
				difficulty.m_str = row.getString("difficulty");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormCategory}" );
		}
/*
	// Execute query to retrieve value of a form
	$query = "SELECT formid,category,description,difficulty FROM forms_categories WHERE formid='" . $id . "' AND category=" . $category;
	$result = db_execute( $dbase, $query );
	  		
	// Fetch resultant row
	$row = db_fetch_array( $result );
	  		
	// Get form data from row
	$description = trim( $row[2] );//trim( $row[description] );
	$difficulty = $row[3];//$row[difficulty];
*/
	}
	
	/**
	* Get the number of questions for a single form
	*
	* @param dbase				[in]	Database object
	* @param id					[in]	Form id
	* @param num_questions		[out]	Number  of available  questions
	*/
	public void GetFormNumQuestions( String id, StringRef num_questions )
	{
		try
		{
			String query = "SELECT count(*) AS num_questions FROM forms_questions WHERE formid='" + id + "'";
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				num_questions.m_str = row.getString("num_questions");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetFormNumQuestions}" );
		}
/*
		// Execute query to retrieve value of a form
		$query = "SELECT count(*) AS num_questions FROM forms_questions WHERE formid='" . $id . "'";
		$result = db_execute( $dbase, $query );
				
		// Fetch resultant row
		$row = db_fetch_array( $result );
				
		// Get form data from row
		$num_questions = $row[num_questions];
*/
	}
	
	/**
	* Get a list of questions
	*
	* @param dbase				[in]	Database object
	* @param formid				[in]	Form id
	* @param question_list		[out]	Resultant question list
	* @returns					Number of questions on form
	*/
	public int GetQuestionList( String formid )
	{
		return 0;
	}
	
	/**
	* Get the data for a specified question
	*
	* @param question_list		[in]	Question list
	* @param number				[in]	Question number
	* @param type				[out]	Question type
	* @param category			[out]	Question category
	* @param body				[out]	Question body
	*/
	public void GetQuestionDataFromList( int number, StringRef type,
											StringRef category, StringRef difficulty, StringRef body )
	{
	}
	
	/**
	* Get the data for a specified question
	*
	* @param formid				[in]	Form id
	* @param number				[in]	Question number
	* @param type				[out]	Question type
	* @param category			[out]	Question category
	* @param body				[out]	Question body
	*/
	public void GetQuestionData( String formid, int number, StringRef questid, StringRef type,
									StringRef category, StringRef difficulty, StringRef body )
	{
		try
		{
			String number_str = new Integer(number).toString();
			//System.err.println( "SELECT id,formid,number,type,category,difficulty,body FROM forms_questions WHERE formid='" + formid + "' AND number=" + number_str );
			String query = "SELECT id,formid,number,type,category,difficulty,body FROM forms_questions WHERE formid='" + formid + "' AND number=" + number_str;
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				questid.m_str = row.getString("id");
				type.m_str = row.getString("type");
				category.m_str = row.getString("category");
				difficulty.m_str = row.getString("difficulty");
				body.m_str = row.getString("body");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetQuestionData}" );
		}
/*
	// Execute query to retrieve question data
	$query = "SELECT id,formid,number,type,category,difficulty,body FROM forms_questions WHERE formid='" . $formid . "' AND number=" . $number;
	$result = db_execute( $dbase, $query );

	// Fetch resultant row
	$row = db_fetch_array( $result );
	// Get question data from row
	$questid = $row[id];
	$type = $row[type];
	$category = $row[category];
	$difficulty = $row[difficulty];
	$body = trim( $row[body] );
*/
	}
	
	/**
	* Get a list of inputs
	*
	* @param dbase				[in]	Database object
	* @param formid				[in]	Form id
	* @param questnum			[in]	Question number
	* @param input_list			[out]	Resultant input list
	* @returns					Number of inputs on form
	*/
	public DBResultSet GetInputList( String questid )
	{
		try
		{
			//System.err.println( "SELECT count(*) AS num_inputs,questnum,number,display,type,body FROM forms_inputs WHERE questnum='" + questid + "' ORDER BY number ASC" );
			String query = "SELECT questnum,number,display,type,body FROM forms_inputs WHERE questnum='" + questid + "' ORDER BY number ASC";
			//m_inputsStmt.close();
			//m_inputsRows.close();
			DBResultSet dbResults = new DBResultSet(m_db,query);
			////m_inputsStmt = m_db.createStatement();
			////m_inputsRows = m_inputsStmt.executeQuery( query );
			return dbResults;
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetInputList}" );
		}
		return null;
/*
		$query = "SELECT questnum,number,display,type,body FROM forms_inputs WHERE questnum='" . $questnum . "' ORDER BY number ASC";
		$input_list = db_execute( $dbase, $query );
		$count = db_fetch_numrows( $input_list );
		return $count;
*/
	}
	
	/**
	* Get the data for a specified input
	*
	* @param input_list			[in]	Input list
	* @param number				[in]	Input number
	* @param type				[out]	Input type
	* @param body				[out]	Input body
	*/
	public boolean GetInputDataFromList( DBResultSet results, StringRef number, StringRef display, StringRef type, StringRef body )
	{
		try
		{
			//if( m_inputsRows.next() )
			if( results.getNext() )
			{
				number.m_str = results.getStr("number");
				display.m_str = results.getStr("display");
				type.m_str = results.getStr("type");
				body.m_str = results.getStr("body");
				return false;
			}
		
			// Close statement when end is reached
			//m_inputsStmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetInputDataFromList}" );
		}
		return true;
	}
	
	/**
	* Get the data for a specified input
	*
	* @param formid				[in]	Form id
	* @param questnum			[in]	Question number
	* @param number				[in]	Input number
	* @param type				[out]	Input type
	* @param body				[out]	Input body
	*/
	/*public void GetInputData( String questid, int number, StringRef display,
								StringRef type, StringRef body )
	{
		try
		{
			String query = "SELECT questnum,number,display,type,body FROM forms_inputs WHERE questnum='" + questid + "' AND number=" + number;
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				display.m_str = m_inputsRows.getString("display");
				type.m_str = m_inputsRows.getString("type");
				body.m_str = m_inputsRows.getString("body");
			}
		
			// Close statement when end is reached
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetInputData}" );
		}
	}*/

	/**
	* Inserts a users response
	*
	* @param dbase                                [in]        Database object
	* @param userid                                [in]        User ID
	* @param parent                        [in]        User name
	* @param accountnum                        [in]        User account number
	* @param formid                                [in]        Response form
	* @param response_time                [in]        Response time
	* @param type                                [in]        Response type
	*/
	public int AddUsersResponse( String userid, String parent, String accountnum, String formid, String issid, String response_time, int type )
	{
		int iRowId = 0;
		try
		{
			// Building and executing insert query
			String type_str = new Integer(type).toString();
			String query = "INSERT INTO users_forms_responses( userid, parentid, accountnum, formid, issid, response_time, type ) VALUES( '" +
							userid + "','" + parent + "',0,'" + formid + "','" + issid + "','" +
							response_time + "', " + type_str + " )";
			Statement stmt = m_db.createStatement();
			stmt.execute( query );
			ResultSet row = stmt.executeQuery( "SELECT LAST_INSERT_ID() as rowId" );
			if( row.next() )
			{
				String rowId = row.getString("rowId");
				iRowId = Integer.parseInt(rowId);
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {AddUsersResponse}" );
		}
		return iRowId;
	}

	/**
	* Inserts a users question response
	*
	* @param dbase                                [in]        Database object
	* @param responseid                        [in]        Response ID
	* @param responsenum                [in]        Response number
	* @param questnum                        [in]        Question number
	* @param answer                                [in]        Question answer
	*/
	public void AddUsersQuestionResponse( int responseid, int responsenum, String questnum, String answer )
	{
		try
		{
			// Building and executing insert query
			String responseid_str = new Integer(responseid).toString();
			String responsenum_str = new Integer(responsenum).toString();
			String query = "INSERT INTO users_questions_responses( responseid, responsenum, questnum, answer ) VALUES( " +
									responseid_str + "," +
									responsenum_str + "," +
									questnum + ",'" +
									answer + "' )";
			Statement stmt = m_db.createStatement();
			stmt.execute( query );
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {AddUsersQuestionResponse}" );
		}
	}

	public void SetUsersResponseGrade( int responseid, int grade )
	{
		try
		{
			// Building and executing insert query
			String responseid_str = new Integer(responseid).toString();
			String grade_str = new Integer(grade).toString();
			//System.err.println( "grade=" + grade_str );
			String query = "UPDATE users_forms_responses SET grade=" + grade_str + " where id='" + responseid_str + "'";
			Statement stmt = m_db.createStatement();
			stmt.execute( query );
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {SetUsersResponseGrade}" );
		}
	}

	public int GetUsersAverageGrade( String issid )
	{
		int iGrade = 0;
		try
		{
			String query = "SELECT AVG(grade) as grade from users_forms_responses where issid='" + issid + "'";
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				iGrade = row.getInt("grade");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetUsersResponseGrade}" );
		}
		return iGrade;
	}
	
	public void GetResponseCount( String issid, int responsenum, String answer, StringRef sum )
	{
		try
		{
			String responsenum_str = new Integer(responsenum).toString();
			String query = "";
			query += "select count(*) as sum from ";
			query += "users_questions_responses T1 ";
			query += "left join users_forms_responses T2 on T1.responseid = T2.id ";
			query += "where ";
			if( answer != null )
			{
				query += "T1.answer = '" + answer + "' and ";
			}
			query += "T1.responsenum = " + responsenum_str + " and ";
			query += "T2.issid = '" + issid + "'";

			//System.err.println( query );
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				sum.m_str = row.getString("sum");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetResponseCount}" );
		}
	}

	/**
	* Get a users response
	*
	* @param dbase                                [in]        Database object
	* @param responseid                        [in]        Form response id
	* @param userid                                [out]        Resultant user id
	* @param fullname                        [out]        Resultant user name
	* @param formid                                [out]        Resultant form
	* @param response_time                [out]        Responses data/time
	*/
	public void GetUsersResponse( int responseid, StringRef userid, StringRef parent, StringRef accountnum, StringRef formid, StringRef response_time, StringRef res_type )
	{
		try
		{
			String responseid_str = new Integer(responseid).toString();
			String query = "SELECT userid, parentid, accountnum, formid, response_time, type FROM users_forms_responses WHERE id=" + responseid_str;
			//System.err.println( query );
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				userid.m_str = row.getString("userid");
				parent.m_str = row.getString("parentid");
				accountnum.m_str = row.getString("accountnum");
				formid.m_str = row.getString("formid");
				response_time.m_str = row.getDate("response_time").toString();
				res_type.m_str = row.getString("type");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetUsersResponse}" );
		}
	}

	/**
	* Get a list of users responses for specified account
	*
	* @param dbase                                [in]        Database object
	* @param responseid                        [in]        Form response id
	* @param response_list                [out]        Resultant user response list
	* @returns                                        Number of users on account
	*/
	public DBResultSet GetUsersQuestionResponses( int responseid )
	{
		try
		{
			String responseid_str = new Integer(responseid).toString();
			String query = "SELECT questnum, answer FROM users_questions_responses WHERE responseid=" + responseid_str + " ORDER BY responsenum ASC";
			//m_responsesStmt = m_db.createStatement();
			//m_responsesRows = m_responsesStmt.executeQuery( query );
			DBResultSet dbResults = new DBResultSet(m_db,query);
			return dbResults;
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetUsersQuestionResponses}" );
		}
		return null;
	}

	/**
	* Get the data for a specified user responses
	*
	* @param response_list                [in]        Response list
	* @param index                                [in]        Response number
	* @param questnum                        [out]        Response questnum
	* @param answer                                [out]        Response answer
	*/
	public boolean GetUsersQuestionResponseFromList( DBResultSet results, StringRef questnum, StringRef answer )
	{
		try
		{
			if( results.getNext() )
			{
				questnum.m_str = results.getStr("questnum");
				answer.m_str = results.getStr("answer");
				return false;
			}
		
			// Close statement when end is reached
			//m_responsesStmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetUsersQuestionResponseFromList}" );
		}
		return true;
	}

	public void SetUsersPermissions( String userid, String formid, int auth_count )
	{
		try
		{
			// Building and executing insert query
			String formid_str = new Integer(formid).toString();
			String auth_count_str = new Integer(auth_count).toString();
			String query = "select * from users_forms_permissions where userid='" + userid + "' and formid=" + formid_str;
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				if( auth_count <= 0 )
				{
						query = "DELETE FROM users_forms_permissions WHERE userid='" + userid + "' and formid=" + formid_str;
				}
				else
				{
						query = "UPDATE users_forms_permissions SET " +
												"auth_count=" + auth_count_str + " WHERE userid='" + userid + "' and formid=" + formid_str;
				}
			}
			else
			{
					if( !( auth_count <= 0 ) )
					{
							query = "insert into users_forms_permissions( userid, formid, auth_count ) values( '" +
													userid + "'," +
													formid_str + "," +
													auth_count_str + " )";
					}
			}
			stmt.execute( query );
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {SetUsersPermissions}" );
		}
	}

	public void GetUsersPermissions( String userid, String formid, StringRef auth_count )
	{			
		try
		{
			//System.err.println( "SELECT id,formid,number,type,category,difficulty,body FROM forms_questions WHERE formid='" + formid + "' AND number=" + number_str );
			String query = "SELECT auth_count FROM users_forms_permissions WHERE userid='" + userid + "' and formid=" + formid;
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				auth_count.m_str = row.getString("auth_count");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetUsersPermissions}" );
		}
	}
	
	public void GetQuestionParam( String formid, String questnum, String paramName, StringRef paramValue )
	{			
		try
		{
			String query = "SELECT value FROM forms_questions_params WHERE formid='" + formid + "' and questnum='" + questnum + "' and name='" + paramName + "'";
			System.err.println( query );
			Statement stmt = m_db.createStatement();
			ResultSet row = stmt.executeQuery( query );
			if( row.next() )
			{
				paramValue.m_str = row.getString("value");
			}
			stmt.close();
		}
		catch(Exception ex)
		{
			System.err.println( "Exception thrown in {GetQuestionParam}" );
		}
	}
	
	public String StringOut( String inStr )
	{
		String outStr = "";
		if( inStr != null && inStr.length() > 0 )
		{
			outStr = inStr;
		}
		return outStr;
	}
}