CREATE OR REPLACE VIEW kam_leave_listings AS 
SELECT DISTINCT ON (applicant,caseno,leavecategory) * from(
SELECT d.createdby AS applicant,
    d.caseno,
    to_char(d.created, 'DD-MM-YYYY HH24:MI'::text) AS applicationdate,
    d.data ->> 'staffName'::text AS staffname,
    d.data ->> 'staffNo'::text AS staffno,
    d.data ->> 'unit'::text AS unit,
    d.data ->> 'nameOfActingOfficer'::text AS actingofficer,
    l.data ->> 'leaveCategory'::text AS leavecategory,
    l.data ->> 'beginning'::text AS beginning,
    l.data ->> 'ending'::text AS ending,
    l.data ->> 'days'::text AS noofdays,
    l.data ->> 'balance'::text AS balanceBefore,
    cast(l.data ->> 'balance' as decimal) - cast(l.data ->> 'days' as decimal) balanceAfter,
    concat((d.doc -> 'taskActualOwner'::text) ->> 'surname'::text, ' ', (d.doc -> 'taskActualOwner'::text) ->> 'name'::text) AS currentapprover,
    d.doc ->> 'currentTaskName'::text AS currentstep
   FROM documentjson d
     JOIN documentlinejson l ON l.docrefid::text = d.refid::text
  WHERE d.data @> '{"ceApprovalStatus":"Approve"}' AND
  d.processid::text = 'org.kam.hr.LeaveApplication'::text AND 
  d.status::text <> 'DRAFTED'::text 
  AND d.createdby::text <> 'Administrator'::text 
  AND l.name::text = 'leaveDetailsGrid'::text
  ORDER BY d.createdby) as table1;
