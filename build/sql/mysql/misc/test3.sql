DROP TABLE IF EXISTS base_patron;

CREATE TABLE base_patron
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	email varchar (64) NOT NULL,
	firstname varchar (48) NOT NULL,
	lastname varchar (48) NOT NULL,
	phone_num varchar (32) NOT NULL
);
ALTER TABLE base_patron ADD
CONSTRAINT PK_base_patron PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE base_patron ADD
CONSTRAINT IX_base_patron UNIQUE NONCLUSTERED 
(
	email
);