 
drop function if exists func_RoundRobinAssignment(bigint,varchar(100),varchar(255),varchar[]);

CREATE FUNCTION func_RoundRobinAssignment(in p_taskid bigint, in p_taskname varchar(100),
in p_processid varchar(255), in p_potOwners varchar[] ) RETURNS text AS $$
DECLARE
v_assigneeid varchar(60);
v_lastAssigneeId varchar(60);
v_potAssignees varchar[];
v_idx int;
BEGIN

	 select t.actualowner_id into v_lastAssigneeId from task t 
	 left join i18ntext i on (i.task_names_id=t.id) 
	 where t.id= (select max(t1.id) from task t1 
	 inner join i18ntext i2 on (i2.task_names_id=t1.id) 
	 where i2.text= p_taskname 
	 and t1.processid= p_processid 
	 and t1.id<>p_taskid and t.actualowner_id is not null);
	 RAISE NOTICE 'Last Assignee= %',v_lastAssigneeId;
	 
	select array(select u.userid from buser u inner join usergroup ug on (ug.userid=u.id) 
	inner join bgroup g on (ug.groupid=g.id) where g.name 
	in (select * from unnest(p_potOwners)) order by u.created)
	into v_potAssignees;
	
	v_idx=1;
	if(array_length(v_potAssignees,1)!=0) then
		if(v_lastAssigneeId is not null) then
			
			FOREACH v_assigneeid in ARRAY v_potAssignees
			LOOP
				--RAISE NOTICE 'Pot Assignee= %',v_assigneeid;
				if(v_potAssignees[v_idx]=v_lastAssigneeId) then
					if(v_idx=array_length(v_potAssignees,1)) then
						v_assigneeid= v_potAssignees[1];
					else
						v_assigneeid = v_potAssignees[v_idx+1];
					end if;
				end if;
				v_idx=v_idx+1;
			END LOOP;
			
		else
			--RAISE NOTICE 'Last Assignee was null defaulting to first assignee = %',v_assigneeid;
			v_assigneeid= v_potAssignees[1];
		end if;
	end if;

		
    RETURN v_assigneeid;
END;
$$ LANGUAGE plpgsql;
