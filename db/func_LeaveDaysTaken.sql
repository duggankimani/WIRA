drop function if exists fun_LeaveDaysTaken(varchar, varchar);

CREATE FUNCTION fun_LeaveDaysTaken(in p_userid varchar, in p_leavetype varchar) 
returns numeric as $$
DECLARE
 v_leavedaystaken numeric;
BEGIN
    select sum(coalesce(noofdays)) into v_leavedaystaken 
    from kam_leave_listings_typed where applicant= p_userid  
    and leavecategory=p_leavetype 
	and applicationdate > TO_DATE(concat(extract(YEAR from current_date), '-01-01'),'YYYY-MM-DD');
	
	return v_leavedaystaken;
END;
$$ LANGUAGE plpgsql;