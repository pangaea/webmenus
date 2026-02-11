CREATE TABLE webmenus_schedule
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
	description varchar (255) character set utf8 NOT NULL
);
ALTER TABLE webmenus_schedule ADD
CONSTRAINT PK_webmenus_schedule PRIMARY KEY NONCLUSTERED 
(
	id
);
