//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//// MAIN CLIENT OBJECT //////////////////////////
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

/**
 *	method:		ObjectClient
 *	purpose:	Constructor
 */
function ObjectClient()
{
	/**
	 *	Class properties
	 *
	 *	All the properties are treated as statics due to problems
	 *	related to XMLHTTP callback issues. This object is to
	 *	be treated as a singleton.
	 */
	//this.m_http_request;					// request control (ajax)
	//this.m_callback;						// callback function
	 var m_callback;
	 //var m_StatusCode = 0;
	 //var m_responseXML = null;
	 var m_http_request = null;
	
	/**
	 *	Class methods
	 */
	this.setListener = function( callback )
	{
		m_callback = callback;
	}
	this.getXMLResponse = function()
	{
		return m_http_request.responseXML;
		//return m_responseXML;
	}
	this.getStatusCode = function()
	{
		return m_http_request.status;
		//return m_StatusCode;
	}

	/**
	 *	method:		initialize
	 *	purpose:	Create the HHTP request object
	 */
	 this.initialize = function()
	{
		if( window.XMLHttpRequest )			// Mozilla, Safari,...
		{
			m_http_request = new XMLHttpRequest();
			if( m_http_request.overrideMimeType )
			{
				m_http_request.overrideMimeType( "text/xml" );
			}
		}
		else if( window.ActiveXObject )	// IE
		{
			try
			{
				m_http_request = new ActiveXObject( "Msxml2.XMLHTTP" );
			}
			catch(e)
			{
				try
				{
					m_http_request = new ActiveXObject( "Microsoft.XMLHTTP" );
				}
				catch(e)
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		
		m_http_request.onreadystatechange = this.onRequestComplete;

		return true;
	}
	
	/**
	 *	method:		makeRequest
	 *	purpose:	Make HTTP request
	 */
	this.makeRequest = function( url, postdata )
	{
		// Initialize the XMLHTTP control
		if( !this.initialize() )
		{
			alert( "failed to initialize client control" );
			return;
		}
	
		// Make AJAX request
		if( postdata == null )
		{
			m_http_request.open( "GET", url );
			//m_http_request.setrequestheader( "Pragma","no-cache" );
			//m_http_request.setrequestheader( "Cache-control","no-cache" );
			m_http_request.send( null );
		}
		else
		{
			m_http_request.open( "POST", url );
			m_http_request.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
			m_http_request.setRequestHeader( "Content-Length", postdata.length );
			//m_http_request.setrequestheader( "Pragma","no-cache" );
			//m_http_request.setrequestheader( "Cache-control","no-cache" );
			m_http_request.send( postdata );
		}
		
		
		
		
		// jQuery AJAX ///////////////////
		//var sReq = contextPath + "/MenuDesigner/query/menus";
		//sReq += "?" + "loc=" + locationId;
		//sReq += "&r=" + Math.floor( Math.random() * (1000000) );
		//alert(sReq);
		/*if( postdata == null )
		{
			$.ajax({
				type: "GET",
				url: url,
				dataType: "xml",
				success: this.onRequestComplete,
				error: this.processError
			});
		}
		else
		{
			$.ajax({
				type: "POST",
				url: url,
				dataType: "xml",
				data: postdata,
				success: this.onRequestComplete,
				error: this.processError
			});
		}*/
		////////////////////////////////////////////////
		
	
		return true;
	}
	 
	 this.processError = function(XMLHttpRequest, textStatus, errorThrown)
	 {
		 m_StatusCode = XMLHttpRequest.status;
		 m_responseXML = null;
		 m_callback( null );
		 alert(XMLHttpRequest.responseText);
	 }
	
	/**
	 *	method:		onRequestComplete
	 *	purpose:	Recieve HTTP response
	 */
	 this.onRequestComplete = function(data, textStatus, XMLHttpRequest)
	 {
		 //m_StatusCode = XMLHttpRequest.status;
		 //m_responseXML = data;
		 //m_callback( data );
		 
		// Verify that the request is ready
		if(  m_http_request.readyState == 4 )
		{
			// Verify that the request is valid
			if( m_http_request.status == 200 )
			{
				// Extract the XML DOM from the response
				if(	m_http_request.responseXML.xml != null &&			// IE
					m_http_request.responseXML.xml.length == 0 )		// No XML in DOM
				{
					var hdr = m_http_request.responseText.indexOf( "?>" );
					if( hdr >= 0 )
					{
						// IE doesn't seem to handle xml header if they specify an encoding of UTF-8
						// Since we are only supporting acsii right now this means we can remove it
						var adjText = m_http_request.responseText.substr( hdr + 2 );
						m_http_request.responseXML.loadXML( adjText );
					}
					else
					{
						// Initialize the XML DOM based on the text response
						m_http_request.responseXML.loadXML( m_http_request.responseText );
					}
				}
			}
			m_callback( m_http_request.responseXML );
			return true;
		}
		return false;
		// return true;
	}
}
