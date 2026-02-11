///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.repository.sql;

//import java.nio.LongBuffer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.util.TimeZone;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.*;
import com.genesys.SystemServlet;
import com.genesys.repository.*;
import com.genesys.repository.sql.SQLStatement.StmtType;
import com.genesys.util.LongRef;
import com.genesys.util.xml.*;

//import com.twmacinta.util.*;

public class StatementBuilder
{
	private class StatementParams
	{
		public StatementParams()
		{
			Columns = new StringBuilder();
			Search = new StringBuilder();
			CountOnly = false;
		}
		public StringBuilder Columns;
		public StringBuilder Search;
		public boolean CountOnly;
	}

	private class JoinTable
	{
		public JoinTable()
		{
			Depth = 0;
			LocName = new String("");
			Name = new String("");
			Table = new String("");
			ForKey = new String("");
		}
		public long Depth;
		public String LocName;
		public String Name;
		public String Table;
		public String ForKey;
	}

	/**
	 * Object property types
	 */
	public enum PropType
	{
		DATE,			// Date only
		TIME,			// Time only
		DATETIME,		// Date and time
		INT,			// Number without decimal
		TEXT,			// Character string
		PASSWORD,		// Character string w/MD5
		BOOLEAN,		// True / False
		REAL,			// Number containing decimal
		OBJECT,			// GUID
		LIST,			// Embedded Object List
		INVALID;		// Invalid type
		public static PropType toType(String str)
		{
			try{return valueOf(str.toUpperCase());} 
			catch(Exception ex){return INVALID;}
		}
	}

	/**
	 * Expression operators
	 */
	private enum ExpOp
	{
		EQ,			// [ =  ] equal
		NEQ,		// [ != ] not equal
		LESS,		// [ <  ] less than
		LESSEQ,		// [ <= ] less than or equal
		GREATER,	// [ >  ] greater than
		GREATEREQ,	// [ >= ] greater than or equal
		INVALID;	// Invalid type
		public static ExpOp toType(String str)
		{
			try{return valueOf(str.toUpperCase());} 
			catch(Exception ex){return INVALID;}
		}
	}

	private class SearchExp
	{
		public SearchExp()
		{
			Property = new String("");
			//Operator = new String("eq");
			Operator = ExpOp.EQ;
			Search = new String("");
		}
		public String Property;
		//public String Operator;
		public ExpOp Operator;
		public String Search;
	}
	
	//XMLDocument m_taxonomyCFG;
	HashMap m_classNodes;

	String OID_COL_NAME 		= "id";
	String METADATA_COLS 		= "id, owner, role, author, publisher, created, modified";
	//String sInsertObjList		= "insert into %s (fkey,objid) values (?,?)";

