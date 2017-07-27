drop function if exists fun_LeaveDays(varchar,DATE,DATE);

CREATE FUNCTION fun_LeaveDays(in p_leavetype varchar, in p_start_date DATE, in p_end_date DATE) 
returns int as  $$
DECLARE
	v_count INT;
	v_daytype varchar(5);
BEGIN
    
	v_count = 0;
	select daytype into v_daytype from ext_leavetype where leavetype=p_leavetype;
	
	if(v_daytype='WD') then
		select fun_CalcWorkdays(p_start_date, p_end_date) into v_count;
	elsif(v_daytype = 'CD') then
	    SELECT DATE_PART('day', p_end_date::timestamp - p_start_date::timestamp)+1 into v_count;
	end if;
	
	return v_count;
END;
$$ LANGUAGE plpgsql;


	