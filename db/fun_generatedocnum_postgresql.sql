drop function if exists proc_generatedocnum(varchar(45),varchar(45));

CREATE FUNCTION proc_generatedocnum(in p_doctype varchar(45), in p_casenumber varchar(45)) 
returns text as  $$
DECLARE

v_startval int;
v_nextval int;	
v_filler text;
v_previousallocatedno varchar(20);

BEGIN
	p_doctype=upper(p_doctype);
	v_startval = (select startval from doctypeseq where doctype=p_doctype);
	v_nextval= (select nextval  from doctypeseq where doctype=p_doctype);	
	
	select docno into v_previousallocatedno from docnums where casenumber=p_casenumber and doctype=p_doctype;
	
	if (v_previousallocatedno is null) then
		if(v_startval is null) then
			
			insert into doctypeseq values (p_doctype, 0,1);
			v_startval = 0;
			v_nextval = v_startval+1;
		else
			v_nextval = v_nextval+1;
			update doctypeseq set nextval= v_nextval where doctype=p_doctype;
		end if;
		
		if(v_nextval<10) then v_filler = '000';
                elsif (v_nextval<100 ) then v_filler = '00';
                elsif (v_nextval<1000 ) then v_filler = '0';
                else v_filler = '';
		end if;

		--RAISE INFO 'p_doctype = %, v_filler=%, v_nextval=%',p_doctype,v_filler,v_nextval;
		insert into docnums values (p_casenumber,p_doctype, concat(p_doctype,'-',v_filler,v_nextval));
		RETURN concat(p_doctype,'-',v_filler,v_nextval) as ponum;
	else
		RETURN v_previousallocatedno as ponum;
	end if;
	
END;
$$ LANGUAGE plpgsql;