	//const String METADATA_AUTHOR 	= "author"
	//const String METADATA_MODIFIED	= "modified"
	//const String szgetObjList = "select \nT2.id as id, \nT2.%s as display \nfrom \n%s T1 \nleft join %s T2 on T1.objid = T2.id \nwhere T1.fkey='%s'";
	//const String szinsertObjList = "insert into %s (fkey,objid) values ('%s','%s')";
	//const String szdeleteObjList = "delete from %s where fkey='%s'";

	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// ServerClassSQL //////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//public StatementBuilder( XMLDocument taxonomyCFG )
	public StatementBuilder( HashMap classMap )
	{
		//m_taxonomyCFG = taxonomyCFG;
		m_classNodes = classMap;
	}
/*
	private String GetSearchCol( XMLNode classNode, String sSearchParam )
	{
		if( sSearchParam.equalsIgnoreCase("class") == true ||
			sSearchParam.equalsIgnoreCase("id") == true ||
			sSearchParam.equalsIgnoreCase("owner") == true ||
			sSearchParam.equalsIgnoreCase("created") == true ||
			sSearchParam.equalsIgnoreCase("author") == true ||
			sSearchParam.equalsIgnoreCase("modified") == true )
		{
			// Don't bother looking for system properties
			return sSearchParam;
		}
		
		String sColName = sSearchParam;
		boolean bFound = false;
		XMLNodeList properties = classNode.getNodeList("properties/property");
		if( properties.getCount() > 0 )
		{
			XMLNode property = properties.getFirstNode();
			while( property != null )
			{
				String sParam = property.getAttribute("name");
				if( sParam.equalsIgnoreCase(sSearchParam) == true )
				{
					sColName = property.getAttribute("column");
					bFound = true;
					break;
				}
			}
			property = properties.getNextNode();
		}
		
		if( bFound == false )
		{
			// Check for a base class
			XMLNodeList baseclasses = classNode.getNodeList("baseclass");
			if( baseclasses.getCount() > 0 )
			{
				XMLNode baseclass = baseclasses.getFirstNode();
				while( baseclass != null )
				{
					String sBaseClass = baseclass.getAttribute("name");
					XMLNode baseNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + sBaseClass + "']" );
					if( baseNode.isNull() == false )
					{
						sColName = GetSearchCol( classNode, sSearchParam );
					}
					baseclass = baseclasses.getNextNode();
				}
			}
		}
		return sColName;
	}
*/
	private void BuildJoins( TaxonomyClass classNode, LinkedList searchList, long lJoinDepth,
							 LongRef olFunctionLevel, LongRef olTableIdx, StatementParams stmtParam,
							 QueryPropertyList qryPropList,
							 //long lFunctionLevel, long lTableIdx, StatementParams stmtParam,
							 LinkedList joinList, String sJPropName, String sJPropCol, ObjectQuery query )
	{
		// Cludge - to force T1 for parent and second level
		long lLocalFunctionLevel = olFunctionLevel.m_lng;
		//long lLocalFunctionLevel = lFunctionLevel;
		String sTable = classNode.getTable();//.getAttribute("table");
		boolean bEmptyClass = true;	// This value indicates not to increase the joindepth value
		long lJoinDepthNext = olTableIdx.m_lng;
		//long lJoinDepthNext = lTableIdx;

		//long startMillis5 = SystemServlet.enterCodeBlock("properties");
		//XMLNodeList properties = classNode.getNodeList("properties/property");
		//int iCount = properties.getCount();
		//SystemServlet.exitCodeBlock("properties",startMillis5);
		//if( iCount > 0 )
		for( int i = 0; i < classNode.getPropertyCount(); i++ )
		{
			TaxonomyClass.Property property = classNode.getProperty(i);
			bEmptyClass = false;
			olFunctionLevel.m_lng++;
			//lFunctionLevel++;
			//long startMillis15 = SystemServlet.enterCodeBlock("properties.getFirstNode");
			//XMLNode property = properties.getFirstNode();
			//SystemServlet.exitCodeBlock("properties.getFirstNode",startMillis15);
			//while( property != null )
			//{
				// Determine property name
				String param_name;
				if( sJPropName.length() > 0 )
					param_name = sJPropName + "." + property.getName();//.getAttribute("name");
				else
					param_name = property.getName();//.getAttribute("name");

				String propType = property.getType();//.getAttribute("type");
				if( propType.equalsIgnoreCase("list") == true )
				{
					//long startMillis = SystemServlet.enterCodeBlock("BuildJoinsBody3");
					//String sName = classNode.getAttribute("name");
					String sRefTable = property.getRefTable();//.getAttribute("table");
					String sDisplayTable = property.getObjectTable();//.getAttribute("display_table");
					String sDisplayCol = property.getColumn();//.getAttribute("display_col");
					String sFKey = property.getFKey();
					String sObjectRef = property.getObjectRef();
					//String sId = sName + "." + property.getAttribute("column");
					BuildEmbeddedObjList( param_name, sDisplayCol, sRefTable, sDisplayTable, sFKey, sObjectRef, qryPropList );
					//pChildNode->GetAttribute( _T("table"), szTable, MAX_SYSSTRING_LEN );
					//pChildNode->GetAttribute( _T("display_table"), szDisTable, MAX_SYSSTRING_LEN );
					//pChildNode->GetAttribute( _T("display_col"), szDisCol, MAX_SYSSTRING_LEN );
					//BuildEmbeddedObjList( pszID, szDisCol, szTable, szDisTable, stdResponse );
					//SystemServlet.exitCodeBlock("BuildJoinsBody3",startMillis);
				}
				else
				{
					//long startMillis = SystemServlet.enterCodeBlock("BuildJoinsBody2");
					String sName = "";
			
					// Build SELECT clause //////////////
					String sBuf = property.getObjectTable();
					if( sBuf.length() == 0 )
					{
						sBuf = sTable;
					}

					// Find table in join list first
					boolean bFound = false;
					for( int ii = 0; ii < joinList.size(); ii++ )
					{
						JoinTable pjoinTable = (JoinTable)joinList.get(ii);
						if( sBuf.equalsIgnoreCase( pjoinTable.Table ) &&
							( lLocalFunctionLevel == pjoinTable.Depth ) )
						{
							sName = pjoinTable.Name;
							bFound = true;
							break;
						}
					}
					if( bFound == false )
					{
						String sLocTableIdx;
						if( lJoinDepth > 0 )
						{
							Long joinDepth = new Long(lJoinDepth);
							sLocTableIdx = "T" + joinDepth.toString();
						}
						else
						{
							sLocTableIdx = "T1";
						}
						//Long TableIdx = new Long(lTableIdx);
						//String sTableIdx = "T" + TableIdx.toString();
						String sTableIdx = "T" + Long.toString(olTableIdx.m_lng);
						olTableIdx.m_lng++;
						//lTableIdx++;
						sName = sTableIdx;						
						JoinTable joinTable = new JoinTable();
						joinTable.Depth = lLocalFunctionLevel;
						joinTable.LocName = sLocTableIdx;
						joinTable.Name = sName;
						joinTable.Table = sBuf;
						if( sJPropCol != null && sJPropCol.length( ) > 0 )
							joinTable.ForKey = sJPropCol;
						else
							joinTable.ForKey = OID_COL_NAME;
						joinList.add( joinTable );
					}
					
					if(stmtParam.CountOnly == false){
					
						// Add query property to list for iteration later when retrieving results
						PropType qPropType = PropType.toType(propType);
						QueryProperty qProp = new QueryProperty(param_name, qPropType);
						qryPropList.add(qProp);
						
						//if(query.getSortBy().equalsIgnoreCase(property.getColumn())) query.setSortBy(sName + "." + property.getColumn());
						
						// Build query column
						switch(qPropType)
						{
						case DATE:
							stmtParam.Columns.append(",\n" + "date_format(");
							stmtParam.Columns.append(sName + "." + property.getColumn());
							stmtParam.Columns.append(",\"%m/%d/%Y\") AS '" + param_name + "'");
							
							// HACK: datetime fields are converted to strings with the same name and the sortby ends up sorting by string instead of datetime 
							//if(query.getSortBy().equalsIgnoreCase(property.getColumn())) query.setSortBy(sName + "." + property.getColumn());
							break;
		
						case TIME:
							stmtParam.Columns.append(",\n" + "time_format(");
							stmtParam.Columns.append(sName + "." + property.getColumn());
							stmtParam.Columns.append(",\"%h:%i %p\") AS '" + param_name + "'");
							
							// HACK: datetime fields are converted to strings with the same name and the sortby ends up sorting by string instead of datetime 
							//if(query.getSortBy().equalsIgnoreCase(property.getColumn())) query.setSortBy(sName + "." + property.getColumn());
							break;
	
						case DATETIME:
							stmtParam.Columns.append(",\n" + "date_format(");
							stmtParam.Columns.append(sName + "." + property.getColumn());
							stmtParam.Columns.append(",\"%m/%d/%Y %h:%i %p\") AS '" + param_name + "'");
							
							// HACK: datetime fields are converted to strings with the same name and the sortby ends up sorting by string instead of datetime 
							//if(query.getSortBy().equalsIgnoreCase(property.getColumn())) query.setSortBy(sName + "." + property.getColumn());
							break;
	
						case INT:
						case TEXT:
						case BOOLEAN:
						case REAL:
						case PASSWORD:
							stmtParam.Columns.append(",\n" + sName + "." + property.getColumn());
							stmtParam.Columns.append(" AS '" + param_name + "'");
							break;
							
	//					case LIST:
	//						String sObjTable = property.getAttribute("table");
	//						String sDisplayTable = property.getAttribute("display_table");
	//						String sDisplayCol = property.getAttribute("display_col");
	//						String sId = sName + "." + property.getAttribute("column");
	//						BuildEmbeddedObjList( sId, sDisplayCol, sObjTable, sDisplayTable, embObjStmts );
							//pChildNode->GetAttribute( _T("table"), szTable, MAX_SYSSTRING_LEN );
							//pChildNode->GetAttribute( _T("display_table"), szDisTable, MAX_SYSSTRING_LEN );
							//pChildNode->GetAttribute( _T("display_col"), szDisCol, MAX_SYSSTRING_LEN );
							//BuildEmbeddedObjList( pszID, szDisCol, szTable, szDisTable, stdResponse );
	//						break;
	
						case OBJECT:
							stmtParam.Columns.append(",\n" + sName + "." + property.getColumn());
							stmtParam.Columns.append(" AS '" + param_name + "'");
	
							if( sJPropName.length() == 0 )	// Band-aid until I make the object depth level adjustable
							{
								// Adjust param name for linked object reference
								String sPropertyClass = property.getClassName();//.getAttribute("class");
								//XMLNode paramNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + sPropertyClass + "']" );
								//XMLNode paramNode = (XMLNode)m_classNodes.get(sPropertyClass);
								TaxonomyClass paramNode = (TaxonomyClass)m_classNodes.get(sPropertyClass);
								//if( classNode.isNull() == false )
								if( paramNode != null )
								{
									//String sJPropName = property.getAttribute("name");
									//String sJPropCol = property.getAttribute("column");
									BuildJoins( paramNode, searchList, lJoinDepthNext, olFunctionLevel,
												olTableIdx, stmtParam, qryPropList, joinList,
												property.getName(), property.getColumn(), query );
								}
								else
								{
									SystemServlet.g_logger.error( "ERROR: Loading class " + sPropertyClass );
								}
							}
							break;
						}
					}
					//SystemServlet.exitCodeBlock("BuildJoinsBody2",startMillis);
					
					//long startMillis3 = SystemServlet.enterCodeBlock("BuildJoinsBody4");

					// ========== Search Parameter Logic ========== //
					// Add search parameter if it exists
					for( int ii = 0; ii < searchList.size(); ii++ )
					{
						HashMap searchMap = (HashMap)searchList.get(ii);
						SearchExp searchEntry = (SearchExp)searchMap.get(param_name);
						if( searchEntry != null )
						{
							// Append AND if not first
							if( stmtParam.Search.length() > 0 ) stmtParam.Search.append(" AND \n");
							
							// Append left side of expression
							stmtParam.Search.append(sName + "." + property.getColumn());
							
							// Extract the search value into a variable
							String sSearch = searchEntry.Search;
							
							// Append operation
							switch(searchEntry.Operator)
							{
							case EQ:
								if( searchEntry.Search.indexOf("*") < 0 )
								{
									stmtParam.Search.append(" = ");
								}
								else
								{
									// Alter search value to switch wildcard (*) to (%)
									sSearch = sSearch.replace( '*', '%' );
									stmtParam.Search.append(" LIKE ");
								}
								break;
								
							case GREATER:
								stmtParam.Search.append(" > ");
								break;
							case GREATEREQ:
								stmtParam.Search.append(" >= ");
								break;
							case LESS:
								stmtParam.Search.append(" < ");
								break;
							case LESSEQ:
								stmtParam.Search.append(" <= ");
								break;
							case NEQ:
								stmtParam.Search.append(" != ");
								break;
								
							default:
								stmtParam.Search.append(" = ");
								//stmtParam.Search.append(" " + searchEntry.Operator + " ");
								break;
							}
							
							// Append the search value
							switch(PropType.toType(propType))
							{
							case INT:
							//case BOOLEAN:
							case REAL:
								stmtParam.Search.append(sSearch);
								break;
							case DATE:
								stmtParam.Search.append("str_to_date('" + sSearch + "','%m/%d/%Y')");
								break;
							case TIME:
								stmtParam.Search.append("str_to_date('" + sSearch + "','%h:%i %p')");
								break;
							case DATETIME:
								stmtParam.Search.append("str_to_date('" + sSearch + "','%m/%d/%Y %h:%i %p')");
								break;
							default:
								stmtParam.Search.append("'" + sSearch + "'");
							}
						}
					}
					// ============================================ //
					//SystemServlet.exitCodeBlock("BuildJoinsBody4",startMillis3);
				}
				//property = properties.getNextNode();
			//}
		}

		//long startMillis6 = SystemServlet.enterCodeBlock("BuildJoins.classNode.getNodeList");
		// Check for a base class
		//XMLNodeList baseclasses = classNode.getNodeList("baseclass");
		//SystemServlet.exitCodeBlock("BuildJoins.classNode.getNodeList",startMillis6);
		//if( baseclasses.getCount() > 0 )
		for( int i = 0; i < classNode.getBaseclassCount(); i++ )
		{
			String sRefParam = "";
			long lAdjJoinDepth = lJoinDepth;
			if( bEmptyClass == false )
			{
				lAdjJoinDepth = lJoinDepthNext;
			}
			else
			{
				sRefParam = sJPropName;
			}
			//long startMillis9 = SystemServlet.enterCodeBlock("baseclasses");
			//XMLNode baseclass = baseclasses.getFirstNode();
			//while( baseclass != null )
			//{
				//long startMillis7 = SystemServlet.enterCodeBlock("m_taxonomyCFG");
			TaxonomyClass.Baseclass baseclass = classNode.getBaseclass(i);
			String sBaseClass = baseclass.getName();//.getAttribute("name");
				//XMLNode baseNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + sBaseClass + "']" );
			TaxonomyClass baseNode = (TaxonomyClass)m_classNodes.get(sBaseClass);
			if( baseNode != null )
			{
				BuildJoins( baseNode, searchList, lAdjJoinDepth, olFunctionLevel, olTableIdx,
							stmtParam, qryPropList, joinList, sRefParam, null, query );
			}
				//SystemServlet.exitCodeBlock("m_taxonomyCFG",startMillis7);
			//baseclass = baseclasses.getNextNode();
			//}
			//SystemServlet.exitCodeBlock("baseclasses",startMillis9);
		}
	}

