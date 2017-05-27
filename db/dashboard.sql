--http://stackoverflow.com/questions/24407270/calculate-total-business-working-days-between-two-dates
﻿select count(d.*), 
case
when p.status=-1 then 'Failed'
when p.status=0 then 'INPROGRESS'
when p.status=1 then 'Exited' 
when p.status=2 then 'COMPLETED' 
else 'Other'
end 
from documentjson d inner join processinstancelog p on (p.processinstanceid=d.processinstanceid) group by p.status;

select count(*),status from processinstancelog group by status;

select count(*) from processinstanceinfo;

select count(*), 
case 
when status='DRAFTED' then 'DRAFTED'
when status='INPROGRESS' then 'INPROGRESS'
when status='COMPLETED' then 'COMPLETED' 
end as category
from documentjson group by status;

select caseno, processinstanceid, status,process from documentjson;
