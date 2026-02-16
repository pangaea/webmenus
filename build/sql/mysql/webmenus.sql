DROP TABLE IF EXISTS webmenus_menu_order_item;
DROP TABLE IF EXISTS webmenus_menu_order;
DROP TABLE IF EXISTS webmenus_menu_item_option;
DROP TABLE IF EXISTS webmenus_menu_item_size;
DROP TABLE IF EXISTS webmenus_menu_item;
DROP TABLE IF EXISTS webmenus_menu_category;
DROP TABLE IF EXISTS webmenus_menu;
DROP TABLE IF EXISTS webmenus_ophours_menus_list;
DROP TABLE IF EXISTS webmenus_ophours;
DROP TABLE IF EXISTS base_patron;
DROP TABLE IF EXISTS base_theme;
DROP TABLE IF EXISTS base_location;
DROP TABLE IF EXISTS base_account;
-- ---------------------------------------------------

-- ----------------------------------------------
CREATE TABLE base_account
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	-- name varchar (64) NOT NULL,
	type varchar (16) NOT NULL,
	accept_license varchar (1) NOT NULL,
	allow_email varchar (1) NOT NULL
);
ALTER TABLE base_account ADD
CONSTRAINT PK_base_account PRIMARY KEY NONCLUSTERED 
(
	id
);

CREATE TABLE base_location
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	tax_rate double NOT NULL,
	timezone varchar (48) NOT NULL,
	phone_num varchar (32) NOT NULL,
	fax_num varchar (32) NOT NULL,
	fax_orders varchar (1) NOT NULL,
	email_addr varchar (64) NOT NULL,
	email_orders varchar (1) NOT NULL,
	logo varchar (255) NOT NULL,
	template varchar (32) NOT NULL,
	theme varchar (36) NOT NULL,
	exit_url varchar (255) NOT NULL,
	delivery_avail varchar (1) NOT NULL
);
ALTER TABLE base_location ADD
CONSTRAINT PK_base_location PRIMARY KEY NONCLUSTERED 
(
	id
);

CREATE TABLE base_theme
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	menuwidth varchar (32) NOT NULL,
	bkcolor varchar (6) NOT NULL,
	fgcolor varchar (6) NOT NULL,
	titlebar_color varchar (6) NOT NULL,
	cat_text_color varchar (6) NOT NULL,
	item_text_color varchar (6) NOT NULL,
	itemdesc_text_color varchar (6) NOT NULL,
	system_text_color varchar (6) NOT NULL
);
ALTER TABLE base_theme ADD
CONSTRAINT PK_base_theme PRIMARY KEY NONCLUSTERED 
(
	id
);

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
ALTER TABLE base_patron ADD UNIQUE INDEX(role, email);
-- ALTER TABLE base_patron ADD
-- CONSTRAINT IX_base_patron UNIQUE NONCLUSTERED 
-- (
-- 	email, role
-- );

-- -------------------------------------------------
-- Sunday
-- Monday
-- Tuesday
-- Wednesday
-- Thursday
-- Friday
-- Saturday

CREATE TABLE webmenus_ophours
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	-- weekday varchar (9) NOT NULL,
	on_sunday varchar (1) NOT NULL,
	on_monday varchar (1) NOT NULL,
	on_tuesday varchar (1) NOT NULL,
	on_wednesday varchar (1) NOT NULL,
	on_thursday varchar (1) NOT NULL,
	on_friday varchar (1) NOT NULL,
	on_saturday varchar (1) NOT NULL,
	start_time time NOT NULL,
	hours int NOT NULL,
	minutes int NOT NULL,
	location_id varchar (36) NOT NULL,
	schedule_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_ophours ADD
CONSTRAINT PK_webmenus_ophours PRIMARY KEY NONCLUSTERED
(
	id
);
ALTER TABLE webmenus_ophours ADD 
CONSTRAINT FK_webmenus_ophours__location FOREIGN KEY 
(
	location_id
) REFERENCES base_location
(
	id
);

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
-- ----------------------------------------------

CREATE TABLE webmenus_menu
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	hidden varchar (1) NOT NULL,
	show_options varchar (1) NOT NULL,
	menu_index int NOT NULL,
	location_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_menu ADD