	/**
	 * Generate full SQL select, including joins, to represent the class specified
	 */
	public String GenerateSelect( Credentials info, ObjectQuery query, QueryPropertyList qryPropList )
	{
		//long startMillis = SystemServlet.enterCodeBlock("GenerateSelect");

		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + query.getClassName() + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(query.getClassName());
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + query.getClassName() );
		//	return "ERROR";
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(query.getClassName());
		if( classNode == null ) return "ERROR";

		// Setup frame variables
		long lTableIdx = 1;
		String sTableIdx = "T1";

		// Init the stdCols buffer
		StatementParams stmtParams = new StatementParams();
		if(query.getCountOnly()){
			stmtParams.CountOnly = true;
			stmtParams.Columns.append("count(*) AS 'count'");
		}
		else{
			stmtParams.Columns.append(sTableIdx);	// Standard for any obj
			stmtParams.Columns.append(".id AS 'id'");
		}

		String sTable = classNode.getTable();//.getAttribute( "table" );

		// Add T1 table (root) to the join list before we start recursion
		LinkedList joinList = new LinkedList();
		JoinTable rootTable = new JoinTable();
		rootTable.Depth = 1;
		rootTable.LocName = "";
		rootTable.Name = sTableIdx;
		rootTable.Table = sTable;
		joinList.add( rootTable );

