drop function if exists func_GetTasksTrend(varchar,TIMESTAMP,TIMESTAMP, varchar, int);

CREATE FUNCTION func_GetTasksTrend(in p_processId varchar(100),in p_start_date TIMESTAMP, in p_end_date TIMESTAMP, in p_period varchar(50), in actiontype int) 
returns TABLE (
	 processId varchar(100), 
	 period int,
	 taskcount int
) 
as  $$
DECLARE
	
BEGIN
	if(actiontype=0) then
		--initialization/ start
		RETURN QUERY 	
			select base1.processId,base1.period, cast(count(*) as int) taskcount  from 
			(select l.processId, fun_extractPeriod(cast(t.createdon as TIMESTAMP), p_period) period 
			from Task t 
			inner join processinstancelog l 
			on (l.processinstanceid=t.processinstanceid) 
			where l.processId=p_processId  
			and t.createdon >= cast(p_start_date as DATE) 
			and t.createdon<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE)) as base1 
			group by base1.processId, base1.period;
		
	else
		--completion
		RETURN QUERY 	
			select base1.processId,base1.period, cast(count(*) as int) taskcount  from 
			(select l.processId, fun_extractPeriod(cast(t.completedon as TIMESTAMP), p_period) period 
			from Task t 
			inner join processinstancelog l 
			on (l.processinstanceid=t.processinstanceid) 
			where l.processId=p_processId  
			and t.completedon >= cast(p_start_date as DATE) and t.completedon<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE)) as base1 
			group by base1.processId, base1.period;
			
		end if;
	END;
$$ LANGUAGE plpgsql;
