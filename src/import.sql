insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Demian', false, 'Calcacuervo','calcacuervo','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Mariano', false, 'MM','mariano','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'James', false, 'Omboyo','james','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Esteban', false, 'Ti','esteban','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Salaboy', false, 'Sb','salaboy','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'Administrator',now(), 'Administrator', 'Gatheru', false, 'Joseph','gatheru','mdkimani@gmail.com','pass');

insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','User', false, 'USER');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Development', false, 'HOD_DEV');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','HOD Finance', false, 'HOD_FIN');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','User', false, 'CEO');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'Administrator',now(), 'Administrator','Administrator', false, 'Admin');

insert into UserGroup(userid,groupid) values(1,1);
insert into UserGroup(userid,groupid) values(2,2);
insert into UserGroup(userid,groupid) values(3,2);
insert into UserGroup(userid,groupid) values(4,5);
insert into UserGroup(userid,groupid) values(5,3);
insert into UserGroup(userid,groupid) values(6,1);
insert into UserGroup(userid,groupid) values(7,4);

 insert into ADDocType(created, createdBy, updated, updatedBy, display, name) values (now(),'Administrator',null,null,'Invoice', 'INVOICE');
 insert into ADDocType(created, createdBy, updated, updatedBy, display, name) values (now(),'Administrator',null,null,'Contract', 'CONTRACT');
 insert into ADDocType(created, createdBy, updated, updatedBy, display, name) values (now(),'Administrator',null,null,'Requisition', 'REQUISITION');
 insert into ADDocType(created, createdBy, updated, updatedBy, display, name) values (now(),'Administrator',null,null,'LPO', 'LPO');
 insert into ADDocType(created, createdBy, updated, updatedBy, display, name) values (now(),'Administrator',null,null,'Leave Application', 'LEAVEAPP');
