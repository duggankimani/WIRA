drop function if exists func_GetTasksWorkfload(varchar,DATE,DATE);

CREATE FUNCTION func_GetTasksWorkfload(in p_processId varchar(100),in p_start_date DATE, in p_end_date DATE) 
returns TABLE (
	 processId varchar(100), 
	 taskname text, 
	 taskcount int, 
	 avgdays decimal,
	 peoplecount int, 
	 peoplenames text
) 
as  $$
DECLARE
	
BEGIN
	RETURN QUERY 	
		select q1.processId, q1.taskname, cast(q1.taskcount as int), q1.avgdays,cast(q2.peoplecount as int), q2.peoplenames from
		(select base1.processId, base1.taskname, count(*) taskcount,avg(base1.days) avgdays from
		(select l.processId, i.text taskname,  
		l.start_date pstart, l.end_date pend, fun_CalcWorkdays(cast(t.createdon as Date),cast(t.completedon as Date)) days 
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		inner join i18ntext i on (i.task_names_id=t.id) 
		where t.status in ('Created','InProgress','Ready','Reserved')
		and l.status=0 
		and start_date >= cast(p_start_date as DATE) and start_date<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE) 
		and l.processId=p_processId) as base1 
		group by base1.processId,base1.taskname) as q1  
		
		inner join
		
		(select base1.processId, base1.taskname, count(distinct(base1.entity_id)) peoplecount, string_agg(distinct(base1.peoplenames), ',') peoplenames from
		(select l.processId, i.text taskname, p.entity_id, concat(u.lastname,' ', u.firstname) peoplenames
		from Task t 
		inner join processinstancelog l 
		on (l.processinstanceid=t.processinstanceid) 
		inner join i18ntext i on (i.task_names_id=t.id) 
		inner join peopleassignments_potowners p on (p.task_id=t.id) 
		inner join buser u on (p.entity_id=u.userid) 
		where t.status in ('Created','InProgress','Ready','Reserved') 
		and l.status=0 and start_date >= cast(p_start_date as DATE) and start_date<=COALESCE(cast(p_end_date as  DATE), CURRENT_DATE) 
		and u.isActive=1 and l.processId=p_processId) as base1 
		group by base1.processId,base1.taskname) as q2 
		on (q1.processId=q2.processId and q1.taskname=q2.taskname);
	END;
$$ LANGUAGE plpgsql;
