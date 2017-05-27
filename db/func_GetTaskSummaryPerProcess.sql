drop function if exists fun_GetTaskSummaryPerProcess(varchar,DATE,DATE);

CREATE FUNCTION fun_GetTaskSummaryPerProcess(in p_processId varchar(100),in p_start_date DATE, in p_end_date DATE) 
returns TABLE (
	refId varchar(30),
	ownerid varchar(100),
	fullName text,
	inprogress int,
	completed int, 
	total int,
	overdue int, 
	avg decimal
) 
as  $$
DECLARE
	
BEGIN
	RETURN QUERY 	
		select u.refId,avgtbl.ownerid,case when avgtbl.ownerid='UNASSIGNED' then 'UNASSIGNED' else concat(u.lastname,' ',u.firstname) END fullName, 
		cast(coalesce(counts.inprogress,0) as int) inprogress, cast(coalesce(counts.completed,0) as int) completed, 
		cast(coalesce(counts.inprogress,0)+coalesce(counts.completed,0) as int) total,
		cast(coalesce(overdue.overdue,0) as int) overdue, cast(coalesce(avgtbl.avg,0.0) as decimal) avg 
		from
		(select base1.ownerid, avg(base1.days) avg from 
		(select case when t.actualowner_id is null then 'UNASSIGNED' else t.actualowner_id end as ownerid,
		l.start_date pstart, l.end_date pend, fun_CalcWorkdays(cast(t.createdon as Date),cast(t.completedon as Date)) days 
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		where l.processId=p_processId and 
		l.status=0 and start_date >= cast(p_start_date as DATE) and start_date<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE)
		) as base1 
		group by base1.ownerid) as avgtbl
		
		inner join
		
		(select q3.ownerid, sum(q3.inprogress) inprogress, sum(q3.completed) completed from
		(select q2.ownerid, case when q2.status='InProgress' then q2.count end InProgress, case when q2.status='Completed' then q2.count end Completed from
		(select count(*), base1.ownerid, base1.status  from 
		(select case when t.actualowner_id is null then 'UNASSIGNED' else t.actualowner_id end as ownerid,
		l.processId,
		case when t.status='Created' or t.status='InProgress' or t.status='Ready' or t.status='Reserved' or t.status='Suspended' 
		then 'InProgress'  
		when t.status='Completed' then 'Completed'
		end as status 
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		where l.processId=p_processId and  
		l.status=0 and start_date >= cast(p_start_date as DATE) and start_date<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE)) as base1 
		group by base1.ownerid, base1.status) as q2) as q3
		group by q3.ownerid) as counts 
		on (counts.ownerid=avgtbl.ownerid)
		
		left join 
		
		(select base1.ownerid, count(*) overdue  from 
		(select case when t.actualowner_id is null then 'UNASSIGNED' else t.actualowner_id end as ownerid,
		l.start_date pstart, l.end_date pend, fun_CalcWorkdays(cast(t.createdon as Date),cast(t.completedon as Date)) days 
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		where  l.processId=p_processId and 
		l.status=0 and start_date >= cast(p_start_date as DATE) and start_date<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE)
		) as base1 where base1.days>1 
		group by base1.ownerid) as overdue
		on avgtbl.ownerid=overdue.ownerid 
		
		left join buser u on (u.userid=avgtbl.ownerid);

	END;
$$ LANGUAGE plpgsql;
