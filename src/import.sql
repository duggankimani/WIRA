insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Demian', false, 'Calcacuervo','calcacuervo','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Mariano', false, 'MM','mariano','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'James', false, 'Omboyo','james','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Esteban', false, 'Ti','esteban','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Salaboy', false, 'Sb','salaboy','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Gatheru', false, 'Joseph','gatheru','tosh0948@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'TomKim', false, 'Tom','Kimani','tosh0948@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Lumumba', false, 'Patrice','plumumba','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Odonya', false, 'Japheth','jodonya','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Njenga', false, 'Patrick','pnjenga','mdkimani@gmail.com','pass');

insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','User', false, 'USER');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Development', false, 'HOD_DEV');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Finance', false, 'HOD_FIN');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','CEO', false, 'CEO');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD HR', false, 'HOD_HR');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Procurement', false, 'HOD_PROC');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','Procurement Officer', false, 'OFF_PROC');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Operations', false, 'HOD_OPERATIONS');

insert into UserGroup(userid,groupid) values(1,1);
insert into UserGroup(userid,groupid) values(2,2);
insert into UserGroup(userid,groupid) values(3,2);
insert into UserGroup(userid,groupid) values(4,5);
insert into UserGroup(userid,groupid) values(5,3);
insert into UserGroup(userid,groupid) values(6,1);
insert into UserGroup(userid,groupid) values(7,4);
insert into UserGroup(userid,groupid) values(8,6);
insert into UserGroup(userid,groupid) values(9,7);
insert into UserGroup(userid,groupid) values(10,8);

 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-green','Invoice', 'INVOICE');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-blue','Contract', 'CONTRACT');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-win8','Requisition', 'REQUISITION');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-silver-dark','LPO', 'LPO');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-teal','Leave Application', 'LEAVEAPP');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-silver-dark','RFQ', 'RFQ');
 insert into ADDocType(created, createdBy, updated, updatedBy,className, display, name) values (now(),'Administrator',null,null,'color-blue','Form8', 'FORM8');

 
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'HR', 'Human Resources');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'FIN', 'Finance');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'PROC', 'Procurement');
 insert into ADKeyValuePair(created, createdBy, updated, updatedBy, referenceType, name, displayValue) values (now(),'Administrator',null,null,'DEPARTMENT', 'CONSTR', 'Construction');
 
 create index on localdocument (lower(subject)); 
alter table adfield alter column formid drop not null;