package com.genesys.repository;

import java.util.*;

import com.genesys.util.xml.*;

public class TaxonomyClass
{
	public class Property
	{	
		private String name;
		private String type;
		private String className;
		private String column;
		private boolean readOnly;
		private boolean required;
		private String validate;
		
		private boolean ownerRef;
		private String refTable;
		private String objectTable;
		private String fKey;
		private String objectRef;
		
		public Property( XMLNode property )
		{
			init(property);
		}
		
		private void init( XMLNode propNode )
		{
			this.name = propNode.getAttribute("name");
			this.type = propNode.getAttribute("type");
			this.column = propNode.getAttribute("column");
			
			String table = propNode.getAttribute("table");
			if( table != null ) this.objectTable = table;
			
			// Extract object property class name
			if( this.type.equalsIgnoreCase("object") == true )
				this.className = propNode.getAttribute("class");
			else
				this.className = new String("");
			
			// Extract list property info
			if( this.type.equalsIgnoreCase("list") == true )
			{
				this.refTable = propNode.getAttribute("ref_table");
				String owner_ref = propNode.getAttribute("owner_ref");
				if( owner_ref != null && owner_ref.equalsIgnoreCase("true") == true ) this.ownerRef = true;
				else this.ownerRef = false;
				this.fKey = propNode.getAttribute("fkey");
				if( this.fKey == null || this.fKey.length() == 0 ) this.fKey = new String("fkey");
				this.objectRef = propNode.getAttribute("object_ref");
				if( this.objectRef == null ) this.objectRef = new String("objid");
				//this.objectTable = property.getAttribute("obj_table");
			}
			else
			{
				this.refTable = new String("");
				this.ownerRef = false;
				this.objectTable = new String("");
				this.fKey = new String("");
				this.objectRef = new String("");
			}

			// Check for read-only property
			String read_only = propNode.getAttribute("readonly");
			if( read_only != null && read_only.equalsIgnoreCase("true") == true ) this.readOnly = true;
			else this.readOnly = false;
			
			// Check for required property
			String required = propNode.getAttribute("required");
			if( required != null && required.equalsIgnoreCase("true") == true ) this.required = true;
			else this.required = false;
			
			//Check for validation regex
			this.validate = propNode.getValue();
		}

		// Public interface
		public String getName(){ return this.name; };
		public String getType(){ return this.type; };
		public String getClassName(){ return this.className; };
		public String getColumn(){ return this.column; };
		public String getRefTable(){ return this.refTable; };
		public boolean isOwnerRef(){ return this.ownerRef; };
		public String getObjectTable(){ return this.objectTable; };
		public boolean isReadOnly(){ return this.readOnly; };
		public boolean isRequired(){ return this.required; };
		public String getValidation(){ return this.validate; };
		public String getFKey(){ return this.fKey; };
		public String getObjectRef(){ return this.objectRef; };
	}

	public class Baseclass
	{
		private String name;
		
		// Public interface
		public Baseclass( String name )
		{
			this.name = name;
		}
		public String getName(){ return this.name; };
	}

	public class Reference
	{
		private String className;
		private String fkey;
		
		// Public interface
		public Reference( String className, String fkey )
		{
			this.className = className;
			this.fkey = fkey;
		}
		public String getClassName(){ return this.className; };
		public String getFKey(){ return this.fkey; };
	}

	
	// Script Events
	private String preSelect = null;
	private String postSelect = null;
	private String preInsert = null;
	private String postInsert = null;
	private String preUpdate = null;
	private String postUpdate = null;
	private String preDelete = null;
	private String postDelete = null;
	/////////////////////////////////
	
	
	
	private String name;
	private String abstractType;
	private String table;
	private String security;
	private String permit;
	private Vector m_properties;
	private HashMap m_propertiesMap;
	private Vector m_baseclasses;
	private Vector m_references;
	
	// Public interface
	public TaxonomyClass()
	{
	}

	public TaxonomyClass( XMLNode classNode )
	{
		init( classNode );
	}
	