		// Build search map
		LinkedList searchList = new LinkedList();
		HashMap searchMap = new HashMap();
		ObjectProperties oSearchProps = query.getProperties();
		for( int i = 0; i < oSearchProps.count(); i++ )
		{
			ObjectProperty oSearchProp = oSearchProps.getAt(i);
			SearchExp seachExp = new SearchExp();
			seachExp.Property = oSearchProp.getName();
			String search = oSearchProp.getText().trim();
			
			// Determine operator
			if( search.startsWith(">=") ){
				seachExp.Operator = ExpOp.GREATEREQ;
				search = search.substring(2);
			}
			else if( search.startsWith("<=") ){
				seachExp.Operator = ExpOp.LESSEQ;
				search = search.substring(2);
			}
			else if( search.startsWith("!=") ){
				seachExp.Operator = ExpOp.NEQ;
				search = search.substring(2);
			}
			else if( search.startsWith(">") ){
				seachExp.Operator = ExpOp.GREATER;
				search = search.substring(1);
			}
			else if( search.startsWith("<") ){
				seachExp.Operator = ExpOp.LESS;
				search = search.substring(1);
			}
			else{
				seachExp.Operator = ExpOp.EQ;
			}
			//seachExp.Operator = "eq";
			seachExp.Search = search;
			searchMap.put( oSearchProp.getName(), seachExp );
		}
		searchList.add( searchMap );

