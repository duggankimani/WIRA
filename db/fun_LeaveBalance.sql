drop function if exists fun_LeaveBalance(varchar, varchar);

CREATE FUNCTION fun_LeaveBalance(in p_userid varchar, in p_leavetype varchar) 
returns TABLE (
	 r_balance decimal, 
	 r_daysallocated decimal, 
	 r_leavedaystaken decimal,
	 r_daysearned decimal,
	 r_prevyearbal decimal
) as $$
DECLARE
	v_daysearned decimal;
	v_leavebalance decimal;	
	v_daysallocated decimal;
	v_leavedays_taken decimal;
	v_openingleavetaken decimal;
	v_leavedaystoutilize decimal;
	v_balance_from_previous_year decimal;
BEGIN
    
	
	v_daysallocated = 0;
	v_daysearned = 0;
	
	--allocation
	select days into v_daysallocated from ext_leavetype where leavetype=p_leavetype;
	
	--leave days taken
	select coalesce(sum(days),0) into v_leavedays_taken from ext_kam_leave_report_per_type t 
	inner join ext_leaveapplications a 
	on (t."caseNo" = a."caseNo") where t."leaveCategory"=p_leavetype and t."staffId"=p_userid;
	
	v_leavedays_taken = coalesce(v_leavedays_taken, 0);
	v_daysallocated = coalesce(v_daysallocated, 0);
	
	if(p_leavetype='Annual Leave') then
		v_daysearned = 1.75 * (SELECT DATE_PART('month', current_date::timestamp)-1);
		
		--opening balances
		select coalesce(leave_taken, 0), coalesce(days_to_utilize,0), coalesce(balance_from_previous_year,0) 
		into v_openingleavetaken,v_leavedaystoutilize,v_balance_from_previous_year 
		from ext_kam_leave_master where email = p_userid;
		
		v_leavedays_taken = v_leavedays_taken + v_openingleavetaken;
		v_daysallocated = v_daysallocated + v_balance_from_previous_year;
	end if;
	
	RAISE NOTICE 'v_daysallocated = % , v_leavedays_taken = % , v_daysearned = %, v_balance_from_previous_year = % ', v_daysallocated,v_leavedays_taken,v_daysearned, v_balance_from_previous_year;
	
	v_leavebalance = v_daysallocated - v_leavedays_taken;
	
	RETURN QUERY select v_leavebalance,v_daysallocated,v_leavedays_taken,v_daysearned, v_balance_from_previous_year;
END;
$$ LANGUAGE plpgsql;


	