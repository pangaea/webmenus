///////////////////////////////////////
// Copyright (c) 2004-2011 Kevin Jacovelli
// All Rights Reserved
///////////////////////////////////////

package com.genesys.views;

import java.util.*;

import com.genesys.SystemServlet;
import com.genesys.util.xml.*;

public class InterfaceCfg
{
	public class Enum
	{
		public class Value
		{
			private String _text, _code;
			
			// Public interface
			protected Value(XMLNode node)
			{
				_text = safeString(node.getAttribute("text"),"");
				_code = safeString(node.getAttribute("code"),"");
			}
			public String getText(){ return _text; }
			public String getCode(){ return _code; }
		}
		private String _name;
		private List<Value> _values;
		
		// Public interface
		protected Enum(XMLNode node)
		{
			_values = new Vector<Value>();
			XMLIterator iterValues = new XMLIterator(node.getNodeList("value"));
			while(iterValues.each()) _values.add(new Value(iterValues.getNode()));
		}
		public String getName(){ return _name; }
		public List<Value> getValues(){ return _values; }
	}

	public class Portal
	{
		public class Tab
		{
			public class Shortcut
			{
				private String _text, _image, _viewName, _filter, _details_val;
				
				// Public interface
				protected Shortcut(XMLNode node)
				{
					_text = safeString(node.getAttribute("text"),"");
					_image = safeString(node.getAttribute("image"),"");
					_viewName = safeString(node.getAttribute("view"),"");
					_filter = safeString(node.getAttribute("filter"),"");
					_details_val = safeString(node.getValue(),"");
				}
				public String getText(){ return _text; }
				public String getImage(){ return _image; }
				public String getViewName(){ return _viewName; }
				public String getFilter(){ return _filter; }
				public String getDetails(){ return _details_val; }
			}
			
			private String _name, _text;
			private List<Shortcut> _shortcuts;
			
			// Public interface
			protected Tab(XMLNode node)
			{
				_name = safeString(node.getAttribute("name"),"");
				_text = safeString(node.getAttribute("text"),"");
				_shortcuts = new Vector<Shortcut>();
				XMLIterator iterShortcuts = new XMLIterator(node.getNodeList("shortcut"));
				while(iterShortcuts.each()) _shortcuts.add(new Shortcut(iterShortcuts.getNode()));
			}
			public String getName(){ return _name; }
			public String getText(){ return _text; }
			public List<Shortcut> getShortcuts(){ return _shortcuts; }
		}
		private String _name, _welcomepage, _defaultview;
		private int _navWidth;
		private List<Tab> _tabs;
		
		// Public interface
		protected Portal( XMLNode node )
		{
			_name = safeString(node.getAttribute("name"),"");
			XMLNode navbar = node.getSingleNode("navbar");
			_tabs = new Vector<Tab>();
			XMLIterator iterTabs = new XMLIterator(node.getNodeList("navbar/tab"));
			while(iterTabs.each()) _tabs.add(new Tab(iterTabs.getNode()));
			if(!navbar.isNull()){
				_navWidth = tryParseInt(navbar.getAttribute("width"), 0);
				_defaultview = safeString(navbar.getAttribute("default_view"), "");
			}
			_welcomepage = safeString(node.getAttribute("welcomepage"),"");
		}
		public String getName(){ return _name; }
		public int getNavbarWidth(){ return _navWidth; }
		public String getDefaultView(){ return _defaultview; }
		public String getWelcomePage(){ return _welcomepage; }
		public List<Tab> getTabs(){ return _tabs; }
	}
	
	public class View
	{
		public class Field
		{
			private String _name, _property, _filter;
			
			protected Field(XMLNode node)
			{
				_name = safeString(node.getAttribute("name"),"");
				_property = safeString(node.getAttribute("property"),"");
				_filter = safeString(node.getAttribute("filter"),"");
			}
			public String getName(){ return _name; }
			public String getProperty(){ return _property; }
			public String getFilter(){ return _filter; }
		}
		public class ToolbarButton
		{
			private String _text;
			private int _eventNum;
			
			protected ToolbarButton(XMLNode node)
			{
				_text = safeString(node.getAttribute("text"),"");
				_eventNum = tryParseInt(node.getAttribute("eventnum"), 0);
			}
			public String getText(){ return _text; }
			public int getEventnum(){ return _eventNum; }
			
		}
		public class Column
		{
			private String _text, _enum, _fieldname, _viewname, _fkey;
			private int _width;			
			
			// Public interface
			protected Column(XMLNode node)
			{
				_text = safeString(node.getAttribute("text"),"");
				_enum = safeString(node.getAttribute("enum"),"");
				_fieldname = safeString(node.getAttribute("field"),"");
				_viewname = safeString(node.getAttribute("view"),"");
				_fkey = safeString(node.getAttribute("fkey"),"");
				_width = tryParseInt(node.getAttribute("width"), 0);
			}
			public String getText(){ return _text; }
			public String getEnum(){ return _enum; }
			public String getFieldName(){ return _fieldname; }
			public String getViewName(){ return _viewname; }
			public String getFKey(){ return _fkey; }
			public int getWidth(){ return _width; }
			
		}
		public class Link
		{
			private String _text, _type, _viewName, _reference;
			
