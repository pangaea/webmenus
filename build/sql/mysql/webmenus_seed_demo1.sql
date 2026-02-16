INSERT INTO base_location ( id, name, tax_rate, timezone, phone_num, fax_num, fax_orders, email_addr, email_orders, logo, theme, exit_url ) VALUES
('E3DEA1B9-BA5C-D8EA-5501-BC7814ECDC1F', 'Matt''s Pizza', 8.5, 'US/Eastern', '(631)123-4567', '(631)123-4567', 'N', 'kjacovelli@gmail.com', 'N', '', 'demo1', '')
;

delete from sys_addr where id='E3DEA1B9-BA5C-D8EA-5501-BC7814ECDC1F';

INSERT INTO sys_addr ( id, address, address2, address3, city, state, zip ) VALUES
('E3DEA1B9-BA5C-D8EA-5501-BC7814ECDC1F', '100 Middle Country Rd.', '', '', 'Sound Beach', 'NY', '11789')
;
