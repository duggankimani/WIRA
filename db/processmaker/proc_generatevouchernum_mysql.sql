drop procedure if exists `proc_generatevouchernum`;

DELIMITER $$
CREATE PROCEDURE proc_generatevouchernum(in p_doctype varchar(45), in p_casenumber varchar(45), in p_startval int)
BEGIN
	declare v_startval int;
	declare v_nextval int;	
    declare v_filler char(5);
	declare v_previousallocatedno varchar(20);
	
	set p_doctype=upper(p_doctype);
	set v_startval = (select startval from doctypeseq where doctype=p_doctype);
	set v_nextval= (select nextval  from doctypeseq where doctype=p_doctype);	
	
	if(p_startval is null) then
		set p_startval = 0;
	end if;
	
	select docno into v_previousallocatedno from docnums where casenumber=p_casenumber and doctype=p_doctype;
	
	if (v_previousallocatedno is null) then
		if(v_startval is null) then
			
			insert into doctypeseq values (p_doctype, p_startval, p_startval+1);
			set v_startval = p_startval;
			set v_nextval = v_startval+1;
			
		elseif(p_startval is not null && p_startval>v_nextval) then
		
			set v_startval = p_startval;
			set v_nextval = v_startval+1;
			update doctypeseq set startval= p_startval where doctype=p_doctype;
			
		else
			set v_nextval = v_nextval+1;
		end if;
		
		update doctypeseq set nextval= v_nextval where doctype=p_doctype;
		
		if(v_nextval<10) then set v_filler = '000';
                elseif (v_nextval<100 ) then set v_filler = '00';
                elseif (v_nextval<1000 ) then set v_filler = '0';
                else set v_filler = '';
		end if;

		insert into docnums values (p_casenumber,p_doctype, concat(p_doctype,'-',v_filler,v_nextval));
		select concat(p_doctype,'-',v_filler,v_nextval) as ponum;
	else
		select v_previousallocatedno as ponum;
	end if;
	
END$$

DELIMITER ;