			protected Link(XMLNode node)
			{
				_text = safeString(node.getAttribute("text"),"");
				_type = safeString(node.getAttribute("type"),"");
				_viewName = safeString(node.getAttribute("view"),"");
				_reference = safeString(node.getAttribute("reference"),"");
			}
			public String getText(){ return _text; }
			public String getType(){ return _type; }
			public String getViewName(){ return _viewName; }
			public String getReference(){ return _reference; }
		}
		public class Input
		{
			private String _field, _property, _type, _text, _display, _constraint, _view;
			private String _target, _filter, _visible, _default_val;
			private int _length, _height, _width;
			private  boolean _required;
			
			// Public interface
			protected Input(XMLNode node)
			{
				_field = safeString(node.getAttribute("field"),"");
				_property = safeString(node.getAttribute("property"),"");
				_type = safeString(node.getAttribute("type"),"");
				_text = safeString(node.getAttribute("text"),"");
				_display = safeString(node.getAttribute("display"),"");
				_constraint = safeString(node.getAttribute("constraint"),"");
				_view = safeString(node.getAttribute("view"),"");
				_target = safeString(node.getAttribute("target"),"");
				_filter = safeString(node.getAttribute("filter"),"");
				_length = tryParseInt(node.getAttribute("length"), 0);
				_height = tryParseInt(node.getAttribute("height"), 0);
				_width = tryParseInt(node.getAttribute("width"), 0);
				_visible = safeString(node.getAttribute("visible"),"");
				_required = tryParseBool(node.getAttribute("required"), false);
				_default_val = safeString(node.getValue(),"");
			}
			public String getField(){ return _field; }
			public String getProperty(){ return _property; }
			public String getType(){ return _type; }
			public String getText(){ return _text; }
			public String getDisplay(){ return _display; }
			public String getConstraint(){ return _constraint; }
			public int getLength(){ return _length; }
			public String getView(){ return _view; }
			public int getHeight(){ return _height; }
			public int getWidth(){ return _width; }
			public String getVisible(){ return _visible; }
			public String getTarget(){ return _target; }
			public String getFilter(){ return _filter; }
			public boolean getRequired(){ return _required; }
			public String getDefaultVal(){ return _default_val; }
		}
		private String _name, _byref, _classname, _title, _desc, _helpindex, _formLayout, _formExternal, _formExternalLinkColumn;
		private String _sortBy, _sortOrder, _access, _transform, _eventScript;
		private int _listsize, _listexpandsize, _listrowheight;
		private boolean _listVisible, _formVisible;
		private HashMap<String, Field> _fields;
		private List<ToolbarButton> _toolbarButtons, _formToolbarButtons;
		private List<Column> _columns;
		private List<Link> _links;
		private List<Input> _inputs;
		