		//////////////////////////////////////////////////////////////////////
		String sParam = "";
		//long lFunctionLevel = 2;
		LongRef olFunctionLevel = new LongRef(2);
		LongRef olTableIdx = new LongRef(lTableIdx);
		//long startMillis2 = SystemServlet.enterCodeBlock("BuildJoins");
		BuildJoins( classNode, searchList, 0, olFunctionLevel, olTableIdx, stmtParams, qryPropList, joinList, sParam, null, query );
		//SystemServlet.exitCodeBlock("BuildJoins",startMillis2);
		/////////////////////////////////////////////////////////////

		StringBuilder sJoins = new StringBuilder();
		//String sJoins = sTable + " T1";
		sJoins.append(sTable + " T1");
		ListIterator iter = joinList.listIterator();
		while ( iter.hasNext() )
		{
			JoinTable pjoinTable = (JoinTable)iter.next();
			
			// Never join to yourself:-)
			String sName = pjoinTable.Name;
			if( sName.equalsIgnoreCase("T1") == false )
			{
				// Join new table
				sJoins.append(" \nLEFT JOIN ");
				sJoins.append(pjoinTable.Table);
				sJoins.append(" ");
				sJoins.append(pjoinTable.Name);
				String sColName = pjoinTable.ForKey;
				//String sColName = GetSearchCol( classNode, pjoinTable.ForKey );
				//if( sColName.length() == 0 ) sColName = "id";
				sJoins.append(" ON " + pjoinTable.LocName + "." + sColName + "=");
				sJoins.append(pjoinTable.Name);
				sJoins.append(".id");
			}
		}

		// Build select SQL statement
		StringBuilder sQuery = new StringBuilder();
		//String sQuery = "SELECT \n\n" + stmtParams.Columns.toString() + " \n\nFROM \n\n" + sJoins.toString();
		sQuery.append("SELECT \n\n" + stmtParams.Columns.toString() + " \n\nFROM \n\n" + sJoins.toString());
		
		//if( stmtParams.Search.length() > 0 ) sQuery.append("\n\nWHERE \n\n" + stmtParams.Search.toString());
		StringBuilder whereClause = new StringBuilder();
		whereClause.append(stmtParams.Search.toString());
		
		if( info.m_bSystemUser == false )
		{
			if( classNode.getSecurity().equalsIgnoreCase("private") == true && query.getRequestLevel().equalsIgnoreCase("2") == false )
			{
				// Add owner restriction to returned objects
				if( whereClause.length() > 0 ) whereClause.append(" \n\nAND ");
				else whereClause.append(" \n\n ");
				String permitAttr = classNode.getPermit();
				if( permitAttr == null || permitAttr.length() == 0 )
					whereClause.append("T1.owner='" + info.m_UserId + "' \n\n");
				else
					whereClause.append("T1." + permitAttr + "='" + info.m_UserId + "' \n\n");
			}
			else if( classNode.getSecurity().equalsIgnoreCase("role") == true && query.getRequestLevel().equalsIgnoreCase("0") == true )
			{
				// TODO: make this query more powerful so it includes all child roles of the user assigned role
				
				// Add role restriction to returned objects
				if( whereClause.length() > 0 ) whereClause.append(" \n\nAND ");
				else whereClause.append(" \n\n ");
				whereClause.append("T1.role='" + info.m_RoleId + "' \n\n");
			}
		}
		
		if( whereClause.length() > 0 ) sQuery.append("\n\nWHERE \n\n" + whereClause.toString());

		// Concatinate order clause if necessary
		String sSortField = query.getSortBy();
		if( sSortField.length() > 0 )
		{
			String orderByStatment = "";
			TaxonomyClass.Property sortField = classNode.getNamedProperty(sSortField);

			// Adjust order by properties based on type
			PropType qPropType = PropType.toType(sortField.getType());
			switch(qPropType)
			{
			case DATE:
				orderByStatment = "DATE(" + query.getSortByPrefix() + "`" + sSortField + "`)";
				break;
	
			case TIME:
			case DATETIME:
				orderByStatment = "TIMESTAMP(" + query.getSortByPrefix() + "`" + sSortField + "`)";
				break;
				
			default:
				orderByStatment = query.getSortByPrefix() + "`" + sSortField + "`";
				break;
			}
		
		
		
		
		
		
			//String sSortField = classNode.getNamedProperty(query.getSortBy()).getColumn();
			//// String sSortOrder = query.getSortOrder();
			//if( sSortField.length() > 0 )
			//{
			String sSortOrder = query.getSortOrder();
			if( sSortOrder.length() > 0 )
				sQuery.append(" \n\nORDER BY " + orderByStatment + " " + sSortOrder);
			else
				sQuery.append(" \n\nORDER BY " + orderByStatment + " ASC");
		}

