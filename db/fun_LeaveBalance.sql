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
	v_months_to_deduct integer;
BEGIN
    
	
	v_daysallocated = 0;
	v_daysearned = 0;
	
	--allocation
	select days into v_daysallocated from ext_leavetype where leavetype=p_leavetype;
	
	select case when EXTRACT(YEAR FROM appointmentdate) = EXTRACT(YEAR FROM current_date) then EXTRACT(MONTH FROM appointmentdate) else 0 end into v_months_to_deduct from ext_kam_staff_list where email=p_userid;
	
	--leave days taken
	select fun_LeaveDaysTaken(p_userid, p_leavetype) into v_leavedays_taken;
	v_leavedays_taken = coalesce(v_leavedays_taken,0);
	v_daysallocated = coalesce(v_daysallocated, 0);
	
	if(p_leavetype='Annual Leave') then
		if(v_months_to_deduct = 0) then
		  v_months_to_deduct = 1;
		end if;
		
		v_daysearned = 1.75 * (SELECT DATE_PART('month', current_date::timestamp)- v_months_to_deduct);
		if(v_daysearned < 0) then
			v_daysearned = 0;
		end if;
		
		--opening balances
		select coalesce(leave_taken, 0), coalesce(days_to_utilize,0), coalesce(balance_from_previous_year,0) 
		into v_openingleavetaken,v_leavedaystoutilize,v_balance_from_previous_year 
		from ext_kam_leave_master where email = p_userid;
		
		v_leavedays_taken = coalesce(v_leavedays_taken,0) + coalesce(v_openingleavetaken,0);
		v_daysallocated = coalesce(v_daysallocated,0) + coalesce(v_balance_from_previous_year,0);
	end if;
	
	RAISE NOTICE 'v_daysallocated = % , v_leavedays_taken = % , v_daysearned = %, v_balance_from_previous_year = % ', v_daysallocated,v_leavedays_taken,v_daysearned, v_balance_from_previous_year;
	
	v_daysearned = coalesce(v_daysearned,0);
	v_leavebalance = v_daysallocated - v_leavedays_taken;
	v_balance_from_previous_year = coalesce(v_balance_from_previous_year, 0);
	RETURN QUERY select v_leavebalance,v_daysallocated,v_leavedays_taken,v_daysearned, v_balance_from_previous_year;
END;
$$ LANGUAGE plpgsql;


	