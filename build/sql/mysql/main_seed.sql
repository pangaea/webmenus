-- INSERT user group seed data
INSERT INTO sys_group ( id, name, description ) VALUES
('KJ1CP6I-7IY-5OBN-EB41','all access','Read / Write access to all views')
;
INSERT INTO sys_access ( id, view, access, groupid ) VALUES
('KJ1CP6I-7IY-5OBN-EB42','roles','read/write','KJ1CP6I-7IY-5OBN-EB41')
;
INSERT INTO sys_group ( id, name, description ) VALUES
('KJ1CP6I-7IY-5OBN-EB71','guest access','Basic access to certain views')
;

-- INSERT roles seed data
INSERT INTO sys_role ( id, name, description, admin ) VALUES
('KJ1CP6I-7IY-5OBN-EB43','administration','Administrative role','Y')
;
INSERT INTO sys_role_group_ref ( id, roleid, groupid ) VALUES
('KJ1CP6I-7IY-5OBN-EB44','KJ1CP6I-7IY-5OBN-EB43','KJ1CP6I-7IY-5OBN-EB41')
;
INSERT INTO sys_role ( id, name, description, admin ) VALUES
('KJ1CP6I-7IY-5OBN-EB73','guest','Guest user role','N')
;
INSERT INTO sys_role_group_ref ( id, roleid, groupid ) VALUES
('KJ1CP6I-7IY-5OBN-EB74','KJ1CP6I-7IY-5OBN-EB73','KJ1CP6I-7IY-5OBN-EB71')
;

-- INSERT user seed data
INSERT INTO sys_client ( id, username, password, roleid ) VALUES
('KJ1CP6I-7IY-5OBN-EB35','admin','21232f297a57a5a743894a0e4a801fc3','KJ1CP6I-7IY-5OBN-EB43')
;
INSERT INTO sys_addr ( id ) VALUES
('KJ1CP6I-7IY-5OBN-EB35')
;
INSERT INTO sys_user ( id, firstname, lastname, emailaddr, phonenum ) VALUES
('KJ1CP6I-7IY-5OBN-EB35','Kevin','Jacovelli','kjacovelli@gmail.com','123.456.7890')
;
INSERT INTO sys_client ( id, username, password, roleid ) VALUES
('KJ1CP6I-7IY-5OBN-EB75','guest','084e0343a0486ff05530df6c705c8bb4','KJ1CP6I-7IY-5OBN-EB73')
;
INSERT INTO sys_addr ( id ) VALUES
('KJ1CP6I-7IY-5OBN-EB75')
;
INSERT INTO sys_user ( id ) VALUES
('KJ1CP6I-7IY-5OBN-EB75')
;