	public String getEventScript(String eventName)
	{
		if(eventName.equalsIgnoreCase("preSelect"))	return this.preSelect;
		else if(eventName.equalsIgnoreCase("postSelect")) return this.postSelect;
		else if(eventName.equalsIgnoreCase("preInsert")) return this.preInsert;
		else if(eventName.equalsIgnoreCase("postInsert")) return this.postInsert;
		else if(eventName.equalsIgnoreCase("preUpdate")) return this.preUpdate;
		else if(eventName.equalsIgnoreCase("postUpdate")) return this.postUpdate;
		else if(eventName.equalsIgnoreCase("preDelete")) return this.preDelete;
		else if(eventName.equalsIgnoreCase("postDelete")) return this.postDelete;
		return null;
	}
	
	public void init( XMLNode classNode )
	{
		// Load Script Events
		this.preSelect = classNode.getAttribute("preSelect");
		this.postSelect = classNode.getAttribute("postSelect");
		this.preInsert = classNode.getAttribute("preInsert");
		this.postInsert = classNode.getAttribute("postInsert");
		this.preUpdate = classNode.getAttribute("preUpdate");
		this.postUpdate = classNode.getAttribute("postUpdate");
		this.preDelete = classNode.getAttribute("preDelete");
		this.postDelete = classNode.getAttribute("postDelete");
		
		
		// Initialize the collection objects
		m_properties = new Vector();
		m_propertiesMap = new HashMap();
		m_baseclasses = new Vector();
		m_references = new Vector();
		
		// Extract class information
		this.name = classNode.getAttribute("name");
		this.abstractType = classNode.getAttribute("abstract");
		this.table = classNode.getAttribute("table");
		this.security = classNode.getAttribute("security");
		this.permit = classNode.getAttribute("permit");
		
		// Extract properties
		XMLNodeList properties = classNode.getNodeList("properties/property");
		if( properties.getCount() > 0 )
		{
			XMLNode property = properties.getFirstNode();
			while( property != null )
			{
				Property obj = new Property( property );
				m_properties.add( obj );										// Used for fast iteration
				m_propertiesMap.put( property.getAttribute("name"), obj );		// Used for fast lookup
				property = properties.getNextNode();
			}
		}
		
		// Extract baseclasses
		XMLNodeList baseclasses = classNode.getNodeList("baseclass");
		if( baseclasses.getCount() > 0 )
		{
			XMLNode baseclass = baseclasses.getFirstNode();
			while( baseclass != null )
			{
				Baseclass obj = new Baseclass( baseclass.getAttribute("name") );
				m_baseclasses.add( obj );
				baseclass = baseclasses.getNextNode();
			}
		}
		
		// Extract references
		XMLNodeList references = classNode.getNodeList("references/reference");
		if( references.getCount() > 0 )
		{
			XMLNode reference = references.getFirstNode();
			while( reference != null )
			{
				Reference obj = new Reference( reference.getAttribute("class"),
											   reference.getAttribute("fkey") );
				m_references.add( obj );
				reference = references.getNextNode();
			}
		}
	}
	
	public String getName(){ return this.name; };
	public String getAbstractType(){ return this.abstractType; };
	public String getTable(){ return this.table; };
	public String getSecurity(){ return this.security; };
	public String getPermit(){ return this.permit; };
	public int getPropertyCount(){ return m_properties.size(); };
	public TaxonomyClass.Property getProperty( int index ){ return (TaxonomyClass.Property)m_properties.get(index); };
	public TaxonomyClass.Property getNamedProperty( String name ){ return (TaxonomyClass.Property)m_propertiesMap.get(name); };
	public int getBaseclassCount(){ return m_baseclasses.size(); };
	public TaxonomyClass.Baseclass getBaseclass( int index ){ return (TaxonomyClass.Baseclass)m_baseclasses.get(index); };
	public int getReferenceCount(){ return m_references.size(); };
	public TaxonomyClass.Reference getRefernce( int index ){ return (TaxonomyClass.Reference)m_references.get(index); };
}
