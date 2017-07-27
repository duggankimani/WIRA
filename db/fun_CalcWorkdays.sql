drop function if exists fun_CalcWorkdays(DATE,DATE);

CREATE FUNCTION fun_CalcWorkdays(in p_start_date DATE, in p_end_date DATE) 
returns int as  $$
DECLARE
	v_count INT;
BEGIN
    
	select count(*) into v_count from
	(select the_day 
	FROM generate_series(CAST(p_start_date as DATE), COALESCE(CAST(p_end_date as DATE), CURRENT_DATE), '1 day') d(the_day) 
	except select to_date("date", 'DD/MM/YYYY') from ext_kenya_holidays) as days  
	WHERE extract('ISODOW' FROM the_day) < 6;
        
	return v_count;
END;
$$ LANGUAGE plpgsql;


	