		// Public interface
		protected View(XMLNode node)
		{
			_name = safeString(node.getAttribute("name"),"");
			_classname = safeString(node.getAttribute("class"),"");
			_byref = safeString(node.getAttribute("byref"),"");
			_title = safeString(node.getChildNodeValue("title"),"");
			_desc = safeString(node.getChildNodeValue("description"),"");
			_helpindex = safeString(node.getChildNodeValue("helpindex"),"");
			
			XMLNode form = node.getSingleNode("form");
			if(!form.isNull())
			{
				_formLayout = form.getAttribute("layout");
				_formVisible = tryParseBool(form.getAttribute("visible"), true);
				_formExternal = form.getAttribute("external");
				_formExternalLinkColumn = form.getAttribute("external_link_column");
			}
			else
			{
				_formLayout = new String("columns");
				_formVisible = true;
				_formExternal = "";
				_formExternalLinkColumn = "";
			}
			
			_sortBy = safeString(node.getAttribute("sortBy"),"");
			_sortOrder = safeString(node.getAttribute("sortOrder"),"");
			_access = safeString(node.getAttribute("access"),"");
			_transform = safeString(node.getAttribute("transform"),"");
			
			XMLNode events = node.getSingleNode("events");
			if(!events.isNull())
				_eventScript = events.getAttribute("script");
			else
				_eventScript = "";
			
			XMLNode list = node.getSingleNode("list");
			if(!list.isNull())
			{
				_listsize = tryParseInt(list.getAttribute("size"), 0);
				_listexpandsize = tryParseInt(list.getAttribute("expandsize"), 0);
				_listVisible = tryParseBool(list.getAttribute("visible"), true);
				_listrowheight = tryParseInt(list.getAttribute("rowheight"), 34);
			}
			else
			{
				_listsize = 0;
				_listexpandsize = 0;
				_listVisible = true;
				_listrowheight = 34;
			}

			_fields = new HashMap<String, Field>();
			XMLIterator iterFields = new XMLIterator(node.getNodeList("field"));
			while(iterFields.each()) _fields.put(iterFields.getNode().getAttribute("name"), new Field(iterFields.getNode()));
			
			_toolbarButtons = new Vector<ToolbarButton>();
			XMLIterator iterToolbarButtons = new XMLIterator(node.getNodeList("events/toolbar[not(@type) or @type!='form']/button"));
			while(iterToolbarButtons.each()) _toolbarButtons.add(new ToolbarButton(iterToolbarButtons.getNode()));
			
			_formToolbarButtons = new Vector<ToolbarButton>();
			XMLIterator iterFormToolbarButtons = new XMLIterator(node.getNodeList("events/toolbar[@type='form']/button"));
			while(iterFormToolbarButtons.each()) _formToolbarButtons.add(new ToolbarButton(iterFormToolbarButtons.getNode()));

			_columns = new Vector<Column>();
			XMLIterator iterCols = new XMLIterator(node.getNodeList("list/columns/column"));
			while(iterCols.each()) _columns.add(new Column(iterCols.getNode()));

			_links = new Vector<Link>();
			XMLIterator iterLinks = new XMLIterator(node.getNodeList("links/link"));
			while(iterLinks.each()) _links.add(new Link(iterLinks.getNode()));
			
			_inputs = new Vector<Input>();
			XMLIterator iterInputs = new XMLIterator(node.getNodeList("form/input"));
			while(iterInputs.each()) _inputs.add(new Input(iterInputs.getNode()));
		}
		public String getName(){ return _name; }
		public String getClassName(){ return _classname; }
		public String getByRef(){ return _byref; }
		public String getTitle(){ return _title; }
		public String getDescription(){ return _desc; }
		public String getHelpIndex(){ return _helpindex; }
		public int getListSize(){ return _listsize; }
		public int getListExpandSize(){ return _listexpandsize; }
		public int getListRowHeight(){ return _listrowheight; }
		public boolean getListVisible(){ return _listVisible; }
		public String getFormLayout(){ return _formLayout; }
		public boolean getFormVisible(){ return _formVisible; }
		public String getFormExternal(){ return _formExternal; }
		public String getFormExternalLinkColumn(){ return _formExternalLinkColumn; }
		public String getSortBy(){ return _sortBy; }
		public String getSortOrder(){ return _sortOrder; }
		public String getAccess(){ return _access; }
		public String getTransform() { return _transform; }
		public String getEventScript() { return _eventScript; }
		public HashMap<String, Field> getFields(){ return _fields; }
		public Field getField(String name) { return _fields.get(name); }
		public List<ToolbarButton> getToolbarButtons(){ return _toolbarButtons; }
		public List<ToolbarButton> getFormToolbarButtons(){ return _formToolbarButtons; }
		public List<Column> getColumns(){ return _columns; }
		public List<Link> getLinks(){ return _links; }
		public List<Input> getInputs(){ return _inputs; }
	}
	
	// Private properties
	private HashMap<String, Enum>	_enumNodes;
	private HashMap<String, Portal>	 _portalNodes;
	private HashMap<String, View>	_viewNodes;
	
	// Public interface
	public InterfaceCfg()
	{
		_enumNodes = new HashMap<String, Enum>();
		_portalNodes = new HashMap<String, Portal>();
		_viewNodes = new HashMap<String, View>();
	}

	public InterfaceCfg( XMLDocument cfgDoc )
	{
		this();
		append( cfgDoc );
	}
	
	protected String safeString(String str, String defaultVal)
	{
		try{
			if(str!=null) return str;
		}catch(Exception e){}
		return defaultVal;
	}
	
	protected int tryParseInt(String str, int defaultVal)
	{
		try{
			return Integer.parseInt(str);
		}catch(Exception e){
			return defaultVal;
		}
	}
	
	protected boolean tryParseBool(String str, boolean defaultVal)
	{
		try{
			if(str.length() == 0) return defaultVal;
			return Boolean.parseBoolean(str);
		}catch(Exception e){
			return defaultVal;
		}
	}
	
	public void append( XMLDocument cfgDoc )
	{
		if( cfgDoc.isNull() )
		{
			SystemServlet.g_logger.fatal( "server.cfg failed to load" );
			return;
		}		
		XMLIterator iterEnums = new XMLIterator(cfgDoc.getNodeList("//enum"));
		while(iterEnums.each()) _enumNodes.put(iterEnums.getNode().getAttribute("name"), new Enum(iterEnums.getNode()));
		
		XMLIterator iterPortals = new XMLIterator(cfgDoc.getNodeList("//portal"));
		while(iterPortals.each()) _portalNodes.put(iterPortals.getNode().getAttribute("name"), new Portal(iterPortals.getNode()));
		
		XMLIterator iterViews = new XMLIterator(cfgDoc.getNodeList("//view"));
		while(iterViews.each()) _viewNodes.put(iterViews.getNode().getAttribute("name"), new View(iterViews.getNode()));
	}
	public HashMap<String, Enum> getEnums() { return _enumNodes; }
	public Enum getEnum(String name) { return _enumNodes.get(name); }
	public HashMap<String, Portal> getPortals() { return _portalNodes; }
	public Portal getPortal(String name) { return _portalNodes.get(name); }
	public HashMap<String, View> getViews() { return _viewNodes; }
	public View getView(String name) { return _viewNodes.get(name); }
}