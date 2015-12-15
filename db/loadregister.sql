select d.subject,concat(u1.firstname,' ' ,u1.lastname) createdby, l.processinstanceid,l.start_date,l.end_date,l.processid, l.status processstatus,t.id taskid,t.status,concat(u.firstname,' ' ,u.lastname) taskowner,(select array_agg(entity_id) from peopleassignments_potowners where task_id=t.id) potowners
  from processinstancelog l inner join task t on (t.processinstanceid=l.processinstanceid and t.status!='Completed') inner join localdocument d on (d.processinstanceid=l.processinstanceid)  left join buser u on (u.userid=t.actualowner_id) left join buser u1 on (u1.userid=d.createdby) order by l.processinstanceid;