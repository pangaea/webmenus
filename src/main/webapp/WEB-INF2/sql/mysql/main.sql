-- DROP TABLE IF EXISTS main_message_recipients_list;
-- DROP TABLE IF EXISTS main_message;
DROP TABLE IF EXISTS sys_access;
DROP TABLE IF EXISTS sys_group;
DROP TABLE IF EXISTS sys_role_group_ref;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_addr;
DROP TABLE IF EXISTS sys_client;

CREATE TABLE sys_client
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,
	
	--  properties
	username varchar (64) not null,
	password varchar (64) not null,
	roleid varchar (36) null,
	last_logged_in datetime null
);

ALTER TABLE sys_client ADD
CONSTRAINT PK_sys_client PRIMARY KEY NONCLUSTERED
(
	id
);

ALTER TABLE sys_client ADD
CONSTRAINT PK_sys_client_username UNIQUE
(
	username
);

CREATE TABLE sys_addr
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,
	
	--  properties
	address varchar (64) null,
	address2 varchar (64) null,
	address3 varchar (64) null,
	city varchar (64) null,
	state varchar (16) null,
	zip varchar (16) null
);

ALTER TABLE sys_addr ADD
CONSTRAINT PK_sys_addr PRIMARY KEY NONCLUSTERED
(
	id
);


CREATE TABLE sys_user
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,
	
	--  properties
	firstname varchar (64) null,
	lastname varchar (64) null,
	emailaddr varchar (64) null,
	phonenum varchar (64) null,
	show_welcome varchar (1) NULL
);

ALTER TABLE sys_user ADD
CONSTRAINT PK_sys_user PRIMARY KEY NONCLUSTERED
(
	id
);

CREATE TABLE sys_role
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,
	
	--  properties
	name varchar (64) null,
	description varchar (64) null,
	parent varchar (36) null,
	admin varchar (1) null
);

ALTER TABLE sys_role ADD
CONSTRAINT PK_sys_role PRIMARY KEY NONCLUSTERED
(
	id
);

CREATE TABLE sys_role_group_ref
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	--  properties
	roleid varchar (36) null,
	groupid varchar (36) null
);

ALTER TABLE sys_role_group_ref ADD
CONSTRAINT PK_sys_role_group_ref PRIMARY KEY NONCLUSTERED
(
	id
);

CREATE TABLE sys_group
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	--  properties
	name varchar (64) null,
	description varchar (64) null
);

ALTER TABLE sys_group ADD
CONSTRAINT PK_sys_group PRIMARY KEY NONCLUSTERED
(
	id
);

CREATE TABLE sys_access
(
	--  meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	--  properties
	view varchar (64) null,
	access varchar (32) null,
	groupid varchar (36) null
);

ALTER TABLE sys_access ADD
CONSTRAINT PK_sys_access PRIMARY KEY NONCLUSTERED
(
	id
);

-- ---------------------------------------------------
