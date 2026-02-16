SELECT 

T1.id AS 'id',
T1.name AS 'name',
T1.description AS 'description',
T1.options AS 'options',
T1.size_desc AS 'size',
T1.price AS 'price',
T1.quantity AS 'quantity',
T1.menuorder_id AS 'menuorder',
T2.id AS 'menuorder.id',
date_format(T2.order_time,"%m/%d/%Y %h:%i %p") AS 'menuorder.order_time',
T2.email AS 'menuorder.email' 

FROM 

xorder_menu_order_item T1 
LEFT JOIN xorder_menu_order T2 ON T1.menuorder_id=T2.id

WHERE 

T1.menuorder_id = '3CE34E07-FCB2-22AC-7C77-BDD4BDF1' 

LIMIT 6