CONSTRAINT PK_webmenus_menu PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu ADD 
CONSTRAINT FK_webmenus_menu__location FOREIGN KEY 
(
	location_id
) REFERENCES base_location
(
	id
);
-- ALTER TABLE webmenus_menu ADD
-- INDEX IX_webmenus_menu_index NONCLUSTERED 
-- 
--	menu_index
-- );

-- ----------------------------------------------
-- -------------------------------------------------

CREATE TABLE webmenus_menu_category
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	hidden varchar (1) NOT NULL,
	cat_index int NOT NULL,
	menu_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_menu_category ADD
CONSTRAINT PK_webmenus_menu_category PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_category ADD 
CONSTRAINT FK_webmenus_menu_category__menu FOREIGN KEY 
(
	menu_id
) REFERENCES webmenus_menu
(
	id
);

-- -------------------------------------------------

CREATE TABLE webmenus_menu_item
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	description varchar (255) NOT NULL,
	hidden varchar (1) NOT NULL,
	image varchar (255) NOT NULL,
	item_index int NOT NULL,
	menu_cat_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_menu_item ADD
CONSTRAINT PK_webmenus_menu_item PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_item ADD INDEX IX_webmenus_menu_item__cat(menu_cat_id);
ALTER TABLE webmenus_menu_item ADD 
CONSTRAINT FK_webmenus_menu_item__category FOREIGN KEY 
(
	menu_cat_id
) REFERENCES webmenus_menu_category
(
	id
);

-- ---------------------------------

CREATE TABLE webmenus_menu_item_size
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	size_desc varchar (32) NOT NULL,
	price double NOT NULL,
	size_index int NULL,
	menuitem_id varchar (36) NULL
);
ALTER TABLE webmenus_menu_item_size ADD
CONSTRAINT PK_webmenus_menu_item_size PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_item_size ADD 
CONSTRAINT FK_webmenus_menu_item_size__menu FOREIGN KEY 
(
	menuitem_id
) REFERENCES webmenus_menu_item
(
	id
);

-- ---------------------------------

CREATE TABLE webmenus_menu_item_option
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	price double NOT NULL,
	type varchar (32) NOT NULL,
	data varchar (255) NOT NULL,
	option_index int NOT NULL,
	menuitem_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_menu_item_option ADD
CONSTRAINT PK_webmenus_menu_item_option PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_item_option ADD 
CONSTRAINT FK_webmenus_menu_item_option__menu FOREIGN KEY 
(
	menuitem_id
) REFERENCES webmenus_menu_item
(
	id
);

-- ---------------------------------

CREATE TABLE webmenus_menu_order
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	order_time datetime NOT NULL,
	email varchar (64) NOT NULL,
	subtotal double NOT NULL,
	tax_rate double NOT NULL,
	tax double NOT NULL,
	total double NOT NULL,
	location_id varchar (36) NOT NULL,
	delivery varchar (1) NOT NULL,
	delivery_info varchar (255) NOT NULL,
	fulfilled varchar (1) NOT NULL,
	notification_status varchar (11) NOT NULL
);
ALTER TABLE webmenus_menu_order ADD
CONSTRAINT PK_webmenus_menu_order PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_order ADD 
CONSTRAINT FK_webmenus_menu_order__location FOREIGN KEY 
(
	location_id
) REFERENCES base_location
(
	id
);

-- ---------------------------------

CREATE TABLE webmenus_menu_order_item
(
	-- meta data
	id varchar (36) not null,
	owner varchar (36) null,
	role varchar (36) null,
	author varchar (36) null,
	publisher varchar (36) null,
	created datetime null,
	modified datetime null,

	name varchar (64) NOT NULL,
	description varchar (255) NOT NULL,
	options varchar (255) NOT NULL,
	size_desc varchar (32) NOT NULL,
	price double NOT NULL,
	quantity int NOT NULL,
	menuorder_id varchar (36) NOT NULL
);
ALTER TABLE webmenus_menu_order_item ADD
CONSTRAINT PK_webmenus_menu_order_item PRIMARY KEY NONCLUSTERED 
(
	id
);
ALTER TABLE webmenus_menu_order_item ADD 
CONSTRAINT FK_webmenus_menu_order_item__menu FOREIGN KEY 
(
	menuorder_id
) REFERENCES webmenus_menu_order
(
	id
);
