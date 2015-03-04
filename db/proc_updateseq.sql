drop function if exists proc_updateseq(varchar);

CREATE FUNCTION proc_updateseq(in p_tablename varchar(100))
returns void as  $$
DECLARE

v_nextval int;
BEGIN
        EXECUTE 'select max(id) from '|| p_tablename INTO v_nextval;
        PERFORM setval(p_tablename||'_id_seq',v_nextval);
END;
$$ LANGUAGE plpgsql;

select proc_updateseq('buser');
select proc_updateseq('bgroup');
select proc_updateseq('adform');
select proc_updateseq('adfield');
select proc_updateseq('adproperty');
select proc_updateseq('advalue');
select proc_updateseq('processdefmodel');
select proc_updateseq('addoctype');
select proc_updateseq('adkeyvaluepair');
select proc_updateseq('adoutputdoc');
select proc_updateseq('adtrigger');
select proc_updateseq('taskstepmodel');
select proc_updateseq('adtasksteptrigger');
select proc_updateseq('datasourceconfig');
select proc_updateseq('localattachment');
