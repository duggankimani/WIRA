alter table adtrigger alter column script type text;

CREATE TABLE buser (
    id bigint NOT NULL,
    created timestamp without time zone,
    createdby character varying(255),
    isactive integer DEFAULT 1,
    refid character varying(45) NOT NULL,
    updated timestamp without time zone,
    updatedby character varying(255),
    email character varying(100),
    firstname character varying(255) NOT NULL,
    isarchived boolean NOT NULL,
    lastname character varying(255) NOT NULL,
    password character varying(255),
    userid character varying(255) NOT NULL,
    orgid bigint,
    pictureurl character varying(255),
    refreshtoken character varying(255)
);
ALTER TABLE buser OWNER TO postgres;

--
-- Name: buser_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE buser_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE buser_id_seq OWNER TO postgres;

--
-- Name: buser_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE buser_id_seq OWNED BY buser.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser ALTER COLUMN id SET DEFAULT nextval('buser_id_seq'::regclass);


--
-- Name: buser_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_pkey PRIMARY KEY (id);


--
-- Name: buser_refid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_refid_key UNIQUE (refid);


--
-- Name: buser_userid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT buser_userid_key UNIQUE (userid);


--
-- Name: uk_re2sfgtejo19tdfb4sq3mqwwq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY buser
    ADD CONSTRAINT uk_re2sfgtejo19tdfb4sq3mqwwq UNIQUE (userid);


insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass', 's6o5mvPtV57R23db');

CREATE TABLE bgroup (
    id bigint NOT NULL,
    created timestamp without time zone,
    createdby character varying(255),
    isactive integer DEFAULT 1,
    refid character varying(45) NOT NULL,
    updated timestamp without time zone,
    updatedby character varying(255),
    fullname character varying(255),
    isarchived boolean NOT NULL,
    name character varying(255) NOT NULL
);
ALTER TABLE bgroup OWNER TO postgres;

--
-- Name: bgroup_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bgroup_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bgroup_id_seq OWNER TO postgres;

--
-- Name: bgroup_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bgroup_id_seq OWNED BY bgroup.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup ALTER COLUMN id SET DEFAULT nextval('bgroup_id_seq'::regclass);


--
-- Name: bgroup_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_name_key UNIQUE (name);


--
-- Name: bgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_pkey PRIMARY KEY (id);


--
-- Name: bgroup_refid_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT bgroup_refid_key UNIQUE (refid);


--
-- Name: uk_5spukcbu0ojd3lku4asyarbq9; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bgroup
    ADD CONSTRAINT uk_5spukcbu0ojd3lku4asyarbq9 UNIQUE (name);


--
-- PostgreSQL database dump complete
--

insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','Administrator', false, 'ADMIN','ULt0xvgQn0uiMA2m');


CREATE TABLE usergroup (
    userid bigint NOT NULL,
    groupid bigint NOT NULL
);
ALTER TABLE usergroup OWNER TO postgres;

--
-- Name: fk8a5be154220bf979; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usergroup
    ADD CONSTRAINT fk8a5be154220bf979 FOREIGN KEY (groupid) REFERENCES bgroup(id);


--
-- Name: fk8a5be15473e396d1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usergroup
    ADD CONSTRAINT fk8a5be15473e396d1 FOREIGN KEY (userid) REFERENCES buser(id);


--
-- PostgreSQL database dump complete
--

insert into UserGroup(userid,groupid) values(1,1);

--Case Number Sequence - must always be included
create sequence caseno_sequence increment by 1 minvalue 1 MaxValue 100000000 start with 1;

create sequence permission_id_seq;
CREATE TABLE permission (
    id bigint DEFAULT nextval('permission_id_seq'::regclass) NOT NULL,
    createddate date DEFAULT ('now'::text)::date NOT NULL,
    isactive integer DEFAULT 1,
    updated timestamp without time zone,
    updatedby character varying(255),
    description character varying(255),
    name character varying(255) NOT NULL
);

--
-- Name: permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- Name: uk_2ojme20jpga3r4r79tdso17gi; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY permission
    ADD CONSTRAINT uk_2ojme20jpga3r4r79tdso17gi UNIQUE (name);


--
-- Name: idx_permission_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_permission_name ON permission USING btree (name);


--
-- PostgreSQL database dump complete
--



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
	


CREATE TABLE role_permissions (
    roleid bigint NOT NULL,
    permissionid bigint NOT NULL
);


ALTER TABLE role_permissions OWNER TO postgres;

--
-- Name: role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (roleid, permissionid);


--
-- Name: fkead9d23b5df964e4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT fkead9d23b5df964e4 FOREIGN KEY (permissionid) REFERENCES permission(id);


--
-- Name: fkead9d23bd965e1b0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role_permissions
    ADD CONSTRAINT fkead9d23bd965e1b0 FOREIGN KEY (roleid) REFERENCES bgroup(id);


--
-- PostgreSQL database dump complete
--
insert into role_permissions(permissionid,roleid) values 
((select id from permission where name='ACCESSMGT_CAN_VIEW_ACCESSMGT'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_USER'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_ROLE'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='ACCESSMGT_CAN_EDIT_UNITS'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='SETTINGS_CAN_VIEW'),(select id from bgroup where name='ADMIN')),
((select id from permission where name='SETTINGS_CAN_EDIT'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='PROCESSES_CAN_VIEW_PROCESSES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='PROCESSES_CAN_EDIT_PROCESSES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='UNASSIGNED_CAN_REASSIGN_UNASSIGNEDTASKS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='DASHBOARDS_CAN_VIEW_DASHBOARDS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='DATATABLES_CAN_VIEW_DATATABLES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='DATATABLES_CAN_EDIT_DATATABLES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='MAILLOG_CAN_VIEW_MAILLOG'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='MAILLOG_CAN_RESEND_MAILS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='DATASOURCES_CAN_VIEW_DATASOURCES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='DATASOURCES_CAN_EDIT_DATASOURCES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='REPORTS_CAN_VIEW_REPORTS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='REPORTS_CAN_EXPORT_REPORTS'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='CASEREGISTRY_CAN_VIEW_CASES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='FILEVIEWER_CAN_VIEW_FILES'),(select id from bgroup where name='ADMIN')),
	((select id from permission where name='FILEVIEWER_CAN_DOWNLOAD_FILES'),(select id from bgroup where name='ADMIN'))
; 

