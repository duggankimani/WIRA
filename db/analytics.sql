select * from func_GetTasksAging('chasebank.finance.ExpenseClaim','2016-01-01'::TIMESTAMP, null::TIMESTAMP);

select base1.processId, count(*) taskcount, base1.period from 
(select l.processId, fun_extractPeriod(cast(t.createdon as TIMESTAMP),'Month') period 
from Task t 
inner join processinstancelog l 
on (l.processinstanceid=t.processinstanceid) 
where l.status=0 and start_date >= cast('2016-01-01' as DATE) and start_date<=COALESCE(cast(null as  DATE), CURRENT_DATE)) as base1 
group by base1.processId, base1.period;