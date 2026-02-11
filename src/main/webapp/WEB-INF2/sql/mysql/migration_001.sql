CREATE TABLE w3_js
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) character set utf8 NOT NULL,
	description varchar (128) character set utf8 NOT NULL,
	code longtext character set utf8
);
ALTER TABLE w3_js ADD
CONSTRAINT PK_w3_js PRIMARY KEY NONCLUSTERED 
(
	id
);

-- ----------------------------------------------
CREATE TABLE w3_page_js_list
(
	-- properties
	fkey varchar (36) not null,
	objid varchar (36) not null,
	objindex int not null
);
ALTER TABLE w3_page_js_list ADD
INDEX PK_w3_page_js_list
(
	fkey
);

-- ----------------------------------------------
CREATE TABLE w3_css
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) character set utf8 NOT NULL,
	description varchar (128) character set utf8 NOT NULL,
	body longtext character set utf8
);
ALTER TABLE w3_css ADD
CONSTRAINT PK_w3_css PRIMARY KEY NONCLUSTERED 
(
	id
);

-- ----------------------------------------------
CREATE TABLE w3_page_css_list
(
	-- properties
	fkey varchar (36) not null,
	objid varchar (36) not null,
	objindex int not null
);
ALTER TABLE w3_page_css_list ADD
INDEX PK_w3_page_css_list
(
	fkey
);