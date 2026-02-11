SELECT 

T1.id AS 'id',
T1.id AS 'id',
T2.username AS 'username',
T2.password AS 'password',
T3.id AS 'role__id',
T3.name AS 'role__name',
T3.description AS 'role__description',
T4.id AS 'parent__id',
T4.name AS 'parent__name',
T4.description AS 'parent__description',
T4.admin AS 'parent__admin',
T3.admin AS 'role__admin',
T2.firstname AS 'firstname',
T2.lastname AS 'lastname',
T2.emailaddr AS 'emailaddr',
T2.phonenum AS 'phonenum',
T3.address AS 'address',
T3.address2 AS 'address2',
T3.address3 AS 'address3',
T3.city AS 'city',
T3.state AS 'state',
T3.zip AS 'zip' 

FROM 

sys_client T1 
LEFT JOIN sys_client T2 ON T1.id=T2.id 
LEFT JOIN sys_role T3 ON T2.id=T3.id 
LEFT JOIN sys_role T4 ON T3.id=T4.id 
LEFT JOIN sys_user T2 ON T1.id=T2.id 
LEFT JOIN sys_addr T3 ON T2.id=T3.id 

LIMIT 6