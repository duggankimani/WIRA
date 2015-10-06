insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Demian', false, 'Calcacuervo','calcacuervo','mdkimani@gmail.com','pass','gNtLJ03iEfS3LCac');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Mariano', false, 'MM','mariano','mdkimani@gmail.com','pass', 'OYfPsqwsz4g8KPTB');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'James', false, 'Omboyo','james','mdkimani@gmail.com','pass', 'omy4w7bRRKEUFP9Z');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass', 's6o5mvPtV57R23db');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Esteban', false, 'Ti','esteban','mdkimani@gmail.com','pass', 'vNVwdr1zhkH3HHWI');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Salaboy', false, 'Sb','salaboy','mdkimani@gmail.com','pass', 'PEz4bCPfLneR1GyZ');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Gatheru', false, 'Joseph','gatheru','tosh0948@gmail.com','pass', 'J54AuidkhgE0T2gT');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'TomKim', false, 'Tom','Kimani','tosh0948@gmail.com','pass', '8Kc7aOyRNJbl1jGN');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Njenga', false, 'Patrick','pnjenga','mdkimani@gmail.com','pass', 'EaTEcSR9jYWykgEd');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Odonya', false, 'Japheth','jodonya','mdkimani@gmail.com','pass', 'nVib9DssqUFhLyGo');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Lumumba', false, 'Patrice','plumumba','mdkimani@gmail.com','pass', 'TdcSoNg3c08wpHcB');

insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','User', false, 'USER', 'CQuyDBuiNLZPwPRX');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','HOD Development', false, 'HOD_DEV','SGNhh6k6EZBwoZve');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','HOD Finance', false, 'HOD_FIN','RNi1OCTOGYjWt1DX');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','Administrator', false, 'ADMIN','ULt0xvgQn0uiMA2m');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','CEO', false, 'CEO','w3zjybRXOjK4yorj');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','HOD HR', false, 'HOD_HR','KxXoIKlen2PmIMIZ');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','HOD Procurement', false, 'HOD_PROC','wWVHshBawH6s9Tx6');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','Procurement Officer', false, 'OFF_PROC','tsaibj3EA1lD2H4k');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','HOD Operations', false, 'HOD_OPERATIONS','2nPakBHHHruCD226');

insert into UserGroup(userid,groupid) values(1,1);
insert into UserGroup(userid,groupid) values(2,2);
insert into UserGroup(userid,groupid) values(3,2);
insert into UserGroup(userid,groupid) values(4,4);
insert into UserGroup(userid,groupid) values(5,3);
insert into UserGroup(userid,groupid) values(6,1);
insert into UserGroup(userid,groupid) values(7,5);
insert into UserGroup(userid,groupid) values(8,6);
insert into UserGroup(userid,groupid) values(9,7);
insert into UserGroup(userid,groupid) values(10,8);
insert into UserGroup(userid,groupid) values(11,9);

 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-green','Invoice', 'INVOICE',0,'INV/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-blue','Contract', 'CONTRACT',0,'CNT/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Requisition', 'REQUISITION',0,'REQ/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-silver-dark','LPO', 'LPO',0,'LPO/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-teal','Leave Application', 'LEAVEAPP',0,'Leave/{No}/{MM}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-silver-dark','RFQ', 'RFQ',0,'RFQ/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-blue','Form8', 'FORM8',0,'REG/FRM8/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Loan Application', 'FACILITYAPPLICATIONFORM',0,'L51/CBLC/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Account Application', 'AccountApplication',0,'ACC/CBLC/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Asset Finance Application', 'ASSETFINANCE',0,'AFN/CBLC/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Requisition -WD', 'WDRequisition',0,'Req/WD/{No}/{YY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Student Leave', 'StudentLeave',0,'BUS-AFI-{No}/{YYYY}');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-blue','KINGA YA MKULIMA Application Form', 'KINGAYAMKULIMA',0,'BA/KM/{No}');
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','MPESAIPN', 'MPESAIPN',0,'MPESA_IPN#{No}');
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Terminal Allocation', 'TERMINAL ALLOCATION',0,'Allocation Request #{No}');
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name, lastnum, subjectformat) values (now(),'Administrator',null,null,'color-win8','Terminal De-allocation', 'TERMINAL DEALLOCATION',0,'De-Allocation Request #{No}');
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Asset Finance Application I&M', 'ASSETFINANCEFORMIM');

insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Asset Accession and Publication Workflow', 'ASSETACCESSION');

--Default numbering uses a sequence producing case numbers as: Case-XYZ [this is activated iff subjectformat=null)
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Procurement To Payment Process', 'PROCURETOPAY');
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Imprest Request', 'IMPRESTREQUEST');
 
insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Entry Permit Application', 'ENTRYPERMIT');

 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'HR', 'Human Resources');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'FIN', 'Finance');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'PROC', 'Procurement');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'CONSTR', 'Construction');
 
create index on localdocument (lower(subject)); 
create index on localdocument (lower(description));
alter table adfield alter column formid drop not null;


/*A fix for ADProperties Bug - Multiple Repeated properties for a single Field specifically grid columns - Repeated as the grid is moved around*/
delete from advalue where propertyid is not null and propertyid in(select id from adproperty where fieldid is not null and id not in (select max(id) from adproperty where fieldid is not null group by fieldid,name order by fieldid));
delete from adproperty where fieldid is not null and id in(select id from adproperty where fieldid is not null and id not in (select max(id) from adproperty where fieldid is not null group by fieldid,name order by fieldid));
delete from advalue where documentid is not null and fieldname is not null and id not in (select max(id) from advalue where documentid is not null and fieldname is not null  group by documentid,fieldname);

create index idx_propertyid on advalue(propertyid);
create index idx_documentid on advalue(documentid);

--Case Number Sequence - must always be included
create sequence caseno_sequence increment by 1 minvalue 1 MaxValue 100000000 start with 1;
