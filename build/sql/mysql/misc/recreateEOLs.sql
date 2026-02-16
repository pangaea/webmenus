DROP TABLE IF EXISTS webmenus_ophours_menus_list;

-- ----------------------------------------------
CREATE TABLE webmenus_ophours_menus_list
(
	-- properties
	fkey varchar (36) not null,
	objid varchar (36) not null,
	objindex int not null
);
ALTER TABLE webmenus_ophours_menus_list ADD
INDEX PK_webmenus_ophours_menus_list
(
	fkey
);


DROP TABLE IF EXISTS w3_page_link_list;

CREATE TABLE w3_page_link_list
(
	-- properties
	fkey varchar (36) not null,
	objid varchar (36) not null,
	objindex int not null
);
ALTER TABLE w3_page_link_list ADD
INDEX PK_w3_page_link_list
(
	fkey
);