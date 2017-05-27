drop function if exists func_GetTasksAging(varchar,DATE,DATE);

CREATE FUNCTION func_GetTasksAging(in p_processId varchar(100),in p_start_date DATE, in p_end_date DATE) 
returns TABLE (
	 processId varchar(100), 
	 agingperiod text, 
	 taskcount int
) 
as  $$
DECLARE
	
BEGIN
	RETURN QUERY 	
		select q1.processId, q1.agingperiod, cast((count(*)) as int) taskcount from
		(select base1.processId, 
		case 
		when base1.days < 7 then '0 - 7 days' 
		when base1.days < 14 then '8 - 14 days' 
		when base1.days < 30 then '15 - 30 days' 
		when base1.days < 60 then '31 - 60 days' 
		else 'Greater than 60 days' end as agingperiod,
		days from
		(select l.processId, fun_CalcWorkdays(cast(t.createdon as Date),cast(coalesce(t.completedon, CURRENT_DATE) as Date)) days 
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		where t.status in ('Created','InProgress','Ready','Reserved')  
		and l.processId=p_processId and l.status=0 and start_date >= p_start_date and start_date<=COALESCE(p_end_date, CURRENT_DATE)) as base1) as q1 
		group by q1.agingperiod, q1.processId;
	END;
$$ LANGUAGE plpgsql;
