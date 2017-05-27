drop function if exists fun_CalcWorkdays(DATE,DATE);

CREATE FUNCTION fun_CalcWorkdays(in p_start_date DATE, in p_end_date DATE) 
returns int as  $$
DECLARE
	v_count INT;
BEGIN
	select count(*) INTO v_count AS count_days_no_weekend 
	FROM generate_series(CAST(p_start_date as DATE) + 1, COALESCE(CAST(p_end_date as DATE), CURRENT_DATE), '1 day') d(the_day)
	WHERE  extract('ISODOW' FROM the_day) < 6 ;
	/*
	 * except select holiday_date from holiday_table
	 */
	
	return v_count;
END;
$$ LANGUAGE plpgsql;
