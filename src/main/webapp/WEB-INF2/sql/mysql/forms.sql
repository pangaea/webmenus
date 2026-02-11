DROP TABLE IF EXISTS forms_inputs;
DROP TABLE IF EXISTS forms_questions;
DROP TABLE IF EXISTS forms_value;
DROP TABLE IF EXISTS forms_categories;
DROP TABLE IF EXISTS forms;
DROP TABLE IF EXISTS workgroups_forms_issuance;
DROP TABLE IF EXISTS workgroups_users_ref;
DROP TABLE IF EXISTS workgroups;
DROP TABLE IF EXISTS users_questions_responses;
DROP TABLE IF EXISTS users_forms_responses;

-----------------------------------------------------

--- type ---
--- 0 : disabled
--- 1 : survey
--- 2 : exam - random
--- 3 : trivia exam - static
--- 4 : trivia exam - random
CREATE TABLE forms
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	type int NOT NULL,
	title varchar (64) NOT NULL,
	questcount int NOT NULL,
	status varchar (16) NOT NULL
);

ALTER TABLE forms ADD
CONSTRAINT PK_forms PRIMARY KEY NONCLUSTERED 
(
	id
);

------------------------------------------------

CREATE TABLE forms_categories
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	formid char (24) NOT NULL,
	category int NOT NULL,
	description varchar (32) NOT NULL,
	difficulty int NOT NULL
);

ALTER TABLE forms_categories ADD 
CONSTRAINT FK_forms_categories__forms FOREIGN KEY 
(
	formid
) REFERENCES forms
(
	id
);

ALTER TABLE forms_categories ADD
CONSTRAINT PK_forms_categories PRIMARY KEY NONCLUSTERED 
(
	formid,
	category
);

-----------------------------------------------

--- type ---
--- 0 : multiple choice
--- 1 : fill-in
--- 2 : muliple select

--- difficulty ---
--- 0 : no-op
--- 1 : easy
--- 2 : normal
--- 3 : hard
CREATE TABLE forms_questions
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	formid char (24) NOT NULL,
	number int NOT NULL,
	type int NOT NULL,
	category int NOT NULL,
	difficulty int NOT NULL,
	body text NOT NULL 
);

--CREATE CLUSTERED INDEX IX_forms_questions ON forms_questions
--(
--	formid
--);

ALTER TABLE forms_questions ADD
CONSTRAINT PK_forms_questions PRIMARY KEY NONCLUSTERED 
(
	formid,
	number
);

ALTER TABLE forms_questions ADD 
CONSTRAINT FK_forms_questions__forms FOREIGN KEY 
(
	formid
) REFERENCES forms
(
	id
);

---------------------------------------------------

--- type ---
--- 0 : selection
--- 1 : correct selection
--- 2 : entry
CREATE TABLE forms_inputs
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	-- formid char (24) NOT NULL,
	questnum char (24) NOT NULL,
	number int NOT NULL,
	display varchar (16) NOT NULL,
	type int NOT NULL,
	body text NOT NULL 
);

--CREATE CLUSTERED INDEX IX_forms_inputs ON forms_inputs
--(
--	formid,
--	questnum
--);

ALTER TABLE forms_inputs ADD
CONSTRAINT PK_forms_inputs PRIMARY KEY NONCLUSTERED 
(
	formid,
	questnum,
	number
);

ALTER TABLE forms_inputs ADD 
CONSTRAINT FK_forms_inputs__forms FOREIGN KEY 
(
	questnum
) REFERENCES forms_questions
(
	id
);

CREATE TABLE workgroups
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	name char (64) NOT NULL,
	description char (255) NOT NULL,
);

ALTER TABLE workgroups ADD
CONSTRAINT PK_workgroups PRIMARY KEY NONCLUSTERED
(
	id
);

CREATE TABLE workgroups_users_ref
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	-- properties
	workgroupid char (24) null,
	userid char (24) null
);

ALTER TABLE workgroups_users_ref ADD
CONSTRAINT PK_workgroups_users_ref PRIMARY KEY NONCLUSTERED
(
	id
);
CREATE TABLE workgroups_forms_issuance
(
	-- meta data
	id char (24) not null,
	owner char (24) null,
	role char (24) null,
	author char (24) null,
	publisher char (24) null,
	created datetime null,
	modified datetime null,

	-- properties
	workgroupid char (24) null,
	formid char (24) null,
	notes char (255) null
);

ALTER TABLE workgroups_forms_issuance ADD
CONSTRAINT PK_workgroups_forms_issuance PRIMARY KEY NONCLUSTERED
(
	id
);

--================================
--======= User Data ==============
--================================

--- type
--- 0 : unprocessed
--- 1 : accepted
CREATE TABLE users_forms_responses
(
	id int NOT NULL auto_increment, primary key(id),
	userid varchar (16) NULL,
	fullname varchar (64) NULL,
	accountnum int NULL,
	formid char (24) NULL,
	issid char (24) NULL,
	response_time datetime NULL,
	type int NULL,
	grade int NULL
);

--CREATE CLUSTERED INDEX IX_users_forms_responses ON users_forms_responses
--(
--	userid,
--	accountnum
--);

--ALTER TABLE users_forms_responses ADD
--CONSTRAINT PK_users_forms_responses PRIMARY KEY NONCLUSTERED 
--(
--	id
--);

------------------------------------------------

CREATE TABLE users_questions_responses
(
	responseid int NOT NULL,
	responsenum int NOT NULL,
	questnum int NOT NULL,
	answer text NOT NULL 
);

--CREATE CLUSTERED INDEX IX_questions_responses ON users_questions_responses
--(
--	responseid
--);

ALTER TABLE users_questions_responses ADD
CONSTRAINT PK_questions_responses PRIMARY KEY NONCLUSTERED 
(
	responseid,
	responsenum
);

ALTER TABLE users_questions_responses ADD 
CONSTRAINT FK_forms_responses FOREIGN KEY 
(
	responseid
) REFERENCES users_forms_responses
(
	id
);
