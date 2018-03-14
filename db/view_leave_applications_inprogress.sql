CREATE OR REPLACE VIEW view_leave_applications_inprogress as 
(SELECT d.createdby AS applicant,
    d.caseno,
    to_char(d.created, 'DD-MM-YYYY HH24:MI'::text) AS applicationdate,
    d.data ->> 'staffName'::text AS staffname,
    d.data ->> 'staffNo'::text AS staffno,
    d.data ->> 'unit'::text AS unit,
    d.data ->> 'nameOfActingOfficer'::text AS actingofficer,
    l.data ->> 'leaveCategory'::text AS leavecategory,
    to_date(l.data ->> 'beginning'::text, 'dd-mm-YYYY') AS beginning,
    to_date(l.data ->> 'ending'::text, 'dd-mm-YYYY')  AS ending,
    l.data ->> 'days'::text AS noofdays,
    l.data ->> 'balance'::text AS balanceBefore,
    cast(l.data ->> 'balance' as decimal(5,2)) - cast(l.data ->> 'days' as decimal(5,2)) balanceAfter,
    concat((d.doc -> 'taskActualOwner'::text) ->> 'surname'::text, ' ', (d.doc -> 'taskActualOwner'::text) ->> 'name'::text) AS currentapprover,
    d.doc ->> 'currentTaskName'::text AS currentstep
   FROM documentjson d
     JOIN documentlinejson l ON l.docrefid::text = d.refid::text
     inner join processinstancelog p on (p.processinstanceid=d.processinstanceid) 
  WHERE 
  d.processid::text = 'org.kam.hr.LeaveApplication'::text 
  AND p.status=0 
  AND d.createdby::text <> 'Administrator'::text 
  AND l.name::text = 'leaveDetailsGrid'::text 
  AND d.created > TO_DATE(concat(extract(YEAR from current_date), '-01-01'),'YYYY-MM-DD') 
  ORDER BY d.createdby)