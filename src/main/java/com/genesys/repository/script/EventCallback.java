package com.genesys.repository.script;

public class EventCallback
{
	private String m_sClassName, m_sEventName, m_ErrMsg;
	private StringBuilder m_sResponse;
	private boolean m_abortReq = false;
	public EventCallback( String sClassName, String sEventName )
	{
		m_sClassName = new String(sClassName);
		m_sEventName = new String(sEventName);
		m_sResponse = new StringBuilder();
	}
	//public void setAbortRequest(boolean bAbort){ m_abortReq = bAbort; }
	//public boolean getAbortRequest(){ return m_abortReq; }
	
	public boolean isAborted(){ return m_abortReq; }
	public void setAbort(String msg){ m_abortReq = true; m_ErrMsg = msg; }
	
	public String getClassName(){ return m_sClassName; };
	public String getEvent(){ return m_sEventName; };
	public String getErrorMessage(){ return m_ErrMsg; };
	public void write( String output )
	{
		m_sResponse.append(output);
	}
	public void writeln( String output )
	{
		m_sResponse.append(output);
		m_sResponse.append("\r\n");
	}
	public String getOutput()
	{
		return m_sResponse.toString();
	}
}