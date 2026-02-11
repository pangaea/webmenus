--CREATE TABLE xorder_menu_order
--(
	-- meta data
--	id varchar (32) not null,
--	owner varchar (32) null,
--	role varchar (32) null,
--	author varchar (32) null,
--	publisher varchar (32) null,
--	created datetime null,
--	modified datetime null,

--	order_time datetime NOT NULL,
--	email varchar (64) NOT NULL
--);
--ALTER TABLE xorder_menu_order ADD
--CONSTRAINT PK_xorder_menu_order PRIMARY KEY NONCLUSTERED 
--(
--	id
--);

ALTER TABLE base_location ADD tax_rate double AFTER name;

ALTER TABLE base_location ADD timezone varchar (10) AFTER tax_rate;

ALTER TABLE base_location DROP COLUMN timezone;
ALTER TABLE base_location ADD timezone varchar (48) AFTER tax_rate;

ALTER TABLE base_location ADD exit_url varchar (255) AFTER theme;

ALTER TABLE base_location ADD theme varchar (32) AFTER email_addr;

CREATE NONCLUSTERED INDEX IX_xorder_menu_item__cat ON xorder_menu_item (menu_cat_id);
ALTER TABLE xorder_menu_item ADD INDEX IX_xorder_menu_item__cat(menu_cat_id);