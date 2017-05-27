drop function if exists fun_GetProcessSummary(DATE,DATE);

CREATE FUNCTION fun_GetProcessSummary(in p_start_date DATE, in p_end_date DATE) 
returns TABLE (
processId varchar(50),
name varchar(50), 
refId varchar(50), 
inprogress int, 
completed int, 
overdue int,
avgtot decimal, 
targetDays int
) 
as  $$
DECLARE
	
BEGIN
	RETURN QUERY 	
		select base1.processId, processes.name, processes.refId, cast(coalesce(base1.inprogress, 0) as int),
		cast(coalesce(base1.completed,0) as int), cast(coalesce(overdue.overdue,0) as int), 
		cast(coalesce(tot.tot,0) as decimal), cast(coalesce(targets.targetDays,0) as int)
		from (
		select q2.processId,sum(q2.inprogress) inprogress, sum(q2.completed) completed 
		from(
		select q1.processId, 
		case when q1.status=0 then q1.total end as inprogress,
		case when q1.status=2 then q1.total end as completed from (
		select count(*) total, i.status,i.processId from processinstancelog i where i.start_date >= p_start_date and i.start_date<=COALESCE(p_end_date, CURRENT_DATE) group by i.processId,i.status) as q1) as q2 group by q2.processId) as base1
		inner join 
		(select ai.processId, avg(fun_CalcWorkdays(cast(ai.start_date as Date), cast(ai.end_date as Date))) tot from processinstancelog ai
		where ai.start_date >= p_start_date and ai.start_date<=COALESCE(p_end_date, CURRENT_DATE) group by ai.processId) as tot 
		on (tot.processId=base1.processId) 
		inner join 
		(select p1.processId, p1.targetDays from processdefmodel p1) as targets 
		on (targets.processId=base1.processId) 
		inner join 
		(select d.name, d.processid, d.refId from processdefmodel d) as processes 
		on (processes.processid = base1.processId)
		left join 
		(select targets.processId, count(*) overdue from (
		select l.processId, fun_CalcWorkdays(cast(start_date as Date),cast(end_date as Date)) days, p.targetDays from processinstancelog l 
		inner join processdefmodel p on l.processId=p.processId 
		where l.status=0 and start_date >= p_start_date and start_date<=COALESCE(p_end_date, CURRENT_DATE)) as targets 
		where targets.days > targets.targetDays 
		group by targets.processId) as overdue
		on (overdue.processId=base1.processId); 
		
END;
$$ LANGUAGE plpgsql;
