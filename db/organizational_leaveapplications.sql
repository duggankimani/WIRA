select d.createdBy Applicant,d.caseNo CaseNo,d.created ApplicationDate, 
d.data->>'staffName' StaffName,d.data->>'staffNo' StaffNo,
d.data->>'unit' Unit,d.data->>'nameOfActingOfficer' Actingofficer,
l.data->>'leaveCategory' LeaveCategory, l.data->>'beginning' Beginning,  
l.data->>'ending' Ending, l.data->>'days' NoOfDays,
concat(d.doc->'taskActualOwner'->>'surname',' ', d.doc->'taskActualOwner'->>'name') CurrentApprover, 
d.doc->>'currentTaskName' CurrentStep 
from documentjson d 
inner join documentlinejson l on (l.docrefid = d.refId) 
where d.processid='org.kam.hr.LeaveApplication' 
and d.status!='DRAFTED' 
and d.createdBy!='Administrator' 
and l.name='leaveDetailsGrid' 	
order by d.createdby, d.created;