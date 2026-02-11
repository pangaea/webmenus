///////////////////////////////////////
// Copyright (c) 2004-2014 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.util.email;

public class EmailAttachment
{
	String 	_name;
	String 	_mime_type;
	byte[] _content;
	
	public EmailAttachment( String name, String mime_type, byte[] content )
	{
		_name = name;
		_mime_type = mime_type;
		_content = content;
	}
	
	// Public
	public String getName(){ return _name; };
	public String getMimeType(){ return _mime_type; };
	public byte[] getContent(){ return _content; };
}