		// Concatinate range clause if necessary
		long lCount = query.getCount();
		long lStart = query.getStart();
		if( lCount > 0 )
		{
			Long oCount = new Long(lCount);
			if( lStart > 0 )
			{
				Long oStart = new Long(lStart);
				sQuery.append(" \n\nLIMIT " + oStart.toString() + "," + oCount.toString());
			}
			else
			{
				sQuery.append(" \n\nLIMIT " + oCount.toString());
			}
		}
		else if( lStart > 0 )
		{
			Long oStart = new Long(lStart);
			sQuery.append(" \n\nLIMIT " + oStart.toString() + ",8");
		}
		
		//SystemServlet.exitCodeBlock("GenerateSelect",startMillis);
		
		return sQuery.toString();
	}
/*
	const char szgetObjList[] = "\
		select \
		T2.id as id, \
		T2.%s as display \
		from \
		%s T1 \
		left join %s T2 on T1.objid = T2.id \
		where T1.fkey='%s'";
*/
////EMBEDDED OBJECT LIST - BEGIN ////
	int BuildEmbeddedObjList( String sName, String sDisplay, String sRefTable, String sBaseTable, String sFKey, String sObjectRef, QueryPropertyList qryPropList )
	{
		SQLEmbeddedObjStmt embObjStmt = new SQLEmbeddedObjStmt(sName, sDisplay, sRefTable, sBaseTable, sFKey, sObjectRef);
		qryPropList.add(embObjStmt);
		return 0;
	}

	int InsertEmbeddedObj( String sFKey, String sObjID, String sRefTable, SQLStatements stmtList, int index )
	{
		SQLStatement stmtObj = new SQLStatement(sRefTable);
		stmtObj.addParam( new SQLParam("fkey", sFKey, SQLParam.ParamType.TEXT) );
		stmtObj.addParam( new SQLParam("objid", sObjID, SQLParam.ParamType.TEXT) );
		stmtObj.addParam( new SQLParam("objindex", index, SQLParam.ParamType.INTEGER) );
		stmtObj.m_type = StmtType.INSERT;
		stmtList.add( stmtObj );
		return 0;
	}

	int DeleteEmbeddedObjList( String sFKey, String sRefTable, SQLStatements stmtList )
	{
		SQLStatement stmtObj = new SQLStatement(sRefTable);
		stmtObj.addParam( new SQLParam("fkey", sFKey, SQLParam.ParamType.TEXT) );
		stmtObj.m_type = StmtType.DELETE;
		stmtList.add( stmtObj );
		return 0;
	}

	void SaveEmbeddedObjectList( TaxonomyClass.Property propNode, ObjectProperty prop, String sParamName, String sFKey, String sMustIncludeID, SQLStatements stmtList  )
	{
		// Handle special embedded object "list" case here
		String sTable = propNode.getRefTable();//.getAttribute( "table" );
		boolean bAddOwnerRef = false;
		String sOwnerID = new String("");
		if( sMustIncludeID.length() > 0 )
		{
			//String sOwnerRef = propNode.getAttribute( "owner_ref" );
			//if( sOwnerRef.equalsIgnoreCase("yes") )
			if( propNode.isOwnerRef() )
			{
				sOwnerID = sMustIncludeID;
				bAddOwnerRef = true;
			}
			else
			{
				bAddOwnerRef = false;
			}
		}

		// Re-generate new references 
		String sObjListStr = prop.getText();
		String[] sObjList = sObjListStr.split(";");
		DeleteEmbeddedObjList( sFKey, sTable, stmtList );
		for( int i = 0; i < sObjList.length; i++ )
		{
			String sObjID = sObjList[i];//pEmbObj.getValue();
			if( sObjID.length() == 0 ) continue;
			
			// Check for owner in EOL
			if( bAddOwnerRef == true && sOwnerID == sObjID ) bAddOwnerRef = false;
			
			// Insert list objects here
			InsertEmbeddedObj( sFKey, sObjID, sTable, stmtList, i + 1 );
		}
		if( bAddOwnerRef == true )
		{
			InsertEmbeddedObj( sFKey, sOwnerID, sTable, stmtList, 0 );
		}
	}

	private void BuildSubmitStatements( String sOwnerID, String sRoleID, TaxonomyClass pNode,
										String sObjGUID, ObjectSubmit submit, SQLStatements stmtList,
										boolean bInsert ) throws RepositoryException
	{
		String sTable = pNode.getTable();//.getAttribute( "table" );
		//XMLNodeList properties = pNode.getNodeList("properties/property");
		//XMLNode property = properties.getFirstNode();
		//while( property != null )
		for( int i = 0; i < pNode.getPropertyCount(); i++ )
		{
			TaxonomyClass.Property property = pNode.getProperty(i);
			// Iterate properties ////////////////////
			//String sReadonly = property.getAttribute("readonly");
			//if( sReadonly.equalsIgnoreCase("true") == false )
			if( property.isReadOnly() != true )
			{
				String sType = property.getType();//.getAttribute( "type" );
				String sName = property.getName();//.getAttribute( "name" );
				if( sType.equalsIgnoreCase("list"))
				{
					// Handle special embedded object "list" case here
					ObjectProperties dataProps = submit.getProperties();
					ObjectProperty dataProp = dataProps.get(sName);
					if( dataProp != null )
						SaveEmbeddedObjectList( property, dataProp, sName, sObjGUID, "", stmtList );
				}
				else
				{
					// Examine property now
					String sBuf = property.getObjectTable();//.getAttribute( "table" );
					if( sBuf.length() == 0 )
					{
						// Use root table if none is specified
						sBuf = sTable;
					}
	
					// Off base table?
					SQLStatement stmtObj = (SQLStatement)stmtList.get(sBuf);
					if( stmtObj == null )
					{
						stmtObj = new SQLStatement(sBuf);
						stmtObj.addParam( new SQLParam("id", sObjGUID, SQLParam.ParamType.TEXT) );
						stmtList.add( stmtObj );
						
						// Generate metadata for object
						stmtObj.addParam( new SQLParam("owner", sOwnerID, SQLParam.ParamType.TEXT) );
						
						//String orole = submit.getOverrideRole();
						//if( orole == null )
						stmtObj.addParam( new SQLParam("role", sRoleID, SQLParam.ParamType.TEXT) );
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
						dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						String today = dateFormat.format(new Date());
						if(bInsert){
							stmtObj.addParam( new SQLParam("created", today, SQLParam.ParamType.DATETIME) );
						}
						stmtObj.addParam( new SQLParam("modified", today, SQLParam.ParamType.DATETIME) );
						//else
						//	stmtObj.addParam( new SQLParam("role", orole, SQLParam.ParamType.TEXT) );
						//////////////////////////////////////////////
					}
	
					ObjectProperties dataProps = submit.getProperties();
					ObjectProperty dataProp = dataProps.get(sName);
					if( dataProp != null )
					{
						/// *** TODO: Perform validation here and return error to user if invalid or missing data is submitted
						//if( dataProp.getText().compareToIgnoreCase("bob") == 0 ) throw new RepositoryException("Test", RepositoryException.VALIDATION_ERROR);
						if( property.isRequired() && dataProp.getText().length() == 0 ) throw new RepositoryException("Error: Field '" + sName + "' is required.", RepositoryException.VALIDATION_ERROR);
						
						//String sValue = dataProp.getText();
						switch(PropType.toType(sType))
						{
						case DATE:
							//ConvertDate( pszValue );
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getDate(), SQLParam.ParamType.DATE) );
							break;
	
						case TIME:
							//ConvertDateTime( pszValue );
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getTime(), SQLParam.ParamType.TIME) );
							break;
	
						case DATETIME:
							//ConvertDateTime( pszValue );
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getDateTime(), SQLParam.ParamType.DATETIME) );
							break;
	
						case TEXT:
						{
							String val = property.getValidation().trim();
							if( val.length() > 0 )
							{
								// Validate input
								if( dataProp.getText().length() > 0 && Pattern.matches(val, dataProp.getText()) == false )
									throw new RepositoryException("Error: Field '" + sName + "' is invalid.", RepositoryException.VALIDATION_ERROR);
							}
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getText(), SQLParam.ParamType.TEXT) );
							break;
						}
							
						case PASSWORD:
						{
							String val = property.getValidation().trim();
							if( val.length() > 0 )
							{
								// Validate input
								if( dataProp.getText().length() > 0 && Pattern.matches(val, dataProp.getText()) == false )
									throw new RepositoryException("Error: Field '" + sName + "' is invalid.", RepositoryException.VALIDATION_ERROR);
							}
//							String hash = "";
//							try
//							{
//								MD5 md5 = new MD5();
//								md5.Update(dataProp.getText(), null);
//								hash = md5.asHex();
//							}
//							catch(Exception e){}
							
							String passQuery = "SELECT " + property.getColumn() + " as " + property.getColumn() + " FROM " + pNode.getTable() + " WHERE id='" + sObjGUID + "'";
							SQLParam prm = new SQLParam(property.getColumn(), dataProp.getText(), SQLParam.ParamType.PASSWORD);
							prm.setPasswordQuery(passQuery);
							stmtObj.addParam( prm );
							break;
						}
							
						case OBJECT:
							//EscSQLStr( pszValue )
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getText(), SQLParam.ParamType.TEXT) );
							break;
	
						case BOOLEAN:
							String sValue = dataProp.getText();
							if( sValue.equalsIgnoreCase("Y") == true )
								stmtObj.addParam( new SQLParam(property.getColumn(), "Y", SQLParam.ParamType.BOOLEAN) );
							else
								stmtObj.addParam( new SQLParam(property.getColumn(), "N", SQLParam.ParamType.BOOLEAN) );
							break;
	
						case INT:
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getInt(), SQLParam.ParamType.INTEGER) );
							break;
							
						case REAL:
							stmtObj.addParam( new SQLParam(property.getColumn(), dataProp.getReal(), SQLParam.ParamType.REAL) );
							break;
						}
					}
				}
			}
			// Iterate properties ////////////////////
			
			//property = properties.getNextNode();
		}
		//////////////////////////////////////////////////////
		///////////////////////////////////////////////////////
		// Check for a base class
		//XMLNodeList baseclasses = pNode.getNodeList("baseclass");
		//if( baseclasses.getCount() > 0 )
		for( int i = 0; i < pNode.getBaseclassCount(); i++ )
		{
			//XMLNode baseclass = baseclasses.getFirstNode();
			//while( baseclass != null )
			//{
			TaxonomyClass.Baseclass baseclass = pNode.getBaseclass(i);
			String sBaseClass = baseclass.getName();//.getAttribute("name");
			//XMLNode baseNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + sBaseClass + "']" );
			TaxonomyClass baseNode = (TaxonomyClass)m_classNodes.get(sBaseClass);
			//if( baseNode.isNull() == false )
			if( baseNode != null )
			{
				BuildSubmitStatements( sOwnerID, sRoleID, baseNode, sObjGUID, submit, stmtList, bInsert );
			}
			//	baseclass = baseclasses.getNextNode();
			//}
		}
	}

	//public function SubmitObj( $className, $fieldArray, $id )
	public SQLStatements SubmitObj( Credentials info, ObjectSubmit submit, boolean bInsert ) throws RepositoryException
	{
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + submit.getClassName() + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(submit.getClassName());
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + submit.getClassName() );
		//	return null;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(submit.getClassName());
		if( classNode == null ) return null;
		
		String sTable = classNode.getTable();//.getAttribute( "table" );
		ObjectProperties props = submit.getProperties();
		ObjectProperty key = props.get("id");
		if( key == null )
		{
			SystemServlet.g_logger.error( "ERROR: No ID param found on submit of class " + submit.getClassName() );
			return null;
		}
		String sObjGUID = key.getText();
		//if( $id != null )
		//	$sObjGUID = $id;
		//else
		//	$sObjGUID = $this->GetNextAvailGlobalID();

		//$insertList = array();
		//HashMap insertList = new HashMap();
		SQLStatements stmts = new SQLStatements();
		//////////////////////////////////////
		BuildSubmitStatements( info.m_UserId, info.m_RoleId, classNode, sObjGUID, submit, stmts, bInsert );
		return stmts;
		//////////////////////////////////////////////////////////////////
	}

	//private function BuildDeletes( $pNode, $strKey, &$deleteList )
	private void BuildDeletes( TaxonomyClass pNode, String sKey, String sId, SQLStatements deleteList )
	{
		String sTable = pNode.getTable();//.getAttribute( "table" );
	
		// Iterate the properties of the specified class
		///////////////////////////////////////////////////////////
		//XMLNodeList properties = pNode.getNodeList("properties/property");
		for( int i = 0; i < pNode.getPropertyCount(); i++ )
		{
		//if( properties != null )
		//{
		//	XMLNode property = properties.getFirstNode();
		//	while( property != null )
		//	{
			TaxonomyClass.Property property = pNode.getProperty(i);
			String sBuf = property.getObjectTable();//.getAttribute("table");
			if( sBuf.length() == 0 )
			{
				sBuf = sTable;
			}
			
			String sType = property.getType();//.getAttribute("type");
			if( sType.equalsIgnoreCase("list") == true )
			{
				DeleteEmbeddedObjList( sId, sBuf, deleteList );
			}
			else
			{
				SQLStatement stmtObj = (SQLStatement)deleteList.get(sBuf);
				if( stmtObj == null )
				{
					stmtObj = new SQLStatement(sBuf);
					stmtObj.addParam( new SQLParam(sKey, sId, SQLParam.ParamType.TEXT) );
					stmtObj.m_type = StmtType.DELETE;
					deleteList.add( stmtObj );
				}
			}
		//		property = properties.getNextNode();
		//	}
		}
		
		// Iterate ancestors of class
		///////////////////////////////////////////////////////
		// Check for a base class
		//XMLNodeList baseclasses = pNode.getNodeList("baseclass");
		//if( baseclasses.getCount() > 0 )
		for( int i = 0; i < pNode.getBaseclassCount(); i++ )
		{
			//XMLNode baseclass = baseclasses.getFirstNode();
			//while( baseclass != null )
			//{
			TaxonomyClass.Baseclass baseclass = pNode.getBaseclass(i);
			String sBaseClass = baseclass.getName();//.getAttribute("name");
			//XMLNode baseNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + sBaseClass + "']" );
			TaxonomyClass baseNode = (TaxonomyClass)m_classNodes.get(sBaseClass);
			//if( baseNode.isNull() == false )
			if( baseNode != null )
			{
				BuildDeletes( baseNode, sKey, sId, deleteList );
			}
			//	baseclass = baseclasses.getNextNode();
			//}
		}
	}

	public SQLStatements DeleteObj( String className, String objId )
	{
		//XMLNode classNode = m_taxonomyCFG.getSingleNode( "//class[@name='" + className + "']" );
		//XMLNode classNode = (XMLNode)m_classNodes.get(className);
		//if( classNode.isNull() )
		//{
		//	SystemServlet.g_logger.error( "ERROR: Loading class " + className );
		//	return null;
		//}
		TaxonomyClass classNode = (TaxonomyClass)m_classNodes.get(className);
		if( classNode == null ) return null;

		String sTable = classNode.getTable();//.getAttribute( "table" );
		String sIDPath = new String("id");

		//HashMap insertList = new HashMap();
		SQLStatements stmts = new SQLStatements();
		//////////////////////////////////////
		BuildDeletes( classNode, sIDPath, objId, stmts );
		return stmts;
	}
}
