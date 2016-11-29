insert into BUser(created,createdBy,updated,updatedBy,firstName,isArchived,lastName,userId,email,password,refId) values (now(),'Administrator',now(), 'Administrator', 'Admin', false, 'Admin','Administrator','mdkimani@gmail.com','pass', 's6o5mvPtV57R23db');
insert into BGroup(created,createdBy,updated,updatedBy,fullName,isArchived,name,refId) values(now(),'Administrator',now(), 'Administrator','Administrator', false, 'ADMIN','ULt0xvgQn0uiMA2m');

insert into UserGroup(userid,groupid) values(1,1);
 
create index on localdocument (lower(subject)); 
create index on localdocument (lower(description));
alter table adfield alter column formid drop not null;

--Case Number Sequence - must always be included
create sequence caseno_sequence increment by 1 minvalue 1 MaxValue 100000000 start with 1;

--INDEXES ADForm_Json
create index idx_adformjson_processRefId on adform_json (processRefId);

--INDEXES ADField_Json
--Optimizing field retrieval (Inner joins with jsonb fields are way slower, so some of the variables have to be added as table cols)
update adfieldjson set formref=field->>'formRef';
create index idx_adfieldjson_formrefid on adfieldjson(formref);

--Optimizing Field Retrieval
update adfieldjson set fieldType=field->>'type';
create index idx_adfieldjson_type on adfieldjson(fieldType);

CREATE INDEX adfieldjson_field_idx
  ON public.adfieldjson
  USING gin
  (field);

-- Index: public.adfieldjson_field_idx1

-- DROP INDEX public.adfieldjson_field_idx1;

CREATE INDEX adfieldjson_field_idx1
  ON public.adfieldjson
  USING gin
  (field jsonb_path_ops);


--FORM JSONB INDEXES ADFIELDJSON--
--f.field->>'formRef'
--field->>'type'
--field ?? 'parentRef'
--field @> '{\"refId\":\":refId\"}'
--refId=':fieldRefId'


--ADFORM_JSON
--form @> '{\"processRefId\":\":processRefId\"}'



	--JSONB INDEXES--
--doctyperefid
--createdby
--processId
