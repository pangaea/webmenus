-- DROP TABLE IF EXISTS w3_content;
-- DROP TABLE IF EXISTS w3_div;
-- DROP TABLE IF EXISTS w3_page_content_list;
DROP TABLE IF EXISTS w3_js;
DROP TABLE IF EXISTS w3_page_js_list;
DROP TABLE IF EXISTS w3_css;
DROP TABLE IF EXISTS w3_page_css_list;
DROP TABLE IF EXISTS w3_page;
DROP TABLE IF EXISTS w3_page_link_list;
DROP TABLE IF EXISTS w3_link;
-- ---------------------------------------------------

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

-- ----------------------------------------------
CREATE TABLE w3_link
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
	title varchar (64) character set utf8 NOT NULL,
	uri varchar (255) character set utf8 NOT NULL
);
ALTER TABLE w3_link ADD
CONSTRAINT PK_w3_link PRIMARY KEY NONCLUSTERED 
(
	id
);

-- ----------------------------------------------
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

-- ----------------------------------------------
-- CREATE TABLE w3_content
-- (
	-- meta data
-- 	id varchar (36) not null,
-- 	owner varchar (36) null,
-- 	role varchar (36) null,
-- 	author varchar (36) null,
-- 	publisher varchar (36) null,
-- 	created datetime null,
-- 	modified datetime null,

-- 	name varchar (64) character set utf8 NOT NULL,
-- 	type varchar (32) character set utf8 NOT NULL,
-- 	content longtext character set utf8
-- );
-- ALTER TABLE w3_content ADD
-- CONSTRAINT PK_w3_content PRIMARY KEY NONCLUSTERED 
-- (
-- 	id
-- );

-- ----------------------------------------------
-- CREATE TABLE w3_div
-- (
	-- meta data
-- 	id varchar (36) not null,
-- 	owner varchar (36) null,
-- 	role varchar (36) null,
-- 	author varchar (36) null,
-- 	publisher varchar (36) null,
-- 	created datetime null,
-- 	modified datetime null,

-- 	name varchar (32) character set utf8 NOT NULL,
-- 	div_index int NOT NULL,
-- 	page_id varchar (36) NOT NULL,
-- 	content_id varchar (36) NOT NULL
-- );
-- ALTER TABLE w3_div ADD
-- CONSTRAINT PK_w3_div PRIMARY KEY NONCLUSTERED 
-- (
-- 	id
-- );

-- ----------------------------------------------
-- CREATE TABLE w3_page_content_list
-- (
-- 	-- properties
-- 	fkey varchar (36) not null,
-- 	objid varchar (36) not null
-- );
-- ALTER TABLE w3_page_content_list ADD
-- INDEX PK_w3_page_content_list
-- (
-- 	fkey
-- );

-- ----------------------------------------------
CREATE TABLE w3_page
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
	title varchar (64) character set utf8 NOT NULL,
	viewname varchar (32) character set utf8 NOT NULL,
	content longtext character set utf8,
	show_alerts varchar (1) character set utf8 NOT NULL,
	show_giftcerts varchar (1) character set utf8 NOT NULL
);
ALTER TABLE w3_page ADD
CONSTRAINT PK_w3_page PRIMARY KEY NONCLUSTERED 
(
	id
);
