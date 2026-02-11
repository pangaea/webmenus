package com.genesys.repository.script;

import com.genesys.SystemServlet;
import com.genesys.repository.*;

public class ServerCallback
{
	private ObjectManager m_ObjMan;
	private Credentials m_info;
	private Object m_context = null;

	public ServerCallback( Credentials info, ObjectManager objMan, Object context )
	{
		m_ObjMan = objMan;
		m_info = info;
		m_context = context;
	}
	
	public Credentials getCredentials()
	{
		return m_info;
	}
	
	public Object getContext()
	{
		return m_context;
	}
	
	public ObjectQuery createObjectQuery( String className )
	{
		return new ObjectQuery( className );
	}
	
	public ObjectSubmit createObjectSubmit( String className )
	{
		return new ObjectSubmit( className );
	}
	
	public QueryResponse Query(ObjectQuery query) throws Exception
	{
		try
		{
			return m_ObjMan.Query(m_info, query);
		}
		catch(AuthenticationException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public String QueryXML(ObjectQuery query) throws Exception
	{
		try
		{
			return m_ObjMan.QueryXML(m_info, query);
		}
		catch(AuthenticationException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public String Insert(ObjectSubmit data) throws Exception
	{
		try
		{
			return m_ObjMan.Insert(m_info, data);
		}
		catch(RepositoryException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public boolean Update(String id, ObjectSubmit data) throws Exception
	{
		try
		{
			return m_ObjMan.Update(m_info, id, data);
		}
		catch(AuthenticationException e)
		{
			throw new Exception(e.getErrMsg());
		}
		catch(RepositoryException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public boolean Copy(String id, ObjectSubmit data) throws Exception
	{
		try
		{
			return m_ObjMan.Copy(m_info, id, data);
		}
		catch(AuthenticationException e)
		{
			throw new Exception(e.getErrMsg());
		}
		catch(RepositoryException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public boolean Delete(String className, String id) throws Exception
	{
		try
		{
			return m_ObjMan.Delete(m_info, className, id);
		}
		catch(AuthenticationException e)
		{
			throw new Exception(e.getErrMsg());
		}
	}
	
	public void LogError( String entry )
	{
		SystemServlet.g_logger.error(entry);
	}
	
	public void LogInfo( String entry )
	{
		SystemServlet.g_logger.info(entry);
	}
}