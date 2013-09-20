insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Demian', false, 'Calcacuervo','calcacuervo','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Mariano', false, 'MM','mariano','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'James', false, 'Omboyo','james','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Esteban', false, 'Ti','esteban','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Salaboy', false, 'Sb','salaboy','mdkimani@gmail.com','pass');
insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password) values (now(),'calcacuervo',now(), 'calcacuervo', 'Gatheru', false, 'Joseph','gatheru','mdkimani@gmail.com','pass');

insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'calcacuervo',now(), 'calcacuervo','User', false, 'USER');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'calcacuervo',now(), 'calcacuervo','HOD Development', false, 'HOD_DEV');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'calcacuervo',now(), 'calcacuervo','HOD Finance', false, 'HOD_FIN');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'calcacuervo',now(), 'calcacuervo','User', false, 'CEO');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name) values(now(),'calcacuervo',now(), 'calcacuervo','Administrator', false, 'Admin');

insert into UserGroup(userid,groupid) values(1,1);
insert into UserGroup(userid,groupid) values(2,2);
insert into UserGroup(userid,groupid) values(3,2);
insert into UserGroup(userid,groupid) values(4,5);
insert into UserGroup(userid,groupid) values(5,3);
insert into UserGroup(userid,groupid) values(6,1);
insert into UserGroup(userid,groupid) values(7,4);