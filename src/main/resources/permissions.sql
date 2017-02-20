CREATE TABLE permission (
    id bigint DEFAULT nextval('permission_id_seq'::regclass) NOT NULL,
    createddate date DEFAULT ('now'::text)::date NOT NULL,
    isactive integer DEFAULT 1,
    updated timestamp without time zone,
    updatedby character varying(255),
    description character varying(255),
    name character varying(255) NOT NULL
);

create sequence permission_id_seq;
alter table permission alter id set default nextval('permission_id_seq');

delete from permission;

insert into permission(name,description) 
values ('ACCESSMGT_CAN_VIEW_ACCESSMGT','Can view access management'),
	('ACCESSMGT_CAN_EDIT_USER','Can Create/Edit User'),
	('ACCESSMGT_CAN_EDIT_ROLE','Can Create/Edit Role'),
	('ACCESSMGT_CAN_EDIT_UNITS','Can Create/Edit Units'),
	('PROCESSES_CAN_VIEW_PROCESSES','Can view processes'),
	('PROCESSES_CAN_EDIT_PROCESSES','Can edit processes'),
	('UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS','Can view unassigned tasks'),
	('UNASSIGNED_CAN_REASSIGN_UNASSIGNEDTASKS','Can reassign unassigned tasks'),
	('DASHBOARDS_CAN_VIEW_DASHBOARDS','Can view admin dashboards'),
	('DATATABLES_CAN_VIEW_DATATABLES','Can view data tables'),
	('DATATABLES_CAN_EDIT_DATATABLES','Can edit data tables'),
	('MAILLOG_CAN_VIEW_MAILLOG','Can view mail log'),
	('MAILLOG_CAN_RESEND_MAILS','Can resend emails'),
	('DATASOURCES_CAN_VIEW_DATASOURCES','Can view datasources'),
	('DATASOURCES_CAN_EDIT_DATASOURCES','Can edit datasources'),
	('REPORTS_CAN_VIEW_REPORTS','Can view reports'),
	('REPORTS_CAN_EXPORT_REPORTS','Can export reports'),
	('CASEREGISTRY_CAN_VIEW_CASES','Can view registry'),
	('FILEVIEWER_CAN_VIEW_FILES','Can view files'),
	('FILEVIEWER_CAN_DOWNLOAD_FILES','Can download files'),
	('SETTINGS_CAN_VIEW','Can view system settings'),
	('SETTINGS_CAN_EDIT','Can edit system settings');
	
insert into role_permissions(permissionid,roleid) values 
((select id from permission where name='ACCESSMGT_CAN_VIEW_ACCESSMGT'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_USER'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_ROLE'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_UNITS'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='SETTINGS_CAN_VIEW'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='SETTINGS_CAN_EDIT'),(select id from bgroup where name='ADMIN'))
; 
