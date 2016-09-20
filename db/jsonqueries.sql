-- Postgresql 9.4 Json Queries - 101


select id, refid from localdocument order by id;
select * from adform_json;
select * from adfieldjson;

update taskstepmodel t set formref=(select refid from adform where id=t.formid) where t.formid is not null and formref is null;
select formref,outputdocid from taskstepmodel;
select form from adform_json where refid='SpOf8IzZHyu1UAvC';

select * from adfieldjson where field @> '{"name":"genericForm"}';

select field from adfieldjson where field @> '{"formRef":"8fpbo0WvWAuqo0hV"}' and not field ? 'parentRef';

select field->'parentRef',adfieldjson.* from adfieldjson where field @> '{"parentRef":"WTpNXCQ8N2gY7iCs"}';
select field->'parentRef',adfieldjson.* from adfieldjson where field @> '{"formRef":"8fpbo0WvWAuqo0hV"}' and field ? 'parentRef';
select field->'parentRef',adfieldjson.* from adfieldjson where field @> '{"formRef":"8fpbo0WvWAuqo0hV"}' and not field ? 'parentRef';
select field->'parentRef',adfieldjson.* from adfieldjson where field @> '{"parentRef":"8fpbo0WvWAuqo0hV"}' and not field ? 'parentRef';

--Select value of a top level json key, where the key exists
select field ->> 'parentRef' from adfieldjson where field ? 'parentRef' ;

select field ? 'parentRef' from adfieldjson;
select field from adfieldjson;
select * from adfieldjson where field @> '{"name":"genericForm"}';
select * from adfieldjson where field @> '{"name":"genericForm", "type":"FORM"}';
select * from adfieldjson where field @> '{"props":[{"key":"HTMLCONTENT"}]}';

select * from adfieldjson where field@>'props' ? 'SQLSELECT'; --Containment fails, only passes for top level keys

select * from adform where refid='14GLPeBHWsdeeU7u';
select id,refid,name from adform order by id;



--READING A SINGLE VALUE IN A JSONOBJECT FROM A JSONARRAY FIELD
http://stackoverflow.com/questions/19568123/query-for-element-of-array-in-json-column
select elem->>'value' formula from adfieldjson,jsonb_array_elements(field->'props') as elem where  field@>'{"formRef":"btXZrGQzR4azoSvC"}' and field@>'{"props":[{"key":"FORMULA"}]}' and elem@> '{"key":"FORMULA"}';

